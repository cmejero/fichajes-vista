package Servicios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.json.JSONObject;
import Dtos.MatriculacionDto;
import Log.Log;

/**
 * Clase que se encarga de la logica de los metodos CRUD matriculacion
 */
public class MatriculacionServicio {

    private static final String BASE_API = "http://localhost:9527/api/guardarMatriculacion";
    private static final String BASE_API_LISTAR_POR_ALUMNO = "http://localhost:9527/api/matriculaciones/alumno/";


    /**
     * Guarda una nueva matriculación enviando los datos al endpoint de la API.
     *
     * @param dto Objeto MatriculacionDto con los datos de la matrícula.
     */
    public void guardarMatriculacion(MatriculacionDto dto) {
        try {
            // Construir JSON
            JSONObject json = new JSONObject();
            json.put("alumnoId", dto.getAlumnoId());
            json.put("cursoId", dto.getCursoId());
            json.put("grupoId", dto.getGrupoId());
            json.put("anioEscolar", dto.getAnioEscolar());
            json.put("uidLlave", dto.getUidLlave());

            String resp = ejecutarPost(BASE_API, json);

        } catch (Exception e) {
   
        }
    }

    /**
     * Ejecuta una solicitud HTTP POST a la URL indicada con un cuerpo JSON opcional.
     *
     * @param urlStr URL a la que se realiza la solicitud POST.
     * @param body   JSONObject con los datos a enviar; puede ser null.
     * @return Contenido de la respuesta como String.
     * @throws Exception Si ocurre un error en la conexión o si el código HTTP no es 200 o 201.
     */
    private String ejecutarPost(String urlStr, JSONObject body) throws Exception {
        java.net.URI uri = new java.net.URI(urlStr);
        java.net.URL url = uri.toURL();

        HttpURLConnection conex = (HttpURLConnection) url.openConnection();
        conex.setRequestMethod("POST");
        conex.setRequestProperty("Content-Type", "application/json");
        conex.setDoOutput(true);

        if (body != null) {
            try (var os = conex.getOutputStream()) {
                byte[] input = body.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        int code = conex.getResponseCode();
        InputStream stream = (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED)
                ? conex.getInputStream()
                : conex.getErrorStream();

        String resp = readStream(stream);

        if (code != HttpURLConnection.HTTP_OK && code != HttpURLConnection.HTTP_CREATED) {
            Log.ficheroLog("POST " + urlStr + " -> " + code + " : " + resp);
            throw new RuntimeException("Error en POST: " + code + " - " + resp);
        }

        return resp;
    }
    
    
    /**
     * Obtiene todas las matriculaciones de un alumno desde la API.
     *
     * @param idAlumno ID del alumno
     * @return JSON con la lista de matriculaciones
     */
    public String obtenerMatriculacionesPorAlumno(Long idAlumno) {
        try {
            String url = BASE_API_LISTAR_POR_ALUMNO + idAlumno;
            return ejecutarGet(url);
        } catch (Exception e) {
            Log.ficheroLog("❌ Error al obtener matriculaciones por alumno: " + e.getMessage());
            e.printStackTrace();
            return "[]";
        }
    }
    
    private String ejecutarGet(String urlStr) throws Exception {
        java.net.URI uri = new java.net.URI(urlStr);
        java.net.URL url = uri.toURL();
        HttpURLConnection conex = (HttpURLConnection) url.openConnection();
        conex.setRequestMethod("GET");
        conex.setRequestProperty("Accept", "application/json");

        int code = conex.getResponseCode();
        InputStream stream = (code == HttpURLConnection.HTTP_OK) ? conex.getInputStream() : conex.getErrorStream();

        String resp = readStream(stream);
        if (code != HttpURLConnection.HTTP_OK) {
            Log.ficheroLog("GET " + urlStr + " -> " + code + " : " + resp);
            throw new RuntimeException("Error en GET: " + code + " - " + resp);
        }
        return resp;
    }
    
    /**
     * Elimina una matrícula en la API por su ID
     * 
     * @param idMatriculacion ID de la matrícula a eliminar
     * @return true si se eliminó correctamente, false si no
     */
    public boolean eliminarMatriculacion(Long idMatriculacion) {
        try {
            String url = "http://localhost:9527/api/eliminarMatriculacion/" + idMatriculacion;
            java.net.URI uri = new java.net.URI(url);
            java.net.URL urlObj = uri.toURL();
            HttpURLConnection conex = (HttpURLConnection) urlObj.openConnection();
            conex.setRequestMethod("DELETE");
            conex.setRequestProperty("Accept", "application/json");

            int code = conex.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                Log.ficheroLog("✅ Matrícula eliminada en API: " + idMatriculacion);
                return true;
            } else {
                InputStream errStream = conex.getErrorStream();
                String resp = readStream(errStream);
                Log.ficheroLog("❌ Error eliminando matrícula en API: " + resp);
                return false;
            }
        } catch (Exception e) {
            Log.ficheroLog("❌ ERROR en eliminarMatriculacion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



    /**
     * Lee un InputStream y devuelve su contenido como String.
     *
     * @param is InputStream a leer.
     * @return Contenido del stream como String.
     * @throws Exception Si ocurre un error de lectura.
     */
    private String readStream(InputStream is) throws Exception {
        if (is == null) return "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

}
