package io.github.storagereloaded.android.ui;

import android.content.Intent;
import android.os.Bundle;
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
import io.github.storagereloaded.android.db.entity.TagEntity;
import io.github.storagereloaded.android.viewmodel.TagViewModel;

public class TagListActivity extends AppCompatActivity {

    public static final String EXTRA_TAG_ID = "io.github.storagereloaded.android.tag_id";
    public static final String EXTRA_CHOOSE_MODE = "io.github.storagereloaded.android.choose_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_list);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.tag_list);
        TagsRecyclerAdapter adapter = new TagsRecyclerAdapter((index, tagId) -> {
            if (getIntent().getBooleanExtra(EXTRA_CHOOSE_MODE, false)) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_TAG_ID, tagId);
                setResult(RESULT_OK, intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), TagEditActivity.class);
                intent.putExtra(EXTRA_TAG_ID, tagId);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton itemAddButton = findViewById(R.id.fab);
        itemAddButton.setOnClickListener(v -> {
            startActivity(new Intent(this, TagEditActivity.class));
        });

        setResult(RESULT_CANCELED);

        TagViewModel model = new ViewModelProvider(this).get(TagViewModel.class);
        model.getTags().observe(this, adapter::setTags);
    }

    private interface OnItemClickListener {
        void onItemClick(int index, int tagId);
    }

    private static class TagsRecyclerAdapter extends RecyclerView.Adapter<TagListActivity.TagsRecyclerAdapter.ViewHolder> {

        List<TagEntity> tags;
        OnItemClickListener listener;

        public TagsRecyclerAdapter(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public TagListActivity.TagsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tag, parent, false);
            return new TagListActivity.TagsRecyclerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TagListActivity.TagsRecyclerAdapter.ViewHolder holder, int position) {
            View root = holder.itemView;
            TagEntity tag = tags.get(position);

            root.setOnClickListener(v -> listener.onItemClick(position, tag.getId()));

            TextView name = root.findViewById(R.id.name);
            name.setText(tag.getName());
        }

        @Override
        public int getItemCount() {
            return tags == null ? 0 : tags.size();
        }

        public void setTags(List<TagEntity> tags) {
            if (this.tags == null) {
                this.tags = tags;
                notifyItemRangeInserted(0, tags.size());
                return;
            }

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return TagsRecyclerAdapter.this.tags.size();
                }

                @Override
                public int getNewListSize() {
                    return tags.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return TagListActivity.TagsRecyclerAdapter.this.tags.get(oldItemPosition).getId() == tags.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    TagEntity newItem = tags.get(newItemPosition);
                    TagEntity oldItem = TagListActivity.TagsRecyclerAdapter.this.tags.get(oldItemPosition);

                    return newItem.equals(oldItem);
                }
            });
            this.tags = tags;
            result.dispatchUpdatesTo(this);
        }

        private static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }
        }
    }
}