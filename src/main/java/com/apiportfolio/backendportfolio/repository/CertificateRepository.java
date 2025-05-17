package com.apiportfolio.backendportfolio.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.apiportfolio.backendportfolio.model.Certificate;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends MongoRepository<Certificate, String> {

}
