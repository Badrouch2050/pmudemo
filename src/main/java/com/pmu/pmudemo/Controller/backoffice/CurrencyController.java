package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.Currency;
import com.pmu.pmudemo.repositories.CurrencyRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/backoffice/currencies")
@PreAuthorize("hasRole('ADMIN')")
public class CurrencyController {
    private final CurrencyRepository repository;

    public CurrencyController(CurrencyRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Currency> getAllCurrencies() {
        return repository.findByIsActiveTrueOrderByPriorityAsc();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrency(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Currency createCurrency(@RequestBody Currency currency) {
        return repository.save(currency);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable Long id, @RequestBody Currency currency) {
        return repository.findById(id)
            .map(existingCurrency -> {
                existingCurrency.setCode(currency.getCode());
                existingCurrency.setName(currency.getName());
                existingCurrency.setSymbol(currency.getSymbol());
                existingCurrency.setActive(currency.isActive());
                existingCurrency.setRegion(currency.getRegion());
                existingCurrency.setPriority(currency.getPriority());
                return ResponseEntity.ok(repository.save(existingCurrency));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Currency> toggleCurrency(@PathVariable Long id) {
        return repository.findById(id)
            .map(currency -> {
                currency.setActive(!currency.isActive());
                return ResponseEntity.ok(repository.save(currency));
            })
            .orElse(ResponseEntity.notFound().build());
    }
} 