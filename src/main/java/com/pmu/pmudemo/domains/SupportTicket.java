package com.pmu.pmudemo.domains;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    private String sujet;
    private String message;
    private String statut; // OUVERT, EN_COURS, RESOLU, FERME
    private String reponse;
    private LocalDateTime dateCreation;
    private LocalDateTime dateResolution;
    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getSujet() { return sujet; }
    public void setSujet(String sujet) { this.sujet = sujet; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public LocalDateTime getDateResolution() { return dateResolution; }
    public void setDateResolution(LocalDateTime dateResolution) { this.dateResolution = dateResolution; }
} 