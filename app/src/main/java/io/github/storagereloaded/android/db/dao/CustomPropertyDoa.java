package io.github.storagereloaded.android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.github.storagereloaded.android.db.entity.CustomPropertyEntity;

@Dao
public interface CustomPropertyDoa {

    @Query("SELECT * FROM custom_properties WHERE itemId=:itemId")
    LiveData<List<CustomPropertyEntity>> getCustomPropertiesFromItem(int itemId);

    @Query("SELECT * FROM custom_properties WHERE id=:propertyId")
    LiveData<CustomPropertyEntity> getCustomProperty(int propertyId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CustomPropertyEntity> internalProperties);
}
