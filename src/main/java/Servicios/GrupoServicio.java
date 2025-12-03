package Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * GrupoServicio
 * Servicio del proyecto web encargado de comunicarse con la API REST
 * para obtener los grupos asociados a un curso.
 */
public class GrupoServicio {


    /**
     * Llama a la API REST externa para obtener los grupos pertenecientes
     * a un curso concreto.
     *
     * @param idCurso ID del curso
     * @return JSON devuelto por la API
     * @throws Exception si ocurre un error de conexión
     */
    public String obtenerGruposPorCursoDesdeAPI(Long idCurso) throws Exception {

        URL url = new URL("http://localhost:9527/api/grupos/curso/" + idCurso);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8")
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
