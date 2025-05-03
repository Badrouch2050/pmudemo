package com.pmu.pmudemo.domains;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    @Column(unique = true)
    private String email;
    private String motDePasse;
    private String methodeAuthentification; // EMAIL, GOOGLE, etc.
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles; // ADMIN, AGENT, AGENT_SUPPORT, AGENT_STOCK, etc.
    private LocalDateTime dateInscription;
    private String statut; // ACTIF, DESACTIVE
    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public String getMethodeAuthentification() { return methodeAuthentification; }
    public void setMethodeAuthentification(String methodeAuthentification) { this.methodeAuthentification = methodeAuthentification; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
    public LocalDateTime getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
} 