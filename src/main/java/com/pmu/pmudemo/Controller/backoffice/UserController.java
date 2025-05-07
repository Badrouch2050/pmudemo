package com.pmu.pmudemo.Controller.backoffice;

import com.pmu.pmudemo.domains.dto.UserDTO;
import com.pmu.pmudemo.domains.dto.CreateUserRequest;
import com.pmu.pmudemo.domains.dto.UpdateUserRequest;
import com.pmu.pmudemo.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/agents")
@Tag(name = "Gestion des utilisateurs", description = "Endpoints pour la gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Récupérer tous les utilisateurs", description = "Récupère la liste de tous les utilisateurs (ADMIN et AGENT).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des utilisateurs récupérée avec succès",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
 
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<List<UserDTO>> getAllAgents() {
        return ResponseEntity.ok(userService.getAllAgents());
    }

    @Operation(summary = "Créer un nouvel utilisateur", description = "Crée un nouvel utilisateur (ADMIN uniquement).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utilisateur créé avec succès",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Données invalides"),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @Operation(summary = "Modifier un utilisateur", description = "Modifie les informations d'un utilisateur (ADMIN uniquement).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utilisateur modifié avec succès",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Données invalides"),
                    @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(summary = "Supprimer un utilisateur", description = "Supprime un utilisateur (ADMIN uniquement).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Utilisateur supprimé avec succès"),
                    @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
} 