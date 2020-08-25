package com.example.ecobici.classes;

public class Stations {
    private String id;
    private String name;
    private String timestamp;
    private int free_bikes;
    private int empty_slots;
    private Double longitude;
    private Double latitude;
    private Extra extra;

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public Stations(String id, String name, String timestamp, int free_bikes, int empty_slots, Double longitude, Double latitude, Extra extra) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.free_bikes = free_bikes;
        this.empty_slots = empty_slots;
        this.longitude = longitude;
        this.latitude = latitude;
        this.extra = extra;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getFree_bikes() {
        return free_bikes;
    }

    public void setFree_bikes(int free_bikes) {
        this.free_bikes = free_bikes;
    }

    public int getEmpty_slots() {
        return empty_slots;
    }

    public void setEmpty_slots(int empty_slots) {
        this.empty_slots = empty_slots;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
