package ucsal.clinica.medica.paciente.model;

import jakarta.persistence.*;
import lombok.*;
import ucsal.clinica.medica.escola.model.Escola;
import ucsal.clinica.medica.global.Status;
import ucsal.clinica.medica.paciente.enums.CategoriaPaciente;
import ucsal.clinica.medica.paciente.enums.VinculoPaciente;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;
import ucsal.clinica.medica.unidade.model.Unidade;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_paciente", nullable = false)
    private CategoriaPaciente categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "vinculo_paciente", nullable = false)
    private VinculoPaciente vinculoTipo;

    @Column(name = "vinculo_nome")
    private String vinculoNome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escola_id")
    private Escola escola;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    /**
     * Profissional que cadastrou/gerencia este paciente.
     * Garante isolamento: cada profissional só vê seus próprios pacientes.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id", nullable = false)
    private ProfissionalSaude profissional;
}
