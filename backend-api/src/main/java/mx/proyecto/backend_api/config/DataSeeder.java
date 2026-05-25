package mx.proyecto.backend_api.config;

import mx.proyecto.backend_api.entities.Usuario;
import mx.proyecto.backend_api.entities.RolEnum;
import mx.proyecto.backend_api.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar Administrador de base
        if (usuarioRepository.findByCorreo("admin@aragon.unam.mx").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setCorreo("admin@aragon.unam.mx");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(RolEnum.ADMIN);
            usuarioRepository.save(admin);
            System.out.println(">>> Base de datos: Administrador inicial creado (admin@aragon.unam.mx / admin123)");
        }

        // Inicializar Profesor demo para pruebas de inicio de sesión
        if (usuarioRepository.findByCorreo("profesor@aragon.unam.mx").isEmpty()) {
            Usuario profesor = new Usuario();
            profesor.setCorreo("profesor@aragon.unam.mx");
            profesor.setPassword(passwordEncoder.encode("profesor123"));
            profesor.setRol(RolEnum.PROFESOR);
            usuarioRepository.save(profesor);
            System.out.println(">>> Base de datos: Profesor inicial creado (profesor@aragon.unam.mx / profesor123)");
        }

        // Inicializar Alumno demo para pruebas de inicio de sesión
        if (usuarioRepository.findByCorreo("alumno@aragon.unam.mx").isEmpty()) {
            Usuario alumno = new Usuario();
            alumno.setCorreo("alumno@aragon.unam.mx");
            alumno.setPassword(passwordEncoder.encode("alumno123"));
            alumno.setRol(RolEnum.ALUMNO);
            usuarioRepository.save(alumno);
            System.out.println(">>> Base de datos: Alumno inicial creado (alumno@aragon.unam.mx / alumno123)");
        }
    }
}
