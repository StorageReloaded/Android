package io.github.storagereloaded.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.db.entity.ItemEntity;
import io.github.storagereloaded.android.viewmodel.ItemViewModel;

public class ItemEditActivity extends AppCompatActivity {

    private static final String ITEM_NAME = "item_name";
    private static final String ITEM_DESCRIPTION = "item_description";
    private static final String ITEM_AMOUNT = "item_amount";
    public static final String EXTRA_DATABASE_ID = "io.github.storagereloaded.android.database_id";

    int itemId = 0;

    ItemViewModel model;
    ItemEntity item;

    EditText name;
    EditText description;
    EditText amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        model = new ViewModelProvider(this).get(ItemViewModel.class);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        toolbar.setNavigationOnClickListener(v -> showUnsavedDialog());

        name = ((TextInputLayout) findViewById(R.id.name)).getEditText();
        description = ((TextInputLayout) findViewById(R.id.description)).getEditText();
        amount = ((TextInputLayout) findViewById(R.id.amount)).getEditText();

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
                    model.loaded = true;
                }
            });
        }

        // Restore ui after configuration change, so the user doesn't lose his edits
        if (savedInstanceState != null) {
            name.setText(savedInstanceState.getString(ITEM_NAME));
            description.setText(savedInstanceState.getString(ITEM_DESCRIPTION));
            amount.setText(String.valueOf(savedInstanceState.getInt(ITEM_AMOUNT)));
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
        item.setLastEdited(System.currentTimeMillis());

        model.saveItem(item);
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
}