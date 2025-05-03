package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.SupportTicket;
import com.pmu.pmudemo.domains.User;
import com.pmu.pmudemo.repositories.SupportTicketRepository;
import com.pmu.pmudemo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SupportTicketService {
    private final SupportTicketRepository ticketRepo;
    private final UserRepository userRepo;

    public SupportTicketService(SupportTicketRepository ticketRepo, UserRepository userRepo) {
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
    }

    public SupportTicket createTicket(Long userId, String sujet, String message) {
        User user = userRepo.findById(userId).orElseThrow();
        SupportTicket ticket = new SupportTicket();
        ticket.setUser(user);
        ticket.setSujet(sujet);
        ticket.setMessage(message);
        ticket.setStatut("OUVERT");
        ticket.setDateCreation(LocalDateTime.now());
        return ticketRepo.save(ticket);
    }

    public List<SupportTicket> getAllTickets() {
        return ticketRepo.findAll();
    }

    public List<SupportTicket> getTicketsByUser(Long userId) {
        return ticketRepo.findByUserId(userId);
    }

    public Optional<SupportTicket> getTicket(Long id) {
        return ticketRepo.findById(id);
    }

    public SupportTicket respondToTicket(Long id, String reponse, String statut) {
        SupportTicket ticket = ticketRepo.findById(id).orElseThrow();
        ticket.setReponse(reponse);
        ticket.setStatut(statut);
        if ("RESOLU".equals(statut) || "FERME".equals(statut)) {
            ticket.setDateResolution(LocalDateTime.now());
        }
        return ticketRepo.save(ticket);
    }
} 