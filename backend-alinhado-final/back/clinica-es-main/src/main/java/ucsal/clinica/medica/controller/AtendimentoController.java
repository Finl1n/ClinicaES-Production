package ucsal.clinica.medica.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ucsal.clinica.medica.atendimento.dto.AtendimentoRequest;
import ucsal.clinica.medica.atendimento.dto.AtendimentoResponse;
import ucsal.clinica.medica.atendimento.dto.AtendimentoUpdate;
import ucsal.clinica.medica.atendimento.service.AtendimentoService;
import ucsal.clinica.medica.security.UsuarioAutenticado;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/atendimento")
public class AtendimentoController {

    private final AtendimentoService service;
    private final UsuarioAutenticado usuarioAutenticado;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AtendimentoResponse> findAllAtendimentos() {
        if (usuarioAutenticado.isAdmin()) {
            return service.findAllAtendimentos();
        }
        return service.findAllAtendimentosByUsername(usuarioAutenticado.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    public AtendimentoResponse criarAtendimento(@RequestBody @Valid AtendimentoRequest request) {
        return service.criarAtendimento(request, usuarioAutenticado.getUsername());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AtendimentoResponse atualizarAtendimento(@RequestBody @Valid AtendimentoUpdate request) {
        return service.atualizarAtendimento(request);
    }
}
