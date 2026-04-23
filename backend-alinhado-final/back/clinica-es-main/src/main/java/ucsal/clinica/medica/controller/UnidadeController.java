package ucsal.clinica.medica.controller;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ucsal.clinica.medica.unidade.dto.UnidadeRequestDTO;
import ucsal.clinica.medica.unidade.dto.UnidadeResponseDTO;
import ucsal.clinica.medica.unidade.service.UnidadeService;

@RestController
@RequestMapping("/unidades")
@RequiredArgsConstructor
public class UnidadeController {

    private final UnidadeService unidadeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UnidadeResponseDTO> cadastrar(@RequestBody @Valid UnidadeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadeService.cadastrar(request));
    }

    /**
     * CORREÇÃO: profissional precisa listar unidades ao cadastrar paciente.
     * Era apenas ADMINISTRADOR — agora inclui PROFISSIONAL_SAUDE.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFISSIONAL_SAUDE')")
    public ResponseEntity<List<UnidadeResponseDTO>> listar() {
        return ResponseEntity.ok(unidadeService.listarAtivas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFISSIONAL_SAUDE')")
    public ResponseEntity<UnidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(unidadeService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UnidadeResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid UnidadeRequestDTO request) {
        return ResponseEntity.ok(unidadeService.atualizar(id, request));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        unidadeService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/inativar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> inativarLegado(@PathVariable Long id) {
        unidadeService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
