// package ucsal.clinica.medica.medicamento.service;

// import java.util.List;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import ucsal.clinica.medica.medicamento.dto.MedicamentoRequest;
// import ucsal.clinica.medica.medicamento.dto.MedicamentoResponse;
// import ucsal.clinica.medica.medicamento.dto.MedicamentoUpdate;
// import ucsal.clinica.medica.medicamento.mapper.MedicamentoMapper;
// import ucsal.clinica.medica.medicamento.model.Medicamento;
// import ucsal.clinica.medica.medicamento.repository.MedicamentoRepository;

// /**
//  * CORRIGIDO: toggleMedicamento inverte o campo ativo (Boolean), não status (enum),
//  * alinhado com o novo model e com o que o frontend chama em PUT /medicamento/inativar/{id}.
//  */
// @Service
// @RequiredArgsConstructor
// public class MedicamentoService {

//     private final MedicamentoRepository medicamentoRepository;
//     private final MedicamentoMapper medicamentoMapper;

//     @Transactional(readOnly = true)
//     public List<MedicamentoResponse> findAllMedicamentos() {
//         return medicamentoMapper.toResponse(medicamentoRepository.findAll());
//     }

//     @Transactional
//     public MedicamentoResponse createMedicamento(MedicamentoRequest request) {
//         if (medicamentoRepository.existsByNome(request.nome())) {
//             throw new RuntimeException("Medicamento '" + request.nome() + "' já existe.");
//         }
//         var medicamento = medicamentoMapper.build(request);
//         return medicamentoMapper.toResponse(medicamentoRepository.save(medicamento));
//     }

//     @Transactional
//     public MedicamentoResponse updateMedicamento(MedicamentoUpdate request) {
//         var medicamento = findByIdOrThrow(request.id());
//         medicamentoMapper.update(medicamento, request);
//         return medicamentoMapper.toResponse(medicamentoRepository.save(medicamento));
//     }

//     @Transactional
//     public void toggleMedicamento(Long id) {
//         var medicamento = findByIdOrThrow(id);
//         // CORRIGIDO: inverte campo ativo, não um enum Status que não existe mais no model
//         medicamento.setAtivo(!Boolean.TRUE.equals(medicamento.getAtivo()));
//         medicamentoRepository.save(medicamento);
//     }

//     private Medicamento findByIdOrThrow(Long id) {
//         return medicamentoRepository.findById(id)
//             .orElseThrow(() -> new RuntimeException("Medicamento com id: " + id + " não encontrado."));
//     }
// }

package ucsal.clinica.medica.medicamento.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucsal.clinica.medica.medicamento.dto.MedicamentoRequest;
import ucsal.clinica.medica.medicamento.dto.MedicamentoResponse;
import ucsal.clinica.medica.medicamento.dto.MedicamentoUpdate;
import ucsal.clinica.medica.medicamento.mapper.MedicamentoMapper;
import ucsal.clinica.medica.medicamento.model.Medicamento;
import ucsal.clinica.medica.medicamento.repository.MedicamentoRepository;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final MedicamentoMapper medicamentoMapper;

    @Transactional(readOnly = true)
    public List<MedicamentoResponse> findAllMedicamentos() {
        return medicamentoMapper.toResponse(medicamentoRepository.findAll());
    }

    @Transactional
    public MedicamentoResponse createMedicamento(MedicamentoRequest request) {
        if (medicamentoRepository.findByNomeIgnoreCase(request.nome()).isPresent()) {
            throw new RuntimeException("Medicamento '" + request.nome() + "' já existe.");
        }

        var medicamento = medicamentoMapper.build(request);
        return medicamentoMapper.toResponse(medicamentoRepository.save(medicamento));
    }

    @Transactional
    public MedicamentoResponse updateMedicamento(MedicamentoUpdate request) {
        var medicamento = findByIdOrThrow(request.id());

        if (request.nome() != null && !request.nome().isBlank()) {
            var existente = medicamentoRepository.findByNomeIgnoreCase(request.nome());

            if (existente.isPresent() && !existente.get().getId().equals(medicamento.getId())) {
                throw new RuntimeException("Medicamento '" + request.nome() + "' já existe.");
            }
        }

        medicamentoMapper.update(medicamento, request);
        return medicamentoMapper.toResponse(medicamentoRepository.save(medicamento));
    }

    @Transactional
    public void toggleMedicamento(Long id) {
        var medicamento = findByIdOrThrow(id);
        medicamento.setAtivo(!Boolean.TRUE.equals(medicamento.getAtivo()));
        medicamentoRepository.save(medicamento);
    }

    private Medicamento findByIdOrThrow(Long id) {
        return medicamentoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Medicamento com id: " + id + " não encontrado."));
    }
}