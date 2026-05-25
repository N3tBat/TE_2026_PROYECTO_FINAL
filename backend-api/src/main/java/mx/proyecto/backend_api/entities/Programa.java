package mx.proyecto.backend_api.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "programas")
public class Programa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String areaResponsable;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private Integer horasRequeridas;
    private String estado; // activo, inactivo o finalizado

    // Relación N:M con Profesores
    @ManyToMany(mappedBy = "programasAsignados")
    @JsonIgnore
    private List<Profesor> profesores;

    // --- GETTERS Y SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getAreaResponsable() { return areaResponsable; }
    public void setAreaResponsable(String areaResponsable) { this.areaResponsable = areaResponsable; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaTermino() { return fechaTermino; }
    public void setFechaTermino(LocalDate fechaTermino) { this.fechaTermino = fechaTermino; }

    public Integer getHorasRequeridas() { return horasRequeridas; }
    public void setHorasRequeridas(Integer horasRequeridas) { this.horasRequeridas = horasRequeridas; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<Profesor> getProfesores() { return profesores; }
    public void setProfesores(List<Profesor> profesores) { this.profesores = profesores; }
}