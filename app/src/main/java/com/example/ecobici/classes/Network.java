package com.example.ecobici.classes;

import java.util.ArrayList;

public class Network {
    private String id;
    private String name;
    private String[] company;
    private String href;
    private ArrayList<Stations> stations;
    private Location location;

    public Network(String id, String name, String[] company, String href, ArrayList<Stations> stations, Location location) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.href = href;
        this.stations = stations;
        this.location = location;
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

    public String[] getCompany() {
        return company;
    }

    public void setCompany(String[] company) {
        this.company = company;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public ArrayList<Stations> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Stations> stations) {
        this.stations = stations;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
