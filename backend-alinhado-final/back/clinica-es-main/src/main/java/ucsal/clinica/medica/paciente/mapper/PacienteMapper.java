package ucsal.clinica.medica.paciente.mapper;

import org.springframework.stereotype.Component;
import ucsal.clinica.medica.escola.model.Escola;
import ucsal.clinica.medica.global.Status;
import ucsal.clinica.medica.paciente.dto.PacienteRequest;
import ucsal.clinica.medica.paciente.dto.PacienteResponse;
import ucsal.clinica.medica.paciente.model.Paciente;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;
import ucsal.clinica.medica.prontuario.repository.ProntuarioRepository;
import ucsal.clinica.medica.unidade.model.Unidade;

import java.util.List;

/**
 * CORRIGIDO: toResponse busca o prontuarioId via ProntuarioRepository para incluir no response.
 */
@Component
public class PacienteMapper {

    private final ProntuarioRepository prontuarioRepository;

    public PacienteMapper(ProntuarioRepository prontuarioRepository) {
        this.prontuarioRepository = prontuarioRepository;
    }

    public Paciente build(PacienteRequest req, ProfissionalSaude profissional,
                          Escola escola, Unidade unidade) {
        Paciente p = new Paciente();
        p.setNome(req.nome());
        p.setEmail(req.email());
        p.setTelefone(req.telefone());
        p.setStatus(Status.ATIVO);
        p.setCategoria(req.categoria());
        p.setVinculoTipo(req.vinculoTipo());
        p.setVinculoNome(req.vinculoNome());
        p.setEscola(escola);
        p.setUnidade(unidade);
        p.setProfissional(profissional);
        return p;
    }

    public PacienteResponse toResponse(Paciente p) {
        // Busca o prontuário associado ao paciente (pode ser null se ainda não existe)
        Long prontuarioId = prontuarioRepository.findByPaciente(p)
            .map(pr -> pr.getId())
            .orElse(null);

        return new PacienteResponse(
            p.getId(),
            p.getNome(),
            p.getEmail(),
            p.getTelefone(),
            p.getStatus(),
            p.getCategoria(),
            p.getVinculoTipo(),
            p.getVinculoNome(),
            p.getEscola() != null ? p.getEscola().getId()   : null,
            p.getEscola() != null ? p.getEscola().getNome() : null,
            p.getUnidade() != null ? p.getUnidade().getId()   : null,
            p.getUnidade() != null ? p.getUnidade().getNome() : null,
            prontuarioId
        );
    }

    public List<PacienteResponse> toResponse(List<Paciente> list) {
        return list.stream().map(this::toResponse).toList();
    }
}
