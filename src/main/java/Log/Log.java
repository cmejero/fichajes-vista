package Log;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Utilidades.Utilidades;



/**
 * Clase que se encarga del Log
 */
public class Log {
	 private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	    private static final String RUTA_BASE_LOGS = "C:\\Users\\Carlo\\OneDrive\\Escritorio\\FICHEROS\\fichajes-vista-Log\\";

	    /**
	     * Escribe un mensaje en un archivo de log con un timestamp.
	     * 
	     * @param mensaje El mensaje a registrar en el log.
	     */
	    public static void ficheroLog(String mensaje) {
	        try {
	            String timestamp = LocalDateTime.now().format(formatter);
	            String logEntry = "[" + timestamp + "] " + mensaje;

	            String nombreCarpeta = Utilidades.nombreCarpetaFecha();
	            String nombreArchivo = Utilidades.nombreArchivoLog();

	            String rutaCarpeta = RUTA_BASE_LOGS + nombreCarpeta;
	            File carpeta = new File(rutaCarpeta);
	            if (!carpeta.exists()) {
	                carpeta.mkdirs();
	            }

	            String rutaArchivo = rutaCarpeta + File.separator + nombreArchivo;
	            FileWriter fw = new FileWriter(rutaArchivo, true);
	            fw.write(logEntry + "\n");
	            fw.close();

	        } catch (Exception e) {
	            // System.out.println("Se ha producido un error al escribir en el log: " + e.getMessage());
	        }
	    }
}
