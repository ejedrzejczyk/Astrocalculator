package com.example.astrocalculator;

import com.astrocalculator.AstroDateTime;

public class DataMenager {

    private String hour;
    private String minute;
    private String second;

    private String day;
    private String month;
    private String year;

    public String timeToString(AstroDateTime time){
        if(time.getHour() < 10){
            hour = "0" + time.getHour();
        }
        else hour = String.valueOf(time.getHour());

        if(time.getMinute() < 10){
            minute = "0" + time.getMinute();
        }
        else minute = String.valueOf(time.getMinute());

        if(time.getSecond() < 10){
            second = "0" + time.getSecond();
        }
        else second = String.valueOf(time.getSecond());

        return (hour + ":" + minute + ":" + second);
    }

    public String dateToString(AstroDateTime date){
        if(date.getDay() < 10){
            day = "0" + date.getDay();
        }
        else day = String.valueOf(date.getDay());

        if(date.getMonth() < 10){
            month = "0" + date.getMonth();
        }
        else month = String.valueOf(date.getDay());

        year = String.valueOf(date.getYear());

        return (day + "/" + month + "/" + year);
    }
}