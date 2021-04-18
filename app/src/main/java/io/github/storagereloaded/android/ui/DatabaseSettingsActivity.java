package io.github.storagereloaded.android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import io.github.storagereloaded.android.R;

public class DatabaseSettingsActivity extends AppCompatActivity {

    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_settings);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TextInputLayout til = findViewById(R.id.database_name);
        name = til.getEditText();
    }

    @Override
    public void onBackPressed() {
        if (name.getText().toString().equals(""))
            finish();
        else
            showUnsavedDialog();
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