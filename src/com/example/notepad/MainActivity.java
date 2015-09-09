package com.example.notepad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;











import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ListView;



public class MainActivity extends ListActivity {
	private ListAdapter listAdapter;
	private ItemDAO itemDAO;
	private List<Note> noteList = new ArrayList<Note>();
	private BackgroundContainer mBackgroundContainer;
    private boolean mSwiping = false;
    private boolean mItemPressed = false;
    private HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();

    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBackgroundContainer = (BackgroundContainer) findViewById(R.id.listViewBackground);
		itemDAO = new ItemDAO(getApplicationContext());
		noteList = itemDAO.getAll();
		listAdapter =  new ListAdapter(this, R.layout.opaque_text_view, noteList, mTouchListener);
		//listAdapter.setList(noteList);
		setListAdapter(listAdapter);
	}

	/*private void setDataToAdapter(){
		noteList = itemDAO.getAll();
		listAdapter.setList(noteList);
		setListAdapter(listAdapter);
	}*/
	
	
	@Override
	protected void onResume() {
		//sharedpreferences = getApplicationContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
		//if(this.getIntent().getExtras()!=null){
			//Note note = new Note();
			//Bundle data = getIntent().getExtras();
			//note = (Note) data.getParcelable("noteFromNoteActivity");
			//noteList.add(note);
			//Log.e("123", note.getTitle());
			//listAdapter.setList(noteList);
		//}
		/*
		if(sharedpreferences.contains("insert")){
			//setDataToAdapter();
			note.setTitle(sharedpreferences.getString("title", "null"));
			note.setContext(sharedpreferences.getString("context", "null"));
			notifydate = sharedpreferences.getString("notifyDate", "null");
			//Note note = new Note(title, context);
			note.setDateTime(getCurrentTime());
			itemDAO.insert(note);
			noteList.add(note);
			
		}
		else if(sharedpreferences.contains("update")){
			note.setTitle(sharedpreferences.getString("title", "null"));
			note.setContext(sharedpreferences.getString("context", "null"));
			notifydate = sharedpreferences.getString("notifyDate", "null");
			long id = sharedpreferences.getLong("id", 0);
			//Note note = new Note(title, context);
			note.setId(id);
			note.setDateTime(getCurrentTime());
			itemDAO.update(note);
			for(Note n : noteList){
				if(n.getId()==note.getId()){
					int position = noteList.indexOf(n);
					noteList.set(position, note);
					break;
				}
			}
			
		}*/

		
		/*
		if(notifydate!=null && !notifydate.isEmpty()){
			Log.e("tag", notifydate);
			note.setNotifyDate(notifydate);
			//setAlarmToNotify(note);
		}
		sharedpreferences.edit().clear().commit();*/
		//listAdapter.setList(noteList);
	    super.onResume();
	    
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	 
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
		 
	}
	/* 
	@Override
	protected void onListItemClick (ListView l, View v, int position, long id){
		Note clickNote = noteList.get(position);
		Intent intent = new Intent(MainActivity.this, NoteActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("title", clickNote.getTitle());
		bundle.putString("context", clickNote.getContext());
		bundle.putLong("id", clickNote.getId());
		intent.putExtra("note", clickNote);
		MainActivity.this.startActivity(intent);
		this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
	}*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_more:
		 	moreClick();
		 	return true;
		 default:
		 	return super.onOptionsItemSelected(item);
		}
		 	

		 
	 }
	 private void moreClick(){
		 //go to note activity
		 Intent intent = new Intent(MainActivity.this, NoteActivity.class);
		 MainActivity.this.startActivity(intent);
		 this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
	 }
	 
	 private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

	        float mDownX;
	        private int mSwipeSlop = -1;

	        @Override
	        public boolean onTouch(final View v, MotionEvent event) {
	            if (mSwipeSlop < 0) {
	                mSwipeSlop = ViewConfiguration.get(MainActivity.this).
	                        getScaledTouchSlop();
	            }
	            switch (event.getAction()) {
	            case MotionEvent.ACTION_DOWN:
	                if (mItemPressed) {
	                    // Multi-item swipes not handled
	                	
	                    return false;
	                }
	                mItemPressed = true;
	                mDownX = event.getX();
	                break;
	            case MotionEvent.ACTION_CANCEL:
	                v.setAlpha(1);
	                v.setTranslationX(0);
	                mItemPressed = false;
	                break;
	            case MotionEvent.ACTION_MOVE:
	                {
	                    float x = event.getX() + v.getTranslationX();
	                    float deltaX = x - mDownX;
	                    //float deltaXAbs = Math.abs(deltaX);
	                    if (!mSwiping) {
	                        if (deltaX > mSwipeSlop) {
	                            mSwiping = true;
	                            MainActivity.this.getListView().requestDisallowInterceptTouchEvent(true);                         
	                            mBackgroundContainer.showBackground(v.getTop(), v.getHeight());
	                        }
	                    }
	                    if (mSwiping) {
	                        v.setTranslationX((x - mDownX));
	                        v.setAlpha(1 - deltaX / v.getWidth());
	                    }
	                }
	                break;
	            case MotionEvent.ACTION_UP:
	                {
	                	if(!mSwiping && mItemPressed){
	                		int position = MainActivity.this.getListView().getPositionForView(v);
	                		Note clickNote = noteList.get(position);
	                		Intent intent = new Intent(MainActivity.this, NoteActivity.class);
	                		intent.putExtra("note", clickNote);
	                		MainActivity.this.startActivity(intent);
	                		MainActivity.this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
	                	}
	                    // User let go - figure out whether to animate the view out, or back into place
	                    if (mSwiping) {
	                        float x = event.getX() + v.getTranslationX();
	                        float deltaX = x - mDownX;
	                        float deltaXAbs = Math.abs(deltaX);
	                        float fractionCovered;
	                        float endX;
	                        float endAlpha;
	                        final boolean remove;
	                        if (deltaXAbs > v.getWidth() / 4) {
	                            // Greater than a quarter of the width - animate it out
	                            fractionCovered = deltaXAbs / v.getWidth();
	                            endX = deltaX < 0 ? -v.getWidth() : v.getWidth();
	                            endAlpha = 0;
	                            remove = true;
	                        } else {
	                            // Not far enough - animate it back
	                            fractionCovered = 1 - (deltaXAbs / v.getWidth());
	                            endX = 0;
	                            endAlpha = 1;
	                            remove = false;
	                        }
	                        // Animate position and alpha of swiped item
	                        // NOTE: This is a simplified version of swipe behavior, for the
	                        // purposes of this demo about animation. A real version should use
	                        // velocity (via the VelocityTracker class) to send the item off or
	                        // back at an appropriate speed.
	                        long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
	                        MainActivity.this.getListView().setEnabled(false);
	                        v.animate().setDuration(duration).
	                                alpha(endAlpha).translationX(endX).
	                                withEndAction(new Runnable() {
	                                    @Override
	                                    public void run() {
	                                        // Restore animated values
	                                        v.setAlpha(1);
	                                        v.setTranslationX(0);
	                                        if (remove) {
	                                            animateRemoval(MainActivity.this.getListView(), v);
	                                        } else {
	                                            mBackgroundContainer.hideBackground();
	                                            mSwiping = false;
	                                            MainActivity.this.getListView().setEnabled(true);
	                                        }
	                                    }
	                                });
	                    }
	                }
	                mItemPressed = false;
	                break;
	            default: 
	                return false;
	            }
	            return true;
	        }
	    };
	    private void animateRemoval(final ListView listview, View viewToRemove) {
	        int firstVisiblePosition = listview.getFirstVisiblePosition();
	        for (int i = 0; i < listview.getChildCount(); ++i) {
	            View child = listview.getChildAt(i);
	            if (child != viewToRemove) {
	                int position = firstVisiblePosition + i;
	                long itemId = listAdapter.getItemId(position);
	                mItemIdTopMap.put(itemId, child.getTop());
	            }
	        }
	        // Delete the item from the adapter
	        int position = MainActivity.this.getListView().getPositionForView(viewToRemove);
	        Note n = (Note)listAdapter.getItem(position);
	        listAdapter.remove(n);
	        itemDAO.delete(n.getId());

	        final ViewTreeObserver observer = listview.getViewTreeObserver();
	        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
	            @Override
				public boolean onPreDraw() {
	                observer.removeOnPreDrawListener(this);
	                boolean firstAnimation = true;
	                int firstVisiblePosition = listview.getFirstVisiblePosition();
	                for (int i = 0; i < listview.getChildCount(); ++i) {
	                    final View child = listview.getChildAt(i);
	                    int position = firstVisiblePosition + i;
	                    long itemId = listAdapter.getItemId(position);
	                    Integer startTop = mItemIdTopMap.get(itemId);
	                    int top = child.getTop();
	                    if (startTop != null) {
	                        if (startTop != top) {
	                            int delta = startTop - top;
	                            child.setTranslationY(delta);
	                            child.animate().setDuration(MOVE_DURATION).translationY(0);
	                            if (firstAnimation) {
	                                child.animate().withEndAction(new Runnable() {
	                                    @Override
										public void run() {
	                                        mBackgroundContainer.hideBackground();
	                                        mSwiping = false;
	                                        MainActivity.this.getListView().setEnabled(true);
	                                    }
	                                });
	                                firstAnimation = false;
	                            }
	                        }
	                    } else {
	                        // Animate new views along with the others. The catch is that they did not
	                        // exist in the start state, so we must calculate their starting position
	                        // based on neighboring views.
	                        int childHeight = child.getHeight() + listview.getDividerHeight();
	                        startTop = top + (i > 0 ? childHeight : -childHeight);
	                        int delta = startTop - top;
	                        child.setTranslationY(delta);
	                        child.animate().setDuration(MOVE_DURATION).translationY(0);
	                        if (firstAnimation) {
	                            child.animate().withEndAction(new Runnable() {
	                                @Override
									public void run() {
	                                    mBackgroundContainer.hideBackground();
	                                    mSwiping = false;
	                                    MainActivity.this.getListView().setEnabled(true);
	                                }
	                            });
	                            firstAnimation = false;
	                        }
	                    }
	                }
	                mItemIdTopMap.clear();
	                return true;
	            }
	        });
	        mBackgroundContainer.hideBackground();
            mSwiping = false;
            MainActivity.this.getListView().setEnabled(true);
	    }

}
