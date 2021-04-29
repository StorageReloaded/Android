package io.github.storagereloaded.android.model;

public interface Item {
    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    String getImagePath();

    void setImagePath(String imagePath);

    int getLocationId();

    void setLocationId(int locationId);

    int getAmount();

    void setAmount(int amount);

    long getLastEdited();

    void setLastEdited(long lastEdited);

    long getCreated();

    void setCreated(long created);

    int getDatabaseId();

    void setDatabaseId(int databaseId);
}
