package co.edu.ucompensar.web2.Exception;

public class ProductoNotFoundException extends RuntimeException {

    public ProductoNotFoundException(String message) {
        super("Producto no encontrado: " + message);
    }

}
