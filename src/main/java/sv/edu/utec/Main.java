package sv.edu.utec;

import sv.edu.utec.datos.ProductoDAO;
import sv.edu.utec.modelo.Producto;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static final ProductoDAO dao = new ProductoDAO();

    public static void main(String[] args) {
        try {
            dao.crearTabla();
            System.out.println("Tabla producto lista.");

            sembrarDatos();

            System.out.println("\n--- Inventario inicial ---");
            imprimir(dao.listar());

            if (dao.actualizar(new Producto(2, "Monitor 24 pulgadas", 12))) {
                System.out.println("\nProducto 2 actualizado.");
            }
            if (dao.eliminar(1)) {
                System.out.println("Producto 1 eliminado.");
            }

            System.out.println("\n--- Inventario final ---");
            imprimir(dao.listar());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }

    // Inserta solo lo que aun no existe: el programa es re-ejecutable
    private static void sembrarDatos() throws SQLException {
        if (!dao.existe(1)) dao.insertar(new Producto(1, "Teclado mecanico", 15));
        if (!dao.existe(2)) dao.insertar(new Producto(2, "Monitor 24 pulgadas", 8));
    }

    private static void imprimir(List<Producto> productos) {
        System.out.printf("%-5s %-25s %10s%n", "ID", "PRODUCTO", "CANTIDAD");
        for (Producto p : productos) {
            System.out.printf("%-5d %-25s %10d%n",
                    p.getId(), p.getNombre(), p.getCantidad());
        }
    }
}