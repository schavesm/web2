package co.edu.ucompensar.web2.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;


@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("documento")
    @Column (unique = true, nullable = false, length = 20)
    private String documento;

    @JsonProperty("nombres")
    @Column(nullable = false, length = 20, name = "nombres")
    private String nombres;

    @JsonProperty("apellidos")
    @Column(nullable = false, length = 20, name = "apellidos")
    private String apellidos;

    @JsonProperty("email")
    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @JsonProperty("password")
    @Column(nullable = false, length = 20)
    private String password;

    public Usuario() {
    }

    public Usuario(Long id, String documento, String nombre, String apellido, String email, String password) {
        this.id = id;
        this.documento = documento;
        this.nombres = nombre;
        this.apellidos = apellido;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombres;
    }

    public void setNombre(String nombre) {
        this.nombres = nombre;
    }

    public String getApellido() {
        return apellidos;
    }

    public void setApellido(String apellido) {
        this.apellidos = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
