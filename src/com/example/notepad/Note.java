package com.example.notepad;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable{
	private String title;
	private String context;
	private long id;
	private String datetime;
	
	public Note(String title, String context){
		this.title = title;
		this.context = context;
	}
	
	public Note(){
		
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setContext(String context){
		this.context = context;
	}
	
	public String getContext(){
		return context;
	}
	
	public String getDateTime(){
		return datetime;
	}
	
	public void setDateTime(String datetime){
		this.datetime = datetime;
	}
	

	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.title);
		dest.writeString(this.context);
		dest.writeLong(this.id);
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
		public Note createFromParcel(Parcel in) {
            return new Note(in); 
        }

        @Override
		public Note[] newArray(int size) {
            return new Note[size];
        }
    };
    
    public Note(Parcel pc){
        this.title = pc.readString();
        this.context = pc.readString();
        this.id = pc.readLong();
   }
}