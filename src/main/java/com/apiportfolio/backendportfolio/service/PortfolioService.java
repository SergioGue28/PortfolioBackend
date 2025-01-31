package com.apiportfolio.backendportfolio.service;

import com.apiportfolio.backendportfolio.model.Portfolio;
import com.apiportfolio.backendportfolio.repository.PortfolioRepository;
import com.apiportfolio.backendportfolio.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;

    // Obtener el único documento del portfolio
    public Portfolio getPortfolio() {
        return portfolioRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No portfolio document found."));
    }

    // Crear o actualizar el único documento, incluyendo la imagen de perfil
    public Portfolio upsertPortfolio(String fullName, String position, String description, MultipartFile photo) {
        Optional<Portfolio> existingPortfolioOpt = portfolioRepository.findAll().stream().findFirst();

        Portfolio portfolio = existingPortfolioOpt.orElse(new Portfolio());

        // Validación de parámetros antes de setearlos
        if (fullName != null && !fullName.isEmpty()) {
            portfolio.setFullName(fullName);
        } else {
            throw new IllegalArgumentException("Full name cannot be empty");
        }

        if (position != null && !position.isEmpty()) {
            portfolio.setPosition(position);
        }

        if (description != null && !description.isEmpty()) {
            portfolio.setDescription(description);
        }

        // Guardado de la imagen de perfil, si existe
        if (photo != null && !photo.isEmpty()) {
            try {
                String photoUrl = saveFile(photo);
                portfolio.setPhoto(photoUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar la imagen", e);
            }
        }

        return portfolioRepository.save(portfolio);
    }

    // Simulación de guardado de archivos, aquí puedes usar un servicio de almacenamiento real
    private String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        // Aquí puedes implementar lógica para guardar en almacenamiento externo (ejemplo: S3, servidor local, etc.)
        return "/img/" + fileName;
    }
}
