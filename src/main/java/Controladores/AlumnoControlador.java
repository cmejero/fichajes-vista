package Controladores;

import java.io.IOException;

import com.google.gson.Gson;

import Dtos.AlumnoConMatriculacionDto;
import Log.Log;
import Servicios.AlumnoServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con alumnos.
 * Maneja peticiones POST para crear nuevos alumnos junto con su matriculación.
 */
@WebServlet("/alumno")
public class AlumnoControlador extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private AlumnoServicio servicio = new AlumnoServicio();

	@Override
	/**
	 * Maneja las peticiones POST hacia /alumno. Actualmente solo gestiona la acción
	 * "guardar".
	 *
	 * @param request  Solicitud HTTP con los datos enviados desde la vista.
	 * @param response Respuesta HTTP enviada al cliente.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String accion = request.getParameter("accion");
		Log.ficheroLog("Petición POST recibida en /alumno con acción: " + accion);

		if ("guardar".equals(accion)) {
			guardarAlumno(request, response);
		} else {
			Log.ficheroLog("Acción POST no reconocida en /alumno: " + accion);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"success\": false, \"mensaje\": \"Acción no válida\"}");
		}
	}

	/**
	 * Guarda un nuevo alumno y su matriculación.
	 *
	 * @param request  Contiene los parámetros enviados desde el formulario.
	 * @param response Respuesta con JSON indicando éxito o error.
	 * @throws IOException Si ocurre un error de entrada/salida.
	 */
	private void guardarAlumno(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("application/json; charset=UTF-8");
		Log.ficheroLog("Ejecutando guardarAlumno() desde /alumno");

		try {

			String nombre = request.getParameter("nombre");
			String apellidos = request.getParameter("apellidos");
			Long cursoId = Long.parseLong(request.getParameter("curso"));
			Long grupoId = Long.parseLong(request.getParameter("grupo"));
			String anioEscolar = request.getParameter("anioEscolar");
			String uidLlave = request.getParameter("uidLlave");

			Log.ficheroLog(
					"Datos recibidos -> Nombre: " + nombre + ", Apellidos: " + apellidos + ", Curso ID: " + cursoId
							+ ", Grupo ID: " + grupoId + ", Año escolar: " + anioEscolar + ", UID llave: " + uidLlave);

			AlumnoConMatriculacionDto alumno = new AlumnoConMatriculacionDto();
			alumno.setNombreAlumno(nombre);
			alumno.setApellidoAlumno(apellidos);
			alumno.setCursoId(cursoId);
			alumno.setGrupoId(grupoId);
			alumno.setAnioEscolar(anioEscolar);
			alumno.setUidLlave(uidLlave);

			String jsonAlumno = new Gson().toJson(alumno);
			Log.ficheroLog("JSON recibido en guardarAlumno(): " + jsonAlumno);

			servicio.guardarAlumno(alumno);
			Log.ficheroLog("Alumno guardado correctamente en base de datos");

			response.getWriter().write("{\"success\": true, \"mensaje\": \"Alumno guardado correctamente\"}");

		} catch (Exception e) {
			Log.ficheroLog("Error en guardarAlumno(): " + e.getMessage());
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"mensaje\": \"Error al guardar alumno\"}");
		}
	}
	
	  @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {

	        response.setContentType("application/json; charset=UTF-8");

	        try {
	            String json = servicio.obtenerTodosAlumnos();
	            response.getWriter().write(json);

	        } catch (Exception e) {
	            e.printStackTrace();
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            response.getWriter().write("{\"error\":\"No se pudieron obtener los alumnos\"}");
	        }
	    }
}
