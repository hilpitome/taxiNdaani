package com.ndaani.taxi.taxindaani.model;

/**
 * Created by hilary on 9/10/17.
 */

public class CurrentLocation {
    double latitude;
    double longitude;
    public CurrentLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
