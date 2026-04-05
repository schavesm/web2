package co.edu.ucompensar.web2.service;

import co.edu.ucompensar.web2.Exception.ProductoNotFoundException;
import co.edu.ucompensar.web2.modelo.Producto;
import co.edu.ucompensar.web2.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoServiceImpl;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop dell");
        producto.setPrecio(2500000.0);
        producto.setStock(10);
        producto.setCategoria("Electronica");
        producto.setActivo(true);
    }
    @Test
    void testCrear_exitoso(){
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        Producto result = productoServiceImpl.crear(producto);
        assertNotNull(result);
        assertEquals(1L,result.getId());
        assertEquals("Laptop dell", result.getNombre());
        assertEquals(2500000.0, result.getPrecio());
        assertEquals(10 , result.getStock());
        assertEquals("Electronica", result.getCategoria());
        assertEquals(true, result.getActivo());
        verify(productoRepository,times(1)).save(any(Producto.class));
    }
    @Test
    void testCrear_nombreNulo(){
        Producto productoNulo = new Producto();
        productoNulo.setNombre(null);
        productoNulo.setId(1L);
        productoNulo.setPrecio(2500000.0);
        productoNulo.setStock(10);
        productoNulo.setCategoria("Electronica");
        productoNulo.setActivo(true);
        assertThrows(IllegalArgumentException.class, ()-> {
            productoServiceImpl.crear(productoNulo);
        });
    }
    @Test
    void testCrear_precioNegativo() {
        Producto precioMenorCero = new Producto();
        precioMenorCero.setPrecio(-2000000.0);
        precioMenorCero.setNombre("Laptop dell");
        precioMenorCero.setId(1L);
        precioMenorCero.setStock(10);
        precioMenorCero.setCategoria("Electronica");
        precioMenorCero.setActivo(true);
        assertThrows(IllegalArgumentException.class, ()->{
            productoServiceImpl.crear(precioMenorCero);
        });

    }
    @Test
    void testCrear_stockNegativo() {
        Producto stockMenorCero = new Producto();
        stockMenorCero.setStock(-5);
        stockMenorCero.setPrecio(2000000.0);
        stockMenorCero.setNombre("Laptop dell");
        stockMenorCero.setId(1L);
        stockMenorCero.setCategoria("Electronica");
        stockMenorCero.setActivo(true);
        assertThrows(IllegalArgumentException.class, ()->{
            productoServiceImpl.crear(stockMenorCero);
        });

    }


    @Test
    void testObtener_exitoso(){
        when(productoRepository.findById(producto.getId())).thenReturn(Optional.of(producto));
        Producto result = productoServiceImpl.obtener(producto.getId());
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productoRepository,times(1)).findById((producto.getId()));

    }
    @Test
    void testObtenerPorId_noExiste(){
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ProductoNotFoundException.class, ()->{
            productoServiceImpl.obtener(99L);
        });
        verify(productoRepository).findById(99L);
    }

    @Test
    void testObtenerPorId_mensajeError(){
        when(productoRepository.findById(producto.getId())).thenReturn(Optional.empty());
        ProductoNotFoundException exception = assertThrows(
                ProductoNotFoundException.class,
                () -> productoServiceImpl.obtener(producto.getId())
        );

        assertTrue(exception.getMessage().contains("1"));
    }

    @Test
    void testListar_exitoso(){
        when(productoRepository.findAll()).thenReturn(List.of(producto));
        List<Producto> result = productoServiceImpl.listar();
        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals(1L,result.get(0).getId());
        assertEquals("Laptop dell",result.get(0).getNombre());
        assertEquals(2500000.0,result.get(0).getPrecio());
        assertEquals(10 ,result.get(0).getStock());
        assertEquals("Electronica",result.get(0).getCategoria());
        assertEquals(true,result.get(0).getActivo());
        assertFalse(result.isEmpty());
        verify(productoRepository, times(1)).findAll();
    }
    @Test
    void testObtenerTodos_listaVacia(){
        when(productoRepository.findAll()).thenReturn(List.of());
        List<Producto> result = productoServiceImpl.listar();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productoRepository).findAll();

    }

    @Test
    void testActualizar_exitoso(){
        Producto producto1 = new Producto();
        producto1.setNombre("Laptop hp");
        producto1.setPrecio(150000.0);
        producto1.setStock(20);
        producto1.setCategoria("electronica 2");
        producto1.setActivo(false);

        when(productoRepository.findById(producto.getId())).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto1);
        Producto result = productoServiceImpl.actualizar(producto.getId(),producto1);
        assertNotNull(result);
        assertEquals("Laptop hp", result.getNombre());
        assertEquals(150000.0, result.getPrecio());
        assertEquals(20 , result.getStock());
        assertEquals("electronica 2", result.getCategoria());
        assertEquals(false, result.getActivo());
        verify(productoRepository, times(1)).save(any(Producto.class));

    }
    @Test
    void testActualizar_noExiste(){
        Producto producto2 = new Producto();
        producto2.setNombre("Laptop hp");
        producto2.setPrecio(150000.0);
        producto2.setStock(20);
        producto2.setCategoria("electronica 2");
        producto2.setActivo(false);

        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ProductoNotFoundException.class, ()-> {
            productoServiceImpl.actualizar(99L, producto2);
        });

    }
    @Test
    void testBorrar_exitoso() {
        when(productoRepository.existsById(producto.getId())).thenReturn(true);
        doNothing().when(productoRepository).deleteById(producto.getId());
        productoServiceImpl.borrar(producto.getId());
        verify(productoRepository).deleteById(producto.getId());
    }
    @Test
    void testEliminar_noExiste_noLlamaDelete(){
        when(productoRepository.existsById(producto.getId())).thenReturn(false);
        assertThrows(ProductoNotFoundException.class, () -> {
            productoServiceImpl.borrar(producto.getId());
        });
        verify(productoRepository, never()).deleteById(any());
    }
    @Test
    void testEliminar_noExiste(){

        when(productoRepository.existsById(99L)).thenReturn(false);

        assertThrows(ProductoNotFoundException.class,
                () -> productoServiceImpl.borrar(99L));

        verify(productoRepository, never()).deleteById(99L);
    }



    @Test
    void testBuscarPorCategoria_exitoso() {
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Monitor Samsung");
        producto2.setDescripcion("Monitor 24\"");
        producto2.setPrecio(600000.0);
        producto2.setStock(5);
        producto2.setCategoria("Electronica");
        producto2.setActivo(true);

        List<Producto> productosEnCategoria = List.of(producto, producto2);

        when(productoRepository.findByCategoria("Electronica"))
                .thenReturn(productosEnCategoria);

        List<Producto> result = productoServiceImpl.buscarPorCategoria("Electronica");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electronica", result.get(0).getCategoria());
        assertEquals("Electronica", result.get(1).getCategoria());
        verify(productoRepository, times(1)).findByCategoria("Electronica");
    }

    @Test
    void testBuscarPorCategoria_categoriaNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            productoServiceImpl.buscarPorCategoria(null);
        });

        verify(productoRepository, never()).findByCategoria(any());
    }

    @Test
    void testBuscarPorCategoria_categoriaVacia() {
        assertThrows(IllegalArgumentException.class, () -> {
            productoServiceImpl.buscarPorCategoria("");
        });

        verify(productoRepository, never()).findByCategoria(any());
    }

    @Test
    void testBuscarPorCategoria_categoriaSoloEspacios() {
        assertThrows(IllegalArgumentException.class, () -> {
            productoServiceImpl.buscarPorCategoria("   ");
        });

        verify(productoRepository, never()).findByCategoria(any());
    }

    @Test
    void testBuscarPorCategoria_sinResultados() {
        when(productoRepository.findByCategoria("NoExiste"))
                .thenReturn(List.of());

        List<Producto> result = productoServiceImpl.buscarPorCategoria("NoExiste");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productoRepository, times(1)).findByCategoria("NoExiste");
    }

    @Test
    void testBuscarPorCategoria_unResultado() {
        when(productoRepository.findByCategoria("Electronica"))
                .thenReturn(List.of(producto));

        List<Producto> result = productoServiceImpl.buscarPorCategoria("Electronica");


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Laptop dell", result.get(0).getNombre());
        assertEquals("Electronica", result.get(0).getCategoria());
        verify(productoRepository, times(1)).findByCategoria("Electronica");
    }
    @Test
    void testConstructorProducto() {

        Long id = 1L;
        String nombre = "Laptop";
        String descripcion = "Laptop gamer";
        Double precio = 3500.0;
        Integer stock = 10;
        String categoria = "Tecnologia";
        Boolean activo = true;

        Producto producto = new Producto(
                id, nombre, descripcion, precio, stock, categoria, activo
        );

        assertEquals(id, producto.getId());
        assertEquals(nombre, producto.getNombre());
        assertEquals(descripcion, producto.getDescripcion());
        assertEquals(precio, producto.getPrecio());
        assertEquals(stock, producto.getStock());
        assertEquals(categoria, producto.getCategoria());
        assertEquals(activo, producto.getActivo());
    }

}
