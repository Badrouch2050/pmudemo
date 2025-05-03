package com.pmu.pmudemo.Controller.front;

import com.pmu.pmudemo.domains.FeatureFlag;
import com.pmu.pmudemo.services.FeatureFlagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/front/features")
public class FeatureController {
    private final FeatureFlagService featureFlagService;

    public FeatureController(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @GetMapping
    public ResponseEntity<List<FeatureFlag>> getActiveFeatures() {
        return ResponseEntity.ok(featureFlagService.getActiveFeatures());
    }
} 