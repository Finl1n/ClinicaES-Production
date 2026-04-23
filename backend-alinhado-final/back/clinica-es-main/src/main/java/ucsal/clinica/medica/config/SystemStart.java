package ucsal.clinica.medica.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ucsal.clinica.medica.auth.enums.RoleUsuario;
import ucsal.clinica.medica.auth.model.Usuario;
import ucsal.clinica.medica.auth.repository.UsuarioRepository;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;
import ucsal.clinica.medica.profissional.repository.ProfissionalSaudeRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class SystemStart implements CommandLineRunner {

	@Value("${settings.security.user:admin}")
	private String adminUsername;

	@Value("${settings.security.password:admin123}")
	private String adminPassword;

	@Value("${settings.security.profissional.user:profissional}")
	private String profissionalUsername;

	@Value("${settings.security.profissional.password:profissional123}")
	private String profissionalPassword;

	private final UsuarioRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final ProfissionalSaudeRepository profissionalRepository;

	@Override
	public void run(String... args) throws Exception {
		generateAdminUser();
		generateDefaultProfissional();
	}

	/**
	 * Cria o usuário ADMINISTRADOR padrão na primeira inicialização.
	 */
	public void generateAdminUser() {
		if (!repository.existsByUsername(adminUsername)) {
			Usuario user = new Usuario();
			user.setUsername(adminUsername);
			user.setPassword(passwordEncoder.encode(adminPassword));
			user.setRole(RoleUsuario.ADMINISTRADOR);
			try {
				repository.save(user);
				log.info(">>>> Default ADMIN user created: {}", adminUsername);
			} catch (DataIntegrityViolationException e) {
				log.warn(">>>> Default ADMIN user '{}' was created by another instance concurrently. Skipping.",
						adminUsername);
			}
		} else {
			log.info(">>>> Default ADMIN user '{}' already exists. Skipping creation.", adminUsername);
		}
	}

	/**
	 * CORREÇÃO: cria um usuário PROFISSIONAL_SAUDE padrão na primeira
	 * inicialização, junto com o registro em profissional_saude.
	 * Sem isso, o login com role PROFISSIONAL_SAUDE retornava 401
	 * pois nenhum usuário desse tipo existia no banco após o primeiro boot.
	 *
	 * Credenciais padrão:
	 *   username: profissional  (sobrescrevível via settings.security.profissional.user)
	 *   password: profissional123 (sobrescrevível via settings.security.profissional.password)
	 */
	private void generateDefaultProfissional() {
		if (!repository.existsByUsername(profissionalUsername)) {
			try {
				Usuario usuario = new Usuario();
				usuario.setUsername(profissionalUsername);
				usuario.setPassword(passwordEncoder.encode(profissionalPassword));
				usuario.setRole(RoleUsuario.PROFISSIONAL_SAUDE);
				repository.save(usuario);

				ProfissionalSaude profissional = new ProfissionalSaude();
				profissional.setNome("Profissional Padrão");
				profissional.setUsuario(usuario);
				profissionalRepository.save(profissional);

				log.info(">>>> Default PROFISSIONAL user created: {} / senha: {}",
						profissionalUsername, profissionalPassword);
			} catch (DataIntegrityViolationException e) {
				log.warn(">>>> Default PROFISSIONAL user '{}' was created by another instance concurrently. Skipping.",
						profissionalUsername);
			}
		} else {
			log.info(">>>> Default PROFISSIONAL user '{}' already exists. Skipping creation.", profissionalUsername);
		}
	}
}