package co.edu.ucompensar.web2.service;

import co.edu.ucompensar.web2.modelo.Producto;

import java.util.List;

public interface Productoservice {

    Producto crear(Producto producto);
    Producto obtener(Long id);
    List<Producto> listar();
    Producto actualizar(Long id, Producto producto);
    void borrar (Long id);


}
