package ucsal.clinica.medica.paciente.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucsal.clinica.medica.escola.model.Escola;
import ucsal.clinica.medica.escola.repository.EscolaRepository;
import ucsal.clinica.medica.global.Status;
import ucsal.clinica.medica.paciente.dto.PacienteRequest;
import ucsal.clinica.medica.paciente.dto.PacienteResponse;
import ucsal.clinica.medica.paciente.enums.VinculoPaciente;
import ucsal.clinica.medica.paciente.mapper.PacienteMapper;
import ucsal.clinica.medica.paciente.model.Paciente;
import ucsal.clinica.medica.paciente.repository.PacienteRepository;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;
import ucsal.clinica.medica.profissional.repository.ProfissionalSaudeRepository;
import ucsal.clinica.medica.prontuario.model.Prontuario;
import ucsal.clinica.medica.prontuario.repository.ProntuarioRepository;
import ucsal.clinica.medica.unidade.model.Unidade;
import ucsal.clinica.medica.unidade.repository.UnidadeRepository;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    private final ProfissionalSaudeRepository profissionalRepository;
    private final EscolaRepository escolaRepository;
    private final UnidadeRepository unidadeRepository;
    private final ProntuarioRepository prontuarioRepository;

    @Transactional(readOnly = true)
    public List<PacienteResponse> findAllPacientes(String username) {
        ProfissionalSaude profissional = buscarProfissionalOuLancar(username);
        return pacienteMapper.toResponse(pacienteRepository.findAllByProfissional(profissional));
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> findAllPacientes() {
        return pacienteMapper.toResponse(pacienteRepository.findAll());
    }

    @Transactional
    public PacienteResponse createPaciente(PacienteRequest request, String username) {
        ProfissionalSaude profissional = buscarProfissionalOuLancar(username);

        if (!profissional.isCadastroCompleto()) {
            throw new RuntimeException(
                    "Você precisa completar seu cadastro profissional antes de cadastrar pacientes."
            );
        }

        if (request.email() != null && pacienteRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Paciente com e-mail '" + request.email() + "' já existe.");
        }

        Escola escola   = resolverEscola(request);
        Unidade unidade = resolverUnidade(request);

        Paciente paciente = pacienteMapper.build(request, profissional, escola, unidade);
        Paciente salvo = pacienteRepository.save(paciente);

        prontuarioRepository.findByPaciente(salvo).orElseGet(() -> {
            Prontuario prontuario = new Prontuario();
            prontuario.setPaciente(salvo);
            return prontuarioRepository.save(prontuario);
        });

        return pacienteMapper.toResponse(salvo);
    }

    @Transactional
    public PacienteResponse updatePaciente(Long id, PacienteRequest request, String username) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente com id: " + id + " não encontrado."));

        if (!paciente.getProfissional().getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("Acesso negado: este paciente não pertence ao seu cadastro.");
        }

        if (request.email() != null
                && !request.email().equals(paciente.getEmail())
                && pacienteRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Paciente com e-mail '" + request.email() + "' já existe.");
        }

        Escola escola   = resolverEscola(request);
        Unidade unidade = resolverUnidade(request);

        paciente.setNome(request.nome());
        paciente.setEmail(request.email());
        paciente.setTelefone(request.telefone());
        paciente.setCategoria(request.categoria());
        paciente.setVinculoTipo(request.vinculoTipo());
        paciente.setVinculoNome(request.vinculoNome());
        paciente.setEscola(escola);
        paciente.setUnidade(unidade);

        return pacienteMapper.toResponse(pacienteRepository.save(paciente));
    }

    @Transactional
    public void togglePaciente(Long id, String username) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente com id: " + id + " não encontrado."));

        if (!paciente.getProfissional().getUsuario().getUsername().equals(username)) {
            throw new RuntimeException("Acesso negado: este paciente não pertence ao seu cadastro.");
        }

        paciente.setStatus(paciente.getStatus() == Status.ATIVO ? Status.INATIVO : Status.ATIVO);
        pacienteRepository.save(paciente);
    }

    private ProfissionalSaude buscarProfissionalOuLancar(String username) {
        return profissionalRepository.findByUsuario_Username(username)
                .orElseThrow(() -> new RuntimeException(
                        "Perfil profissional não encontrado para o usuário: " + username
                ));
    }

    private Escola resolverEscola(PacienteRequest request) {
        if (request.vinculoTipo() != VinculoPaciente.ESCOLA) return null;
        if (request.escolaId() == null)
            throw new RuntimeException("Escola é obrigatória para vínculo do tipo ESCOLA.");
        return escolaRepository.findById(request.escolaId())
                .orElseThrow(() -> new RuntimeException("Escola não encontrada com id: " + request.escolaId()));
    }

    private Unidade resolverUnidade(PacienteRequest request) {
        if (request.vinculoTipo() != VinculoPaciente.UNIDADE) return null;
        if (request.unidadeId() == null)
            throw new RuntimeException("Unidade é obrigatória para vínculo do tipo UNIDADE.");
        return unidadeRepository.findById(request.unidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada com id: " + request.unidadeId()));
    }
}
