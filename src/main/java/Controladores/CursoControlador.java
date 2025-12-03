package Controladores;

import java.io.IOException;

import Servicios.CursoServicio;
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
     * Atiende solicitudes GET para obtener el listado de cursos.
     * Devuelve el JSON directamente consumido desde la API externa.
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
}
