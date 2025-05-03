package com.pmu.pmudemo.Controller.front;

import com.pmu.pmudemo.domains.dto.RegisterRequest;
import com.pmu.pmudemo.domains.dto.LoginRequest;
import com.pmu.pmudemo.domains.dto.AuthResponse;
import com.pmu.pmudemo.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/front/auth")
@Tag(name = "Authentification", description = "Endpoints d'inscription et de connexion utilisateur")
public class AuthController {
    private final AuthService authService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public AuthController(AuthService authService, ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService) {
        this.authService = authService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientService = authorizedClientService;
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
} 