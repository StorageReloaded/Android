package io.github.storagereloaded.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.db.entity.ItemEntity;
import io.github.storagereloaded.android.db.entity.LocationEntity;
import io.github.storagereloaded.android.db.entity.TagEntity;
import io.github.storagereloaded.android.viewmodel.ItemViewModel;

public class ItemEditActivity extends AppCompatActivity {

    private static final String ITEM_NAME = "item_name";
    private static final String ITEM_DESCRIPTION = "item_description";
    private static final String ITEM_AMOUNT = "item_amount";
    private static final String LOCATION_ID = "location_id";
    private static final String TAG_IDS = "tag_ids";
    public static final String EXTRA_DATABASE_ID = "io.github.storagereloaded.android.database_id";

    int itemId = 0;

    ItemViewModel model;
    ItemEntity item;

    EditText name;
    EditText description;
    EditText amount;

    LocationAdapter locationAdapter;
    TagsAdapter tagsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        model = new ViewModelProvider(this).get(ItemViewModel.class);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        toolbar.setNavigationOnClickListener(v -> showUnsavedDialog());

        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        amount = findViewById(R.id.amount);

        LinearLayout locationLayout = findViewById(R.id.location_layout);
        TextView locationHint = findViewById(R.id.location_hint);
        locationAdapter = new LocationAdapter(locationLayout, locationHint, model);
        if (getIntent().hasExtra(EXTRA_DATABASE_ID))
            locationAdapter.setDatabaseId(getIntent().getIntExtra(EXTRA_DATABASE_ID, 0));

        LinearLayout tagLayout = findViewById(R.id.tag_layout);
        TextView tagHint = findViewById(R.id.tags_hint);
        ChipGroup tags = findViewById(R.id.tags);
        tagsAdapter = new TagsAdapter(tagLayout, tagHint, tags, model);

        Intent intent = getIntent();
        if (intent.hasExtra(ItemViewActivity.EXTRA_ITEM_ID)) {
            // Existing item should be edited
            // Set "edit" title
            toolbar.setTitle(R.string.edit_item);

            itemId = getIntent().getIntExtra(ItemViewActivity.EXTRA_ITEM_ID, 0);
            model.setItemId(itemId);
            model.getItem().observe(this, item -> {
                if (item == null)
                    return;

                this.item = item;

                // Don't overwrite ui after user stared editing
                if (!model.loaded) {
                    name.setText(item.getName());
                    description.setText(item.getDescription());
                    amount.setText(String.valueOf(item.getAmount()));

                    locationAdapter.setDatabaseId(item.getDatabaseId());
                    locationAdapter.setLocationId(item.getLocationId());

                    model.loaded = true;
                }
            });

            model.getTagsInItem().observe(this, tagEntities -> {
                if(tagEntities == null)
                    return;

                if(!model.tagsLoaded) {
                    Log.d("Test123", "setTags");
                    tagsAdapter.setTags(tagEntities);
                    model.tagsLoaded = true;
                }
            });
        }

