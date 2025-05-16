package com.apiportfolio.backendportfolio.service;

import com.apiportfolio.backendportfolio.config.AwsConfig;
import com.apiportfolio.backendportfolio.exceptions.FileTooLargeException;
import com.apiportfolio.backendportfolio.exceptions.ResourceNotFoundException;
import com.apiportfolio.backendportfolio.exceptions.S3UploadException;
import com.apiportfolio.backendportfolio.exceptions.ValidationException;
import com.apiportfolio.backendportfolio.model.Certificate;
import com.apiportfolio.backendportfolio.repository.CertificateRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private AwsConfig awsConfig; // Inyectamos AwsConfig para obtener el bucket name

    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    public Certificate getCertificateById(String id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate with ID: " + id + " not found"));
    }

    public Certificate createCertificate(String name, MultipartFile image) throws IOException {
        // Validación de nombre
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("The name field cannot be empty.");
        }

        // Subir la imagen a S3
        String imageUrl = uploadImageToS3(image);

        // Crear y guardar el certificado
        Certificate certificate = new Certificate();
        certificate.setName(name);
        certificate.setImageUrl(imageUrl);
        return certificateRepository.save(certificate);
    }

    public Certificate updateCertificate(String id, Certificate certificate) {
        if (!certificateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot update. Certificate with ID: " + id + " not found.");
        }
        certificate.setId(id);
        return certificateRepository.save(certificate);
    }

    public void deleteCertificate(String id) {
        if (!certificateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Certificate with ID: " + id + " not found.");
        }
        certificateRepository.deleteById(id);
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        // Validar tamaño del archivo (por ejemplo, limitar a 5 MB)
        if (image.getSize() > 5 * 1024 * 1024) {
            throw new FileTooLargeException("The file is too large. Maximum size allowed is 5 MB.");
        }

        try {
            // Generar un nombre único para la imagen
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();

            // Crear metadatos con el tamaño del archivo y tipo de contenido
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());

            // Obtener el stream del archivo
            InputStream inputStream = image.getInputStream();

            // Subir la imagen al bucket de S3 con metadata
            amazonS3.putObject(new PutObjectRequest(awsConfig.getAwsBucketName(), fileName, inputStream, metadata));

            // Retornar la URL pública del archivo en S3
            return amazonS3.getUrl(awsConfig.getAwsBucketName(), fileName).toString();
        } catch (AmazonS3Exception e) {
            throw new S3UploadException("S3 error: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new IOException("File reading error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new S3UploadException("Unexpected error: " + e.getMessage(), e);
        }
    }
}
