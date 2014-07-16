package com.vaugan.csf.match;

import com.vaugan.csf.match.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug.IntToString;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MatchDisplay extends Activity {

    private static final String TAG = "MatchDisplay";
    private EditText mTitleText;
    private EditText mBodyText;
    private EditText mCellText;
    private Long mRowId;
    private MatchDbAdapter mDbHelper;
    private TextView mSet1P1ResultString;
    private TextView mSet2P1ResultString;
    private TextView mSet3P1ResultString;
    private EditText mP1Text;
    private EditText mP2Text;
    
    //Set1
    private GridView gvS1P1FrameCodes;
    private GridView gvS1P2FrameCodes;
    private EditText etS1P1Score;
    private EditText etS1P2Score;

    //Set2
    private GridView gvS2P1FrameCodes;
    private GridView gvS2P2FrameCodes;
    private EditText etS2P1Score;
    private EditText etS2P2Score;

    //Set3
    private GridView gvS3P1FrameCodes;
    private GridView gvS3P2FrameCodes;
    private EditText etS3P1Score;
    private EditText etS3P2Score;
    
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

        mSet1P1ResultString = (TextView)findViewById(R.id.set1P1ResultString);
        mSet2P1ResultString = (TextView)findViewById(R.id.set2P1ResultString);
        mSet3P1ResultString = (TextView)findViewById(R.id.set3P1ResultString);
        mP1Text = (EditText) findViewById(R.id.player1name);
        mP2Text = (EditText) findViewById(R.id.player2name);

        //##############################
        //#####      Set 1         #####
        //##############################
        //Player1 Scorecard       
        etS1P1Score  = (EditText) findViewById(R.id.set1Player1Score);
        gvS1P1FrameCodes = (GridView) findViewById(R.id.set1Player1FrameCodes);
        gvS1P1FrameCodes.setNumColumns(ScoreCodeImageAdapter.MAXIMUM_FRAMES);
        gvS1P1FrameCodes.setAdapter(new ScoreCodeImageAdapter(MatchDisplay.this));
        ((ScoreCodeImageAdapter)gvS1P1FrameCodes.getAdapter()).resetScore();
        
        gvS1P1FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                
              Intent i = new Intent(MatchDisplay.this, FrameCodeChooser.class);
              i.putExtra("player", 1);
              i.putExtra("pos", position);
              i.putExtra("set", 1);
              startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });
        
        
        //Player2 Scorecard   
        etS1P2Score  = (EditText) findViewById(R.id.set1Player2Score);       
        gvS1P2FrameCodes = (GridView) findViewById(R.id.set1Player2FrameCodes);        
        gvS1P2FrameCodes.setNumColumns(ScoreCodeImageAdapter.MAXIMUM_FRAMES);
        gvS1P2FrameCodes.setAdapter(new ScoreCodeImageAdapter(MatchDisplay.this));
        ((ScoreCodeImageAdapter)gvS1P2FrameCodes.getAdapter()).resetScore();

        gvS1P2FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(MatchDisplay.this, FrameCodeChooser.class);
                i.putExtra("player", 2);
                i.putExtra("pos", position);
                i.putExtra("set", 1);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });

        //##############################
        //#####      Set 2         #####
        //##############################
        //Player1      
        etS2P1Score  = (EditText) findViewById(R.id.set2Player1Score);
        gvS2P1FrameCodes = (GridView) findViewById(R.id.set2Player1FrameCodes);
        gvS2P1FrameCodes.setNumColumns(ScoreCodeImageAdapter.MAXIMUM_FRAMES);
        gvS2P1FrameCodes.setAdapter(new ScoreCodeImageAdapter(MatchDisplay.this));
        ((ScoreCodeImageAdapter)gvS2P1FrameCodes.getAdapter()).resetScore();
        
        gvS2P1FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                
              Intent i = new Intent(MatchDisplay.this, FrameCodeChooser.class);
              i.putExtra("player", 1);
              i.putExtra("pos", position);
              i.putExtra("set", 2);
              startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });
        
        
        //Player2  
        etS2P2Score  = (EditText) findViewById(R.id.set2Player2Score);       
        gvS2P2FrameCodes = (GridView) findViewById(R.id.set2Player2FrameCodes);        
        gvS2P2FrameCodes.setNumColumns(ScoreCodeImageAdapter.MAXIMUM_FRAMES);
        gvS2P2FrameCodes.setAdapter(new ScoreCodeImageAdapter(MatchDisplay.this));
        ((ScoreCodeImageAdapter)gvS2P2FrameCodes.getAdapter()).resetScore();

        gvS2P2FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(MatchDisplay.this, FrameCodeChooser.class);
                i.putExtra("player", 2);
                i.putExtra("pos", position);
                i.putExtra("set", 2);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });

        
        
        //##############################
        //#####      Set 3         #####
        //##############################
        //Player1 Scorecard       
        etS3P1Score  = (EditText) findViewById(R.id.set3Player1Score);
        gvS3P1FrameCodes = (GridView) findViewById(R.id.set3Player1FrameCodes);
        gvS3P1FrameCodes.setNumColumns(ScoreCodeImageAdapter.MAXIMUM_FRAMES);
        gvS3P1FrameCodes.setAdapter(new ScoreCodeImageAdapter(MatchDisplay.this));
        ((ScoreCodeImageAdapter)gvS3P1FrameCodes.getAdapter()).resetScore();
        
        gvS3P1FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                
              Intent i = new Intent(MatchDisplay.this, FrameCodeChooser.class);
              i.putExtra("player", 1);
              i.putExtra("pos", position);
              i.putExtra("set", 3);
              startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });
        
        
        //Player2 Scorecard   
        etS3P2Score  = (EditText) findViewById(R.id.set3Player2Score);       
        gvS3P2FrameCodes = (GridView) findViewById(R.id.set3Player2FrameCodes);        
        gvS3P2FrameCodes.setNumColumns(ScoreCodeImageAdapter.MAXIMUM_FRAMES);
        gvS3P2FrameCodes.setAdapter(new ScoreCodeImageAdapter(MatchDisplay.this));
        ((ScoreCodeImageAdapter)gvS3P2FrameCodes.getAdapter()).resetScore();

        gvS3P2FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(MatchDisplay.this, FrameCodeChooser.class);
                i.putExtra("player", 2);
                i.putExtra("pos", position);
                i.putExtra("set", 3);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });
        
        
        if (mRowId != null) {
            Cursor temp_match = mDbHelper.fetchMatch(mRowId);
            startManagingCursor(temp_match);
            
            ((ScoreCodeImageAdapter)gvS1P1FrameCodes.getAdapter()).updateCurrentScoreArray(temp_match.getString(temp_match
                    .getColumnIndexOrThrow(MatchDbAdapter.KEY_SET1_RESULT)));       
            String inverseResultString = ScoreCodeImageAdapter.getInverseScoreArray(temp_match.getString(temp_match
                    .getColumnIndexOrThrow(MatchDbAdapter.KEY_SET1_RESULT)));
            ((ScoreCodeImageAdapter)gvS1P2FrameCodes.getAdapter()).updateCurrentScoreArray(inverseResultString);        


            temp_match.close();
        }

        populateFields() ;

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
        
    if (resultCode == Activity.RESULT_OK) {       
        
	    Bundle extras = intent.getExtras();
	    int player = extras.getInt("player");
	    int pos = extras.getInt("pos");
	    int set = extras.getInt("set");
	    int score=0;
	
	//    int imageid = extras.getInt("image_id");
	//   ImageView  Image_A= (ImageView) findViewById(imageid);
	//   Drawable image = Image_A.getDrawable();
	    Log.v(TAG, "Return this value from Frame Selector dialog"+extras.toString());
	    
	    switch(requestCode) {
	    case ACTIVITY_FRAME_IMAGE_SELECTOR:
	        int p1_icon_index = 8;
	        int p2_icon_index = 8;
	                
	        if (player == 1)
	        {
	            p1_icon_index = extras.getInt("selected_icon");
	            p2_icon_index = FrameCodeAPI.getInverseCodeInteger(p1_icon_index);
	        }
	        else
	        {
	            p2_icon_index = extras.getInt("selected_icon");
	            p1_icon_index = FrameCodeAPI.getInverseCodeInteger(p2_icon_index);
	        }
	            
			switch (set){
			case 1:
			    //Update P1 score
			    Log.v(TAG, "Returned from frameselector activity! p1_icon_index=" + p1_icon_index);   
			    score=((ScoreCodeImageAdapter)gvS1P1FrameCodes.getAdapter()).updateScore(pos, p1_icon_index);    
			    Log.v(TAG, "mS1P1Score=" + score);   
			    ((ScoreCodeImageAdapter)gvS1P1FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS1P1Score.setText(Integer.toString(score));
			
			    Log.v(TAG, "S1P1ResultString=" + ((ScoreCodeImageAdapter)gvS1P1FrameCodes.getAdapter()).getScoreString());   
			    mSet1P1ResultString.setText(((ScoreCodeImageAdapter)gvS1P1FrameCodes.getAdapter()).getScoreString());
			
			    //Update P2 score
			    Log.v(TAG, "Returned from frameselector activity! p2_icon_index=" + p2_icon_index);   
			    score= ((ScoreCodeImageAdapter)gvS1P2FrameCodes.getAdapter()).updateScore(pos, p2_icon_index);    
			    Log.v(TAG, "mP2Score=" + score);   
			    ((ScoreCodeImageAdapter)gvS1P2FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS1P2Score.setText(Integer.toString(score));
				break;
			case 2:
			    //Update P1 score
			    Log.v(TAG, "Returned from frameselector activity! p1_icon_index=" + p1_icon_index);   
			    score=((ScoreCodeImageAdapter)gvS2P1FrameCodes.getAdapter()).updateScore(pos, p1_icon_index);    
			    Log.v(TAG, "mS2P1Score=" + score);   
			    ((ScoreCodeImageAdapter)gvS2P1FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS2P1Score.setText(Integer.toString(score));
			
			    Log.v(TAG, "S2P1ResultString=" + ((ScoreCodeImageAdapter)gvS2P1FrameCodes.getAdapter()).getScoreString());   
			    mSet2P1ResultString.setText(((ScoreCodeImageAdapter)gvS2P1FrameCodes.getAdapter()).getScoreString());
			
			    //Update P2 score
			    Log.v(TAG, "Returned from frameselector activity! p2_icon_index=" + p2_icon_index);   
			    score= ((ScoreCodeImageAdapter)gvS2P2FrameCodes.getAdapter()).updateScore(pos, p2_icon_index);    
			    Log.v(TAG, "mP2Score=" + score);   
			    ((ScoreCodeImageAdapter)gvS2P2FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS2P2Score.setText(Integer.toString(score));
				break;
			case 3:
			    //Update P1 score
			    Log.v(TAG, "Returned from frameselector activity! p1_icon_index=" + p1_icon_index);   
			    score=((ScoreCodeImageAdapter)gvS3P1FrameCodes.getAdapter()).updateScore(pos, p1_icon_index);    
			    Log.v(TAG, "mS3P1Score=" + score);   
			    ((ScoreCodeImageAdapter)gvS3P1FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS3P1Score.setText(Integer.toString(score));
			
			    Log.v(TAG, "S3P1ResultString=" + ((ScoreCodeImageAdapter)gvS3P1FrameCodes.getAdapter()).getScoreString());   
			    mSet3P1ResultString.setText(((ScoreCodeImageAdapter)gvS3P1FrameCodes.getAdapter()).getScoreString());
			
			    //Update P2 score
			    Log.v(TAG, "Returned from frameselector activity! p2_icon_index=" + p2_icon_index);   
			    score= ((ScoreCodeImageAdapter)gvS3P2FrameCodes.getAdapter()).updateScore(pos, p2_icon_index);    
			    Log.v(TAG, "mP2Score=" + score);   
			    ((ScoreCodeImageAdapter)gvS3P2FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS3P2Score.setText(Integer.toString(score));
			    break;
			
			default:
				break;
			}
	
	
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
        else
	   {
	       Log.v(TAG, "Returned from frameselector activity! ERROR!!!! resultCode["+resultCode);              
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
    
    @Override
    protected void onDestroy() {
        super.onResume();
        mDbHelper.close();
    }


    
    private void saveState() {
        String set1Result = ((ScoreCodeImageAdapter)gvS1P1FrameCodes.getAdapter()).getScoreString();
        String set2Result = ((ScoreCodeImageAdapter)gvS2P1FrameCodes.getAdapter()).getScoreString();
        String set3Result = ((ScoreCodeImageAdapter)gvS3P1FrameCodes.getAdapter()).getScoreString();

        if (mRowId != null) {
            mDbHelper.updateMatchResult(mRowId, set1Result, set2Result, set3Result);
        }
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

            //Set 1
            mSet1P1ResultString.setText(match.getString(match
                    .getColumnIndexOrThrow(MatchDbAdapter.KEY_SET1_RESULT)));   

            ((ScoreCodeImageAdapter)gvS1P1FrameCodes.getAdapter()).notifyDataSetChanged();
            int score=((ScoreCodeImageAdapter)gvS1P1FrameCodes.getAdapter()).getScoreInteger();    
            etS1P1Score.setText(Integer.toString(score));
            
            score=((ScoreCodeImageAdapter)gvS1P2FrameCodes.getAdapter()).getScoreInteger();    
            etS1P2Score.setText(Integer.toString(score));

            //Set 2
            mSet2P1ResultString.setText(match.getString(match
                    .getColumnIndexOrThrow(MatchDbAdapter.KEY_SET2_RESULT)));   

            ((ScoreCodeImageAdapter)gvS2P1FrameCodes.getAdapter()).notifyDataSetChanged();
             score=((ScoreCodeImageAdapter)gvS2P1FrameCodes.getAdapter()).getScoreInteger();    
            etS2P1Score.setText(Integer.toString(score));
            
            score=((ScoreCodeImageAdapter)gvS2P2FrameCodes.getAdapter()).getScoreInteger();    
            etS2P2Score.setText(Integer.toString(score));

            //Set 3
            mSet3P1ResultString.setText(match.getString(match
                    .getColumnIndexOrThrow(MatchDbAdapter.KEY_SET3_RESULT)));   

            ((ScoreCodeImageAdapter)gvS3P1FrameCodes.getAdapter()).notifyDataSetChanged();
             score=((ScoreCodeImageAdapter)gvS3P1FrameCodes.getAdapter()).getScoreInteger();    
            etS3P1Score.setText(Integer.toString(score));
            
            score=((ScoreCodeImageAdapter)gvS3P2FrameCodes.getAdapter()).getScoreInteger();    
            etS3P2Score.setText(Integer.toString(score));

            match.close();
        }
    }

}
