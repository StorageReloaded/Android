package io.github.storagereloaded.android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.github.storagereloaded.android.DataRepository;
import io.github.storagereloaded.android.StoReApp;
import io.github.storagereloaded.android.db.entity.CustomPropertyEntity;
import io.github.storagereloaded.android.db.entity.InternalPropertyEntity;
import io.github.storagereloaded.android.db.entity.ItemEntity;
import io.github.storagereloaded.android.db.entity.LocationEntity;
import io.github.storagereloaded.android.db.entity.TagEntity;
import io.github.storagereloaded.api.Location;

public class ItemViewModel extends AndroidViewModel {

    private final DataRepository repository;
    private int itemId;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        this.repository = ((StoReApp) application).getRepository();
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public LiveData<ItemEntity> getItem() {
        return repository.getItem(itemId);
    }

    public LiveData<LocationEntity> getLocation(int locationId) {
        return repository.getLocation(locationId);
    }

    public LiveData<List<TagEntity>> getTags() {
        return repository.getTagsInItem(itemId);
    }

    public LiveData<List<CustomPropertyEntity>> getCustomProperties() {
        return repository.getCustomPropertiesInItem(itemId);
    }

    public LiveData<List<InternalPropertyEntity>> getInternalProperties() {
        return repository.getInternalPropertiesInItem(itemId);
    }
}
