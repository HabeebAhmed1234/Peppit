package com.peppit;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

public class PeppitService extends Service  {
	private static final String TAG = "MyService";
	public static final String PEPPIT_URL = "some-url";
	
	private static PreferencesHandler prefsHandler;
	private Preferences prefs;
	
	private boolean ringingStarted = false;
	
	private Timer myTimer;
	TelephonyManager telephonymanager;
	PhoneStateListener listener;
	private static Schedule schedule;
	MusicPlayer musicPlayer;
	
	public static boolean issending;
	
	private static PeppitService instance = null;
	
	public static boolean peppitCalendarIsOn = false;
	public static boolean isBusy = false;
	
	private AudioManager audioManager;
	
	private int previousRingerMode;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	//returns the event with which the schedule is conflicting. null otherwise
	Event scheduleConflict()
	{
		ArrayList <Event> events=schedule.getEvents();
		CompareClass comparer = new CompareClass();
		
		for(int i=0;i<events.size();i++)
		{
			if(comparer.eventConflictsWithCurrentTime(events.get(i)))
			{
				return events.get(i);
			}
		}
		return null;
	}
	
	String getNextAvailableTime()
	{
		Event conflictingEvent=scheduleConflict();
		if(conflictingEvent==null) return "";
		
		String nextTime=Integer.toString(conflictingEvent.getEndTime().getHour())+":"+Integer.toString(conflictingEvent.getEndTime().getMinute());
		
		return nextTime;
	}
	
	void sendSMS(String number)
	{
		if(number==null)
		{
	        Log.d("MyService","Phone Number Not Found. Please get caller ID!");
		    Toast.makeText(getBaseContext(),"Peppit requires caller ID! Cannot send message!",Toast.LENGTH_SHORT).show();
	        return;
		}
		// make sure the fields are not empty
		String msg = "";
		
		if(PeppitService.isBusy)
		{
			msg="I am busy for the next few minutes";
		}
		else{
			msg="I am busy right now. My next available time is "+getNextAvailableTime();
		}
		
		msg+= " <<<Sent from Peppit @ "+ PEPPIT_URL + ">>>";
		
		if (number.length()>0 && msg.length()>0)                
		{   
			Log.d("MyService","Sending Text");
		    SmsManager sms = SmsManager.getDefault();
		    // this is the function that does all the magic
		    sms.sendTextMessage(number, null, msg, null, null);  
		}
		else
		{
			// display message if text fields are empty
		    Toast.makeText(getBaseContext(),"Error text not sent!",Toast.LENGTH_SHORT).show();
		    Log.d("MyService","Error text not sent!");
		}
	}
	
	void alertUserOfCall()
	{
		Log.d("MyService","Playing Ringtone");
		if(musicPlayer!=null)
		{
			musicPlayer.start();
	        ringingStarted=true;
		}
	}
	
	void stopRinger()
	{
		if(musicPlayer!=null)
		{
			if(musicPlayer.isPlaying())
			{
				musicPlayer.stop();
			}
		}
	}
	
	void handleIncomingCall(String number)
	{
		//check if there is a conflict in the schedule
		if((scheduleConflict()!=null&&peppitCalendarIsOn) || PeppitService.isBusy) 
		{
			Log.d("MyService","Conflict");
			sendSMS(number);
			return;
		}
		Log.d("MyService","No Conflict");
		alertUserOfCall();
		return;
    }
	
	public static void updateSchedule()
	{
		if(!isInstanceCreated())return;
		//update schedule
		Log.d(TAG, "Updating Schedule");
        schedule=prefsHandler.getSettings().getSchedule();
    	Log.d(TAG, "Finished Updating Schedule");
	}
	
	public static boolean isInstanceCreated() { 
	      if(PeppitService.instance == null)
	      {
	    	  return false;
	      }
	      return true;
	}
	
	@Override
	public void onCreate() {
		instance = this;
		issending=false;
		Log.d("Peppitservicestate","start");
		Log.d(TAG, "onCreate");
		
		audioManager =(AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
		previousRingerMode = audioManager.getRingerMode();
		audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT );
		
		
		prefsHandler = new PreferencesHandler(this);

		musicPlayer = new MusicPlayer(this);
		
		myTimer = new Timer();
	    myTimer.schedule(new TimerTask() {          
	        @Override
	        public void run() {
	        	updateSchedule();
	        }

	    }, 0, 60000);
	    
		//create Schedule
		updateSchedule();
		
		telephonymanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				
		// Create a new PhoneStateListener
	    listener = new PhoneStateListener() {
	      @Override
	      public void onCallStateChanged(int state, String incomingNumber) {
	        String stateString = "N/A";
	        switch (state) {
	        case TelephonyManager.CALL_STATE_IDLE:
	          stateString = "Idle";
	          if(ringingStarted)
	          {
	        	  stopRinger();
	        	  ringingStarted=false;
	          }
	          issending=false;
	          break;
	        case TelephonyManager.CALL_STATE_OFFHOOK:
	          stateString = "Off Hook";
	          issending=false;
	          if(ringingStarted)
	          {
	        	  stopRinger();
	        	  ringingStarted=false;
	          }
	          break;
	        case TelephonyManager.CALL_STATE_RINGING:
	          stateString = "Ringing";
	          Log.d("MyService","Ringing");
	          if(issending==false)
	          {
	        	  handleIncomingCall(incomingNumber);
	        	  issending=true;
	          }
	          break;
	        }
	      }
	    };

	    // Register the listener with the telephony manager
	    telephonymanager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Peppit Stopped", Toast.LENGTH_SHORT).show();
		Log.d("Peppitservicestate","stop");
		audioManager.setRingerMode(previousRingerMode);
		instance = null;
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "Peppit Started", Toast.LENGTH_SHORT).show();
		issending=false;
		instance = this;
		Log.d("Peppitservicestate","start");
	}
}