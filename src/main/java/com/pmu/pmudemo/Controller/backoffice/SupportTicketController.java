package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.SupportTicket;
import com.pmu.pmudemo.services.SupportTicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/support-tickets")
public class SupportTicketController {
    private final SupportTicketService ticketService;

    public SupportTicketController(SupportTicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SupportTicket>> getTicketsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(userId));
    }

    @PostMapping
    public ResponseEntity<SupportTicket> createTicket(@RequestParam Long userId, @RequestParam String sujet, @RequestParam String message) {
        return ResponseEntity.ok(ticketService.createTicket(userId, sujet, message));
    }

    @PatchMapping("/{id}/respond")
    public ResponseEntity<SupportTicket> respondToTicket(@PathVariable Long id, @RequestParam String reponse, @RequestParam String statut) {
        return ResponseEntity.ok(ticketService.respondToTicket(id, reponse, statut));
    }
} 