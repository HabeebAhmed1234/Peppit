package com.peppit;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferencesHandler {

	private Context context;
	private static final String PREFS_NAME="CONFIG";
	public static final String MUSIC_KEY="musicison";
	public static final String MUSICS_AMMOUNT_KEY="MUSICS_AMMOUNT";
	public static final String TODAY_DAY_KEY="TODAY_DAY";
	public static final String TODAY_MONTH_KEY="TODAY_MONTH";
	public static final String TODAY_YEAR_KEY="TODAY_YEAR";
	public static final String SCHEDULE_KEY="SCHEDULE";
	public static final String NUMBER_OF_BOOTS = "NUMBEROFBOOTS";
	
	public static final String NULL="0";
	private SharedPreferences settings ;
	
	PreferencesHandler(Context con)
	{
		context=con;
		settings = context.getSharedPreferences(PREFS_NAME, 0);
	}
	
	private void set(String settingname, String settingvalue)
	{
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(settingname, settingvalue);
        editor.commit();
	}
	
	private String get(String key)
	{
        return settings.getString(key, NULL);
	}
	
	void setTodaysDate(int day, int month, int year)
	{
		set(this.TODAY_DAY_KEY,Integer.toString(day));
		set(this.TODAY_MONTH_KEY,Integer.toString(month));
		set(this.TODAY_YEAR_KEY,Integer.toString(year));
	}
	
	void setMusicOption(boolean setting)
	{
		if(setting)
		{
			set(MUSIC_KEY,"true");
		}else
		{
			set(MUSIC_KEY,"false");
		}
	}
	
	private Schedule getSchedule()
	{
		//get events from calendar app
		ScheduleImporter schImporter = new ScheduleImporter(this.context);
		Schedule newSchedule = new Schedule(schImporter.readCalendarEvents());
		return  newSchedule;
	}
	
	void setMusicList(ArrayList<Music> selectedMusic) {
		//shows the number of contacts in total
		this.set(MUSICS_AMMOUNT_KEY,Integer.toString(selectedMusic.size()));
		
		for(int i=0;i<selectedMusic.size();i++)
		{
			this.set("MUSIC_NUMBER_"+i,selectedMusic.get(i).getPath());
			this.set("MUSIC_NAME_"+i,selectedMusic.get(i).getName());
		}
	}
	
	public void incrementNumberOfBoots(int increment)
	{
		int curval = 0;
		
		if(get(this.NUMBER_OF_BOOTS).compareTo("NULL")!=0)
		{
			curval = Integer.parseInt(get(this.NUMBER_OF_BOOTS));
		}
		
		set(this.NUMBER_OF_BOOTS,Integer.toString(curval+increment));
	}
	
	public void disableIncrementNumberOfBoots()
	{
		set(this.NUMBER_OF_BOOTS,Integer.toString(-1));
	}
	
	private int getNumberOfBoots()
	{
		if(get(this.NUMBER_OF_BOOTS).compareTo("NULL")!=0)
		{
			return Integer.parseInt(get(this.NUMBER_OF_BOOTS));
		}
		return 0;
	}
	
	Preferences getSettings()
	{
		Preferences prefs=new Preferences();
	
		if(get(this.MUSIC_KEY).compareTo("true")==0)
		{
			prefs.setmusic(true);
		}else{
			prefs.setmusic(false);
		}
		
		//add in music
		String ammountOfMusic=get(MUSICS_AMMOUNT_KEY);
		if(!(ammountOfMusic.compareTo(NULL)==0))
		{
			for(int i = 0; i<Integer.parseInt(ammountOfMusic);i++)
			{
				Music mus = new Music(get("MUSIC_NUMBER_"+i),get("MUSIC_NAME_"+i));
				mus.select();
				prefs.addMusicToList(mus);
			}
		}
		
		prefs.setSchedule(getSchedule());
		prefs.setNumberOfBoots(this.getNumberOfBoots());
		return prefs;
	}
	
}
