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

@WebServlet("/asistencia")
public class AsistenciaControlador extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private AsistenciaServicio asistenciaServicio;

	@Override
	public void init() throws ServletException {
		this.asistenciaServicio = new AsistenciaServicio();
		Log.ficheroLog("✅ [INIT] AsistenciaControlador inicializado correctamente");
	}

	// -------------------- POST --------------------
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    try {
	        String accion = request.getParameter("accion");
	        Log.ficheroLog("➡️ [POST] Usuario accede a /asistencia, acción=" + accion);

	        if ("entrada".equalsIgnoreCase(accion)) {
	            Long matriculacionId = Long.parseLong(request.getParameter("matriculacionId"));
	            Log.ficheroLog("⏰ [ENTRADA] Usuario intenta fichar entrada para matriculacionId=" + matriculacionId);

	            AsistenciaDto asistencia = asistenciaServicio.ficharEntrada(matriculacionId);
	            String json = convertirAsistenciaAJson(asistencia);
	            response.getWriter().write(json);

	            Log.ficheroLog("✅ [ENTRADA OK] Usuario fichó entrada correctamente para matriculacionId=" + matriculacionId);

	        } else if ("salida".equalsIgnoreCase(accion)) {
	            Long matriculacionId = Long.parseLong(request.getParameter("matriculacionId"));
	            Log.ficheroLog("⏳ [SALIDA] Usuario intenta fichar salida para matriculacionId=" + matriculacionId);

	            AsistenciaDto asistencia = asistenciaServicio.ficharSalida(matriculacionId);
	            String json = convertirAsistenciaAJson(asistencia);
	            response.getWriter().write(json);

	            Log.ficheroLog("✅ [SALIDA OK] Usuario fichó salida correctamente para matriculacionId=" + matriculacionId);

	        } else if ("modificar".equalsIgnoreCase(accion)) {
	            try {
	                String idStr = request.getParameter("idAsistencia");
	                if (idStr == null || idStr.isEmpty() || "null".equalsIgnoreCase(idStr)) {
	                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                    response.getWriter().write("{\"success\": false, \"error\": \"ID de asistencia vacío\"}");
	                    Log.ficheroLog("⚠️ [MODIFICAR] Usuario intentó modificar asistencia sin ID");
	                    return;
	                }
	                Long asistenciaId = Long.parseLong(idStr);

	                String horaEntradaStr = request.getParameter("horaEntrada");
	                String horaSalidaStr = request.getParameter("horaSalida");
	                String estado = request.getParameter("estado");
	                String justificarModificacion = request.getParameter("justificarModificacion");

	                Log.ficheroLog("✏️ [MODIFICAR] Usuario intenta modificar asistencia id=" + asistenciaId +
	                                " con parametros -> horaEntrada=" + horaEntradaStr + 
	                                ", horaSalida=" + horaSalidaStr + ", estado=" + estado + 
	                                ", justificarModificacion=" + justificarModificacion);

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
	                    response.getWriter().write(respuesta.toString());
	                    Log.ficheroLog("✅ [MODIFICAR OK] Usuario modificó asistencia id=" + asistenciaId);
	                } else {
	                    respuesta.put("success", false);
	                    respuesta.put("error", "No se pudo modificar la asistencia");
	                    response.getWriter().write(respuesta.toString());
	                    Log.ficheroLog("⚠️ [MODIFICAR] No se encontró asistencia con ID " + asistenciaId);
	                }

	            } catch (NumberFormatException e) {
	                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                response.getWriter().write("{\"success\": false, \"error\": \"ID o formato de hora inválido\"}");
	                Log.ficheroLog("❌ [POST ERROR] ID o formato de hora inválido: " + e.getMessage());

	            } catch (Exception e) {
	                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	                response.getWriter().write("{\"success\": false, \"error\": \"Error interno al modificar asistencia\"}");
	                Log.ficheroLog("🔥 [POST ERROR] Excepción al modificar asistencia: " + e.getMessage());
	            }

	        } else {
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
	        response.getWriter().write("{\"success\": false, \"error\": \"Error en el servidor: " + e.getMessage() + "\"}");
	    }
	}



	// -------------------- GET --------------------
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    try {
	        String accion = request.getParameter("accion");
	        Log.ficheroLog("➡️ [GET] Usuario accede a /asistencia, acción=" + accion);

	     

	        switch (accion) {
	        
	            case "porAlumnoYEstado": {
	                Long alumnoId = Long.parseLong(request.getParameter("alumnoId"));
	                String estado = request.getParameter("estado");
	                String anioEscolar = request.getParameter("anioEscolar"); // ej: "2025/2026"

	                Log.ficheroLog("📋 [GET] Usuario filtra asistencias por alumnoId=" + alumnoId
	                               + ", estado=" + estado + ", año escolar=" + anioEscolar);

	                List<AsistenciaDto> lista = asistenciaServicio.obtenerPorAlumnoYEstado(alumnoId, estado, anioEscolar);
	                response.getWriter().write(convertirAsistenciasAJson(lista));
	                Log.ficheroLog("✅ [GET OK] Enviadas asistencias filtradas por alumno, estado y año escolar");
	                break;
	            }
	            case "porCursoGrupo": {
	                String curso = request.getParameter("curso");
	                String grupo = request.getParameter("grupo");
	                LocalDate hoy = LocalDate.now();
	                Log.ficheroLog("🏫 [GET] Usuario filtra asistencias por curso=" + curso + ", grupo=" + grupo + " en fecha=" + hoy);
	                List<AsistenciaDto> lista = asistenciaServicio.obtenerAsistenciaPorCursoYGrupoEnFecha(curso, grupo, hoy);
	                response.getWriter().write(convertirAsistenciasAJson(lista));
	                Log.ficheroLog("✅ [GET OK] Enviadas asistencias del grupo=" + grupo + ", curso=" + curso);
	                break;
	            }
	            case "porCursoGrupoYFecha": {
	                String curso = request.getParameter("curso");
	                String grupo = request.getParameter("grupo");
	                String fechaStr = request.getParameter("fecha");
	                LocalDate fecha = (fechaStr != null && !fechaStr.isEmpty()) ? LocalDate.parse(fechaStr) : null;

	                if(fecha == null) {
	                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                    response.getWriter().write("{\"error\":\"Debe proporcionar una fecha válida\"}");
	                    Log.ficheroLog("⚠️ [GET] Usuario intentó filtrar asistencias sin fecha");
	                    return;
	                }

	                Log.ficheroLog("🏫 [GET] Usuario filtra asistencias por curso=" + curso + ", grupo=" + grupo + ", fecha=" + fecha);
	                List<AsistenciaDto> lista = asistenciaServicio.obtenerAsistenciaPorCursoYGrupoYFecha(curso, grupo, fecha);
	                response.getWriter().write(convertirAsistenciasAJson(lista));
	                Log.ficheroLog("✅ [GET OK] Enviadas asistencias del grupo=" + grupo + ", curso=" + curso + " para fecha=" + fecha);
	                break;
	            }
	            case "porAlumnoYRango": {
	                Long alumnoId = Long.parseLong(request.getParameter("alumnoId"));
	                LocalDate desde = LocalDate.parse(request.getParameter("desde"));
	                LocalDate hasta = LocalDate.parse(request.getParameter("hasta"));
	                Log.ficheroLog("📆 [GET] Usuario filtra asistencias por ID=" + alumnoId + " desde " + desde + " hasta " + hasta);
	                response.getWriter().write(convertirAsistenciasAJson(asistenciaServicio.obtenerPorAlumnoYRango(alumnoId, desde, hasta)));
	                break;
	            }
	            case "conteoEstados": {
	                Long alumnoId = Long.parseLong(request.getParameter("alumnoId"));
	                LocalDate desde = LocalDate.parse(request.getParameter("desde"));
	                LocalDate hasta = LocalDate.parse(request.getParameter("hasta"));

	                Log.ficheroLog("📊 [GET] Usuario solicita conteo de estados para alumnoId=" + alumnoId
	                               + " desde " + desde + " hasta " + hasta);

	            
	                    var conteo = asistenciaServicio.obtenerConteoEstados(alumnoId, desde, hasta);

	                    JSONObject json = new JSONObject();
	                    json.put("PRESENTE", conteo.getOrDefault("PRESENTE", 0));
	                    json.put("COMPLETA", conteo.getOrDefault("COMPLETA", 0));
	                    json.put("SIN SALIDA", conteo.getOrDefault("SIN SALIDA", 0));
	                    json.put("FALTA", conteo.getOrDefault("FALTA", 0));

	                    response.getWriter().write(json.toString());
	                    Log.ficheroLog("✅ [GET OK] Enviado conteo de estados para alumnoId=" + alumnoId);
	               
	                break;
	            }

	        
	            default: {
	                Log.ficheroLog("⚠️ [GET] Acción no válida recibida: " + accion);
	                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                response.getWriter().write("{\"error\":\"Acción GET no válida\"}");
	            }
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

		// Convertimos el mapa a JSON básico sin usar Gson
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

	// 🔁 Convertir una sola asistencia a JSON
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
