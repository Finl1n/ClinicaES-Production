package ucsal.clinica.medica.prontuario.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ucsal.clinica.medica.atendimento.model.Atendimento;
import ucsal.clinica.medica.paciente.model.Paciente;
import ucsal.clinica.medica.requisicao.model.UsoMedicacao;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prontuario")
public class Prontuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paciente_id", nullable = false)
	private Paciente paciente;

	@OneToMany(mappedBy = "prontuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Atendimento> atendimentos;

	@OneToMany(mappedBy = "prontuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<UsoMedicacao> usoMedicacoes;

}
