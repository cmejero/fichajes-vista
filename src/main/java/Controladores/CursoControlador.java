package Controladores;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;

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
    
    
    @Override
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        try (BufferedReader reader = request.getReader()) {
            // Leer JSON del cuerpo de la petición
            CursoDto dto = new Gson().fromJson(reader, CursoDto.class);

            // Log de recepción
            Log.ficheroLog("JSON recibido para guardar curso: " + new Gson().toJson(dto));

            // Guardar curso usando tu servicio
            servicio.guardarCurso(dto);

            // Respuesta al cliente
            response.getWriter().write("{\"success\": true, \"mensaje\": \"Curso guardado correctamente\"}");
            Log.ficheroLog("✅ Curso guardado correctamente: " + dto.getNombreCurso());

        } catch (Exception e) {
            e.printStackTrace();
            Log.ficheroLog("❌ Error al guardar curso: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"mensaje\": \"Error al guardar curso\"}");
        }
    }


  
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
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

        // Leer ID del curso de la query string: /curso?id=123
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
