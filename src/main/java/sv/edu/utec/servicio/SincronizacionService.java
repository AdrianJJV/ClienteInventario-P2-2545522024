package sv.edu.utec.servicio;

import sv.edu.utec.api.ProveedorAPI;
import sv.edu.utec.datos.ProductoDAO;
import sv.edu.utec.modelo.Producto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SincronizacionService {

    private final ProductoDAO productoDAO;
    private final ProveedorAPI proveedorAPI;

    public SincronizacionService(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
        this.proveedorAPI = new ProveedorAPI();
    }

    public SincronizacionService(ProductoDAO productoDAO, ProveedorAPI proveedorAPI) {
        this.productoDAO = productoDAO;
        this.proveedorAPI = proveedorAPI;
    }

    public int sincronizarConProveedor(int limite) throws IOException, InterruptedException, SQLException {
        List<Producto> productosProveedor = proveedorAPI.obtenerProductos(limite);
        int guardados = 0;

        for (Producto prod : productosProveedor) {
            if (!productoDAO.existe(prod.getId())) {
                productoDAO.insertar(prod);
                guardados++;
            }
        }

        return guardados;
    }
}