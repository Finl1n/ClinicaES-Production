package ucsal.clinica.medica.unidade.dto;

import jakarta.validation.constraints.NotBlank;

public record UnidadeRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "IES é obrigatória")
        String ies,

        @NotBlank(message = "Responsável é obrigatório")
        String responsavel
) {
}
