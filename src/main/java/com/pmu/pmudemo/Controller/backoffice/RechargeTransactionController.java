package com.pmu.pmudemo.controllers;

import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.services.RechargeTransactionService;
import com.pmu.pmudemo.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import com.pmu.pmudemo.domains.User;
import com.pmu.pmudemo.dto.DirectRechargeDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recharge-transactions")
public class RechargeTransactionController {

    @Autowired
    private RechargeTransactionService transactionService;

    @Autowired
    private UserService userService;

    @PostMapping("/direct")
    public ResponseEntity<RechargeTransaction> createDirectRecharge(
            @Valid @RequestBody DirectRechargeDTO dto) {
        User user = userService.getUserById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return ResponseEntity.ok(transactionService.createDirectRecharge(
            user, dto.getPays(), dto.getOperateur(), dto.getNumeroCible(), 
            dto.getMontant(), dto.getDevisePaiement()));
    }
} 