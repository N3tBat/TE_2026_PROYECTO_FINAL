package mx.proyecto.backend_api.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "profesores")
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;
    private String correo; // Para iniciar sesión
    private String password;
    private String carrera;

    // Relación N:M con Programa (Un administrador asigna profesores a programas)
    @ManyToMany
    @JoinTable(
        name = "profesor_programa",
        joinColumns = @JoinColumn(name = "profesor_id"),
        inverseJoinColumns = @JoinColumn(name = "programa_id")
    )
    private List<Programa> programasAsignados;

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public List<Programa> getProgramasAsignados() { return programasAsignados; }
    public void setProgramasAsignados(List<Programa> programasAsignados) { this.programasAsignados = programasAsignados; }
}