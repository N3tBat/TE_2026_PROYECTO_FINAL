package mx.proyecto.backend_api.servicios.interfaces;

import mx.proyecto.backend_api.entities.Alumno;

import java.util.List;

public interface AlumnoService {
    List<Alumno> obtenerTodos();
    List<Alumno> obtenerPorPrograma(Long programaId);
    Alumno obtenerPorId(Long id);
    Alumno guardar(Alumno alumno, Long programaId, Long profesorId);
    Alumno actualizarParcial(Long id, Alumno alumno);
    Alumno eliminar(Long id);
}
