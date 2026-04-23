package ucsal.clinica.medica.medicamento.dto;

import java.time.LocalDate;

public record MedicamentoResponse(
		Long id,
		String nome,
		String descricao,
		Long quantidade,
		String unidadeMedida,
		LocalDate validade,
		Boolean ativo,
		String status
) {}