package com.vaugan.csf.match;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class FrameCodeChooser extends Activity {
    /** Called when the activity is first created. */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_code_chooser);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new FrameCodeImageAdapter(FrameCodeChooser.this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(FrameCodeChooser.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}