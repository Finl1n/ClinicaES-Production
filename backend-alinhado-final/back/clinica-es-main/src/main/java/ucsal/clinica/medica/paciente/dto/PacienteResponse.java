package ucsal.clinica.medica.paciente.dto;

import ucsal.clinica.medica.global.Status;
import ucsal.clinica.medica.paciente.enums.CategoriaPaciente;
import ucsal.clinica.medica.paciente.enums.VinculoPaciente;

/**
 * CORRIGIDO: adicionado prontuarioId (campo opcional que o frontend usa para
 * navegar direto ao prontuário do paciente sem busca adicional).
 */
public record PacienteResponse(

        Long id,
        String nome,
        String email,
        String telefone,
        Status status,
        CategoriaPaciente categoria,
        VinculoPaciente vinculoTipo,
        String vinculoNome,
        Long escolaId,
        String escolaNome,
        Long unidadeId,
        String unidadeNome,
        Long prontuarioId

) {}
