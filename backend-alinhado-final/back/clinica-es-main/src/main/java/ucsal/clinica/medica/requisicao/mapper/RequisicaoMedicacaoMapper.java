package ucsal.clinica.medica.requisicao.mapper;

import org.springframework.stereotype.Component;
import ucsal.clinica.medica.medicamento.model.Medicamento;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;
import ucsal.clinica.medica.requisicao.dto.RequisicaoMedicacaoRequest;
import ucsal.clinica.medica.requisicao.dto.RequisicaoMedicacaoResponse;
import ucsal.clinica.medica.requisicao.model.RequisicaoMedicacao;
import java.util.List;

/**
 * CORRIGIDO:
 * - quantidade retornada como Long (sem .doubleValue())
 * - profissionalNome adicionado
 */
@Component
public class RequisicaoMedicacaoMapper {

    public RequisicaoMedicacao build(RequisicaoMedicacaoRequest request, Medicamento medicamento,
                                     ProfissionalSaude profissionalSaude) {
        RequisicaoMedicacao requisicao = new RequisicaoMedicacao();
        requisicao.setMedicamento(medicamento);
        requisicao.setQuantidade(request.quantidade());
        requisicao.setTipoRequisicao(request.tipo());
        requisicao.setProfissionalSaude(profissionalSaude);
        requisicao.setData(request.data());
        requisicao.setObservacao(request.observacao());
        return requisicao;
    }

    public RequisicaoMedicacaoResponse toResponse(RequisicaoMedicacao r) {
        return new RequisicaoMedicacaoResponse(
            r.getId(),
            r.getMedicamento() != null ? r.getMedicamento().getId() : null,
            r.getMedicamento() != null ? r.getMedicamento().getNome() : null,
            r.getQuantidade(),
            r.getTipoRequisicao(),
            r.getProfissionalSaude() != null ? r.getProfissionalSaude().getId() : null,
            r.getProfissionalSaude() != null ? r.getProfissionalSaude().getNome() : null,
            r.getData(),
            r.getObservacao()
        );
    }

    public List<RequisicaoMedicacaoResponse> toResponse(List<RequisicaoMedicacao> list) {
        return list.stream().map(this::toResponse).toList();
    }
}
