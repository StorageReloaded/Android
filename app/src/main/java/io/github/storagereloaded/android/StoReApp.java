package io.github.storagereloaded.android;

import android.app.Application;

public class StoReApp extends Application {

    public DataRepository getRepository() {
        return DataRepository.getInstance();
    }

}
