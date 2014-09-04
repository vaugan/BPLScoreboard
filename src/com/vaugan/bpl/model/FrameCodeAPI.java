package com.vaugan.bpl.model;




import com.vaugan.bpl.R;

import android.util.Log;

public class FrameCodeAPI {
		/** The Tag constant */
    private static final String TAG = "FrameCodeAPI";
   
    // references to our images
    static protected Integer[] mFrameResultImages = {
            R.drawable.a, 
            R.drawable.b,
            R.drawable.c, 
            R.drawable.d,
            R.drawable.e, 
            R.drawable.f,
            R.drawable.g, 
            R.drawable.z,
            R.drawable.empty
            };

    static protected Integer[] mFrameResultInverseImages = {
        R.drawable.z,
        R.drawable.e, 
        R.drawable.d,
        R.drawable.c, 
        R.drawable.b,
        R.drawable.g, 
        R.drawable.f,
        R.drawable.a, 
        R.drawable.empty
        };

    public static int getInverseCodeInteger(int Code)
    {
//        Log.v(TAG, "getInverseCodeImage: Code=" + Code); 
//        Log.v(TAG, "getInverseCodeImage: mFrameResultInverseImages[Code=" + mFrameResultInverseImages[Code]); 
        int index = indexOf(mFrameResultInverseImages[Code], mFrameResultImages);
        return index;
    }
    
    public static char getInverseCodeChar(char Code)
    {
        int index = indexOfChar(Code, IBPLConstants.CSFCodes);
        return IBPLConstants.CSFCodesInverse[index];
    }
    
    public static String getScoreString(Integer[] setScoreIntegerArray)
    {
        String setScoreString = "";
        for (int i = 0; i < setScoreIntegerArray.length; i++) {
            setScoreString += IBPLConstants.CSFCodes[setScoreIntegerArray[i]];
        }
        return setScoreString;
    }
    
    public static String getInverseScoreString(String scoreString) {
        
        String inverseResultString = "";
        Log.v(TAG, "getInverseScoreString of " + scoreString);   
        for (int i = 0; i < scoreString.length(); i++) {
            inverseResultString += getInverseCodeChar(scoreString.charAt(i));
        }
        Log.v(TAG, "inverseResultString[" + inverseResultString);   
        return inverseResultString;
    }    
    
    public static <T> int indexOf(T needle, T[] haystack)
    {
        for (int i=0; i<haystack.length; i++)
        {
            if (haystack[i] != null && haystack[i].equals(needle)
                || needle == null && haystack[i] == null) return i;
        }

        return -1;
    }

    public static <T> int indexOfChar(char needle, char[] haystack)
    {
        for (int i=0; i<haystack.length; i++)
        {
            if (haystack[i]==(needle)) return i;
        }

        return -1;
    }
    
}
