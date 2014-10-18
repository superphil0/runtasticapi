package com.superphil0.runtasticapi;


public class Date {
	private String seconds;
	private String month;
	private String hour;
	private String year;
	private String minutes;
	private String day;

	@SuppressWarnings("deprecation")
	public java.util.Date getDate()
	{
		java.util.Date date = new java.util.Date(Integer.parseInt(year),
				Integer.parseInt(month),
				Integer.parseInt(day),
				Integer.parseInt(hour),
				Integer.parseInt(minutes),Integer.parseInt(seconds));
		return date;	
	}
}
