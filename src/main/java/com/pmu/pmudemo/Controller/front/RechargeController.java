package com.pmu.pmudemo.Controller.front;

import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.services.RechargeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Schema(name = "RechargeResponseDTO", description = "Réponse détaillée lors de la création d'une recharge, incluant toutes les informations de conversion et de paiement.")
class RechargeResponseDTO {
    public Long id;
    public String operateur;
    public String numeroCible;
    public Double montantAPayer;
    public String devisePaiement;
    public Double montantCarte;
    public String deviseCarte;
    public Double tauxDeChange;
    public Double fraisConversion;
    public String statut;
    public String stripeSessionId;
    public String message;
    public Double commission;
    public String typeCommission;
    public Double commissionBase;
    public RechargeResponseDTO(RechargeTransaction t, String message) {
        this.id = t.getId();
        this.operateur = t.getOperateur();
        this.numeroCible = t.getNumeroCible();
        this.montantAPayer = t.getMontant();
        this.devisePaiement = t.getDevisePaiement();
        this.montantCarte = t.getMontantCarte();
        this.deviseCarte = t.getDeviseCarte();
        this.tauxDeChange = t.getTauxDeChange();
        this.fraisConversion = t.getFraisConversion();
        this.statut = t.getStatut();
        this.stripeSessionId = t.getStripeSessionId();
        this.message = message;
        this.commission = t.getCommission();
        this.typeCommission = t.getTypeCommission();
        this.commissionBase = t.getCommissionBase();
    }
}

@RestController
@RequestMapping("/api/front/recharges")
@Tag(name = "Recharges", description = "Endpoints pour la gestion des recharges et la conversion de devises.")
public class RechargeController {
    private final RechargeService rechargeService;

    public RechargeController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @Operation(
        summary = "Créer une recharge avec conversion de devise",
        description = "Crée une transaction de recharge en appliquant le taux de change et la marge configurée. Retourne toutes les informations de conversion.",
        requestBody = @RequestBody(
            content = @Content(
                mediaType = "application/x-www-form-urlencoded",
                schema = @Schema(type = "object"),
                examples = @ExampleObject(value = "operateur=Orange&numeroCible=21612345678&montantCarte=5&deviseCarte=TND&devisePaiement=EUR")
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Recharge créée avec succès", content = @Content(schema = @Schema(implementation = RechargeResponseDTO.class)))
        }
    )
    @PostMapping
    public ResponseEntity<RechargeResponseDTO> createRecharge(@RequestParam String operateur,
                                                             @RequestParam String numeroCible,
                                                             @RequestParam Double montantCarte,
                                                             @RequestParam String deviseCarte,
                                                             @RequestParam String devisePaiement,
                                                             @RequestParam String pays,
                                                             Authentication authentication) {
        String email = authentication.getName();
        Long userId = rechargeService.getUserRepo().findByEmail(email).orElseThrow().getId();
        RechargeTransaction recharge = rechargeService.createRecharge(userId, operateur, numeroCible, montantCarte, deviseCarte, devisePaiement, pays);
        String message = "Votre demande de recharge a été prise en compte.";
        return ResponseEntity.ok(new RechargeResponseDTO(recharge, message));
    }

    @Operation(
        summary = "Lister mes recharges",
        description = "Retourne l'historique des recharges de l'utilisateur connecté.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Liste des recharges", content = @Content(schema = @Schema(implementation = RechargeTransaction.class)))
        }
    )
    @GetMapping("/me")
    public ResponseEntity<List<RechargeTransaction>> getMyRecharges(Authentication authentication) {
        String email = authentication.getName();
        Long userId = rechargeService.getUserRepo().findByEmail(email).orElseThrow().getId();
        return ResponseEntity.ok(rechargeService.getRechargesByUser(userId));
    }
} 