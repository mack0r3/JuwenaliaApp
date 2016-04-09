package com.mpier.juvenaliaapp.LineUp;

public class Event {

    private String time;
    private String name;
    private int image;

    public Event(String time, String name, int image) {
        this.time = time;
        this.name = name;
        this.image = image;
    }

    public String getTime() {
        return this.time;
    }

    public String getName() {
        return this.name;
    }

    public int getImage() {
        return this.image;
    }

}
