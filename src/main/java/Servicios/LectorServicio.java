package Servicios;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

import Dtos.EventoLectorDto;

/**
 * Servicio que consulta la API del lector NFC y devuelve información sobre la UID detectada.
 */
public class LectorServicio {

    private static final String URL_API_EVENTO = "http://localhost:9527/api/lector/evento";

    /**
     * Consulta la API del lector de NFC y devuelve la información del DTO.
     *
     * @return EventoLectorDto con la información del evento.
     * @throws Exception si hay un error en la conexión o lectura
     */
    public EventoLectorDto consultarEvento() throws Exception {
        URL url = new URL(URL_API_EVENTO);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Error al consultar la API");
        }

        ObjectMapper mapper = new ObjectMapper();

        try (InputStream is = conn.getInputStream()) {
            return mapper.readValue(is, EventoLectorDto.class);
        }
    }
}
