package io.github.storagereloaded.android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.github.storagereloaded.android.db.entity.ItemEntity;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM items WHERE databaseId=:databaseId ORDER BY name")
    LiveData<List<ItemEntity>> getItemsInDatabase(int databaseId);

    @Query("SELECT * FROM items WHERE databaseId=:databaseId AND name LIKE :searchQuery ORDER BY name")
    LiveData<List<ItemEntity>> searchItemsInDatabase(int databaseId, String searchQuery);

    @Query("SELECT * FROM items WHERE id=:itemId")
    LiveData<ItemEntity> getItem(int itemId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ItemEntity> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemEntity item);

    @Query("DELETE FROM items WHERE id=:itemId")
    void deleteItem(int itemId);
}
