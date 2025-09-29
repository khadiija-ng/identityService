package com.disi.identyService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.disi.identyService.model.PublicService;

public interface PublicServiceRepository extends JpaRepository<PublicService, Integer> {
   Optional<PublicService> findById(int id);
   PublicService findByserviceId(int id);
}
