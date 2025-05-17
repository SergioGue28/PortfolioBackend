package com.apiportfolio.backendportfolio.security.controller;

import com.apiportfolio.backendportfolio.security.model.Portfolio;
import com.apiportfolio.backendportfolio.security.service.PortfolioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<Portfolio> getPortfolio() {
        return ResponseEntity.ok(portfolioService.getPortfolio());
    }

    @PutMapping("/updateInformation")
    public ResponseEntity<Portfolio> updatePortfolio(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) MultipartFile photo) {

        Portfolio updatedPortfolio = portfolioService.upsertPortfolio(fullName, position, description, email, password,
                phone, photo);
        return ResponseEntity.ok(updatedPortfolio);
    }
}
