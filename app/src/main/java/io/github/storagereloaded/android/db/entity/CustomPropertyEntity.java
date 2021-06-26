package io.github.storagereloaded.android.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.github.storagereloaded.android.model.CustomProperty;

@Entity(tableName = "custom_properties")
public class CustomPropertyEntity implements CustomProperty {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int itemId;
    private String name;
    private Object value;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public void setItemId(int itemId) {
        this.itemId = itemId;
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
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }
}