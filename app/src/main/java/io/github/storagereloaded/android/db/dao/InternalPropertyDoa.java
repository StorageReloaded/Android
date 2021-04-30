package io.github.storagereloaded.android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.github.storagereloaded.android.db.entity.InternalPropertyEntity;

@Dao
public interface InternalPropertyDoa {

    @Query("SELECT * FROM internal_properties WHERE itemId=:itemId")
    LiveData<List<InternalPropertyEntity>> getInternalPropertiesFromItem(int itemId);

    @Query("SELECT * FROM internal_properties WHERE id=:propertyId")
    LiveData<InternalPropertyEntity> getInternalProperty(int propertyId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InternalPropertyEntity> internalProperties);
}
