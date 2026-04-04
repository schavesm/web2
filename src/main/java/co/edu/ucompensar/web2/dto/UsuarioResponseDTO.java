package co.edu.ucompensar.web2.dto;

import co.edu.ucompensar.web2.modelo.Usuario;

public class UsuarioResponseDTO {

    private Long   id;
    private String documento;
    private String nombres;
    private String apellidos;
    private String email;
    // ← password NO está aquí (seguridad)

    // Constructor a partir de la entidad
    public static UsuarioResponseDTO from(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.id         = u.getId();
        dto.documento  = u.getDocumento();
        dto.nombres    = u.getNombre();
        dto.apellidos  = u.getApellido();
        dto.email      = u.getEmail();
        return dto;
    }

    // Getters
    public Long   getId()        { return id; }
    public String getDocumento() { return documento; }
    public String getNombres()   { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getEmail()     { return email; }
}
