package com.soysin.mobile.jobseeker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class Project {

    public static void pickDate(Context context, final TextInputEditText textInputEditText, DatePickerDialog picker){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        textInputEditText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }
    public static void dropDrown(Context context,AutoCompleteTextView autoCompleteTextView,String[] COUNTRIES){

        Log.e("Country",COUNTRIES.toString());

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        context,
                        R.layout.list_item,R.id.textView,
                        COUNTRIES);
        autoCompleteTextView.setAdapter(adapter);
    }
}
