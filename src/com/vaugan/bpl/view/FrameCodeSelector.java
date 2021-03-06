package com.vaugan.bpl.view;




import com.vaugan.bpl.R;
import com.vaugan.bpl.model.FrameCodeImageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class FrameCodeSelector extends Activity {
    /** Called when the activity is first created. */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_code_chooser);

        Bundle extras = this.getIntent().getExtras();
        final int pos = extras.getInt("pos");
        final int player = extras.getInt("player");
        final int set = extras.getInt("set");

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new FrameCodeImageAdapter(FrameCodeSelector.this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selected_icon", position);
                resultIntent.putExtra("player", player);
                resultIntent.putExtra("pos", pos);
                resultIntent.putExtra("set", set);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}