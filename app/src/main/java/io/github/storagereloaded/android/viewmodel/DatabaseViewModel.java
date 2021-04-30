package io.github.storagereloaded.android.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;

import java.util.List;

import io.github.storagereloaded.android.DataRepository;
import io.github.storagereloaded.android.StoReApp;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.db.entity.ItemEntity;
import io.github.storagereloaded.api.Item;

public class DatabaseViewModel extends AndroidViewModel {

    private static final String SEARCH_QUERY_KEY = "SEARCH_QUERY";

    private final DataRepository repository;
    private final SavedStateHandle savedStateHandle;
    private final LiveData<List<ItemEntity>> items;
    private int databaseId;

    public DatabaseViewModel(@NonNull Application application, @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        this.savedStateHandle = savedStateHandle;
        this.repository = ((StoReApp) application).getRepository();

        items = Transformations.switchMap(savedStateHandle.getLiveData(SEARCH_QUERY_KEY, null), (Function<CharSequence, LiveData<List<ItemEntity>>>) searchQuery -> {
            if (TextUtils.isEmpty(searchQuery)) {
                return repository.getItemsInDatabase(databaseId);
            }
            return repository.searchItemsInDatabase(databaseId, "%" + searchQuery + "%");
        });
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public void setSearchQuery(String searchQuery) {
        savedStateHandle.set(SEARCH_QUERY_KEY, searchQuery);
    }

    public LiveData<List<ItemEntity>> getItems() {
        return items;
    }

    public LiveData<DatabaseEntity> getDatabase() {
        return repository.getDatabase(databaseId);
    }

    public LiveData<List<DatabaseEntity>> getDatabases() {
        return repository.getDatabases();
    }
}
