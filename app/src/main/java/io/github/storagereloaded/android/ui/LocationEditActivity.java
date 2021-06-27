package io.github.storagereloaded.android.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.db.entity.LocationEntity;
import io.github.storagereloaded.android.viewmodel.LocationViewModel;

public class LocationEditActivity extends AppCompatActivity {

    public static final String LOCATION_NAME = "tag_name";
    public static final String DATABASE_INDEX = "database_index";

    EditText name;
    AutoCompleteTextView databaseSelect;
    TextInputLayout databaseInputLayout;
    int databaseSelectionIndex = 0;

    LocationViewModel model;
    LocationEntity location;
    List<DatabaseEntity> databases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_edit);

        model = new ViewModelProvider(this).get(LocationViewModel.class);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        name = ((TextInputLayout) findViewById(R.id.location_name)).getEditText();

        databaseInputLayout = findViewById(R.id.database_select);
        databaseSelect = (AutoCompleteTextView) databaseInputLayout.getEditText();
        ArrayAdapter<DatabaseEntity> adapter = new ArrayAdapter<>(this, R.layout.list_item_database_select);
        databaseSelect.setAdapter(adapter);
        databaseSelect.setOnItemClickListener((parent, view, position, id) -> databaseSelectionIndex = position);

        if (getIntent().hasExtra(LocationListActivity.EXTRA_DATABASE_ID))
            databaseInputLayout.setVisibility(View.GONE);

        model.getDatabases().observe(this, databases -> {
            if (databases == null)
                return;

            this.databases = databases;
            adapter.clear();
            adapter.addAll(databases);

            // Very hacky solution because the material exposed dropdown has no getSelection or setSelection methods :(
            if (savedInstanceState == null) {
                if (location != null)
                    databaseSelect.setText(getDatabaseNameForLocation(location), false);
            } else
                databaseSelect.setText(databases.get(savedInstanceState.getInt(DATABASE_INDEX, 0)).toString(), false);
        });

        if (getIntent().hasExtra(LocationListActivity.EXTRA_LOCATION_ID)) {
            // Existing tag should be edited
            model.getLocation(getIntent().getIntExtra(LocationListActivity.EXTRA_LOCATION_ID, 0)).observe(this, location -> {
                if (location == null)
                    return;

                this.location = location;

                if (!model.loaded) {
                    name.setText(location.getName());

                    // Very hacky solution because the material exposed dropdown has no getSelection or setSelection methods :(
                    if (databases != null)
                        databaseSelect.setText(getDatabaseNameForLocation(location), false);

                    model.loaded = true;
                }
            });
        }

        if (savedInstanceState != null) {
            name.setText(savedInstanceState.getString(LOCATION_NAME));
            databaseSelectionIndex = savedInstanceState.getInt(DATABASE_INDEX);
        }
    }

    @Override
    public void onBackPressed() {
        if (name.getText().toString().equals(""))
            finish();
        else
            showUnsavedDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveLocation();
            finish();
            return true;
        } else if(item.getItemId() == R.id.delete) {
            model.deleteLocation(location.getId());
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LOCATION_NAME, name.getText().toString());
        outState.putInt(DATABASE_INDEX, databaseSelectionIndex);
    }

    private void showUnsavedDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.unsaved_dialog_title);
        builder.setMessage(R.string.unsaved_dialog_description);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    private void saveLocation() {
        if (location == null) {
            location = new LocationEntity();
            location.setId(0);
        }
        if (getIntent().hasExtra(LocationListActivity.EXTRA_DATABASE_ID))
            location.setDatabaseId(getIntent().getIntExtra(LocationListActivity.EXTRA_DATABASE_ID, location.getDatabaseId()));
        else
            location.setDatabaseId(databases.get(databaseSelectionIndex).getId());
        location.setName(name.getText().toString());
        model.saveLocation(location);
    }

    private String getDatabaseNameForLocation(LocationEntity location) {
        for (DatabaseEntity database : databases) {
            if (database.getId() == location.getDatabaseId())
                return database.getName();
        }

        return "";
    }
}