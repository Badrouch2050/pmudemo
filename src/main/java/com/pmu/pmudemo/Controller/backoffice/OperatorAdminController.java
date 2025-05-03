package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.Operator;
import com.pmu.pmudemo.repositories.OperatorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/backoffice/operators")
@PreAuthorize("hasRole('ADMIN')")
public class OperatorAdminController {
    private final OperatorRepository operatorRepository;
    private static final Logger logger = LoggerFactory.getLogger(OperatorAdminController.class);

    public OperatorAdminController(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    @GetMapping
    public ResponseEntity<List<Operator>> getAll(@RequestParam(required = false) String pays) {
        if (pays != null && !pays.isBlank()) {
            return ResponseEntity.ok(operatorRepository.findByActifTrueAndPaysIgnoreCase(pays));
        }
        return ResponseEntity.ok(operatorRepository.findByActifTrue());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Operator>> getAllOperators() {
        return ResponseEntity.ok(operatorRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Operator> getById(@PathVariable Long id) {
        Optional<Operator> op = operatorRepository.findById(id);
        return op.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Operator operator) {
        if (operatorRepository.findByNomIgnoreCase(operator.getNom()).isPresent()) {
            return ResponseEntity.badRequest().body("Un opérateur avec ce nom existe déjà.");
        }
        if (operator.getPays() == null || operator.getPays().isBlank()) {
            return ResponseEntity.badRequest().body("Le champ 'pays' est obligatoire.");
        }
        Operator saved = operatorRepository.save(operator);
        logger.info("Opérateur créé : {} ({})", saved.getNom(), saved.getPays());
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Operator operator) {
        Optional<Operator> existingByName = operatorRepository.findByNomIgnoreCase(operator.getNom());
        if (existingByName.isPresent() && !existingByName.get().getId().equals(id)) {
            return ResponseEntity.badRequest().body("Un opérateur avec ce nom existe déjà.");
        }
        if (operator.getPays() == null || operator.getPays().isBlank()) {
            return ResponseEntity.badRequest().body("Le champ 'pays' est obligatoire.");
        }
        return operatorRepository.findById(id)
            .map(existing -> {
                existing.setNom(operator.getNom());
                existing.setCodeDetection(operator.getCodeDetection());
                existing.setStatut(operator.getStatut());
                existing.setLogoUrl(operator.getLogoUrl());
                existing.setPays(operator.getPays());
                Operator updated = operatorRepository.save(existing);
                logger.info("Opérateur modifié : {} ({})", updated.getNom(), updated.getPays());
                return ResponseEntity.ok(updated);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/activation")
    public ResponseEntity<?> setActivation(@PathVariable Long id, @RequestParam boolean actif) {
        return operatorRepository.findById(id)
            .map(existing -> {
                existing.setActif(actif);
                Operator updated = operatorRepository.save(existing);
                logger.info("Opérateur {} : {}", actif ? "activé" : "désactivé", updated.getNom());
                return ResponseEntity.ok(updated);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return operatorRepository.findById(id)
            .map(existing -> {
                existing.setActif(false);
                operatorRepository.save(existing);
                logger.info("Opérateur désactivé (soft delete) : {}", existing.getNom());
                return ResponseEntity.noContent().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
} 