package ucsal.clinica.medica.auth.dto;

import ucsal.clinica.medica.auth.enums.RoleUsuario;

public record UsuarioResponse(
		
		String username,
		RoleUsuario role
		
) {

}
