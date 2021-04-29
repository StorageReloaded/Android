package io.github.storagereloaded.android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.github.storagereloaded.android.DataRepository;
import io.github.storagereloaded.android.StoReApp;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;

public class DatabaseListViewModel extends AndroidViewModel {

    private final DataRepository repository;

    public DatabaseListViewModel(@NonNull Application application) {
        super(application);
        this.repository = ((StoReApp) application).getRepository();
    }

    public LiveData<List<DatabaseEntity>> getDatabases() {
        return repository.getDatabases();
    }
}
