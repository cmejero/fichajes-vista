package Servicios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.google.gson.Gson;

import Dtos.GrupoDto;
import Log.Log;

/**
 * GrupoServicio
 * Servicio del proyecto web encargado de comunicarse con la API REST
 * para obtener los grupos asociados a un curso.
 */
public class GrupoServicio {

	/**
	 * Guarda un nuevo grupo mediante la API REST externa.
	 *
	 * @param dto Objeto con los datos del grupo a guardar.
	 */
	public void guardarGrupo(GrupoDto dto) {
	    try {
	        JSONObject json = new JSONObject();
	        json.put("nombreGrupo", dto.getNombreGrupo());
	        json.put("cursoId", dto.getCursoId());
	        String resp = ejecutarPost("http://localhost:9527/api/guardarGrupo", json);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Modifica un grupo existente mediante la API REST externa.
	 *
	 * @param idGrupo ID del grupo a modificar.
	 * @param dto     Objeto con los nuevos datos del grupo.
	 * @return true si la modificación fue exitosa, false en caso contrario.
	 */
	public boolean modificarGrupo(Long idGrupo, GrupoDto dto) {
	    try {
	        Gson gson = new Gson();
	        String json = gson.toJson(dto);

	        String urlApi = "http://localhost:9527/api/modificarGrupo/" + idGrupo;
	        URL url = new URL(urlApi);

	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	        conn.setDoOutput(true);

	        try (OutputStream os = conn.getOutputStream()) {
	            os.write(json.getBytes("UTF-8"));
	        }

	        return conn.getResponseCode() == HttpURLConnection.HTTP_OK;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/**
	 * Obtiene todos los grupos desde la API REST externa.
	 *
	 * @return JSON con todos los grupos.
	 * @throws Exception Si ocurre un error al conectar con la API.
	 */
	public String obtenerTodosGruposDesdeAPI() throws Exception {
	    URL url = new URL("http://localhost:9527/api/grupos");
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");

	    if (conn.getResponseCode() != 200) {
	        throw new RuntimeException("Error HTTP: " + conn.getResponseCode());
	    }

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
    
    /**
     * Elimina un grupo mediante la API REST externa.
     *
     * @param idGrupo ID del grupo a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarGrupo(Long idGrupo) {
        try {
            java.net.URI uri = new java.net.URI("http://localhost:9527/api/eliminarGrupo/" + idGrupo);
            java.net.URL url = uri.toURL();

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Accept", "application/json");

            int code = con.getResponseCode();
            return code == HttpURLConnection.HTTP_OK;

        } catch (Exception e) {
            System.out.println("❌ ERROR en GrupoServicio.eliminarGrupo(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
	 * Ejecuta una solicitud HTTP POST a la URL indicada con un cuerpo JSON
	 * opcional.
	 *
	 * @param urlStr URL a la que se realiza la solicitud POST.
	 * @param body   JSONObject con los datos a enviar; puede ser null.
	 * @return Contenido de la respuesta como String.
	 * @throws Exception Si ocurre un error en la conexión o si el código HTTP no es
	 *                   200 o 201.
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
	 * Lee un InputStream y devuelve su contenido como String.
	 *
	 * @param is InputStream a leer.
	 * @return Contenido del stream como String.
	 * @throws Exception Si ocurre un error de lectura.
	 */
	private String readStream(InputStream is) throws Exception {
		if (is == null)
			return "";
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
