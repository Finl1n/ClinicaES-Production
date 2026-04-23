package ucsal.clinica.medica.global;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ucsal.clinica.medica.atendimento.repository.AtendimentoRepository;
import ucsal.clinica.medica.escola.repository.EscolaRepository;
import ucsal.clinica.medica.medicamento.repository.MedicamentoRepository;
import ucsal.clinica.medica.paciente.repository.PacienteRepository;
import ucsal.clinica.medica.profissional.repository.ProfissionalSaudeRepository;
import ucsal.clinica.medica.requisicao.repository.RequisicaoMedicacaoRepository;
import ucsal.clinica.medica.unidade.repository.UnidadeRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final EscolaRepository escolaRepository;
	private final UnidadeRepository unidadeRepository;
	private final PacienteRepository pacienteRepository;
	private final AtendimentoRepository atendimentoRepository;
	private final ProfissionalSaudeRepository profissionalSaudeRepository;
	private final MedicamentoRepository medicamentoRepository;
	private final RequisicaoMedicacaoRepository requisicaoRepository;

	private static final long LIMIAR_ESTOQUE_BAIXO = 20L;

	@Transactional(readOnly = true)
	public StatusDashboardResponse statusDashboard() {
		LocalDate hoje = LocalDate.now();

		long totalEscolasAtivas      = escolaRepository.countByStatus(Status.ATIVO);
		long totalUnidadesAtivas     = unidadeRepository.countByStatus(Status.ATIVO);
		long totalPacientesAtivos    = pacienteRepository.countByStatus(Status.ATIVO);
		long totalProfissionaisAtivos = profissionalSaudeRepository.countByStatus(Status.ATIVO);
		long totalMedicacoes         = medicamentoRepository.count();
		long medicacoesAtivas        = medicamentoRepository.countByAtivoTrue();
		long medicacoesEstoqueBaixo  = medicamentoRepository
				.countByAtivoTrueAndQuantidadeLessThanAndQuantidadeGreaterThan(
						LIMIAR_ESTOQUE_BAIXO, 0L);
		long medicacoesVencidas      = medicamentoRepository
				.countByAtivoTrueAndValidadeBeforeAndValidadeIsNotNull(hoje);

		return new StatusDashboardResponse(
				totalEscolasAtivas,
				totalUnidadesAtivas,
				totalPacientesAtivos,
				atendimentoRepository.count(),
				totalProfissionaisAtivos,
				totalMedicacoes,
				medicacoesAtivas,
				medicacoesEstoqueBaixo,
				medicacoesVencidas,
				requisicaoRepository.count()
		);
	}
}
