package ucsal.clinica.medica.requisicao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ucsal.clinica.medica.requisicao.model.RequisicaoMedicacao;

@Repository
public interface RequisicaoMedicacaoRepository extends JpaRepository<RequisicaoMedicacao, Long> {

}
