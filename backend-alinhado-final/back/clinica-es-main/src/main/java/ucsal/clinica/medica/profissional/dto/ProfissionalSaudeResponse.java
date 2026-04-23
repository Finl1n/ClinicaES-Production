package ucsal.clinica.medica.profissional.dto;

import java.time.LocalDate;
import ucsal.clinica.medica.global.Status;
import ucsal.clinica.medica.profissional.model.ProfissionalSaude;

public record ProfissionalSaudeResponse(
        Long id,
        String nome,
        String username,
        Long usuarioId,
        String formacao,
        String especialidade,
        String conselho,
        String numeroRegistro,
        String diasAtendimento,
        String turnosAtendimento,
        LocalDate dataCadastro,
        Status status,
        boolean cadastroCompleto
) {
    public static ProfissionalSaudeResponse from(ProfissionalSaude p) {
        return new ProfissionalSaudeResponse(
                p.getId(),
                p.getNome(),
                p.getUsuario() != null ? p.getUsuario().getUsername() : null,
                p.getUsuario() != null ? p.getUsuario().getId() : null,
                p.getFormacao(),
                p.getEspecialidade(),
                p.getConselho(),
                p.getNumeroRegistro(),
                p.getDiasAtendimento(),
                p.getTurnosAtendimento(),
                p.getDataCadastro(),
                p.getStatus(),
                p.isCadastroCompleto()
        );
    }
}
