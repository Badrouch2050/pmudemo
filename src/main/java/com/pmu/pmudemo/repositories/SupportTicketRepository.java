package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.SupportTicket;
import com.pmu.pmudemo.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUser(User user);
    List<SupportTicket> findByStatut(String statut);
} 