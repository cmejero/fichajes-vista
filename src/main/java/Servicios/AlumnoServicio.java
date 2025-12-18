package Servicios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.google.gson.Gson;

import Dtos.AlumnoConMatriculacionDto;
import Dtos.AlumnoDto;

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
     * Modifica los datos de un alumno mediante la API externa.
     *
     * @param idAlumno ID del alumno a modificar.
     * @param dto      Objeto con los nuevos datos del alumno.
     * @return true si la modificación fue exitosa, false en caso contrario.
     */
    public boolean modificarAlumno(Long idAlumno, AlumnoDto dto) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(dto);

            String urlApi = "http://localhost:9527/api/modificarAlumno/" + idAlumno;
            URL url = new URL(urlApi);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes("UTF-8"));
            }

            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
     * Obtiene un alumno desde la API.
     *
     * @return Cadena JSON con el alumno.
     */
    public AlumnoConMatriculacionDto obtenerAlumnoPorId(Long id) {
        try {
            String json = ejecutarGet("http://localhost:9527/api/alumno/" + id);
            return new Gson().fromJson(json, AlumnoConMatriculacionDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Elimina un alumno enviando una solicitud DELETE a la API.
     *
     * @param idAlumno ID del alumno a eliminar.
     * @return true si se eliminó correctamente, false si no existía.
     */
    public boolean eliminarAlumno(Long idAlumno) {
        try {
            java.net.URI uri = new java.net.URI("http://localhost:9527/api/eliminarAlumno/" + idAlumno);
            java.net.URL url = uri.toURL();

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Accept", "application/json");

            int code = con.getResponseCode();

            return code == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            System.out.println("❌ ERROR en AlumnoServicio.eliminarAlumno(): " + e.getMessage());
            e.printStackTrace();
            return false;
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
