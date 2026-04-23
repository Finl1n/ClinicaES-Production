package ucsal.clinica.medica.requisicao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ucsal.clinica.medica.requisicao.enums.TipoRequisicao;

public record RequisicaoMedicacaoRequest(

    @NotNull(message = "Medicacao é obrigatória")
    Long medicacaoId,

    @NotNull(message = "Quantidade é obrigatória")
    Long quantidade,

    @NotNull(message = "Tipo da requisição é obrigatório")
    TipoRequisicao tipo,

    @NotNull(message = "Profissional é obrigatório")
    Long profissionalId,

    @NotBlank(message = "Data é obrigatória")
    String data,

    String observacao

) {}
