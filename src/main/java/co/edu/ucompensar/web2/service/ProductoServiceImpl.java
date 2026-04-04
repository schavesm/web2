package co.edu.ucompensar.web2.service;

import co.edu.ucompensar.web2.Exception.ProductoNotFoundException;
import co.edu.ucompensar.web2.modelo.Producto;
import co.edu.ucompensar.web2.repository.ProductoRepository;

import java.util.List;

public class ProductoServiceImpl implements Productoservice {

    private ProductoRepository productoRepository;

    public Producto crear(Producto producto) {
        if (producto.getNombre() == null) {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
        if (producto.getPrecio() < 0){
            throw new IllegalArgumentException("El valor del producto no puede ser menor a 0");
        }
        if (producto.getStock() < 0){
            throw new IllegalArgumentException("El numero de stock no puede ser menor a 0");
        }

        return productoRepository.save(producto) ;
    }

    public Producto obtener(Long id){

        return productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado" + id));
    }

    public List<Producto> listar(){
        return productoRepository.findAll();
    };

    public Producto actualizar(Long id, Producto producto){
        Producto existente = productoRepository.findById(id).orElse(null);
        existente.setNombre(producto.getNombre());
        return productoRepository.save(existente);
    }
    public void borrar(Long id){

        productoRepository.deleteById(id);
    }



}
