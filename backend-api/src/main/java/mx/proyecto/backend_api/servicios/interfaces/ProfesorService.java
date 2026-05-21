package mx.proyecto.backend_api.servicios.interfaces;

import mx.proyecto.backend_api.entities.Profesor;

import java.util.List;

public interface ProfesorService {
    List<Profesor> obtenerTodos();
    Profesor obtenerPorId(Long id);
    Profesor guardar(Profesor profesor);
    Profesor actualizarCompleto(Long id, Profesor profesor);
    Profesor actualizarParcial(Long id, Profesor profesor);
    Profesor eliminar(Long id);
}
