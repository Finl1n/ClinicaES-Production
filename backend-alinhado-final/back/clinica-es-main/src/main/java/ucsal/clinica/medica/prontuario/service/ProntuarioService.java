package ucsal.clinica.medica.prontuario.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ucsal.clinica.medica.paciente.repository.PacienteRepository;
import ucsal.clinica.medica.prontuario.dto.ProntuarioResponse;
import ucsal.clinica.medica.prontuario.mapper.ProntuarioMapper;
import ucsal.clinica.medica.prontuario.repository.ProntuarioRepository;

@Service
@RequiredArgsConstructor
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final ProntuarioMapper prontuarioMapper;

    @Transactional(readOnly = true)
    public List<ProntuarioResponse> findAllProntuariosByUsername(String username) {
        return prontuarioMapper.toResponse(
                prontuarioRepository.findAllByPaciente_Profissional_Usuario_Username(username)
        );
    }

    @Transactional(readOnly = true)
    public ProntuarioResponse findProntuarioById(Long id) {
        return prontuarioMapper.toResponse(
            prontuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prontuário com id: " + id + " não encontrado."))
        );
    }

    @Transactional(readOnly = true)
    public List<ProntuarioResponse> findProntuarioByPacienteId(Long id) {
        var paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente com id: " + id + " não encontrado."));
        return prontuarioMapper.toResponse(prontuarioRepository.findAllByPaciente(paciente));
    }
}
