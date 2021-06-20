package io.github.storagereloaded.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.db.entity.LocationEntity;
import io.github.storagereloaded.android.viewmodel.LocationViewModel;

public class LocationListActivity extends AppCompatActivity {

    public static final String EXTRA_LOCATION_ID = "io.github.storagereloaded.android.location_id";
    public static final String EXTRA_DATABASE_ID = "io.github.storagereloaded.android.database_id";
    public static final String EXTRA_CHOOSE_MODE = "io.github.storagereloaded.android.choose_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.location_list);
        LocationsRecyclerAdapter adapter = new LocationsRecyclerAdapter((index, locationId) -> {
            if (getIntent().getBooleanExtra(EXTRA_CHOOSE_MODE, false)) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_LOCATION_ID, locationId);
                setResult(RESULT_OK, intent);
            } else {
                //Intent intent = new Intent(getApplicationContext(), LocationEditActivity.class);
                //intent.putExtra(EXTRA_LOCATION_ID, locationId);
                //if(getIntent().hasExtra(EXTRA_DATABASE_ID))
                //    intent.putExtra(EXTRA_DATABASE_ID, getIntent().getIntExtra(EXTRA_DATABASE_ID, 0));
                //startActivity(intent);
            }
        }, true /*!getIntent().hasExtra(EXTRA_DATABASE_ID)*/);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton itemAddButton = findViewById(R.id.fab);
        itemAddButton.setOnClickListener(v -> {
            //Intent intent = new Intent(getApplicationContext(), LocationEditActivity.class);
            //if(getIntent().hasExtra(EXTRA_DATABASE_ID))
            //    intent.putExtra(EXTRA_DATABASE_ID, getIntent().getIntExtra(EXTRA_DATABASE_ID, 0));
            //startActivity(intent);
        });

        setResult(RESULT_CANCELED);

        LocationViewModel model = new ViewModelProvider(this).get(LocationViewModel.class);
        model.getLocations().observe(this, adapter::setLocations);
        model.getDatabases().observe(this, adapter::setDatabases);
    }

    private interface OnItemClickListener {
        void onItemClick(int index, int locationId);
    }

    private static class LocationsRecyclerAdapter extends RecyclerView.Adapter<LocationsRecyclerAdapter.ViewHolder> {

        List<LocationEntity> locations;
        List<DatabaseEntity> databases;
        OnItemClickListener listener;
        boolean displayDatabaseName;

        public LocationsRecyclerAdapter(OnItemClickListener listener, boolean displayDatabaseName) {
            this.listener = listener;
            this.displayDatabaseName = displayDatabaseName;
        }

        @NonNull
        @Override
        public LocationsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_location, parent, false);
            return new LocationsRecyclerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationsRecyclerAdapter.ViewHolder holder, int position) {
            View root = holder.itemView;
            LocationEntity location = locations.get(position);

            root.setOnClickListener(v -> listener.onItemClick(position, location.getId()));

            TextView name = root.findViewById(R.id.name);
            name.setText(location.getName());

            TextView database = root.findViewById(R.id.database);

            if (displayDatabaseName) {
                String dbName = getDatabaseNameForLocation(location);
                database.setText(dbName);
            } else {
                database.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return locations == null ? 0 : locations.size();
        }

        public void setLocations(List<LocationEntity> locations) {
            if (this.locations == null) {
                this.locations = locations;
                notifyItemRangeInserted(0, locations.size());
                return;
            }

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return LocationsRecyclerAdapter.this.locations.size();
                }

                @Override
                public int getNewListSize() {
                    return locations.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return LocationsRecyclerAdapter.this.locations.get(oldItemPosition).getId() == locations.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    LocationEntity newItem = locations.get(newItemPosition);
                    LocationEntity oldItem = LocationsRecyclerAdapter.this.locations.get(oldItemPosition);

                    return newItem.equals(oldItem);
                }
            });
            this.locations = locations;
            result.dispatchUpdatesTo(this);
        }

        public void setDatabases(List<DatabaseEntity> databases) {
            this.databases = databases;
            notifyDataSetChanged();
        }

        private String getDatabaseNameForLocation(LocationEntity location) {
            for (DatabaseEntity database : databases) {
                if (database.getId() == location.getDatabaseId())
                    return database.getName();
            }

            return "";
        }

        private static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }
        }
    }
}