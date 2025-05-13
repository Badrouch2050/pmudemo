package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.TauxDeChange;
import com.pmu.pmudemo.services.TauxDeChangeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/backoffice/taux-de-change")
@PreAuthorize("hasRole('ADMIN')")
public class TauxDeChangeController {
    private final TauxDeChangeService service;

    public TauxDeChangeController(TauxDeChangeService service) {
        this.service = service;
    }

    @GetMapping
    public List<TauxDeChange> getAllRates() {
        return service.getAllRates();
    }

    @GetMapping("/{id}")
    public TauxDeChange getRate(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public TauxDeChange createRate(@RequestBody TauxDeChange taux) {
        return service.saveRate(taux);
    }

    @PutMapping("/{id}")
    public TauxDeChange updateRate(@PathVariable Long id, @RequestBody TauxDeChange taux) {
        return service.updateRate(id, taux);
    }

    @PatchMapping("/{id}/toggle")
    public TauxDeChange toggleRate(@PathVariable Long id) {
        return service.toggleRate(id);
    }

    @GetMapping("/{id}/historique")
    public List<Map<String, Object>> getHistorique(@PathVariable Long id) {
        return service.getHistorique(id);
    }

    @GetMapping("/calcul")
    public Map<String, Object> calculerMontant(
        @RequestParam double montant,
        @RequestParam String deviseSource,
        @RequestParam String deviseCible
    ) {
        return service.calculerMontant(montant, deviseSource, deviseCible);
    }
} 