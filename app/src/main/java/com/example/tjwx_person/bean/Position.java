package com.example.tjwx_person.bean;

/**
 * Created by zuo on 2016/6/26.
 */
public class Position extends ImModelData {

    String address;
    double longitude;
    double latitude;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
