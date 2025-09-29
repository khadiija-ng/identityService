package com.disi.identyService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int serviceId;
    private String name;
    private String address;
    private int phoneUrgence;
    
}
