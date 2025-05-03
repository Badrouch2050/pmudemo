package com.pmu.pmudemo.Controller.front;

import com.pmu.pmudemo.domains.Operator;
import com.pmu.pmudemo.services.OperatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/front/operators")
public class OperatorController {
    private final OperatorService operatorService;

    public OperatorController(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping
    public ResponseEntity<List<Operator>> getOperators() {
        return ResponseEntity.ok(operatorService.getAllOperators());
    }
} 