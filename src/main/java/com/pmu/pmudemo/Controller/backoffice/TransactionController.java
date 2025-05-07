package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.dto.*;
import com.pmu.pmudemo.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/transactions")
@Tag(name = "Gestion des transactions", description = "Endpoints pour la gestion des transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Lister les transactions", description = "Récupère la liste paginée des transactions.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des transactions récupérée avec succès",
                            content = @Content(schema = @Schema(implementation = TransactionListDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<Page<TransactionListDTO>> getAllTransactions(Pageable pageable) {
        return ResponseEntity.ok(transactionService.getAllTransactions(pageable));
    }

    @Operation(summary = "Filtrer les transactions", description = "Récupère la liste filtrée des transactions.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste filtrée des transactions récupérée avec succès",
                            content = @Content(schema = @Schema(implementation = TransactionListDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
    @PostMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<Page<TransactionListDTO>> getTransactionsByFilter(
            @RequestBody TransactionFilterDTO filter,
            Pageable pageable) {
        return ResponseEntity.ok(transactionService.getTransactionsByFilter(filter, pageable));
    }

    @Operation(summary = "Détails d'une transaction", description = "Récupère les détails d'une transaction.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Détails de la transaction récupérés avec succès",
                            content = @Content(schema = @Schema(implementation = TransactionDetailsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Transaction non trouvée"),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<TransactionDetailsDTO> getTransactionDetails(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionDetails(id));
    }

    @Operation(summary = "Mettre à jour le statut", description = "Met à jour le statut d'une transaction.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Statut mis à jour avec succès",
                            content = @Content(schema = @Schema(implementation = TransactionDetailsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Transaction non trouvée"),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<TransactionDetailsDTO> updateTransactionStatus(
            @PathVariable Long id,
            @RequestBody UpdateTransactionStatusDTO updateDTO) {
        return ResponseEntity.ok(transactionService.updateTransactionStatus(id, updateDTO));
    }

    @Operation(summary = "Assigner à un agent", description = "Assign une transaction à un agent.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transaction assignée avec succès",
                            content = @Content(schema = @Schema(implementation = TransactionDetailsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Transaction ou agent non trouvé"),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionDetailsDTO> assignTransaction(
            @PathVariable Long id,
            @RequestBody AssignTransactionDTO assignDTO) {
        return ResponseEntity.ok(transactionService.assignTransaction(id, assignDTO));
    }

    @Operation(summary = "Transactions par agent", description = "Récupère les transactions d'un agent.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des transactions récupérée avec succès",
                            content = @Content(schema = @Schema(implementation = TransactionListDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Agent non trouvé"),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
    @GetMapping("/agent/{agentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<List<TransactionListDTO>> getTransactionsByAgent(@PathVariable Long agentId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAgent(agentId));
    }

    @Operation(summary = "Transactions automatiques", description = "Récupère les transactions traitées automatiquement.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des transactions récupérée avec succès",
                            content = @Content(schema = @Schema(implementation = TransactionListDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
    @GetMapping("/automatic")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<List<TransactionListDTO>> getAutomaticTransactions() {
        return ResponseEntity.ok(transactionService.getAutomaticTransactions());
    }
} 