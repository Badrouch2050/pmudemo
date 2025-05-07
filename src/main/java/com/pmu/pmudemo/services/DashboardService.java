package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.*;
import com.pmu.pmudemo.repositories.RechargeTransactionRepository;
import com.pmu.pmudemo.repositories.UserDashboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardService {
    private final UserDashboardRepository dashboardRepository;
    private final RechargeTransactionRepository transactionRepository;

    public DashboardService(
        UserDashboardRepository dashboardRepository,
        RechargeTransactionRepository transactionRepository
    ) {
        this.dashboardRepository = dashboardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void updateDashboardStats(User user) {
        UserDashboard dashboard = getOrCreateDashboard(user);
        
        // Calcul des statistiques
        List<RechargeTransaction> transactions = transactionRepository.findByUser(user);
        
        DashboardStatistics stats = new DashboardStatistics();
        stats.setMontantTotalRecharge(calculateTotalAmount(transactions));
        stats.setNombreRecharges(transactions.size());
        stats.setMontantMoyenRecharge(calculateAverageAmount(transactions));
        stats.setOperateurPrefere(findPreferredOperator(transactions));
        stats.setPaysPrefere(findPreferredCountry(transactions));
        
        dashboard.setStats(stats);
        dashboard.setLastUpdate(LocalDateTime.now());
        dashboardRepository.save(dashboard);
    }

    @Transactional
    public void updateFrequentContacts(User user, String numero, String nom) {
        UserDashboard dashboard = getOrCreateDashboard(user);
        Contact newContact = new Contact();
        newContact.setNumero(numero);
        newContact.setNom(nom);
        
        List<Contact> contacts = dashboard.getContactsFrequents();
        if (!contacts.contains(newContact)) {
            contacts.add(newContact);
            if (contacts.size() > 10) {
                contacts.remove(0);
            }
            dashboard.setContactsFrequents(contacts);
            dashboardRepository.save(dashboard);
        }
    }

    public UserDashboard getDashboardData(User user) {
        return getOrCreateDashboard(user);
    }

    private UserDashboard getOrCreateDashboard(User user) {
        return dashboardRepository.findByUser(user)
            .orElseGet(() -> {
                UserDashboard newDashboard = new UserDashboard();
                newDashboard.setUser(user);
                newDashboard.setStats(new DashboardStatistics());
                return dashboardRepository.save(newDashboard);
            });
    }

    private Double calculateTotalAmount(List<RechargeTransaction> transactions) {
        return transactions.stream()
            .mapToDouble(RechargeTransaction::getMontant)
            .sum();
    }

    private Double calculateAverageAmount(List<RechargeTransaction> transactions) {
        if (transactions.isEmpty()) return 0.0;
        return calculateTotalAmount(transactions) / transactions.size();
    }

    private String findPreferredOperator(List<RechargeTransaction> transactions) {
        return transactions.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                RechargeTransaction::getOperateur,
                java.util.stream.Collectors.counting()
            ))
            .entrySet()
            .stream()
            .max(java.util.Map.Entry.comparingByValue())
            .map(java.util.Map.Entry::getKey)
            .orElse(null);
    }

    private String findPreferredCountry(List<RechargeTransaction> transactions) {
        return transactions.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                RechargeTransaction::getPays,
                java.util.stream.Collectors.counting()
            ))
            .entrySet()
            .stream()
            .max(java.util.Map.Entry.comparingByValue())
            .map(java.util.Map.Entry::getKey)
            .orElse(null);
    }
} 