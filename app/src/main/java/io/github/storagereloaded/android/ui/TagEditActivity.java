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
import io.github.storagereloaded.android.db.entity.TagEntity;
import io.github.storagereloaded.android.viewmodel.TagViewModel;

public class TagEditActivity extends AppCompatActivity {

    public static final String TAG_NAME = "tag_name";

    EditText name;

    TagViewModel model;
    TagEntity tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_edit);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        name = ((TextInputLayout) findViewById(R.id.tag_name)).getEditText();

        model = new ViewModelProvider(this).get(TagViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra(TagListActivity.EXTRA_TAG_ID)) {
            // Existing tag should be edited
            model.getTag(intent.getIntExtra(TagListActivity.EXTRA_TAG_ID, 0)).observe(this, tag -> {
                if (tag == null)
                    return;

                this.tag = tag;

                if (!model.loaded) {
                    name.setText(tag.getName());
                    model.loaded = true;
                }
            });
        }

        if (savedInstanceState != null)
            name.setText(savedInstanceState.getString(TAG_NAME));
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
        } else if (item.getItemId() == R.id.delete) {
            model.deleteTag(tag.getId());
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAG_NAME, name.getText().toString());
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
        if (tag == null) {
            tag = new TagEntity();
            tag.setId(0);
        }
        tag.setName(name.getText().toString());
        model.saveTag(tag);
    }
}