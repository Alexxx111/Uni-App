package com.unimate.model;


import android.graphics.Bitmap;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Hans Vader on 12.11.2016.
 */

public class Message {

    private String messageText, sender;
    private int image; // is 0 when message is no image, is 1 when message is image
    private Bitmap bmp;

    public Message(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }
}
