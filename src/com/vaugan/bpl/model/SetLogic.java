package com.vaugan.bpl.model;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.vaugan.bpl.model.FrameCodeAPI;

public class SetLogic extends BaseAdapter {
    private Context mContext;
    private static final String TAG = "SetLogic";
    
//    public static final int MAX_SETS_IN_MATCH = 3;
//    public static final int SETS_TO_WIN_MATCH = 2;
    public static int MAX_FRAMES_IN_SET = 7;
    public static final int FRAMES_TO_WIN_SET = 4;    
    protected int mTotalScore = 0;

    public SetLogic(Context c) {
        mContext = c;
    }
    
    public String getScoreString()
    {
        String matchResultString = "";
        for (int i = 0; i < mCurrentScore.length; i++) {
            matchResultString += CSFCodes[mCurrentScore[i]];
        }
        return matchResultString;
    }
    
    public void updateCurrentScoreArray(String scoreString) {
        
        Log.v(TAG, "updateCurrentScoreArray: resultString=" + scoreString);       
        for (int i = 0; i < mCurrentScore.length; i++) {
            mCurrentScore[i] = FrameCodeAPI.indexOfChar (scoreString.charAt(i), CSFCodes);
//            Log.v(TAG, "mCurrentScore[" + i +"] = " + mCurrentScore[i]+"  scoreString.charAt(i)= "+ scoreString.charAt(i));   
        }
    }

    public static String getInverseScoreArray(String scoreString) {
        
        String inverseResultString = "";
        Log.v(TAG, "getInverseScoreArray of " +scoreString);   
        for (int i = 0; i < scoreString.length(); i++) {
//            Log.v(TAG, "scoreString[" + i +"] = " + scoreString.charAt(i));  
//            Log.v(TAG, "FrameCodeAPI.getInverseCodeImage(scoreString.charAt(i)) = " + FrameCodeAPI.getInverseCodeChar(scoreString.charAt(i))); 
            inverseResultString += FrameCodeAPI.getInverseCodeChar(scoreString.charAt(i));
        }
        Log.v(TAG, "inverseResultString[" + inverseResultString);   
        return inverseResultString;

    }
    
    
    public int updateScore(int position, int framecode) {
        mCurrentScore[position] = framecode;
        Log.v(TAG, "updateScore: framecode=" + framecode);   
        
        mTotalScore=0;
        for (int i = 0; i < mCurrentScore.length; i++) {
//            Log.v(TAG, "mCurrentScore[" + i +"] = " + mCurrentScore[i]);   
            switch (mCurrentScore[i])
            {
                case 0: //intentional fall thru
                case 1: //intentional fall thru
                case 2: //intentional fall thru
                case 5:
                    mTotalScore++;
                    Log.v(TAG, "Incrementing mTotalScore, mCurrentScore[]:=" + mCurrentScore[i]);   
                    break;
            }
        }
        return mTotalScore;
    }
    
    public int getScoreInteger() {
        mTotalScore=0;
        for (int i = 0; i < mCurrentScore.length; i++) {
//            Log.v(TAG, "mCurrentScore[" + i +"] = " + mCurrentScore[i]);   
            switch (mCurrentScore[i])
            {
                case 0: //intentional fall thru
                case 1: //intentional fall thru
                case 2: //intentional fall thru
                case 5:
                    mTotalScore++;
                    Log.v(TAG, "Incrementing mTotalScore, mCurrentScore[]:=" + mCurrentScore[i]);   
                    break;
            }
        }
        return mTotalScore;
    }
   
    public int getCount() {
        return MAX_FRAMES_IN_SET;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(40, 40));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0,0,0,0);
            imageView.setAdjustViewBounds(true);

        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(FrameCodeAPI.mFrameResultImages[mCurrentScore[position]]);
        return imageView;
    }

public void resetScore()
{
    mTotalScore=0;
    for (int i = 0; i < mCurrentScore.length; i++) {
        mCurrentScore[i] = 8;
    }
}
    

public static boolean isSetWon(String setResultString)
{
	//Calculate if a player won the set
	
	return false;
}

public static boolean isMatchWon(String set1ResultString, String set2ResultString, String set3ResultString)
{
	//Calculate if a player won the match
	return false;
}
    // references to our images
    private Integer[] mCurrentScore = {
            0, 
            1,
            0, 
            3,
            4, 
            5,
            6,
            };

    static public char[] CSFCodes = {
    'A',
    'B',
    'C',
    'D',
    'E',
    'F',
    'G',
    'Z',
    'M'
    };
    
    static public char[] CSFCodesInverse = {
        'Z',
        'E',
        'D',
        'C',
        'B',
        'G',
        'F',
        'A',
        'M'
        };
}

