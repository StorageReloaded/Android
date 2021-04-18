package io.github.storagereloaded.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.storagereloaded.android.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView recyclerView = findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MainActivity.RecyclerTestAdapter((index, item) -> {
            startActivity(new Intent(this, ItemViewActivity.class));
        }));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
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
        void onItemClick(int index, Object item);
    }

    private static class RecyclerTestAdapter extends RecyclerView.Adapter<MainActivity.RecyclerTestAdapter.ViewHolder> {
        String[] testItems = {"Title", "Item1165", "Item1178", "Item1165", "Item1178", "Item1165", "Item1178", "Title", "Item1165", "Item1178", "Item1165", "Item1178", "Item1165", "Item1178", "Title", "Item1165", "Item1178", "Item1165", "Item1178", "Item1165", "Item1178"};
        MainActivity.OnItemClickListener listener;

        public RecyclerTestAdapter(MainActivity.OnItemClickListener listener) {
            this.listener = listener;
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
            root.setOnClickListener(v -> listener.onItemClick(position, testItems[position]));

            TextView name = root.findViewById(R.id.name);
            name.setText(testItems[position]);
        }

        @Override
        public int getItemCount() {
            return testItems.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }
        }
    }
}