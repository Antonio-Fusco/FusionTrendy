package org.generation.italy.ecommerce.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDAO {
	// Questo metodo dovrà essere implementato da tutti i Dao per trovare un utente attraverso l'username
	// Servirà a SpringSecurity per verificare le credenziali
	Optional<? extends UserDetails> findByUsername(String username);
}
