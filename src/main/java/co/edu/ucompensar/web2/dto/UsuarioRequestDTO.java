package co.edu.ucompensar.web2.dto;

import jakarta.validation.constraints.*;

public class UsuarioRequestDTO {

    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 20, message = "El documento no puede tener más de 20 caracteres")
    private String documento;

    @NotBlank
    @Size(max = 20, message = "Los nombres no pueden tener más de 100 caracteres")
    private String nombres;

    @NotBlank
    @Size(max = 20, message = "Los apellidos no pueden tener más de 100 caracteres")
    private String apellidos;

    @NotBlank
    @Email(message = "El correo debe ser una dirección de correo electrónico válida")
    private String correo;

    @NotBlank
    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    private String password;//123456

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

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
}
