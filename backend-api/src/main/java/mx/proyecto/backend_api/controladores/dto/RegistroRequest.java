package mx.proyecto.backend_api.controladores.dto;

import mx.proyecto.backend_api.entities.RolEnum;

public class RegistroRequest {
    private String correo;
    private String password;
    private RolEnum rol;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolEnum getRol() {
        return rol;
    }

    public void setRol(RolEnum rol) {
        this.rol = rol;
    }
}
