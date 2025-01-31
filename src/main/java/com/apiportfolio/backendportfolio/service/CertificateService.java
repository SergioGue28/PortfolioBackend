package com.apiportfolio.backendportfolio.service;

import com.apiportfolio.backendportfolio.model.Certificate;
import com.apiportfolio.backendportfolio.repository.CertificateRepository;
import com.apiportfolio.backendportfolio.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {
    
    @Autowired
    private CertificateRepository certificateRepository;

    public List<Certificate> getAllCertificates(){
        return certificateRepository.findAll();
    }

    public Certificate getCertificateById(String id) {
        return certificateRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Certificate with ID: " + id + "not found"));     
    }

    public Certificate createCertificate(Certificate certificate){
        if(certificate.getName() == null){
            throw new ValidationException("The name field cannot be empty.");
        }
        return certificateRepository.save(certificate);
    }

    public Certificate updateCertificate(String id, Certificate certificate){
        if(!certificateRepository.existsById(id)){
            throw new ResourceNotFoundException("Cannot update. Certificate with ID: " + id + " not found.");
        }
        certificate.setId(id);
        return certificateRepository.save(certificate);
    }

    public void deleteCertificate(String id){
        if(!certificateRepository.existsById(id)){
            throw new  ResourceNotFoundException("Cannot delete. Certificate with ID: " + id + "not found");
        }
        certificateRepository.deleteById(id);
    }

}
