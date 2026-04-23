package ucsal.clinica.medica.requisicao.dto;

import ucsal.clinica.medica.requisicao.enums.TipoRequisicao;

/**
 * CORRIGIDO:
 * - quantidade como Long (era Double, inconsistente com o model Long)
 * - adicionado profissionalNome (frontend espera opcional)
 */
public record RequisicaoMedicacaoResponse(

	    Long id,
	    Long medicacaoId,
	    String medicacaoNome,
	    Long quantidade,
	    TipoRequisicao tipo,
	    Long profissionalId,
	    String profissionalNome,
	    String data,
	    String observacao

) {}
