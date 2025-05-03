package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.Operator;
import com.pmu.pmudemo.repositories.OperatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorService {
    private final OperatorRepository operatorRepo;

    public OperatorService(OperatorRepository operatorRepo) {
        this.operatorRepo = operatorRepo;
    }

    public List<Operator> getAllOperators() {
        return operatorRepo.findAll();
    }
} 