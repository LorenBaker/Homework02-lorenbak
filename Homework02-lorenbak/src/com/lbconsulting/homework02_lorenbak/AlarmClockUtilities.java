package com.lbconsulting.homework02_lorenbak;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class AlarmClockUtilities {

	public static final String TAG = "AlarmClock";
	public static final Boolean L = false; // enable Logging

	/**
	 * Returns a formated String of the the given time as hours:minutes:seconds
	 * AM/PM
	 * 
	 * @param timeToFormatInMilliseconds
	 * @return
	 */
	public static String formatTime(long timeToFormatInMilliseconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("h:mm:ss a", Locale.US);
		formatter.setTimeZone(TimeZone.getDefault());
		return formatter.format(timeToFormatInMilliseconds);
	}

	/**
	 * Returns a formated String of the the given time as hours:minutes:seconds
	 * AM/PM
	 * 
	 * @param timeToFormatInMilliseconds
	 * @return
	 */
	public static String formatTimeNoSeconds(long timeToFormatInMilliseconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.US);
		formatter.setTimeZone(TimeZone.getDefault());
		return formatter.format(timeToFormatInMilliseconds);
	}

	/**
	 * Returns a formated String of the the given time as Month day, year
	 * 
	 * @param timeToFormatInMilliseconds
	 * @return
	 */
	public static String formatDate(long dateToFormatInMilliseconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
		formatter.setTimeZone(TimeZone.getDefault());
		return formatter.format(dateToFormatInMilliseconds);
	}

	/**
	 * Returns a formated String of the the given time as Month day, year
	 * hours:minutes AM/PM
	 * 
	 * @param timeToFormatInMilliseconds
	 * @return
	 */
	public static String formatDateAndTime(long dateAndTimeToFormatInMilliseconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("MMMM d, yyyy  h:mm a", Locale.US);
		formatter.setTimeZone(TimeZone.getDefault());
		return formatter.format(dateAndTimeToFormatInMilliseconds);
	}
}
