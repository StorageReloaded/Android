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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LocationEntity> locations);
}
