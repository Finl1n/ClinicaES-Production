package ucsal.clinica.medica.escola.dto;

import jakarta.validation.constraints.NotBlank;

public record EscolaRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "IES é obrigatória")
        String ies,

        @NotBlank(message = "Coordenador é obrigatório")
        String coordenador
) {
}
