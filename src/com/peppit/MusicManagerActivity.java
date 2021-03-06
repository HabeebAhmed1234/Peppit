package com.peppit;

import java.util.ArrayList;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MusicManagerActivity extends Activity {

	public static final String TAG = "MusicManagerActivity";
    
    private PreferencesHandler prefsHandler;
    private Preferences prefs;
    // Data and List Adapter
    MusicListAdapter musicListAdapter;
    ArrayList<Music> music = new ArrayList<Music>();
    ArrayList<Music> selectedMusic = new ArrayList <Music>();
    
    private boolean calledFromWidget = false;
    
    @Override
    public void onDestroy ()
    {
    	super.onDestroy();
    	ArrayList<Music> selectedMusic = new ArrayList<Music>();
    	
    	for(int i=0;i<music.size();i++)
    	{
    		if(music.get(i).isSelected())
    		{
    			selectedMusic.add(music.get(i));
    		}
    	}
        prefsHandler.setMusicList(selectedMusic); 
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Activity State: onCreate()");
        if(getIntent().getExtras()!=null)
        {
	        if(getIntent().getExtras().get(WidgetIntentReciever.FROM_WIDGET)!=null)
	        {
	        	if(getIntent().getExtras().get(WidgetIntentReciever.FROM_WIDGET).toString().compareTo("true")==0)
	        	{
	        		calledFromWidget = true;
	        	}
	        }
        }
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_manager);
        
        prefsHandler = new PreferencesHandler(this);
        prefs = prefsHandler.getSettings();
        selectedMusic=prefs.getMusicList();
        
        ListView lvMusicssContent = (ListView) findViewById(R.id.musicList);
		
        music=getMusic(); 
        
        musicListAdapter = new MusicListAdapter(this, music);
        lvMusicssContent.setAdapter(musicListAdapter);
		
        lvMusicssContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		      @Override
		      public void onItemClick(AdapterView<?> parent,  final View view,int position, long id) {
		    	    String name=(String) ((TextView) view.findViewById(R.id.tvLAYOUTMUSICName)).getText();
		            Music selectedMusic = (Music)parent.getAdapter().getItem(position);
		            selectedMusic.toggleSelected();
		            for(int i = 0;i < parent.getAdapter().getCount();i++)
		            {
		            	if(i!=position)
		            	{
		            		((Music)parent.getAdapter().getItem(i)).unselect();
		            	}
		            }
		            musicListAdapter.notifyDataSetChanged();
		            view.refreshDrawableState();
		      }
		    });
    }
    
    private ArrayList <Music> getMusic()
    {
    	
    	ArrayList <Music> musicList = new ArrayList<Music>();

    	String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

    	String[] projection = {
    	        MediaStore.Audio.Media._ID,
    	        MediaStore.Audio.Media.ARTIST,
    	        MediaStore.Audio.Media.TITLE,
    	        MediaStore.Audio.Media.DATA,
    	        MediaStore.Audio.Media.DISPLAY_NAME,
    	        MediaStore.Audio.Media.DURATION
    	};

    	Cursor cursor = this.managedQuery(
    	        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
    	        projection,
    	        selection,
    	        null,
    	        null);
    	
    	while(cursor.moveToNext()){
    		musicList.add(new Music (cursor.getString(3),cursor.getString(2)));
    	}
  
    	return filter(musicList);
    }
    
    //only checks for names 
    boolean isInArray (ArrayList <Music> mus, Music isin)
    {
    	for(int i=0;i<mus.size();i++)
    	{
    		if(mus.get(i).getName().compareTo(isin.getName())==0)
    		{
    			return true;
    		}
    	}
    	return false;
    }
   
    private ArrayList <Music> filter (ArrayList <Music> unfiltered)
    {
    	ArrayList <Music> filtered = unfiltered;
    	for(int i=0;i<filtered.size();i++)
    	{
    		for(int x=0;x<selectedMusic.size();x++)
    		{
    			if(filtered.get(i).equals(selectedMusic.get(x)))
    			{
    				filtered.get(i).select();
    			}
    			
    		}
    	}
    	return filtered;
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	if(!calledFromWidget)
	    	{
	    		Intent intent = new Intent(this, MainActivity.class);
	    		startActivity(intent);
	    	}
	    	finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}

}






