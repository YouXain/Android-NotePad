package com.example.notepad;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ItemDAO {
	public static final String TABLE_NAME = "note";
	public static final String KEY_ID = "_id";
	public static final String TITLE_COLUMN = "title";
	public static final String CONTEXT_COLUMN = "context";
	public static final String DATETIME_COLUMN = "datetime";
	
	 public static final String CREATE_TABLE = 
	            "CREATE TABLE " + TABLE_NAME + " (" + 
	            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	            TITLE_COLUMN + " TEXT NOT NULL, " +
	            CONTEXT_COLUMN + " TEXT NOT NULL, " +
	            DATETIME_COLUMN + " TEXT NOT NULL)";
	private SQLiteDatabase db;
	
	public ItemDAO(Context context){
		db = MyDBHelper.getDatabase(context);
	}
	
	public void close(){
		db.close();
	}
	
	public Note insert(Note note){
		ContentValues cv = new ContentValues();
		
		cv.put(TITLE_COLUMN, note.getTitle());
		cv.put(CONTEXT_COLUMN, note.getContext());
		cv.put(DATETIME_COLUMN, note.getDateTime());
		long id = db.insert(TABLE_NAME, null, cv);
		note.setId(id);
		return note;
	}
	
	public boolean update(Note note){
		ContentValues cv = new ContentValues();
		
		cv.put(TITLE_COLUMN, note.getTitle());
		cv.put(CONTEXT_COLUMN, note.getContext());
		cv.put(DATETIME_COLUMN, note.getDateTime());
		String where = KEY_ID + "=" + note.getId();
		
		return db.update(TABLE_NAME, cv, where, null) > 0;
	}
	
	public boolean delete(long id){
		String where = KEY_ID + "=" + id;
		return db.delete(TABLE_NAME, where, null) > 0;
	}
	
	public List<Note> getAll(){
		List<Note> result = new ArrayList<Note>();
		Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		while(cursor.moveToNext()){
			result.add(getRecord(cursor));
		}
		
		cursor.close();
		return result;
	}
	
	public Note get(long id){
		Note note = null;
		String where = KEY_ID + "=" + id;
		Cursor result = db.query(TABLE_NAME, null, where, null, null, null, null);
		
		if(result.moveToFirst())
			note = getRecord(result);
		result.close();
		return note;
	}
	
	public Note getRecord(Cursor cursor){
		Note note = new Note(cursor.getString(1), cursor.getString(2));
		note.setId(cursor.getLong(0));
		note.setDateTime(cursor.getString(3));
		return note;
	}
	
	public int getCount(){
		int result = 0;
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
		
		if (cursor.moveToNext()) {
			result = cursor.getInt(0);
	    }
	 
	    return result;
	}
	
}
