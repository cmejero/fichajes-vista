package Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * CursoServicio
 * Servicio del proyecto web encargado de comunicarse con la API REST
 * para obtener información de cursos.
 */
public class CursoServicio {


    /**
     * Llama a la API externa y devuelve el JSON de los cursos.
     *
     * @return JSON String devuelto por la API
     * @throws Exception si ocurre un error en la conexión
     */
    public String obtenerCursosDesdeAPI() throws Exception {

        URL url = new URL("http://localhost:9527/api/cursos");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error HTTP: " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader((conn.getInputStream()), "UTF-8")
        );

        StringBuilder salida = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            salida.append(linea);
        }

        conn.disconnect();
        return salida.toString();
    }
}
