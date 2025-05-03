package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.TauxDeChange;
import com.pmu.pmudemo.repositories.TauxDeChangeRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TauxDeChangeService {
    private final TauxDeChangeRepository repository;
    private final ExchangeRateApiService apiService;

    public TauxDeChangeService(TauxDeChangeRepository repository, ExchangeRateApiService apiService) {
        this.repository = repository;
        this.apiService = apiService;
    }

    public TauxDeChange getOrFetchRate(String deviseSource, String deviseCible) {
        // On cherche le taux le plus récent (moins de 24h)
        TauxDeChange last = repository.findTopByDeviseSourceAndDeviseCibleOrderByDateObtentionDesc(deviseSource, deviseCible).orElse(null);
        if (last != null && last.getDateObtention().isAfter(LocalDateTime.now().minusHours(24))) {
            return last;
        }
        // Sinon, on va chercher le taux via l'API
        double taux = apiService.getRate(deviseSource, deviseCible);
        TauxDeChange nouveau = new TauxDeChange();
        nouveau.setDeviseSource(deviseSource);
        nouveau.setDeviseCible(deviseCible);
        nouveau.setTaux(taux);
        nouveau.setDateObtention(LocalDateTime.now());
        repository.save(nouveau);
        return nouveau;
    }

    public List<TauxDeChange> getHistory(String deviseSource, String deviseCible) {
        return repository.findAll().stream()
                .filter(t -> t.getDeviseSource().equalsIgnoreCase(deviseSource) && t.getDeviseCible().equalsIgnoreCase(deviseCible))
                .toList();
    }
} 