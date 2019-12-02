package com.myListApp.mylist.Models;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

public class BoardingModel {

    private int icon;
    private String title;
    private String details;
    private String activity;

    public BoardingModel(int icon, String title, String details, String activity) {
        this.icon = icon;
        this.title = title;
        this.details = details;
        this.activity=activity;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
