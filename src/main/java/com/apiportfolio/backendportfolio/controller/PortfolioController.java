package com.apiportfolio.backendportfolio.controller;

import com.apiportfolio.backendportfolio.model.Portfolio;
import com.apiportfolio.backendportfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
    @Autowired
    private PortfolioService portfolioService;

    // Obtener el único documento del portfolio
    @GetMapping
    public Portfolio getPortfolio() {
        return portfolioService.getPortfolio();
    }

    // Crear o actualizar el único documento del portfolio
    @PutMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Portfolio> upsertPortfolio(
            @RequestParam("fullName") String fullName,
            @RequestParam("position") String position,
            @RequestParam("description") String description,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {

        try {
            Portfolio updatedPortfolio = portfolioService.upsertPortfolio(fullName, position, description, photo);
            return ResponseEntity.ok(updatedPortfolio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Puedes mejorar este manejo de errores
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Manejo de errores genéricos
        }
    }
}
