package sv.edu.utec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import sv.edu.utec.modelo.Producto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ProveedorAPI {

    public List<Producto> obtenerProductos(int limite) throws IOException, InterruptedException {
        String url = "https://dummyjson.com/products?limit=" + limite + "&select=title,stock";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error al consultar la API del proveedor. Código recibido: " + response.statusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        RespuestaProductos respuesta = mapper.readValue(response.body(), RespuestaProductos.class);

        List<Producto> listaProductos = new ArrayList<>();
        if (respuesta != null && respuesta.getProducts() != null) {
            for (ProductoApi apiProd : respuesta.getProducts()) {
                listaProductos.add(apiProd.aProducto());
            }
        }

        return listaProductos;
    }
}
