package com.alanai.todogenix.Models;

public class Task {
    private String taskID;
    private String title;
    private String description;
    private String type;
    private int duration; //minutes
    private String date;
    private String time;
    private long timeStamp;
    private boolean complete;

    public Task() {
        this.complete = false;
    }

    //Explorer
    public Task(String taskID, String title, String description, String type, long timeStamp) {
        this.taskID = taskID;
        this.title = title;
        this.description = description;
        this.type = type;
        this.timeStamp = timeStamp;
        this.complete = false;
    }

    //Flexible or Strict
    public Task(String taskID, String title, String description, String type, String date, String time, long timeStamp) {
        this.taskID = taskID;
        this.title = title;
        this.description = description;
        this.type = type;
        this.date = date;
        this.time = time;
        this.timeStamp = timeStamp;
        this.complete = false;
    }

    //with duration
    public Task(String taskID, String title, String description, String type, int duration, String date, String time, long timeStamp) {
        this.taskID = taskID;
        this.title = title;
        this.description = description;
        this.type = type;
        this.duration = duration;
        this.date = date;
        this.time = time;
        this.timeStamp = timeStamp;
        this.complete = false;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
