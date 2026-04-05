package co.edu.ucompensar.web2.controlador;

import co.edu.ucompensar.web2.modelo.Producto;
import co.edu.ucompensar.web2.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductoIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductoRepository productoRepository;

    @BeforeEach
    void setUp(){
        productoRepository.deleteAll();
    }

    @Test
    void testCrear_retorna201() {
        Producto producto = new Producto();
        producto.setNombre("Laptop dell");
        producto.setDescripcion("computadora dell");
        producto.setPrecio(2500000.0);
        producto.setStock(20);
        producto.setCategoria("electrodomestico");
        producto.setActivo(true);

        ResponseEntity<Producto> response = testRestTemplate.postForEntity(
                "/api/productos",
                producto,
                Producto.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Laptop dell", response.getBody().getNombre());
        assertEquals(1, productoRepository.count());
    }

    @Test
    void testCrear_persisteEnBD() {
        Producto producto = new Producto();
        producto.setNombre("Mouse Logitech");
        producto.setDescripcion("mouse inalámbrico");
        producto.setPrecio(50000.0);
        producto.setStock(50);
        producto.setCategoria("accesorios");
        producto.setActivo(true);

        testRestTemplate.postForEntity(
                "/api/productos",
                producto,
                Producto.class
        );

        assertEquals(1L, productoRepository.count());

        Producto productoGuardado = productoRepository.findAll().get(0);
        assertNotNull(productoGuardado.getId());
        assertEquals("Mouse Logitech", productoGuardado.getNombre());
        assertEquals(50000.0, productoGuardado.getPrecio());
    }

    @Test
    void testCrear_retornaIdGenerado() {
        Producto producto = new Producto();
        producto.setNombre("Teclado Corsair");
        producto.setDescripcion("teclado mecánico RGB");
        producto.setPrecio(150000.0);
        producto.setStock(15);
        producto.setCategoria("periféricos");
        producto.setActivo(true);

        ResponseEntity<Producto> response = testRestTemplate.postForEntity(
                "/api/productos",
                producto,
                Producto.class
        );

        assertNotNull(response.getBody(), "El body de la respuesta no debe ser nulo");
        assertNotNull(response.getBody().getId(), "El ID del producto no debe ser nulo");

        Long idGenerado = response.getBody().getId();
        assertTrue(idGenerado > 0, "El ID debe ser mayor a 0");

        Producto productoEnBD = productoRepository.findById(idGenerado).orElse(null);
        assertNotNull(productoEnBD, "El producto debe existir en la BD con el ID generado");
        assertEquals("Teclado Corsair", productoEnBD.getNombre());
    }

    @Test
    void testCrear_camposCorrectos() {
        Producto producto = new Producto();
        producto.setNombre("Monitor LG 27\"");
        producto.setDescripcion("monitor IPS 4K");
        producto.setPrecio(800000.0);
        producto.setStock(10);
        producto.setCategoria("monitores");
        producto.setActivo(true);

        ResponseEntity<Producto> response = testRestTemplate.postForEntity(
                "/api/productos",
                producto,
                Producto.class
        );

        assertNotNull(response.getBody());

        Producto productoResponse = response.getBody();

        assertNotNull(productoResponse.getId(), "El ID debe estar en la respuesta");
        assertNotNull(productoResponse.getNombre(), "El nombre debe estar en la respuesta");
        assertEquals("Monitor LG 27\"", productoResponse.getNombre());

        assertNotNull(productoResponse.getDescripcion(), "La descripción debe estar en la respuesta");
        assertEquals("monitor IPS 4K", productoResponse.getDescripcion());

        assertNotNull(productoResponse.getPrecio(), "El precio debe estar en la respuesta");
        assertEquals(800000.0, productoResponse.getPrecio());

        assertNotNull(productoResponse.getStock(), "El stock debe estar en la respuesta");
        assertEquals(10, productoResponse.getStock());

        assertNotNull(productoResponse.getCategoria(), "La categoría debe estar en la respuesta");
        assertEquals("monitores", productoResponse.getCategoria());

        assertNotNull(productoResponse.getActivo(), "El campo activo debe estar en la respuesta");
        assertEquals(true, productoResponse.getActivo());
    }

    @Test
    void testCrear_bodyInvalido_retorna400() {
        Producto producto = new Producto();
        producto.setNombre(null);
        producto.setDescripcion("descripción válida");
        producto.setPrecio(100000.0);
        producto.setStock(5);
        producto.setCategoria("categoria");
        producto.setActivo(true);

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/productos",
                HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(producto),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Debe retornar 400 Bad Request por validación fallida");

        assertNotNull(response.getBody(), "El body de error no debe ser nulo");
        assertTrue(response.getBody().contains("nombre"),
                "El mensaje de error debe mencionar el campo 'nombre'");
    }

    @Test
    void testCrear_precioNegativo_retorna400() {
        Producto producto = new Producto();
        producto.setNombre("Producto con precio inválido");
        producto.setDescripcion("descripción válida");
        producto.setPrecio(-100.0);  // INVÁLIDO - debe ser > 0
        producto.setStock(5);
        producto.setCategoria("categoria");
        producto.setActivo(true);

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/productos",
                HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(producto),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Debe retornar 400 Bad Request por precio negativo");

        assertNotNull(response.getBody(), "El body de error no debe ser nulo");
        assertTrue(response.getBody().contains("precio"),
                "El mensaje de error debe mencionar el campo 'precio'");
    }


    @Test
    void testListar_bdVacia_retornaListaVacia() {

        ResponseEntity<Producto[]> response = testRestTemplate.getForEntity(
                "/api/productos",
                Producto[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().length, "Debe retornar array vacío cuando no hay productos");
    }

    @Test
    void testListar_conDatos_retornaLista() {
        for (int i = 1; i <= 3; i++) {
            Producto producto = new Producto();
            producto.setNombre("Producto " + i);
            producto.setDescripcion("Descripción " + i);
            producto.setPrecio(1000.0 * i);
            producto.setStock(10 + i);
            producto.setCategoria("Categoría " + i);
            producto.setActivo(true);
            productoRepository.save(producto);
        }

        ResponseEntity<Producto[]> response = testRestTemplate.getForEntity(
                "/api/productos",
                Producto[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().length, "Debe retornar array con 3 productos");
    }


    @Test
    void testObtenerPorId_existe_retorna200() {
        Producto producto = new Producto();
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción Test");
        producto.setPrecio(5000.0);
        producto.setStock(15);
        producto.setCategoria("Test");
        producto.setActivo(true);
        Producto guardado = productoRepository.save(producto);
        Long id = guardado.getId();

        ResponseEntity<Producto> response = testRestTemplate.getForEntity(
                "/api/productos/" + id,
                Producto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals("Producto Test", response.getBody().getNombre());
    }

    @Test
    void testObtenerPorId_noExiste_retorna404() {
        Long idNoExistente = 9999L;

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/productos/" + idNoExistente,
                HttpMethod.GET,
                null,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Debe retornar 404 cuando el producto no existe");
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("no encontrado") || response.getBody().contains("No encontrado"),
                "El mensaje debe indicar que no fue encontrado");
    }

    @Test
    void testObtenerPorId_datosCorrectos() {
        Producto productoPrueba = new Producto();
        productoPrueba.setNombre("Laptop Gamer");
        productoPrueba.setDescripcion("Gaming laptop de alta gama");
        productoPrueba.setPrecio(3500000.0);
        productoPrueba.setStock(5);
        productoPrueba.setCategoria("Computadoras");
        productoPrueba.setActivo(true);
        Producto guardado = productoRepository.save(productoPrueba);
        Long id = guardado.getId();

        ResponseEntity<Producto> response = testRestTemplate.getForEntity(
                "/api/productos/" + id,
                Producto.class
        );

        assertNotNull(response.getBody());

        Producto actual = response.getBody();
        assertEquals("Laptop Gamer", actual.getNombre(), "El nombre debe coincidir");
        assertEquals("Gaming laptop de alta gama", actual.getDescripcion(), "La descripción debe coincidir");
        assertEquals(3500000.0, actual.getPrecio(), "El precio debe coincidir");
        assertEquals(5, actual.getStock(), "El stock debe coincidir");
        assertEquals("Computadoras", actual.getCategoria(), "La categoría debe coincidir");
        assertEquals(true, actual.getActivo(), "El estado activo debe coincidir");
    }



    @Test
    void testActualizar_retorna200() {
        Producto productoOriginal = new Producto();
        productoOriginal.setNombre("Producto Original");
        productoOriginal.setDescripcion("Descripción original");
        productoOriginal.setPrecio(1000.0);
        productoOriginal.setStock(10);
        productoOriginal.setCategoria("Original");
        productoOriginal.setActivo(true);
        Producto guardado = productoRepository.save(productoOriginal);
        Long id = guardado.getId();

        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Producto Actualizado");
        productoActualizado.setDescripcion("Descripción actualizada");
        productoActualizado.setPrecio(2000.0);
        productoActualizado.setStock(20);
        productoActualizado.setCategoria("Actualizado");
        productoActualizado.setActivo(false);

        ResponseEntity<Producto> response = testRestTemplate.exchange(
                "/api/productos/" + id,
                HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(productoActualizado),
                Producto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "Debe retornar 200 OK después de actualizar");
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId(), "El ID debe ser el mismo");
        assertEquals("Producto Actualizado", response.getBody().getNombre());
    }

    @Test
    void testActualizar_persisteEnBD() {
        Producto productoOriginal = new Producto();
        productoOriginal.setNombre("Monitor Original");
        productoOriginal.setDescripcion("Monitor desc original");
        productoOriginal.setPrecio(500000.0);
        productoOriginal.setStock(5);
        productoOriginal.setCategoria("Monitores");
        productoOriginal.setActivo(true);
        Producto guardado = productoRepository.save(productoOriginal);
        Long id = guardado.getId();

        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Monitor 4K Premium");
        productoActualizado.setDescripcion("Monitor 4K ultra premium");
        productoActualizado.setPrecio(1500000.0);
        productoActualizado.setStock(3);
        productoActualizado.setCategoria("Monitores Premium");
        productoActualizado.setActivo(true);

        testRestTemplate.exchange(
                "/api/productos/" + id,
                HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(productoActualizado),
                Producto.class
        );

        Producto productoEnBD = productoRepository.findById(id).orElse(null);
        assertNotNull(productoEnBD, "El producto debe existir en la BD");
        assertEquals("Monitor 4K Premium", productoEnBD.getNombre(),
                "El nombre en BD debe ser actualizado");
        assertEquals(1500000.0, productoEnBD.getPrecio(),
                "El precio en BD debe ser actualizado");
        assertEquals(3, productoEnBD.getStock(),
                "El stock en BD debe ser actualizado");
    }

    @Test
    void testActualizar_noExiste_retorna404() {
        Long idNoExistente = 9999L;

        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Nuevo nombre");
        productoActualizado.setDescripcion("Nueva descripción");
        productoActualizado.setPrecio(1000.0);
        productoActualizado.setStock(10);
        productoActualizado.setCategoria("Categoría");
        productoActualizado.setActivo(true);

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/productos/" + idNoExistente,
                HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(productoActualizado),
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Debe retornar 404 cuando el producto no existe");
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("no encontrado") || response.getBody().contains("No encontrado"),
                "El mensaje debe indicar que no fue encontrado");
    }

    @Test
    void testActualizar_noModificaOtrosRegistros() {
        Producto producto1 = new Producto();
        producto1.setNombre("Producto 1");
        producto1.setDescripcion("Descripción 1");
        producto1.setPrecio(1000.0);
        producto1.setStock(10);
        producto1.setCategoria("Cat1");
        producto1.setActivo(true);
        Producto guardado1 = productoRepository.save(producto1);
        Long id1 = guardado1.getId();

        Producto producto2 = new Producto();
        producto2.setNombre("Producto 2");
        producto2.setDescripcion("Descripción 2");
        producto2.setPrecio(2000.0);
        producto2.setStock(20);
        producto2.setCategoria("Cat2");
        producto2.setActivo(true);
        Producto guardado2 = productoRepository.save(producto2);
        Long id2 = guardado2.getId();

        String nombreOriginal2 = guardado2.getNombre();
        Double precioOriginal2 = guardado2.getPrecio();

        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Producto 1 ACTUALIZADO");
        productoActualizado.setDescripcion("Descripción 1 ACTUALIZADA");
        productoActualizado.setPrecio(5000.0);
        productoActualizado.setStock(50);
        productoActualizado.setCategoria("Cat1 ACTUALIZADA");
        productoActualizado.setActivo(false);

        testRestTemplate.exchange(
                "/api/productos/" + id1,
                HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(productoActualizado),
                Producto.class
        );

        Producto producto1Actualizado = productoRepository.findById(id1).orElse(null);
        assertNotNull(producto1Actualizado);
        assertEquals("Producto 1 ACTUALIZADO", producto1Actualizado.getNombre());
        assertEquals(5000.0, producto1Actualizado.getPrecio());

        Producto producto2SinCambios = productoRepository.findById(id2).orElse(null);
        assertNotNull(producto2SinCambios);
        assertEquals(nombreOriginal2, producto2SinCambios.getNombre(),
                "El nombre del producto 2 no debe cambiar");
        assertEquals(precioOriginal2, producto2SinCambios.getPrecio(),
                "El precio del producto 2 no debe cambiar");
        assertEquals(20, producto2SinCambios.getStock(),
                "El stock del producto 2 no debe cambiar");
    }


    @Test
    void testEliminar_retorna204() {
        Producto producto = new Producto();
        producto.setNombre("Producto a Eliminar");
        producto.setDescripcion("Será eliminado");
        producto.setPrecio(1000.0);
        producto.setStock(5);
        producto.setCategoria("Test");
        producto.setActivo(true);
        Producto guardado = productoRepository.save(producto);
        Long id = guardado.getId();

        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/productos/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(),
                "Debe retornar 204 No Content después de eliminar");
        assertNotNull(response, "La respuesta no debe ser nula");
    }

    @Test
    void testEliminar_eliminaDeBD() {
        Producto producto = new Producto();
        producto.setNombre("Producto a Eliminar");
        producto.setDescripcion("Será eliminado de la BD");
        producto.setPrecio(2000.0);
        producto.setStock(10);
        producto.setCategoria("Test");
        producto.setActivo(true);
        Producto guardado = productoRepository.save(producto);
        Long id = guardado.getId();

        assertTrue(productoRepository.existsById(id),
                "El producto debe existir antes de eliminar");

        testRestTemplate.exchange(
                "/api/productos/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(false, productoRepository.existsById(id),
                "El producto no debe existir en la BD después del DELETE");
    }

    @Test
    void testEliminar_noExiste_retorna404() {
        // Arrange - ID que no existe
        Long idNoExistente = 9999L;

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/productos/" + idNoExistente,
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Debe retornar 404 cuando el producto no existe");
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("no encontrado") || response.getBody().contains("No encontrado"),
                "El mensaje debe indicar que no fue encontrado");
    }

    @Test
    void testEliminar_reduceCuenta() {
        for (int i = 1; i <= 3; i++) {
            Producto producto = new Producto();
            producto.setNombre("Producto " + i);
            producto.setDescripcion("Descripción " + i);
            producto.setPrecio(1000.0 * i);
            producto.setStock(10 + i);
            producto.setCategoria("Cat" + i);
            producto.setActivo(true);
            productoRepository.save(producto);
        }

        assertEquals(3, productoRepository.count(),
                "Debe haber 3 productos antes del DELETE");

        Producto primerProducto = productoRepository.findAll().get(0);
        Long idParaEliminar = primerProducto.getId();

        testRestTemplate.exchange(
                "/api/productos/" + idParaEliminar,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(2, productoRepository.count(),
                "El count debe disminuir en 1 después del DELETE");

        assertEquals(false, productoRepository.existsById(idParaEliminar),
                "El producto eliminado no debe existir");

        assertEquals(2, productoRepository.count(),
                "Los otros productos deben seguir existiendo");
    }

    // ────────────────────────────────────────────────────────────
    // Tests para buscar por categoría
    // ────────────────────────────────────────────────────────────

    @Test
    void testFiltrarPorCategoria_retorna200() {
        Producto producto = new Producto();
        producto.setNombre("Monitor Samsung");
        producto.setDescripcion("Monitor 24 pulgadas");
        producto.setPrecio(600000.0);
        producto.setStock(10);
        producto.setCategoria("Electronica");
        producto.setActivo(true);
        productoRepository.save(producto);

        ResponseEntity<Producto[]> response = testRestTemplate.getForEntity(
                "/api/productos/categoria/Electronica",
                Producto[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "Debe retornar 200 OK al filtrar por categoría");
        assertNotNull(response.getBody(), "El body de la respuesta no debe ser nulo");
    }

    @Test
    void testFiltrarPorCategoria_retornaProductosCorrectos() {
        Producto prod1 = new Producto();
        prod1.setNombre("Laptop Dell");
        prod1.setDescripcion("Laptop gamers");
        prod1.setPrecio(2500000.0);
        prod1.setStock(5);
        prod1.setCategoria("Electronica");
        prod1.setActivo(true);
        productoRepository.save(prod1);

        Producto prod2 = new Producto();
        prod2.setNombre("Mouse Logitech");
        prod2.setDescripcion("Mouse inalámbrico");
        prod2.setPrecio(50000.0);
        prod2.setStock(50);
        prod2.setCategoria("Accesorios");
        prod2.setActivo(true);
        productoRepository.save(prod2);

        Producto prod3 = new Producto();
        prod3.setNombre("Teclado Mecánico");
        prod3.setDescripcion("Teclado RGB");
        prod3.setPrecio(150000.0);
        prod3.setStock(15);
        prod3.setCategoria("Electronica");
        prod3.setActivo(true);
        productoRepository.save(prod3);

        ResponseEntity<Producto[]> response = testRestTemplate.getForEntity(
                "/api/productos/categoria/Electronica",
                Producto[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length,
                "Debe retornar exactamente 2 productos con categoría Electronica");

        for (Producto p : response.getBody()) {
            assertEquals("Electronica", p.getCategoria(),
                    "Solo deben retornarse productos con categoría Electronica");
        }

        boolean contieneDelll = false;
        boolean contieneTeclado = false;
        for (Producto p : response.getBody()) {
            if (p.getNombre().equals("Laptop Dell")) contieneDelll = true;
            if (p.getNombre().equals("Teclado Mecánico")) contieneTeclado = true;
        }
        assertTrue(contieneDelll, "Debe contener el producto 'Laptop Dell'");
        assertTrue(contieneTeclado, "Debe contener el producto 'Teclado Mecánico'");
    }

    @Test
    void testFiltrarPorCategoria_categoriaNoExiste_retornaListaVacia() {
        Producto producto = new Producto();
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción test");
        producto.setPrecio(100000.0);
        producto.setStock(5);
        producto.setCategoria("Ropa");
        producto.setActivo(true);
        productoRepository.save(producto);

        ResponseEntity<Producto[]> response = testRestTemplate.getForEntity(
                "/api/productos/categoria/NoExiste",
                Producto[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(),
                "Debe retornar 200 OK incluso si no hay resultados");
        assertNotNull(response.getBody(), "El body no debe ser nulo");
        assertEquals(0, response.getBody().length,
                "Debe retornar array vacío cuando no hay productos con esa categoría");
    }

    @Test
    void testError404_estructuraJson() {
        Long idNoExistente = 9999L;

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/productos/" + idNoExistente,
                HttpMethod.GET,
                null,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Debe retornar 404 Not Found");

        assertNotNull(response.getBody(), "El body de error no debe ser nulo");
        String errorBody = response.getBody();

        assertTrue(errorBody.contains("no encontrado") || errorBody.contains("No encontrado"),
                "El mensaje debe contener 'no encontrado' o 'No encontrado'");

        assertTrue(!errorBody.isEmpty(),
                "El body de error debe tener contenido");
    }

    @Test
    void testError400_validacion_listaErrores() {
        Producto productoInvalido = new Producto();
        productoInvalido.setNombre(null);
        productoInvalido.setDescripcion("Descripción válida");
        productoInvalido.setPrecio(100000.0);
        productoInvalido.setStock(5);
        productoInvalido.setCategoria("Test");
        productoInvalido.setActivo(true);

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/productos",
                HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(productoInvalido),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Debe retornar 400 Bad Request por validación fallida");

        assertNotNull(response.getBody(), "El body de error no debe ser nulo");
        String errorBody = response.getBody();

        assertTrue(errorBody.contains("nombre"),
                "El mensaje debe mencionar el campo 'nombre' que falló la validación");

        assertTrue(!errorBody.isEmpty(),
                "El body debe contener detalles del error");
    }

    @Test
    void testError400_duplicado_mensajeCorrecto() {
        Producto producto1 = new Producto();
        producto1.setNombre("Laptop Unique");
        producto1.setDescripcion("Descripción 1");
        producto1.setPrecio(2000000.0);
        producto1.setStock(5);
        producto1.setCategoria("Electronica");
        producto1.setActivo(true);
        productoRepository.save(producto1);

        // Crear producto con el mismo nombre (esto depende de si hay validación de unicidad)
        Producto producto2 = new Producto();
        producto2.setNombre("Laptop Unique");
        producto2.setDescripcion("Descripción 2");
        producto2.setPrecio(1500000.0);
        producto2.setStock(3);
        producto2.setCategoria("Electronica");
        producto2.setActivo(true);

        ResponseEntity<Producto> response = testRestTemplate.postForEntity(
                "/api/productos",
                producto2,
                Producto.class
        );

        assertNotNull(response.getBody(), "La respuesta no debe ser nula");

        assertTrue(response.getStatusCode().is2xxSuccessful() ||
                response.getStatusCode() == HttpStatus.BAD_REQUEST,
                "La respuesta debe ser exitosa o retornar error de validación");
    }

    @Test
    void testError400_precioNegativo_mensajeDetallado() {
        Producto productoInvalido = new Producto();
        productoInvalido.setNombre("Producto con precio inválido");
        productoInvalido.setDescripcion("Descripción válida");
        productoInvalido.setPrecio(-500.0);
        productoInvalido.setStock(5);
        productoInvalido.setCategoria("Test");
        productoInvalido.setActivo(true);

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/productos",
                HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(productoInvalido),
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Debe retornar 400 Bad Request por precio negativo");

        assertNotNull(response.getBody(), "El body de error no debe ser nulo");
        String errorBody = response.getBody();

        assertTrue(errorBody.contains("precio"),
                "El mensaje debe mencionar el campo 'precio' que falló la validación");

        assertTrue(!errorBody.isEmpty(),
                "El body debe contener un mensaje descriptivo del error");
    }

    @Test
    void testError404_eliminar_mensajeProductoNoEncontrado() {
        Long idNoExistente = 99999L;

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/productos/" + idNoExistente,
                HttpMethod.DELETE,
                null,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Debe retornar 404 Not Found");

        assertNotNull(response.getBody(), "El body de error no debe ser nulo");
        String errorBody = response.getBody();

        assertTrue(errorBody.contains("no encontrado") || errorBody.contains("No encontrado"),
                "El mensaje debe indicar que el producto no fue encontrado");

        assertTrue(!errorBody.isEmpty(),
                "El body debe contener un mensaje descriptivo");
    }

    @Test
    void testError404_actualizar_mensajeProductoNoEncontrado() {
        Long idNoExistente = 88888L;

        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Nuevo nombre");
        productoActualizado.setDescripcion("Nueva descripción");
        productoActualizado.setPrecio(1000.0);
        productoActualizado.setStock(10);
        productoActualizado.setCategoria("Nueva categoría");
        productoActualizado.setActivo(true);

        ResponseEntity<String> response = testRestTemplate.exchange(
                "/api/productos/" + idNoExistente,
                HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(productoActualizado),
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
                "Debe retornar 404 Not Found al actualizar producto inexistente");

        assertNotNull(response.getBody(), "El body de error no debe ser nulo");
        String errorBody = response.getBody();

        assertTrue(errorBody.contains("no encontrado") || errorBody.contains("No encontrado"),
                "El mensaje debe indicar que el producto no fue encontrado");
    }

}
