package com.example.android.worldheadlines.utilitaries;

public class StringManipulation {

    public String getFormatedString(String s){
        String date = getFormatedDateString(s);
        String hour = getFormatedHoursString(s);
        String string = date + " " + "/" + " " + hour;
        return string;
    }

    private String getFormatedDateString(String s){
        String date = s.substring(0, 10);
        return date;
    }

    private String getFormatedHoursString(String s){
        String hour = s.substring(11, 16);
        return hour;
    }
}
