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

@WebServlet("/matriculacion")
public class MatriculacionControlador extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private MatriculacionServicio servicio = new MatriculacionServicio();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("guardar".equals(accion)) {
            guardarMatriculacion(request, response);
        }
    }

    private void guardarMatriculacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");

        try {
            // Obtener datos del formulario
            Long alumnoId = Long.parseLong(request.getParameter("idAlumnoSeleccionado"));
            Long cursoId = Long.parseLong(request.getParameter("curso"));
            Long grupoId = Long.parseLong(request.getParameter("grupo"));
            String anioEscolar = request.getParameter("anioEscolar");
            String uidLlave = request.getParameter("uidLlave");

            // Construir DTO
            MatriculacionDto dto = new MatriculacionDto();
            dto.setAlumnoId(alumnoId);
            dto.setCursoId(cursoId);
            dto.setGrupoId(grupoId);
            dto.setAnioEscolar(anioEscolar);
            dto.setUidLlave(uidLlave);

            // 🔹 Log JSON recibido
            String jsonDto = new Gson().toJson(dto);
            Log.ficheroLog("JSON recibido en el servlet: " + jsonDto);
            System.out.println("📩 JSON recibido: " + jsonDto);

            // Llamada al servicio
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
