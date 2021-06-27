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
    @Query("SELECT * FROM databases ORDER BY name")
    LiveData<List<DatabaseEntity>> getDatabases();

    @Query("SELECT * FROM databases WHERE id=:databaseId")
    LiveData<DatabaseEntity> getDatabase(int databaseId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DatabaseEntity> database);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DatabaseEntity database);

    @Query("DELETE FROM databases WHERE id=:databaseId")
    void deleteDatabase(int databaseId);
}
