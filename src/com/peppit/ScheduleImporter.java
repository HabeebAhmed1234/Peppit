package com.peppit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ScheduleImporter {
	
	private Context context;
	
	ScheduleImporter(Context con)
	{
		this.context=con;
	}
	
	public ArrayList<Event> readCalendarEvents() {
		
		ArrayList <Event> events = new ArrayList();
		
	    Cursor cursor = context.getContentResolver()
	            .query(
	                    Uri.parse("content://com.android.calendar/events"),
	                    new String[] { "calendar_id", "title", "description",
	                            "dtstart", "dtend", "eventLocation" }, null,
	                    null, null);
	    
	    cursor.moveToFirst();
	    
	    // fetching calendars name
	    String CNames[] = new String[cursor.getCount()];
	    
	    for (int i = 0; i < CNames.length; i++) {
	    	
	        String name = cursor.getString(1);
	        Calendar startDate = getDate(Long.parseLong(cursor.getString(3)));
	        Calendar endDate = getDate(Long.parseLong(cursor.getString(4)));
	        String description = cursor.getString(2);
	        
	        events.add(makeEvent(name,startDate,endDate));
	        
	        CNames[i] = cursor.getString(1);
	        cursor.moveToNext();
	    }
	    
	    return events;
	}

	private Event makeEvent(String name, Calendar start, Calendar end)
	{
		Event event = new Event(start.get(Calendar.DAY_OF_MONTH), start.get(Calendar.MONTH), start.get(Calendar.YEAR),
								end.get(Calendar.DAY_OF_MONTH), end.get(Calendar.MONTH), end.get(Calendar.YEAR),
								start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), 
								end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE), 
								name);
		return event;
	}
	
	private Calendar getDate(long milliSeconds) {
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTimeInMillis(milliSeconds);
	    return calendar;
	}
}
