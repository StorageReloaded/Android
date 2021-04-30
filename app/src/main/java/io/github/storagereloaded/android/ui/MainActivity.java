package io.github.storagereloaded.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.db.entity.ItemEntity;
import io.github.storagereloaded.android.viewmodel.DatabaseViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    LinearLayout noDatabaseLayout;
    RecyclerView recyclerView;
    RecyclerTestAdapter adapter;
    int databaseId = -1;

    DatabaseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.item_list);
        adapter = new MainActivity.RecyclerTestAdapter((index, itemId) -> {
            Intent intent = new Intent(this, ItemViewActivity.class);
            intent.putExtra(ItemViewActivity.EXTRA_ITEM_ID, itemId);
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        noDatabaseLayout = findViewById(R.id.no_database_layout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == DatabaseListActivity.REQUEST_CODE) {
            databaseId = data.getIntExtra(DatabaseListActivity.EXTRA_DATABASE_ID, -1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        // If onActivityResult delivered a databaseId
        if (this.databaseId != -1) {
            viewModel.setDatabaseId(this.databaseId);
            viewModel.getDatabase().observe(this, databaseEntity -> {
                if (databaseEntity != null) {
                    toolbar.setTitle(databaseEntity.getName());
                }
            });

            viewModel.getItems().observe(this, items -> {
                if (items != null)
                    adapter.setItems(items);
            });

            return;
        }

        // Get the first available database if none was selected
        viewModel.getDatabases().observe(this, databases -> {
            if (databases != null) {

                if(databases.isEmpty()){
                    // Show the "no database" text and button
                    recyclerView.setVisibility(View.GONE);
                    noDatabaseLayout.setVisibility(View.VISIBLE);
                    return;
                }

                recyclerView.setVisibility(View.VISIBLE);
                noDatabaseLayout.setVisibility(View.GONE);

                int databaseId = databases.get(0).getId();
                viewModel.setDatabaseId(databaseId);
                viewModel.getDatabase().observe(this, databaseEntity -> {
                    if (databaseEntity != null) {
                        toolbar.setTitle(databaseEntity.getName());
                    }
                });

                viewModel.getItems().observe(this, items -> {
                    if (items != null)
                        adapter.setItems(items);
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setSearchQuery(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_database_select:
                startActivityForResult(new Intent(this, DatabaseListActivity.class), DatabaseListActivity.REQUEST_CODE);
                break;
            case R.id.nav_app_settings:
                startActivity(new Intent(this, AppSettingsActivity.class));
                break;
            case R.id.nav_logout:
                // For testing
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }

        drawer.closeDrawers();
        return true;
    }

    private interface OnItemClickListener {
        void onItemClick(int index, int itemId);
    }

    private static class RecyclerTestAdapter extends RecyclerView.Adapter<MainActivity.RecyclerTestAdapter.ViewHolder> {
        List<ItemEntity> items;
        MainActivity.OnItemClickListener listener;

        public RecyclerTestAdapter(MainActivity.OnItemClickListener listener) {
            this.listener = listener;
        }

        public void setItems(List<ItemEntity> items) {
            if (this.items == null) {
                this.items = items;
                notifyItemRangeInserted(0, items.size());
                return;
            }

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RecyclerTestAdapter.this.items.size();
                }

                @Override
                public int getNewListSize() {
                    return items.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return RecyclerTestAdapter.this.items.get(oldItemPosition).getId() == items.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    ItemEntity newItem = items.get(newItemPosition);
                    ItemEntity oldItem = MainActivity.RecyclerTestAdapter.this.items.get(oldItemPosition);

                    return newItem.equals(oldItem);
                }
            });
            this.items = items;
            result.dispatchUpdatesTo(this);
        }

        @NonNull
        @Override
        public MainActivity.RecyclerTestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_items, parent, false);
            return new MainActivity.RecyclerTestAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.RecyclerTestAdapter.ViewHolder holder, int position) {
            View root = holder.itemView;
            ItemEntity item = items.get(position);

            root.setOnClickListener(v -> listener.onItemClick(position, item.getId()));

            TextView name = root.findViewById(R.id.name);
            name.setText(item.getName());

            TextView description = root.findViewById(R.id.description);
            description.setText(item.getDescription());
        }

        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }
        }
    }
}