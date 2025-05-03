package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.repositories.RechargeTransactionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Export", description = "Export des transactions et statistiques (CSV, PDF, etc.)")
@RestController
@RequestMapping("/api/backoffice/export")
public class ExportController {
    private final RechargeTransactionRepository transactionRepo;

    public ExportController(RechargeTransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    @Operation(
        summary = "Exporter toutes les transactions au format CSV",
        description = "Exporte toutes les transactions, y compris le montant de la commission, au format CSV. Les colonnes incluent : ID, Utilisateur, Opérateur, Numéro, Montant, Devise, Statut, Date Demande, Commission.",
        responses = @ApiResponse(responseCode = "200", description = "Fichier CSV généré", content = @Content(mediaType = "text/csv"))
    )
    @GetMapping("/transactions/csv")
    public ResponseEntity<byte[]> exportTransactionsCsv() {
        List<RechargeTransaction> transactions = transactionRepo.findAll();
        String header = "ID,Utilisateur,Opérateur,Numéro,Montant,Devise,Statut,Date Demande,Commission\n";
        String csv = transactions.stream().map(t ->
                t.getId() + ","
                + (t.getUser() != null ? t.getUser().getEmail() : "") + ","
                + t.getOperateur() + ","
                + t.getNumeroCible() + ","
                + t.getMontant() + ","
                + t.getDevisePaiement() + ","
                + t.getStatut() + ","
                + (t.getDateDemande() != null ? t.getDateDemande() : "") + ","
                + (t.getCommission() != null ? t.getCommission() : "")
        ).collect(Collectors.joining("\n", header, "\n"));
        byte[] csvBytes = csv.getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvBytes);
    }

    // Préparation pour l'export PDF (structure à compléter avec une librairie PDF)
    // @GetMapping("/transactions/pdf")
    // public ResponseEntity<byte[]> exportTransactionsPdf() { ... }
} 