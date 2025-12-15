package Controladores;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;

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
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        try (BufferedReader reader = request.getReader()) {
            // Leer JSON del cuerpo de la petición
            GrupoDto dto = new Gson().fromJson(reader, GrupoDto.class);

            // Log de recepción
            Log.ficheroLog("JSON recibido para guardar grupo: " + new Gson().toJson(dto));

            // Guardar grupo usando tu servicio
            servicio.guardarGrupo(dto);

            // Respuesta al cliente
            response.getWriter().write("{\"success\": true, \"mensaje\": \"Grupo guardado correctamente\"}");
            Log.ficheroLog("✅ Grupo guardado correctamente: " + dto.getNombreGrupo());

        } catch (Exception e) {
            e.printStackTrace();
            Log.ficheroLog("❌ Error al guardar grupo: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"mensaje\": \"Error al guardar grupo\"}");
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

        try {
            String pathInfo = request.getPathInfo(); 

            if (pathInfo == null || pathInfo.equals("/")) {
                // Sin ID → obtenemos todos los grupos
                String json = servicio.obtenerTodosGruposDesdeAPI();
                response.getWriter().write(json);
                return;
            }

            // Con ID → obtenemos grupos de un curso concreto
            Long idCurso = Long.parseLong(pathInfo.substring(1));
            String json = servicio.obtenerGruposPorCursoDesdeAPI(idCurso);
            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ID inválido\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"No se pudieron cargar los grupos\"}");
        }
    }

    
    
    @Override
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
