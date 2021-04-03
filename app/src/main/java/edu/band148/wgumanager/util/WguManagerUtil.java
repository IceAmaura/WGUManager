package edu.band148.wgumanager.util;

import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WguManagerUtil {
    public static void updateLabel(Calendar calendar, EditText editText) {
        String dateFormat = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        editText.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public static void setCalendar(Calendar calendar, String dateString) {
        String dateFormat = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        try {
            calendar.setTime(simpleDateFormat.parse(dateString));
        } catch (ParseException e) {
            //Fallback for parse exception
            calendar.set(2020, 01, 01);
        }
    }
}
