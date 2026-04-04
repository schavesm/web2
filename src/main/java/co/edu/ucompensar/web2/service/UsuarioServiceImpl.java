package co.edu.ucompensar.web2.service;

import co.edu.ucompensar.web2.modelo.Usuario;
import co.edu.ucompensar.web2.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {


    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public Usuario crear(Usuario usuario) {

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id,Usuario usuario) {
        Usuario existente = obtenerPorId(id);
        existente.setDocumento(usuario.getDocumento());
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setEmail(usuario.getEmail());
        existente.setPassword(usuario.getPassword());
        return usuarioRepository.save(existente);
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario obtenerPorId(Long id) {

        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
