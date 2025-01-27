package com.apiportfolio.backendportfolio.repository;

import com.apiportfolio.backendportfolio.model.Portfolio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends MongoRepository<Portfolio, String> {

}
