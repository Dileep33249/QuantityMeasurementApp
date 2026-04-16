package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.dto.AuthRequest;
import com.app.quantitymeasurement.dto.AuthResponse;
import com.app.quantitymeasurement.dto.RegisterRequest;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.AppUserEntity;
import com.app.quantitymeasurement.repository.AppUserRepository;
import com.app.quantitymeasurement.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AppUserRepository appUserRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new QuantityMeasurementException("Username already exists");
        }

        AppUserEntity user = new AppUserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        appUserRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token, "Bearer", user.getUsername());
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtService.generateToken(authentication.getName());
        return new AuthResponse(token, "Bearer", authentication.getName());
    }
}
