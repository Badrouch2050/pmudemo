package com.pmu.pmudemo.domains;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "user_dashboard")
public class UserDashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    @CollectionTable(name = "dashboard_contacts", joinColumns = @JoinColumn(name = "dashboard_id"))
    @AttributeOverrides({
        @AttributeOverride(name = "numero", column = @Column(name = "contact_numero")),
        @AttributeOverride(name = "nom", column = @Column(name = "contact_nom"))
    })
    private List<Contact> contactsFrequents;

    @ElementCollection
    @CollectionTable(name = "dashboard_promotions")
    private List<Promotion> promotionsActives;

    @Embedded
    private DashboardStatistics stats;

    @Column(nullable = false)
    private LocalDateTime lastUpdate = LocalDateTime.now();
}

@Embeddable
@Data
class Promotion {
    private String code;
    private String description;
    private LocalDateTime dateExpiration;
    private Double valeur;
} 