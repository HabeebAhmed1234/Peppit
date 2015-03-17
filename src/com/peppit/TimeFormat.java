package com.peppit;

public class TimeFormat {
	
	private int hour;
	private int minute;
	
	TimeFormat (int hour, int minute)
	{
		this.hour=hour;
		this.minute=minute;
	}
	
	public void setHour(int hr)
	{
		this.hour=hr;
	}
	
	public void setMinute(int min)
	{
		if(min>=60)
		{
			min=min-60;
		}
		this.minute=min;
	}
	
	public int getHour()
	{
		return hour;
	}
	
	public int getMinute()
	{
		return minute;
	}
	
	public int getTimeInMinutes()
	{
		return hour*60 + minute;
	}
	
	public boolean equals(TimeFormat time)
	{
		if(this.getTimeInMinutes()==time.getTimeInMinutes())
		{
			return true;
		}else
		{
			return false;
		}
	}
}
