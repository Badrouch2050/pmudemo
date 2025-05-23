package com.pmu.pmudemo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Map;

@Service
public class ExchangeRateApiService {
    
    @Value("${exchangerate.api.url}")
    private String apiUrl;
    
    @Value("${exchangerate.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Double getRate(String base, String target) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("base", base)
                .queryParam("symbols", target)
                .queryParam("access_key", apiKey)
                .toUriString();
        Map response = restTemplate.getForObject(url, Map.class);
        if (response != null && response.containsKey("rates")) {
            Map<String, Object> rates = (Map<String, Object>) response.get("rates");
            Object rate = rates.get(target);
            if (rate instanceof Number) {
                return ((Number) rate).doubleValue();
            }
        }
        throw new RuntimeException("Taux de change non trouvé pour " + base + "/" + target);
    }
} 