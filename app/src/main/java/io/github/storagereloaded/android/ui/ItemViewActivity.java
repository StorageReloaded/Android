package io.github.storagereloaded.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.db.entity.CustomPropertyEntity;
import io.github.storagereloaded.android.db.entity.InternalPropertyEntity;
import io.github.storagereloaded.android.db.entity.LocationEntity;
import io.github.storagereloaded.android.db.entity.TagEntity;
import io.github.storagereloaded.android.model.CustomProperty;
import io.github.storagereloaded.android.model.InternalProperty;
import io.github.storagereloaded.android.viewmodel.ItemViewModel;
import io.github.storagereloaded.api.DisplayType;

public class ItemViewActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "io.github.storagereloaded.android.item_id";

    MaterialToolbar toolbar;
    PropertiesRecyclerAdapter adapter;

    ItemViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        int itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, 0);

        NestedScrollView scrollView = findViewById(R.id.scroll_view);
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY < 8)
                fab.extend();
            else
                fab.shrink();
        });

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, ItemEditActivity.class);
            intent.putExtra(EXTRA_ITEM_ID, itemId);
            startActivity(intent);
        });

        ImageView image = findViewById(R.id.image);
        TextView name = findViewById(R.id.subtitle);
        TextView description = findViewById(R.id.description);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ChipGroup chips = findViewById(R.id.chips);
        TextView created = findViewById(R.id.created);
        TextView lastEdited = findViewById(R.id.last_edited);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PropertiesRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        model = new ViewModelProvider(this).get(ItemViewModel.class);
        model.setItemId(itemId);
        model.getItem().observe(this, item -> {
            if(item == null)
                return;

            name.setText(item.getName());
            description.setText(item.getDescription());
            toolbar.setTitle(item.getName());
            adapter.setAmount(item.getAmount());

            DateFormat format = android.text.format.DateFormat.getLongDateFormat(this);
            created.setText(format.format(item.getCreated()));
            lastEdited.setText(format.format(item.getLastEdited()));

            model.getLocation(item.getLocationId()).observe(ItemViewActivity.this, adapter::setLocation);
        });

        model.getTags().observe(this, tags -> {
            if (tags == null || tags.isEmpty()) {
                HorizontalScrollView chipsScroll = findViewById(R.id.chips_scroll_layout);
                View divider = findViewById(R.id.chips_divider);
                chipsScroll.setVisibility(View.GONE);
                divider.setVisibility(View.GONE);
            } else {
                chips.removeAllViews();
                for (TagEntity tag : tags) {
                    Chip chip = new Chip(this);
                    chip.setText(tag.getName());
                    chips.addView(chip);
                }
            }
        });

        model.getInternalProperties().observe(this, internalProperties -> adapter.setInternalProperties(internalProperties));
        model.getCustomProperties().observe(this, customProperties -> adapter.setCustomProperties(customProperties));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_item) {
            model.deleteItem();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class FakeProperty {
        private final int nameId;
        private final int iconId;
        private final Object value;

        public FakeProperty(int nameId, int iconId, Object value) {
            this.nameId = nameId;
            this.iconId = iconId;
            this.value = value;
        }

        public int getNameId() {
            return nameId;
        }

        public int getIconId() {
            return iconId;
        }

        public Object getValue() {
            return value;
        }
    }

    private static class PropertiesRecyclerAdapter extends RecyclerView.Adapter<ItemViewActivity.PropertiesRecyclerAdapter.ViewHolder> {

        List<FakeProperty> fakeProperties = new ArrayList<>();
        List<InternalPropertyEntity> internalProperties;
        List<CustomPropertyEntity> customProperties;
        //TODO: Attachments

        @NonNull
        @Override
        public ItemViewActivity.PropertiesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attribute, parent, false);
            return new ItemViewActivity.PropertiesRecyclerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewActivity.PropertiesRecyclerAdapter.ViewHolder holder, int position) {
            View root = holder.itemView;
            ImageView icon = root.findViewById(R.id.icon);
            TextView title = root.findViewById(R.id.title);
            TextView subtitle = root.findViewById(R.id.subtitle);
            View divider = root.findViewById(R.id.divider);

            // Hide the divider on the last item
            divider.setVisibility((position + 1 == getItemCount()) ? View.GONE : View.VISIBLE);

            // Amount and Location
            if (position < getFakePropertiesSize()) {
                FakeProperty prop = fakeProperties.get(position);
                icon.setImageResource(prop.getIconId());
                title.setText(prop.getNameId());
                subtitle.setText(String.valueOf(prop.getValue()));
                return;
            }

            // Internal Properties
            if (position < getFakePropertiesSize() + getInternalPropertiesSize()) {
                InternalProperty prop = internalProperties.get(position - getFakePropertiesSize());
                icon.setImageResource(prop.getIconId());
                title.setText(prop.getNameId());
                subtitle.setText(String.valueOf(prop.getValue()));
                return;
            }

            // Custom Properties
            if (position < getFakePropertiesSize() + getInternalPropertiesSize() + getCustomPropertiesSize()) {
                CustomProperty prop = customProperties.get(position - getFakePropertiesSize() - getInternalPropertiesSize());
                icon.setImageDrawable(null);
                title.setText(prop.getName());
                subtitle.setText(String.valueOf(prop.getValue()));
            }
        }

        public void setAmount(int amount) {
            FakeProperty prop = new FakeProperty(R.string.amount, R.drawable.ic_baseline_widgets_24, amount);

            if (fakeProperties.size() < 1) {
                fakeProperties.add(prop);
                notifyItemInserted(0);
            } else {
                fakeProperties.set(0, prop);
                notifyItemChanged(0);
            }
        }

        public void setLocation(LocationEntity location) {
            FakeProperty prop = new FakeProperty(R.string.location, R.drawable.ic_baseline_place_24, location);

            if (fakeProperties.size() < 2) {
                fakeProperties.add(prop);
                notifyItemInserted(1);
            } else {
                fakeProperties.set(1, prop);
                notifyItemChanged(1);
            }
        }

        public void setInternalProperties(List<InternalPropertyEntity> internalProperties) {
            this.internalProperties = internalProperties;
            notifyItemRangeChanged(getFakePropertiesSize() - 1, getFakePropertiesSize() + internalProperties.size() - 1);
        }

        public void setCustomProperties(List<CustomPropertyEntity> customProperties) {
            this.customProperties = customProperties;
            notifyItemRangeChanged(getFakePropertiesSize() + getInternalPropertiesSize() - 1, getFakePropertiesSize() + getInternalPropertiesSize() + customProperties.size() - 1);
        }

        private int getFakePropertiesSize() {
            return fakeProperties == null ? 0 : fakeProperties.size();
        }

        private int getInternalPropertiesSize() {
            return internalProperties == null ? 0 : internalProperties.size();
        }

        private int getCustomPropertiesSize() {
            return customProperties == null ? 0 : customProperties.size();
        }

        private Integer getIconIDFromDisplayType(DisplayType displayType) {
            switch (displayType) {
                case RANGE:
                    return R.drawable.ic_baseline_width_24;
                case EAN13:
                    return R.drawable.ic_baseline_filter_center_focus_24;
                case UNKNOWN:
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return getFakePropertiesSize() + getInternalPropertiesSize() + getCustomPropertiesSize();
        }

        private static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }
        }
    }
}