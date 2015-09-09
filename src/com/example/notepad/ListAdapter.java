package com.example.notepad;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter{
	private List<Note> noteList;
	private Context mContext;
	private HashMap<Note, Integer> mIdMap = new HashMap<Note, Integer>();
    private View.OnTouchListener mTouchListener;
    
	public ListAdapter(Context context, int textViewResourceId,
            List<Note> objects, View.OnTouchListener listener){
		mContext = context;
		mTouchListener = listener;
		noteList = objects;
		setList(noteList);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return noteList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return noteList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		Note item = noteList.get(position);
        return mIdMap.get(item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listrow, parent, false);
			convertView.setOnTouchListener(mTouchListener);
		}
		setListRow(convertView, position);
		return convertView;
	}
	
	private void setListRow(View convertView, int position) {
		// TODO Auto-generated method stub
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView context = (TextView) convertView.findViewById(R.id.context);
		TextView datetime = (TextView) convertView.findViewById(R.id.datetime);
		Note note = noteList.get(position);
		
		title.setText(note.getTitle());
		context.setText(note.getContext());
		datetime.setText(note.getDateTime());
	}
	
	public void remove(Object object){
		noteList.remove(object);
		notifyDataSetChanged();
	}
	
	public void setList(List<Note> noteList){
		this.noteList = noteList;
		for(Note n: noteList){
			mIdMap.put(n, (int)n.getId());
		}
		notifyDataSetChanged();
	}
	
	
}