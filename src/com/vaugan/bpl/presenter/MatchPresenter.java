package com.vaugan.bpl.presenter;

import com.vaugan.bpl.R;
import com.vaugan.bpl.model.FrameCodeAPI;
import com.vaugan.bpl.model.FrameCodeImageAdapter;
import com.vaugan.bpl.model.IBPLConstants;
import com.vaugan.bpl.model.MatchDbAdapter;
import com.vaugan.bpl.model.PlayerDbAdapter;
import com.vaugan.bpl.model.SetLogic;
import com.vaugan.bpl.model.MatchLogic;
import com.vaugan.bpl.view.MatchDisplay;

import android.app.Activity;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Separate the model from the view via a presenter layer.
 * 
 * This class handles all the mapping from the MatchDisplay view to the underlying models.
 * TODO: Consider splitting this into multiple classes
 * 
 * @author Vaugan.Nayagar
 *
 */
public class MatchPresenter{
	
	static MatchPresenter instance = null;
    private static final String TAG = "MatchPresenter";
	private static MatchDbAdapter matchesDbHelper;
	private static PlayerDbAdapter playerDbHelper;
	// Array of P1 and P2 sets
	private static SetLogic aP1Sets[] = new SetLogic[IBPLConstants.MAX_SETS_IN_MATCH];
	private static SetLogic aP2Sets[] = new SetLogic[IBPLConstants.MAX_SETS_IN_MATCH];
	private static Long mRowID;
	private static Long mP1RowId;
	private static Long mP2RowId;
	private static Long playerIDs[] = new Long[2];

	static int p1CurrentSetScore = 0;
	static int p2CurrentSetScore = 0;
	static Bitmap bmp = null;

	


    private MatchPresenter(Context ctx) {
        
        matchesDbHelper = new MatchDbAdapter(ctx);
        matchesDbHelper.open();
        playerDbHelper = new PlayerDbAdapter(ctx);
        playerDbHelper.open();
        
    }
    
	public static MatchPresenter getInstance(Context ctx){
		
		if (instance == null){
			instance = new MatchPresenter(ctx);
		}
		return instance;
	}
	
    //If match(mRowID) exists, load it else create new match.
	public void initialiseMatch(Context context, long matchRowID, long player1RowID, long player2RowID){
		
		mRowID = matchRowID;


        Cursor matchCursor = matchesDbHelper.fetchMatch(mRowID);

        //Populate player name and picture

        playerIDs[0] = player1RowID;
        playerIDs[1] = player2RowID;
        		
        for (int i=0;i<IBPLConstants.MAX_SETS_IN_MATCH;i++)
        {
        	aP1Sets[i] = new SetLogic(context);
        	aP2Sets[i] = new SetLogic(context);
        }

        //If match exists, update set adapters
		if (matchCursor != null) {
			aP1Sets[IBPLConstants.SET_ONE]
					.updateCurrentScoreArray(matchCursor.getString(matchCursor
							.getColumnIndexOrThrow(MatchDbAdapter.KEY_SET1_RESULT)));
			aP1Sets[IBPLConstants.SET_TWO]
					.updateCurrentScoreArray(matchCursor.getString(matchCursor
							.getColumnIndexOrThrow(MatchDbAdapter.KEY_SET2_RESULT)));
			aP1Sets[IBPLConstants.SET_THREE]
					.updateCurrentScoreArray(matchCursor.getString(matchCursor
							.getColumnIndexOrThrow(MatchDbAdapter.KEY_SET3_RESULT)));

			aP2Sets[IBPLConstants.SET_ONE]
					.updateCurrentScoreArray(FrameCodeAPI.getInverseScoreString(matchCursor.getString(matchCursor
							.getColumnIndexOrThrow(MatchDbAdapter.KEY_SET1_RESULT))));
			aP2Sets[IBPLConstants.SET_TWO]
					.updateCurrentScoreArray(FrameCodeAPI.getInverseScoreString(matchCursor.getString(matchCursor
							.getColumnIndexOrThrow(MatchDbAdapter.KEY_SET2_RESULT))));
			aP2Sets[IBPLConstants.SET_THREE]
					.updateCurrentScoreArray(FrameCodeAPI.getInverseScoreString(matchCursor.getString(matchCursor
							.getColumnIndexOrThrow(MatchDbAdapter.KEY_SET3_RESULT))));

			int currentSet = MatchLogic.getCurrentSet(aP1Sets);
			p1CurrentSetScore = aP1Sets[currentSet].getScoreInteger();
			p2CurrentSetScore = aP2Sets[currentSet].getScoreInteger();

		} else {
			// Reset all scorecards for new match
			for (int i = 0; i < IBPLConstants.MAX_SETS_IN_MATCH; i++) {
				aP1Sets[i].resetScore();
				aP2Sets[i].resetScore();
			}
			p1CurrentSetScore = 0;
			p2CurrentSetScore = 0;

		}
	}

	public ListAdapter getSet(int player, int set) {
		if (player == IBPLConstants.HOME_PLAYER) {
			return aP1Sets[set];
		}
		return aP2Sets[set];
	}

	public static int getMatchScore(int player) {
		if (player == IBPLConstants.HOME_PLAYER) {
			return MatchLogic.getMatchScore(aP1Sets);
		}
		return MatchLogic.getMatchScore(aP2Sets);
	}
	
	
	
