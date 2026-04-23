package ucsal.clinica.medica.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ucsal.clinica.medica.profissional.dto.ProfissionalSaudeComplementoRequestDTO;
import ucsal.clinica.medica.profissional.dto.ProfissionalSaudeRequestDTO;
import ucsal.clinica.medica.profissional.dto.ProfissionalSaudeResponse;
import ucsal.clinica.medica.profissional.service.ProfissionalSaudeService;
import ucsal.clinica.medica.security.UsuarioAutenticado;

@RestController
@RequestMapping("/profissionais")
@RequiredArgsConstructor
public class ProfissionalSaudeController {

    private final ProfissionalSaudeService profissionalService;
    private final UsuarioAutenticado usuarioAutenticado;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ProfissionalSaudeResponse> cadastrar(
            @RequestBody @Valid ProfissionalSaudeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profissionalService.cadastrar(request));
    }

    /** Frontend: PATCH /profissionais/meu-perfil/complemento */
    @PatchMapping("/meu-perfil/complemento")
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    public ResponseEntity<ProfissionalSaudeResponse> completarCadastro(
            @RequestBody @Valid ProfissionalSaudeComplementoRequestDTO request) {
        return ResponseEntity.ok(profissionalService.completarCadastro(usuarioAutenticado.getUsername(), request));
    }

    /** Alias legado: POST /profissionais/complemento */
    @PostMapping("/complemento")
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    public ResponseEntity<ProfissionalSaudeResponse> completarCadastroLegado(
            @RequestBody @Valid ProfissionalSaudeComplementoRequestDTO request) {
        return ResponseEntity.ok(profissionalService.completarCadastro(usuarioAutenticado.getUsername(), request));
    }

    @GetMapping("/meu-perfil")
    @PreAuthorize("hasRole('PROFISSIONAL_SAUDE')")
    public ResponseEntity<ProfissionalSaudeResponse> consultarPerfil() {
        return ResponseEntity.ok(profissionalService.consultarPerfil(usuarioAutenticado.getUsername()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<ProfissionalSaudeResponse>> listar() {
        return ResponseEntity.ok(profissionalService.listarAtivos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ProfissionalSaudeResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(profissionalService.buscarPorId(id));
    }

    /** Frontend: PATCH /profissionais/{id}/inativar */
    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        profissionalService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    /** Alias legado: POST /profissionais/inativar/{id} */
    @PostMapping("/inativar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> inativarLegado(@PathVariable Long id) {
        profissionalService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
