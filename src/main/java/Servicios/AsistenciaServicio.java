package Servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Dtos.AsistenciaDto;
import Log.Log;
/**
 * Clase que se encarga de la logica de los metodos CRUD asistencia
 */
public class AsistenciaServicio {

    private static final String BASE_API = "http://localhost:9527/api";

    // ---------------- POST ----------------

    /**
     * Registra la entrada de un alumno a partir de su ID de matriculación.
     *
     * @param matriculacionId ID de la matriculación del alumno.
     * @return Objeto AsistenciaDto con los datos de la asistencia registrada.
     * @throws Exception Si ocurre un error en la llamada HTTP o en el parseo de la respuesta.
     */
    public AsistenciaDto ficharEntrada(Long matriculacionId) throws Exception {
        String url = BASE_API + "/asistencia/entrada/" + matriculacionId;
        String resp = ejecutarPost(url, null);
        return parseAsistencia(resp);
    }

    /**
     * Registra la salida de un alumno a partir de su ID de matriculación.
     *
     * @param matriculacionId ID de la matriculación del alumno.
     * @return Objeto AsistenciaDto con los datos de la asistencia registrada.
     * @throws Exception Si ocurre un error en la llamada HTTP o en el parseo de la respuesta.
     */
    public AsistenciaDto ficharSalida(Long matriculacionId) throws Exception {
        String url = BASE_API + "/asistencia/salida/" + matriculacionId;
        String resp = ejecutarPut(url, null);
        return parseAsistencia(resp);
    }

    /**
     * Modifica una asistencia existente con los datos proporcionados.
     *
     * @param idAsistencia ID de la asistencia a modificar.
     * @param dto          Objeto AsistenciaDto con los nuevos datos.
     * @return Objeto AsistenciaDto actualizado.
     * @throws Exception Si ocurre un error en la llamada HTTP o en el parseo de la respuesta.
     */
    public AsistenciaDto modificarAsistencia(Long idAsistencia, AsistenciaDto dto) throws Exception {
        String url = BASE_API + "/modificarAsistencia/" + idAsistencia;

        JSONObject body = new JSONObject();
        body.put("estado", dto.getEstado());
        body.put("justificarModificacion", dto.getJustificarModificacion());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        if (dto.getHoraEntrada() != null) body.put("horaEntrada", dto.getHoraEntrada().format(dtf));
        if (dto.getHoraSalida() != null) body.put("horaSalida", dto.getHoraSalida().format(dtf));

        String resp = ejecutarPut(url, body); // método que hace PUT
        return parseAsistencia(resp);
    }

    // ---------------- GET ----------------

    
    
