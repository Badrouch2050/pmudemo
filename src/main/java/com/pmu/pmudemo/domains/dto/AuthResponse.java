package com.pmu.pmudemo.domains.dto;

import com.pmu.pmudemo.domains.Role;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String email;
    private Role role;
    private String nom;
    private String statut;
    // Getters et setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
} 