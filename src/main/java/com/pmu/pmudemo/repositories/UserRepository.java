package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.User;
import com.pmu.pmudemo.domains.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // méthodes personnalisées si besoin
    Optional<User> findByEmail(String email);
    Page<User> findByRole(Role role, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = 'USER' " +
           "AND (:nom IS NULL OR LOWER(u.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) " +
           "AND (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
           "AND (:statut IS NULL OR u.statut = :statut) " +
           "AND (:actif IS NULL OR u.actif = :actif)")
    Page<User> findBySearchCriteria(
            @Param("nom") String nom,
            @Param("email") String email,
            @Param("statut") String statut,
            @Param("actif") Boolean actif,
            Pageable pageable);

    List<User> findByRole(Role role);
} 