        // Restore ui after configuration change, so the user doesn't lose his edits
        if (savedInstanceState != null) {
            name.setText(savedInstanceState.getString(ITEM_NAME));
            description.setText(savedInstanceState.getString(ITEM_DESCRIPTION));
            amount.setText(String.valueOf(savedInstanceState.getInt(ITEM_AMOUNT)));
            locationAdapter.setLocationId(savedInstanceState.getInt(LOCATION_ID));
            tagsAdapter.setTagIds(savedInstanceState.getIntegerArrayList(TAG_IDS));
        }
    }

    @Override
    public void onBackPressed() {
        showUnsavedDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveItem();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ITEM_NAME, name.getText().toString());
        outState.putString(ITEM_DESCRIPTION, description.getText().toString());
        outState.putInt(ITEM_AMOUNT, Integer.parseInt(amount.getText().toString()));
        outState.putInt(LOCATION_ID, locationAdapter.getLocationId());
        outState.putIntegerArrayList(TAG_IDS, tagsAdapter.getTagIs());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LocationListActivity.LOCATION_CHOSE_CODE)
                locationAdapter.onChooseResult(data);
            else if(requestCode == TagListActivity.TAG_CHOSE_CODE)
                tagsAdapter.onChooseResult(data);
        }
    }

    void saveItem() {
        ItemEntity item = getItemValuesFromUI();
        int itemId;
        int databaseId;

        if (this.item == null) {
            Intent intent = getIntent();
            // Item id of 0 for auto id generation
            itemId = 0;
            // For new items we need to get the database id, it should go to
            databaseId = getIntent().getIntExtra(EXTRA_DATABASE_ID, 0);
            // We also add a created date
            item.setCreated(System.currentTimeMillis());
        } else {
            // Get values from the original item
            itemId = this.item.getId();
            databaseId = this.item.getDatabaseId();
        }

        item.setId(itemId);
        item.setDatabaseId(databaseId);
        item.setLocationId(locationAdapter.getLocationId());
        item.setLastEdited(System.currentTimeMillis());

        model.saveItem(item);
        model.setTags(tagsAdapter.getTagIs());
    }

    ItemEntity getItemValuesFromUI() {
        ItemEntity item = new ItemEntity();
        item.setName(name.getText().toString());
        item.setDescription(description.getText().toString());
        item.setAmount(Integer.parseInt(amount.getText().toString()));

        return item;
    }

    private void showUnsavedDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.unsaved_dialog_title);
        builder.setMessage(R.string.unsaved_dialog_description);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    private class LocationAdapter implements View.OnClickListener {
        LinearLayout locationLayout;
        TextView locationHint;
        ItemViewModel model;
        int databaseId;
        int locationId;

        private LocationAdapter(LinearLayout locationLayout, TextView locationHint, ItemViewModel model) {
            this.locationLayout = locationLayout;
            this.locationHint = locationHint;
            this.model = model;
            this.databaseId = databaseId;
            locationLayout.setOnClickListener(this);
        }

        public void onChooseResult(Intent data) {
            setLocationId(data.getIntExtra(LocationListActivity.EXTRA_LOCATION_ID, 0));
        }

        public void setLocationId(int locationId) {
            this.locationId = locationId;
            LiveData<LocationEntity> liveData = model.getLocation(locationId);
            liveData.observe(ItemEditActivity.this, location -> {
                if (location == null)
                    return;

                locationHint.setText(location.getName());
                liveData.removeObservers(ItemEditActivity.this);
            });
        }

        public int getLocationId() {
            return locationId;
        }

        public void setDatabaseId(int databaseId) {
            this.databaseId = databaseId;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), LocationListActivity.class);
            intent.putExtra(LocationListActivity.EXTRA_DATABASE_ID, databaseId);
            intent.putExtra(LocationListActivity.EXTRA_CHOOSE_MODE, true);
            startActivityForResult(intent, LocationListActivity.LOCATION_CHOSE_CODE);
        }
    }

    private class TagsAdapter implements View.OnClickListener {

        LinearLayout tagLayout;
        TextView tagHint;
        ChipGroup tags;

        ArrayList<Integer> tagIds;

        private TagsAdapter(LinearLayout tagLayout, TextView tagHint, ChipGroup tags, ItemViewModel model) {
            this.tagLayout = tagLayout;
            this.tagHint = tagHint;
            this.tags = tags;
            tagIds = new ArrayList<>();

            tagLayout.setOnClickListener(this);
        }

        public void onChooseResult(Intent data) {
            addTagId(data.getIntExtra(TagListActivity.EXTRA_TAG_ID, 0));
        }

        public void setTags(List<TagEntity> tags) {
            if(tags.isEmpty())
                return;

            tagHint.setVisibility(View.GONE);

            for(TagEntity tag : tags){
                if(!tagIds.contains(tag.getId()))
                    addTag(tag);
            }
        }

        public void setTagIds(List<Integer> tagIds) {
            LiveData<List<TagEntity>> liveData = model.getTagsInItem();
            liveData.observe(ItemEditActivity.this, tags -> {
                if (tags == null)
                    return;

                for(TagEntity tag : tags){
                    if(tagIds.contains(tag.getId()))
                        addTag(tag);
                }
                liveData.removeObservers(ItemEditActivity.this);
            });
        }

        public ArrayList<Integer> getTagIs() {
            return tagIds;
        }

        private void addTagId(int tagId) {
            tagHint.setVisibility(View.GONE);

            if(tagIds.contains(tagId))
                return;

            LiveData<TagEntity> liveData = model.getTag(tagId);
            liveData.observe(ItemEditActivity.this, tag -> {
                if (tag == null)
                    return;

                addTag(tag);
                liveData.removeObservers(ItemEditActivity.this);
            });
        }

        private void addTag(TagEntity tag) {
            tagIds.add(tag.getId());

            int index = tags.getChildCount();
            Chip chip = new Chip(tags.getContext());
            chip.setText(tag.getName());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {
                tags.removeViewAt(index);
                tagIds.remove(index);
                if(tagIds.isEmpty())
                    tagHint.setVisibility(View.VISIBLE);
            });
            tags.addView(chip);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), TagListActivity.class);
            intent.putExtra(TagListActivity.EXTRA_CHOOSE_MODE, true);
            startActivityForResult(intent, TagListActivity.TAG_CHOSE_CODE);
        }
    }
}