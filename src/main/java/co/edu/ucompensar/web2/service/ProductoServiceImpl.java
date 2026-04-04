package co.edu.ucompensar.web2.service;

import co.edu.ucompensar.web2.modelo.Producto;
import co.edu.ucompensar.web2.repository.ProductoRepository;

import java.util.List;

public class ProductoServiceImpl implements Productoservice {

    private ProductoRepository productoRepository;

    public Producto crear(Producto producto) {

        return productoRepository.save(producto) ;
    }

    public Producto obtener(Long id){

        return productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
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
