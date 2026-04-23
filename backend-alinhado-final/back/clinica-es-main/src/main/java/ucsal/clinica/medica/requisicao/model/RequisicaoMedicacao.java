package ucsal.clinica.medica.requisicao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ucsal.clinica.medica.medicamento.model.Medicamento;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;
import ucsal.clinica.medica.requisicao.enums.TipoRequisicao;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requisicao_medicacao")
public class RequisicaoMedicacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medicamento_id", nullable = true)
	private Medicamento medicamento;

	@Column(name = "quantidade", nullable = false)
	private Long quantidade;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_requisicao", nullable = true)
	private TipoRequisicao tipoRequisicao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profissional_saude_id", nullable = true)
	private ProfissionalSaude profissionalSaude;

	@Column(name = "data", nullable = false)
	private String data;

	@Column(name = "observacao", columnDefinition = "TEXT")
	private String observacao;

}
