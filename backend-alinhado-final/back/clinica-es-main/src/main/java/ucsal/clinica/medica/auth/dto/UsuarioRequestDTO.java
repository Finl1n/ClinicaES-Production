package ucsal.clinica.medica.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank String login,
        @Size(min = 8) String senha,
        @NotNull Long pessoaId
) {}