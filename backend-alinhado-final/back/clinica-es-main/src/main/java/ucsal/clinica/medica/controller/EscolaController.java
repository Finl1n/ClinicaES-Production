package ucsal.clinica.medica.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ucsal.clinica.medica.escola.dto.EscolaRequestDTO;
import ucsal.clinica.medica.escola.dto.EscolaResponseDTO;
import ucsal.clinica.medica.escola.service.EscolaService;

import java.util.List;

@RestController
@RequestMapping("/escolas")
public class EscolaController {

    private final EscolaService escolaService;

    public EscolaController(EscolaService escolaService) {
        this.escolaService = escolaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<EscolaResponseDTO> cadastrar(@RequestBody @Valid EscolaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(escolaService.cadastrar(request));
    }

    /**
     * CORREÇÃO: profissional precisa listar escolas ao cadastrar paciente.
     * Era apenas ADMINISTRADOR — agora inclui PROFISSIONAL_SAUDE.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFISSIONAL_SAUDE')")
    public ResponseEntity<List<EscolaResponseDTO>> listar() {
        return ResponseEntity.ok(escolaService.listarAtivas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFISSIONAL_SAUDE')")
    public ResponseEntity<EscolaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(escolaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<EscolaResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid EscolaRequestDTO request) {
        return ResponseEntity.ok(escolaService.atualizar(id, request));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        escolaService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
