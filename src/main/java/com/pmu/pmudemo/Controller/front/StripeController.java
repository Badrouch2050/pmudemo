package com.pmu.pmudemo.Controller.front;

import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.repositories.RechargeTransactionRepository;
import com.pmu.pmudemo.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;

import com.pmu.pmudemo.services.TransactionAdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.stripe.model.Charge;
import com.stripe.model.Refund;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/front/stripe")
public class StripeController {
    private final StripeService stripeService;
    private final RechargeTransactionRepository transactionRepo;
    private final TransactionAdminService transactionAdminService;

    @Value("${stripe.webhook-secret}")
    private String stripeWebhookSecret;

    public StripeController(StripeService stripeService, RechargeTransactionRepository transactionRepo, TransactionAdminService transactionAdminService) {
        this.stripeService = stripeService;
        this.transactionRepo = transactionRepo;
        this.transactionAdminService = transactionAdminService;
    }

    @PostMapping("/checkout-session/{transactionId}")
    public ResponseEntity<?> createCheckoutSession(@PathVariable Long transactionId) {
        Optional<RechargeTransaction> transactionOpt = transactionRepo.findById(transactionId);
        if (transactionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Transaction introuvable");
        }
        RechargeTransaction transaction = transactionOpt.get();
        try {
            String url = stripeService.createCheckoutSession(
                    transaction.getMontant(),
                    transaction.getDevisePaiement(),
                    transaction.getId().toString(),
                    transaction.getUser().getEmail()
            );
            return ResponseEntity.ok(url);
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Erreur Stripe: " + e.getMessage());
        }
    }

    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request, @RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook signature verification failed");
        }
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session != null) {
                String transactionId = session.getMetadata().get("transactionId");
                transactionRepo.findById(Long.valueOf(transactionId)).ifPresent(t -> {
                    t.setStatut("PAYE");
                    transactionRepo.save(t);
                    // Validation automatique après paiement
                    transactionAdminService.validateTransaction(t.getId());
                });
            }
        }
        // Paiement échoué
        if ("checkout.session.async_payment_failed".equals(event.getType()) || "checkout.session.expired".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session != null) {
                String transactionId = session.getMetadata().get("transactionId");
                transactionRepo.findById(Long.valueOf(transactionId)).ifPresent(t -> {
                    t.setStatut("ECHEC_PAIEMENT");
                    transactionRepo.save(t);
                    transactionAdminService.notifyFailure(t);
                });
            }
        }
        // Remboursement Stripe
        if ("charge.refunded".equals(event.getType())) {
            Charge charge = (Charge) event.getDataObjectDeserializer().getObject().orElse(null);
            if (charge != null && charge.getPaymentIntent() != null) {
                // Recherche de la transaction par paymentIntent (à condition de stocker l'id Stripe dans la transaction)
                String paymentIntentId = charge.getPaymentIntent();
                transactionRepo.findAll().stream()
                    .filter(t -> paymentIntentId.equals(t.getStripeSessionId()))
                    .findFirst()
                    .ifPresent(t -> {
                        t.setStatut("REMBOURSE");
                        transactionRepo.save(t);
                        // Notifier l'utilisateur du remboursement
                        transactionAdminService.notifyFailure(t); // ou méthode dédiée pour remboursement
                    });
            }
        }
        // Gérer d'autres événements Stripe si besoin
        return ResponseEntity.ok("Webhook reçu");
    }
} 