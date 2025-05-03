package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.TauxDeChange;
import com.pmu.pmudemo.services.TauxDeChangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/backoffice/taux-de-change")
@PreAuthorize("hasRole('ADMIN')")
public class TauxDeChangeController {
    private final TauxDeChangeService service;

    public TauxDeChangeController(TauxDeChangeService service) {
        this.service = service;
    }

    @GetMapping("/current")
    public ResponseEntity<TauxDeChange> getCurrent(@RequestParam String source, @RequestParam String cible) {
        return ResponseEntity.ok(service.getOrFetchRate(source, cible));
    }

    @GetMapping("/history")
    public ResponseEntity<List<TauxDeChange>> getHistory(@RequestParam String source, @RequestParam String cible) {
        return ResponseEntity.ok(service.getHistory(source, cible));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TauxDeChange> forceRefresh(@RequestParam String source, @RequestParam String cible) {
        // Force la récupération d'un nouveau taux
        TauxDeChange taux = service.getOrFetchRate(source, cible);
        return ResponseEntity.ok(taux);
    }
} 