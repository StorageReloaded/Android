package io.github.storagereloaded.android;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.github.storagereloaded.android.db.AppDatabase;
import io.github.storagereloaded.android.db.entity.CustomPropertyEntity;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.db.entity.InternalPropertyEntity;
import io.github.storagereloaded.android.db.entity.ItemEntity;
import io.github.storagereloaded.android.db.entity.LocationEntity;
import io.github.storagereloaded.android.db.entity.TagEntity;
import io.github.storagereloaded.api.StoRe;

public class DataRepository {

    private static DataRepository instance;

    private AppDatabase appDatabase;
    private StoRe stoRe;

    private DataRepository(AppDatabase appDatabase, StoRe stoRe) {
        this.appDatabase = appDatabase;
        this.stoRe = stoRe;
    }

    public static DataRepository getInstance(AppDatabase appDatabase, StoRe stoRe) {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository(appDatabase, stoRe);
                }
            }
        }
        return instance;
    }

    public LiveData<List<DatabaseEntity>> getDatabases() {
        return appDatabase.databaseDao().getDatabases();
    }

    public LiveData<List<ItemEntity>> getItemsInDatabase(int databaseId) {
        return appDatabase.itemDao().getItemsInDatabase(databaseId);
    }

    public LiveData<List<ItemEntity>> searchItemsInDatabase(int databaseId, String searchQuery) {
        return appDatabase.itemDao().searchItemsInDatabase(databaseId, searchQuery);
    }

    public LiveData<List<TagEntity>> getTagsInItem(int itemId) {
        return appDatabase.tagDao().getTagsInItem(itemId);
    }

    public LiveData<List<InternalPropertyEntity>> getInternalPropertiesInItem(int itemId) {
        return appDatabase.internalPropertyDoa().getInternalPropertiesFromItem(itemId);
    }

    public LiveData<List<CustomPropertyEntity>> getCustomPropertiesInItem(int itemId) {
        return appDatabase.customPropertyDoa().getCustomPropertiesFromItem(itemId);
    }

    public LiveData<DatabaseEntity> getDatabase(int databaseId) {
        return appDatabase.databaseDao().getDatabase(databaseId);
    }

    public LiveData<ItemEntity> getItem(int itemId) {
        return appDatabase.itemDao().getItem(itemId);
    }

    public LiveData<LocationEntity> getLocation(int locationId) {
        return appDatabase.locationDao().getLocation(locationId);
    }

    public LiveData<List<TagEntity>> getTags() {
        return appDatabase.tagDao().getTags();
    }

    public LiveData<TagEntity> getTag(int tagId) {
        return appDatabase.tagDao().getTag(tagId);
    }

    public LiveData<List<LocationEntity>> getLocations() {
        return appDatabase.locationDao().getLocations();
    }

    public LiveData<List<LocationEntity>> getLocationsFromDatabase(int databaseId) {
        return appDatabase.locationDao().getLocationsFromDatabase(databaseId);
    }

    public void saveItem(ItemEntity item) {
        appDatabase.saveItem(item);
    }

    public void saveDatabase(DatabaseEntity database) {
        appDatabase.saveDatabase(database);
    }

    public void saveTag(TagEntity tag) {
        appDatabase.saveTag(tag);
    }

    public void saveLocation(LocationEntity location) {
        appDatabase.saveLocation(location);
    }

    public void deleteItem(int itemId) {
        appDatabase.deleteItem(itemId);
    }

    public void deleteLocation(int locationId) {
        appDatabase.deleteLocation(locationId);
    }

    public void deleteTag(int tagId) {
        appDatabase.deleteTag(tagId);
    }

    public void deleteDatabase(int databaseId) {
        appDatabase.deleteDatabase(databaseId);
    }

    public LiveData<Boolean> checkConnection() {
        LiveData<Boolean> data = new LiveData<Boolean>() {
        };

        stoRe.info();
        return data;
    }

    public boolean isUserLoggedIn() {
        return stoRe.hasLogin();
    }


}