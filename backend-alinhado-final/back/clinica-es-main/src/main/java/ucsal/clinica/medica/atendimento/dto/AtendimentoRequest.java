package ucsal.clinica.medica.atendimento.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * BUG FIX #3: adicionado campo `medicacoesUsadas` no request.
 *
 * O front (atendimentos.component.ts) envia o payload com essa lista:
 * <pre>
 * {
 *   pacienteId: 1,
 *   profissionalId: 2,
 *   descricao: "...",
 *   observacoes: "...",
 *   dataAtendimento: "2026-04-22",
 *   status: "CONSULTA",
 *   medicacoesUsadas: [
 *     { medicacaoId: 3, quantidade: 2, dosagem: "500mg" }
 *   ]
 * }
 * </pre>
 *
 * Sem esse campo no record Java, o Spring simplesmente ignorava a lista e
 * o AtendimentoService nunca sabia quais medicações foram usadas.
 */
public record AtendimentoRequest(

		@NotNull(message = "Paciente é obrigatório")
		Long pacienteId,

		Long profissionalId,

		String descricao,
		String observacoes,
		String dataAtendimento,
		String status,

		/** Lista de medicações utilizadas no atendimento. Pode ser null ou vazia. */
		List<UsoMedicacaoRequest> medicacoesUsadas

) {

	/**
	 * Nested record — representa uma medicação utilizada no atendimento.
	 * Espelha o shape que o front envia dentro de `medicacoesUsadas[]`.
	 */
	public record UsoMedicacaoRequest(

			@NotNull(message = "ID da medicação é obrigatório")
			Long medicacaoId,

			String medicacaoNome,

			@NotNull(message = "Quantidade é obrigatória")
			Long quantidade,

			String dosagem

	) {}
}