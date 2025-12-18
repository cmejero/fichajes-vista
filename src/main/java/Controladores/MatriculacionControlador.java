package Controladores;

import java.io.IOException;
import com.google.gson.Gson;
import Dtos.MatriculacionDto;
import Log.Log;
import Servicios.MatriculacionServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con
 * matriculacion. Maneja peticiones POST para crear nuevas matriculaciones.
 */
@WebServlet("/matriculacion")
public class MatriculacionControlador extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private MatriculacionServicio servicio = new MatriculacionServicio();

	@Override
	/**
	 * Maneja las peticiones POST hacia /matriculacion.
	 * Según el parámetro 'accion', realiza la operación correspondiente: guardar o modificar.
	 *
	 * @param request  Solicitud HTTP con los datos enviados desde la vista.
	 * @param response Respuesta HTTP enviada al cliente.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    String accion = request.getParameter("accion");
	    Log.ficheroLog("Petición POST recibida en /matriculacion con acción: " + accion);

	    if ("guardar".equals(accion)) {
	        guardarMatriculacion(request, response);
	    } else if ("modificar".equals(accion)) {
	        modificarMatriculacion(request, response);
	    } else {
	        Log.ficheroLog("Acción POST no reconocida en /matriculacion: " + accion);
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.getWriter().write("{\"success\": false, \"mensaje\": \"Acción no válida\"}");
	    }
	}

	/**
	 * 
	 * Guarda una nueva matrícula utilizando los parámetros recibidos desde el
	 * formulario.
	 * 
	 * Construye un DTO, lo registra mediante el servicio correspondiente y devuelve
	 * un JSON con el resultado.
	 * 
	 * @param request  Objeto HttpServletRequest con los parámetros de la matrícula.
	 * 
	 * @param response Objeto HttpServletResponse para enviar la respuesta JSON.
	 * 
	 * @throws IOException Si ocurre un error al escribir la respuesta.
	 */
	private void guardarMatriculacion(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json; charset=UTF-8");

		try {

			Long alumnoId = Long.parseLong(request.getParameter("idAlumnoSeleccionado"));
			Long cursoId = Long.parseLong(request.getParameter("curso"));
			Long grupoId = Long.parseLong(request.getParameter("grupo"));
			String anioEscolar = request.getParameter("anioEscolar");
			String uidLlave = request.getParameter("uidLlave");


			MatriculacionDto dto = new MatriculacionDto();
			dto.setAlumnoId(alumnoId);
			dto.setCursoId(cursoId);
			dto.setGrupoId(grupoId);
			dto.setAnioEscolar(anioEscolar);
			dto.setUidLlave(uidLlave);

			String jsonDto = new Gson().toJson(dto);
			Log.ficheroLog("JSON recibido en el servlet: " + jsonDto);

			servicio.guardarMatriculacion(dto);

			response.getWriter().write("{\"success\": true, \"mensaje\": \"Matrícula guardada correctamente\"}");

		} catch (Exception e) {
			e.printStackTrace();
			Log.ficheroLog("Error al guardar matrícula: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"success\": false, \"mensaje\": \"Error al guardar matrícula\"}");
		}
	}
	
	
	/**
	 * Modifica los datos de una matrícula existente.
	 *
	 * @param request  Objeto HttpServletRequest con los parámetros de la matrícula.
	 * @param response Objeto HttpServletResponse para enviar la respuesta JSON.
	 * @throws IOException Si ocurre un error al escribir la respuesta.
	 */
	private void modificarMatriculacion(HttpServletRequest request, HttpServletResponse response)
	        throws IOException {

	    response.setContentType("application/json; charset=UTF-8");

	    try {
	        String idMatriculacionStr = request.getParameter("idMatriculacion");
	        if (idMatriculacionStr == null || idMatriculacionStr.isEmpty()) {
	            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            response.getWriter().write("{\"success\": false, \"mensaje\": \"Falta ID de la matrícula\"}");
	            return;
	        }

	        Long idMatriculacion = Long.parseLong(idMatriculacionStr);

	        Long cursoId = Long.parseLong(request.getParameter("curso"));
	        Long grupoId = Long.parseLong(request.getParameter("grupo"));
	        String anioEscolar = request.getParameter("anioEscolar");
	        String uidLlave = request.getParameter("uidLlave");

	        MatriculacionDto dto = new MatriculacionDto();
	        dto.setCursoId(cursoId);
	        dto.setGrupoId(grupoId);
	        dto.setAnioEscolar(anioEscolar);
	        dto.setUidLlave(uidLlave);

	        boolean modificado = servicio.modificarMatriculacion(idMatriculacion, dto);

	        if (modificado) {
	            Log.ficheroLog("✅ Matrícula modificada correctamente ID: " + idMatriculacion);
	            response.getWriter().write("{\"success\": true, \"mensaje\": \"Matrícula modificada correctamente\"}");
	        } else {
	            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            response.getWriter().write("{\"success\": false, \"mensaje\": \"Matrícula no encontrada\"}");
	        }

	    } catch (Exception e) {
	        Log.ficheroLog("❌ Error al modificar matrícula: " + e.getMessage());
	        e.printStackTrace();
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"success\": false, \"mensaje\": \"Error al modificar matrícula\"}");
	    }
	}
	
	
	/**
     * Maneja la acción GET para obtener todas las matriculaciones de un alumno.
     * Espera un parámetro "idAlumno" en la URL.
     *
     * @param request  Objeto HttpServletRequest con el parámetro "idAlumno".
     * @param response Objeto HttpServletResponse para devolver JSON.
     * @throws ServletException Si ocurre un error de servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        String idAlumnoParam = request.getParameter("idAlumno");
        if (idAlumnoParam != null) {
            try {
                Long idAlumno = Long.parseLong(idAlumnoParam);
                String json = servicio.obtenerMatriculacionesPorAlumno(idAlumno);
                response.getWriter().write(json);
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"ID inválido\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Falta el parámetro idAlumno\"}");
        }
    }

    
    @Override
    /**
     * Atiende solicitudes DELETE para eliminar una matrícula por ID.
     *
     * @param request  Solicitud HTTP con parámetro "id".
     * @param response Respuesta HTTP con JSON indicando resultado.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        // Leer el ID desde el query string
        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                Long idMatriculacion = Long.parseLong(idParam);
                boolean eliminado = servicio.eliminarMatriculacion(idMatriculacion);

                if (eliminado) {
                    response.getWriter().write("{\"success\": true, \"mensaje\": \"Matrícula eliminada correctamente\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"success\": false, \"mensaje\": \"Matrícula no encontrada\"}");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"mensaje\": \"ID inválido\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"mensaje\": \"Falta el parámetro id\"}");
        }
    }

    
}
