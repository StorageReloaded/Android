package io.github.storagereloaded.android.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.util.List;

import io.github.storagereloaded.android.AppExecutors;
import io.github.storagereloaded.android.db.converter.ObjectConverter;
import io.github.storagereloaded.android.db.dao.CustomPropertyDoa;
import io.github.storagereloaded.android.db.dao.DatabaseDao;
import io.github.storagereloaded.android.db.dao.InternalPropertyDoa;
import io.github.storagereloaded.android.db.dao.ItemDao;
import io.github.storagereloaded.android.db.dao.LocationDao;
import io.github.storagereloaded.android.db.entity.CustomPropertyEntity;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.db.entity.InternalPropertyEntity;
import io.github.storagereloaded.android.db.entity.ItemEntity;
import io.github.storagereloaded.android.db.entity.LocationEntity;

@Database(entities = {DatabaseEntity.class, ItemEntity.class, InternalPropertyEntity.class, CustomPropertyEntity.class, LocationEntity.class}, version = 1)
@TypeConverters(ObjectConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "store";

    public abstract DatabaseDao databaseDao();

    public abstract ItemDao itemDao();

    public abstract InternalPropertyDoa internalPropertyDoa();

    public abstract CustomPropertyDoa customPropertyDoa();

    public abstract LocationDao locationDao();

    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(Context context, AppExecutors executors) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context.getApplicationContext(), executors);
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

                    // For testing
                    List<DatabaseEntity> databases = DataGenerator.generateDatabases();
                    List<LocationEntity> locations = DataGenerator.generateLocations();
                    List<ItemEntity> items = DataGenerator.generateItems(databases, locations);
                    List<InternalPropertyEntity> internalProperties = DataGenerator.generateInternalProperties(items);
                    List<CustomPropertyEntity> customProperties = DataGenerator.generateCustomProperties(items);

                    // Insert data
                    database.runInTransaction(() -> {
                        database.databaseDao().insertAll(databases);
                        database.locationDao().insertAll(locations);
                        database.itemDao().insertAll(items);
                        database.internalPropertyDoa().insertAll(internalProperties);
                        database.customPropertyDoa().insertAll(customProperties);
                    });

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
}
