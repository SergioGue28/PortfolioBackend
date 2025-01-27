package com.apiportfolio.backendportfolio.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final Dotenv dotenv = Dotenv.load();

    @Override
    protected String getDatabaseName() {
        return dotenv.get("DATABASE_NAME");
    }

    @Bean
    public MongoClientSettings mongoClientSettings() {
        ConnectionString connectionString = new ConnectionString(dotenv.get("MONGODB_URI"));
        return MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
    }
}