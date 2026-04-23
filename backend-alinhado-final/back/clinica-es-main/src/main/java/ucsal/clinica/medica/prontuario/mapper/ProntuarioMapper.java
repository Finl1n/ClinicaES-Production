// package ucsal.clinica.medica.prontuario.mapper;

// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Component;
// import ucsal.clinica.medica.atendimento.mapper.AtendimentoMapper;
// import ucsal.clinica.medica.prontuario.dto.ProntuarioResponse;
// import ucsal.clinica.medica.prontuario.model.Prontuario;
// import java.util.Collections;
// import java.util.List;


// @Component
// @RequiredArgsConstructor
// public class ProntuarioMapper {

//     private final AtendimentoMapper atendimentoMapper;

//     public ProntuarioResponse toResponse(Prontuario p) {
//         var atendimentos = p.getAtendimentos() != null
//             ? atendimentoMapper.toResponse(p.getAtendimentos())
//             : Collections.emptyList();

//         return new ProntuarioResponse(
//             p.getId(),
//             p.getPaciente() != null ? p.getPaciente().getId() : null,
//             p.getPaciente() != null ? p.getPaciente().getNome() : null,
//             atendimentos
//         );
//     }/**
//  * CORRIGIDO: usa AtendimentoMapper para converter a lista de atendimentos para DTOs.
//  * Antes passava a List<Atendimento> (entidade) direto no record, causando LazyInitializationException.
//  */

//     public List<ProntuarioResponse> toResponse(List<Prontuario> list) {
//         return list.stream().map(this::toResponse).toList();
//     }
// }

package ucsal.clinica.medica.prontuario.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ucsal.clinica.medica.atendimento.dto.AtendimentoResponse;
import ucsal.clinica.medica.atendimento.mapper.AtendimentoMapper;
import ucsal.clinica.medica.prontuario.dto.ProntuarioResponse;
import ucsal.clinica.medica.prontuario.model.Prontuario;
import ucsal.clinica.medica.requisicao.repository.UsoMedicacaoRepository;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProntuarioMapper {

    private final AtendimentoMapper atendimentoMapper;
    private final UsoMedicacaoRepository usoMedicacaoRepository;

    public ProntuarioResponse toResponse(Prontuario p) {
        List<AtendimentoResponse> atendimentos = p.getAtendimentos() != null
                ? p.getAtendimentos().stream()
                    .map(atendimento -> atendimentoMapper.toResponse(
                            atendimento,
                            usoMedicacaoRepository.findAllByAtendimento_Id(atendimento.getId())
                    ))
                    .toList()
                : Collections.emptyList();

        return new ProntuarioResponse(
                p.getId(),
                p.getPaciente() != null ? p.getPaciente().getId() : null,
                p.getPaciente() != null ? p.getPaciente().getNome() : null,
                atendimentos
        );
    }

    public List<ProntuarioResponse> toResponse(List<Prontuario> list) {
        return list.stream().map(this::toResponse).toList();
    }
}
