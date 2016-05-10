package com.mpier.juvenaliaapp.LineUp;

import android.os.Parcelable;

import java.io.Serializable;

public class Event implements Serializable {

    private String time;
    private String name;
    private String description;
    private int image;

    public Event(String time, String name, String description, int image) {
        this.time = time;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getTime() {
        return this.time;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getImage() {
        return this.image;
    }

}
