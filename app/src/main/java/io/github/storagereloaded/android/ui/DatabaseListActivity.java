package io.github.storagereloaded.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.viewmodel.DatabaseViewModel;

public class DatabaseListActivity extends ListActivityBase<DatabaseEntity> {

    public static final int REQUEST_CODE = 25565;
    public static final String EXTRA_DATABASE_ID = "io.github.storagereloaded.android.database_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.title_activity_database_list);

        DatabaseViewModel model = new ViewModelProvider(this).get(DatabaseViewModel.class);
        setData(model.getDatabases(), R.layout.list_item_database, mapper, itemClickListener);

        setResult(RESULT_CANCELED);
    }

    @Override
    void onAddButtonPressed(View view) {
        startActivity(new Intent(this, TagEditActivity.class));
    }

    ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onItemClick(View view, int position, int id) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATABASE_ID, id);
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void onItemOptionsClick(View view, int position, int id) {
            Intent intent = new Intent(getApplicationContext(), DatabaseSettingsActivity.class);
            intent.putExtra(EXTRA_DATABASE_ID, id);
            startActivity(intent);
        }
    };

    ObjectViewMapper<DatabaseEntity> mapper = (view, database, position, listener) -> {
        view.setOnClickListener(v -> listener.onItemClick(view, position, database.getId()));

        ImageButton button = view.findViewById(R.id.settings_button);
        button.setOnClickListener(v -> listener.onItemOptionsClick(view, position, database.getId()));

        TextView name = view.findViewById(R.id.name);
        name.setText(database.getName());
    };
}