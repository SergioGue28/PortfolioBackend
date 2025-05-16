package com.apiportfolio.backendportfolio.service;

import com.apiportfolio.backendportfolio.model.Project;
import com.apiportfolio.backendportfolio.repository.ProjectRepository;
import com.apiportfolio.backendportfolio.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    public Project getProjectById(String id) {
        return projectRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Project with ID: " + id + " not found"));
    }

    public Project createProject(Project project){
        if(project.getName() == null){
            throw new ValidationException("The name field cannot be empty.");
        }
        return projectRepository.save(project);
    }

    public Project updateProject(String id, Project project){
        if(!projectRepository.existsById(id)){
            throw new ResourceNotFoundException("Cannot update. Project with ID: " + id + " not found.");
        }
        project.setId(id);
        return projectRepository.save(project);
    }

    public void deleteProject(String id){
        if(!projectRepository.existsById(id)){
            throw new ResourceNotFoundException("Cannot delete. Project with ID: " + id + " not found");
        }
        projectRepository.deleteById(id);
    }
}