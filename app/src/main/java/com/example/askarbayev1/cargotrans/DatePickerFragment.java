package com.example.askarbayev1.cargotrans;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class  DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    Button button;
    TextView textView;
    DatePickerFragment(Button button, TextView textView){
        this.button = button;
        this.textView = textView;

    }
    public DatePickerFragment(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        button.setText(month+"/"+dayOfMonth+"/"+year);
        textView.setText(month+"/"+dayOfMonth+"/"+year);
    }

}
