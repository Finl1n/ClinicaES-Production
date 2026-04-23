package ucsal.clinica.medica.medicamento.dto;

import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record MedicamentoUpdate(

		Long id,
		String nome,
		String descricao,

		@Positive(message = "A quantidade deve ser maior que zero")
		Long quantidade,

		String unidadeMedida,
		LocalDate validade,
		Boolean ativo

) {}