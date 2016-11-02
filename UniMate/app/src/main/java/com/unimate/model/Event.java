package com.unimate.model;

/**
 * Created by Hans Vader on 01.11.2016.
 */
public class Event {

    private String name;
    private int memberCount;
    private float gpsX, gpsY;

    public Event() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public float getGpsX() {
        return gpsX;
    }

    public void setGpsX(float gpsX) {
        this.gpsX = gpsX;
    }

    public float getGpsY() {
        return gpsY;
    }

    public void setGpsY(float gpsY) {
        this.gpsY = gpsY;
    }
}
