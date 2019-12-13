package com.example.pro.shopping.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.pro.shopping.Activities.Company.CreateOffer;

import java.util.Calendar;

public class CustomDateTimePicker {
    public static  DatePickerDialog.OnDateSetListener mDateSetListener;
    public static int YEAR = 0 ,MONTH = 0,DAY = 0 ;
    public static int MIN = 0 , HOUR;
    private Context context ;

    public CustomDateTimePicker(Context context) {
        this.context = context ;
    }

    public void ShowDateDialog(final Button btn)
    {


        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                btn.setText((month+1)+"/"+dayOfMonth+"/"+year);
                YEAR = year ;
                MONTH = month ;
                DAY = dayOfMonth ;
            }
        }, 2019, 1, 1);
        datePickerDialog.show();
    }


    public static String SELECTED_TIME = null ;
    // Create and show a TimePickerDialog when click button.
    public void ShowTimeDialog(final Button btn)
    {
        // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker timePicker, int hour, int minute) {
                StringBuffer strBuf = new StringBuffer();
                HOUR = hour ;
                MIN = minute ;
                strBuf.append(hour);
                strBuf.append(":");
                strBuf.append(minute);
                SELECTED_TIME = strBuf.toString();
                if (btn !=null) {
                        btn.setText(strBuf.toString());
                }
            }
        };

        Calendar now = Calendar.getInstance();
        int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = now.get(java.util.Calendar.MINUTE);

        // Whether show time in 24 hour format or not.
        boolean is24Hour = true;

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, onTimeSetListener, hour, minute, is24Hour);

        timePickerDialog.setTitle("Please select time.");

        timePickerDialog.show();

    }




}
