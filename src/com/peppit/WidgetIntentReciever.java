package com.peppit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetIntentReciever extends BroadcastReceiver {

	private static final String TAG = "Peppit widget intent reciever";
	
	private int busyTime = 300000;
	
	private Context context;
	
	private RemoteViews remoteViews;
	
	String buttonClicked = "";
	
	public static final String BUSY_BUTTON = "1";
	public static final String RINGTONE_BUTTON = "2";
	public static final String START_PEPPIT_BUTTON = "3";
	public static final String SCHEDULE_BUTTON = "4";
	
	public static final String FROM_WIDGET = "from_widget";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getExtras().getString(WidgetProvider.BUTTON_PRESSED_TYPE)!=null)
		{
			buttonClicked = intent.getExtras().getString(WidgetProvider.BUTTON_PRESSED_TYPE);
		}
		
		Log.d("Peppit Widget","widget intent recieved button type is "+buttonClicked);
		
		this.context = context;
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

		refreshButtonClickListeners();
		onClicked();
		pushUpdates();
	}
	
	private void refreshButtonClickListeners()
	{
		Log.d("Peppit Widget","refreshing widget button listeners");
		remoteViews.setOnClickPendingIntent(R.id.Widget_ImBusy, WidgetProvider.buildButtonPendingIntent(context,this.BUSY_BUTTON));
	}
	
	private void onClicked()
	{
		if (buttonClicked.compareTo(this.BUSY_BUTTON)==0) {
			Log.d(TAG, "onClick: Im Busy");
			createBusyEntry();
			if (PeppitService.isBusy){
				remoteViews.setImageViewResource(R.id.Widget_ImBusy, R.drawable.busy_button_on);
			}else{
				remoteViews.setImageViewResource(R.id.Widget_ImBusy, R.drawable.busy_button_off);
			}
		}
	}
	
	public void updateWidgetView()
	{
		if (PeppitService.isBusy){
			remoteViews.setImageViewResource(R.id.Widget_ImBusy, R.drawable.busy_button_on);
		}else{
			remoteViews.setImageViewResource(R.id.Widget_ImBusy, R.drawable.busy_button_off);
		}
		
		pushUpdates();
	}

	protected void createBusyEntry() {
		if(PeppitService.isBusy)
		{
			PeppitService.isBusy = false;
			if(PeppitService.isInstanceCreated()&&!PeppitService.peppitCalendarIsOn)
			{
				context.stopService(new Intent(context, PeppitService.class));
			}
			Toast.makeText(context,"You are not busy.",Toast.LENGTH_SHORT).show();
		}
		else
		{
			PeppitService.isBusy = true;
			
			if(!PeppitService.isInstanceCreated())
			{
				context.startService(new Intent(context, PeppitService.class));
			}
	    
		    Toast.makeText(context,"You are busy",Toast.LENGTH_SHORT).show();
		}
	}
	
	private void pushUpdates()
	{
		Log.d("Peppit Widget","pushing widget updates after intent recieved");
		WidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}
}