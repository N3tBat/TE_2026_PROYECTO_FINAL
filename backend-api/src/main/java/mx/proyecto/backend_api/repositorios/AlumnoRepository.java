package mx.proyecto.backend_api.repositorios;
//en esta parte usamos  Jpa para poder hacer busquedas tipo SQL sin directamente usarlo
import mx.proyecto.backend_api.entities.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    List<Alumno> findByProgramaAsignadoId(Long programaId);
}