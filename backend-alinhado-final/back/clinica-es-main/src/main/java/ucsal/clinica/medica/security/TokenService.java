package ucsal.clinica.medica.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import jakarta.servlet.http.HttpServletRequest;
import ucsal.clinica.medica.auth.model.Usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * CORRIGIDO: o JWT agora inclui as claims que o frontend espera ao decodar o token.
 *
 * Frontend (auth.service.ts decodeJwt):
 *   payload["sub"]      → usado como id (parseInt) — agora é o id numérico do usuário
 *   payload["role"]     → RoleUsuario.name()
 *   payload["username"] → username para exibição
 *
 * Antes: sub = username (String), logo parseInt(sub) retornava NaN/0 no frontend.
 */
@Service
public class TokenService {

	@Value("${security.token.secret}")
	private String secret;

	@Value("${security.token.expiration}")
	private long expirationMs;

	private static final String ISSUER = "clinica-ucsal";

	public String gerarToken(Usuario usuario) {
		Algorithm algorithm = Algorithm.HMAC256(secret);

		return JWT.create()
				.withIssuer(ISSUER)
				// sub = id numérico (como String, padrão JWT) para parseInt no frontend
				.withSubject(String.valueOf(usuario.getId()))
				.withClaim("username", usuario.getUsername())
				.withClaim("role", usuario.getRole().name())
				.withExpiresAt(Instant.now().plus(expirationMs, ChronoUnit.MILLIS))
				.sign(algorithm);
	}

	/**
	 * Valida o token e retorna o USERNAME (não o sub/id).
	 * O SecurityFilter usa esse valor para carregar o UserDetails pelo username.
	 */
	public String validarToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			var decoded = JWT.require(algorithm).withIssuer(ISSUER).build().verify(token);
			// Retorna a claim "username", não o subject (que agora é o id)
			return decoded.getClaim("username").asString();
		} catch (JWTVerificationException e) {
			return null;
		}
	}

	public String extrairToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}
}
