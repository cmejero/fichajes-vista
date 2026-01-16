package Controladores;

import java.io.IOException;

import org.json.JSONObject;

import Servicios.LectorServicio;
import Servicios.LectorServicio.ResultadoLector;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@WebServlet("/leerUid")
public class LectorControlador extends HttpServlet {
    private static final long serialVersionUID = 1L; 
    private final LectorServicio lectorServicio = new LectorServicio();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            ResultadoLector resultado = lectorServicio.consultarEvento();

            // Construir JSON usando JSONObject
            JSONObject json = new JSONObject();
            json.put("hayUid", resultado.isHayUid());
            json.put("registrado", resultado.isRegistrado());
            json.put("uid", resultado.getUid() != null ? resultado.getUid() : "");
            json.put("alumno", resultado.getAlumno() != null ? resultado.getAlumno() : "");
            json.put("curso", resultado.getCurso() != null ? resultado.getCurso() : "");
            json.put("grupo", resultado.getGrupo() != null ? resultado.getGrupo() : "");

            
            response.getWriter().write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject jsonError = new JSONObject();
            jsonError.put("error", e.getMessage() != null ? e.getMessage() : "Error desconocido");
            response.getWriter().write(jsonError.toString());
        }
    }
}




