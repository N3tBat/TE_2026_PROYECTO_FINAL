package mx.proyecto.backend_api.servicios;
//aqui esta la capa de servicios del alumno, en donde se revisa si su programa existe y otra logica importante para este
import mx.proyecto.backend_api.entities.Alumno;
import mx.proyecto.backend_api.entities.Profesor;
import mx.proyecto.backend_api.entities.Programa;
import mx.proyecto.backend_api.repositorios.AlumnoRepository;
import mx.proyecto.backend_api.repositorios.ProfesorRepository;
import mx.proyecto.backend_api.repositorios.ProgramaRepository;
import mx.proyecto.backend_api.servicios.interfaces.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private ProgramaRepository programaRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Override
    public List<Alumno> obtenerTodos() {
        List<Alumno> alumnos = new ArrayList<>();
        alumnoRepository.findAll().forEach(alumnos::add);
        return alumnos;
    }

    @Override
    public List<Alumno> obtenerPorPrograma(Long programaId) {
        // Validar que el programa existe
        programaRepository.findById(programaId)
                .orElseThrow(() -> new NoSuchElementException("No existe programa con id " + programaId));
        return alumnoRepository.findByProgramaAsignadoId(programaId);
    }

    @Override
    public Alumno obtenerPorId(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe alumno con id " + id));
    }

    @Override
    public Alumno guardar(Alumno alumno, Long programaId, Long profesorId) {
        // Regla: el profesor solo puede dar de alta alumnos en sus programas asignados
        Programa programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new NoSuchElementException("No existe programa con id " + programaId));

        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new NoSuchElementException("No existe profesor con id " + profesorId));

        boolean tieneAcceso = profesor.getProgramasAsignados().stream()
                .anyMatch(p -> p.getId().equals(programaId));

        if (!tieneAcceso) {
            throw new IllegalArgumentException(
                    "El profesor con id " + profesorId + " no tiene asignado el programa con id " + programaId);
        }

        // Regla: el programa debe estar activo
        if (!"activo".equalsIgnoreCase(programa.getEstado())) {
            throw new IllegalArgumentException("Solo se pueden registrar alumnos en programas activos");
        }

        alumno.setProgramaAsignado(programa);
        alumno.setEstado("activo");
        if (alumno.getTotalHorasAcumuladas() == null) {
            alumno.setTotalHorasAcumuladas(0.0);
        }
        return alumnoRepository.save(alumno);
    }

    @Override
    public Alumno actualizarParcial(Long id, Alumno alumno) {
        return alumnoRepository.findById(id).map(existente -> {
            if (alumno.getNombre() != null) existente.setNombre(alumno.getNombre());
            if (alumno.getApellidoPaterno() != null) existente.setApellidoPaterno(alumno.getApellidoPaterno());
            if (alumno.getApellidoMaterno() != null) existente.setApellidoMaterno(alumno.getApellidoMaterno());
            if (alumno.getNumeroCuenta() != null) existente.setNumeroCuenta(alumno.getNumeroCuenta());
            if (alumno.getCarrera() != null) existente.setCarrera(alumno.getCarrera());
            if (alumno.getCorreo() != null) existente.setCorreo(alumno.getCorreo());
            if (alumno.getTelefono() != null) existente.setTelefono(alumno.getTelefono());
            if (alumno.getEstado() != null) existente.setEstado(alumno.getEstado());
            return alumnoRepository.save(existente);
        }).orElseThrow(() -> new NoSuchElementException("No existe alumno con id " + id));
    }

    @Override
    public Alumno eliminar(Long id) {
        return alumnoRepository.findById(id).map(existente -> {
            alumnoRepository.deleteById(id);
            return existente;
        }).orElseThrow(() -> new NoSuchElementException("No existe alumno con id " + id));
    }
}
