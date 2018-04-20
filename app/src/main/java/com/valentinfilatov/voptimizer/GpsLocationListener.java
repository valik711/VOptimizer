package com.valentinfilatov.voptimizer;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.valentinfilatov.voptimizer.Adapters.StreetCoordRVAdapter;

/**
 * Created by valik on 4/16/18.
 */

public class GpsLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location loc) {
        CoordsBus.instanceOf().pushCoordinate(loc);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}