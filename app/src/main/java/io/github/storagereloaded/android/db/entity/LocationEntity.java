package io.github.storagereloaded.android.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.github.storagereloaded.android.model.Location;

@Entity(tableName = "locations")
public class LocationEntity implements Location {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int databaseId;

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
    public int getDatabaseId() {
        return databaseId;
    }

    @Override
    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    @Override
    public String toString() {
        return name;
    }
}
