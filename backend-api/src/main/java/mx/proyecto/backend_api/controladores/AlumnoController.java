package mx.proyecto.backend_api.controladores;

import mx.proyecto.backend_api.entities.Alumno;
import mx.proyecto.backend_api.servicios.interfaces.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    // GET /alumnos
    @GetMapping
    public ResponseEntity<List<Alumno>> index() {
        return ResponseEntity.ok(alumnoService.obtenerTodos());
    }

    // GET /alumnos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alumnoService.obtenerPorId(id));
    }

    // GET /alumnos/programa/{programaId}  — Profesor consulta alumnos de su programa
    @GetMapping("/programa/{programaId}")
    public ResponseEntity<List<Alumno>> getPorPrograma(@PathVariable Long programaId) {
        return ResponseEntity.ok(alumnoService.obtenerPorPrograma(programaId));
    }

    // POST /alumnos?programaId=X&profesorId=Y  — Profesor da de alta alumno en su programa
    @PostMapping
    public ResponseEntity<Alumno> crear(
            @RequestBody Alumno alumno,
            @RequestParam Long programaId,
            @RequestParam Long profesorId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(alumnoService.guardar(alumno, programaId, profesorId));
    }

    // PATCH /alumnos/{id}  — actualización parcial (ej. cambiar estado)
    @PatchMapping("/{id}")
    public ResponseEntity<Alumno> actualizarParcial(@PathVariable Long id, @RequestBody Alumno alumno) {
        return ResponseEntity.ok(alumnoService.actualizarParcial(id, alumno));
    }

    // DELETE /alumnos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alumnoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
