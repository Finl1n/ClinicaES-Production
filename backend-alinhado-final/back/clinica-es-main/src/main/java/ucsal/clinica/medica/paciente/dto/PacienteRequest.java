package ucsal.clinica.medica.paciente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ucsal.clinica.medica.paciente.enums.CategoriaPaciente;
import ucsal.clinica.medica.paciente.enums.VinculoPaciente;

public record PacienteRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @Email(message = "E-mail inválido")
        String email,

        String telefone,

        @NotNull(message = "Categoria é obrigatória")
        CategoriaPaciente categoria,

        @NotNull(message = "Vínculo é obrigatório")
        VinculoPaciente vinculoTipo,

        String vinculoNome,

        /** ID da escola (obrigatório quando vinculoTipo = ESCOLA) */
        Long escolaId,

        /** ID da unidade (obrigatório quando vinculoTipo = UNIDADE) */
        Long unidadeId

) {}
