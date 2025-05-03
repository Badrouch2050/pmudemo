package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.CommissionConfig;
import com.pmu.pmudemo.repositories.CommissionConfigRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@Tag(name = "Commissions", description = "Gestion des commissions par pays et opérateur.")
@RestController
@RequestMapping("/api/backoffice/commissions")
@PreAuthorize("hasRole('ADMIN')")
public class CommissionConfigController {
    private final CommissionConfigRepository repository;

    public CommissionConfigController(CommissionConfigRepository repository) {
        this.repository = repository;
    }

    @Operation(
        summary = "Lister toutes les configurations de commission",
        responses = @ApiResponse(responseCode = "200", description = "Liste des commissions", content = @Content(schema = @Schema(implementation = CommissionConfig.class)))
    )
    @GetMapping
    public ResponseEntity<List<CommissionConfig>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @Operation(
        summary = "Obtenir une configuration de commission par id",
        responses = @ApiResponse(responseCode = "200", description = "Commission trouvée", content = @Content(schema = @Schema(implementation = CommissionConfig.class)))
    )
    @GetMapping("/{id}")
    public ResponseEntity<CommissionConfig> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Créer une configuration de commission",
        requestBody = @RequestBody(
            content = @Content(schema = @Schema(implementation = CommissionConfig.class),
                examples = @ExampleObject(value = "{\"pays\":\"TUNISIE\",\"operateur\":\"Orange\",\"typeCommission\":\"POURCENTAGE\",\"valeur\":3.0,\"actif\":true}"))
        ),
        responses = @ApiResponse(responseCode = "200", description = "Commission créée", content = @Content(schema = @Schema(implementation = CommissionConfig.class)))
    )
    @PostMapping
    public ResponseEntity<CommissionConfig> create(@RequestBody CommissionConfig config) {
        return ResponseEntity.ok(repository.save(config));
    }

    @Operation(
        summary = "Mettre à jour une configuration de commission",
        requestBody = @RequestBody(
            content = @Content(schema = @Schema(implementation = CommissionConfig.class))
        ),
        responses = @ApiResponse(responseCode = "200", description = "Commission mise à jour", content = @Content(schema = @Schema(implementation = CommissionConfig.class)))
    )
    @PutMapping("/{id}")
    public ResponseEntity<CommissionConfig> update(@PathVariable Long id, @RequestBody CommissionConfig config) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setPays(config.getPays());
                    existing.setOperateur(config.getOperateur());
                    existing.setTypeCommission(config.getTypeCommission());
                    existing.setValeur(config.getValeur());
                    existing.setActif(config.isActif());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Supprimer une configuration de commission",
        responses = @ApiResponse(responseCode = "204", description = "Commission supprimée")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 