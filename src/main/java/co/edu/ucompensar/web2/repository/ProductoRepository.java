package co.edu.ucompensar.web2.repository;

import co.edu.ucompensar.web2.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {

    // ── Métodos heredados de JpaRepository (automáticos) ──
    //  save(usuario)           → INSERT o UPDATE
    //  findById(id)            → SELECT por ID
    //  findAll()               → SELECT todos
    //  deleteById(id)          → DELETE por ID
    //  existsById(id)          → EXISTS por ID
    //  count()                 → COUNT total
    // ──────────────────────────────────────────────────

}
