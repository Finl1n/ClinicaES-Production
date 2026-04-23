package ucsal.clinica.medica.medicamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record MedicamentoRequest(

		@NotBlank
		String nome,

		String descricao,

		@NotNull
		@Positive(message = "A quantidade deve ser maior que zero")
		Long quantidade,

		@NotBlank
		String unidadeMedida,

		LocalDate validade,

		Boolean ativo

) {}