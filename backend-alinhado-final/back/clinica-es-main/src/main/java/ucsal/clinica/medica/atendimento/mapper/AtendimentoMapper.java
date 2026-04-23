package ucsal.clinica.medica.atendimento.mapper;

import org.springframework.stereotype.Component;
import ucsal.clinica.medica.atendimento.dto.AtendimentoRequest;
import ucsal.clinica.medica.atendimento.dto.AtendimentoResponse;
import ucsal.clinica.medica.atendimento.dto.AtendimentoUpdate;
import ucsal.clinica.medica.atendimento.model.Atendimento;
import ucsal.clinica.medica.paciente.model.Paciente;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;
import ucsal.clinica.medica.requisicao.model.UsoMedicacao;

import java.util.Collections;
import java.util.List;

@Component
public class AtendimentoMapper {

    public Atendimento build(AtendimentoRequest req, Paciente paciente, ProfissionalSaude profissional) {
        Atendimento a = new Atendimento();
        a.setPaciente(paciente);
        a.setProfissionalSaude(profissional);
        a.setDescricao(req.descricao());
        a.setObservacoes(req.observacoes());
        a.setDataAtendimento(req.dataAtendimento());
        a.setStatusAtendimento(req.status());
        return a;
    }

    public AtendimentoResponse toResponse(Atendimento a) {
        return toResponse(a, Collections.emptyList());
    }

    public AtendimentoResponse toResponse(Atendimento a, List<UsoMedicacao> usos) {
        return new AtendimentoResponse(
            a.getId(),
            a.getPaciente() != null ? a.getPaciente().getId() : null,
            a.getPaciente() != null ? a.getPaciente().getNome() : null,
            a.getProfissionalSaude() != null ? a.getProfissionalSaude().getId() : null,
            a.getProfissionalSaude() != null ? a.getProfissionalSaude().getNome() : null,
            a.getDescricao(),
            a.getObservacoes(),
            a.getDataAtendimento(),
            a.getStatusAtendimento(),
            a.getDataInicio(),
            a.getDataFim(),
            a.getSintomas(),
            a.getDiagnostico(),
            a.getTratamento(),
            a.getStatusAtendimento(),
            usos == null ? Collections.emptyList() : usos.stream().map(this::toUsoResponse).toList()
        );
    }

    private AtendimentoResponse.UsoMedicacaoResponse toUsoResponse(UsoMedicacao uso) {
        return new AtendimentoResponse.UsoMedicacaoResponse(
            uso.getMedicamento() != null ? uso.getMedicamento().getId() : null,
            uso.getMedicamento() != null ? uso.getMedicamento().getNome() : null,
            uso.getQuantidade(),
            uso.getDosagem()
        );
    }

    public List<AtendimentoResponse> toResponse(List<Atendimento> list) {
        return list.stream().map(this::toResponse).toList();
    }

    public void update(Atendimento a, Paciente paciente, ProfissionalSaude profissional,
                       AtendimentoUpdate req) {
        if (paciente != null) a.setPaciente(paciente);
        if (profissional != null) a.setProfissionalSaude(profissional);
        if (req.descricao() != null) a.setDescricao(req.descricao());
        if (req.observacoes() != null) a.setObservacoes(req.observacoes());
        if (req.dataAtendimento() != null) a.setDataAtendimento(req.dataAtendimento());
        if (req.status() != null) a.setStatusAtendimento(req.status());
    }
}
