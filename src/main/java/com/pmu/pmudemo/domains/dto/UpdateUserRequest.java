package com.pmu.pmudemo.domains.dto;

import com.pmu.pmudemo.domains.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;
    
    private String motDePasse; // Optionnel pour la mise à jour
    
    @NotNull(message = "Le rôle est obligatoire")
    private Role role;
    
    private boolean actif;
} 