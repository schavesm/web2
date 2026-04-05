package co.edu.ucompensar.web2.controlador;

import co.edu.ucompensar.web2.modelo.Producto;
import co.edu.ucompensar.web2.modelo.Usuario;
import co.edu.ucompensar.web2.service.Productoservice;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController              // = @Controller + @ResponseBody
@RequestMapping("/api/productos")
public class ProductoController {


    private final Productoservice productoservice;

    public ProductoController(Productoservice productoservice) {
        this.productoservice = productoservice;
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {

        Producto creado = productoservice.crear(producto);
        return ResponseEntity
                .status(HttpStatus.CREATED)   // 201
                .body(creado);
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {

        return ResponseEntity.ok(productoservice.listar()); // 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(
            @PathVariable Long id) {

        return ResponseEntity.ok(productoservice.obtener(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @RequestBody  Producto producto) {

        return ResponseEntity.ok(productoservice.actualizar(id, producto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        productoservice.borrar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> buscarPorCategoria(
            @PathVariable String categoria) {

        return ResponseEntity.ok(productoservice.buscarPorCategoria(categoria));
    }

}
