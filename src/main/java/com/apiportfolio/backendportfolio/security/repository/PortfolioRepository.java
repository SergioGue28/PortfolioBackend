package com.apiportfolio.backendportfolio.security.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.apiportfolio.backendportfolio.security.model.Portfolio;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends MongoRepository<Portfolio, String> {
  Optional<Portfolio> findByEmail(String email);
}
