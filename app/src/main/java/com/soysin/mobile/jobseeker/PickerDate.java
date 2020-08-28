package com.soysin.mobile.jobseeker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.ArrayAdapter;

import java.util.List;

public class PickerDate {
    public static List<String> date;
    public static DatePickerDialog.OnDateSetListener onDateSetListener;
    public static List<String> getDate(){

        return date;
    }
}
