package co.edu.ucompensar.web2.service;

import co.edu.ucompensar.web2.Exception.ProductoNotFoundException;
import co.edu.ucompensar.web2.modelo.Producto;
import co.edu.ucompensar.web2.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements Productoservice {

    private ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
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

    @Override
    public Producto obtener(Long id){

        return productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado" + id));
    }
    @Override
    public List<Producto> listar(){
        return productoRepository.findAll();
    };
    @Override
    public Producto actualizar(Long id, Producto producto){
        Producto existente = productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado" + id));
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setCategoria(producto.getCategoria());
        existente.setActivo(producto.getActivo());
        return productoRepository.save(existente);
    }
    @Override
    public void borrar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFoundException(String.valueOf(id));
        }
        productoRepository.deleteById(id);
    }
    @Override
    public List<Producto> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        }
        return productoRepository.findByCategoria(categoria);
    }



}
