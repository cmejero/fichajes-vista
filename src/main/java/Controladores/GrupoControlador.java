package Controladores;

import java.io.IOException;

import Dtos.GrupoDto;
import Log.Log;
import Servicios.GrupoServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * GrupoControlador
 * Controlador encargado de gestionar las peticiones relacionadas
 * con los grupos dentro del proyecto web. Llama a la API REST externa
 * para obtener los grupos filtrados por curso y devuelve JSON para las JSP.
 */
@WebServlet("/grupo/*")
public class GrupoControlador extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private GrupoServicio servicio = new GrupoServicio();
    
    /**
     * Maneja peticiones POST hacia /grupo. Delegará según la acción: "guardar" o "modificar".
     *
     * @param request  Solicitud HTTP con parámetros de acción y datos del grupo.
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
            Log.ficheroLog("Petición POST recibida en /grupo con acción: " + accion);

            if ("guardar".equals(accion)) {
                guardarGrupo(request, response);

            } else if ("modificar".equals(accion)) {
                modificarGrupo(request, response);

            } else {
                Log.ficheroLog("Acción POST no reconocida en /grupo: " + accion);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"mensaje\": \"Acción no válida\"}");
            }

        } catch (Exception e) {
            Log.ficheroLog("Error al procesar petición POST en /grupo: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"mensaje\": \"Error interno del servidor\"}");
            e.printStackTrace(); // opcional, útil para depuración
        }
    }


    /**
     * Guarda un nuevo grupo.
     *
     * @param request  Solicitud HTTP con parámetros del grupo.
     * @param response Respuesta HTTP con JSON indicando resultado.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    private void guardarGrupo(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            String nombreGrupo = request.getParameter("nombreGrupo");
            String cursoIdStr = request.getParameter("idCurso");

            if (nombreGrupo == null || nombreGrupo.trim().isEmpty()
                    || cursoIdStr == null || cursoIdStr.isEmpty()) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(
                    "{\"success\": false, \"mensaje\": \"Nombre del grupo y curso obligatorios\"}"
                );
                return;
            }

            Long cursoId = Long.parseLong(cursoIdStr);

            GrupoDto dto = new GrupoDto();
            dto.setNombreGrupo(nombreGrupo);
            dto.setCursoId(cursoId);

            servicio.guardarGrupo(dto);

            Log.ficheroLog("✅ Grupo guardado correctamente: " + nombreGrupo);
            response.getWriter().write(
                "{\"success\": true, \"mensaje\": \"Grupo guardado correctamente\"}"
            );

        } catch (Exception e) {
            Log.ficheroLog("❌ Error al guardar grupo: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                "{\"success\": false, \"mensaje\": \"Error al guardar grupo\"}"
            );
        }
    }

    /**
     * Modifica un grupo existente.
     *
     * @param request  Solicitud HTTP con ID y datos del grupo.
     * @param response Respuesta HTTP con JSON indicando resultado.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    private void modificarGrupo(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        try {
            String idGrupoStr = request.getParameter("idGrupo");

            if (idGrupoStr == null || idGrupoStr.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(
                    "{\"success\": false, \"mensaje\": \"Falta ID del grupo\"}"
                );
                return;
            }

            Long idGrupo = Long.parseLong(idGrupoStr);

            String nombreGrupo = request.getParameter("nombreGrupo");
            String cursoIdStr = request.getParameter("idCurso");

            GrupoDto dto = new GrupoDto();
            dto.setNombreGrupo(nombreGrupo);

            if (cursoIdStr != null && !cursoIdStr.isEmpty()) {
                dto.setCursoId(Long.parseLong(cursoIdStr));
            }

            boolean modificado = servicio.modificarGrupo(idGrupo, dto);

            if (modificado) {
                Log.ficheroLog("✅ Grupo modificado correctamente ID: " + idGrupo);
                response.getWriter().write(
                    "{\"success\": true, \"mensaje\": \"Grupo modificado correctamente\"}"
                );
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(
                    "{\"success\": false, \"mensaje\": \"Grupo no encontrado\"}"
                );
            }

        } catch (Exception e) {
            Log.ficheroLog("❌ Error al modificar grupo: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                "{\"success\": false, \"mensaje\": \"Error al modificar grupo\"}"
            );
        }
    }


    /**
     * Maneja solicitudes GET dirigidas a /grupos/curso/{idCurso}
     * Obtiene los grupos desde la API REST usando el servicio web.
     *
     * @param request  petición HTTP
     * @param response respuesta HTTP (JSON)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json; charset=UTF-8");

        Log.ficheroLog("➡️ Solicitud para obtener grupos desde API");

        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                // Sin ID → obtenemos todos los grupos
                String json = servicio.obtenerTodosGruposDesdeAPI();

                Log.ficheroLog("✅ Grupos obtenidos correctamente (todos)");
                response.getWriter().write(json);
                return;
            }

            // Con ID → obtenemos grupos de un curso concreto
            Long idCurso = Long.parseLong(pathInfo.substring(1));
            String json = servicio.obtenerGruposPorCursoDesdeAPI(idCurso);

            Log.ficheroLog("✅ Grupos obtenidos correctamente para CursoID: " + idCurso);
            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            Log.ficheroLog("❌ Error: ID inválido en pathInfo: " + request.getPathInfo());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ID inválido\"}");
        } catch (Exception e) {
            Log.ficheroLog("❌ Error al cargar grupos desde API: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"No se pudieron cargar los grupos\"}");
        }
    }


    
    
    @Override
    /**
     * Atiende solicitudes DELETE para eliminar un grupo por ID.
     *
     * @param request  Solicitud HTTP con parámetro "id".
     * @param response Respuesta HTTP con JSON indicando resultado.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        // Leer ID del grupo de la query string: /grupo?id=123
        String idParam = request.getParameter("id");
        if (idParam != null) {
            try {
                Long idGrupo = Long.parseLong(idParam);
                boolean eliminado = servicio.eliminarGrupo(idGrupo);

                if (eliminado) {
                    Log.ficheroLog("✅ Grupo eliminado correctamente con ID: " + idGrupo);
                    response.getWriter().write("{\"success\": true, \"mensaje\": \"Grupo eliminado correctamente\"}");
                } else {
                    Log.ficheroLog("⚠️ Grupo no encontrado con ID: " + idGrupo);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"success\": false, \"mensaje\": \"Grupo no encontrado\"}");
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
