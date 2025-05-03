package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // méthodes personnalisées si besoin
    Optional<User> findByEmail(String email);
} 