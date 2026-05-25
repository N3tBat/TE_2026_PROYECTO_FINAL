package mx.proyecto.backend_api.config;

import mx.proyecto.backend_api.entities.Usuario;
import mx.proyecto.backend_api.entities.RolEnum;
import mx.proyecto.backend_api.entities.Programa;
import mx.proyecto.backend_api.entities.Profesor;
import mx.proyecto.backend_api.repositorios.UsuarioRepository;
import mx.proyecto.backend_api.repositorios.ProfesorRepository;
import mx.proyecto.backend_api.repositorios.ProgramaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private ProgramaRepository programaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Inicializar Programas si no existen
        Programa prog1 = null;
        Programa prog2 = null;

        if (programaRepository.count() == 0) {
            prog1 = new Programa();
            prog1.setNombre("Desarrollo de Software Interno");
            prog1.setDescripcion("Apoyo en el desarrollo de software para FES Aragón");
            prog1.setAreaResponsable("Sistemas FES Aragón");
            prog1.setFechaInicio(LocalDate.now().minusMonths(1));
            prog1.setFechaTermino(LocalDate.now().plusMonths(6));
            prog1.setHorasRequeridas(480);
            prog1.setEstado("activo");
            prog1 = programaRepository.save(prog1);

            prog2 = new Programa();
            prog2.setNombre("Asesorías Académicas de Matemáticas");
            prog2.setDescripcion("Asesoría académica para alumnos de primeros semestres");
            prog2.setAreaResponsable("Coordinación de Ciencias");
            prog2.setFechaInicio(LocalDate.now().minusMonths(3));
            prog2.setFechaTermino(LocalDate.now().minusDays(5));
            prog2.setHorasRequeridas(480);
            prog2.setEstado("activo");
            prog2 = programaRepository.save(prog2);

            System.out.println(">>> Base de datos: Programas iniciales creados.");
        } else {
            Iterable<Programa> progs = programaRepository.findAll();
            for (Programa p : progs) {
                if (prog1 == null) prog1 = p;
                else if (prog2 == null) {
                    prog2 = p;
                    break;
                }
            }
        }

        // 2. Inicializar Administrador
        if (usuarioRepository.findByCorreo("admin@aragon.unam.mx").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setCorreo("admin@aragon.unam.mx");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(RolEnum.ADMIN);
            admin.setNombreCompleto("Dr. Jesús Martínez (Admin)");
            usuarioRepository.save(admin);
            System.out.println(">>> Base de datos: Administrador inicial creado.");
        }

        // 3. Inicializar Profesores
        if (profesorRepository.count() == 0) {
            // Mtra. Alejandra Muciño (ICO)
            Profesor p1 = new Profesor();
            p1.setNombreCompleto("Mtra. Alejandra Muciño");
            p1.setCorreo("profesor@aragon.unam.mx");
            p1.setPassword("profesor123");
            p1.setCarrera("ICO");
            if (prog1 != null) {
                p1.setProgramasAsignados(new ArrayList<>(Arrays.asList(prog1)));
            } else {
                p1.setProgramasAsignados(new ArrayList<>());
            }
            profesorRepository.save(p1);

            // Registrar su Usuario de login correspondientemente si no existe
            if (usuarioRepository.findByCorreo(p1.getCorreo()).isEmpty()) {
                Usuario u1 = new Usuario();
                u1.setCorreo(p1.getCorreo());
                u1.setPassword(passwordEncoder.encode(p1.getPassword()));
                u1.setRol(RolEnum.PROFESOR);
                u1.setNombreCompleto(p1.getNombreCompleto());
                usuarioRepository.save(u1);
            }

            // Dr. Ernesto Zamora (IM)
            Profesor p2 = new Profesor();
            p2.setNombreCompleto("Dr. Ernesto Zamora");
            p2.setCorreo("ernesto.zamora@aragon.unam.mx");
            p2.setPassword("profesor123");
            p2.setCarrera("IM");
            if (prog2 != null) {
                p2.setProgramasAsignados(new ArrayList<>(Arrays.asList(prog2)));
            } else {
                p2.setProgramasAsignados(new ArrayList<>());
            }
            profesorRepository.save(p2);

            // Registrar su Usuario de login si no existe
            if (usuarioRepository.findByCorreo(p2.getCorreo()).isEmpty()) {
                Usuario u2 = new Usuario();
                u2.setCorreo(p2.getCorreo());
                u2.setPassword(passwordEncoder.encode(p2.getPassword()));
                u2.setRol(RolEnum.PROFESOR);
                u2.setNombreCompleto(p2.getNombreCompleto());
                usuarioRepository.save(u2);
            }

            System.out.println(">>> Base de datos: Profesores iniciales y logins creados.");
        }

        // 4. Inicializar Alumno demo
        if (usuarioRepository.findByCorreo("alumno@aragon.unam.mx").isEmpty()) {
            Usuario alumno = new Usuario();
            alumno.setCorreo("alumno@aragon.unam.mx");
            alumno.setPassword(passwordEncoder.encode("alumno123"));
            alumno.setRol(RolEnum.ALUMNO);
            alumno.setNombreCompleto("Ernesto Zamora (Alumno)");
            usuarioRepository.save(alumno);
            System.out.println(">>> Base de datos: Alumno inicial creado.");
        }
    }
}
