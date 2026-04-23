package ucsal.clinica.medica.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import ucsal.clinica.medica.prontuario.dto.ProntuarioResponse;
import ucsal.clinica.medica.prontuario.service.ProntuarioService;
import ucsal.clinica.medica.security.UsuarioAutenticado;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/prontuario")
public class ProntuarioController {

    private final ProntuarioService service;
    private final UsuarioAutenticado usuarioAutenticado;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    public List<ProntuarioResponse> findAllProntuarios() {
        return service.findAllProntuariosByUsername(usuarioAutenticado.getUsername());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProntuarioResponse findProntuarioById(@PathVariable(value = "id") Long query) {
        return service.findProntuarioById(query);
    }

    @GetMapping(value = "/paciente/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProntuarioResponse> findProntuarioByPacientId(@PathVariable(value = "id") Long query) {
        return service.findProntuarioByPacienteId(query);
    }
}
