package ucsal.clinica.medica.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ucsal.clinica.medica.medicamento.dto.MedicamentoRequest;
import ucsal.clinica.medica.medicamento.dto.MedicamentoResponse;
import ucsal.clinica.medica.medicamento.dto.MedicamentoUpdate;
import ucsal.clinica.medica.medicamento.service.MedicamentoService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/medicamento")
public class MedicamentoController {

	private final MedicamentoService service;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<MedicamentoResponse> findAllMedicamentos() {
		return service.findAllMedicamentos();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MedicamentoResponse criarMedicamento(@RequestBody @Valid MedicamentoRequest request) {
		return service.createMedicamento(request);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	public MedicamentoResponse atualizarMedicamento(@RequestBody @Valid MedicamentoUpdate request) {
		return service.updateMedicamento(request);
	}

	@PutMapping(value = "/inativar/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void inativarMedicamento(@PathVariable(value = "id") Long query) {
		service.toggleMedicamento(query);
	}

}
