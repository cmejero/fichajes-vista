package Controladores;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;

import Dtos.AsistenciaDto;
import Log.Log;
import Servicios.AsistenciaServicio;
import Utilidades.Utilidades;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con asistencias.
 * Maneja peticiones POST y GET.
 */
@WebServlet("/asistencia")
public class AsistenciaControlador extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private AsistenciaServicio asistenciaServicio;

	@Override
	public void init() throws ServletException {
		this.asistenciaServicio = new AsistenciaServicio();
		Log.ficheroLog("✅ [INIT] AsistenciaControlador inicializado correctamente");
	}

	/**
	 * Maneja las solicitudes POST para la entidad Asistencia.
	 * <p>
	 * Dependiendo del parámetro "accion" recibido en la solicitud, este método:
	 * 
	 * Envía la respuesta en formato JSON e informa de errores si la acción no es
	 * válida o ocurre una excepción.
	 *
	 * @param request  Objeto HttpServletRequest que contiene los parámetros
	 *                 enviados desde el formulario.
	 * @param response Objeto HttpServletResponse para enviar la respuesta en
	 *                 formato JSON.
	 * @throws ServletException Si ocurre un error de servlet.
	 * @throws IOException      Si ocurre un error de entrada/salida al escribir la
	 *                          respuesta.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try {
			String accion = request.getParameter("accion");
			Log.ficheroLog("➡️ [POST] Usuario accede a /asistencia, acción=" + accion);

			switch (accion.toLowerCase()) {
			case "entrada":
				ficharEntrada(request, response);
				break;
			case "salida":
				ficharSalida(request, response);
				break;
			case "modificar":
				modificarAsistencia(request, response);
				break;
			default:
				Log.ficheroLog("⚠️ [POST] Acción no válida recibida: " + accion);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"success\": false, \"error\": \"Acción no válida\"}");
			}

		} catch (RuntimeException e) {
			Log.ficheroLog("❌ [POST ERROR] Error de ejecución: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");

		} catch (Exception e) {
			Log.ficheroLog("🔥 [POST ERROR] Error en servidor: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter()
					.write("{\"success\": false, \"error\": \"Error en el servidor: " + e.getMessage() + "\"}");
		}
	}

	/**
	 * 
	 * Escribe una respuesta JSON en el HttpServletResponse y registra el log.
	 *
	 * @param response HttpServletResponse donde se enviará la respuesta.
	 * @param json     Contenido JSON a enviar.
	 * @param logMsg   Mensaje a registrar en el log.
	 * @throws IOException Si ocurre un error al escribir en la respuesta.
	 */
	private void escribirRespuesta(HttpServletResponse response, String json, String logMsg) throws IOException {
		response.getWriter().write(json);
		Log.ficheroLog(logMsg);
	}

	/**
	 * 
	 * Ficha la entrada de un alumno según su matriculación.
	 *
	 * @param request  Contiene el parámetro matriculacionId.
	 * @param response Respuesta con JSON de la asistencia registrada.
	 * @throws IOException Si ocurre un error al escribir la respuesta.
	 */
	private void ficharEntrada(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
		Long matriculacionId = Long.parseLong(request.getParameter("matriculacionId"));
		Log.ficheroLog("⏰ [ENTRADA] Usuario intenta fichar entrada para matriculacionId=" + matriculacionId);

		AsistenciaDto asistencia = asistenciaServicio.ficharEntrada(matriculacionId);
		String json = convertirAsistenciaAJson(asistencia);

		escribirRespuesta(response, json,
				"✅ [ENTRADA OK] Usuario fichó entrada correctamente para matriculacionId=" + matriculacionId);
	}

	/**
	 * 
	 * Ficha la salida de un alumno según su matriculación.
	 *
	 * @param request  Contiene el parámetro matriculacionId.
	 * @param response Respuesta con JSON de la asistencia registrada.
	 * @throws IOException Si ocurre un error al escribir la respuesta.
	 */
	private void ficharSalida(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Long matriculacionId = Long.parseLong(request.getParameter("matriculacionId"));
			Log.ficheroLog("⏳ [SALIDA] Usuario intenta fichar salida para matriculacionId=" + matriculacionId);

			AsistenciaDto asistencia = asistenciaServicio.ficharSalida(matriculacionId);
			String json = convertirAsistenciaAJson(asistencia);

			escribirRespuesta(response, json,
					"✅ [SALIDA OK] Usuario fichó salida correctamente para matriculacionId=" + matriculacionId);

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			escribirRespuesta(response, "{\"success\": false, \"error\": \"Error al fichar salida\"}",
					"🔥 [SALIDA ERROR] " + e.getMessage());
		}
	}

	/**
	 * 
	 * Modifica una asistencia existente según los parámetros recibidos.
	 *
	 * @param request  Contiene idAsistencia, horaEntrada, horaSalida, estado y
	 *                 justificarModificacion.
	 * @param response Respuesta JSON indicando éxito o fallo de la modificación.
	 * @throws IOException Si ocurre un error al escribir la respuesta.
	 */
	/**
	 * Modifica una asistencia existente según los parámetros recibidos.
	 *
	 * @param request  Contiene idAsistencia, horaEntrada, horaSalida, estado y
	 *                 justificarModificacion.
	 * @param response Respuesta JSON indicando éxito o fallo de la modificación.
	 * @throws IOException Si ocurre un error al escribir la respuesta.
	 */
	private void modificarAsistencia(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String idStr = request.getParameter("idAsistencia");
			if (idStr == null || idStr.isEmpty() || "null".equalsIgnoreCase(idStr)) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				escribirRespuesta(response, "{\"success\": false, \"error\": \"ID de asistencia vacío\"}",
						"⚠️ [MODIFICAR] Usuario intentó modificar asistencia sin ID");
				return;
			}

			Long asistenciaId = Long.parseLong(idStr);
			String horaEntradaStr = request.getParameter("horaEntrada");
			String horaSalidaStr = request.getParameter("horaSalida");
			String estado = request.getParameter("estado");
			String justificarModificacion = request.getParameter("justificarModificacion");

			Log.ficheroLog("✏️ [MODIFICAR] Usuario intenta modificar asistencia id=" + asistenciaId
					+ " con parametros -> horaEntrada=" + horaEntradaStr + ", horaSalida=" + horaSalidaStr + ", estado="
					+ estado + ", justificarModificacion=" + justificarModificacion);

			AsistenciaDto dto = new AsistenciaDto();
			dto.setEstado(estado);
			dto.setJustificarModificacion(justificarModificacion);

			if (horaEntradaStr != null && !horaEntradaStr.isEmpty() && !"null".equalsIgnoreCase(horaEntradaStr)) {
				String[] partes = horaEntradaStr.split(":");
				dto.setHoraEntrada(LocalDateTime.of(LocalDate.now(),
						LocalTime.of(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]))));
			}

			if (horaSalidaStr != null && !horaSalidaStr.isEmpty() && !"null".equalsIgnoreCase(horaSalidaStr)) {
				String[] partes = horaSalidaStr.split(":");
				dto.setHoraSalida(LocalDateTime.of(LocalDate.now(),
						LocalTime.of(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]))));
			}

			AsistenciaDto asistenciaModificada = asistenciaServicio.modificarAsistencia(asistenciaId, dto);

			JSONObject respuesta = new JSONObject();
			if (asistenciaModificada != null) {
				String asistenciaJson = convertirAsistenciaAJson(asistenciaModificada);
				respuesta.put("success", true);
				respuesta.put("asistencia", new JSONObject(asistenciaJson));
				escribirRespuesta(response, respuesta.toString(),
						"✅ [MODIFICAR OK] Usuario modificó asistencia id=" + asistenciaId);
			} else {
				respuesta.put("success", false);
				respuesta.put("error", "No se pudo modificar la asistencia");
				escribirRespuesta(response, respuesta.toString(),
						"⚠️ [MODIFICAR] No se encontró asistencia con ID " + asistenciaId);
			}

		} catch (NumberFormatException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			escribirRespuesta(response, "{\"success\": false, \"error\": \"ID o formato de hora inválido\"}",
					"❌ [POST ERROR] ID o formato de hora inválido: " + e.getMessage());
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			escribirRespuesta(response, "{\"success\": false, \"error\": \"Error interno al modificar asistencia\"}",
					"🔥 [POST ERROR] Excepción al modificar asistencia: " + e.getMessage());
		}
	}

	// -------------------- GET --------------------
	/**
	 * Maneja las solicitudes GET para la entidad Asistencia.
	 * Dependiendo del parámetro "accion", redirige a métodos privados específicos
	 * que procesan la solicitud y devuelven la respuesta en formato JSON.
	 *
	 * @param request  Objeto HttpServletRequest que contiene los parámetros
	 *                 enviados desde el cliente.
	 * @param response Objeto HttpServletResponse para enviar la respuesta en
	 *                 formato JSON.
	 * @throws ServletException Si ocurre un error de servlet.
	 * @throws IOException      Si ocurre un error de entrada/salida al escribir la
	 *                          respuesta.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try {
			String accion = request.getParameter("accion");
			Log.ficheroLog("➡️ [GET] Usuario accede a /asistencia, acción=" + accion);

			switch (accion) {
			case "todas":
			    obtenerTodasAsistencias(request, response);
			    break;
			case "porAlumnoYEstado":
				obtenerAsistenciasPorAlumnoYEstado(request, response);
				break;
			case "porCursoGrupo":
				obtenerAsistenciasPorCursoYGrupo(request, response);
				break;
			case "porCursoGrupoYFecha":
				obtenerAsistenciasPorCursoYGrupoYFecha(request, response);
				break;
			case "porAlumnoYRango":
				obtenerAsistenciasPorAlumnoYRango(request, response);
				break;
			case "conteoEstados":
				obtenerConteoEstadosAlumno(request, response);
				break;
			default:
				Log.ficheroLog("⚠️ [GET] Acción no válida recibida: " + accion);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"error\":\"Acción GET no válida\"}");
			}

		} catch (DateTimeParseException e) {
			Log.ficheroLog("❌ [GET ERROR] Usuario introdujo fecha inválida: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\":\"Formato de fecha inválido\"}");
		} catch (Exception e) {
			Log.ficheroLog("🔥 [GET ERROR] Error inesperado al procesar acción GET: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\":\"Error en servidor: " + e.getMessage() + "\"}");
		}
	}
	
	/**
	 * Obtiene todas las asistencias desde la API REST externa.
	 */
	private void obtenerTodasAsistencias(HttpServletRequest request, HttpServletResponse response)
	        throws IOException {

	    try {
	        Log.ficheroLog("📋 [GET] Usuario solicita todas las asistencias");

	        String json = asistenciaServicio.obtenerTodasAsistencias();

	        response.getWriter().write(json);

	        Log.ficheroLog("✅ [GET OK] Enviadas todas las asistencias");

	    } catch (Exception e) {
	        Log.ficheroLog("🔥 [GET ERROR] Error al obtener todas las asistencias: " + e.getMessage());
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"error\":\"Error al obtener asistencias: " + e.getMessage() + "\"}");
	    }
	}


	/**
	 * Obtiene asistencias de un alumno filtradas por estado y año escolar.
	 */
	private void obtenerAsistenciasPorAlumnoYEstado(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception { // <-- Agregado Exception
		Long alumnoId = Long.parseLong(request.getParameter("alumnoId"));
		String estado = request.getParameter("estado");
		String anioEscolar = request.getParameter("anioEscolar");

		Log.ficheroLog("📋 [GET] Usuario filtra asistencias por alumnoId=" + alumnoId + ", estado=" + estado
				+ ", año escolar=" + anioEscolar);

		List<AsistenciaDto> lista = asistenciaServicio.obtenerPorAlumnoYEstado(alumnoId, estado, anioEscolar);
		response.getWriter().write(convertirAsistenciasAJson(lista));
		Log.ficheroLog("✅ [GET OK] Enviadas asistencias filtradas por alumno, estado y año escolar");
	}

	/**
	 * Obtiene asistencias por curso y grupo para la fecha actual.
	 */
	/**
	 * Obtiene asistencias por curso y grupo para la fecha actual.
	 */
	private void obtenerAsistenciasPorCursoYGrupo(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			String curso = request.getParameter("curso");
			String grupo = request.getParameter("grupo");
			LocalDate hoy = LocalDate.now();

			Log.ficheroLog(
					"🏫 [GET] Usuario filtra asistencias por curso=" + curso + ", grupo=" + grupo + " en fecha=" + hoy);

			List<AsistenciaDto> lista = asistenciaServicio.obtenerAsistenciaPorCursoYGrupoEnFecha(curso, grupo, hoy);
			response.getWriter().write(convertirAsistenciasAJson(lista));

			Log.ficheroLog("✅ [GET OK] Enviadas asistencias del grupo=" + grupo + ", curso=" + curso);
		} catch (Exception e) {
			Log.ficheroLog("🔥 [GET ERROR] Error al obtener asistencias por curso y grupo: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\":\"Error al obtener asistencias: " + e.getMessage() + "\"}");
		}
	}

	/**
	 * Obtiene asistencias por curso, grupo y fecha específica.
	 */
	/**
	 * Obtiene asistencias por curso, grupo y fecha específica.
	 */
	private void obtenerAsistenciasPorCursoYGrupoYFecha(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			String curso = request.getParameter("curso");
			String grupo = request.getParameter("grupo");
			String fechaStr = request.getParameter("fecha");
			LocalDate fecha = (fechaStr != null && !fechaStr.isEmpty()) ? LocalDate.parse(fechaStr) : null;

			if (fecha == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("{\"error\":\"Debe proporcionar una fecha válida\"}");
				Log.ficheroLog("⚠️ [GET] Usuario intentó filtrar asistencias sin fecha");
				return;
			}

			Log.ficheroLog(
					"🏫 [GET] Usuario filtra asistencias por curso=" + curso + ", grupo=" + grupo + ", fecha=" + fecha);

			List<AsistenciaDto> lista = asistenciaServicio.obtenerAsistenciaPorCursoYGrupoYFecha(curso, grupo, fecha);
			response.getWriter().write(convertirAsistenciasAJson(lista));

			Log.ficheroLog(
					"✅ [GET OK] Enviadas asistencias del grupo=" + grupo + ", curso=" + curso + " para fecha=" + fecha);
		} catch (Exception e) {
			Log.ficheroLog("🔥 [GET ERROR] Error al obtener asistencias por curso, grupo y fecha: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\":\"Error al obtener asistencias: " + e.getMessage() + "\"}");
		}
	}

	/**
	 * Obtiene asistencias de un alumno dentro de un rango de fechas.
	 */
	/**
	 * Obtiene asistencias de un alumno dentro de un rango de fechas.
	 */
	private void obtenerAsistenciasPorAlumnoYRango(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			Long alumnoId = Long.parseLong(request.getParameter("alumnoId"));
			LocalDate desde = LocalDate.parse(request.getParameter("desde"));
			LocalDate hasta = LocalDate.parse(request.getParameter("hasta"));

			Log.ficheroLog(
					"📆 [GET] Usuario filtra asistencias por ID=" + alumnoId + " desde " + desde + " hasta " + hasta);

			response.getWriter().write(
					convertirAsistenciasAJson(asistenciaServicio.obtenerPorAlumnoYRango(alumnoId, desde, hasta)));
		} catch (Exception e) {
			Log.ficheroLog("🔥 [GET ERROR] Error al obtener asistencias por alumno y rango: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\":\"Error al obtener asistencias: " + e.getMessage() + "\"}");
		}
	}

	/**
	 * Obtiene el conteo de estados de asistencias para un alumno dentro de un rango
	 * de fechas.
	 */
	private void obtenerConteoEstadosAlumno(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		try {
			Long alumnoId = Long.parseLong(request.getParameter("alumnoId"));
			LocalDate desde = LocalDate.parse(request.getParameter("desde"));
			LocalDate hasta = LocalDate.parse(request.getParameter("hasta"));

			Log.ficheroLog("📊 [GET] Usuario solicita conteo de estados para alumnoId=" + alumnoId + " desde " + desde
					+ " hasta " + hasta);

			var conteo = asistenciaServicio.obtenerConteoEstados(alumnoId, desde, hasta);
			JSONObject json = new JSONObject();
			json.put("PRESENTE", conteo.getOrDefault("PRESENTE", 0));
			json.put("COMPLETA", conteo.getOrDefault("COMPLETA", 0));
			json.put("SIN SALIDA", conteo.getOrDefault("SIN SALIDA", 0));
			json.put("FALTA", conteo.getOrDefault("FALTA", 0));

			response.getWriter().write(json.toString());
			Log.ficheroLog("✅ [GET OK] Enviado conteo de estados para alumnoId=" + alumnoId);
		} catch (Exception e) {
			Log.ficheroLog("🔥 [GET ERROR] Error al obtener conteo de estados: " + e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\":\"Error al obtener conteo de estados: " + e.getMessage() + "\"}");
		}
	}

	/**
	 * 
	 * Convierte una lista de objetos AsistenciaDto a una representación JSON en
	 * String. Cada asistencia se transforma en un mapa de propiedades y luego se
	 * construye un JSON manualmente.
	 *
	 * @param lista Lista de objetos AsistenciaDto a convertir.
	 * @return Cadena JSON representando la lista de asistencias.
	 */
	public String convertirAsistenciasAJson(List<AsistenciaDto> lista) {
		List<Map<String, Object>> listaSerializable = lista.stream().map(a -> {
			Map<String, Object> m = new LinkedHashMap<>();
			m.put("idAsistencia", a.getIdAsistencia());
			m.put("nombreCompletoAlumno", a.getNombreCompletoAlumno());
			m.put("nombreCurso", a.getNombreCurso());
			m.put("nombreGrupo", a.getNombreGrupo());
			m.put("fecha", Utilidades.formatearFecha(a.getFecha()));
			m.put("horaEntrada", Utilidades.formatearHora(a.getHoraEntrada()));
			m.put("horaSalida", Utilidades.formatearHora(a.getHoraSalida()));
			m.put("fechaModificacion", Utilidades.formatearFechaHora(a.getFechaModificacion()));
			m.put("justificarModificacion", a.getJustificarModificacion());
			m.put("estado", a.getEstado());
			return m;
		}).collect(Collectors.toList());

		StringBuilder json = new StringBuilder("[");
		for (int i = 0; i < listaSerializable.size(); i++) {
			Map<String, Object> map = listaSerializable.get(i);
			json.append("{");
			int j = 0;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				json.append("\"").append(entry.getKey()).append("\":");
				Object val = entry.getValue();
				if (val == null)
					json.append("null");
				else
					json.append("\"").append(val.toString()).append("\"");
				if (++j < map.size())
					json.append(",");
			}
			json.append("}");
			if (i < listaSerializable.size() - 1)
				json.append(",");
		}
		json.append("]");
		return json.toString();
	}

	/**
	 * 
	 * Convierte un único objeto AsistenciaDto a JSON en String.
	 * 
	 * Construye manualmente la representación JSON de la asistencia.
	 * 
	 * @param a Objeto AsistenciaDto a convertir.
	 * 
	 * @return Cadena JSON representando la asistencia, o "{}" si el objeto es null.
	 */
	public String convertirAsistenciaAJson(AsistenciaDto a) {
		if (a == null)
			return "{}";
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("idAsistencia", a.getIdAsistencia());
		m.put("nombreAlumno", a.getNombreCompletoAlumno());
		m.put("nombreCurso", a.getNombreCurso());
		m.put("nombreGrupo", a.getNombreGrupo());
		m.put("fecha", Utilidades.formatearFecha(a.getFecha()));
		m.put("fechaModificacion", Utilidades.formatearFechaHora(a.getFechaModificacion()));
		m.put("horaEntrada", Utilidades.formatearHora(a.getHoraEntrada()));
		m.put("horaSalida", Utilidades.formatearHora(a.getHoraSalida()));
		m.put("justificarModificacion", a.getJustificarModificacion());
		m.put("estado", a.getEstado());

		StringBuilder json = new StringBuilder("{");
		int i = 0;
		for (Map.Entry<String, Object> entry : m.entrySet()) {
			json.append("\"").append(entry.getKey()).append("\":");
			Object val = entry.getValue();
			if (val == null)
				json.append("null");
			else
				json.append("\"").append(val.toString()).append("\"");
			if (++i < m.size())
				json.append(",");
		}
		json.append("}");
		return json.toString();
	}
}
