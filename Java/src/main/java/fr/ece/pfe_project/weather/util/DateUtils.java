package fr.ece.pfe_project.weather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * Utility Class for date formatting 
 */
public class DateUtils {

	public static final String DATE_FORMAT_PATTERN = "EEE, dd MMM yyyy hh:mm a z";
	public static final String DATE_FORMAT_ASTRO = "h:mm a";
	public static final String DATE_FORMAT_FORECAST = "dd MMM yyyy";

	public static Date parseDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN,
				Locale.US);
		Date result = null;
		try {
			result = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	public static Date parseForecastDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_FORECAST,
				Locale.US);
		Date result = null;
		try {
			result = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	public static Date parseCondDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_ASTRO,
				Locale.US);
		Date result = null;
		try {
			result = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

}
