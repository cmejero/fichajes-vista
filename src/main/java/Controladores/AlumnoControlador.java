package Controladores;

import java.io.IOException;

import com.google.gson.Gson;

import Dtos.AlumnoConMatriculacionDto;
import Dtos.AlumnoDto;
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

	    response.setContentType("application/json; charset=UTF-8");

	    try {
	        String accion = request.getParameter("accion");
	        Log.ficheroLog("Petición POST recibida en /alumno con acción: " + accion);

	        if ("guardar".equals(accion)) {
	            guardarAlumno(request, response);

	        } else if ("modificar".equals(accion)) {
	            modificarAlumno(request, response);

	        } else {
	            Log.ficheroLog("Acción POST no reconocida en /alumno: " + accion);
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().write("{\"success\": false, \"mensaje\": \"Acción no válida\"}");
	        }

	    } catch (Exception e) {
	        Log.ficheroLog("Error al procesar petición POST en /alumno: " + e.getMessage());
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"success\": false, \"mensaje\": \"Error interno del servidor\"}");
	        e.printStackTrace(); // opcional, útil para desarrollo
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
	
	
	/**
	 * Modifica los datos de un alumno existente.
	 * <p>
	 * Recoge los parámetros enviados desde el formulario y actualiza el alumno correspondiente
	 * en la base de datos. Valida que se haya proporcionado un ID de alumno válido.
	 *
	 * @param request  Contiene los parámetros enviados desde el formulario (idAlumno, nombre, apellidos).
	 * @param response Respuesta con JSON indicando éxito, error o alumno no encontrado.
	 * @throws IOException Si ocurre un error de entrada/salida al enviar la respuesta.
	 */
	private void modificarAlumno(HttpServletRequest request, HttpServletResponse response)
	        throws IOException {

	    try {
	        String idAlumnoStr = request.getParameter("idAlumno");

	        if (idAlumnoStr == null || idAlumnoStr.isEmpty()) {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().write("{\"success\": false, \"mensaje\": \"Falta ID del alumno\"}");
	            return;
	        }

	        Long idAlumno = Long.parseLong(idAlumnoStr);

	        // Recoger datos del formulario
	        String nombre = request.getParameter("nombre");
	        String apellidos = request.getParameter("apellidos");

	        AlumnoDto dto = new AlumnoDto();
	        dto.setNombreAlumno(nombre);
	        dto.setApellidoAlumno(apellidos);

	        boolean modificado = servicio.modificarAlumno(idAlumno, dto);

	        if (modificado) {
	            Log.ficheroLog("✅ Alumno modificado correctamente ID: " + idAlumno);
	            response.getWriter().write("{\"success\": true, \"mensaje\": \"Alumno modificado correctamente\"}");
	        } else {
	            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            response.getWriter().write("{\"success\": false, \"mensaje\": \"Alumno no encontrado\"}");
	        }

	    } catch (Exception e) {
	        Log.ficheroLog("❌ Error al modificar alumno: " + e.getMessage());
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"success\": false, \"mensaje\": \"Error al modificar alumno\"}");
	    }
	}

	 
	  @Override
	  /**
		 * Maneja las peticiones GET hacia /alumno. 
		 *
		 * @param request  Solicitud HTTP con los datos enviados desde la vista.
		 * @param response Respuesta HTTP enviada al cliente.
		 */
	  protected void doGet(HttpServletRequest request, HttpServletResponse response)
	          throws ServletException, IOException {

	      response.setContentType("application/json; charset=UTF-8");

	      String idParam = request.getParameter("id");
	      if (idParam != null) {
	          try {
	              Long idAlumno = Long.parseLong(idParam);
	              AlumnoConMatriculacionDto alumno = servicio.obtenerAlumnoPorId(idAlumno);

	              if (alumno != null) {
	                  response.getWriter().write(new Gson().toJson(alumno));
	              } else {
	                  response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	                  response.getWriter().write("{\"error\":\"Alumno no encontrado\"}");
	              }

	          } catch (NumberFormatException e) {
	              response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	              response.getWriter().write("{\"error\":\"ID inválido\"}");
	          }
	      } else {
	          // Si no se pasa ID, devolver todos los alumnos
	          response.getWriter().write(servicio.obtenerTodosAlumnos());
	      }
	  }

	  
	  @Override
	  /**
	   * Maneja las peticiones DELETE hacia /alumno.
	   * Elimina un alumno existente a partir del ID proporcionado
	   *
	   * @param request  Solicitud HTTP que contiene el parámetro "id" del alumno a eliminar.
	   * @param response Respuesta HTTP con JSON indicando el resultado de la operación.
	   * @throws ServletException Si ocurre un error en el servlet.
	   * @throws IOException      Si ocurre un error de entrada/salida al enviar la respuesta.
	   */
	  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
	          throws ServletException, IOException {

	      response.setContentType("application/json; charset=UTF-8");

	      // Leer ID del alumno de la query string, por ejemplo: /alumno?id=123
	      String idParam = request.getParameter("id");
	      if (idParam != null) {
	          try {
	              Long idAlumno = Long.parseLong(idParam);
	              boolean eliminado = servicio.eliminarAlumno(idAlumno);

	              if (eliminado) {
	                  Log.ficheroLog("✅ Alumno eliminado correctamente con ID: " + idAlumno);
	                  response.getWriter().write("{\"success\": true, \"mensaje\": \"Alumno eliminado correctamente\"}");
	              } else {
	                  Log.ficheroLog("⚠️ Alumno no encontrado con ID: " + idAlumno);
	                  response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	                  response.getWriter().write("{\"success\": false, \"mensaje\": \"Alumno no encontrado\"}");
	              }

	          } catch (NumberFormatException e) {
	              response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	              response.getWriter().write("{\"success\": false, \"mensaje\": \"ID inválido\"}");
	          }
	      } else {
	          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	          response.getWriter().write("{\"success\": false, \"mensaje\": \"Falta parámetro ID\"}");
	      }
	  }


}
