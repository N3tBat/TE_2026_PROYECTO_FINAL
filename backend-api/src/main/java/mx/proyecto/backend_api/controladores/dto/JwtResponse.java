package mx.proyecto.backend_api.controladores.dto;

public class JwtResponse {
    private String token;
    private String rol;
    private String nombre;

    public JwtResponse(String token, String rol, String nombre) {
        this.token = token;
        this.rol = rol;
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
