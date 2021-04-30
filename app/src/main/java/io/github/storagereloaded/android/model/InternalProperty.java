package io.github.storagereloaded.android.model;

public interface InternalProperty {
    int getId();

    void setId(int id);

    int getItemId();

    void setItemId(int itemId);

    int getNameId();

    int getIconId();

    Object getValue();

    void setValue(Object value);
}
