package sv.edu.utec.datos;

import sv.edu.utec.datos.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    // main() solo ordena los pasos, no hace el trabajo
    public static void main(String[] args) {
        probarConexion();     // Bloque 2 - lo que ya tenías
        //crearTabla();         // Bloque 3, paso 1
        //insertarProductos();  // Bloque 3, paso 2
        listarProductos();    // Bloque 3, paso 3
    }

    // ---------- Bloque 2: tu código actual, ahora como método ----------
    private static void probarConexion() {
        try (Connection cn = ConexionDB.obtenerConexion()) {
            if (cn != null && !cn.isClosed()) {
                System.out.println("Conexion exitosa a: " + cn.getMetaData().getURL());
            }
        } catch (SQLException e) {
            System.out.println("Error de conexion: " + e.getMessage());
        }
    }

    // ---------- Paso 1: crear la tabla (Statement) ----------
    private static void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS producto (" +
                "id INT PRIMARY KEY, nombre VARCHAR(50), cantidad INT)";
        try (Connection cn = ConexionDB.obtenerConexion();
             Statement st = cn.createStatement()) {
            st.execute(sql);
            System.out.println("Tabla producto lista.");
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    // ---------- Paso 2: insertar (PreparedStatement) ----------
    private static void insertarProductos() {
        String sql = "INSERT INTO producto (id, nombre, cantidad) VALUES (?, ?, ?)";
        try (Connection cn = ConexionDB.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, 1);
            ps.setString(2, "Teclado mecanico");
            ps.setInt(3, 15);
            ps.executeUpdate();

            ps.setInt(1, 2);
            ps.setString(2, "Monitor 24 pulgadas");
            ps.setInt(3, 8);
            ps.executeUpdate();

            System.out.println("Productos insertados.");
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
        }
    }

    // ---------- Paso 3: leer (ResultSet) ----------
    private static void listarProductos() {
        String sql = "SELECT id, nombre, cantidad FROM producto";
        try (Connection cn = ConexionDB.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.printf("%-5s %-25s %10s%n", "ID", "PRODUCTO", "CANTIDAD");
            while (rs.next()) {
                System.out.printf("%-5d %-25s %10d%n",
                        rs.getInt("id"), rs.getString("nombre"), rs.getInt("cantidad"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar: " + e.getMessage());
        }
    }
}