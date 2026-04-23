package ucsal.clinica.medica.unidade.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ucsal.clinica.medica.global.Status;
import ucsal.clinica.medica.unidade.dto.UnidadeRequestDTO;
import ucsal.clinica.medica.unidade.dto.UnidadeResponseDTO;
import ucsal.clinica.medica.unidade.model.Unidade;
import ucsal.clinica.medica.unidade.repository.UnidadeRepository;

@Service
public class UnidadeService {

	private final UnidadeRepository unidadeRepository;

	public UnidadeService(UnidadeRepository unidadeRepository) {
		this.unidadeRepository = unidadeRepository;
	}

	public UnidadeResponseDTO cadastrar(UnidadeRequestDTO request) {

		if (unidadeRepository.existsByNomeAndIes(request.nome(), request.ies())) {
			throw new RuntimeException("Já existe uma unidade com esse nome nesta IES.");
		}

		Unidade unidade = new Unidade();
		unidade.setNome(request.nome());
		unidade.setIes(request.ies());
		unidade.setResponsavel(request.responsavel());

		return UnidadeResponseDTO.from(unidadeRepository.save(unidade));
	}

	public List<UnidadeResponseDTO> listarAtivas() {
		return unidadeRepository.findAllByStatus(Status.ATIVO).stream().map(UnidadeResponseDTO::from).toList();
	}

	public UnidadeResponseDTO buscarPorId(Long id) {
		return UnidadeResponseDTO.from(buscarOuLancarExcecao(id));
	}

	public UnidadeResponseDTO atualizar(Long id, UnidadeRequestDTO request) {
		Unidade unidade = buscarOuLancarExcecao(id);

		unidade.setNome(request.nome());
		unidade.setIes(request.ies());
		unidade.setResponsavel(request.responsavel());

		return UnidadeResponseDTO.from(unidadeRepository.save(unidade));
	}

	public void inativar(Long id) {

		Unidade unidade = buscarOuLancarExcecao(id);

		if (unidade.getStatus() == Status.INATIVO) {
			throw new RuntimeException("Unidade já está inativa.");
		}

		unidade.setStatus(Status.INATIVO);
		unidadeRepository.save(unidade);
	}

	private Unidade buscarOuLancarExcecao(Long id) {
		return unidadeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Unidade não encontrada com id: " + id));
	}
}
