package com.apiportfolio.backendportfolio.security.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.apiportfolio.backendportfolio.config.AwsConfig;
import com.apiportfolio.backendportfolio.exceptions.ResourceNotFoundException;
import com.apiportfolio.backendportfolio.security.model.Portfolio;
import com.apiportfolio.backendportfolio.security.repository.PortfolioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PortfolioService {
    private static final Logger logger = Logger.getLogger(PortfolioService.class.getName());

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AwsConfig awsConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Obtener el único documento del portfolio
    public Portfolio getPortfolio() {
        return portfolioRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No portfolio document found."));
    }

    // Crear o actualizar el único documento, incluyendo la imagen de perfil en S3
    public Portfolio upsertPortfolio(String fullName, String position, String description,
            String email, String password, String phone, MultipartFile photo) {
        Optional<Portfolio> existingPortfolioOpt = portfolioRepository.findAll().stream().findFirst();
        Portfolio portfolio = existingPortfolioOpt.orElse(new Portfolio());

        // Validar y actualizar campos si no están vacíos
        if (fullName != null && !fullName.trim().isEmpty()) {
            portfolio.setFullName(fullName);
        }

        if (position != null && !position.trim().isEmpty()) {
            portfolio.setPosition(position);
        }

        if (description != null && !description.trim().isEmpty()) {
            portfolio.setDescription(description);
        }

        if (email != null && !email.trim().isEmpty()) {
            portfolio.setEmail(email);
        }

        if (password != null && !password.trim().isEmpty()) {
            portfolio.setPassword(password);
        }

        if (phone != null && !phone.trim().isEmpty()) {
            portfolio.setPhone(phone);
        }

        // Guardar la imagen en S3 si se proporciona
        if (photo != null && !photo.isEmpty()) {
            try {
                // Validar que el archivo sea una imagen
                if (!photo.getContentType().startsWith("image/")) {
                    throw new IllegalArgumentException("El archivo debe ser una imagen.");
                }

                String photoUrl = uploadFileToS3(photo);
                logger.info("URL de la imagen subida: " + photoUrl);
                portfolio.setPhoto(photoUrl);
            } catch (IOException e) {
                logger.severe("Error al subir la imagen a S3: " + e.getMessage());
                throw new RuntimeException("Error al guardar la imagen en S3", e);
            }
        }

        return portfolioRepository.save(portfolio);
    }

    // Método para subir la foto a Amazon S3 y devolver la URL
    private String uploadFileToS3(MultipartFile file) throws IOException {
        String bucketName = awsConfig.getAwsBucketName();

        // Extraer la extensión del archivo original
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        // Generar un nombre de archivo único
        String fileName = UUID.randomUUID().toString() + fileExtension;

        // Configurar metadatos
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // Subir el archivo a S3
        amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);

        // Devolver la URL pública del archivo en S3
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public void changePassword(String currentPassword, String newPassword, String passwordConfirmation) {
    Portfolio portfolio = portfolioRepository.findAll().stream().findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningún portfolio."));

    if (!passwordEncoder.matches(currentPassword, portfolio.getPassword())) {
        throw new IllegalArgumentException("La contraseña actual es incorrecta.");
    }

    if (!newPassword.equals(passwordConfirmation)) {
        throw new IllegalArgumentException("La nueva contraseña y la confirmación no coinciden.");
    }

    if (passwordEncoder.matches(newPassword, portfolio.getPassword())) {
        throw new IllegalArgumentException("La nueva contraseña no puede ser igual a la actual.");
    }

    // Reglas de seguridad opcionales
    if (newPassword.length() < 8) {
        throw new IllegalArgumentException("La nueva contraseña debe tener al menos 8 caracteres.");
    }

    portfolio.setPassword(passwordEncoder.encode(newPassword));
    portfolioRepository.save(portfolio);
}

}
