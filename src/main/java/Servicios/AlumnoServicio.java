package Servicios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.json.JSONObject;

import Dtos.AlumnoConMatriculacionDto;

/**
 * Clase que se encarga de la lógica de los métodos CRUD relacionados con Alumno.
 */
public class AlumnoServicio {



    /**
     * Envía los datos de un alumno a la API para guardarlo.
     *
     * @param alumno Objeto AlumnoConMatriculacionDto con los datos del alumno a guardar.
     */
    public void guardarAlumno(AlumnoConMatriculacionDto alumno) {
        try {

            JSONObject json = new JSONObject();
            json.put("nombreAlumno", alumno.getNombreAlumno());
            json.put("apellidoAlumno", alumno.getApellidoAlumno());
            json.put("cursoId", alumno.getCursoId());
            json.put("grupoId", alumno.getGrupoId());
            json.put("anioEscolar", alumno.getAnioEscolar());
            json.put("uidLlave", alumno.getUidLlave());

            ejecutarPost("http://localhost:9527/api/guardarAlumno", json);

        } catch (Exception e) {
            System.out.println("❌ ERROR en AlumnoServicio.guardarAlumno(): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene todos los alumnos desde la API.
     *
     * @return Cadena JSON con el listado completo de alumnos.
     */
    public String obtenerTodosAlumnos() {
        try {
            return ejecutarGet("http://localhost:9527/api/alumnos");
        } catch (Exception e) {
            System.out.println("❌ ERROR en AlumnoServicio.obtenerTodosAlumnos(): " + e.getMessage());
            e.printStackTrace();
            return "[]";
        }
    }

    /**
     * Ejecuta una solicitud HTTP GET a la URL indicada.
     *
     * @param urlStr URL a la que se enviará la solicitud GET.
     * @return Respuesta de la API como cadena JSON.
     * @throws Exception Si ocurre un error en la conexión o si la respuesta no es 200.
     */
    private String ejecutarGet(String urlStr) throws Exception {

        java.net.URI uri = new java.net.URI(urlStr);
        java.net.URL url = uri.toURL();

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");

        int code = con.getResponseCode();

        InputStream stream = (code == HttpURLConnection.HTTP_OK)
                ? con.getInputStream()
                : con.getErrorStream();

        String resp = readStream(stream);

        if (code != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Error GET " + code + " - " + resp);
        }

        return resp;
    }

    /**
     * Ejecuta una solicitud HTTP POST a la URL indicada con el cuerpo JSON proporcionado.
     *
     * @param urlStr URL a la que se enviará la solicitud POST.
     * @param body   Objeto JSONObject con los datos que se enviarán a la API.
     * @return Respuesta de la API como cadena JSON.
     * @throws Exception Si ocurre un error en la conexión o si la respuesta HTTP no es 200/201.
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
            throw new RuntimeException("Error POST " + code + " - " + resp);
        }

        return resp;
    }

    /**
     * Lee un InputStream y devuelve su contenido como una cadena.
     *
     * @param is InputStream a leer.
     * @return Contenido del InputStream como String.
     * @throws Exception Si ocurre un error durante la lectura.
     */
    private String readStream(InputStream is) throws Exception {

        if (is == null) return "";

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null)
                sb.append(line);

            return sb.toString();
        }
    }
}
