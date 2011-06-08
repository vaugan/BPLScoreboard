package com.vaugan.csf.match;

/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import com.vaugan.csf.match.MatchDbAdapter;
import com.vaugan.csf.match.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MatchCreate extends Activity {

    private EditText mDateTimeText;
    private EditText mVenueText;
    private EditText mBestOfText;
    private EditText mP1Text;
    private EditText mP2Text;
    private EditText mResultText;
    private Long mRowId;
    private MatchDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{ 
	        mDbHelper = new MatchDbAdapter(this);
	        mDbHelper.open();
	
	        setContentView(R.layout.match_edit);
	        setTitle(R.string.edit_match);
	
	        mDateTimeText = (EditText) findViewById(R.id.editDate);
	        mVenueText = (EditText)findViewById(R.id.editVenue);
	        mBestOfText = (EditText)findViewById(R.id.editMatchBestOf);
	        mP1Text = (EditText) findViewById(R.id.editPlayer1);
	        mP2Text = (EditText) findViewById(R.id.editPlayer2);
	        //mResultText = (EditText)"incomplete";
	
	        Button confirmButton = (Button) findViewById(R.id.btnStartMatch);
	
	        mRowId = (savedInstanceState == null) ? null :
	            (Long) savedInstanceState.getSerializable(MatchDbAdapter.KEY_ROWID);
			if (mRowId == null) {
				Bundle extras = getIntent().getExtras();
				mRowId = extras != null ? extras.getLong(MatchDbAdapter.KEY_ROWID)
										: null;
			}
	
			populateFields();
	
//	        confirmButton.setOnClickListener(new View.OnClickListener() {
//	
//	            public void onClick(View view) {
//	                setResult(RESULT_OK);
//	                finish();
//	            }
//	
//	        });
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
            mDateTimeText.setText(note.getString(
                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_DATETIME)));
            mVenueText.setText(note.getString(
                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_VENUE)));
            mBestOfText.setText(note.getString(
                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_BEST_OF)));
            mP1Text.setText(note.getString(
                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_P1)));
            mP2Text.setText(note.getString(
                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_P2)));
            mResultText.setText(note.getString(
                    note.getColumnIndexOrThrow(MatchDbAdapter.KEY_RESULT)));
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
        String dateTime = mDateTimeText.getText().toString();
        String venue = mVenueText.getText().toString();
        String bestof = mBestOfText.getText().toString();
        String p1 = mP1Text.getText().toString();
        String p2 = mP2Text.getText().toString();
        String result = "not finished";

        if (mRowId == null) {
            long id = mDbHelper.createMatch(dateTime, venue, bestof, p1, p2, result);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateMatch(mRowId, dateTime, venue, bestof, p1, p2, result);
        }
    }

}
