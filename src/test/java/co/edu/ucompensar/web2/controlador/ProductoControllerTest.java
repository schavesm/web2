package co.edu.ucompensar.web2.controlador;

import co.edu.ucompensar.web2.modelo.Producto;
import co.edu.ucompensar.web2.service.Productoservice;
import co.edu.ucompensar.web2.Exception.ProductoNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Productoservice productoservice;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPostProducto_retorna201() throws Exception {
        Producto request = new Producto();
        request.setNombre("Laptop dell");
        request.setDescripcion("computadora dell");
        request.setPrecio(2500000.0);
        request.setStock(20);
        request.setCategoria("electrodomestico");
        request.setActivo(true);

        Producto response = new Producto();
        response.setId(1L);
        response.setNombre("Laptop dell");
        response.setDescripcion("computadora dell");
        response.setPrecio(2500000.0);
        response.setStock(20);
        response.setCategoria("electrodomestico");
        response.setActivo(true);

        when(productoservice.crear(any(Producto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetTodos_retorna200() throws Exception {
        // Arrange
        Producto producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Laptop dell");
        producto1.setDescripcion("computadora dell");
        producto1.setPrecio(2500000.0);
        producto1.setStock(20);
        producto1.setCategoria("electrodomestico");
        producto1.setActivo(true);

        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Mouse Logitech");
        producto2.setDescripcion("mouse inalámbrico");
        producto2.setPrecio(50000.0);
        producto2.setStock(50);
        producto2.setCategoria("accesorios");
        producto2.setActivo(true);

        List<Producto> productosListados = Arrays.asList(producto1, producto2);

        when(productoservice.listar())
                .thenReturn(productosListados);

        mockMvc.perform(get("/api/productos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Laptop dell"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nombre").value("Mouse Logitech"));
    }

    @Test
    void testGetPorId_noExiste_retorna404() throws Exception {
        Long idNoExistente = 999L;
        when(productoservice.obtener(idNoExistente))
                .thenThrow(new ProductoNotFoundException(String.valueOf(idNoExistente)));

        mockMvc.perform(get("/api/productos/{id}", idNoExistente)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostProducto_bodyInvalido_retorna400() throws Exception {
        Producto requestInvalido = new Producto();
        requestInvalido.setNombre("");
        requestInvalido.setDescripcion("computadora dell");
        requestInvalido.setPrecio(2500000.0);
        requestInvalido.setStock(20);
        requestInvalido.setCategoria("electrodomestico");
        requestInvalido.setActivo(true);

        // Act & Assert
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").exists());
    }

    @Test
    void testDeleteProducto_retorna204() throws Exception {
        // Arrange
        Long idProducto = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/productos/{id}", idProducto)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}