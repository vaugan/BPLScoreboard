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

import com.vaugan.csf.match.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MatchDisplay extends Activity {

    private static final String TAG = "MatchDisplay";
    private EditText mTitleText;

    private EditText mBodyText;

    private EditText mCellText;

    private Long mRowId;

    private MatchDbAdapter mDbHelper;

    private EditText mDateTimeText;

    private EditText mVenueText;

    private EditText mBestOfText;

    private EditText mP1Text;

    private EditText mP2Text;

    private EditText mResultText;
    private ImageView P1_frame1;
    protected static final int  ACTIVITY_FRAME_IMAGE_SELECTOR = 5;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new MatchDbAdapter(this);
        mDbHelper.open();

        Bundle extras = this.getIntent().getExtras();
        mRowId = extras.getLong("com.vaugan.csf.match.rowid");
        
        
        Log.v(TAG, "mRowId="+mRowId);
        
        setContentView(R.layout.match_display);
        setTitle(R.string.edit_note);

//        mDateTimeText = (EditText) findViewById(R.id.editDate);
//        mVenueText = (EditText)findViewById(R.id.editVenue);
//        mBestOfText = (EditText)findViewById(R.id.editMatchBestOf);
        mP1Text = (EditText) findViewById(R.id.player1name);
        mP2Text = (EditText) findViewById(R.id.player2name);
        
        populateFields() ;
        
        P1_frame1 = (ImageView) findViewById(R.id.P1_frame1);      
        P1_frame1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(MatchDisplay.this, FrameCodeChooser.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);        
            
                setResult(RESULT_OK);
               // finish();
            }
        });           
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
    Bundle extras = intent.getExtras();
//    int imageid = extras.getInt("image_id");
//   ImageView  Image_A= (ImageView) findViewById(imageid);
//   Drawable image = Image_A.getDrawable();
    Log.v(TAG, "Return this value from Frame Selector dialog"+extras.toString());
    
    switch(requestCode) {
    case ACTIVITY_FRAME_IMAGE_SELECTOR:
        Log.v(TAG, "Returned from frameselector activity! ");   
//        P1_frame1.setImageDrawable(image);
//        P1_frame1.setImageDrawable(extras.getString);

        break;
//    case ACTIVITY_EDIT:
//        Long mRowId = extras.getLong(NotesDbAdapter.KEY_ROWID);
//        if (mRowId != null) {
//            String editTitle = extras.getString(NotesDbAdapter.KEY_TITLE);
//            String editBody = extras.getString(NotesDbAdapter.KEY_BODY);
//            mDbHelper.updateNote(mRowId, editTitle, editBody);
//        }
//        fillData();
//        break;
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // saveState();
        // outState.putSerializable(MatchDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // populateFields();
    }


    
    private void saveState() {
        // String title = mTitleText.getText().toString();
        // String body = mBodyText.getText().toString();
        // String cell = mCellText.getText().toString();
        //
        // if (mRowId == null) {
        // long id = mDbHelper.createNote(title, body, cell);
        // if (id > 0) {
        // mRowId = id;
        // }
        // } else {
        // mDbHelper.updateNote(mRowId, title, body, cell);
        // }
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor match = mDbHelper.fetchMatch(mRowId);
            startManagingCursor(match);
            // mDateTimeText.setText(note.getString(
            // note.getColumnIndexOrThrow(MatchDbAdapter.KEY_DATETIME)));
            // mVenueText.setText(note.getString(
            // note.getColumnIndexOrThrow(MatchDbAdapter.KEY_VENUE)));
            // mBestOfText.setText(note.getString(
            // note.getColumnIndexOrThrow(MatchDbAdapter.KEY_BEST_OF)));
            Log.v(TAG, "Player1="+match.getString(match.getColumnIndexOrThrow(MatchDbAdapter.KEY_P1)));
            mP1Text.setText(match.getString(match
                    .getColumnIndexOrThrow(MatchDbAdapter.KEY_P1)));
            mP2Text.setText(match.getString(match
                    .getColumnIndexOrThrow(MatchDbAdapter.KEY_P2)));
            // mResultText.setText(note.getString(
            // note.getColumnIndexOrThrow(MatchDbAdapter.KEY_RESULT)));
        }
    }

}
