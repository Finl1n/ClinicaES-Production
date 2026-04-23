package ucsal.clinica.medica.paciente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucsal.clinica.medica.paciente.model.Paciente;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;

import java.util.List;
import ucsal.clinica.medica.global.Status;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByEmail(String email);

    /** Retorna somente os pacientes do profissional autenticado */
    List<Paciente> findAllByProfissional(ProfissionalSaude profissional);

    long countByStatus(Status status);
}
