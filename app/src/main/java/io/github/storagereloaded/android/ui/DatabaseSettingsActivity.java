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
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.viewmodel.DatabaseViewModel;

public class DatabaseSettingsActivity extends AppCompatActivity {

    public static final String DATABASE_NAME = "database_name";

    EditText name;

    DatabaseViewModel model;
    DatabaseEntity database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_settings);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        name = ((TextInputLayout) findViewById(R.id.database_name)).getEditText();

        model = new ViewModelProvider(this).get(DatabaseViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra(DatabaseListActivity.EXTRA_DATABASE_ID)) {
            // Existing database should be edited
            model.setDatabaseId(intent.getIntExtra(DatabaseListActivity.EXTRA_DATABASE_ID, 0));
            model.getDatabase().observe(this, database -> {
                if (database == null)
                    return;

                this.database = database;

                if (!model.loaded) {
                    name.setText(database.getName());
                    model.loaded = true;
                }
            });
        }

        if (savedInstanceState != null)
            name.setText(savedInstanceState.getString(DATABASE_NAME));
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
            saveDatabase();
            finish();
            return true;
        } else if(item.getItemId() == R.id.delete) {
            model.deleteDatabase(database.getId());
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DATABASE_NAME, name.getText().toString());
    }

    private void showUnsavedDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.unsaved_dialog_title);
        builder.setMessage(R.string.unsaved_dialog_description);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    private void saveDatabase() {
        if(database == null) {
            database = new DatabaseEntity();
            database.setId(0);
        }
        database.setName(name.getText().toString());
        model.saveDatabase(database);
    }
}