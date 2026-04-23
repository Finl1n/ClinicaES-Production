package ucsal.clinica.medica.requisicao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucsal.clinica.medica.requisicao.model.UsoMedicacao;

@Repository
public interface UsoMedicacaoRepository extends JpaRepository<UsoMedicacao, Long> {
    java.util.List<UsoMedicacao> findAllByAtendimento_Id(Long atendimentoId);
}
