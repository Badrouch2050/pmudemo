package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.User;
import com.pmu.pmudemo.domains.dto.ClientListDTO;
import com.pmu.pmudemo.domains.dto.ClientDetailsDTO;
import com.pmu.pmudemo.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/backoffice/clients")
@PreAuthorize("hasRole('ADMIN')")
public class ClientController {
    private final UserService userService;

    public ClientController(UserService userService) {
        this.userService = userService;
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @GetMapping
    public ResponseEntity<Page<ClientListDTO>> getAllClients(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllClients(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @GetMapping("/search")
    public ResponseEntity<Page<ClientListDTO>> searchClients(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) Boolean actif,
            Pageable pageable) {
        return ResponseEntity.ok(userService.searchClients(nom, email, statut, actif, pageable));
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientDetailsDTO> getClientDetails(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getClientDetails(id));
    }
} 