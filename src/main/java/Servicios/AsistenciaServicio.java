package Servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Dtos.AsistenciaDto;
import Log.Log;

public class AsistenciaServicio {

    private static final String BASE_API = "http://localhost:9527/api";

    // ---------------- POST ----------------
    public AsistenciaDto ficharEntrada(Long matriculacionId) throws Exception {
        String url = BASE_API + "/asistencia/entrada/" + matriculacionId;
        String resp = ejecutarPost(url, null);
        return parseAsistencia(resp);
    }

    public AsistenciaDto ficharSalida(Long matriculacionId) throws Exception {
        String url = BASE_API + "/asistencia/salida/" + matriculacionId;
        String resp = ejecutarPut(url, null);
        return parseAsistencia(resp);
    }

    
 // ---------------- POST para modificar asistencia ----------------
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
    public List<AsistenciaDto> obtenerAsistenciaPorCursoYGrupoEnFecha(String curso, String grupo, LocalDate fecha) throws Exception {
        String url = BASE_API + "/asistencia/" + encode(curso) + "/" + encode(grupo);
        String resp = ejecutarGet(url);
        return parseListaAsistencia(resp);
    }

    public List<AsistenciaDto> obtenerAsistenciaPorCursoYGrupoYFecha(String curso, String grupo, LocalDate fecha) throws Exception {
        // Construir URL usando query parameters según el endpoint real
        String url = BASE_API + "/asistencia/curso-grupo"
                   + "?curso=" + encode(curso)
                   + "&grupo=" + encode(grupo)
                   + "&fecha=" + fecha.toString();

        // <-- Aquí logueamos la URL completa y los parámetros enviados
        Log.ficheroLog("➡️ [SERVICIO] Llamando a URL: " + url);


   

        String resp = ejecutarGet(url);
        return parseListaAsistencia(resp);
    }



    public List<AsistenciaDto> obtenerPorAlumnoYEstado(Long alumnoId, String estado, String anioEscolar) throws Exception {

        String url = BASE_API + "/asistencia/alumno-estado"
            + "?alumnoId=" + alumnoId
            + "&estado=" + encode(estado)
            + "&anioEscolar=" + encode(anioEscolar);

        String resp = ejecutarGet(url);
        return parseListaAsistencia(resp);
    }




    public List<AsistenciaDto> obtenerPorAlumnoYRango(Long alumnoId, LocalDate desde, LocalDate hasta) throws Exception {
        String url = BASE_API + "/asistencia/rango/" + alumnoId + "?desde=" + desde + "&hasta=" + hasta;
        String resp = ejecutarGet(url);
        return parseListaAsistencia(resp);
    }


    public java.util.Map<String, Integer> obtenerConteoEstados(Long alumnoId, LocalDate desde, LocalDate hasta) throws Exception {
        // Llamamos al endpoint de la API
        String url = BASE_API + "/asistencia/conteoEstados/" + alumnoId
                     + "?desde=" + desde.toString()
                     + "&hasta=" + hasta.toString();

        String resp = ejecutarGet(url);

        // La API debe devolver un JSON con conteos, por ejemplo:
        // { "PRESENTE": 5, "COMPLETA": 2, "SIN SALIDA": 1, "FALTA": 0 }

        JSONObject obj = new JSONObject(resp);
        java.util.Map<String, Integer> conteo = new java.util.HashMap<>();
        conteo.put("PRESENTE", obj.optInt("PRESENTE", 0));
        conteo.put("COMPLETA", obj.optInt("COMPLETA", 0));
        conteo.put("SIN SALIDA", obj.optInt("SIN SALIDA", 0));
        conteo.put("FALTA", obj.optInt("FALTA", 0));

        return conteo;
    }


    // ---------------- Parsers ----------------
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

    private String readStream(InputStream is) throws IOException {
        if (is == null) return "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = in.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    private String encode(String s) {
        try { return java.net.URLEncoder.encode(s, "UTF-8"); }
        catch(Exception e) { return s; }
    }
}
