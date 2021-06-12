package io.github.storagereloaded.android.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

import io.github.storagereloaded.android.model.Item;

@Entity(tableName = "items")
public class ItemEntity implements Item {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String imagePath;
    private int locationId;
    private int amount;
    private long lastEdited;
    private long created;
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
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int getLocationId() {
        return locationId;
    }

    @Override
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public long getLastEdited() {
        return lastEdited;
    }

    @Override
    public void setLastEdited(long lastEdited) {
        this.lastEdited = lastEdited;
    }

    @Override
    public long getCreated() {
        return created;
    }

    @Override
    public void setCreated(long created) {
        this.created = created;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemEntity that = (ItemEntity) o;
        return id == that.id &&
                locationId == that.locationId &&
                amount == that.amount &&
                lastEdited == that.lastEdited &&
                created == that.created &&
                databaseId == that.databaseId &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(imagePath, that.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, imagePath, locationId, amount, lastEdited, created, databaseId);
    }
}
