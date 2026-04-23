package ucsal.clinica.medica.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucsal.clinica.medica.auth.dto.LoginRequestDTO;
import ucsal.clinica.medica.auth.dto.LoginResponseDTO;
import ucsal.clinica.medica.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** Compatível com o frontend: POST /auth/login */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authService.autenticar(request));
    }

    /** Mantido por compatibilidade — alias de /login */
    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponseDTO> signIn(@RequestBody @Valid LoginRequestDTO request) {
        return ResponseEntity.ok(authService.autenticar(request));
    }
}
