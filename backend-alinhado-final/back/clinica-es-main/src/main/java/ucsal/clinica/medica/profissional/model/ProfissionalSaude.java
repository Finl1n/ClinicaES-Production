package ucsal.clinica.medica.profissional.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ucsal.clinica.medica.auth.model.Usuario;
import ucsal.clinica.medica.global.Status;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profissional_saude")
public class ProfissionalSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "formacao")
    private String formacao;

    private String especialidade;
    private String conselho;

    @Column(name = "numero_registro")
    private String numeroRegistro;

    @Column(name = "dias_atendimento")
    private String diasAtendimento;

    @Column(name = "turnos_atendimento")
    private String turnosAtendimento;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ATIVO;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "cadastro_completo", nullable = false)
    private boolean cadastroCompleto = false;

    @PrePersist
    public void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = LocalDate.now();
        }
        if (status == null) {
            status = Status.ATIVO;
        }
    }
}
