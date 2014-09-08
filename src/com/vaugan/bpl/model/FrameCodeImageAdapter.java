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
import android.widget.LinearLayout;

/**
 * Utility to get the image for a frame code.
 * 
 * @author Vaugan.Nayagar
 *
 */
public class FrameCodeImageAdapter extends BaseAdapter {
    private Context mContext;
    private static final String TAG = "FrameCodeImageAdapter";


    public FrameCodeImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return FrameCodeAPI.mFrameResultImages.length;
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

            int size = getSizeForDevice();
            imageView.setLayoutParams(new GridView.LayoutParams(size, size));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(FrameCodeAPI.mFrameResultImages[position]);
        return imageView;
    }


    public int getSizeForDevice() {

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
    
}
