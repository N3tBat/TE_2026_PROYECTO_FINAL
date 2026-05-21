package mx.proyecto.backend_api.repositorios;
// JPA para busquedas
import mx.proyecto.backend_api.entities.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    Optional<Profesor> findByCorreo(String correo);
}