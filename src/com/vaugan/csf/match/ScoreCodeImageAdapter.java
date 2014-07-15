package com.vaugan.csf.match;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.vaugan.csf.match.FrameCodeAPI;

public class ScoreCodeImageAdapter extends BaseAdapter {
    private Context mContext;
    private static final String TAG = "ScoreCodeImageAdapter";
    
    protected static int MAXIMUM_FRAMES = 7;
    protected int mTotalScore = 0;

    public ScoreCodeImageAdapter(Context c) {
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
        return MAXIMUM_FRAMES;
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
    
    // references to our images
    private Integer[] mCurrentScore = {
            0, 
            1,
            0, 
            3,
            4, 
            5,
            6,
            6,
            7,
            5,
            2,
            1, //12
            0, 
            1,
            0, 
            3,
            4, 
            7,
            8,
            8,
            8,
            8,
            8,
            8
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

