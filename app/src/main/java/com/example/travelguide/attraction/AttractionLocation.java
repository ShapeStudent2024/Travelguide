package com.example.travelguide.attraction;

import com.google.android.gms.maps.model.LatLng;

public class AttractionLocation {

    public enum Type {
        THEME_PARK,      // 主题公园
        TRANSPORTATION,  // 交通景点
        SIGHTSEEING,     // 观光景点
        ENTERTAINMENT    // 娱乐区域
    }

    private String chineseName;
    private String englishName;
    private LatLng latLng;
    private String description;
    private float rating;
    private String distance;
    private int imageResId;
    private Type type;

    public AttractionLocation(String chineseName, String englishName, LatLng latLng,
                              String description, float rating, String distance,
                              int imageResId, Type type) {
        this.chineseName = chineseName;
        this.englishName = englishName;
        this.latLng = latLng;
        this.description = description;
        this.rating = rating;
        this.distance = distance;
        this.imageResId = imageResId;
        this.type = type;
    }

    // Getters
    public String getChineseName() { return chineseName; }
    public String getEnglishName() { return englishName; }
    public LatLng getLatLng() { return latLng; }
    public String getDescription() { return description; }
    public float getRating() { return rating; }
    public String getDistance() { return distance; }
    public int getImageResId() { return imageResId; }
    public Type getType() { return type; }

    // Setters
    public void setChineseName(String chineseName) { this.chineseName = chineseName; }
    public void setEnglishName(String englishName) { this.englishName = englishName; }
    public void setLatLng(LatLng latLng) { this.latLng = latLng; }
    public void setDescription(String description) { this.description = description; }
    public void setRating(float rating) { this.rating = rating; }
    public void setDistance(String distance) { this.distance = distance; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
    public void setType(Type type) { this.type = type; }
}