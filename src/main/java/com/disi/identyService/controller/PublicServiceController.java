package com.disi.identyService.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.disi.identyService.model.PublicService;
import com.disi.identyService.service.PublicServicies;

@RestController
@RequestMapping("/users")
public class PublicServiceController {

    @Autowired
    private PublicServicies service;

    @PostMapping("/servicePublic/add")
    @CrossOrigin(origins = "*")
    public String addServicePublic(@RequestBody PublicService ps) {
        return service.savePublicService(ps);

    }

    @GetMapping("/servicePublic/all")
    @CrossOrigin(origins = "*")
    public List<PublicService> getAllPublicServicies() {
        return service.getAllPublicServices();
    }

    @GetMapping("/servicePublic/{id}")
    @CrossOrigin(origins = "*")
    public Optional<PublicService> getServicePublic(@PathVariable int id) {
        return service.getPublicService(id);
    }

    @PutMapping("/servicePublic/update/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Map<String, String>> updateServicePublic(@PathVariable int serviceId,
            @RequestBody PublicService ps) {
        Map<String, String> response = new HashMap<>();

        try {
            service.updatePublicService(serviceId, ps);
            response.put("message", "service publique mis à jour avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", "Erreur : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/servicePublic/{id}")
    @CrossOrigin(origins = "*")
    public void deletePublicService(@PathVariable("id") int id) {
        service.deleteServicies(id);
    }

}
