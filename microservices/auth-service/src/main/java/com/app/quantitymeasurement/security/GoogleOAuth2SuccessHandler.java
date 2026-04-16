package com.app.quantitymeasurement.security;

import com.app.quantitymeasurement.model.AppUserEntity;
import com.app.quantitymeasurement.repository.AppUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.frontend.oauth-success-url}")
    private String frontendSuccessUrl;

    public GoogleOAuth2SuccessHandler(AppUserRepository appUserRepository,
                                      PasswordEncoder passwordEncoder,
                                      JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String username = resolveUsername(oauthUser);

        AppUserEntity user = appUserRepository.findByUsername(username)
                .orElseGet(() -> createGoogleUser(username));

        String token = jwtService.generateToken(user.getUsername());
        String redirectUrl = UriComponentsBuilder.fromUriString(frontendSuccessUrl)
                .queryParam("token", token)
                .queryParam("username", user.getUsername())
                .queryParam("provider", "google")
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }

    private String resolveUsername(OAuth2User oauthUser) {
        Object email = oauthUser.getAttributes().get("email");
        if (email != null) {
            return String.valueOf(email);
        }
        Object name = oauthUser.getAttributes().get("name");
        if (name != null) {
            return String.valueOf(name);
        }
        return oauthUser.getName();
    }

    private AppUserEntity createGoogleUser(String username) {
        AppUserEntity user = new AppUserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("google-oauth-" + UUID.randomUUID()));
        user.setRole("ROLE_USER");
        return appUserRepository.save(user);
    }
}
