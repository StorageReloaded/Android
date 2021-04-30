package io.github.storagereloaded.android.model;

public interface CustomProperty {
    int getId();

    void setId(int id);

    int getItemId();

    void setItemId(int itemId);

    String getName();

    void setName(String name);

    Object getValue();

    void setValue(Object value);
}
