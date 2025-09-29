package com.disi.identyService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.disi.identyService.filter.JwtAuthFilter;


// import com.disi.identyService.filter.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class AuthConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    
    public AuthConfig(JwtAuthFilter jwtAuthFilter){
     this.jwtAuthFilter=jwtAuthFilter;
    }

    @Bean
    public UserDetailsServiceInfo userDetailsService() {
        return new UserDetailsServiceInfo();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("---------------------------filter");
       
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/users/register", "/users/login")
                        .permitAll() // Permettre l'accès public à ces routes
                        .anyRequest().authenticated() // authentification requise pour toutes les autres requêtes par
                                                      // défaut
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Utiliser une politique sans session
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Ajouter le filtre JWT
                                                                                            // avant l'authentification
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }




    //, "/users/validate", "/users/role/{id}",
    //  "/users/{id}/roles", "/users/{userId}/role", "/users/all/{page}/{size}", "/users/{userId}/roles", "/users/{userId}/role", "/users/{userId}", "/users/role/{id}", "/users/refresh-token"
}
