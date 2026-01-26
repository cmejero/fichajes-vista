package Controladores;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import Dtos.EventoLectorDto;
import Log.Log;
import Servicios.LectorServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet encargado de exponer el endpoint /leerUid.
 * Devuelve en formato JSON la información del último UID detectado.
 */
@WebServlet("/leerUid")
public class LectorControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final LectorServicio lectorServicio = new LectorServicio();

    /**
     * Maneja solicitudes GET a /leerUid y devuelve el evento del lector NFC.
     * 
     * @param request  Petición HTTP recibida.
     * @param response Respuesta HTTP donde se escribe el JSON del DTO.
     * @throws ServletException En caso de error en el servlet.
     * @throws IOException      En caso de error de E/S al escribir la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Log.ficheroLog("➡️ Solicitud GET /leerUid (obtener evento lector)");

        try {
            // Ahora recibimos el DTO directamente
            EventoLectorDto resultado = lectorServicio.consultarEvento();

            // Devolvemos el JSON del DTO
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(resultado);

            Log.ficheroLog("✅ Evento lector obtenido correctamente: " + json);

            response.getWriter().write(json);

        } catch (Exception e) {
            Log.ficheroLog("❌ Error al obtener evento lector: " + e.getMessage());

            // En caso de error devolvemos JSON con error
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

}
