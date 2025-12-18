package Servicios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.google.gson.Gson;

import Dtos.CursoDto;
import Log.Log;

/**
 * CursoServicio Servicio del proyecto web encargado de comunicarse con la API
 * REST para obtener información de cursos.
 */
public class CursoServicio {


	public void guardarCurso(CursoDto dto) {
		try {
			JSONObject json = new JSONObject();
			json.put("nombreCurso", dto.getNombreCurso());
			String resp = ejecutarPost( "http://localhost:9527/api/guardarCurso", json);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Modifica un curso mediante la API REST externa.
	 *
	 * @param idCurso ID del curso a modificar.
	 * @param dto     Objeto con los nuevos datos del curso.
	 * @return true si la modificación fue exitosa, false en caso contrario.
	 */
	public boolean modificarCurso(Long idCurso, CursoDto dto) {
	    try {
	        Gson gson = new Gson();
	        String json = gson.toJson(dto);

	        String urlApi = "http://localhost:9527/api/modificarCurso/" + idCurso;
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

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()), "UTF-8"));

		StringBuilder salida = new StringBuilder();
		String linea;
		while ((linea = br.readLine()) != null) {
			salida.append(linea);
		}

		conn.disconnect();
		return salida.toString();
	}

	
	/**
	 * Elimina un curso mediante la API REST externa.
	 *
	 * @param idCurso ID del curso a eliminar.
	 * @return true si la eliminación fue exitosa, false en caso contrario.
	 */
	public boolean eliminarCurso(Long idCurso) {
		try {
			java.net.URI uri = new java.net.URI("http://localhost:9527/api/eliminarCurso/" + idCurso);
			java.net.URL url = uri.toURL();

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("DELETE");
			con.setRequestProperty("Accept", "application/json");

			int code = con.getResponseCode();
			return code == HttpURLConnection.HTTP_OK;

		} catch (Exception e) {
			System.out.println("❌ ERROR en CursoServicio.eliminarCurso(): " + e.getMessage());
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
