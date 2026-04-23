package ucsal.clinica.medica.profissional.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import ucsal.clinica.medica.auth.enums.RoleUsuario;
import ucsal.clinica.medica.auth.model.Usuario;
import ucsal.clinica.medica.auth.repository.UsuarioRepository;
import ucsal.clinica.medica.global.Status;
import ucsal.clinica.medica.profissional.dto.ProfissionalSaudeComplementoRequestDTO;
import ucsal.clinica.medica.profissional.dto.ProfissionalSaudeRequestDTO;
import ucsal.clinica.medica.profissional.dto.ProfissionalSaudeResponse;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;
import ucsal.clinica.medica.profissional.repository.ProfissionalSaudeRepository;

@Service
public class ProfissionalSaudeService {

    private final ProfissionalSaudeRepository profissionalRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfissionalSaudeService(
            ProfissionalSaudeRepository profissionalRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.profissionalRepository = profissionalRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ProfissionalSaudeResponse cadastrar(ProfissionalSaudeRequestDTO request) {
        if (usuarioRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username '" + request.username() + "' já está em uso.");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setRole(RoleUsuario.PROFISSIONAL_SAUDE);
        usuarioRepository.save(usuario);

        ProfissionalSaude profissional = new ProfissionalSaude();
        profissional.setNome(request.nome());
        profissional.setUsuario(usuario);
        profissional.setStatus(Status.ATIVO);
        profissional.setCadastroCompleto(false);
        profissional.setDataCadastro(LocalDate.now());

        return ProfissionalSaudeResponse.from(profissionalRepository.save(profissional));
    }

    @Transactional
    public ProfissionalSaudeResponse completarCadastro(String username,
            ProfissionalSaudeComplementoRequestDTO request) {
        ProfissionalSaude profissional = buscarPorUsernameOuLancarExcecao(username);

        if (profissional.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Profissional inativo não pode atualizar o cadastro.");
        }

        profissional.setEspecialidade(request.especialidade());
        profissional.setFormacao(request.formacao());
        profissional.setConselho(request.conselho());
        profissional.setNumeroRegistro(request.numeroRegistro());
        profissional.setDiasAtendimento(request.diasAtendimento());
        profissional.setTurnosAtendimento(request.turnosAtendimento());
        if (profissional.getDataCadastro() == null) {
            profissional.setDataCadastro(LocalDate.now());
        }
        profissional.setCadastroCompleto(true);

        return ProfissionalSaudeResponse.from(profissionalRepository.save(profissional));
    }

    public ProfissionalSaudeResponse consultarPerfil(String username) {
        return ProfissionalSaudeResponse.from(buscarPorUsernameOuLancarExcecao(username));
    }

    public List<ProfissionalSaudeResponse> listarAtivos() {
        return profissionalRepository.findAllByStatus(Status.ATIVO)
                .stream()
                .map(ProfissionalSaudeResponse::from)
                .toList();
    }

    public ProfissionalSaudeResponse buscarPorId(Long id) {
        return ProfissionalSaudeResponse.from(buscarOuLancarExcecao(id));
    }

    @Transactional
    public void inativar(Long id) {
        ProfissionalSaude profissional = buscarOuLancarExcecao(id);

        if (profissional.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Profissional já está inativo.");
        }

        profissional.setStatus(Status.INATIVO);
        profissionalRepository.save(profissional);
    }

    public ProfissionalSaude buscarEntidadePorUsername(String username) {
        return buscarPorUsernameOuLancarExcecao(username);
    }

    private ProfissionalSaude buscarOuLancarExcecao(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado com id: " + id));
    }

    private ProfissionalSaude buscarPorUsernameOuLancarExcecao(String username) {
        return profissionalRepository.findByUsuario_Username(username)
                .orElseThrow(() -> new RuntimeException(
                        "Perfil profissional não encontrado para o usuário: " + username));
    }
}
