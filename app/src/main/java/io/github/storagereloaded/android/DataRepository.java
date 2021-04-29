package io.github.storagereloaded.android;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.github.storagereloaded.android.db.AppDatabase;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.api.Database;
import io.github.storagereloaded.api.Item;
import io.github.storagereloaded.api.Location;
import io.github.storagereloaded.api.Property;
import io.github.storagereloaded.api.Tag;

public class DataRepository {

    private static DataRepository instance;

    private AppDatabase appDatabase;

    private DataRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    public static DataRepository getInstance(AppDatabase appDatabase) {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository(appDatabase);
                }
            }
        }
        return instance;
    }

    public LiveData<List<DatabaseEntity>> getDatabases() {
        return appDatabase.databaseDao().getDatabases();
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

    public LiveData<DatabaseEntity> getDatabase(int databaseId) {
        return appDatabase.databaseDao().getDatabase(databaseId);
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