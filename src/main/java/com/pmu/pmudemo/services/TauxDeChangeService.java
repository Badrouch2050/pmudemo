package com.pmu.pmudemo.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmu.pmudemo.domains.MainCurrency;
import com.pmu.pmudemo.domains.TauxDeChange;
import com.pmu.pmudemo.domains.Currency;
import com.pmu.pmudemo.repositories.MainCurrencyRepository;
import com.pmu.pmudemo.repositories.TauxDeChangeRepository;
import com.pmu.pmudemo.repositories.CurrencyRepository;
 

@Service
public class TauxDeChangeService {
    private final TauxDeChangeRepository repository;
    private final ExchangeRateApiService apiService;
    private final MainCurrencyRepository mainCurrencyRepository;
    private final CurrencyRepository currencyRepository;
    private static final Logger logger = LoggerFactory.getLogger(TauxDeChangeService.class);

    public TauxDeChangeService(
        TauxDeChangeRepository repository, 
        ExchangeRateApiService apiService,
        MainCurrencyRepository mainCurrencyRepository,
        CurrencyRepository currencyRepository
    ) {
        this.repository = repository;
        this.apiService = apiService;
        this.mainCurrencyRepository = mainCurrencyRepository;
        this.currencyRepository = currencyRepository;
    }
    
    public List<TauxDeChange> getAllRates() {
        return repository.findAll();
    }

    public TauxDeChange findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Taux de change non trouvé"));
    }

    private void validateDevises(String deviseSource, String deviseCible) {
        // Vérifier que la devise source est une devise principale
        MainCurrency mainCurrency = mainCurrencyRepository.findByCode(deviseSource)
            .orElseThrow(() -> new RuntimeException("La devise source doit être une devise principale"));

        // Vérifier que la devise cible est une devise valide
        Currency currency = currencyRepository.findByCode(deviseCible)
            .orElseThrow(() -> new RuntimeException("La devise cible n'est pas une devise valide"));

        // Vérifier que la devise source n'est pas la même que la devise cible
        if (deviseSource.equals(deviseCible)) {
            throw new RuntimeException("La devise source ne peut pas être la même que la devise cible");
        }
    }

    public TauxDeChange getOrFetchRate(String deviseSource, String deviseCible) {
        validateDevises(deviseSource, deviseCible);
        
        return repository.findTopByDeviseSourceAndDeviseCibleOrderByDateObtentionDesc(deviseSource, deviseCible)
            .orElseGet(() -> {
                TauxDeChange taux = new TauxDeChange();
                taux.setDeviseSource(deviseSource);
                taux.setDeviseCible(deviseCible);
                taux.setTaux(1.0);
                taux.setDateObtention(LocalDateTime.now());
                return repository.save(taux);
            });
    }

    @Transactional
    public TauxDeChange saveRate(TauxDeChange taux) {
        validateDevises(taux.getDeviseSource(), taux.getDeviseCible());
        taux.setDateObtention(LocalDateTime.now());
        return repository.save(taux);
    }

    @Transactional
    public TauxDeChange updateRate(Long id, TauxDeChange newTaux) {
        validateDevises(newTaux.getDeviseSource(), newTaux.getDeviseCible());
        
        TauxDeChange taux = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Taux non trouvé"));
        taux.setTaux(newTaux.getTaux());
        taux.setDateObtention(LocalDateTime.now());
        return repository.save(taux);
    }

    @Transactional
    public TauxDeChange toggleRate(Long id) {
        TauxDeChange taux = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Taux non trouvé"));
        taux.setActif(!taux.isActif());
        return repository.save(taux);
    }

    public List<Map<String, Object>> getHistorique(Long id) {
        return repository.findHistoriqueById(id).stream()
            .map(taux -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", taux.getId());
                map.put("taux", taux.getTaux());
                map.put("dateModification", taux.getDateObtention());
                return map;
            })
            .collect(Collectors.toList());
    }

    public Map<String, Object> calculerMontant(double montant, String deviseSource, String deviseCible) {
        validateDevises(deviseSource, deviseCible);
        
        TauxDeChange taux = getOrFetchRate(deviseSource, deviseCible);
        double montantConverti = montant * taux.getTaux();
        
        Map<String, Object> result = new HashMap<>();
        result.put("montantSource", montant);
        result.put("deviseSource", deviseSource);
        result.put("montantCible", montantConverti);
        result.put("deviseCible", deviseCible);
        result.put("taux", taux.getTaux());
        result.put("dateCalcul", LocalDateTime.now());
        
        return result;
    }
} 