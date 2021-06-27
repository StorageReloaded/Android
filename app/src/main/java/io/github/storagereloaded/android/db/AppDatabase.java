package io.github.storagereloaded.android.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

import io.github.storagereloaded.android.AppExecutors;
import io.github.storagereloaded.android.db.converter.ObjectConverter;
import io.github.storagereloaded.android.db.dao.CustomPropertyDoa;
import io.github.storagereloaded.android.db.dao.DatabaseDao;
import io.github.storagereloaded.android.db.dao.InternalPropertyDoa;
import io.github.storagereloaded.android.db.dao.ItemDao;
import io.github.storagereloaded.android.db.dao.LocationDao;
import io.github.storagereloaded.android.db.dao.TagDao;
import io.github.storagereloaded.android.db.dao.TagRelationDao;
import io.github.storagereloaded.android.db.entity.CustomPropertyEntity;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.db.entity.InternalPropertyEntity;
import io.github.storagereloaded.android.db.entity.ItemEntity;
import io.github.storagereloaded.android.db.entity.LocationEntity;
import io.github.storagereloaded.android.db.entity.TagEntity;
import io.github.storagereloaded.android.db.entity.TagRelationEntity;

@Database(entities = {DatabaseEntity.class, ItemEntity.class, InternalPropertyEntity.class, CustomPropertyEntity.class, LocationEntity.class, TagEntity.class, TagRelationEntity.class}, version = 1)
@TypeConverters(ObjectConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final boolean ADD_TEST_DATA = false;

    private static AppDatabase instance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "store";

    public abstract DatabaseDao databaseDao();

    public abstract ItemDao itemDao();

    public abstract InternalPropertyDoa internalPropertyDoa();

    public abstract CustomPropertyDoa customPropertyDoa();

    public abstract LocationDao locationDao();

    public abstract TagDao tagDao();

    public abstract TagRelationDao tagRelationDao();

    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();
    AppExecutors appExecutors;

    public static AppDatabase getInstance(Context context, AppExecutors executors) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context.getApplicationContext(), executors);
                    instance.appExecutors = executors;
                    instance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private static AppDatabase buildDatabase(Context appContext, AppExecutors executors) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME).addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                executors.diskIO().execute(() -> {
                    // Generate the data for pre-population
                    AppDatabase database = AppDatabase.getInstance(appContext, executors);

                    if(ADD_TEST_DATA) {
                        // For testing
                        List<DatabaseEntity> databases = DataGenerator.generateDatabases();
                        List<LocationEntity> locations = DataGenerator.generateLocations();
                        List<ItemEntity> items = DataGenerator.generateItems(databases, locations);
                        List<InternalPropertyEntity> internalProperties = DataGenerator.generateInternalProperties(items);
                        List<CustomPropertyEntity> customProperties = DataGenerator.generateCustomProperties(items);
                        List<TagEntity> tags = DataGenerator.generateTags();
                        List<TagRelationEntity> tagRelations = DataGenerator.generateTagRelations(tags, items);

                        // Insert data
                        database.runInTransaction(() -> {
                            database.databaseDao().insertAll(databases);
                            database.locationDao().insertAll(locations);
                            database.itemDao().insertAll(items);
                            database.internalPropertyDoa().insertAll(internalProperties);
                            database.customPropertyDoa().insertAll(customProperties);
                            database.tagDao().insertAll(tags);
                            database.tagRelationDao().insertAll(tagRelations);
                        });
                    }

                    // notify that the database was created and it's ready to be used
                    database.setDatabaseCreated();
                });
            }
        }).build();
    }

    private void updateDatabaseCreated(Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        isDatabaseCreated.postValue(true);
    }


    public LiveData<Boolean> getDatabaseCreated() {
        return isDatabaseCreated;
    }

    public void saveItem(ItemEntity item) {
        appExecutors.diskIO().execute(() -> itemDao().insert(item));
    }

    public void saveDatabase(DatabaseEntity database) {
        appExecutors.diskIO().execute(() -> databaseDao().insert(database));
    }

    public void saveTag(TagEntity tag) {
        appExecutors.diskIO().execute(() -> tagDao().insert(tag));
    }

    public void deleteItem(int itemId) {
        appExecutors.diskIO().execute(() -> itemDao().deleteItem(itemId));
    }

    public void saveLocation(LocationEntity location) {
        appExecutors.diskIO().execute(() -> locationDao().insert(location));
    }

    public void deleteLocation(int locationId) {
        appExecutors.diskIO().execute(() -> locationDao().deleteLocation(locationId));
    }

    public void deleteTag(int tagId) {
        appExecutors.diskIO().execute(() -> {
            tagDao().deleteTag(tagId);
            tagRelationDao().deleteTagRelationsWithTag(tagId);
        });
    }

    public void deleteDatabase(int databaseId) {
        appExecutors.diskIO().execute(() -> {
            databaseDao().deleteDatabase(databaseId);
            itemDao().deleteItemsInDatabase(databaseId);
            locationDao().deleteLocationsInDatabase(databaseId);
            tagRelationDao().deleteTagRelationsInDatabase(databaseId);
        });
    }
}
