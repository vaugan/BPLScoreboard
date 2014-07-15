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

    private TextView mResultText;

    private EditText mP1Text;

    private EditText mP2Text;

    private GridView gridviewPlayer1Scorecard;
    private GridView gridviewPlayer2Scorecard;

    private EditText mP1Score;
    private EditText mP2Score;

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

        mResultText = (TextView)findViewById(R.id.tvP1ResultString);
        mP1Text = (EditText) findViewById(R.id.player1name);
        mP2Text = (EditText) findViewById(R.id.player2name);

        mP1Score  = (EditText) findViewById(R.id.player1score);
        mP2Score  = (EditText) findViewById(R.id.player2score);       

        //Player1 Scorecard       
        gridviewPlayer1Scorecard = (GridView) findViewById(R.id.gvPlayer1);
        gridviewPlayer1Scorecard.setNumColumns(ScoreCodeImageAdapter.MAXIMUM_FRAMES/2);
        gridviewPlayer1Scorecard.setAdapter(new ScoreCodeImageAdapter(MatchDisplay.this));
        ((ScoreCodeImageAdapter)gridviewPlayer1Scorecard.getAdapter()).resetScore();
        
        gridviewPlayer1Scorecard.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                
              Intent i = new Intent(MatchDisplay.this, FrameCodeChooser.class);
              i.putExtra("player", 1);
              i.putExtra("pos", position);
              startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });
        
        
        //Player1 Scorecard   
        gridviewPlayer2Scorecard = (GridView) findViewById(R.id.gvPlayer2);        
        gridviewPlayer2Scorecard.setNumColumns(ScoreCodeImageAdapter.MAXIMUM_FRAMES/2);
        gridviewPlayer2Scorecard.setAdapter(new ScoreCodeImageAdapter(MatchDisplay.this));
        ((ScoreCodeImageAdapter)gridviewPlayer2Scorecard.getAdapter()).resetScore();

        gridviewPlayer2Scorecard.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(MatchDisplay.this, FrameCodeChooser.class);
                i.putExtra("player", 2);
                i.putExtra("pos", position);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });
        
        if (mRowId != null) {
            Cursor temp_match = mDbHelper.fetchMatch(mRowId);
            startManagingCursor(temp_match);
            
            ((ScoreCodeImageAdapter)gridviewPlayer1Scorecard.getAdapter()).updateCurrentScoreArray(temp_match.getString(temp_match
                    .getColumnIndexOrThrow(MatchDbAdapter.KEY_RESULT)));       
            String inverseResultString = ScoreCodeImageAdapter.getInverseScoreArray(temp_match.getString(temp_match
                    .getColumnIndexOrThrow(MatchDbAdapter.KEY_RESULT)));
            ((ScoreCodeImageAdapter)gridviewPlayer2Scorecard.getAdapter()).updateCurrentScoreArray(inverseResultString);        
            
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
                
                //Update P1 score
                Log.v(TAG, "Returned from frameselector activity! p1_icon_index=" + p1_icon_index);   
                score=((ScoreCodeImageAdapter)gridviewPlayer1Scorecard.getAdapter()).updateScore(pos, p1_icon_index);    
                Log.v(TAG, "mP1Score=" + score);   
                ((ScoreCodeImageAdapter)gridviewPlayer1Scorecard.getAdapter()).notifyDataSetChanged();
                mP1Score.setText(Integer.toString(score));

                Log.v(TAG, "P1ResultString=" + ((ScoreCodeImageAdapter)gridviewPlayer1Scorecard.getAdapter()).getScoreString());   
                mResultText.setText(((ScoreCodeImageAdapter)gridviewPlayer1Scorecard.getAdapter()).getScoreString());

                //Update P2 score
                Log.v(TAG, "Returned from frameselector activity! p2_icon_index=" + p2_icon_index);   
                score= ((ScoreCodeImageAdapter)gridviewPlayer2Scorecard.getAdapter()).updateScore(pos, p2_icon_index);    
                Log.v(TAG, "mP2Score=" + score);   
                ((ScoreCodeImageAdapter)gridviewPlayer2Scorecard.getAdapter()).notifyDataSetChanged();
                mP2Score.setText(Integer.toString(score));


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
        String result = ((ScoreCodeImageAdapter)gridviewPlayer1Scorecard.getAdapter()).getScoreString();

        if (mRowId != null) {
            mDbHelper.updateMatchResult(mRowId, result);
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

            mResultText.setText(match.getString(match
                    .getColumnIndexOrThrow(MatchDbAdapter.KEY_RESULT)));   
            
           // ((ScoreCodeImageAdapter)gridviewPlayer1Scorecard.getAdapter()).updateCurrentScoreArray(mResultText.getText().toString());

            ((ScoreCodeImageAdapter)gridviewPlayer1Scorecard.getAdapter()).notifyDataSetChanged();
            int score=((ScoreCodeImageAdapter)gridviewPlayer1Scorecard.getAdapter()).getScoreInteger();    
            mP1Score.setText(Integer.toString(score));
            
            score=((ScoreCodeImageAdapter)gridviewPlayer2Scorecard.getAdapter()).getScoreInteger();    
            mP2Score.setText(Integer.toString(score));

            match.close();
        }
    }

}
