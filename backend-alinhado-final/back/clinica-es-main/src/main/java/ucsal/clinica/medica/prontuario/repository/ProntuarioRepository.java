package ucsal.clinica.medica.prontuario.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucsal.clinica.medica.paciente.model.Paciente;
import ucsal.clinica.medica.prontuario.model.Prontuario;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {

    List<Prontuario> findAllByPaciente(Paciente paciente);

    List<Prontuario> findAllByPaciente_Profissional_Usuario_Username(String username);

    Optional<Prontuario> findByPaciente(Paciente paciente);
}
