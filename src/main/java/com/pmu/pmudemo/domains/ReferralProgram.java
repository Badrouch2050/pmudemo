package com.pmu.pmudemo.domains;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "referral_program")
public class ReferralProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parrain_id")
    private User parrain;

    @ManyToOne
    @JoinColumn(name = "filleul_id")
    private User filleul;

    @Column(nullable = false, unique = true)
    private String codeParrain;

    @Column(nullable = false)
    private Double bonusParrain = 0.05; // 5% par défaut

    @Column(nullable = false)
    private Double bonusFilleul = 0.02; // 2% par défaut

    @Column(nullable = false)
    private String statut = "ACTIF";

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    private LocalDateTime dateUtilisation;

    @Column(nullable = false)
    private Double montantTotalParrainage = 0.0;

    @Column(nullable = false)
    private Integer nombreRecharges = 0;
} 