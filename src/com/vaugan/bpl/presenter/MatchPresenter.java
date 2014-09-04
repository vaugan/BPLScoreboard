package com.vaugan.bpl.presenter;

import com.vaugan.bpl.R;
import com.vaugan.bpl.model.FrameCodeAPI;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MatchPresenter {
	
	static MatchPresenter instance = null;
    private static final String TAG = "MatchPresenter";
	private static MatchDbAdapter mDbHelper;
	private static PlayerDbAdapter playerDbHelper;
	// Array of P1 and P2 sets
	private static SetLogic aP1Sets[] = new SetLogic[IBPLConstants.MAX_SETS_IN_MATCH];
	private static SetLogic aP2Sets[] = new SetLogic[IBPLConstants.MAX_SETS_IN_MATCH];
	private static Long mRowId;
	private static Long mP1RowId;
	private static Long mP2RowId;
	private static Long playerIDs[] = new Long[2];


	
	public static MatchPresenter getInstance(){
		
		if (instance == null){
			instance = new MatchPresenter();
		}
		return instance;
	}
	
	public void initialiseMatch(Context context, long mRowID, long player1RowID, long player2RowID){
        mDbHelper = new MatchDbAdapter(context);
        mDbHelper.open();

        //Populate player name and picture
        playerDbHelper = new PlayerDbAdapter(context);
        playerDbHelper.open();

        playerIDs[0] = player1RowID;
        playerIDs[1] = player2RowID;
        
        for (int i=0;i<IBPLConstants.MAX_SETS_IN_MATCH;i++)
        {
        	aP1Sets[i] = new SetLogic(context);
        	aP2Sets[i] = new SetLogic(context);
        }

        //Reset all scorecards for new match
        for (int i=0;i<IBPLConstants.MAX_SETS_IN_MATCH;i++)
        {
        	aP1Sets[i].resetScore();
        	aP2Sets[i].resetScore();
        }

	}

	public static ListAdapter getSet(int player, int set) {
		if (player == 0) {
			return aP1Sets[set];
		}
		return aP2Sets[set];
	}

	public static int getMatchScore(int player) {
		if (player == 0) {
			return MatchLogic.getMatchScore(aP1Sets);
		}
		return MatchLogic.getMatchScore(aP2Sets);
	}
	
	
	public void closeMatch(){
		
        mDbHelper.close();
        playerDbHelper.close();
	}

	public static void saveMatch() {
        String set1Result = aP1Sets[0].getSetScoreString();
        String set2Result = aP1Sets[1].getSetScoreString();
        String set3Result = aP1Sets[2].getSetScoreString();

        if (mRowId != null) {
            mDbHelper.updateMatchResult(mRowId, set1Result, set2Result, set3Result);
        }
	}
	
	public static String getPlayerName(int player)
	{
        if (playerIDs[player] != null) {

        	Cursor playerCursor = playerDbHelper.fetchPlayer(playerIDs[player]);
        	return playerCursor.getString(playerCursor.getColumnIndexOrThrow(PlayerDbAdapter.KEY_NAME));         
        }
        
        return "";
		
	}

	public static Bitmap getPlayerImage(int player) {
		Bitmap bmp = null;
        if (playerIDs[player] != null) {

        	Cursor playerCursor = playerDbHelper.fetchPlayer(playerIDs[player]);
        	byte[] byteArray = playerCursor.getBlob(playerCursor.getColumnIndexOrThrow(PlayerDbAdapter.KEY_PICTURE));  
        	bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        
        return bmp;
	}

}
