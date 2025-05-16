package com.apiportfolio.backendportfolio.controller;

import com.apiportfolio.backendportfolio.model.Certificate;
import com.apiportfolio.backendportfolio.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @GetMapping
    public List<Certificate> getAllCertificates(){
        return certificateService.getAllCertificates();
    }

    @GetMapping("/{id}")
    public Certificate getCertificateById(@PathVariable String id){
        return certificateService.getCertificateById(id);
    }

    @PostMapping("/addCertificate")
    public Certificate createCertificate(@RequestParam("name") String name, 
                                         @RequestParam("image") MultipartFile image) throws IOException {
        return certificateService.createCertificate(name, image);
    }

    @PutMapping("/{id}")
    public Certificate updateCertificate(@PathVariable String id, @RequestBody Certificate certificate){
        return certificateService.updateCertificate(id, certificate);
    }

    @DeleteMapping("/{id}")
    public void deleteCertificate(@PathVariable String id){
        certificateService.deleteCertificate(id);
    }
}