	public void closeMatch(){
		
	    //TODO: Should we always keep this connection open and use it from everywhere in the app?
        matchesDbHelper.close();
        playerDbHelper.close();
	}

	public void saveMatch() {
        String set1Result = aP1Sets[0].getSetScoreString();
        String set2Result = aP1Sets[1].getSetScoreString();
        String set3Result = aP1Sets[2].getSetScoreString();

        if (mRowID != null) {
            matchesDbHelper.updateMatchResult(mRowID, set1Result, set2Result, set3Result);
        }
	}
	
	public String getPlayerName(int player)
	{
        if (playerIDs[player] != null) {

        	Cursor playerCursor = playerDbHelper.fetchPlayer(playerIDs[player]);
        	return playerCursor.getString(playerCursor.getColumnIndexOrThrow(PlayerDbAdapter.KEY_NAME));         
        }
        
        return "";
		
	}

	public Bitmap getPlayerImage(int player) {

		if (playerIDs[player] != null) {

			Cursor playerCursor = playerDbHelper.fetchPlayer(playerIDs[player]);
			byte[] byteArray = playerCursor.getBlob(playerCursor
					.getColumnIndexOrThrow(PlayerDbAdapter.KEY_PICTURE));

			// Create object of bitmapfactory's option method for further option use
			// inPurgeable is used to free up memory while required
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true; 
			bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,
					options);
		}

		return bmp;
	}

	public CharSequence getSetScore(int set, int player) {
		int score = 0;
		if (player == IBPLConstants.HOME_PLAYER) {
			score = aP1Sets[set].getScoreInteger();

		} else {
			score = aP2Sets[set].getScoreInteger();

		}
		return Integer.toString(score);
	}

	public void updateSetScore(int player, int pos, int set, int frameCode){
		
	    Log.v(TAG, "updateSetScore: player=" + player);
	    Log.v(TAG, "updateSetScore: set=" + set);
	    Log.v(TAG, "updateSetScore: frameCode=" + frameCode);
	    Log.v(TAG, "updateSetScore: pos=" + pos);

	    if (player == IBPLConstants.HOME_PLAYER)
	    {
		    aP1Sets[set].updateScore(pos, frameCode);
		    aP1Sets[set].notifyDataSetChanged();
		    Log.v(TAG, "P1 Set["+set+"]=" + aP1Sets[set].getScoreInteger());
		    
		    aP2Sets[set].updateScore(pos, FrameCodeAPI.getInverseCodeInteger(frameCode));
		    aP2Sets[set].notifyDataSetChanged();
		    Log.v(TAG, "P2 Set["+set+"]=" + aP2Sets[set].getScoreInteger());
	    }
	    else
	    {
		    aP2Sets[set].updateScore(pos, frameCode);
		    aP2Sets[set].notifyDataSetChanged();
		    Log.v(TAG, "P2 Set["+set+"]=" + aP2Sets[set].getScoreInteger());
		    
		    aP1Sets[set].updateScore(pos, FrameCodeAPI.getInverseCodeInteger(frameCode));
		    aP1Sets[set].notifyDataSetChanged();
		    Log.v(TAG, "P1 Set["+set+"]=" + aP1Sets[set].getScoreInteger());
	    }
	    
	    if (isSetFinished(set))
	    {   
	    	//This is the only place we should be resetting the set score. 
	    	//ie. After the user has selected a code on the grid.
	    	resetCurrentSetScore();
	    }
	    else
	    {
		    p1CurrentSetScore = aP1Sets[set].getScoreInteger();
		    p2CurrentSetScore = aP2Sets[set].getScoreInteger();
	    }
	}

	public String getCurrentSetScore(int player) {
		if (player == IBPLConstants.HOME_PLAYER) {
			return Integer.toString(p1CurrentSetScore);
		} 	

		return Integer.toString(p2CurrentSetScore);
	
	}
	public void resetCurrentSetScore() {
		p1CurrentSetScore=0;
		p2CurrentSetScore=0;
	}
	
	public boolean isSetFinished(int set) {
		return aP1Sets[set].isSetFinished();
	}
	
	public Cursor fetchAllMatches() {
        return matchesDbHelper.fetchAllMatches();
    }
    	
    public boolean deleteMatch(long rowId) {
        return matchesDbHelper.deleteMatch(rowId);
    }

    public Cursor fetchMatch(long rowId) {
        return matchesDbHelper.fetchMatch(rowId);
    }

    public long createMatch(Long mP1RowId2, Long mP2RowId2, String set1Result,
            String set2Result, String set3Result, String date) {
        return matchesDbHelper.createMatch(mP1RowId2, mP2RowId2, set1Result, set2Result, set3Result, date);
    }

    public void updateMatch(Long mRowId2, Long mP1RowId2, Long mP2RowId2,
            String set1Result, String set2Result, String set3Result, String date) {
        matchesDbHelper.updateMatch(mRowId2, mP1RowId2, mP2RowId2, set1Result, set2Result, set3Result, date);
    }

    public Cursor fetchAllPlayers() {
        return playerDbHelper.fetchAllPlayers();
    }
}
