package com.unimate.model;


import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Hans Vader on 12.11.2016.
 */

public class Message {

    private String messageText, sender;

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


}
