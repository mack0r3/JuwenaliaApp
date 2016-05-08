package com.mpier.juvenaliaapp.Attractions;

public class Attraction {
    private String attrName;
    private int attrImgRes;
    private String description;

    public Attraction(String attrName, int attrImgRes, String description) {
        this.attrName = attrName;
        this.attrImgRes = attrImgRes;
        this.description = description;
    }

    public String getAttrName() {
        return attrName;
    }

    public int getAttrImgRes() {
        return attrImgRes;
    }

    public String getDescription() {
        return description;
    }
}
