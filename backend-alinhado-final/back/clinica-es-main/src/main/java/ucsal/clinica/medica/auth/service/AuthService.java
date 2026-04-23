package ucsal.clinica.medica.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import ucsal.clinica.medica.auth.dto.LoginRequestDTO;
import ucsal.clinica.medica.auth.dto.LoginResponseDTO;
import ucsal.clinica.medica.auth.model.Usuario;
import ucsal.clinica.medica.security.TokenService;

@Service
public class AuthService {

	private final AuthenticationManager authManager;
	private final TokenService tokenService;

	public AuthService(AuthenticationManager authManager, TokenService tokenService) {
		this.authManager = authManager;
		this.tokenService = tokenService;
	}

	public LoginResponseDTO autenticar(LoginRequestDTO request) {
		var auth = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
		var user = (Usuario) auth.getPrincipal();
		String token = tokenService.gerarToken(user);
		return new LoginResponseDTO(token);
	}

}
