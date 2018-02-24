package eficode.fi.weatherapp.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import eficode.fi.weatherapp.entity.LocationInfo;

@Dao
public interface ILocationDao {
    @Insert
    void insertAll(LocationInfo... locationInfo);

    @Delete
    void deleteAll(LocationInfo locationInfo);

    @Query("SELECT * FROM LocationInfo")
    List<LocationInfo> getAll();


    @Query("Select * from LocationInfo where locationId = :locationId")
    LocationInfo getDataWithId(String locationId);

    @Query("Delete from LocationInfo where locationId = :locationId")
    void deleteId(String locationId);


}
