package com.ecobici.app.classes;

public class Location {
    private Double latitude;
    private Double longitude;
    private String city;
    private String country;

    public Location(Double latitude, Double longitude, String city, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
