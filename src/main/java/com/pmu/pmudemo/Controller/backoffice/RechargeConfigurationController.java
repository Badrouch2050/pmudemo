package com.pmu.pmudemo.controllers;

import com.pmu.pmudemo.domains.RechargeConfiguration;
import com.pmu.pmudemo.services.RechargeConfigurationService;
import com.pmu.pmudemo.dto.RechargeConfigurationDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recharge-configurations")
public class RechargeConfigurationController {

    @Autowired
    private RechargeConfigurationService configurationService;

    @PostMapping
    public ResponseEntity<RechargeConfiguration> createConfiguration(
            @Valid @RequestBody RechargeConfigurationDTO dto) {
        return ResponseEntity.ok(configurationService.createConfiguration(
            dto.getPays(), dto.getOperateur(), dto.getMontantMin(), dto.getMontantMax()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RechargeConfiguration> updateConfiguration(
            @PathVariable Long id,
            @Valid @RequestBody RechargeConfigurationDTO dto) {
        return ResponseEntity.ok(configurationService.updateConfiguration(
            id, dto.getMontantMin(), dto.getMontantMax()));
    }

    @GetMapping("/{pays}/{operateur}")
    public ResponseEntity<RechargeConfiguration> getConfiguration(
            @PathVariable String pays,
            @PathVariable String operateur) {
        return configurationService.getConfiguration(pays, operateur)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RechargeConfiguration>> getAllConfigurations() {
        return ResponseEntity.ok(configurationService.getAllConfigurations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfiguration(@PathVariable Long id) {
        configurationService.deleteConfiguration(id);
        return ResponseEntity.ok().build();
    }
} 