package com.vaugan.bpl.model;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;

import com.vaugan.bpl.model.FrameCodeAPI;

/**
 * Class to handle all BPL set-related functionality.
 * 
 * @author Vaugan.Nayagar
 */
public class SetLogic extends BaseAdapter{
    private Context mContext;
    private static final String TAG = "SetLogic";
    private int setScore = 0;
	private Integer[] setScoreIntegerArray = { 0, 0, 0, 0, 0, 0, 0 };
	
    public SetLogic(Context c) {
        mContext = c;
    }
    
    public String getSetScoreString() {

    	return FrameCodeAPI.getScoreString(setScoreIntegerArray);
    }

    
    public void updateCurrentScoreArray(String scoreString) {
        
        Log.v(TAG, "updateCurrentScoreArray: resultString=" + scoreString);       
        for (int i = 0; i < setScoreIntegerArray.length; i++) {
            setScoreIntegerArray[i] = FrameCodeAPI.indexOfChar (scoreString.charAt(i), IBPLConstants.CSFCodes);
        }
    }


    
    //TODO - This must be refactored into FrameCodeAPI, as it is csf logic, not set logic.
    public int updateScore(int position, int framecode) {
        setScoreIntegerArray[position] = framecode;
        Log.v(TAG, "updateScore: framecode=" + framecode);   
        
        setScore=0;
        for (int i = 0; i < setScoreIntegerArray.length; i++) {
//            Log.v(TAG, "mCurrentScore[" + i +"] = " + mCurrentScore[i]);   
            switch (setScoreIntegerArray[i])
            {
                case 0: //intentional fall thru
                case 1: //intentional fall thru
                case 2: //intentional fall thru
                case 5:
                    setScore++;
                    Log.v(TAG, "Incrementing mTotalScore, mCurrentScore[]:=" + setScoreIntegerArray[i]);   
                    break;
            }
        }
        return setScore;
    }
    
    //TODO - This must be refactored into FrameCodeAPI, as it is csf logic, not set logic.
    public int getScoreInteger() {
        setScore=0;
        for (int i = 0; i < setScoreIntegerArray.length; i++) {
            switch (setScoreIntegerArray[i])
            {
                case 0: //intentional fall thru
                case 1: //intentional fall thru
                case 2: //intentional fall thru
                case 5:
                    setScore++;
                    Log.v(TAG, "Incrementing mTotalScore, mCurrentScore[]:=" + setScoreIntegerArray[i]);   
                    break;
            }
        }
        return setScore;
    }

    //TODO - This must be refactored into FrameCodeAPI, as it is csf logic, not set logic.
    public int getInverseScoreInteger() {
        setScore=0;
        for (int i = 0; i < setScoreIntegerArray.length; i++) {
            switch (setScoreIntegerArray[i])
            {
                case 3: //intentional fall thru
                case 4: //intentional fall thru
                case 6: //intentional fall thru
                case 7:
                    setScore++;
                    Log.v(TAG, "Inverse Score (ie. Frames lost this set)=" + setScoreIntegerArray[i]);   
                    break;
            }
        }
        return setScore;
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

    //TODO - This must be refactored into View?

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            int size = getSizeForDevice();
            
            imageView.setLayoutParams(new GridView.LayoutParams(size, size));
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(0,0,0,0);
            imageView.setAdjustViewBounds(true);

        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(FrameCodeAPI.mFrameResultImages[setScoreIntegerArray[position]]);
        return imageView;
    }
    
    private int getSizeForDevice() {

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

        int width=metrics.widthPixels;
        int height=metrics.heightPixels;
        int dens=metrics.densityDpi;
        double wi=(double)width/(double)dens;
        double hi=(double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi,2);
        double screenInches = Math.sqrt(x+y);
        double multiplier = 1;
        
        if ((screenInches < 7) && (metrics.densityDpi < DisplayMetrics.DENSITY_HIGH))
        {
            multiplier = 0.75;
        }
        

        int size = 75;
        
        switch (metrics.densityDpi) {
        case DisplayMetrics.DENSITY_LOW:
            size = 50;
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            size = 75;
            break;
        case DisplayMetrics.DENSITY_HIGH:
            size = 100;
            break;
        case DisplayMetrics.DENSITY_XHIGH:
        case 480:
            size = 120;
            break;
        }

        Log.w(TAG, "getSizeForDevice: metrics.densityDpi="+metrics.densityDpi + " size="+(int)(size*multiplier));
        
        return (int)(size*multiplier);
    }    

	public void resetScore() {
		setScore = 0;
		for (int i = 0; i < setScoreIntegerArray.length; i++) {
			setScoreIntegerArray[i] = 8;
		}
	} 

	public int isSetWon() {
		// Calculate if a player won the set

		return ((this.getScoreInteger() < IBPLConstants.FRAMES_TO_WIN_SET) ? 0
				: 1);
	}

	public boolean isSetFinished() {
		// Calculate if a player won the set

		if ((this.getScoreInteger() == IBPLConstants.FRAMES_TO_WIN_SET)
				|| (this.getInverseScoreInteger() == IBPLConstants.FRAMES_TO_WIN_SET)) {
			return true;
		}
		return false;
	}
}

