package io.github.storagereloaded.android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.github.storagereloaded.android.DataRepository;
import io.github.storagereloaded.android.StoReApp;
import io.github.storagereloaded.api.Database;
import io.github.storagereloaded.api.Item;

public class DatabaseViewModel extends AndroidViewModel {

    private final DataRepository repository;
    private int databaseId;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        this.repository = ((StoReApp) application).getRepository();
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public LiveData<List<Item>> getItems() {
        return repository.getItemsInDatabase(databaseId);
    }

    public LiveData<Database> getDatabase() {
        return repository.getDatabase(databaseId);
    }
}
