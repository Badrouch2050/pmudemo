package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.dto.*;
import com.pmu.pmudemo.domains.*;
import com.pmu.pmudemo.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDashboardRepository userDashboardRepository;
    private final RechargeTransactionRepository rechargeTransactionRepository;
    private final ReferralProgramRepository referralProgramRepository;
    private final DisputeRepository disputeRepository;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserDashboardRepository userDashboardRepository,
            RechargeTransactionRepository rechargeTransactionRepository,
            ReferralProgramRepository referralProgramRepository,
            DisputeRepository disputeRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDashboardRepository = userDashboardRepository;
        this.rechargeTransactionRepository = rechargeTransactionRepository;
        this.referralProgramRepository = referralProgramRepository;
        this.disputeRepository = disputeRepository;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllAgents() {
        return userRepository.findByRole(Role.AGENT).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO createUser(CreateUserRequest request) {
        User user = new User();
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        user.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        user.setRole(request.getRole());
        user.setStatut("ACTIF");
        user.setActif(true);
        
        return convertToDTO(userRepository.save(user));
    }

    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        if (request.getMotDePasse() != null && !request.getMotDePasse().isEmpty()) {
            user.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        }
        user.setRole(request.getRole());
        user.setActif(request.isActif());
        
        return convertToDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActif(user.isActif());
        return dto;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void disableUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setStatut("DESACTIVE");
            userRepository.save(user);
        });
    }

    public void changeUserRole(Long id, Role role) {
        userRepository.findById(id).ifPresent(user -> {
            user.setRole(role);
            userRepository.save(user);
        });
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<ClientListDTO> getAllClients(Pageable pageable) {
        return userRepository.findByRole(Role.USER, pageable)
                .map(user -> {
                    ClientListDTO dto = new ClientListDTO();
                    dto.setId(user.getId());
                    dto.setNom(user.getNom());
                    dto.setEmail(user.getEmail());
                    dto.setStatut(user.getStatut());
                    dto.setDateInscription(user.getDateInscription());
                    dto.setActif(user.isActif());
                    return dto;
                });
    }

    public Page<ClientListDTO> searchClients(String nom, String email, String statut, Boolean actif, Pageable pageable) {
        return userRepository.findBySearchCriteria(nom, email, statut, actif, pageable)
                .map(user -> {
                    ClientListDTO dto = new ClientListDTO();
                    dto.setId(user.getId());
                    dto.setNom(user.getNom());
                    dto.setEmail(user.getEmail());
                    dto.setStatut(user.getStatut());
                    dto.setDateInscription(user.getDateInscription());
                    dto.setActif(user.isActif());
                    return dto;
                });
    }

    public ClientDetailsDTO getClientDetails(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client non trouvé"));

        ClientDetailsDTO dto = new ClientDetailsDTO();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setStatut(user.getStatut());
        dto.setMethodeAuthentification(user.getMethodeAuthentification());
        dto.setDateInscription(user.getDateInscription());
        dto.setActif(user.isActif());

        // Récupération des statistiques
        UserDashboard dashboard = userDashboardRepository.findByUser(user)
                .orElseGet(() -> {
                    UserDashboard newDashboard = new UserDashboard();
                    newDashboard.setUser(user);
                    return userDashboardRepository.save(newDashboard);
                });

        if (dashboard.getStats() != null) {
            dto.setMontantTotalRecharge(dashboard.getStats().getMontantTotalRecharge());
            dto.setNombreRecharges(dashboard.getStats().getNombreRecharges());
            dto.setMontantMoyenRecharge(dashboard.getStats().getMontantMoyenRecharge());
            dto.setOperateurPrefere(dashboard.getStats().getOperateurPrefere());
            dto.setPaysPrefere(dashboard.getStats().getPaysPrefere());
        }

        // Récupération des contacts fréquents
        if (dashboard.getContactsFrequents() != null) {
            dto.setContactsFrequents(dashboard.getContactsFrequents().stream()
                    .map(contact -> {
                        ContactDTO contactDTO = new ContactDTO();
                        contactDTO.setNumero(contact.getNumero());
                        contactDTO.setNom(contact.getNom());
                        return contactDTO;
                    })
                    .collect(Collectors.toList()));
        }

        // Récupération des dernières transactions
        List<RechargeTransaction> transactions = rechargeTransactionRepository.findByUserOrderByDateDemandeDesc(user);
        dto.setDernieresTransactions(transactions.stream()
                .map(transaction -> {
                    TransactionDTO transactionDTO = new TransactionDTO();
                    transactionDTO.setId(transaction.getId());
                    transactionDTO.setOperateur(transaction.getOperateur());
                    transactionDTO.setNumeroCible(transaction.getNumeroCible());
                    transactionDTO.setMontant(transaction.getMontant());
                    transactionDTO.setDevisePaiement(transaction.getDevisePaiement());
                    transactionDTO.setStatut(transaction.getStatut());
                    transactionDTO.setDateDemande(transaction.getDateDemande());
                    transactionDTO.setDateTraitement(transaction.getDateTraitement());
                    transactionDTO.setMontantCarte(transaction.getMontantCarte());
                    transactionDTO.setDeviseCarte(transaction.getDeviseCarte());
                    transactionDTO.setTauxDeChange(transaction.getTauxDeChange());
                    transactionDTO.setFraisConversion(transaction.getFraisConversion());
                    transactionDTO.setPays(transaction.getPays());
                    transactionDTO.setCommission(transaction.getCommission());
                    transactionDTO.setTypeCommission(transaction.getTypeCommission());
                    return transactionDTO;
                })
                .collect(Collectors.toList()));

        // Récupération des informations de parrainage
        ReferralProgram referral = referralProgramRepository.findByFilleul(user);
        if (referral != null) {
            ReferralInfoDTO referralInfo = new ReferralInfoDTO();
            referralInfo.setCodeParrainage(referral.getCodeParrain());
            referralInfo.setParrainEmail(referral.getParrain().getEmail());
            referralInfo.setMontantTotalParrainage(referral.getMontantTotalParrainage());
            referralInfo.setNombreRecharges(referral.getNombreRecharges());
            referralInfo.setBonusTotal(referral.getBonusParrain() * referral.getMontantTotalParrainage());
            referralInfo.setStatut(referral.getStatut());
            dto.setReferralInfo(referralInfo);
        }

        // Récupération des litiges en cours
        List<Dispute> disputes = disputeRepository.findByUserAndStatutNot(user, "RESOLU");
        dto.setLitigesEnCours(disputes.stream()
                .map(dispute -> {
                    DisputeDTO disputeDTO = new DisputeDTO();
                    disputeDTO.setId(dispute.getId());
                    disputeDTO.setTransactionId(dispute.getTransaction().getId());
                    disputeDTO.setMotif(dispute.getMotif());
                    disputeDTO.setStatut(dispute.getStatut());
                    disputeDTO.setCommentaire(dispute.getCommentaire());
                    disputeDTO.setDateCreation(dispute.getDateCreation());
                    disputeDTO.setDateResolution(dispute.getDateResolution());
                    return disputeDTO;
                })
                .collect(Collectors.toList()));

        return dto;
    }
} 