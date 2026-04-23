package ucsal.clinica.medica.unidade.dto;

import ucsal.clinica.medica.global.Status;
import ucsal.clinica.medica.unidade.model.Unidade;

public record UnidadeResponseDTO(
        Long id,
        String nome,
        String ies,
        String responsavel,
        Status status
) {
    public static UnidadeResponseDTO from(Unidade unidade) {
        return new UnidadeResponseDTO(
                unidade.getId(),
                unidade.getNome(),
                unidade.getIes(),
                unidade.getResponsavel(),
                unidade.getStatus()
        );
    }
}