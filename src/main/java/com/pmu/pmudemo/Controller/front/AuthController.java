package com.pmu.pmudemo.Controller.front;

import com.pmu.pmudemo.domains.dto.RegisterRequest;
import com.pmu.pmudemo.domains.dto.LoginRequest;
import com.pmu.pmudemo.domains.dto.AuthResponse;
import com.pmu.pmudemo.domains.dto.ChangePasswordRequest;
import com.pmu.pmudemo.domains.Role;
import com.pmu.pmudemo.services.AuthService;
import com.pmu.pmudemo.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/front/auth")
@Tag(name = "Authentification", description = "Endpoints d'inscription et de connexion utilisateur")
public class AuthController {
    private final AuthService authService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientService = authorizedClientService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Inscription utilisateur", description = "Crée un nouvel utilisateur et retourne un JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(value = "{\"nom\":\"Ali\",\"email\":\"ali@email.com\",\"motDePasse\":\"secret123\"}"))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inscription réussie", content = @Content(schema = @Schema(implementation = AuthResponse.class)))
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Connexion utilisateur", description = "Connecte un utilisateur et retourne un JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = "{\"email\":\"ali@email.com\",\"motDePasse\":\"secret123\"}"))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Connexion réussie", content = @Content(schema = @Schema(implementation = AuthResponse.class)))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Redirection vers Google OAuth2", description = "Initie le flow OAuth2 Google.")
    @GetMapping("/oauth2/authorize/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @Operation(summary = "Callback Google OAuth2", description = "Callback après authentification Google.")
    @GetMapping("/oauth2/callback/google")
    public void googleCallback(HttpServletResponse response) throws IOException {
        // Ce endpoint est géré automatiquement par Spring Security, on redirige vers /api/front/auth/oauth2/success
        response.sendRedirect("/api/front/auth/oauth2/success");
    }

    @Operation(summary = "Succès Google OAuth2", description = "Retourne le JWT après login Google.")
    @GetMapping("/oauth2/success")
    public ResponseEntity<AuthResponse> googleSuccess(@AuthenticationPrincipal OAuth2User principal, Authentication authentication) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        String email = principal.getAttribute("email");
        String nom = principal.getAttribute("name");
        // Créer ou récupérer l'utilisateur, puis générer un JWT
        AuthResponse response = authService.loginOrRegisterGoogleUser(email, nom);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Vérification du token", description = "Vérifie la validité du token JWT et retourne les informations de l'utilisateur.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token valide", 
                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Token invalide ou manquant",
                            content = @Content(schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "\"Token invalide ou manquant\""))),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé",
                            content = @Content(schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "\"Accès non autorisé\"")))
            }
    )
    @GetMapping("/verify")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(403).body("Accès non autorisé");
        }
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token manquant ou mal formaté");
        }
        
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        Role role = jwtService.extractRole(token);
        
        if (email == null || role == null) {
            return ResponseEntity.badRequest().body("Token invalide ou expiré");
        }

        AuthResponse response = new AuthResponse();
        response.setEmail(email);
        response.setRole(role);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Changement de mot de passe", description = "Permet à l'utilisateur de modifier son mot de passe.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ChangePasswordRequest.class),
                            examples = @ExampleObject(value = "{\"ancienMotDePasse\":\"ancien123\",\"nouveauMotDePasse\":\"nouveau123\"}"))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mot de passe modifié avec succès"),
                    @ApiResponse(responseCode = "400", description = "Ancien mot de passe incorrect ou utilisateur non trouvé"),
                    @ApiResponse(responseCode = "403", description = "Accès non autorisé")
            }
    )
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ChangePasswordRequest request) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(403).build();
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        if (email == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            authService.changePassword(email, request.getAncienMotDePasse(), request.getNouveauMotDePasse());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

