package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.StockCarte;
import com.pmu.pmudemo.dto.StockCarteDTO;
import com.pmu.pmudemo.dto.StockCarteResponseDTO;
import com.pmu.pmudemo.services.StockCarteService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/backoffice/stock-cartes")
public class StockCarteController {
    private final StockCarteService stockCarteService;
    private final ModelMapper modelMapper;

    public StockCarteController(StockCarteService stockCarteService, ModelMapper modelMapper) {
        this.stockCarteService = stockCarteService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Page<StockCarteResponseDTO>> getAll(
        @RequestParam(required = false) String pays,
        @RequestParam(required = false) String statut,
        @RequestParam(required = false) String operateur,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StockCarte> stockCartes;
        
        if (pays != null && !pays.isBlank()) {
            if (statut != null && !statut.isBlank()) {
                if (operateur != null && !operateur.isBlank()) {
                    stockCartes = stockCarteService.findByPaysAndStatutAndOperateurIgnoreCase(pays, statut, operateur, pageable);
                } else {
                    stockCartes = stockCarteService.findByPaysAndStatutIgnoreCase(pays, statut, pageable);
                }
            } else if (operateur != null && !operateur.isBlank()) {
                stockCartes = stockCarteService.findByPaysAndOperateurIgnoreCase(pays, operateur, pageable);
            } else {
                stockCartes = stockCarteService.findByPaysIgnoreCase(pays, pageable);
            }
        } else if (statut != null && !statut.isBlank()) {
            if (operateur != null && !operateur.isBlank()) {
                stockCartes = stockCarteService.findByStatutAndOperateurIgnoreCase(statut, operateur, pageable);
            } else {
                stockCartes = stockCarteService.findByStatutIgnoreCase(statut, pageable);
            }
        } else if (operateur != null && !operateur.isBlank()) {
            stockCartes = stockCarteService.findByOperateurIgnoreCase(operateur, pageable);
        } else {
            stockCartes = stockCarteService.findAll(pageable);
        }
        
        Page<StockCarteResponseDTO> responseDTOs = stockCartes.map(
            carte -> modelMapper.map(carte, StockCarteResponseDTO.class)
        );
        
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockCarte> getById(@PathVariable Long id) {
        return stockCarteService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StockCarteResponseDTO> create(@Valid @RequestBody StockCarteDTO dto) {
        StockCarte carte = modelMapper.map(dto, StockCarte.class);
        StockCarte savedCarte = stockCarteService.addStock(carte);
        return ResponseEntity.ok(modelMapper.map(savedCarte, StockCarteResponseDTO.class));
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
                return ResponseEntity.ok(stockCarteService.addStock(existing));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
} 