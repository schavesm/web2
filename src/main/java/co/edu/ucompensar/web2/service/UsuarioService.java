package co.edu.ucompensar.web2.service;

import co.edu.ucompensar.web2.modelo.Usuario;

import java.util.List;

public interface UsuarioService {
    Usuario crear(Usuario usuario);
    Usuario actualizar(Long id, Usuario usuario);
    List<Usuario> obtenerTodos();
    Usuario obtenerPorId(Long id);
    void eliminar(Long id);
}
