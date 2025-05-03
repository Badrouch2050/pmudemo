package com.pmu.pmudemo.services;

import com.pmu.pmudemo.domains.User;
import com.pmu.pmudemo.domains.dto.RegisterRequest;
import com.pmu.pmudemo.domains.dto.LoginRequest;
import com.pmu.pmudemo.domains.dto.AuthResponse;
import com.pmu.pmudemo.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }
        User user = new User();
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        user.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        user.setMethodeAuthentification("EMAIL");
        user.setRoles("USER");
        user.setDateInscription(LocalDateTime.now());
        user.setStatut("ACTIF");
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail(), user.getRoles());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        return response;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        String token = jwtService.generateToken(user.getEmail(), user.getRoles());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        return response;
    }

    public AuthResponse loginOrRegisterGoogleUser(String email, String nom) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setNom(nom);
            user.setEmail(email);
            user.setMethodeAuthentification("GOOGLE");
            user.setRoles("USER");
            user.setDateInscription(LocalDateTime.now());
            user.setStatut("ACTIF");
            userRepository.save(user);
        }
        String token = jwtService.generateToken(user.getEmail(), user.getRoles());
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        return response;
    }
} 