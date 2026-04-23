package ucsal.clinica.medica.escola.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ucsal.clinica.medica.global.CentroCusto;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "escola")
@DiscriminatorValue("ESCOLA")
public class Escola extends CentroCusto {

	@Column(nullable = false)
	private String coordenador;

}
