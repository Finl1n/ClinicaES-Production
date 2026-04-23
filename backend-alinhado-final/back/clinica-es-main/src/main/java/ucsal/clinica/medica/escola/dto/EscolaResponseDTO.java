package ucsal.clinica.medica.escola.dto;

import ucsal.clinica.medica.escola.model.Escola;
import ucsal.clinica.medica.global.Status;

public record EscolaResponseDTO(
        Long id,
        String nome,
        String ies,
        String coordenador,
        Status status
) {

    public static EscolaResponseDTO from(Escola escola) {
        return new EscolaResponseDTO(
                escola.getId(),
                escola.getNome(),
                escola.getIes(),
                escola.getCoordenador(),
                escola.getStatus()
        );
    }
}