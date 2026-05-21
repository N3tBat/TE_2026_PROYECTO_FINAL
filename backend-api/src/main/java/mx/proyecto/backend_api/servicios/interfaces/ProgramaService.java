package mx.proyecto.backend_api.servicios.interfaces;

import mx.proyecto.backend_api.entities.Programa;

import java.util.List;

public interface ProgramaService {
    List<Programa> obtenerTodos();
    Programa obtenerPorId(Long id);
    Programa guardar(Programa programa);
    Programa actualizarCompleto(Long id, Programa programa);
    Programa actualizarParcial(Long id, Programa programa);
    Programa eliminar(Long id);
    Programa asignarProfesor(Long programaId, Long profesorId);
}
