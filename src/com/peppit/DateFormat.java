package com.peppit;

public class DateFormat {
	
	private int day;//like normal days
	private int month;//from 0 to 11
	private int year;//like normal years
	
	DateFormat (int day,int month, int year)
	{
		this.day=day;
		this.month=month;
		this.year=year;
	}
	
	public void setDay(int day)
	{
		this.day=day;
	}
	
	public void setMonth(int month)
	{
		this.month=month;
	}
	
	public void setYear(int year)
	{
		this.year=year;
	}
	
	public int getDay()
	{
		return day;
	}
	
	public int getMonth()
	{
		return month;
	}
	
	public int getYear()
	{
		return year;
	}
	
	public boolean equals(DateFormat date)
	{
		if(day==date.getDay()&&month==date.getMonth()&&year==date.getYear())
		{
			return true;
		}else
		{
			return false;
		}
	}
}
