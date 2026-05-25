package mx.proyecto.backend_api.servicios;

import mx.proyecto.backend_api.entities.Profesor;
import mx.proyecto.backend_api.entities.Usuario;
import mx.proyecto.backend_api.entities.RolEnum;
import mx.proyecto.backend_api.repositorios.ProfesorRepository;
import mx.proyecto.backend_api.repositorios.UsuarioRepository;
import mx.proyecto.backend_api.servicios.interfaces.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Profesor> obtenerTodos() {
        List<Profesor> profesores = new ArrayList<>();
        profesorRepository.findAll().forEach(profesores::add);
        return profesores;
    }

    @Override
    public Profesor obtenerPorId(Long id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe profesor con id " + id));
    }

    @Override
    public Profesor guardar(Profesor profesor) {
        if (profesor.getProgramasAsignados() == null) {
            profesor.setProgramasAsignados(new ArrayList<>());
        }
        
        // 1. Guardar en la tabla de profesores
        Profesor guardado = profesorRepository.save(profesor);

        // 2. Crear o actualizar la cuenta de Usuario de login correspondiente
        if (usuarioRepository.findByCorreo(profesor.getCorreo()).isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setCorreo(profesor.getCorreo());
            // Encriptamos la contraseña para que Spring Security la acepte
            usuario.setPassword(passwordEncoder.encode(profesor.getPassword()));
            usuario.setRol(RolEnum.PROFESOR);
            usuario.setNombreCompleto(profesor.getNombreCompleto());
            usuarioRepository.save(usuario);
        }

        return guardado;
    }

    @Override
    public Profesor actualizarCompleto(Long id, Profesor profesor) {
        return profesorRepository.findById(id).map(existente -> {
            profesor.setId(id);
            if (profesor.getProgramasAsignados() == null) {
                profesor.setProgramasAsignados(existente.getProgramasAsignados());
            }
            
            // Sincronizar en la tabla de Usuarios si cambia el correo o nombre
            usuarioRepository.findByCorreo(existente.getCorreo()).ifPresent(usuario -> {
                usuario.setCorreo(profesor.getCorreo());
                usuario.setNombreCompleto(profesor.getNombreCompleto());
                if (profesor.getPassword() != null && !profesor.getPassword().isEmpty()) {
                    usuario.setPassword(passwordEncoder.encode(profesor.getPassword()));
                }
                usuarioRepository.save(usuario);
            });

            return profesorRepository.save(profesor);
        }).orElseThrow(() -> new NoSuchElementException("No existe profesor con id " + id));
    }

    @Override
    public Profesor actualizarParcial(Long id, Profesor profesor) {
        return profesorRepository.findById(id).map(existente -> {
            String correoAnterior = existente.getCorreo();

            if (profesor.getNombreCompleto() != null) existente.setNombreCompleto(profesor.getNombreCompleto());
            if (profesor.getCorreo() != null) existente.setCorreo(profesor.getCorreo());
            if (profesor.getPassword() != null) existente.setPassword(profesor.getPassword());
            if (profesor.getCarrera() != null) existente.setCarrera(profesor.getCarrera());
            if (profesor.getProgramasAsignados() != null) existente.setProgramasAsignados(profesor.getProgramasAsignados());

            // Guardar cambios del profesor
            Profesor guardado = profesorRepository.save(existente);

            // Sincronizar cambios en la tabla de login
            usuarioRepository.findByCorreo(correoAnterior).ifPresent(usuario -> {
                usuario.setCorreo(guardado.getCorreo());
                usuario.setNombreCompleto(guardado.getNombreCompleto());
                if (profesor.getPassword() != null && !profesor.getPassword().isEmpty()) {
                    usuario.setPassword(passwordEncoder.encode(profesor.getPassword()));
                }
                usuarioRepository.save(usuario);
            });

            return guardado;
        }).orElseThrow(() -> new NoSuchElementException("No existe profesor con id " + id));
    }

    @Override
    public Profesor eliminar(Long id) {
        return profesorRepository.findById(id).map(existente -> {
            // 1. Eliminar su usuario de login asociado
            usuarioRepository.findByCorreo(existente.getCorreo()).ifPresent(usuario -> {
                usuarioRepository.delete(usuario);
            });
            
            // 2. Eliminar al profesor
            profesorRepository.deleteById(id);
            return existente;
        }).orElseThrow(() -> new NoSuchElementException("No existe profesor con id " + id));
    }
}
