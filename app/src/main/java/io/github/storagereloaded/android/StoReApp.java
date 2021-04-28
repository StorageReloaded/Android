package io.github.storagereloaded.android;

import android.app.Application;

import io.github.storagereloaded.android.db.AppDatabase;

public class StoReApp extends Application {

    private AppExecutors appExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        appExecutors = new AppExecutors();
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, appExecutors);
    }
}
