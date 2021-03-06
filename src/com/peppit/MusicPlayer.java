package com.peppit;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import android.widget.Toast;

public class MusicPlayer {

	Context context;
	
	PreferencesHandler prefsHandler;
	Preferences prefs;
	
	MediaPlayer player;
	
	ArrayList <Music> musicList;
	
	MusicPlayer (Context context)
	{
		this.context=context;
		
		prefsHandler = new PreferencesHandler(context);
		prefs = prefsHandler.getSettings();
		musicList = prefs.getMusicList();
	}
	
	public void start()
	{
		player = new MediaPlayer();
		player.setLooping(true);
		
		if(!(musicList==null || musicList.size()==0))
		{
			play(musicList.get(0).getPath());
		}else{
			//play default alarm
			player = MediaPlayer.create(context,R.raw.default_alarm);
			player.setLooping(true);
            player.start();
		}
	}
		
	private void play(String file)
	{
		player.stop();
	  	player.reset();
	  	player.setLooping(true);
        try {
                player.setDataSource(file);
                player.prepare();
                player.start();
        } catch (IllegalArgumentException e) {
                e.printStackTrace();
        } catch (IllegalStateException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
	}
	
	public void stop()
	{
		if(player.isPlaying())
		{
			player.stop();
		}
	}
	
	public boolean isPlaying()
	{
		return player.isPlaying();
	}
}
