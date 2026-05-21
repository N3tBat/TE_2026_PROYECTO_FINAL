package mx.proyecto.backend_api.repositorios;
//JPA para busquedas
import mx.proyecto.backend_api.entities.Programa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramaRepository extends JpaRepository<Programa, Long> {
}