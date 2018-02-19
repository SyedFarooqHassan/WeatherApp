package eficode.fi.weatherapp;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import eficode.fi.weatherapp.interfaces.ILocationHelper;

public class LocationListener implements android.location.LocationListener {
    private ILocationHelper iLocationHelper;

    public LocationListener(final ILocationHelper iLocationHelper) {
        this.iLocationHelper = iLocationHelper;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        iLocationHelper.onLocationChanged(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}
