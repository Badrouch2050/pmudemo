package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.FeatureFlag;
import com.pmu.pmudemo.services.FeatureFlagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/features")
public class FeatureFlagController {
    private final FeatureFlagService featureFlagService;

    public FeatureFlagController(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @GetMapping
    public ResponseEntity<List<FeatureFlag>> getAllFeatures() {
        return ResponseEntity.ok(featureFlagService.getAllFeatures());
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateFeature(@PathVariable Long id) {
        featureFlagService.activateFeature(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateFeature(@PathVariable Long id) {
        featureFlagService.deactivateFeature(id);
        return ResponseEntity.ok().build();
    }
} 