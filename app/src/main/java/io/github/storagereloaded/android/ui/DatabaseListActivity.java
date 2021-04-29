package io.github.storagereloaded.android.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.viewmodel.DatabaseListViewModel;

public class DatabaseListActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 25565;
    public static final String EXTRA_DATABASE_ID = "io.github.storagereloaded.android.database_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_list);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.database_list);
        RecyclerDatabaseListAdapter adapter = new RecyclerDatabaseListAdapter(new OnItemClickListener() {
            @Override
            public void onItemClick(int index, int databaseId) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DATABASE_ID, databaseId);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onItemSettingsClick(int index, int databaseId) {
                startActivity(new Intent(getApplicationContext(), DatabaseSettingsActivity.class));
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        setResult(RESULT_CANCELED);

        DatabaseListViewModel model = new ViewModelProvider(this).get(DatabaseListViewModel.class);
        model.getDatabases().observe(this, adapter::setDatabases);
    }

    private interface OnItemClickListener {
        void onItemClick(int index, int databaseId);

        void onItemSettingsClick(int index, int databaseId);
    }

    private static class RecyclerDatabaseListAdapter extends RecyclerView.Adapter<RecyclerDatabaseListAdapter.ViewHolder> {

        List<DatabaseEntity> databases;
        OnItemClickListener listener;

        public RecyclerDatabaseListAdapter(OnItemClickListener listener) {
            this.listener = listener;
        }

        public void setDatabases(List<DatabaseEntity> databases) {
            if (this.databases == null) {
                this.databases = databases;
                notifyItemRangeInserted(0, databases.size());
                return;
            }

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RecyclerDatabaseListAdapter.this.databases.size();
                }

                @Override
                public int getNewListSize() {
                    return databases.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return RecyclerDatabaseListAdapter.this.databases.get(oldItemPosition).getId() == databases.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    DatabaseEntity newDatabase = databases.get(newItemPosition);
                    DatabaseEntity oldDatabase = RecyclerDatabaseListAdapter.this.databases.get(oldItemPosition);

                    return newDatabase.getId() == oldDatabase.getId() && TextUtils.equals(newDatabase.getName(), oldDatabase.getName());
                }
            });
            this.databases = databases;
            result.dispatchUpdatesTo(this);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_database, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            View root = holder.itemView;
            DatabaseEntity database = databases.get(position);

            root.setOnClickListener(v -> listener.onItemClick(position, database.getId()));

            ImageButton button = root.findViewById(R.id.settings_button);
            button.setOnClickListener(v -> listener.onItemSettingsClick(position, database.getId()));

            TextView name = root.findViewById(R.id.name);
            name.setText(database.getName());
        }

        @Override
        public int getItemCount() {
            return databases == null ? 0 : databases.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }
        }
    }
}