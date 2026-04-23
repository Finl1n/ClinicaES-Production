package ucsal.clinica.medica.unidade.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ucsal.clinica.medica.global.Status;
import ucsal.clinica.medica.unidade.model.Unidade;

import java.util.List;

public interface UnidadeRepository extends JpaRepository<Unidade,Long> {

    List<Unidade> findAllByStatus(Status status);
    boolean existsByNomeAndIes(String nome, String ies);

    long countByStatus(Status status);
}
