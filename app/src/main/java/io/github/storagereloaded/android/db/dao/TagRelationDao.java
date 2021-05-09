package io.github.storagereloaded.android.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

import io.github.storagereloaded.android.db.entity.TagRelationEntity;

@Dao
public interface TagRelationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TagRelationEntity> tagRelations);
}
