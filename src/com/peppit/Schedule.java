package com.peppit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

public class Schedule  {
	
	private ArrayList <Event> events;
	
	public void updateSchedule(ArrayList <Event> evts)
	{
		this.events=evts;
	}
	
	Schedule (ArrayList <Event> evts)
	{
		events=evts;
	}
	
	public ArrayList <Event> getEvents()
	{
		return sortEventList(events);
	}
	
	private boolean isEventInList(ArrayList<Event> evts, Event evt)
	{
		for(int i=0;i<evts.size();i++)
		{
			if(evts.get(i).equals(evt))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEmpty()
	{
		return events.isEmpty();
	}

	public void sortScheduleEvents()
	{
		Collections.sort(events, new CompareClass());
	}
	
	public ArrayList <Event> sortEventList(ArrayList <Event> evts)
	{
		Collections.sort(evts, new CompareClass());
		return evts;
	}
	
	public Schedule getTodaysEvents(int day,int month, int year)
	{
		DateFormat today=new DateFormat(day,month,year);
		
		ArrayList <Event> todaysEvts = new ArrayList();
		
		for(int i=0;i<events.size();i++)
		{
			if(events.get(i).getStartDate().equals(today))
			{
				todaysEvts.add(events.get(i));
			}
		}
		Schedule todaysEventsSchedule= new Schedule (todaysEvts);
		return todaysEventsSchedule;
	}
}
