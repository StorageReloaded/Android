package io.github.storagereloaded.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerTestAdapter(new OnItemClickListener() {
            @Override
            public void onItemClick(int index, Object item) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DATABASE_ID, 1);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onItemSettingsClick(int index, Object item) {
                startActivity(new Intent(getApplicationContext(), DatabaseSettingsActivity.class));
            }
        }));

        setResult(RESULT_CANCELED);
    }

    private interface OnItemClickListener {
        void onItemClick(int index, Object item);

        void onItemSettingsClick(int index, Object item);
    }

    private static class RecyclerTestAdapter extends RecyclerView.Adapter<RecyclerTestAdapter.ViewHolder> {

        String[] testItems = {"Test123", "My super cool Database", "Don't ever look into this"};
        OnItemClickListener listener;

        public RecyclerTestAdapter(OnItemClickListener listener) {
            this.listener = listener;
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
            root.setOnClickListener(v -> listener.onItemClick(position, testItems[position]));

            ImageButton button = root.findViewById(R.id.settings_button);
            button.setOnClickListener(v -> listener.onItemSettingsClick(position, testItems[position]));

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