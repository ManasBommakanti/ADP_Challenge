package com.example.appchallenge;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {

    private String date, initTime, endTime, event, schoolCord, comments, eventID;

    public Event()
    {
        this.date = "";
        this.initTime = "";
        this.endTime = "";
        this.event = "";
        this.schoolCord = "";
        this.comments = "";
        this.eventID = "";
    }

    public Event(String date, String initTime, String endTime, String event, String schoolCord, String comments, String eventID)
    {
        this.date = date;
        this.initTime = initTime;
        this.endTime = endTime;
        this.event = event;
        this.schoolCord = schoolCord;
        this.comments = comments;
        this.eventID = eventID;
    }

    @NonNull
    public String getDate(){
        return date;
    }

    @NonNull
    public String getInitTime() {
        return initTime;
    }

    @NonNull
    public String getEndTime() {
        return endTime;
    }

    @NonNull
    public String getEvent() {
        return event;
    }

    @NonNull
    public String getSchoolCord() {
        return schoolCord;
    }

    @NonNull
    public String getComments() {
        return comments;
    }

    public void setInitTime(String initDate) {
        this.initTime = initDate;
    }

    public void setEndTime(String endDate) {
        this.endTime = endDate;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setSchoolCord(String schoolCord) {
        this.schoolCord = schoolCord;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getEventID(){
        return eventID;
    }

    public void setEventID(String eventID){
        this.eventID = eventID;
    }

    public String toString()
    {
        return date + ";" + initTime + ";" + endTime + ";" + event + ";" + schoolCord + ";" + comments;
    }
}
