package eficode.fi.weatherapp.interfaces;

import android.location.Location;

public interface ILocationHelper {
    void onLocationChanged(final Location location);
}
