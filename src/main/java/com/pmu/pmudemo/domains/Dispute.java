package com.pmu.pmudemo.domains;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Dispute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private RechargeTransaction transaction;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String motif;
    private String statut; // OUVERT, EN_COURS, RESOLU, REMBOURSE, REJETE
    private String commentaire;
    private LocalDateTime dateCreation;
    private LocalDateTime dateResolution;
    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RechargeTransaction getTransaction() { return transaction; }
    public void setTransaction(RechargeTransaction transaction) { this.transaction = transaction; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    public LocalDateTime getDateResolution() { return dateResolution; }
    public void setDateResolution(LocalDateTime dateResolution) { this.dateResolution = dateResolution; }
} 