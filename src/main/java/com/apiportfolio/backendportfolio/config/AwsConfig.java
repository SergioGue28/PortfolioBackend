package com.apiportfolio.backendportfolio.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.apiportfolio.backendportfolio.exceptions.MissingConfigurationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${aws.accessKeyId}") 
    private String awsAccessKeyId;

    @Value("${aws.secretAccessKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.bucketName}") // Añade esta línea si tienes el nombre del bucket en application.properties
    private String awsBucketName;

    @Bean
    public AmazonS3 amazonS3() {
        if (awsAccessKeyId.isEmpty() || awsSecretKey.isEmpty() || awsRegion.isEmpty() || awsBucketName.isEmpty()) {
            System.err.println("❌ ERROR: Faltan variables de configuración de AWS.");
            throw new MissingConfigurationException("Faltan variables de configuración para AWS.");
        }

        System.out.println("✅ AWS Configuración cargada correctamente.");
        System.out.println("🔑 AWS Access Key ID: " + awsAccessKeyId);
        System.out.println("🌍 AWS Región: " + awsRegion);
        System.out.println("🪣 AWS Bucket Name: " + awsBucketName);

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        // Verificar si el bucket existe
        if (!s3Client.doesBucketExistV2(awsBucketName)) {
            throw new RuntimeException("❌ El bucket '" + awsBucketName + "' no existe o no tienes permisos.");
        }

        System.out.println("✅ El bucket '" + awsBucketName + "' existe y está accesible.");
        return s3Client;
    }

    // Método para obtener el nombre del bucket
    public String getAwsBucketName() {
        return awsBucketName;
    }
}
