package ucsal.clinica.medica.atendimento.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucsal.clinica.medica.atendimento.dto.AtendimentoRequest;
import ucsal.clinica.medica.atendimento.dto.AtendimentoResponse;
import ucsal.clinica.medica.atendimento.dto.AtendimentoUpdate;
import ucsal.clinica.medica.atendimento.mapper.AtendimentoMapper;
import ucsal.clinica.medica.atendimento.model.Atendimento;
import ucsal.clinica.medica.atendimento.repository.AtendimentoRepository;
import ucsal.clinica.medica.medicamento.model.Medicamento;
import ucsal.clinica.medica.medicamento.repository.MedicamentoRepository;
import ucsal.clinica.medica.paciente.repository.PacienteRepository;
import ucsal.clinica.medica.profissional.repository.ProfissionalSaudeRepository;
import ucsal.clinica.medica.prontuario.repository.ProntuarioRepository;
import ucsal.clinica.medica.requisicao.model.UsoMedicacao;
import ucsal.clinica.medica.requisicao.repository.UsoMedicacaoRepository;

@Service
@RequiredArgsConstructor
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalSaudeRepository profissionalRepository;
    private final AtendimentoMapper atendimentoMapper;
    private final ProntuarioRepository prontuarioRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final UsoMedicacaoRepository usoMedicacaoRepository;

    @Transactional(readOnly = true)
    public List<AtendimentoResponse> findAllAtendimentos() {
        return atendimentoRepository.findAll().stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AtendimentoResponse> findAllAtendimentosByUsername(String username) {
        return atendimentoRepository.findAllByProfissionalSaude_Usuario_Username(username).stream()
                .map(this::toResponseCompleto)
                .toList();
    }

    @Transactional
    public AtendimentoResponse criarAtendimento(AtendimentoRequest request, String username) {
        var paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new RuntimeException(
                        "Paciente com id: " + request.pacienteId() + " não encontrado."
                ));

        var profissional = profissionalRepository.findByUsuario_Username(username)
                .orElseThrow(() -> new RuntimeException(
                        "Perfil profissional não encontrado para o usuário: " + username
                ));

        if (!paciente.getProfissional().getId().equals(profissional.getId())) {
            throw new RuntimeException("Acesso negado: este paciente não pertence ao profissional autenticado.");
        }

        var prontuario = prontuarioRepository.findByPaciente(paciente)
                .orElseThrow(() -> new RuntimeException(
                        "Prontuário não encontrado para o paciente id: " + request.pacienteId()
                ));

        var atendimento = atendimentoMapper.build(request, paciente, profissional);
        atendimento.setProntuario(prontuario);
        preencherCamposClinicos(atendimento, request);

        var salvo = atendimentoRepository.save(atendimento);
        persistirUsoMedicacoes(salvo, prontuario.getId(), resolveMedicacoes(request));

        return toResponseCompleto(salvo);
    }

    @Transactional
    public AtendimentoResponse atualizarAtendimento(AtendimentoUpdate request) {
        var atendimento = atendimentoRepository.findById(request.id())
                .orElseThrow(() -> new RuntimeException(
                        "Atendimento com id: " + request.id() + " não encontrado."
                ));

        var paciente = request.pacienteId() != null
                ? pacienteRepository.findById(request.pacienteId())
                        .orElseThrow(() -> new RuntimeException(
                                "Paciente com id: " + request.pacienteId() + " não encontrado."
                        ))
                : null;

        var profissional = request.profissionalId() != null
                ? profissionalRepository.findById(request.profissionalId())
                        .orElseThrow(() -> new RuntimeException(
                                "Profissional com id: " + request.profissionalId() + " não encontrado."
                        ))
                : null;

        atendimentoMapper.update(atendimento, paciente, profissional, request);
        preencherCamposClinicos(
                atendimento,
                request.observacoes(),
                request.descricao(),
                request.dataAtendimento(),
                request.status()
        );

        return toResponseCompleto(atendimentoRepository.save(atendimento));
    }

    private void preencherCamposClinicos(Atendimento atendimento, AtendimentoRequest request) {
        preencherCamposClinicos(
                atendimento,
                request.observacoes(),
                request.descricao(),
                request.dataAtendimento(),
                request.status()
        );
    }

    private void preencherCamposClinicos(
            Atendimento atendimento,
            String observacoes,
            String descricao,
            String dataAtendimento,
            String status
    ) {
        atendimento.setDescricao(descricao);
        atendimento.setObservacoes(observacoes);
        atendimento.setDataAtendimento(dataAtendimento);
        atendimento.setStatusAtendimento(status);
        atendimento.setDataInicio(dataAtendimento);
        atendimento.setSintomas(descricao);

        Map<String, Object> detalhes = parseObservacoes(observacoes);
        atendimento.setDataFim(asString(detalhes.get("dataFim")));
        atendimento.setDiagnostico(asString(detalhes.get("diagnostico")));
        atendimento.setTratamento(asString(detalhes.get("tratamento")));
    }

    private void persistirUsoMedicacoes(
            Atendimento atendimento,
            Long prontuarioId,
            List<AtendimentoRequest.UsoMedicacaoRequest> medicacoes
    ) {
        if (medicacoes == null || medicacoes.isEmpty()) {
            return;
        }

        var prontuario = atendimento.getProntuario();
        if (prontuario == null || !prontuario.getId().equals(prontuarioId)) {
            throw new RuntimeException("Prontuário inválido para registrar uso de medicação.");
        }

        for (var item : medicacoes) {
            Medicamento medicamento = medicamentoRepository.findById(item.medicacaoId())
                    .orElseThrow(() -> new RuntimeException(
                            "Medicamento com id: " + item.medicacaoId() + " não encontrado."
                    ));

            long quantidadeSolicitada = item.quantidade() != null ? item.quantidade() : 0L;
            long estoqueAtual = medicamento.getQuantidade() != null ? medicamento.getQuantidade() : 0L;

            if (quantidadeSolicitada <= 0) {
                throw new RuntimeException("Quantidade de uso da medicação deve ser maior que zero.");
            }

            if (estoqueAtual < quantidadeSolicitada) {
                throw new RuntimeException(
                        "Estoque insuficiente para a medicação '" + medicamento.getNome() + "'."
                );
            }

            medicamento.setQuantidade(estoqueAtual - quantidadeSolicitada);
            medicamentoRepository.save(medicamento);

            UsoMedicacao uso = new UsoMedicacao();
            uso.setAtendimento(atendimento);
            uso.setProntuario(prontuario);
            uso.setMedicamento(medicamento);
            uso.setQuantidade(quantidadeSolicitada);
            uso.setDosagem(item.dosagem());
            usoMedicacaoRepository.save(uso);
        }
    }

    private List<AtendimentoRequest.UsoMedicacaoRequest> resolveMedicacoes(AtendimentoRequest request) {
        if (request.medicacoesUsadas() != null && !request.medicacoesUsadas().isEmpty()) {
            return request.medicacoesUsadas();
        }

        return Collections.emptyList();
    }

    private AtendimentoResponse toResponseCompleto(Atendimento atendimento) {
        return atendimentoMapper.toResponse(
                atendimento,
                usoMedicacaoRepository.findAllByAtendimento_Id(atendimento.getId())
        );
    }

    private Map<String, Object> parseObservacoes(String observacoes) {
        return Collections.emptyMap();
    }

    private String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}