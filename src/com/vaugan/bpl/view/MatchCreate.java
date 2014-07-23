package com.vaugan.bpl.view;

import com.vaugan.bpl.R;
import com.vaugan.bpl.model.MatchDbAdapter;
import com.vaugan.bpl.model.PlayerDbAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorTreeAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MatchCreate extends Activity {
    
    private static final String TAG = "MatchCreate";
//    private EditText mDateTimeText;
//    private EditText mVenueText;
//    private EditText mBestOfText;
    private Long mP1RowId;
    private Long mP2RowId;
    private EditText mSet1Result;
//    private EditText mSet2Result;
//    private EditText mSet3Result;
    private Long mRowId;
    private MatchDbAdapter mDbHelper;
    private PlayerDbAdapter playerDbHelper;
    public Context mContext;    
    SimpleCursorAdapter sca;
 
    protected static final int ACTIVITY_MATCH_DISPLAY = 0;
    protected static final int ACTIVITY_MAIN_MENU = 4;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{ 
	        mDbHelper = new MatchDbAdapter(this);
	        mDbHelper.open();
	        
	        
	        playerDbHelper = new PlayerDbAdapter(this);
	        playerDbHelper.open();
	        
	
	        setContentView(R.layout.match_create);
//	        setContentView(R.layout.player_row);
	        setTitle(R.string.create_match);

	        fillData();

//	        mDateTimeText = (EditText) findViewById(R.id.editDate);
//	        mVenueText = (EditText)findViewById(R.id.editVenue);
//	        mBestOfText = (EditText)findViewById(R.id.editMatchBestOf);
	        //mResultText = (EditText)"incomplete";
	
            Log.v(TAG,"mRowID____1="+mRowId);
	        
	        mRowId = (savedInstanceState == null) ? null :
	            (Long) savedInstanceState.getSerializable(MatchDbAdapter.KEY_ROWID);
	        
            Log.v(TAG,"mRowID____2="+mRowId);
	        
			if (mRowId == null) {
				Bundle extras = getIntent().getExtras();
				mRowId = extras != null ? extras.getLong(MatchDbAdapter.KEY_ROWID)
										: null;
                Log.v(TAG,"mRowID____3="+mRowId);
			}
	
			populateFields();
	
            Button startMatchButton = (Button) findViewById(R.id.btnStartMatch);
            startMatchButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    saveState();
                    Intent i = new Intent(MatchCreate.this, MatchDisplay.class);
                    Log.v(TAG,"mRowID="+mRowId);
                    Log.v(TAG,"MatchDbAdapter.KEY_ROWID="+MatchDbAdapter.KEY_ROWID);
                    
                    i.putExtra("com.vaugan.csf.match.rowid", mRowId); 
                    i.putExtra("com.vaugan.csf.match.p1rowid", mP1RowId); 
                    i.putExtra("com.vaugan.csf.match.p2rowid", mP2RowId); 
                    
                    startActivityForResult(i, ACTIVITY_MATCH_DISPLAY);    
                }
            });   

            Button cancelButton = (Button) findViewById(R.id.btnCancel);
            cancelButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MatchCreate.this, MainMenu.class);
                    startActivityForResult(i, ACTIVITY_MAIN_MENU);    
                }
            });   

        }catch (Exception e) {
            // handle any errors
            Log.e("csfmatch", "Error in activity", e);  // log the error
            // Also let the user know something went wrong
            Toast.makeText(
                getApplicationContext(),
                e.getClass().getName() + " " + e.getMessage(),
                Toast.LENGTH_LONG).show();
        }
        
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchMatch(mRowId);
            startManagingCursor(note);
//            mDateTimeText.setText(note.getString(
//                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_DATETIME)));
//            mVenueText.setText(note.getString(
//                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_VENUE)));
//            mBestOfText.setText(note.getString(
//                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_BEST_OF)));
//            mP1RowId = (note.getString(
//                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_P1)));
//            mP2RowId = (note.getString(
//                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_P2)));
//            mSet1Result.setText(note.getString(
//                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_SET1_RESULT)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(MatchDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
    	
    	int position = ((Spinner) findViewById(R.id.spinnerPlayer1)).getSelectedItemPosition();
    	Cursor cursor = (Cursor) sca.getItem(position);
    	mP1RowId = cursor.getLong(cursor.getColumnIndex(PlayerDbAdapter.KEY_ROWID));
    	
    	position = ((Spinner) findViewById(R.id.spinnerPlayer2)).getSelectedItemPosition();
    	cursor = (Cursor) sca.getItem(position);
    	mP2RowId = cursor.getLong(cursor.getColumnIndex(PlayerDbAdapter.KEY_ROWID));
    	
        
        String set1Result = "MMMMMMM";
        String set2Result = "MMMMMMM";
        String set3Result = "MMMMMMM";

        if (mRowId == null) {
            long id = mDbHelper.createMatch(mP1RowId, mP2RowId, set1Result, set2Result, set3Result);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateMatch(mRowId, mP1RowId, mP2RowId, set1Result, set2Result, set3Result);
        }
    }

    private void fillData() {
        Cursor playersCursor = playerDbHelper.fetchAllPlayers();
        startManagingCursor(playersCursor);
        
        playersCursor.moveToFirst();
        // make an adapter from the cursor
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{PlayerDbAdapter.KEY_NAME};
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.firstName};
        // Now create a simple cursor adapter and set it to display
        
        sca = new SimpleCursorAdapter(this, R.layout.player_row, playersCursor, from, to);

        // set layout for activated adapter
        sca.setDropDownViewResource(R.layout.player_row);    
        
        // get xml file spinner and set adapter 
        Spinner spinnerViewP1 = (Spinner) this.findViewById(R.id.spinnerPlayer1);
        Spinner spinnerViewP2 = (Spinner) this.findViewById(R.id.spinnerPlayer2);
        spinnerViewP1.setAdapter(sca);
        spinnerViewP2.setAdapter(sca);

        
        // set spinner listener to display the selected item id
        mContext = this;
        spinnerViewP1.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                Toast.makeText(mContext, "Selected ID=" + id, Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
            });      
    
    }    
       
}
