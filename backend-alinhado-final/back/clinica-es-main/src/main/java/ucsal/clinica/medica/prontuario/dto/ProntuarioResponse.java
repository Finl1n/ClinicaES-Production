package ucsal.clinica.medica.prontuario.dto;

import java.util.List;

/**
 * CORRIGIDO: usa AtendimentoResponse (DTO) em vez de Atendimento (entidade JPA).
 * Antes expunha a entidade diretamente, o que causava:
 *  - LazyInitializationException ao serializar relações lazy
 *  - Risco de loop de serialização bidirecional
 *  - Exposição de campos internos não desejados
 */
public record ProntuarioResponse(

        Long id,
        Long pacienteId,
        String pacienteNome,
        List<?> atendimentos

) {}
