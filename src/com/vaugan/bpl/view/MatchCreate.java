package com.vaugan.bpl.view;

import com.vaugan.bpl.R;
import com.vaugan.bpl.model.MatchDbAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MatchCreate extends Activity {
    
    private static final String TAG = "MatchCreate";
//    private EditText mDateTimeText;
//    private EditText mVenueText;
//    private EditText mBestOfText;
    private EditText mP1Text;
    private EditText mP2Text;
    private EditText mSet1Result;
//    private EditText mSet2Result;
//    private EditText mSet3Result;
    private Long mRowId;
    private MatchDbAdapter mDbHelper;
 
    protected static final int ACTIVITY_MATCH_DISPLAY = 0;
    protected static final int ACTIVITY_MAIN_MENU = 4;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{ 
	        mDbHelper = new MatchDbAdapter(this);
	        mDbHelper.open();
	
	        setContentView(R.layout.match_create);
	        setTitle(R.string.create_match);
	
//	        mDateTimeText = (EditText) findViewById(R.id.editDate);
//	        mVenueText = (EditText)findViewById(R.id.editVenue);
//	        mBestOfText = (EditText)findViewById(R.id.editMatchBestOf);
	        mP1Text = (EditText) findViewById(R.id.editPlayer1);
	        mP2Text = (EditText) findViewById(R.id.editPlayer2);
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
            mP1Text.setText(note.getString(
                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_P1)));
            mP2Text.setText(note.getString(
                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_P2)));
            mSet1Result.setText(note.getString(
                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_SET1_RESULT)));
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
        String p1 = mP1Text.getText().toString();
        String p2 = mP2Text.getText().toString();
        String set1Result = "MMMMMMM";
        String set2Result = "MMMMMMM";
        String set3Result = "MMMMMMM";

        if (mRowId == null) {
            long id = mDbHelper.createMatch( p1, p2, set1Result, set2Result, set3Result);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateMatch(mRowId, p1, p2, set1Result, set2Result, set3Result);
        }
    }

}
