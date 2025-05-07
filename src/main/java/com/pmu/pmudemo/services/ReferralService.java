package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.ReferralProgram;
import com.pmu.pmudemo.domains.RechargeTransaction;
import com.pmu.pmudemo.domains.User;
import com.pmu.pmudemo.repositories.ReferralProgramRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Optional;

@Service
public class ReferralService {
    private final ReferralProgramRepository referralProgramRepository;

    public ReferralService(ReferralProgramRepository referralProgramRepository) {
        this.referralProgramRepository = referralProgramRepository;
    }

    public String generateReferralCode(User user) {
        return user.getId() + "-" + UUID.randomUUID().toString().substring(0, 6);
    }

    @Transactional
    public ReferralProgram registerReferral(User parrain, User filleul) {
        ReferralProgram referral = new ReferralProgram();
        referral.setParrain(parrain);
        referral.setFilleul(filleul);
        referral.setCodeParrain(generateReferralCode(parrain));
        return referralProgramRepository.save(referral);
    }

    @Transactional
    public void applyReferralBonus(RechargeTransaction transaction) {
        ReferralProgram referral = referralProgramRepository.findByFilleul(transaction.getUser());
        if (referral != null && "ACTIF".equals(referral.getStatut())) {
            // Calcul des bonus
            Double bonusParrain = transaction.getMontant() * referral.getBonusParrain();
            Double bonusFilleul = transaction.getMontant() * referral.getBonusFilleul();

            // Mise à jour des statistiques
            referral.setMontantTotalParrainage(
                referral.getMontantTotalParrainage() + transaction.getMontant()
            );
            referral.setNombreRecharges(referral.getNombreRecharges() + 1);

            // Sauvegarde
            referralProgramRepository.save(referral);

            // TODO: Implémenter l'application des bonus aux utilisateurs
        }
    }

    public Optional<ReferralProgram> getReferralStats(User user) {
        return referralProgramRepository.findByParrain(user);
    }
} 