    /**
     * Obtiene todas las asistencias desde un servicio REST.
     *
     * Este método realiza una petición HTTP GET
     *
     * @return String que contiene la respuesta JSON con todas las asistencias.
     * @throws Exception si ocurre algún error durante la conexión HTTP o la lectura de la respuesta.
     */
    public String obtenerTodasAsistencias() throws Exception {

        URL url = new URL("http://localhost:9527/api/asistencias");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), java.nio.charset.StandardCharsets.UTF_8)
        );

        StringBuilder salida = new StringBuilder();
        String linea;

        while ((linea = br.readLine()) != null) {
            salida.append(linea);
        }

        conn.disconnect();
        return salida.toString();
    }

    /**
     * Obtiene las asistencias de un curso y grupo en una fecha específica.
     *
     * @param curso  Nombre del curso.
     * @param grupo  Nombre del grupo.
     * @param fecha  Fecha de la asistencia.
     * @return Lista de AsistenciaDto correspondiente.
     * @throws Exception Si ocurre un error en la llamada HTTP o en el parseo de la respuesta.
     */
    public List<AsistenciaDto> obtenerAsistenciaPorCursoYGrupoEnFecha(String curso, String grupo, LocalDate fecha) throws Exception {
        String url = BASE_API + "/asistencia/" + encode(curso) + "/" + encode(grupo);
        String resp = ejecutarGet(url);
        return parseListaAsistencia(resp);
    }

    /**
     * Obtiene las asistencias de un curso y grupo en una fecha específica usando query parameters.
     *
     * @param curso  Nombre del curso.
     * @param grupo  Nombre del grupo.
     * @param fecha  Fecha de la asistencia.
     * @return Lista de AsistenciaDto correspondiente.
     * @throws Exception Si ocurre un error en la llamada HTTP o en el parseo de la respuesta.
     */
    public List<AsistenciaDto> obtenerAsistenciaPorCursoYGrupoYFecha(String curso, String grupo, LocalDate fecha) throws Exception {
        String url = BASE_API + "/asistencia/curso-grupo"
                   + "?curso=" + encode(curso)
                   + "&grupo=" + encode(grupo)
                   + "&fecha=" + fecha.toString();

        Log.ficheroLog("➡️ [SERVICIO] Llamando a URL: " + url);

        String resp = ejecutarGet(url);
        return parseListaAsistencia(resp);
    }

    /**
     * Obtiene las asistencias de un alumno según estado y año escolar.
     *
     * @param alumnoId   ID del alumno.
     * @param estado     Estado de la asistencia (PRESENTE, FALTA, etc.).
     * @param anioEscolar Año escolar.
     * @return Lista de AsistenciaDto correspondiente.
     * @throws Exception Si ocurre un error en la llamada HTTP o en el parseo de la respuesta.
     */
    public List<AsistenciaDto> obtenerPorAlumnoYEstado(Long alumnoId, String estado, String anioEscolar) throws Exception {
        String url = BASE_API + "/asistencia/alumno-estado"
            + "?alumnoId=" + alumnoId
            + "&estado=" + encode(estado)
            + "&anioEscolar=" + encode(anioEscolar);

        String resp = ejecutarGet(url);
        return parseListaAsistencia(resp);
    }

    /**
     * Obtiene las asistencias de un alumno en un rango de fechas.
     *
     * @param alumnoId ID del alumno.
     * @param desde    Fecha inicial del rango.
     * @param hasta    Fecha final del rango.
     * @return Lista de AsistenciaDto correspondiente.
     * @throws Exception Si ocurre un error en la llamada HTTP o en el parseo de la respuesta.
     */
    public List<AsistenciaDto> obtenerPorAlumnoYRango(Long alumnoId, LocalDate desde, LocalDate hasta) throws Exception {
        String url = BASE_API + "/asistencia/rango/" + alumnoId + "?desde=" + desde + "&hasta=" + hasta;
        String resp = ejecutarGet(url);
        return parseListaAsistencia(resp);
    }

    /**
     * Obtiene el conteo de estados de asistencias de un alumno en un rango de fechas.
     *
     * @param alumnoId ID del alumno.
     * @param desde    Fecha inicial.
     * @param hasta    Fecha final.
     * @return Mapa con los estados de asistencia y sus conteos.
     * @throws Exception Si ocurre un error en la llamada HTTP o en el parseo de la respuesta.
     */
    public java.util.Map<String, Integer> obtenerConteoEstados(Long alumnoId, LocalDate desde, LocalDate hasta) throws Exception {
        String url = BASE_API + "/asistencia/conteoEstados/" + alumnoId
                     + "?desde=" + desde.toString()
                     + "&hasta=" + hasta.toString();

        String resp = ejecutarGet(url);
        JSONObject obj = new JSONObject(resp);
        java.util.Map<String, Integer> conteo = new java.util.HashMap<>();
        conteo.put("PRESENTE", obj.optInt("PRESENTE", 0));
        conteo.put("COMPLETA", obj.optInt("COMPLETA", 0));
        conteo.put("SIN SALIDA", obj.optInt("SIN SALIDA", 0));
        conteo.put("FALTA", obj.optInt("FALTA", 0));

        return conteo;
    }


    /**
     * Convierte un JSON en un objeto AsistenciaDto.
     *
     * @param json Cadena JSON de la asistencia.
     * @return Objeto AsistenciaDto correspondiente.
     */
    private AsistenciaDto parseAsistencia(String json) {
        JSONObject obj = new JSONObject(json);
        AsistenciaDto a = new AsistenciaDto();

        a.setIdAsistencia(obj.optLong("idAsistencia", 0));
        a.setAlumnoId(obj.optLong("alumnoId", 0));
        a.setNombreCompletoAlumno(obj.optString("nombreCompletoAlumno", ""));
        a.setNombreCurso(obj.optString("nombreCurso", ""));
        a.setNombreGrupo(obj.optString("nombreGrupo", ""));
        String fechaStr = obj.optString("fecha", null);
        if (fechaStr != null) a.setFecha(LocalDate.parse(fechaStr));
        String entradaStr = obj.optString("horaEntrada", null);
        if (entradaStr != null) a.setHoraEntrada(LocalDateTime.parse(entradaStr));
        String salidaStr = obj.optString("horaSalida", null);
        if (salidaStr != null) a.setHoraSalida(LocalDateTime.parse(salidaStr));
        a.setEstado(obj.optString("estado", ""));
        a.setJustificarModificacion(obj.optString("justificarModificacion", ""));

        return a;
    }

    /**
     * Convierte un JSON array en una lista de AsistenciaDto.
     *
     * @param json Cadena JSON con un array de asistencias.
     * @return Lista de AsistenciaDto correspondiente.
     */
    private List<AsistenciaDto> parseListaAsistencia(String json) {
        List<AsistenciaDto> lista = new ArrayList<>();
        if (json == null || json.isEmpty()) return lista;
        JSONArray arr = new JSONArray(json);
        for (int i = 0; i < arr.length(); i++) {
            lista.add(parseAsistencia(arr.getJSONObject(i).toString()));
        }
        return lista;
    }
    
    
    
    // ---------------- HTTP Helpers ----------------
    /**
     * Ejecuta una solicitud HTTP GET a la URL indicada.
     *
     * @param urlStr URL a la que se realiza la solicitud GET.
     * @return Contenido de la respuesta como String.
     * @throws Exception Si ocurre un error en la conexión o si el código HTTP no es 200.
     */
    private String ejecutarGet(String urlStr) throws Exception {
        var uri = new java.net.URI(urlStr);
        var url = uri.toURL();
        var conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        int code = conn.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            return readStream(conn.getInputStream());
        } else {
            String err = readStream(conn.getErrorStream());
            Log.ficheroLog("GET " + urlStr + " -> " + code + " : " + err);
            throw new RuntimeException("Error en GET: " + code + " - " + err);
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
        var uri = new java.net.URI(urlStr);
        var url = uri.toURL();
        var conex = (HttpURLConnection) url.openConnection();

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
        if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_CREATED) {
            return readStream(conex.getInputStream());
        } else {
            String err = readStream(conex.getErrorStream());
            Log.ficheroLog("POST " + urlStr + " -> " + code + " : " + err);
            throw new RuntimeException("Error en POST: " + code + " - " + err);
        }
    }

    /**
     * Ejecuta una solicitud HTTP PUT a la URL indicada con un cuerpo JSON opcional.
     *
     * @param urlStr URL a la que se realiza la solicitud PUT.
     * @param body   JSONObject con los datos a enviar; puede ser null.
     * @return Contenido de la respuesta como String.
     * @throws Exception Si ocurre un error en la conexión o si el código HTTP no es 200.
     */
    private String ejecutarPut(String urlStr, JSONObject body) throws Exception {
        var uri = new java.net.URI(urlStr);
        var url = uri.toURL();
        var conex = (HttpURLConnection) url.openConnection();

        conex.setRequestMethod("PUT");
        conex.setRequestProperty("Content-Type", "application/json");
        conex.setDoOutput(true);

        if (body != null) {
            try (var os = conex.getOutputStream()) {
                byte[] input = body.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        int code = conex.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            return readStream(conex.getInputStream());
        } else {
            String err = readStream(conex.getErrorStream());
            Log.ficheroLog("PUT " + urlStr + " -> " + code + " : " + err);
            throw new RuntimeException("Error en PUT: " + code + " - " + err);
        }
    }

    /**
     * Lee un InputStream y devuelve su contenido como String.
     *
     * @param is InputStream a leer.
     * @return Contenido del stream como String.
     * @throws IOException Si ocurre un error de lectura.
     */
    private String readStream(InputStream is) throws IOException {
        if (is == null) return "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = in.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    /**
     * Codifica un string para su uso en URLs.
     *
     * @param s Texto a codificar.
     * @return Texto codificado en UTF-8 apto para URL.
     */
    private String encode(String s) {
        try { return java.net.URLEncoder.encode(s, "UTF-8"); }
        catch(Exception e) { return s; }
    }

}
