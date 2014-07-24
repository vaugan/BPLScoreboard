package com.vaugan.bpl.view;

import com.vaugan.bpl.R;
import com.vaugan.bpl.model.FrameCodeAPI;
import com.vaugan.bpl.model.IBPLConstants;
import com.vaugan.bpl.model.MatchDbAdapter;
import com.vaugan.bpl.model.PlayerDbAdapter;
import com.vaugan.bpl.model.SetLogic;
import com.vaugan.bpl.model.MatchLogic;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MatchDisplay extends Activity {

    private static final String TAG = "MatchDisplay";
    private Long mRowId;
    private Long mP1RowId;
    private Long mP2RowId;
    private MatchDbAdapter mDbHelper;
	private PlayerDbAdapter playerDbHelper;    
    private EditText mP1Text;
    private EditText mP2Text;
    
//    Model Stuff - move out later
    int p1SetScore=0;
    int p2SetScore=0;

    
    //Array of P1 and P2 sets
    private SetLogic aP1Sets[] = new SetLogic[IBPLConstants.MAX_SETS_IN_MATCH];
    private SetLogic aP2Sets[] = new SetLogic[IBPLConstants.MAX_SETS_IN_MATCH];
    
    private View aSetsUI[] = new View[IBPLConstants.MAX_SETS_IN_MATCH];
    
//    Match
    private TextView p1MatchScore;
    private TextView p2MatchScore;
    private TextView p1CurrentSetScore;
    private TextView p2CurrentSetScore;
    
    
    //Set1
    private GridView gvS1P1FrameCodes;
    private GridView gvS1P2FrameCodes;
    private TextView etS1P1Score;
    private TextView etS1P2Score;

    //Set2
    private GridView gvS2P1FrameCodes;
    private GridView gvS2P2FrameCodes;
    private TextView etS2P1Score;
    private TextView etS2P2Score;

    //Set3
    private GridView gvS3P1FrameCodes;
    private GridView gvS3P2FrameCodes;
    private TextView etS3P1Score;
    private TextView etS3P2Score;
    
	private ImageView mP1Image;
	private ImageView mP2Image;

    
    protected static final int  ACTIVITY_FRAME_IMAGE_SELECTOR = 5;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new MatchDbAdapter(this);
        mDbHelper.open();

        //Populate player name and picture
        playerDbHelper = new PlayerDbAdapter(this);
        playerDbHelper.open();
        
        Bundle extras = this.getIntent().getExtras();
        mRowId = extras.getLong("com.vaugan.csf.match.rowid");
        mP1RowId = extras.getLong("com.vaugan.csf.match.p1rowid");
        mP2RowId = extras.getLong("com.vaugan.csf.match.p2rowid");

        for (int i=0;i<IBPLConstants.MAX_SETS_IN_MATCH;i++)
        {
        	aP1Sets[i] = new SetLogic(MatchDisplay.this);
        	aP2Sets[i] = new SetLogic(MatchDisplay.this);

        }
        aSetsUI[0] = (LinearLayout) findViewById(R.id.Set1);
        aSetsUI[1] = (LinearLayout) findViewById(R.id.Set2);
        aSetsUI[2] = (LinearLayout) findViewById(R.id.Set3);
        
        Log.v(TAG, "mRowId="+mRowId);
        
        setContentView(R.layout.match_display);
        setTitle(R.string.edit_note);

        //##############################
        //#####    Match Fields    #####
        //##############################
        mP1Text = (EditText) findViewById(R.id.player1name);
        mP2Text = (EditText) findViewById(R.id.player2name);
        mP1Image = (ImageView) findViewById(R.id.player1Image);
        mP2Image = (ImageView) findViewById(R.id.player2Image);
        p1MatchScore = (TextView) findViewById(R.id.matchP1Score);
        p2MatchScore = (TextView) findViewById(R.id.matchP2Score);
        p1CurrentSetScore = (TextView) findViewById(R.id.currentSetP1Score);
        p2CurrentSetScore = (TextView) findViewById(R.id.currentSetP2Score);
        

        //##############################
        //#####      Set 1         #####
        //##############################
        //Player1 Scorecard       
        etS1P1Score  = (TextView) findViewById(R.id.set1Player1Score);
        gvS1P1FrameCodes = (GridView) findViewById(R.id.set1Player1FrameCodes);
        gvS1P1FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS1P1FrameCodes.setAdapter(aP1Sets[0]);
        ((SetLogic)gvS1P1FrameCodes.getAdapter()).resetScore();
        
        gvS1P1FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                
              Intent i = new Intent(MatchDisplay.this, FrameCodeSelector.class);
              i.putExtra("player", 1);
              i.putExtra("pos", position);
              i.putExtra("set", 0);
              startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });
        
        
        //Player2 Scorecard   
        etS1P2Score  = (TextView) findViewById(R.id.set1Player2Score);       
        gvS1P2FrameCodes = (GridView) findViewById(R.id.set1Player2FrameCodes);        
        gvS1P2FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS1P2FrameCodes.setAdapter(aP2Sets[0]);
        ((SetLogic)gvS1P2FrameCodes.getAdapter()).resetScore();

        gvS1P2FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(MatchDisplay.this, FrameCodeSelector.class);
                i.putExtra("player", 2);
                i.putExtra("pos", position);
                i.putExtra("set", 0);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });

        //##############################
        //#####      Set 2         #####
        //##############################
        //Player1      
        etS2P1Score  = (TextView) findViewById(R.id.set2Player1Score);
        gvS2P1FrameCodes = (GridView) findViewById(R.id.set2Player1FrameCodes);
        gvS2P1FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS2P1FrameCodes.setAdapter(aP1Sets[1]);
        ((SetLogic)gvS2P1FrameCodes.getAdapter()).resetScore();
        
        gvS2P1FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                
              Intent i = new Intent(MatchDisplay.this, FrameCodeSelector.class);
              i.putExtra("player", 1);
              i.putExtra("pos", position);
              i.putExtra("set", 1);
              startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });
        
        
        //Player2  
        etS2P2Score  = (TextView) findViewById(R.id.set2Player2Score);       
        gvS2P2FrameCodes = (GridView) findViewById(R.id.set2Player2FrameCodes);        
        gvS2P2FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS2P2FrameCodes.setAdapter(aP2Sets[1]);
        ((SetLogic)gvS2P2FrameCodes.getAdapter()).resetScore();

        gvS2P2FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(MatchDisplay.this, FrameCodeSelector.class);
                i.putExtra("player", 2);
                i.putExtra("pos", position);
                i.putExtra("set", 1);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });

        
        
        //##############################
        //#####      Set 3         #####
        //##############################
        //Player1 Scorecard       
        etS3P1Score  = (TextView) findViewById(R.id.set3Player1Score);
        gvS3P1FrameCodes = (GridView) findViewById(R.id.set3Player1FrameCodes);
        gvS3P1FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS3P1FrameCodes.setAdapter(aP1Sets[2]);
        ((SetLogic)gvS3P1FrameCodes.getAdapter()).resetScore();
        
        gvS3P1FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                
              Intent i = new Intent(MatchDisplay.this, FrameCodeSelector.class);
              i.putExtra("player", 1);
              i.putExtra("pos", position);
              i.putExtra("set", 2);
              startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });
        
        
        //Player2 Scorecard   
        etS3P2Score  = (TextView) findViewById(R.id.set3Player2Score);       
        gvS3P2FrameCodes = (GridView) findViewById(R.id.set3Player2FrameCodes);        
        gvS3P2FrameCodes.setNumColumns(IBPLConstants.MAX_FRAMES_IN_SET);
        gvS3P2FrameCodes.setAdapter(aP2Sets[2]);
        ((SetLogic)gvS3P2FrameCodes.getAdapter()).resetScore();

        gvS3P2FrameCodes.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(MatchDisplay.this, FrameCodeSelector.class);
                i.putExtra("player", 2);
                i.putExtra("pos", position);
                i.putExtra("set", 2);
                startActivityForResult(i, ACTIVITY_FRAME_IMAGE_SELECTOR);                   
            }
        });

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
			case IBPLConstants.SET_ONE:
			    //Update P1 score
			    Log.v(TAG, "Returned from frameselector activity! p1_icon_index=" + p1_icon_index);
			    p1SetScore=((SetLogic)gvS1P1FrameCodes.getAdapter()).updateScore(pos, p1_icon_index);
			    Log.v(TAG, "mS1P1Score=" + p1SetScore);
			    ((SetLogic)gvS1P1FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS1P1Score.setText(Integer.toString(p1SetScore));
			
			    Log.v(TAG, "S1P1ResultString=" + ((SetLogic)gvS1P1FrameCodes.getAdapter()).getScoreString());
			
			    //Update P2 score
			    Log.v(TAG, "Returned from frameselector activity! p2_icon_index=" + p2_icon_index);
			    p2SetScore= ((SetLogic)gvS1P2FrameCodes.getAdapter()).updateScore(pos, p2_icon_index);
			    Log.v(TAG, "mP2Score=" + p2SetScore);   
			    ((SetLogic)gvS1P2FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS1P2Score.setText(Integer.toString(p2SetScore));
				break;
			case IBPLConstants.SET_TWO:
			    //Update P1 score
			    Log.v(TAG, "Returned from frameselector activity! p1_icon_index=" + p1_icon_index);
			    p1SetScore=((SetLogic)gvS2P1FrameCodes.getAdapter()).updateScore(pos, p1_icon_index);
			    Log.v(TAG, "mS2P1Score=" + p1SetScore);
			    ((SetLogic)gvS2P1FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS2P1Score.setText(Integer.toString(p1SetScore));
			    Log.v(TAG, "S2P1ResultString=" + ((SetLogic)gvS2P1FrameCodes.getAdapter()).getScoreString());
			
			    //Update P2 score
			    Log.v(TAG, "Returned from frameselector activity! p2_icon_index=" + p2_icon_index);
			    p2SetScore= ((SetLogic)gvS2P2FrameCodes.getAdapter()).updateScore(pos, p2_icon_index);
			    Log.v(TAG, "mP2Score=" + p2SetScore);
			    ((SetLogic)gvS2P2FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS2P2Score.setText(Integer.toString(p2SetScore));
				break;
				
			case IBPLConstants.SET_THREE:
			    //Update P1 score
			    Log.v(TAG, "Returned from frameselector activity! p1_icon_index=" + p1_icon_index);
			    p1SetScore=((SetLogic)gvS3P1FrameCodes.getAdapter()).updateScore(pos, p1_icon_index);
			    Log.v(TAG, "mS3P1Score=" + p1SetScore);
			    ((SetLogic)gvS3P1FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS3P1Score.setText(Integer.toString(p1SetScore));
			    Log.v(TAG, "S3P1ResultString=" + ((SetLogic)gvS3P1FrameCodes.getAdapter()).getScoreString());
			
			    //Update P2 score
			    Log.v(TAG, "Returned from frameselector activity! p2_icon_index=" + p2_icon_index);
			    p2SetScore= ((SetLogic)gvS3P2FrameCodes.getAdapter()).updateScore(pos, p2_icon_index);
			    Log.v(TAG, "mP2Score=" + p2SetScore);
			    ((SetLogic)gvS3P2FrameCodes.getAdapter()).notifyDataSetChanged();
			    etS3P2Score.setText(Integer.toString(p2SetScore));
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
	    
	    	updateSetVisibility(set);
	    	updateMatchStatus();
        }
        else
	   {
	       Log.v(TAG, "Returned from frameselector activity! ERROR!!!! resultCode["+resultCode);              
	   }        
    }

	private void updateMatchStatus() {
//		if (MatchLogic.isMatchOver(aP1Sets, aP2Sets)) {
//			// Match is over so disable everything and put up message.
//			Toast.makeText(getApplicationContext(), "Game Over!",
//					Toast.LENGTH_LONG);
//		}

		p1CurrentSetScore.setText(Integer.toString(p1SetScore));
		p2CurrentSetScore.setText(Integer.toString(p2SetScore));

		p1CurrentSetScore.setAnimation(AnimationUtils.loadAnimation(MatchDisplay.this, android.R.anim.fade_in));
		p2CurrentSetScore.setAnimation(AnimationUtils.loadAnimation(MatchDisplay.this, android.R.anim.fade_in));

		p1MatchScore.setText(Integer.toString(MatchLogic.getMatchScore(aP1Sets)));
		p2MatchScore.setText(Integer.toString(MatchLogic.getMatchScore(aP2Sets)));
		
	}

	private void updateSetVisibility(int set) {

		//TODO: Enumerate these sets or create a fragment for sets to make this code more efficient
		
		switch (set) {
		case IBPLConstants.SET_ONE:
			if ((((SetLogic) gvS1P1FrameCodes.getAdapter()).getScoreInteger() == IBPLConstants.FRAMES_TO_WIN_SET)
					|| (((SetLogic) gvS1P2FrameCodes.getAdapter())
							.getScoreInteger() == IBPLConstants.FRAMES_TO_WIN_SET)) {
				Log.v(TAG, "Set 1 is won by a player. Disabling input...");
				LinearLayout myLayout = (LinearLayout) findViewById(R.id.Set1);
				for (int i = 0; i < myLayout.getChildCount(); i++) {
					View view = myLayout.getChildAt(i);
					view.setEnabled(false); // Or whatever you want to do with
											// the view.
				}

				myLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Toast.makeText(getApplicationContext(),
								"This set is over", Toast.LENGTH_SHORT).show();

					}
				});

				findViewById(R.id.Set2).setVisibility(View.VISIBLE);
				
				//Reset the current set scoreboard
				p1SetScore=0;
				p2SetScore=0;
			}

			break;
		case IBPLConstants.SET_TWO:
			if ((((SetLogic) gvS2P1FrameCodes.getAdapter()).getScoreInteger() == IBPLConstants.FRAMES_TO_WIN_SET)
					|| (((SetLogic) gvS2P2FrameCodes.getAdapter())
							.getScoreInteger() == IBPLConstants.FRAMES_TO_WIN_SET)) {
				LinearLayout myLayout = (LinearLayout) findViewById(R.id.Set2);
				for (int i = 0; i < myLayout.getChildCount(); i++) {
					View view = myLayout.getChildAt(i);
					view.setEnabled(false); // Or whatever you want to do with
											// the view.
				}

				myLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Toast.makeText(getApplicationContext(),
								"This set is over", Toast.LENGTH_SHORT).show();

					}
				});
				Log.v(TAG, "Set 2 is won by a player. Disabling input...");
				
				if ((MatchLogic.getMatchScore(aP1Sets) == 2)
						|| (MatchLogic.getMatchScore(aP1Sets) == 2)) {

					// match is over
				} else {
					findViewById(R.id.Set3).setVisibility(View.VISIBLE);
					// Reset the current set scoreboard
					p1SetScore = 0;
					p2SetScore = 0;
				}
			}

			break;
		case IBPLConstants.SET_THREE:
			if ((((SetLogic) gvS3P1FrameCodes.getAdapter()).getScoreInteger() == IBPLConstants.FRAMES_TO_WIN_SET)
					|| (((SetLogic) gvS3P2FrameCodes.getAdapter())
							.getScoreInteger() == IBPLConstants.FRAMES_TO_WIN_SET)) {

				LinearLayout myLayout = (LinearLayout) findViewById(R.id.Set3);
				for (int i = 0; i < myLayout.getChildCount(); i++) {
					View view = myLayout.getChildAt(i);
					view.setEnabled(false); // Or whatever you want to do with
											// the view.
				}

				myLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Toast.makeText(getApplicationContext(),
								"This set is over", Toast.LENGTH_SHORT).show();

					}
				});
				Log.v(TAG,
						"Set 3 is won by a player. Disabling input... The Match is over!");
			}

			break;

		default:
			break;
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
        String set1Result = ((SetLogic)gvS1P1FrameCodes.getAdapter()).getScoreString();
        String set2Result = ((SetLogic)gvS2P1FrameCodes.getAdapter()).getScoreString();
        String set3Result = ((SetLogic)gvS3P1FrameCodes.getAdapter()).getScoreString();

        if (mRowId != null) {
            mDbHelper.updateMatchResult(mRowId, set1Result, set2Result, set3Result);
        }
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor match = mDbHelper.fetchMatch(mRowId);
            
            
            Cursor p1Cursor = playerDbHelper.fetchPlayer(mP1RowId);
            Cursor p2Cursor = playerDbHelper.fetchPlayer(mP2RowId);
            
            startManagingCursor(match);
            Log.v(TAG, "Player1="+match.getString(match.getColumnIndexOrThrow(MatchDbAdapter.KEY_P1)));
            
            
            mP1Text.setText(p1Cursor.getString(p1Cursor
                    .getColumnIndexOrThrow(PlayerDbAdapter.KEY_NAME)));
            mP2Text.setText(p2Cursor.getString(p2Cursor
                    .getColumnIndexOrThrow(PlayerDbAdapter.KEY_NAME)));

            byte[] byteArray = p1Cursor.getBlob(p1Cursor.getColumnIndexOrThrow(PlayerDbAdapter.KEY_PICTURE));
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            mP1Image.setImageBitmap(bmp);
            
            byteArray = p2Cursor.getBlob(p2Cursor.getColumnIndexOrThrow(PlayerDbAdapter.KEY_PICTURE));
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            mP2Image.setImageBitmap(bmp);

            //Set 1
            ((SetLogic)gvS1P1FrameCodes.getAdapter()).notifyDataSetChanged();
            int score=((SetLogic)gvS1P1FrameCodes.getAdapter()).getScoreInteger();    
            etS1P1Score.setText(Integer.toString(score));
            
            score=((SetLogic)gvS1P2FrameCodes.getAdapter()).getScoreInteger();    
            etS1P2Score.setText(Integer.toString(score));

            //Set 2
            ((SetLogic)gvS2P1FrameCodes.getAdapter()).notifyDataSetChanged();
             score=((SetLogic)gvS2P1FrameCodes.getAdapter()).getScoreInteger();    
            etS2P1Score.setText(Integer.toString(score));
            
            score=((SetLogic)gvS2P2FrameCodes.getAdapter()).getScoreInteger();    
            etS2P2Score.setText(Integer.toString(score));

            //Set 3
            ((SetLogic)gvS3P1FrameCodes.getAdapter()).notifyDataSetChanged();
             score=((SetLogic)gvS3P1FrameCodes.getAdapter()).getScoreInteger();    
            etS3P1Score.setText(Integer.toString(score));
            
            score=((SetLogic)gvS3P2FrameCodes.getAdapter()).getScoreInteger();    
            etS3P2Score.setText(Integer.toString(score));

            match.close();
        }
    }

}
