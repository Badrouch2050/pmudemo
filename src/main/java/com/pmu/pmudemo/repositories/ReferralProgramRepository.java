package com.pmu.pmudemo.repositories;

import com.pmu.pmudemo.domains.ReferralProgram;
import com.pmu.pmudemo.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReferralProgramRepository extends JpaRepository<ReferralProgram, Long> {
    Optional<ReferralProgram> findByCodeParrain(String codeParrain);
    Optional<ReferralProgram> findByParrainAndFilleul(User parrain, User filleul);
    ReferralProgram findByFilleul(User filleul);
    Optional<ReferralProgram> findByParrain(User parrain);
} 