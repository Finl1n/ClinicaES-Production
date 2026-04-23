package ucsal.clinica.medica.atendimento.dto;

import java.util.List;

public record AtendimentoResponse(

		Long id,
		Long pacienteId,
		String pacienteNome,
		Long profissionalId,
		String profissionalNome,
		String descricao,
		String observacoes,
		String dataAtendimento,
		String status,
		String dataInicio,
		String dataFim,
		String sintomas,
		String diagnostico,
		String tratamento,
		String tipo,
		List<UsoMedicacaoResponse> medicacoesUsadas

) {
	public record UsoMedicacaoResponse(
			Long medicacaoId,
			String medicacaoNome,
			Long quantidade,
			String dosagem
	) {}
}
