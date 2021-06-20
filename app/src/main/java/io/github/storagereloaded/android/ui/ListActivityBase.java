package io.github.storagereloaded.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import io.github.storagereloaded.android.R;

public abstract class ListActivityBase<T extends ListActivityBase.IdObject> extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_base);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        FloatingActionButton addButton = findViewById(R.id.fab);
        addButton.setOnClickListener(this::onAddButtonPressed);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    void setData(LiveData<List<T>> liveData, int listItemResId, ObjectViewMapper<T> mapper, ItemClickListener listener) {
        RecyclerViewListAdapter<T> adapter = new RecyclerViewListAdapter<>(listItemResId, mapper, listener);
        recyclerView.setAdapter(adapter);

        liveData.observe(this, adapter::setObjects);
    }

    abstract void onAddButtonPressed(View view);

    public interface ItemClickListener {
        void onItemClick(View view, int position, int id);

        void onItemOptionsClick(View view, int position, int id);
    }

    public interface ObjectViewMapper<T extends ListActivityBase.IdObject> {
        void mapToView(View view, T object, int position, ItemClickListener listener);
    }

    public interface IdObject {
        int getId();
    }

    private static class RecyclerViewListAdapter<T extends IdObject> extends RecyclerView.Adapter<RecyclerViewListAdapter.ViewHolder> {
        List<T> objects;

        int listItemResId;
        ObjectViewMapper<T> mapper;
        ItemClickListener listener;

        public RecyclerViewListAdapter(int listItemResId, ObjectViewMapper<T> mapper, ItemClickListener listener) {
            this.listItemResId = listItemResId;
            this.mapper = mapper;
            this.listener = listener;
        }

        public void setObjects(List<T> objects) {
            if (this.objects == null) {
                this.objects = objects;
                notifyItemRangeInserted(0, objects.size());
                return;
            }

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return RecyclerViewListAdapter.this.objects.size();
                }

                @Override
                public int getNewListSize() {
                    return objects.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return RecyclerViewListAdapter.this.objects.get(oldItemPosition).getId() == objects.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    T newObject = objects.get(newItemPosition);
                    T oldObject = RecyclerViewListAdapter.this.objects.get(oldItemPosition);

                    return Objects.equals(newObject, oldObject);
                }
            });
            this.objects = objects;
            result.dispatchUpdatesTo(this);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(listItemResId, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            mapper.mapToView(holder.itemView, objects.get(position), position, listener);
        }

        @Override
        public int getItemCount() {
            return objects == null ? 0 : objects.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }
        }
    }
}