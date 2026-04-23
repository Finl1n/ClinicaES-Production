package ucsal.clinica.medica.profissional.dto;

import jakarta.validation.constraints.NotBlank;

public record ProfissionalSaudeRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Username é obrigatório")
        String username,

        @NotBlank(message = "Senha é obrigatória")
        String password
) {
}
