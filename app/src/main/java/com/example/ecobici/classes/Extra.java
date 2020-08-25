package com.example.ecobici.classes;

public class Extra {
    private int[] NearbyStationList;
    private String address;
    private String districtCode;
    private String status;
    private int uid;
    private String zip;

    public Extra(int[] nearbyStationList, String address, String districtCode, String status, int uid, String zip) {
        NearbyStationList = nearbyStationList;
        this.address = address;
        this.districtCode = districtCode;
        this.status = status;
        this.uid = uid;
        this.zip = zip;
    }

    public int[] getNearbyStationList() {
        return NearbyStationList;
    }

    public void setNearbyStationList(int[] nearbyStationList) {
        NearbyStationList = nearbyStationList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
