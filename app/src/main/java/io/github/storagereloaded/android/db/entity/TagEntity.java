package io.github.storagereloaded.android.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

import io.github.storagereloaded.android.model.Tag;
import io.github.storagereloaded.android.ui.ListActivityBase;

@Entity(tableName = "tags")
public class TagEntity implements Tag, ListActivityBase.IdObject {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagEntity tagEntity = (TagEntity) o;
        return id == tagEntity.id &&
                Objects.equals(name, tagEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
