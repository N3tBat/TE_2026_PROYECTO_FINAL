package mx.proyecto.backend_api.servicios;

import mx.proyecto.backend_api.entities.Profesor;
import mx.proyecto.backend_api.repositorios.ProfesorRepository;
import mx.proyecto.backend_api.servicios.interfaces.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
//logica del profesor
@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

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
        return profesorRepository.save(profesor);
    }

    @Override
    public Profesor actualizarCompleto(Long id, Profesor profesor) {
        return profesorRepository.findById(id).map(existente -> {
            profesor.setId(id);
            if (profesor.getProgramasAsignados() == null) {
                profesor.setProgramasAsignados(existente.getProgramasAsignados());
            }
            return profesorRepository.save(profesor);
        }).orElseThrow(() -> new NoSuchElementException("No existe profesor con id " + id));
    }

    @Override
    public Profesor actualizarParcial(Long id, Profesor profesor) {
        return profesorRepository.findById(id).map(existente -> {
            if (profesor.getNombreCompleto() != null) existente.setNombreCompleto(profesor.getNombreCompleto());
            if (profesor.getCorreo() != null) existente.setCorreo(profesor.getCorreo());
            if (profesor.getPassword() != null) existente.setPassword(profesor.getPassword());
            return profesorRepository.save(existente);
        }).orElseThrow(() -> new NoSuchElementException("No existe profesor con id " + id));
    }

    @Override
    public Profesor eliminar(Long id) {
        return profesorRepository.findById(id).map(existente -> {
            profesorRepository.deleteById(id);
            return existente;
        }).orElseThrow(() -> new NoSuchElementException("No existe profesor con id " + id));
    }
}
