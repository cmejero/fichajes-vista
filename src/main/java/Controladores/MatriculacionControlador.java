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

	/**
	 * 
	 * Maneja las solicitudes POST para el servlet de matrículas.
	 * 
	 * Según la acción indicada en el parámetro 'accion', realiza la operación correspondiente.
	 * 
	 * @param request  Objeto HttpServletRequest que contiene los parámetros de la solicitud.
	 * 
	 * @param response Objeto HttpServletResponse para enviar la respuesta JSON.
	 * 
	 * @throws ServletException Si ocurre un error de servlet.
	 * 
	 * @throws IOException      Si ocurre un error de entrada/salida.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String accion = request.getParameter("accion");

		if ("guardar".equals(accion)) {
			guardarMatriculacion(request, response);
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
}
