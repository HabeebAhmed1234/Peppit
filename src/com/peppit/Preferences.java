package com.peppit;

import java.util.ArrayList;

import android.content.Context;

public class Preferences {
	private boolean music=false;
	private ArrayList<Music>   musicList   = new ArrayList<Music>();
	private Schedule schedule;
	
	private int numberOfBoots = 0;
	
	void setmusic(boolean setting)
	{
		music=setting;
	}
	
	void addMusicToList(Music music)
	{
		musicList.add(music);
	}
	
	boolean getmusic()
	{
		return music;
	}
	
	ArrayList <Music> getMusicList()
	{
		return musicList;
	}
	
	void setSchedule(Schedule sched)
	{
		this.schedule=sched;
	}
	
	Schedule getSchedule()
	{
		return this.schedule;
	} 
	
	public void setNumberOfBoots(int ammount)
	{
		this.numberOfBoots = ammount;
	}
	
	public int getNumberOfBoots()
	{
		return this.numberOfBoots;
	}
}
