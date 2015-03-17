package com.peppit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private static final String TAG = "Peppit";
	
	ImageButton buttonImBusy, buttonCalendar, buttonSetRingtone;
	ImageButton buttonServiceStart;
	
	//id must be in this format
	public static final String APP_ID = "com.peppit";
	
	PreferencesHandler prefsHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		buttonServiceStart = (ImageButton) findViewById(R.id.peppitStart);
		buttonImBusy = (ImageButton) findViewById(R.id.ImBusy);
		buttonCalendar = (ImageButton) findViewById(R.id.Schedule);
		buttonSetRingtone = (ImageButton) findViewById(R.id.SetRingtone);

		buttonServiceStart.setOnClickListener(this);
		buttonImBusy.setOnClickListener(this);
		buttonCalendar.setOnClickListener(this);
		buttonSetRingtone.setOnClickListener(this);

		if (PeppitService.peppitCalendarIsOn) {
			buttonServiceStart.setImageResource(R.drawable.power_on);
		}else{
			buttonServiceStart.setImageResource(R.drawable.power_off);
		}
		
		if (PeppitService.isBusy){
			buttonImBusy.setImageResource(R.drawable.busy_button_on);
		}else{
			buttonImBusy.setImageResource(R.drawable.busy_button_off);
		}
		
		prefsHandler = new PreferencesHandler(this);
		if(prefsHandler.getSettings().getNumberOfBoots()%5==0 && prefsHandler.getSettings().getNumberOfBoots()>0)
		{
			showRatingDialog();
		}
	}
	
	private boolean startAnyActivity(Intent aIntent) {
	    try
	    {
	        startActivity(aIntent);
	        return true;
	    }
	    catch (ActivityNotFoundException e)
	    {
	        return false;
	    }
	}
	 
	//On click event for rate this app button
	public void openRatingsSetter() {
	    Intent intent = new Intent(Intent.ACTION_VIEW);
	    //Try Google play
	    intent.setData(Uri.parse("market://details?id="+APP_ID));
	    if (startAnyActivity(intent) == false) {
	        //Market (Google play) app seems not installed, let's try to open a webbrowser
	        intent.setData(Uri.parse("https://play.google.com/store/apps/details?"+APP_ID));
	        if (startAnyActivity(intent) == false) {
	            //Well if this also fails, we have run out of options, inform the user.
	            Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
	        }
	    }
	}

	public void onClick(View src) {

		if (src.getId() == R.id.peppitStart) {
			adjustStartButton();
		}

		if (src.getId() == R.id.ImBusy) {
			Log.d(TAG, "onClick: Im Busy");
			createBusyEntry();
			if (PeppitService.isBusy){
				buttonImBusy.setImageResource(R.drawable.busy_button_on);
			}else{
				buttonImBusy.setImageResource(R.drawable.busy_button_off);
			}
			
			WidgetProvider.forceWidgetUpdate(this);
		}

		if (src.getId() == R.id.Schedule) {
			Log.d(TAG, "onClick: Schedule");
			//start calendar app
			Intent calendarIntent = new Intent() ;
			
			//Froyo or greater
			calendarIntent.setClassName("com.android.calendar","com.android.calendar.LaunchActivity");
			
			PackageManager packageManager = getPackageManager();
			List<ResolveInfo> activities = packageManager.queryIntentActivities(calendarIntent, 0);
			
			
			boolean isIntentSafe = activities.size() > 0;
			
			if(isIntentSafe)
			{
				startActivity(calendarIntent);
			}else{
				Toast.makeText(getBaseContext(),"Android Calendar app is not installed!",Toast.LENGTH_LONG).show();
			}
		}

		if (src.getId() == R.id.SetRingtone) {
			Log.d(TAG, "onClick: SetRingtone");
			Intent intent3 = new Intent(this, MusicManagerActivity.class);
			startActivity(intent3);
			finish();
		}
	}

	private void adjustStartButton() {

		if (PeppitService.peppitCalendarIsOn) {
			Log.d("Peppitservicestate", "exists");
			PeppitService.peppitCalendarIsOn = false;
			if(!PeppitService.isBusy)stopService(new Intent(this, PeppitService.class));
			buttonServiceStart.setImageResource(R.drawable.power_off);
		} else if (!PeppitService.peppitCalendarIsOn) {
			Log.d("Peppitservicestate", "null");
			PeppitService.peppitCalendarIsOn = true;
			if(!PeppitService.isInstanceCreated())startService(new Intent(this, PeppitService.class));
			buttonServiceStart.setImageResource(R.drawable.power_on);
		}
	}

	protected void createBusyEntry() {
		if(PeppitService.isBusy)
		{
			PeppitService.isBusy = false;
			if(PeppitService.isInstanceCreated()&&!PeppitService.peppitCalendarIsOn)
			{
				stopService(new Intent(this, PeppitService.class));
			}
			Toast.makeText(getBaseContext(),"You are not busy.",Toast.LENGTH_SHORT).show();
		}
		else
		{
			PeppitService.isBusy = true;
			
			if(!PeppitService.isInstanceCreated())
			{
				startService(new Intent(this, PeppitService.class));
			}
	    
		    Toast.makeText(getBaseContext(),"You are busy",Toast.LENGTH_SHORT).show();
		}
	}
	
	private void showRatingDialog()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
			// set title
			alertDialogBuilder.setTitle("Rate!");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Would you like to rate this App?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						prefsHandler.disableIncrementNumberOfBoots();
						openRatingsSetter();
						dialog.cancel();
					}
				  })
				.setNegativeButton("Don't Show Again",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						prefsHandler.disableIncrementNumberOfBoots();
						dialog.cancel();
					}
				});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		PeppitService.updateSchedule();
	}
}
