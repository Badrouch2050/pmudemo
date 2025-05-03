package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // méthodes personnalisées si besoin
} 