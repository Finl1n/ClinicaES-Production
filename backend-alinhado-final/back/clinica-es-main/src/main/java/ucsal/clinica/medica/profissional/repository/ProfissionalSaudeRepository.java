package ucsal.clinica.medica.profissional.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ucsal.clinica.medica.global.Status;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;

import java.util.List;
import java.util.Optional;

public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude,Long> {

    List<ProfissionalSaude> findAllByStatus(Status status);


    Optional<ProfissionalSaude> findByUsuario_Username(String username);


    boolean existsByUsuario_Id(Long usuarioId);

    long countByStatus(Status status);
}
