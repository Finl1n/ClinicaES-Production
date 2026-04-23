package ucsal.clinica.medica.escola.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ucsal.clinica.medica.escola.model.Escola;
import ucsal.clinica.medica.global.Status;

import java.util.List;

public interface EscolaRepository extends JpaRepository<Escola,Long> {

    List<Escola> findAllByStatus(Status status);
    boolean existsByNomeAndIes(String nome, String ies);

    long countByStatus(Status status);
}
