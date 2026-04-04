package co.edu.ucompensar.web2.service;

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
    void testBorrar_exitoso() {
        doNothing().when(productoRepository).deleteById(producto.getId());
        productoServiceImpl.borrar(producto.getId());
        verify(productoRepository,times(1)).deleteById(producto.getId());


    }


}
