package mx.proyecto.backend_api.controladores;

import mx.proyecto.backend_api.entities.Profesor;
import mx.proyecto.backend_api.servicios.interfaces.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    // GET /profesores
    @GetMapping
    public ResponseEntity<List<Profesor>> index() {
        return ResponseEntity.ok(profesorService.obtenerTodos());
    }

    // GET /profesores/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Profesor> getPorId(@PathVariable Long id) {
        return ResponseEntity.ok(profesorService.obtenerPorId(id));
    }

    // POST /profesores  — solo Administrador da de alta profesores
    @PostMapping
    public ResponseEntity<Profesor> crear(@RequestBody Profesor profesor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profesorService.guardar(profesor));
    }

    // PUT /profesores/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Profesor> actualizarCompleto(@PathVariable Long id, @RequestBody Profesor profesor) {
        return ResponseEntity.ok(profesorService.actualizarCompleto(id, profesor));
    }

    // PATCH /profesores/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<Profesor> actualizarParcial(@PathVariable Long id, @RequestBody Profesor profesor) {
        return ResponseEntity.ok(profesorService.actualizarParcial(id, profesor));
    }

    // DELETE /profesores/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        profesorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
