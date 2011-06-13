package com.vaugan.csf.match;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ScoreCodeImageAdapter extends BaseAdapter {
    private Context mContext;
    
    protected static int MAXIMUM_FRAMES = 24;

    public ScoreCodeImageAdapter(Context c) {
        mContext = c;
    }

    public void updateScore(int position, int framecode) {
        mTempScore[position] = framecode;
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

        imageView.setImageResource(mThumbIds[mTempScore[position]]);
        return imageView;
    }


    
    // references to our images
    private Integer[] mTempScore = {
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

    // references to our images
    private Integer[] mThumbIds = {
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
}
