package com.disi.identyService.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.disi.identyService.advice.UserNotFoundException;
import com.disi.identyService.model.PublicService;
import com.disi.identyService.repository.PublicServiceRepository;

@Service
public class PublicServicies {

    @Autowired
    private PublicServiceRepository repository;

    public String savePublicService(PublicService ps) {
        repository.save(ps);
        return "Service publique ajouté avec succés";
    }

    public List<PublicService> getAllPublicServices() {
        return repository.findAll();
    }

    public Optional<PublicService> getPublicService(int serviceId) {
        return repository.findById(serviceId);
    }

    public PublicService updatePublicService(int id, PublicService publics) {
        PublicService ps = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        ps.setName(publics.getName());
        ps.setAddress(publics.getAddress());
        ps.setPhoneUrgence(publics.getPhoneUrgence());
        return repository.save(ps);

    }

    public void deleteServicies(int id) {
        PublicService ps = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        repository.delete(ps);

    }
}
