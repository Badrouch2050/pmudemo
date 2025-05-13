package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.RechargeStock;
import com.pmu.pmudemo.services.RechargeStockService;
import com.pmu.pmudemo.dto.RechargeStockDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
  

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.pmu.pmudemo.dto.RechargeStockStatsDTO;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/backoffice/recharge-stocks")
public class RechargeStockController {
    private final RechargeStockService stockService;

    public RechargeStockController(RechargeStockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<Page<RechargeStock>> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(stockService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<RechargeStock> createStock(
            @Valid @RequestBody RechargeStockDTO dto) {
        return ResponseEntity.ok(stockService.createStock(
            dto.getPays(), dto.getOperateur(), dto.getMontant()));
    }

    @PostMapping("/add")
    public ResponseEntity<RechargeStock> addToStock(
            @Valid @RequestBody RechargeStockDTO dto) {
        return ResponseEntity.ok(stockService.addToStock(
            dto.getPays(), dto.getOperateur(), dto.getMontant()));
    }

    @GetMapping("/{pays}/{operateur}")
    public ResponseEntity<RechargeStock> getStock(
            @PathVariable String pays,
            @PathVariable String operateur) {
        return stockService.getStock(pays, operateur)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkStockAvailability(
            @RequestParam String pays,
            @RequestParam String operateur,
            @RequestParam BigDecimal montant) {
        return ResponseEntity.ok(stockService.checkStockAvailability(pays, operateur, montant));
    }

    @Operation(
        summary = "Obtenir les statistiques globales des stocks",
        description = "Récupère les statistiques détaillées des stocks de recharge avec filtrage par période",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Statistiques récupérées avec succès",
                content = @Content(schema = @Schema(implementation = RechargeStockStatsDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Dates invalides",
                content = @Content(examples = @ExampleObject(
                    value = """
                    {
                        "timestamp": "2024-03-20T10:00:00",
                        "status": 400,
                        "error": "Bad Request",
                        "message": "La date de début doit être antérieure à la date de fin"
                    }
                    """
                ))
            )
        }
    )
    @GetMapping("/statistiques")
    public ResponseEntity<RechargeStockStatsDTO> getStatistiques(
        @Parameter(description = "Date de début de la période (format: yyyy-MM-dd)", example = "2024-03-01")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
        
        @Parameter(description = "Date de fin de la période (format: yyyy-MM-dd)", example = "2024-03-20")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin
    ) {
        return ResponseEntity.ok(stockService.getStatistiques(dateDebut, dateFin));
    }
} 