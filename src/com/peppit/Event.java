package com.peppit;

import android.util.Log;

public class Event {

	private TimeFormat startTime=new TimeFormat(0,0);
	private TimeFormat endTime=new TimeFormat(0,0);
	private DateFormat startDate=new DateFormat(0,0,0);
	private DateFormat endDate=new DateFormat(0,0,0);
	
	private String name;
	
	Event (int start_date_day, int start_date_month,int start_date_year,int end_date_day, int end_date_month,int end_date_year, int begin_hour, int begin_minute, int finish_hour, int finish_minute, String name)
	{
		startTime.setHour(begin_hour);
		startTime.setMinute(begin_minute);
		endTime.setHour(finish_hour);
		endTime.setMinute(finish_minute);
		startDate.setDay(start_date_day);
		startDate.setMonth(start_date_month);
		startDate.setYear(start_date_year);
		endDate.setDay(end_date_day);
		endDate.setMonth(end_date_month);
		endDate.setYear(end_date_year);
		this.name=name;
		//Log.d("eventadd","inEvent - constructor "+Integer.toString(start.getMinute())+"-"+Integer.toString(end.getMinute()));
	}
	
	public boolean equals(Event evt)
	{
		if(evt.getStartDate().equals(this.startDate)&&evt.getEndDate().equals(this.endDate)&&evt.getStartTime().equals(this.startTime)&&evt.getEndTime().equals(this.endTime) && evt.name.compareTo(this.name)==0)
		{
			return true;
		}
		return false;
	}
	
	public TimeFormat getStartTime()
	{
		return startTime;
	}
	
	public TimeFormat getEndTime()
	{
		return endTime;
	}
	
	public DateFormat getStartDate()
	{
		return startDate;
	}
	
	public DateFormat getEndDate()
	{
		return endDate;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setStartTime(TimeFormat start)
	{
		this.startTime=start;
	}
	
	public void setEndTime(TimeFormat end)
	{
		this.endTime=end;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}
	
	public void setDate(DateFormat startDate)
	{
		this.startDate = startDate;
	}
	
}
