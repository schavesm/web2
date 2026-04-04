package co.edu.ucompensar.web2.repository;

import co.edu.ucompensar.web2.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository <Usuario,Long> {
}
