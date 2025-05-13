package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.MainCurrency;
import com.pmu.pmudemo.repositories.MainCurrencyRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/backoffice/main-currencies")
@PreAuthorize("hasRole('ADMIN')")
public class MainCurrencyController {
    private final MainCurrencyRepository repository;

    public MainCurrencyController(MainCurrencyRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<MainCurrency> getAllMainCurrencies() {
        return repository.findByActiveTrue();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MainCurrency> getMainCurrency(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MainCurrency createMainCurrency(@RequestBody MainCurrency mainCurrency) {
        return repository.save(mainCurrency);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MainCurrency> updateMainCurrency(@PathVariable Long id, @RequestBody MainCurrency mainCurrency) {
        return repository.findById(id)
            .map(existingCurrency -> {
                existingCurrency.setCode(mainCurrency.getCode());
                existingCurrency.setName(mainCurrency.getName());
                existingCurrency.setSymbol(mainCurrency.getSymbol());
                existingCurrency.setActive(mainCurrency.isActive());
                return ResponseEntity.ok(repository.save(existingCurrency));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<MainCurrency> toggleMainCurrency(@PathVariable Long id) {
        return repository.findById(id)
            .map(currency -> {
                currency.setActive(!currency.isActive());
                return ResponseEntity.ok(repository.save(currency));
            })
            .orElse(ResponseEntity.notFound().build());
    }
} 