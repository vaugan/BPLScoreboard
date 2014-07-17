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
    
    public static final int MAX_SETS_IN_MATCH = 3;
    public static final int SETS_TO_WIN_MATCH = 2;

    
    public MatchLogic(Context c) {
        mContext = c;
    }


	public static boolean isMatchOver(SetLogic[] aP1Sets, SetLogic[] aP2Sets) {
	
		if ((getMatchScore(aP1Sets) >= SETS_TO_WIN_MATCH) || 
			(getMatchScore(aP2Sets) >= SETS_TO_WIN_MATCH)) 
		{
			return true;
		}
		return false;
	}

	public static int getMatchScore(SetLogic[] aSets) {
		int matchScore = 0;
		for (int i=0;i<MatchLogic.MAX_SETS_IN_MATCH;i++)
		{
			matchScore += aSets[i].isSetWon();
		}
		return matchScore;
	}


    
//    public String getScoreString()
//    {
//        String matchResultString = "";
//        for (int i = 0; i < mCurrentScore.length; i++) {
//            matchResultString += CSFCodes[mCurrentScore[i]];
//        }
//        return matchResultString;
//    }
//    
//    
//    public int updateScore(int position, int framecode) {
//        mCurrentScore[position] = framecode;
//        Log.v(TAG, "updateScore: framecode=" + framecode);   
//        
//        mTotalScore=0;
//        for (int i = 0; i < mCurrentScore.length; i++) {
////            Log.v(TAG, "mCurrentScore[" + i +"] = " + mCurrentScore[i]);   
//            switch (mCurrentScore[i])
//            {
//                case 0: //intentional fall thru
//                case 1: //intentional fall thru
//                case 2: //intentional fall thru
//                case 5:
//                    mTotalScore++;
//                    Log.v(TAG, "Incrementing mTotalScore, mCurrentScore[]:=" + mCurrentScore[i]);   
//                    break;
//            }
//        }
//        return mTotalScore;
//    }
//    
//    public int getScoreInteger() {
//        mTotalScore=0;
//        for (int i = 0; i < mCurrentScore.length; i++) {
////            Log.v(TAG, "mCurrentScore[" + i +"] = " + mCurrentScore[i]);   
//            switch (mCurrentScore[i])
//            {
//                case 0: //intentional fall thru
//                case 1: //intentional fall thru
//                case 2: //intentional fall thru
//                case 5:
//                    mTotalScore++;
//                    Log.v(TAG, "Incrementing mTotalScore, mCurrentScore[]:=" + mCurrentScore[i]);   
//                    break;
//            }
//        }
//        return mTotalScore;
//    }
//   
//
//public void resetScore()
//{
//    mTotalScore=0;
//    for (int i = 0; i < mCurrentScore.length; i++) {
//        mCurrentScore[i] = 8;
//    }
//}
//    
//
//public static boolean isSetWon(String setResultString)
//{
//	//Calculate if a player won the set
//	return false;
//}
//
//public static boolean isMatchWon(String set1ResultString, String set2ResultString, String set3ResultString)
//{
//	//Calculate if a player won the match
//	return false;
//}
}

