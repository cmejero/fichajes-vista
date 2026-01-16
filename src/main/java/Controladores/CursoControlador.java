package Controladores;

import java.io.IOException;

import Dtos.CursoDto;
import Log.Log;
import Servicios.CursoServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CursoControlador
 * Controlador encargado de atender las solicitudes relacionadas
 * con los cursos desde el proyecto web. Consume la API REST externa
 * y devuelve la información en formato JSON para las vistas JSP.
 */
@WebServlet("/curso")
public class CursoControlador extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private CursoServicio servicio = new CursoServicio();
    
    
    /**
     * Maneja peticiones POST hacia /curso. Delegará según la acción: "guardar" o "modificar".
     *
     * @param request  Solicitud HTTP con parámetros de acción y datos del curso.
     * @param response Respuesta HTTP con JSON indicando resultado.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        try {
            String accion = request.getParameter("accion");
            Log.ficheroLog("Petición POST recibida en /curso con acción: " + accion);

            if ("guardar".equals(accion)) {
                guardarCurso(request, response);

            } else if ("modificar".equals(accion)) {
                modificarCurso(request, response);

            } else {
                Log.ficheroLog("Acción POST no reconocida en /curso: " + accion);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"mensaje\": \"Acción no válida\"}");
            }

        } catch (Exception e) {
            Log.ficheroLog("Error al procesar petición POST en /curso: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"mensaje\": \"Error interno del servidor\"}");
            e.printStackTrace(); 
        }
    }


    /**
     * Guarda un nuevo curso.
     *
     * @param request  Solicitud HTTP con parámetros del curso.
     * @param response Respuesta HTTP con JSON indicando resultado.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    private void guardarCurso(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            String nombreCurso = request.getParameter("nombreCurso");

            if (nombreCurso == null || nombreCurso.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"mensaje\": \"El nombre del curso es obligatorio\"}");
                return;
            }

            CursoDto dto = new CursoDto();
            dto.setNombreCurso(nombreCurso);

            servicio.guardarCurso(dto);

            Log.ficheroLog("✅ Curso guardado correctamente: " + nombreCurso);
            response.getWriter().write("{\"success\": true, \"mensaje\": \"Curso guardado correctamente\"}");

        } catch (Exception e) {
            Log.ficheroLog("❌ Error al guardar curso: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"mensaje\": \"Error al guardar curso\"}");
        }
    }

    /**
     * Modifica un curso existente.
     *
     * @param request  Solicitud HTTP con ID y datos del curso.
     * @param response Respuesta HTTP con JSON indicando resultado.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    private void modificarCurso(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            String idCursoStr = request.getParameter("idCurso");

            if (idCursoStr == null || idCursoStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"mensaje\": \"Falta ID del curso\"}");
                return;
            }

            Long idCurso = Long.parseLong(idCursoStr);

            String nombreCurso = request.getParameter("nombreCurso");

            CursoDto dto = new CursoDto();
            dto.setNombreCurso(nombreCurso);

            boolean modificado = servicio.modificarCurso(idCurso, dto);

            if (modificado) {
                Log.ficheroLog("✅ Curso modificado correctamente ID: " + idCurso);
                response.getWriter().write("{\"success\": true, \"mensaje\": \"Curso modificado correctamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"success\": false, \"mensaje\": \"Curso no encontrado\"}");
            }

        } catch (Exception e) {
            Log.ficheroLog("❌ Error al modificar curso: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"mensaje\": \"Error al modificar curso\"}");
        }
    }

    /**
     * Atiende solicitudes GET para obtener el listado de cursos.
     *
     * @param request  Solicitud HTTP.
     * @param response Respuesta HTTP con JSON de cursos.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json; charset=UTF-8");

        try {
            String json = servicio.obtenerCursosDesdeAPI();
            response.getWriter().write(json);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"No se pudieron cargar los cursos\"}");
        }
    }

    /**
     * Atiende solicitudes DELETE para eliminar un curso por ID.
     *
     * @param request  Solicitud HTTP con parámetro "id".
     * @param response Respuesta HTTP con JSON indicando resultado.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                Long idCurso = Long.parseLong(idParam);
                boolean eliminado = servicio.eliminarCurso(idCurso);

                if (eliminado) {
                    Log.ficheroLog("✅ Curso eliminado correctamente con ID: " + idCurso);
                    response.getWriter().write("{\"success\": true, \"mensaje\": \"Curso eliminado correctamente\"}");
                } else {
                    Log.ficheroLog("⚠️ Curso no encontrado con ID: " + idCurso);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"success\": false, \"mensaje\": \"Curso no encontrado\"}");
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
