package eficode.fi.weatherapp.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class LocationInfo implements Serializable {
    @NonNull
    @PrimaryKey
    private String locationId;

    public LocationInfo() {

    }

    public LocationInfo(@NonNull String locationId, String locationName, double latitude, double longitude) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @ColumnInfo(name = "location_name")
    private String locationName;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
