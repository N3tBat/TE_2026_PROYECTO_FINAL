package mx.proyecto.backend_api.servicios;
//logica programas
import mx.proyecto.backend_api.entities.Profesor;
import mx.proyecto.backend_api.entities.Programa;
import mx.proyecto.backend_api.repositorios.ProfesorRepository;
import mx.proyecto.backend_api.repositorios.ProgramaRepository;
import mx.proyecto.backend_api.servicios.interfaces.ProgramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProgramaServiceImpl implements ProgramaService {

    @Autowired
    private ProgramaRepository programaRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Override
    public List<Programa> obtenerTodos() {
        List<Programa> programas = new ArrayList<>();
        programaRepository.findAll().forEach(programas::add);
        return programas;
    }

    @Override
    public Programa obtenerPorId(Long id) {
        return programaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No existe programa con id " + id));
    }

    @Override
    public Programa guardar(Programa programa) {
        return programaRepository.save(programa);
    }

    @Override
    public Programa actualizarCompleto(Long id, Programa programa) {
        return programaRepository.findById(id).map(existente -> {
            programa.setId(id);
            // Conservar la lista de profesores al hacer un PUT completo
            if (programa.getProfesores() == null) {
                programa.setProfesores(existente.getProfesores());
            }
            return programaRepository.save(programa);
        }).orElseThrow(() -> new NoSuchElementException("No existe programa con id " + id));
    }

    @Override
    public Programa actualizarParcial(Long id, Programa programa) {
        return programaRepository.findById(id).map(existente -> {
            if (programa.getNombre() != null) existente.setNombre(programa.getNombre());
            if (programa.getDescripcion() != null) existente.setDescripcion(programa.getDescripcion());
            if (programa.getAreaResponsable() != null) existente.setAreaResponsable(programa.getAreaResponsable());
            if (programa.getFechaInicio() != null) existente.setFechaInicio(programa.getFechaInicio());
            if (programa.getFechaTermino() != null) existente.setFechaTermino(programa.getFechaTermino());
            if (programa.getHorasRequeridas() != null) existente.setHorasRequeridas(programa.getHorasRequeridas());
            if (programa.getEstado() != null) existente.setEstado(programa.getEstado());
            return programaRepository.save(existente);
        }).orElseThrow(() -> new NoSuchElementException("No existe programa con id " + id));
    }

    @Override
    public Programa eliminar(Long id) {
        return programaRepository.findById(id).map(existente -> {
            programaRepository.deleteById(id);
            return existente;
        }).orElseThrow(() -> new NoSuchElementException("No existe programa con id " + id));
    }

    @Override
    public Programa asignarProfesor(Long programaId, Long profesorId) {
        Programa programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new NoSuchElementException("No existe programa con id " + programaId));
        Profesor profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new NoSuchElementException("No existe profesor con id " + profesorId));

        // Evitar duplicados
        if (!profesor.getProgramasAsignados().contains(programa)) {
            profesor.getProgramasAsignados().add(programa);
            profesorRepository.save(profesor);
        }
        return programaRepository.findById(programaId).orElseThrow();
    }
}
