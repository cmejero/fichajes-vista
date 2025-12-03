package Utilidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que se encarga de os metodos que se usaran varias veces en la
 * aplicacion
 */
public class Utilidades {

	/**
	 * Obtiene el nombre del archivo de log con la fecha actual en formato
	 * "ddMMyyyy".
	 * 
	 * @return Nombre del archivo de log.
	 */
	public static final String nombreArchivoLog() {
		try {
			LocalDate fechaActual = LocalDate.now();
			String fechaStr = fechaActual.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
			return "log-" + fechaStr + ".txt";
		} catch (Exception e) {
			// System.out.println("Se ha producido un error [1003], intentelo más tarde");
			return "log-error.txt";
		}
	}

	/**
	 * Obtiene el nombre de la carpeta basada en la fecha actual en formato
	 * "ddMMyyyy".
	 * 
	 * @return Nombre de la carpeta con la fecha actual.
	 */
	public static final String nombreCarpetaFecha() {
		try {
			LocalDate fechaActual = LocalDate.now();
			return fechaActual.format(DateTimeFormatter.ofPattern("ddMMyyyy"));
		} catch (Exception e) {
			// System.out.println("Se ha producido un error [1004], intentelo más tarde");
			return "errorFecha";
		}
	}

	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
	private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	/**
	 * Formatea una fecha (LocalDate) según el patrón definido en FORMATO_FECHA.
	 *
	 * @param fecha Objeto LocalDate a formatear.
	 * @return Fecha formateada como String o "-" si la fecha es null.
	 */
	public static String formatearFecha(LocalDate fecha) {
		return (fecha != null) ? fecha.format(FORMATO_FECHA) : "-";
	}

	/**
	 * Formatea una hora (LocalDateTime) según el patrón definido en FORMATO_HORA.
	 *
	 * @param fechaHora Objeto LocalDateTime a formatear.
	 * @return Hora formateada como String o "-" si el objeto es null.
	 */
	public static String formatearHora(LocalDateTime fechaHora) {
		return (fechaHora != null) ? fechaHora.format(FORMATO_HORA) : "-";
	}

	/**
	 * Formatea una fecha y hora (LocalDateTime) según el patrón definido en
	 * FORMATO_FECHA_HORA.
	 *
	 * @param fechaHora Objeto LocalDateTime a formatear.
	 * @return Fecha y hora formateadas como String o "-" si el objeto es null.
	 */
	public static String formatearFechaHora(LocalDateTime fechaHora) {
		return (fechaHora != null) ? fechaHora.format(FORMATO_FECHA_HORA) : "-";
	}

}
