package com.stormchat;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static Calendar getCurrentDateCalendar() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return cal;
	}
	
	public static Date getCurrentDate() {
		return getCurrentDateCalendar().getTime();
	}
}
