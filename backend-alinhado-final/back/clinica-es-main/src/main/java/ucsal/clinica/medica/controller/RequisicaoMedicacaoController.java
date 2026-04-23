package ucsal.clinica.medica.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import ucsal.clinica.medica.requisicao.dto.RequisicaoMedicacaoRequest;
import ucsal.clinica.medica.requisicao.dto.RequisicaoMedicacaoResponse;
import ucsal.clinica.medica.requisicao.service.RequisicaoMedicacaoService;
import ucsal.clinica.medica.security.UsuarioAutenticado;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/requisicoes")
public class RequisicaoMedicacaoController {

    private final RequisicaoMedicacaoService service;
    private final UsuarioAutenticado usuarioAutenticado;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RequisicaoMedicacaoResponse> findAllRequisicaoMedicacao() {
        return service.findAllRequisicoes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    public RequisicaoMedicacaoResponse criarRequisicaoMedicacao(
            @RequestBody @Valid RequisicaoMedicacaoRequest request) {
        return service.createRequisicao(request, usuarioAutenticado.getUsername());
    }
}
