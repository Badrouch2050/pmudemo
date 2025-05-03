package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.StockCarte;
import com.pmu.pmudemo.services.StockCarteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/stock-cartes")
public class StockCarteController {
    private final StockCarteService stockCarteService;

    public StockCarteController(StockCarteService stockCarteService) {
        this.stockCarteService = stockCarteService;
    }

    @GetMapping
    public ResponseEntity<List<StockCarte>> getAll(@RequestParam(required = false) String pays) {
        if (pays != null && !pays.isBlank()) {
            return ResponseEntity.ok(stockCarteService.findByPaysIgnoreCase(pays));
        }
        return ResponseEntity.ok(stockCarteService.findAll());
    }

    @PostMapping
    public ResponseEntity<StockCarte> create(@RequestBody StockCarte carte) {
        if (carte.getPays() == null || carte.getPays().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(stockCarteService.addStock(carte));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        stockCarteService.deleteStock(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockCarte> update(@PathVariable Long id, @RequestBody StockCarte carte) {
        return stockCarteService.findById(id)
            .map(existing -> {
                existing.setOperateur(carte.getOperateur());
                existing.setMontant(carte.getMontant());
                existing.setCode(carte.getCode());
                existing.setStatut(carte.getStatut());
                existing.setPays(carte.getPays());
                return ResponseEntity.ok(stockCarteService.save(existing));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
} 