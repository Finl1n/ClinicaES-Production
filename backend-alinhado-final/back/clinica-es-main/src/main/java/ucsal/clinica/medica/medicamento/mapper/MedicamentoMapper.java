package ucsal.clinica.medica.medicamento.mapper;

import org.springframework.stereotype.Component;
import ucsal.clinica.medica.medicamento.dto.MedicamentoRequest;
import ucsal.clinica.medica.medicamento.dto.MedicamentoResponse;
import ucsal.clinica.medica.medicamento.dto.MedicamentoUpdate;
import ucsal.clinica.medica.medicamento.model.Medicamento;
import java.util.List;

@Component
public class MedicamentoMapper {

    public Medicamento build(MedicamentoRequest req) {
        Medicamento m = new Medicamento();
        m.setNome(req.nome());
        m.setDescricao(req.descricao());
        m.setQuantidade(req.quantidade());
        m.setUnidadeMedida(req.unidadeMedida());
        m.setValidade(req.validade());
        m.setAtivo(req.ativo() != null ? req.ativo() : true);
        return m;
    }

    public MedicamentoResponse toResponse(Medicamento m) {
        String status = Boolean.TRUE.equals(m.getAtivo()) ? "ATIVO" : "INATIVO";
        return new MedicamentoResponse(
                m.getId(),
                m.getNome(),
                m.getDescricao(),
                m.getQuantidade(),
                m.getUnidadeMedida(),
                m.getValidade(),
                m.getAtivo(),
                status
        );
    }

    public void update(Medicamento m, MedicamentoUpdate req) {
        if (req.nome() != null)          m.setNome(req.nome());
        if (req.descricao() != null)     m.setDescricao(req.descricao());
        if (req.quantidade() != null)    m.setQuantidade(req.quantidade());
        if (req.unidadeMedida() != null) m.setUnidadeMedida(req.unidadeMedida());
        if (req.validade() != null)      m.setValidade(req.validade());
        if (req.ativo() != null)         m.setAtivo(req.ativo());
    }

    public List<MedicamentoResponse> toResponse(List<Medicamento> list) {
        return list.stream().map(this::toResponse).toList();
    }
}