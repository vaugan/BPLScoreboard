package com.vaugan.bpl.model;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.vaugan.bpl.model.FrameCodeAPI;

public class MatchLogic {
    private Context mContext;
    private static final String TAG = "MatchLogic";
    
    public MatchLogic(Context c) {
        mContext = c;
    }


	public static boolean isMatchOver(SetLogic[] aP1Sets, SetLogic[] aP2Sets) {
	
		if ((getMatchScore(aP1Sets) >= IBPLConstants.SETS_TO_WIN_MATCH) || 
			(getMatchScore(aP2Sets) >= IBPLConstants.SETS_TO_WIN_MATCH)) 
		{
			return true;
		}
		return false;
	}

	public static int getMatchScore(SetLogic[] aSets) {
		int matchScore = 0;
		for (int i=0;i<IBPLConstants.MAX_SETS_IN_MATCH;i++)
		{
			matchScore += aSets[i].isSetWon();
		}
		return matchScore;
	}
}

