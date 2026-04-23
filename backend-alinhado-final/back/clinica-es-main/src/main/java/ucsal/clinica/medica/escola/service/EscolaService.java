package ucsal.clinica.medica.escola.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ucsal.clinica.medica.escola.dto.EscolaRequestDTO;
import ucsal.clinica.medica.escola.dto.EscolaResponseDTO;
import ucsal.clinica.medica.escola.model.Escola;
import ucsal.clinica.medica.escola.repository.EscolaRepository;
import ucsal.clinica.medica.global.Status;

@Service
public class EscolaService {

    private final EscolaRepository escolaRepository;

    public EscolaService(EscolaRepository escolaRepository) {
        this.escolaRepository = escolaRepository;
    }

    public EscolaResponseDTO cadastrar(EscolaRequestDTO request) {// RN003: cada escola deve ter nome único por IES
        if (escolaRepository.existsByNomeAndIes(request.nome(), request.ies())) {
            throw new RuntimeException("Já existe uma escola com esse nome nesta IES.");
        }

        Escola escola = new Escola();
        escola.setNome(request.nome());
        escola.setIes(request.ies());
        escola.setCoordenador(request.coordenador());


        return EscolaResponseDTO.from(escolaRepository.save(escola));
    }

    public List<EscolaResponseDTO> listarAtivas() {
        return escolaRepository.findAllByStatus(Status.ATIVO)
                .stream()
                .map(EscolaResponseDTO::from)
                .toList();
    }

    public EscolaResponseDTO buscarPorId(Long id) {
        Escola escola = buscarOuLancarExcecao(id);
        return EscolaResponseDTO.from(escola);
    }

    public EscolaResponseDTO atualizar(Long id, EscolaRequestDTO request) {
        Escola escola = buscarOuLancarExcecao(id);

        escola.setNome(request.nome());
        escola.setIes(request.ies());
        escola.setCoordenador(request.coordenador());

        return EscolaResponseDTO.from(escolaRepository.save(escola));
    }

    public void inativar(Long id) {

        Escola escola = buscarOuLancarExcecao(id);

        if (escola.getStatus() == Status.INATIVO) {
            throw new RuntimeException("Escola já está inativa.");
        }

        escola.setStatus(Status.INATIVO);
        escolaRepository.save(escola);
    }


    private Escola buscarOuLancarExcecao(Long id) {
        return escolaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Escola não encontrada com id: " + id));
    }
}