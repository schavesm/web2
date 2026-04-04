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
        when(productoRepository.findById(producto.getId())).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);
        Producto result = productoServiceImpl.actualizar(producto.getId(),producto);
        assertNotNull(result);
        assertEquals(1L,result.getId());
        assertEquals("Laptop dell", result.getNombre());
        assertEquals(2500000.0, result.getPrecio());
        assertEquals(10 , result.getStock());
        assertEquals("Electronica", result.getCategoria());
        assertEquals(true, result.getActivo());
        verify(productoRepository, times(1)).save(any(Producto.class));

    }
    @Test
    void testBorrar_exitoso() {
        doNothing().when(productoRepository).deleteById(producto.getId());
        productoServiceImpl.borrar(producto.getId());
        verify(productoRepository,times(1)).deleteById(producto.getId());


    }


}
