package com.pmu.pmudemo.domains.dto;

import com.pmu.pmudemo.domains.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String nom;
    private String email;
    private Role role;
    private boolean actif;
} 