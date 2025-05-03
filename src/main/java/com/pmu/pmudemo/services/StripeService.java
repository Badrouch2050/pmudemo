package com.pmu.pmudemo.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.repositories.RechargeTransactionRepository;

@Service
public class StripeService {
    @Value("${stripe.api-key}")
    private String stripeApiKey;
    @Value("${stripe.success-url}")
    private String successUrl;
    @Value("${stripe.cancel-url}")
    private String cancelUrl;

    private final RechargeTransactionRepository transactionRepo;

    public StripeService(RechargeTransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public String createCheckoutSession(Double amount, String currency, String transactionId, String email) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(cancelUrl)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setCustomerEmail(email)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setUnitAmount((long) (amount * 100))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Recharge téléphonique CHARGILI")
                                                                .build())
                                                .build())
                                .build())
                .putMetadata("transactionId", transactionId)
                .build();
        Session session = Session.create(params);
        transactionRepo.findById(Long.valueOf(transactionId)).ifPresent(t -> {
            t.setStripeSessionId(session.getId());
            transactionRepo.save(t);
        });
        return session.getUrl();
    }
} 