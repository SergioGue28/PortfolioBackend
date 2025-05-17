package com.apiportfolio.backendportfolio.security.controller;

import com.apiportfolio.backendportfolio.security.model.ChangePasswordRequest;
import com.apiportfolio.backendportfolio.security.model.LoginRequest;
import com.apiportfolio.backendportfolio.security.model.LoginResponse;
import com.apiportfolio.backendportfolio.security.model.Portfolio;
import com.apiportfolio.backendportfolio.security.repository.PortfolioRepository;
import com.apiportfolio.backendportfolio.security.service.JwtService;
import com.apiportfolio.backendportfolio.security.service.PortfolioService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private PortfolioRepository portfolioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private PortfolioService portfolioService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Optional<Portfolio> optionalUser = portfolioRepository.findByEmail(request.getEmail());

    if (optionalUser.isEmpty()) {
      return ResponseEntity.status(401).body("Usuario no encontrado.");
    }

    Portfolio user = optionalUser.get();

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      return ResponseEntity.status(401).body("Contraseña incorrecta.");
    }

    String token = jwtService.generateToken(user.getEmail());
    return ResponseEntity.ok(new LoginResponse(token));
  }

  @PostMapping("/changePassword")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
    try {
      portfolioService.changePassword(
          request.getCurrentPassword(),
          request.getNewPassword(),
          request.getPasswordConfirmation());
      return ResponseEntity.ok("Contraseña actualizada correctamente.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
