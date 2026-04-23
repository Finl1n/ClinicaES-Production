package ucsal.clinica.medica.requisicao.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucsal.clinica.medica.medicamento.repository.MedicamentoRepository;
import ucsal.clinica.medica.profissional.repository.ProfissionalSaudeRepository;
import ucsal.clinica.medica.requisicao.dto.RequisicaoMedicacaoRequest;
import ucsal.clinica.medica.requisicao.dto.RequisicaoMedicacaoResponse;
import ucsal.clinica.medica.requisicao.mapper.RequisicaoMedicacaoMapper;
import ucsal.clinica.medica.requisicao.repository.RequisicaoMedicacaoRepository;

@Service
@RequiredArgsConstructor
public class RequisicaoMedicacaoService {

    private final RequisicaoMedicacaoRepository repository;
    private final RequisicaoMedicacaoMapper mapper;
    private final MedicamentoRepository medicamentoRepository;
    private final ProfissionalSaudeRepository profissionalRepository;

    @Transactional(readOnly = true)
    public List<RequisicaoMedicacaoResponse> findAllRequisicoes() {
        return mapper.toResponse(repository.findAll());
    }

    @Transactional
    public RequisicaoMedicacaoResponse createRequisicao(RequisicaoMedicacaoRequest request, String username) {
        var medicamento = medicamentoRepository.findById(request.medicacaoId())
            .orElseThrow(() -> new RuntimeException(
                "Medicamento com id: " + request.medicacaoId() + " não encontrado."
            ));

        var profissional = profissionalRepository.findByUsuario_Username(username)
            .orElseThrow(() -> new RuntimeException(
                "Perfil profissional não encontrado para o usuário: " + username
            ));

        var requisicao = mapper.build(request, medicamento, profissional);
        return mapper.toResponse(repository.save(requisicao));
    }
}
