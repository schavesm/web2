package co.edu.ucompensar.web2.controlador;

import co.edu.ucompensar.web2.modelo.Usuario;
import co.edu.ucompensar.web2.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController              // = @Controller + @ResponseBody
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // ── CREATE ────────────────────────── POST /api/usuarios
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {

        Usuario creado = service.crear(usuario);
        return ResponseEntity
                .status(HttpStatus.CREATED)   // 201
                .body(creado);
    }

    // ── READ ALL ─────────────────────── GET /api/usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {

        return ResponseEntity.ok(service.obtenerTodos()); // 200
    }

    // ── READ ONE ─────────────────── GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // ── UPDATE ───────────────────── PUT /api/usuarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @RequestBody  Usuario usuario) {

        return ResponseEntity.ok(service.actualizar(id, usuario));
    }

    // ── DELETE ────────────────── DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        service.eliminar(id);
        return ResponseEntity.noContent().build(); // 204
    }
}