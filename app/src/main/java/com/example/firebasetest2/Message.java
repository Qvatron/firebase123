package com.example.firebasetest2; //поменять название пакета на свой

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class Message {
    private String content;
    private String date;
    private User user;



    public Message(String content, String date, User user) {
        this.content = content;
        this.date = date;
        this.user = user;
    }

    public Message(String content, User user) {
        this.content = content;
        this.user = user;
        Date date = new Date();
        int hours = date.getHours();
        int minutes = date.getMinutes();
        int seconds = date.getSeconds();

        this.date = String.format("%02d:%02d:%02d",hours,minutes,seconds);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
