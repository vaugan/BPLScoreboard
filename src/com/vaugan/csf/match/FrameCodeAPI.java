package com.vaugan.csf.match;

public class FrameCodeAPI {
    
    
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

    public static int getInverseCodeImage(int Code)
    {

        int indexOfYellow = indexOf(mFrameResultInverseImages[Code], mFrameResultImages);
        return indexOfYellow;
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
}
