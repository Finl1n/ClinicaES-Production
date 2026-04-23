package ucsal.clinica.medica.profissional.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfissionalSaudeComplementoRequestDTO(
        @NotBlank(message = "Especialidade é obrigatória")
        String especialidade,

        @NotBlank(message = "Formação é obrigatória")
        String formacao,

        @NotBlank(message = "Conselho é obrigatório")
        String conselho,

        @NotBlank(message = "Número de registro é obrigatório")
        String numeroRegistro,

        @NotBlank(message = "Dias de atendimento são obrigatórios")
        String diasAtendimento,

        @NotBlank(message = "Turnos de atendimento são obrigatórios")
        String turnosAtendimento
) {
}
