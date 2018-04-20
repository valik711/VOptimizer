package com.valentinfilatov.voptimizer.POJO;

/**
 * Created by valik on 4/20/18.
 */

public class Coordinate {
    private double lat;
    private double lng;

    public Coordinate(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
