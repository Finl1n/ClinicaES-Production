// package ucsal.clinica.medica.medicamento.repository;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import ucsal.clinica.medica.medicamento.model.Medicamento;

// import java.time.LocalDate;

// /**
//  * BUG FIX #5: novos métodos de query para o DashboardService.
//  * Spring Data JPA deriva as queries automaticamente dos nomes dos métodos.
//  */
// @Repository
// public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

// 	boolean existsByNome(String nome);

// 	/** Conta medicamentos com ativo = true. */
// 	long countByAtivoTrue();

// 	/**
// 	 * Conta medicamentos ativos com estoque entre 1 (inclusive) e limiar (exclusive).
// 	 * Equivalente SQL: WHERE ativo = true AND quantidade < :limiar AND quantidade > :minimo
// 	 */
// 	long countByAtivoTrueAndQuantidadeLessThanAndQuantidadeGreaterThan(
// 			Long limiar, Long minimo);

// 	/**
// 	 * Conta medicamentos ativos com data de validade anterior à data informada.
// 	 * Equivalente SQL: WHERE ativo = true AND validade IS NOT NULL AND validade < :hoje
// 	 */
// 	long countByAtivoTrueAndValidadeBeforeAndValidadeIsNotNull(LocalDate hoje);
// }

package ucsal.clinica.medica.medicamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucsal.clinica.medica.medicamento.model.Medicamento;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository de Medicamento
 * - Suporte a validação de nome único (create/update)
 * - Métodos para dashboard
 */
@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    // Verifica existência por nome (usado no create)
    boolean existsByNome(String nome);

    // Busca por nome ignorando maiúsculas/minúsculas (usado no update)
    Optional<Medicamento> findByNomeIgnoreCase(String nome);

    // ── DASHBOARD ─────────────────────────────────────────────

    /** Conta medicamentos ativos */
    long countByAtivoTrue();

    /**
     * Conta medicamentos ativos com estoque baixo
     * (ex: quantidade < limiar e > mínimo)
     */
    long countByAtivoTrueAndQuantidadeLessThanAndQuantidadeGreaterThan(
            Long limiar, Long minimo
    );

    /**
     * Conta medicamentos vencidos (com validade anterior à data atual)
     */
    long countByAtivoTrueAndValidadeBeforeAndValidadeIsNotNull(LocalDate hoje);
}