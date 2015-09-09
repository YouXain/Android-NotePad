package com.example.notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper{
	public static final String DATABASE_NAME = "mydata.db";
	public static final int version = 1;
	public static SQLiteDatabase database;
	public MyDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	public static SQLiteDatabase getDatabase(Context context){
		if(database==null || !database.isOpen()){
			database = new MyDBHelper(context, DATABASE_NAME, null , version).getWritableDatabase();
		}
		return database;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(ItemDAO.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP IF EXISTS " + ItemDAO.TABLE_NAME);
		
		onCreate(db);
	}

}
