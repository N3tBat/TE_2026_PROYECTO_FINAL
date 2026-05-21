package mx.proyecto.backend_api.controladores;

import mx.proyecto.backend_api.controladores.dto.JwtResponse;
import mx.proyecto.backend_api.controladores.dto.LoginRequest;
import mx.proyecto.backend_api.security.JwtUtil;
import mx.proyecto.backend_api.security.UserDetailsServiceImpl;
import mx.proyecto.backend_api.controladores.dto.RegistroRequest;
import mx.proyecto.backend_api.entities.Usuario;
import mx.proyecto.backend_api.entities.RolEnum;
import mx.proyecto.backend_api.repositorios.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getCorreo(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getCorreo());

        // Extraer rol. Spring Security añade el prefijo ROLE_, lo podemos limpiar o usar tal cual.
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        final String jwt = jwtUtil.generateToken(userDetails, role);

        return ResponseEntity.ok(new JwtResponse(jwt, role));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroRequest registroRequest) {
        if (usuarioRepository.findByCorreo(registroRequest.getCorreo()).isPresent()) {
            return ResponseEntity.badRequest().body("El correo ya está registrado");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCorreo(registroRequest.getCorreo());
        nuevoUsuario.setPassword(passwordEncoder.encode(registroRequest.getPassword()));
        
        // Si no envía rol o es la creación inicial, asignamos ALUMNO por defecto o el que venga
        if (registroRequest.getRol() != null) {
            nuevoUsuario.setRol(registroRequest.getRol());
        } else {
            nuevoUsuario.setRol(RolEnum.ALUMNO);
        }

        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}
