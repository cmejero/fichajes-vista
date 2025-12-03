package Controladores;

import java.io.IOException;

import Servicios.GrupoServicio;
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
            // Extraer el ID desde la URL: /grupos/curso/3
            String pathInfo = request.getPathInfo(); // "/3"
            if (pathInfo == null || pathInfo.equals("/")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"ID de curso no proporcionado\"}");
                return;
            }

            Long idCurso = Long.parseLong(pathInfo.substring(1)); // "3" → 3

            String json = servicio.obtenerGruposPorCursoDesdeAPI(idCurso);
            response.getWriter().write(json);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"No se pudieron cargar los grupos\"}");
        }
    }
}
