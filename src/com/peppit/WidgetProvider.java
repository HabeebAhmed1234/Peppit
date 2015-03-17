package com.peppit;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
	
	public static final String BUTTON_PRESSED_TYPE = "BUTTON_PRESSED";
	
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d("Peppit Widget","Updating widget");
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		
		if (PeppitService.isBusy){
			remoteViews.setImageViewResource(R.id.Widget_ImBusy, R.drawable.busy_button_on);
		}else{
			remoteViews.setImageViewResource(R.id.Widget_ImBusy, R.drawable.busy_button_off);
		}
		remoteViews.setOnClickPendingIntent(R.id.Widget_ImBusy, buildButtonPendingIntent(context,WidgetIntentReciever.BUSY_BUTTON));
		

		pushWidgetUpdate(context, remoteViews);
	}
	
	public static PendingIntent buildButtonPendingIntent(Context context,String buttonType) {
		Intent intent = new Intent();
		intent.setAction("buttonClickedIntent");
		intent.putExtra(BUTTON_PRESSED_TYPE, buttonType);
	    return PendingIntent.getBroadcast(context, Integer.parseInt(buttonType), intent, PendingIntent.FLAG_ONE_SHOT);
	}

	public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
		Log.d("Peppit Widget","Pushing widget update");
		ComponentName myWidget = new ComponentName(context, WidgetProvider.class);
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    manager.updateAppWidget(myWidget, remoteViews);		
	}
	
	public static void forceWidgetUpdate(Context context)
	{
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		
		if (PeppitService.isBusy){
			remoteViews.setImageViewResource(R.id.Widget_ImBusy, R.drawable.busy_button_on);
		}else{
			remoteViews.setImageViewResource(R.id.Widget_ImBusy, R.drawable.busy_button_off);
		}
		remoteViews.setOnClickPendingIntent(R.id.Widget_ImBusy, forcebuildButtonPendingIntent(context,WidgetIntentReciever.BUSY_BUTTON));
		

		pushWidgetUpdate(context, remoteViews);
	}
	
	public static PendingIntent forcebuildButtonPendingIntent(Context context,String buttonType) {
		Intent intent = new Intent();
		intent.setAction("buttonClickedIntent");
		intent.putExtra(BUTTON_PRESSED_TYPE, buttonType);
	    return PendingIntent.getBroadcast(context, Integer.parseInt(buttonType), intent, PendingIntent.FLAG_ONE_SHOT);
	}
}
