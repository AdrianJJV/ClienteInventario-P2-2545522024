package sv.edu.utec;

import sv.edu.utec.datos.ProductoDAO;
import sv.edu.utec.modelo.Producto;
import sv.edu.utec.servicio.InventarioJsonService;
import sv.edu.utec.servicio.SincronizacionService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {

    private static final ProductoDAO dao = new ProductoDAO();
    private static final InventarioJsonService jsonService = new InventarioJsonService();
    private static final String ARCHIVO = "inventario.json";

    public static void main(String[] args) {
        try {
            // 1. Preparar la base de datos
            dao.crearTabla();
            System.out.println("Tabla producto lista.");
            sembrarDatos();

            System.out.println("\n--- Inventario inicial ---");
            imprimir(dao.listar());

            // 2. Respaldar en JSON ANTES de modificar nada
            jsonService.exportar(ARCHIVO);
            System.out.println("\nRespaldo generado en " + ARCHIVO);

            // 3. Modificar la base de datos
            if (dao.actualizar(new Producto(2, "Monitor 24 pulgadas", 12))) {
                System.out.println("Producto 2 actualizado.");
            }
            if (dao.eliminar(1)) {
                System.out.println("Producto 1 eliminado.");
            }

            System.out.println("\n--- Despues de los cambios ---");
            imprimir(dao.listar());

            // 4. Restaurar desde el respaldo
            int restaurados = jsonService.importar(ARCHIVO);
            System.out.println("\nRegistros restaurados desde JSON: " + restaurados);

            System.out.println("\n--- Inventario final ---");
            imprimir(dao.listar());

            // --- ENUNCIADO 4: Sincronización con la API ---
            SincronizacionService sincronizacionService = new SincronizacionService(dao);
            int[] res = sincronizacionService.sincronizar(10);

            System.out.println("\nSincronizacion con la API -> insertados: " + res[0] + " | actualizados: " + res[1]);
            System.out.println("\n--- Inventario sincronizado ---");
            imprimir(dao.listar());

        } catch (SQLException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al leer o escribir el archivo JSON/API: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Error de interrupcion: " + e.getMessage());
        }
    }

    private static void sembrarDatos() throws SQLException {
        // Sembrar datos iniciales si la tabla está vacía
        if (dao.listar().isEmpty()) {
            dao.insertar(new Producto(1, "Laptop", 5));
            dao.insertar(new Producto(2, "Monitor", 10));
        }
    }

    private static void imprimir(List<Producto> lista) {
        System.out.printf("%-5s %-35s %-10s%n", "ID", "PRODUCTO", "CANTIDAD");
        for (Producto p : lista) {
            System.out.printf("%-5d %-35s %-10d%n", p.getId(), p.getNombre(), p.getCantidad());
        }
    }
}