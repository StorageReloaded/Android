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
    @Query("SELECT DISTINCT tags.id, tags.name FROM tags, tag_relations WHERE (tags.id=tag_relations.tagId AND tag_relations.itemId=:itemId) ORDER BY tags.name")
    LiveData<List<TagEntity>> getTagsInItem(int itemId);

    @Query("SELECT * FROM tags ORDER BY name")
    LiveData<List<TagEntity>> getTags();

    @Query("SELECT * FROM tags WHERE id=:tagId")
    LiveData<TagEntity> getTag(int tagId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TagEntity> tags);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TagEntity tag);

    @Query("DELETE FROM tags WHERE id=:tagId")
    void deleteTag(int tagId);
}
