package com.soysin.mobile.jobseeker;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

public class Validate {
    public static boolean empty(TextInputLayout inputLayout, String message) {
        String value = inputLayout.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            inputLayout.setError(message);
            return false;
        } else {
            inputLayout.setError(null);
            return true;
        }
    }
    public static boolean password(TextInputLayout inputLayout, String message){
        String value = inputLayout.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            inputLayout.setError(message);
            return false;
        } else if(value.length() < 6){
            inputLayout.setError("password must 6 character more");
            return false;
        }
        else {
            inputLayout.setError(null);
            return true;
        }
    }
    public static boolean passwordConform(TextInputLayout inputLayout,String password, String message){
        String value = inputLayout.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            inputLayout.setError(message);
            return false;
        } else if(!value.equals(password)){
            inputLayout.setError("password don't match");
            return false;
        }
        else {
            inputLayout.setError(null);
            return true;
        }
    }
    public static boolean checkFile(File file, Activity activity){
        if (file == null){
            Toast.makeText(activity.getApplicationContext(),"File required",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
}
