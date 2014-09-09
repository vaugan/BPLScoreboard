package com.vaugan.bpl.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.vaugan.bpl.R;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {

    private OnDateSetListener onDateSetListener;
    private String currentDate;
    private SimpleDateFormat df;
    
    public String getCurrentDate() {
        return currentDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        df = new SimpleDateFormat("EEE, d MMM yyyy");
        currentDate = df.format(c.getInstance().getTime());

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        currentDate = df.format(c.getInstance().getTime());

        if(onDateSetListener != null)
            onDateSetListener.onDateSet(view, year, month, day);
        
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String formattedDate = sdf.format(c.getTime());
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener){
        this.onDateSetListener = listener;
    }

    
}