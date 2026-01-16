package Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;


/**
 * Servicio que consulta la API del lector NFC y devuelve información sobre la UID detectada.
 */
public class LectorServicio {

	
	  private static final String URL_API_EVENTO = "http://localhost:9527/api/lector/evento";

	    /**
	     * Consulta la API del lector de NFC y devuelve la información de la UID.
	     * 
	     * @return Resultado del evento: hayUid, registrado y uid
	     * @throws Exception si hay un error en la conexión o lectura
	     */
	  public ResultadoLector consultarEvento() throws Exception {
		    URL url = new URL(URL_API_EVENTO);
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod("GET");

		    if (conn.getResponseCode() != 200) {
		        throw new RuntimeException("Error al consultar la API");
		    }

		    BufferedReader in = new BufferedReader(
		            new InputStreamReader(conn.getInputStream()));
		    StringBuilder response = new StringBuilder();
		    String linea;
		    while ((linea = in.readLine()) != null) {
		        response.append(linea);
		    }
		    in.close();

		    JSONObject json = new JSONObject(response.toString());

		    boolean hayUid = json.getBoolean("hayUid");
		    boolean registrado = json.optBoolean("registrado", false); 
		    String uid = json.optString("uid", null);
		    String alumno = json.optString("alumno", null);
		    String curso = json.optString("curso", null);
		    String grupo = json.optString("grupo", null);

		    return new ResultadoLector(hayUid, registrado, uid, alumno, curso, grupo);

		}


	  /**
	     * Clase interna que representa el resultado de la consulta al lector NFC.
	     */
	    public static class ResultadoLector {
	        private boolean hayUid;
	        private boolean registrado;
	        private String uid;
	        private String alumno;
	        private String curso;
	        private String grupo;

	        public ResultadoLector(boolean hayUid, boolean registrado, String uid,
	                               String alumno, String curso, String grupo) {
	            this.hayUid = hayUid;
	            this.registrado = registrado;
	            this.uid = uid;
	            this.alumno = alumno;
	            this.curso = curso;
	            this.grupo = grupo;
	        }

	        public boolean isHayUid() { return hayUid; }
	        public boolean isRegistrado() { return registrado; }
	        public String getUid() { return uid; }
	        public String getAlumno() { return alumno; }
	        public String getCurso() { return curso; }
	        public String getGrupo() { return grupo; }
	    }


	    

}
