package io.github.storagereloaded.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.db.entity.TagEntity;
import io.github.storagereloaded.android.viewmodel.TagViewModel;

public class TagListActivity extends ListActivityBase<TagEntity> {

    public static final String EXTRA_TAG_ID = "io.github.storagereloaded.android.tag_id";
    public static final String EXTRA_CHOOSE_MODE = "io.github.storagereloaded.android.choose_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_tag_list);

        TagViewModel model = new ViewModelProvider(this).get(TagViewModel.class);
        setData(model.getTags(), R.layout.list_item_tag, mapper, itemClickListener);

        setResult(RESULT_CANCELED);
    }

    @Override
    void onAddButtonPressed(View view) {
        startActivity(new Intent(this, TagEditActivity.class));
    }

    ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void onItemClick(View view, int position, int id) {
            if (getIntent().getBooleanExtra(EXTRA_CHOOSE_MODE, false)) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_TAG_ID, id);
                setResult(RESULT_OK, intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), TagEditActivity.class);
                intent.putExtra(EXTRA_TAG_ID, id);
                startActivity(intent);
            }
        }

        @Override
        public void onItemOptionsClick(View view, int position, int id) {
        }
    };

    ObjectViewMapper<TagEntity> mapper = (view, tag, position, listener) -> {
        view.setOnClickListener(v -> listener.onItemClick(view, position, tag.getId()));
        TextView name = view.findViewById(R.id.name);
        name.setText(tag.getName());
    };
}