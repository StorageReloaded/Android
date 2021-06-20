package io.github.storagereloaded.android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.github.storagereloaded.android.DataRepository;
import io.github.storagereloaded.android.StoReApp;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.db.entity.LocationEntity;

public class LocationViewModel extends AndroidViewModel {

    private final DataRepository repository;
    public boolean loaded = false;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        this.repository = ((StoReApp) application).getRepository();
    }

    public LiveData<List<LocationEntity>> getLocations() {
        return repository.getLocations();
    }

    public LiveData<LocationEntity> getLocation(int locationId) {
        return repository.getLocation(locationId);
    }

    public LiveData<List<LocationEntity>> getLocationsFromDatabase(int databaseId) {
        return repository.getLocationsFromDatabase(databaseId);
    }

    public void saveLocation(LocationEntity location) {
        repository.saveLocation(location);
    }

    public LiveData<List<DatabaseEntity>> getDatabases() {
        return repository.getDatabases();
    }
}
