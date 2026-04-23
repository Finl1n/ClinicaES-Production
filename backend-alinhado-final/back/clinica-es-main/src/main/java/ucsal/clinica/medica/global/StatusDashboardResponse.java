package ucsal.clinica.medica.global;

public record StatusDashboardResponse(
		Long totalEscolasAtivas,
		Long totalUnidadesAtivas,
		Long totalPacientes,
		Long totalAtendimentos,
		Long totalProfissionais,
		Long totalMedicacoes,
		Long totalMedicacoesAtivas,
		Long totalEstoqueBaixo,
		Long totalMedicacoesVencidas,
		Long totalRequisicoes
) {}
