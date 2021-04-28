package io.github.storagereloaded.android;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.github.storagereloaded.api.Database;
import io.github.storagereloaded.api.Item;
import io.github.storagereloaded.api.Location;
import io.github.storagereloaded.api.Property;
import io.github.storagereloaded.api.Tag;

public class DataRepository {

    private static DataRepository sInstance;

    private DataRepository() {

    }

    public static DataRepository getInstance() {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository();
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Database>> getDatabases() {
        return null; // TODO
    }

    public LiveData<List<Item>> getItems() {
        return null; // TODO
    }

    public LiveData<List<Location>> getLocations() {
        return null; // TODO
    }

    public LiveData<List<Tag>> getTags() {
        return null; // TODO
    }

    public LiveData<List<Item>> getItemsInDatabase(int databaseId) {
        return null; // TODO
    }

    public LiveData<List<Tag>> getTagsInItem(int itemId) {
        return null; // TODO
    }

    public LiveData<List<Property>> getPropertiesInItem(int itemId) {
        return null; // TODO
    }

    public LiveData<Database> getDatabase(int databaseId) {
        return null; // TODO
    }

    public LiveData<Item> getItem(int itemId) {
        return null; // TODO
    }

    public LiveData<Location> getLocation(int locationId) {
        return null; // TODO
    }

    public LiveData<Tag> getTag(int tagId) {
        return null; // TODO
    }

    public LiveData<Property> getProperty(int propertyId) {
        return null; // TODO
    }
}