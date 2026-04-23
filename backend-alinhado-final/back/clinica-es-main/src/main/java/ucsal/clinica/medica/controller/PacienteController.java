package ucsal.clinica.medica.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ucsal.clinica.medica.paciente.dto.PacienteRequest;
import ucsal.clinica.medica.paciente.dto.PacienteResponse;
import ucsal.clinica.medica.paciente.service.PacienteService;
import ucsal.clinica.medica.security.UsuarioAutenticado;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/paciente")
public class PacienteController {

    private final PacienteService service;
    private final UsuarioAutenticado usuarioAutenticado;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('PROFISSIONAL_SAUDE','ADMINISTRADOR')")
    public List<PacienteResponse> findAllPaciente() {
        if (usuarioAutenticado.isAdmin()) {
            return service.findAllPacientes();
        }
        return service.findAllPacientes(usuarioAutenticado.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    public PacienteResponse createPaciente(@RequestBody @Valid PacienteRequest request) {
        return service.createPaciente(request, usuarioAutenticado.getUsername());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    public PacienteResponse updatePaciente(
            @PathVariable Long id,
            @RequestBody @Valid PacienteRequest request) {
        return service.updatePaciente(id, request, usuarioAutenticado.getUsername());
    }

    @PutMapping("/inativar/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    public void inativarPaciente(@PathVariable Long id) {
        service.togglePaciente(id, usuarioAutenticado.getUsername());
    }
}
