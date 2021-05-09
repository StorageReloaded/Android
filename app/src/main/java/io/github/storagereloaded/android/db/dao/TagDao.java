package io.github.storagereloaded.android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.github.storagereloaded.android.db.entity.TagEntity;

@Dao
public interface TagDao {
    @Query("SELECT DISTINCT tags.id, tags.name FROM tags, tag_relations WHERE tags.id=tag_relations.tagId AND tag_relations.itemId=:itemId")
    LiveData<List<TagEntity>> getTagsInItem(int itemId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TagEntity> tags);
}
