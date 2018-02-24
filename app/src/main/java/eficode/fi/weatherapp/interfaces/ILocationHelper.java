package eficode.fi.weatherapp.interfaces;

import android.location.Location;

import java.io.Serializable;

public interface ILocationHelper extends Serializable {
    void onLocationChanged(final Location location);
}
