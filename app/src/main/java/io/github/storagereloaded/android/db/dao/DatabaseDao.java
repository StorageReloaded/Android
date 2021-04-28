package io.github.storagereloaded.android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.github.storagereloaded.android.db.entity.DatabaseEntity;

@Dao
public interface DatabaseDao {
    @Query("SELECT * FROM databases")
    LiveData<List<DatabaseEntity>> getDatabases();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DatabaseEntity> database);
}