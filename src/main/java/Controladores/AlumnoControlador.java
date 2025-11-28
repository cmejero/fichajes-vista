package Controladores;

import java.io.IOException;

import com.google.gson.Gson;

import Dtos.AlumnoConMatriculacionDto;
import Log.Log;
import Servicios.AlumnoServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/alumno")
public class AlumnoControlador extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private AlumnoServicio servicio = new AlumnoServicio();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if ("guardar".equals(accion)) {
            guardarAlumno(request, response);
        }
    }

    private void guardarAlumno(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        try {
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            Long cursoId = Long.parseLong(request.getParameter("curso"));
            Long grupoId = Long.parseLong(request.getParameter("grupo"));
            String anioEscolar = request.getParameter("anioEscolar");
            String uidLlave = request.getParameter("uidLlave");

            AlumnoConMatriculacionDto alumno = new AlumnoConMatriculacionDto();
            alumno.setNombreAlumno(nombre);
            alumno.setApellidoAlumno(apellidos);
            alumno.setCursoId(cursoId);
            alumno.setGrupoId(grupoId);
            alumno.setAnioEscolar(anioEscolar);
            alumno.setUidLlave(uidLlave);

            // 🔹 Ver JSON recibido
            String jsonAlumno = new Gson().toJson(alumno);
            System.out.println("📩 JSON recibido en el servlet: " + jsonAlumno);
            Log.ficheroLog("JSON recibido: " + jsonAlumno);

            servicio.guardarAlumno(alumno);

            response.getWriter().write("{\"success\": true, \"mensaje\": \"Alumno guardado correctamente\"}");

        } catch (Exception e) {
            e.printStackTrace();
            Log.ficheroLog("Error al guardar alumno: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"mensaje\": \"Error al guardar alumno\"}");
        }
    }
}
