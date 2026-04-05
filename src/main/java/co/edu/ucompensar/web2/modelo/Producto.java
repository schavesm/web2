package co.edu.ucompensar.web2.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("nombre")
    @Column(nullable = false, length = 150)
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @JsonProperty("descripcion")
    @Column(nullable = false, length = 500)
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @JsonProperty("precio")
    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double precio;

    @JsonProperty("stock")
    @Column(nullable = false)
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @JsonProperty("categoria")
    @Column(length = 100)
    private String categoria;

    @JsonProperty("activo")
    @Column(nullable = false)
    private Boolean activo = true;

    public Producto() {
    }

    public Producto(Long id, String nombre, String descripcion, Double precio, Integer stock, String categoria, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
