package io.github.storagereloaded.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import io.github.storagereloaded.android.db.AppDatabase;
import io.github.storagereloaded.api.StoRe;

public class StoReApp extends Application {

    private AppExecutors appExecutors;
    private StoRe stoRe;

    @Override
    public void onCreate() {
        super.onCreate();
        appExecutors = new AppExecutors();

        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.preferences_session_id)) && prefs.contains(getString(R.string.preferences_server_address)))
            stoRe = new StoRe(prefs.getString(getString(R.string.preferences_session_id), ""), prefs.getString(getString(R.string.preferences_server_address), ""));
        else
            stoRe = new StoRe();
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getAppDatabase(), getStoRe());
    }

    public AppDatabase getAppDatabase() {
        return AppDatabase.getInstance(this, appExecutors);
    }

    public StoRe getStoRe() {
        return stoRe;
    }
}
