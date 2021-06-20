package io.github.storagereloaded.android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.github.storagereloaded.android.db.entity.LocationEntity;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM locations WHERE id=:locationId")
    LiveData<LocationEntity> getLocation(int locationId);

    @Query("SELECT * FROM locations ORDER BY name")
    LiveData<List<LocationEntity>> getLocations();

    @Query("SELECT * FROM locations WHERE databaseId=:databaseId ORDER BY name")
    LiveData<List<LocationEntity>> getLocationsFromDatabase(int databaseId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LocationEntity> locations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocationEntity locations);

    @Query("DELETE FROM locations WHERE id=:locationId")
    void deleteLocation(int locationId);
}
