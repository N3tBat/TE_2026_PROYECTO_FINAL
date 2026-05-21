package mx.proyecto.backend_api.controladores;

import mx.proyecto.backend_api.entities.Programa;
import mx.proyecto.backend_api.servicios.interfaces.ProgramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programas")
public class ProgramaController {

    @Autowired
    private ProgramaService programaService;

    // GET /programas
    @GetMapping
    public ResponseEntity<List<Programa>> index() {
        return ResponseEntity.ok(programaService.obtenerTodos());
    }

    // GET /programas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Programa> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(programaService.obtenerPorId(id));
    }

    // POST /programas  — solo Administrador
    @PostMapping
    public ResponseEntity<Programa> crear(@RequestBody Programa programa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(programaService.guardar(programa));
    }

    // PUT /programas/{id}  — solo Administrador
    @PutMapping("/{id}")
    public ResponseEntity<Programa> actualizarCompleto(@PathVariable Long id, @RequestBody Programa programa) {
        return ResponseEntity.ok(programaService.actualizarCompleto(id, programa));
    }

    // PATCH /programas/{id}  — solo Administrador
    @PatchMapping("/{id}")
    public ResponseEntity<Programa> actualizarParcial(@PathVariable Long id, @RequestBody Programa programa) {
        return ResponseEntity.ok(programaService.actualizarParcial(id, programa));
    }

    // DELETE /programas/{id}  — solo Administrador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        programaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // POST /programas/{programaId}/profesores/{profesorId}  — Administrador asigna profesor
    @PostMapping("/{programaId}/profesores/{profesorId}")
    public ResponseEntity<Programa> asignarProfesor(@PathVariable Long programaId, @PathVariable Long profesorId) {
        return ResponseEntity.ok(programaService.asignarProfesor(programaId, profesorId));
    }
}
