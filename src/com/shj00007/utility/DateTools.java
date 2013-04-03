package com.shj00007.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTools {
	
	public static String getDate(String pDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.US);
		try {
			Date date = sdf.parse(pDate);
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String _Date = sdf.format(date);
			return _Date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
