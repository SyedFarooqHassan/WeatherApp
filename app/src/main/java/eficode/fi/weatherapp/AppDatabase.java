package eficode.fi.weatherapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import eficode.fi.weatherapp.entity.LocationInfo;
import eficode.fi.weatherapp.interfaces.ILocationDao;

@Database(entities = {LocationInfo.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ILocationDao iLocationDao();
}
