package com.vaugan.bpl.model;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.vaugan.bpl.model.FrameCodeAPI;

public class SetLogic extends BaseAdapter{
    private Context mContext;
    private static final String TAG = "SetLogic";
    private int mTotalScore = 0;
	private Integer[] mCurrentScore = { 0, 0, 0, 0, 0, 0, 0 };
	
    public SetLogic(Context c) {
        mContext = c;
    }
    
    public String getScoreString()
    {
        String matchResultString = "";
        for (int i = 0; i < mCurrentScore.length; i++) {
            matchResultString += IBPLConstants.CSFCodes[mCurrentScore[i]];
        }
        return matchResultString;
    }
    
    public void updateCurrentScoreArray(String scoreString) {
        
        Log.v(TAG, "updateCurrentScoreArray: resultString=" + scoreString);       
        for (int i = 0; i < mCurrentScore.length; i++) {
            mCurrentScore[i] = FrameCodeAPI.indexOfChar (scoreString.charAt(i), IBPLConstants.CSFCodes);
        }
    }

    public static String getInverseScoreArray(String scoreString) {
        
        String inverseResultString = "";
        Log.v(TAG, "getInverseScoreArray of " +scoreString);   
        for (int i = 0; i < scoreString.length(); i++) {
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
        return IBPLConstants.MAX_FRAMES_IN_SET;
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
            
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER);
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
    

public int isSetWon()
{
	//Calculate if a player won the set
	
	return ((this.getScoreInteger()<IBPLConstants.FRAMES_TO_WIN_SET)?0:1);
}

}

