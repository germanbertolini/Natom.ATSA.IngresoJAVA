package ar.com.atsa.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase Utilitaria que permtie hacer operaciones con fechas
 * 
 * @author NicolasMariano
 *
 */
public final class DateUtils {

	/**
	 * Constructor default privado. Al ser una clase Utilitaria no se debería
	 * poder instanciarla.
	 */
	private DateUtils() {

	}

	/**
	 * Crea un SimpleDateFormat con el patrón yyyy-MM-dd e instancia un nuevo
	 * Date a partir del String enviado por parámetro.
	 * 
	 * @return
	 */
	public static Date getDateFromStringYyyyMMdd(String dateStr) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(dateStr);
		} catch (Exception e) {
			return new Date();
		}
	}

	/**
	 * Crea un SimpleDateFormat con el patrón dd/MM/yyyy e instancia un nuevo
	 * Date a partir del String enviado por parámetro.
	 * 
	 * @return
	 */
	public static Date getDateFromString(String dateStr) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return sdf.parse(dateStr);
		} catch (Exception e) {
			return new Date();
		}
	}

	/**
	 * Devuelve un String con el formato indicado a partir de la fecha.
	 * 
	 * @param fecha
	 *            La fecha a formatear
	 * @param format
	 *            El formato a aplicarle a la fecha (default: dd/MM/yyyy)
	 * @return Fecha formateada en formato String
	 */
	public static String getFormattedString(final Date fecha,
			final String format) {
		if (fecha == null) {
			return null;
		}
		SimpleDateFormat sdf;
		if (format != null) {
			sdf = new SimpleDateFormat(format);
		} else {
			sdf = new SimpleDateFormat("dd/MM/yyyy");
		}
		return sdf.format(fecha);
	}
}
