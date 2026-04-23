package ucsal.clinica.medica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import ucsal.clinica.medica.auth.repository.UsuarioRepository;

@Configuration
@RequiredArgsConstructor
public class SystemConfiguration {

	private final UsuarioRepository repository;

	@Bean
	UserDetailsService userDetailsService() {
		return identifier -> repository.findByUsername(identifier)
				.orElseThrow(() -> new RuntimeException("User not found with identifier: " + identifier));
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

}
