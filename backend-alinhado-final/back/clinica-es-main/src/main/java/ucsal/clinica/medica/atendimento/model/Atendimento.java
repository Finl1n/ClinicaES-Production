package ucsal.clinica.medica.atendimento.model;

import jakarta.persistence.*;
import lombok.*;
import ucsal.clinica.medica.paciente.model.Paciente;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;
import ucsal.clinica.medica.prontuario.model.Prontuario;

/**
 * CORRIGIDO: adicionados campos descricao, observacoes, dataAtendimento, statusAtendimento
 * que o frontend envia/espera em AtendimentoRequest/AtendimentoResponse.
 * Campos legados (sintomas, diagnostico, tratamento, tipoAtendimento) mantidos para
 * não quebrar o schema existente — serão removidos em refactor futuro.
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "atendimento")
public class Atendimento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paciente_id", nullable = false)
	private Paciente paciente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profissional_saude_id", nullable = false)
	private ProfissionalSaude profissionalSaude;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prontuario_id", nullable = true)
	private Prontuario prontuario;

	// ── Campos do contrato atual com o frontend ────────────────────────────────

	@Column(name = "descricao", columnDefinition = "TEXT")
	private String descricao;

	@Column(name = "observacoes", columnDefinition = "TEXT")
	private String observacoes;

	@Column(name = "data_atendimento")
	private String dataAtendimento;

	@Column(name = "status_atendimento")
	private String statusAtendimento;

	// ── Campos legados (schema existente — não remover sem migration) ──────────

	@Column(name = "data_inicio")
	private String dataInicio;

	@Column(name = "data_fim")
	private String dataFim;

	@Column(name = "sintomas")
	private String sintomas;

	@Column(name = "diagnostico")
	private String diagnostico;

	@Column(name = "tratamento")
	private String tratamento;
}
