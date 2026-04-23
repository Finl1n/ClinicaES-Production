package ucsal.clinica.medica.atendimento.dto;

import jakarta.validation.constraints.NotNull;

/**
 * CORRIGIDO: campos alinhados com o PUT /atendimento do frontend.
 * Frontend envia: id, pacienteId, profissionalId, descricao, observacoes, dataAtendimento, status.
 */
public record AtendimentoUpdate(

		@NotNull(message = "Id é obrigatório para atualização")
		Long id,
		Long pacienteId,
		Long profissionalId,
		String descricao,
		String observacoes,
		String dataAtendimento,
		String status

) {}
