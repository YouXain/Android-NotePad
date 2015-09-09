package com.example.notepad;



import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class NoteActivity extends FragmentActivity implements OnDateSetListener{
	private EditText title;
	private EditText context;
	private ItemDAO itemDAO;
	private Note note = null;
	private AlarmReceiver alarm;
	private static int BASIC_ID = 0;
	private long notifyTime = 0;
	private NotificationManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		itemDAO = new ItemDAO(getApplicationContext());
		findView();
		
		if(this.getIntent().getExtras()!=null){
			Bundle data = getIntent().getExtras();
			note = (Note) data.getParcelable("note");
			initView();
		}
	}
	private void setNotification() {
		// TODO Auto-generated method stub
		Intent noteIntent = new Intent(NoteActivity.this, NoteActivity.class);
		noteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		noteIntent.putExtra("note", note);
		BASIC_ID--;
		NotificationCompat.Builder builder = 
		        new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher)
			   .setWhen(System.currentTimeMillis())
			   .setContentTitle(title.getText().toString())
			   .setContentText(context.getText().toString())
			   .setContentInfo("New Note")
			   .setDefaults(Notification.DEFAULT_VIBRATE);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(NoteActivity.class);
		stackBuilder.addNextIntent(noteIntent);
		PendingIntent notePendingIntent =
		        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(notePendingIntent);
		Notification notification = builder.build();
		manager = (NotificationManager)
		        getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(BASIC_ID, notification);
	}
	@Override
	protected void onResume() {
		
	    super.onResume();
	    
	}
	private void findView() {
		// TODO Auto-generated method stub
		title = (EditText)findViewById(R.id.title_note);
		context = (EditText)findViewById(R.id.context_note);
	}
	private void initView() {
		// TODO Auto-generated method stub
		title.setText(note.getTitle());
		context.setText(note.getContext());
		//Log.e("123", note.getTitle().toString());
	}
	@Override
	protected void onPause() {
	    super.onPause();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // return to the App's Home Activity
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	searchClick();
	    }
	        return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.note_menu, menu);
	 
	    return super.onCreateOptionsMenu(menu);
	}
	 @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()){
			case R.id.action_search:
				searchClick();
			 	return true;
			case R.id.action_settings:
			 	settingsClick();
			 	return true;
			 default:
			 	return super.onOptionsItemSelected(item);
			}
			 		 
	 }
	 
	 
	 private boolean insertOrupdateNote(){
		 if(!context.getText().toString().isEmpty() && note==null){
				if(title.getText().toString().isEmpty()){
					note = new Note("untitle", context.getText().toString());
					note.setDateTime(getCurrentTime());
					itemDAO.insert(note);
				}
				else{
					note = new Note(title.getText().toString(), context.getText().toString());
					note.setDateTime(getCurrentTime());
					itemDAO.insert(note);
				}
				setNotification();
				return true;
		}
		 else if(note!=null){
				if(!title.getText().toString().equalsIgnoreCase(note.getTitle())
						|| !context.getText().toString().equalsIgnoreCase(note.getContext())){
					note.setTitle(title.getText().toString());
					note.setContext(context.getText().toString());
					note.setDateTime(getCurrentTime());
					itemDAO.update(note);
					setNotification();
				}
				return true;
		 }
		 return false;
	 }
	 	
	 private void searchClick(){
		 //go to search activity
		 insertOrupdateNote();
		 Intent intent = new Intent(NoteActivity.this, MainActivity.class);
		 NoteActivity.this.startActivity(intent);
		 this.overridePendingTransition(R.anim.slide_back, R.anim.slide_forward);
	 }
		 
		 @SuppressLint("SimpleDateFormat")
		 private String getCurrentTime(){
			Calendar c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formattedDate = df.format(c.getTime());
			return formattedDate;
		 }
		 

		 
		 private void settingsClick(){
			 //do nothing
			 DatePicker dialog=new DatePicker();
		     FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
		     dialog.show(ft, "DatePicker");
		     
		 }
		@Override
		public void onDateSet(android.widget.DatePicker view, int year,
				int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			if(insertOrupdateNote()){
				Calendar c = Calendar.getInstance();
				//Date today = new Date();
				long curMillis = System.currentTimeMillis();
				c.set(Calendar.YEAR, year);
				c.set(Calendar.MONTH, monthOfYear);
				c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				c.set(Calendar.HOUR, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				long setMillis = c.getTimeInMillis();
				if(setMillis > curMillis){
					notifyTime = setMillis - curMillis;
					alarm = new AlarmReceiver();
					alarm.setAlarm(this, note, notifyTime);
				}
			}
		}
		
}