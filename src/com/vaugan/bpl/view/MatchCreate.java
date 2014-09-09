package com.vaugan.bpl.view;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.vaugan.bpl.R;
import com.vaugan.bpl.model.MatchDbAdapter;
import com.vaugan.bpl.model.PlayerDbAdapter;
import com.vaugan.bpl.presenter.MatchPresenter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MatchCreate extends Activity implements DatePickerDialog.OnDateSetListener{
    
    private static final String TAG = "MatchCreate";
    private Long mP1RowId;
    private Long mP2RowId;
    private Long mRowId;

    public Context mContext;    
    SimpleCursorAdapter sca;
    private MatchPresenter mp;
    private SimpleDateFormat df; 
    TextView matchDate;
    private DatePickerFragment picker;
 
    protected static final int ACTIVITY_MATCH_DISPLAY = 0;
    protected static final int ACTIVITY_MAIN_MENU = 4;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{ 
            

            picker = new DatePickerFragment();
            picker.setOnDateSetListener(this);

            
	        setContentView(R.layout.match_create);
	        setTitle(R.string.create_match);

            matchDate = (TextView) findViewById(R.id.matchDate);

            mp = MatchPresenter.getInstance(this.getApplicationContext());
	        fillData();
	
	        mRowId = (savedInstanceState == null) ? null :
	            (Long) savedInstanceState.getSerializable(MatchDbAdapter.KEY_ROWID);
	        
			if (mRowId == null) {
				Bundle extras = getIntent().getExtras();
				mRowId = extras != null ? extras.getLong(MatchDbAdapter.KEY_ROWID)
										: null;
			}
	
			populateFields();
	
            Button startMatchButton = (Button) findViewById(R.id.btnStartMatch);
            startMatchButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    saveState();
                    Intent i = new Intent(MatchCreate.this, MatchDisplay.class);
                    Log.v(TAG,"mRowID="+mRowId);
                    Log.v(TAG,"MatchDbAdapter.KEY_ROWID="+MatchDbAdapter.KEY_ROWID);
                    
                    i.putExtra("com.vaugan.csf.match.rowid", mRowId); 
                    i.putExtra("com.vaugan.csf.match.p1rowid", mP1RowId); 
                    i.putExtra("com.vaugan.csf.match.p2rowid", mP2RowId); 
                    
                    startActivityForResult(i, ACTIVITY_MATCH_DISPLAY);    
                }
            });   

            Button cancelButton = (Button) findViewById(R.id.btnCancel);
            cancelButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MatchCreate.this, MainMenu.class);
                    startActivityForResult(i, ACTIVITY_MAIN_MENU);    
                }
            });   

//            Button changeDateButton = (Button) findViewById(R.id.btnChangeDate);
//            changeDateButton.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    picker.show(getFragmentManager(), "datePicker");  
//                }
//            });   
//            
            
            matchDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    picker.show(getFragmentManager(), "datePicker");  
                }
            });   

            
        }catch (Exception e) {
            // handle any errors
            Log.e("BPL Scorecard", "Error in activity", e);  // log the error
            // Also let the user know something went wrong
            Toast.makeText(
                getApplicationContext(),
                e.getClass().getName() + " " + e.getMessage(),
                Toast.LENGTH_LONG).show();
        }
        
    }

    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mp.fetchMatch(mRowId);
            startManagingCursor(note);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(MatchDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
    	
    	int position = ((Spinner) findViewById(R.id.spinnerPlayer1)).getSelectedItemPosition();
    	Cursor cursor = (Cursor) sca.getItem(position);
    	mP1RowId = cursor.getLong(cursor.getColumnIndex(PlayerDbAdapter.KEY_ROWID));
    	
    	position = ((Spinner) findViewById(R.id.spinnerPlayer2)).getSelectedItemPosition();
    	cursor = (Cursor) sca.getItem(position);
    	mP2RowId = cursor.getLong(cursor.getColumnIndex(PlayerDbAdapter.KEY_ROWID));
    	
        
        String set1Result = "MMMMMMM";
        String set2Result = "MMMMMMM";
        String set3Result = "MMMMMMM";

        if (mRowId == null) {
            long id = mp.createMatch(mP1RowId, mP2RowId, set1Result, set2Result, set3Result, (String)matchDate.getText());
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mp.updateMatch(mRowId, mP1RowId, mP2RowId, set1Result, set2Result, set3Result, (String)matchDate.getText());
        }
    }

    private void fillData() {
        Cursor playersCursor = mp.fetchAllPlayers();
        startManagingCursor(playersCursor);
        
        playersCursor.moveToFirst();
        // make an adapter from the cursor
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{PlayerDbAdapter.KEY_NAME};
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.firstName};
        // Now create a simple cursor adapter and set it to display
        
        sca = new SimpleCursorAdapter(this, R.layout.player_row, playersCursor, from, to);

        // set layout for activated adapter
        sca.setDropDownViewResource(R.layout.player_row);    
        
        // get xml file spinner and set adapter 
        Spinner spinnerViewP1 = (Spinner) this.findViewById(R.id.spinnerPlayer1);
        Spinner spinnerViewP2 = (Spinner) this.findViewById(R.id.spinnerPlayer2);
        spinnerViewP1.setAdapter(sca);
        spinnerViewP2.setAdapter(sca);

        
        // set spinner listener to display the selected item id
        mContext = this;
        spinnerViewP1.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                Toast.makeText(mContext, "Selected ID=" + id, Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
            });      
    
        df = new SimpleDateFormat("EEE, d MMM yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        matchDate.setText(date);
    }

    @Override
    public void onDateSet(DatePicker arg0, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
        String formattedDate = df.format(cal.getTime());
        matchDate.setText(formattedDate);
        
    }    

    
}
