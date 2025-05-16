package com.apiportfolio.backendportfolio.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.apiportfolio.backendportfolio.model.Project;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
}