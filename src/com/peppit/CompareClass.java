package com.peppit;

import java.util.Comparator;

import android.text.format.Time;

public class CompareClass implements Comparator<Event> {
	@Override
	public int compare(Event lhs, Event rhs) {
		// TODO Auto-generated method stub
		if (lhs.getStartDate().getYear() < rhs.getStartDate().getYear()) {
			if (lhs.getStartDate().getMonth() < rhs.getStartDate().getMonth()) {
				if (lhs.getStartDate().getDay() < rhs.getStartDate().getDay()) {
					if (lhs.getStartTime().getTimeInMinutes() < rhs.getStartTime().getTimeInMinutes()) {
						return -1;
					}
				}
			}
		}
		if (lhs.getStartDate().getYear() > rhs.getStartDate().getYear()) {
			if (lhs.getStartDate().getMonth() > rhs.getStartDate().getMonth()) {
				if (lhs.getStartDate().getDay() > rhs.getStartDate().getDay()) {
					if (lhs.getStartTime().getTimeInMinutes() > rhs.getStartTime().getTimeInMinutes()) {
						return 1;
					}
				}
			}
		}
		return 0;
	}
	
	public boolean eventConflictsWithCurrentTime(Event event)
	{
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		
		int 		currentTimeInMinutes	=today.hour*60+today.minute;
		DateFormat  currentDate				=new DateFormat(today.monthDay,today.month,today.year);
		
		
		if(event.getStartDate().getYear() <= currentDate.getYear() && event.getEndDate().getYear() >= currentDate.getYear())
		{
			if(event.getStartDate().getMonth() <= currentDate.getMonth() && event.getEndDate().getMonth() >= currentDate.getMonth())
			{
				if(event.getStartDate().getDay() <= currentDate.getDay() && event.getEndDate().getDay() >= currentDate.getDay())
				{
					if(event.getStartTime().getTimeInMinutes() <= currentTimeInMinutes && event.getEndTime().getTimeInMinutes() > currentTimeInMinutes)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}
