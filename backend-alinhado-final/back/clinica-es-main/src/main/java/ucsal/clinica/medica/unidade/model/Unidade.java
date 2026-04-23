package ucsal.clinica.medica.unidade.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ucsal.clinica.medica.global.CentroCusto;

@Entity
@Table(name = "unidade")
@DiscriminatorValue("UNIDADE")
@Getter
@Setter
@NoArgsConstructor
public class Unidade extends CentroCusto {

    @Column(nullable = false)
    private String responsavel;
}
