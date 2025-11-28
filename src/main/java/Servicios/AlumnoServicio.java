package Servicios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.json.JSONObject;

import Dtos.AlumnoConMatriculacionDto;
import Log.Log;

public class AlumnoServicio {

    private static final String BASE_API = "http://localhost:9527/api/guardarAlumno";

    public void guardarAlumno(AlumnoConMatriculacionDto alumno) {
        try {
            // Construir JSON
            JSONObject json = new JSONObject();
            json.put("nombreAlumno", alumno.getNombreAlumno());
            json.put("apellidoAlumno", alumno.getApellidoAlumno());
            json.put("cursoId", alumno.getCursoId());
            json.put("grupoId", alumno.getGrupoId());
            json.put("anioEscolar", alumno.getAnioEscolar());
            json.put("uidLlave", alumno.getUidLlave());

            // Llamada HTTP POST
            String resp = ejecutarPost(BASE_API, json);

            // Log del éxito
            System.out.println("✅ Alumno guardado correctamente: " + resp);

        } catch (Exception e) {
            System.out.println("❌ ERROR en AlumnoServicio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String ejecutarPost(String urlStr, JSONObject body) throws Exception {
        // Convertir primero a URI y luego a URL
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
