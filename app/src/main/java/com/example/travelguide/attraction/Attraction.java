package com.example.travelguide.attraction;

public class Attraction {
    private int imageResId;
    private String title;

    public Attraction(int imageResId, String title) {
        this.imageResId = imageResId;
        this.title = title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }
}