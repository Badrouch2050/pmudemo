package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.UserDashboard;
import com.pmu.pmudemo.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserDashboardRepository extends JpaRepository<UserDashboard, Long> {
    UserDashboard findByUserId(Long userId);
    Optional<UserDashboard> findByUser(User user);
} 