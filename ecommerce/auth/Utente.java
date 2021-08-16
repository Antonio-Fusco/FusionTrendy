package org.generation.italy.ecommerce.auth;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.generation.italy.ecommerce.security.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Implementiamo UserDetails, l'interfaccia che offre SpringSecurity per avere
 * una gestione con permessi di livelli differenti
 * 
 * @author lucaf
 *
 */
@Entity
public class Utente implements UserDetails {

	private static final Map<String, Collection<? extends GrantedAuthority>> AUTHORITIES = new HashMap<>();

	{
		AUTHORITIES.put(Roles.ADMIN, Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("ROLE_ADMIN"),
				new SimpleGrantedAuthority("management"), }));
		AUTHORITIES.put(Roles.USER, Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("ROLE_USER") }));

	}

	public static Collection<? extends GrantedAuthority> getAuthorityOfRole(String role) {
		return AUTHORITIES.get(role);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1237489217380966710L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(unique = true) // Non possono esserci due utenti con stessa email
	private String email;
	@Column(unique = true)
	private String username;
	private String password;
	private String ruolo;

	public Utente() {
	}

	public Utente(int id, String email, String username, String password, String ruolo) {
		super();
		this.id = id;
		this.email = email;
		this.username = username;
		this.password = password;
		this.ruolo = ruolo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * I permessi di ogni utente, può servire se gestiamo i livelli di accesso a
	 * seconda dei permessi di ogni utente Nel nostro caso ne faremo a meno, ma vi
	 * lascio un esempio di come si può implementare. Ci baseremo solamente sul
	 * ruolo dell'utente
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getAuthorityOfRole(this.ruolo);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * A seconda se vi interessa avere certe funzionalità, implementate i campi
	 * appositi Questi sono richiesti dall'interfaccia. Per me valgono sempre true.
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "Utente [id=" + id + ", email=" + email + ", username=" + username + ", password=" + password
				+ ", ruolo=" + ruolo + "]";
	}

}
