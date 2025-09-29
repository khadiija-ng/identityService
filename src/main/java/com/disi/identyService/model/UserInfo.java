package com.disi.identyService.model;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    private String firstname;
    private String lastname;
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    private String lieuDeNaissance;
    private Date dateDeNaissance;
    @ManyToOne
    @JoinColumn(name = "publicServiceId") 
    private PublicService servicePublique;
    private String password;
    private boolean isEnabled;
    public Object map(Object object) {
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }
}


