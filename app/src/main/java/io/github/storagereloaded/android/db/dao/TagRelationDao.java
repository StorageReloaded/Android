package io.github.storagereloaded.android.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.github.storagereloaded.android.db.entity.TagRelationEntity;

@Dao
public interface TagRelationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TagRelationEntity> tagRelations);

    @Query("DELETE FROM tag_relations WHERE id=:tagRelationId")
    void deleteTagRelation(int tagRelationId);

    @Query("DELETE FROM tag_relations WHERE tagId=:tagId")
    void deleteTagRelationsWithTag(int tagId);

    @Query("DELETE FROM tag_relations WHERE itemId=:itemId")
    void deleteTagRelationsWithItem(int itemId);

    @Query("DELETE FROM tag_relations  WHERE itemId IN (SELECT itemId FROM tag_relations t INNER JOIN items i ON t.itemId=t.id WHERE i.id=:databaseId);")
    void deleteTagRelationsInDatabase(int databaseId);
}

