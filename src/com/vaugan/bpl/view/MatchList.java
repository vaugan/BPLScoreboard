package com.vaugan.bpl.view;

import com.vaugan.bpl.R;
import com.vaugan.bpl.model.MatchDbAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MatchList extends ListActivity {
    private static final String TAG = "MatchList";    
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private static final int ACTIVITY_MAIN=2;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private MatchDbAdapter mDbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_list);
        mDbHelper = new MatchDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
//        
//        Intent i = new Intent(this, MainMenu.class);
//        startActivityForResult(i, ACTIVITY_MAIN);        
    }

    private void fillData() {
        Cursor matchesCursor = mDbHelper.fetchAllMatches();
        startManagingCursor(matchesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{MatchDbAdapter.KEY_P1, MatchDbAdapter.KEY_P2};

        // and an array of the fields we want to bind those fields to
        int[] to = new int[]{R.id.p1, R.id.p2};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter matches = 
            new SimpleCursorAdapter(this, R.layout.match_row, matchesCursor, from, to);
        setListAdapter(matches);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                //createNote();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete_match);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
               mDbHelper.deleteMatch(info.id);
               fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

//    private void createNote() {
//        Intent i = new Intent(this, PlayerEdit.class);
//        startActivityForResult(i, ACTIVITY_CREATE);
//    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, MatchDisplay.class);
        
        Log.v(TAG, "id="+id);
        
        i.putExtra("com.vaugan.csf.match.rowid", id); 
       // i.putExtra(MatchDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
