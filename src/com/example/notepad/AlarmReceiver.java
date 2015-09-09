package com.example.notepad;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub	
		Bundle data = intent.getExtras();
		Note note = (Note) data.getParcelable("notifyNote");
		Intent noteIntent = new Intent(context, NoteActivity.class);
		noteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		noteIntent.putExtra("note", note);
		NotificationCompat.Builder builder = 
		        new NotificationCompat.Builder(context);
		builder.setSmallIcon(R.drawable.ic_launcher)
			   .setWhen(System.currentTimeMillis())
			   .setContentTitle(note.getTitle())
			   .setContentText(note.getContext())
			   .setContentInfo("Check Note")
			   .setDefaults(Notification.DEFAULT_VIBRATE);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(NoteActivity.class);
		stackBuilder.addNextIntent(noteIntent);
		PendingIntent notePendingIntent =
		        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(notePendingIntent);
		Notification notification = builder.build();
		NotificationManager manager = (NotificationManager)
		        context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify((int) note.getId(), notification);
	}
	
	public void setAlarm(Context context, Note note, long notifyTime){
		/*
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long notifyTimeMillis = 0;
		try {
		    Date mDate = sdf.parse(note.getNotifyDate());
		    notifyTimeMillis = mDate.getTime();
		} catch (ParseException e) {
		            e.printStackTrace();
		}*/
		
		Intent intent = new Intent(context, AlarmReceiver.class);
		//Bundle bundle = new Bundle();
		//bundle.putString("title_note", note.getTitle());
		//bundle.putString("context_note", note.getContext());
		//bundle.putLong("id_note", note.getId());
		intent.putExtra("notifyNote", note);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pending = PendingIntent.getBroadcast(context, (int)note.getId(), 
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + notifyTime, pending);
	}
	
}