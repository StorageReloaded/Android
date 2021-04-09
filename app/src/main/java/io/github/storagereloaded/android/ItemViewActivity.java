package io.github.storagereloaded.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.DateFormat;

import io.github.storagereloaded.api.DisplayType;
import io.github.storagereloaded.api.Item;
import io.github.storagereloaded.api.Property;
import io.github.storagereloaded.api.Tag;
import io.github.storagereloaded.api.impl.ItemDummyImpl;
import io.github.storagereloaded.api.impl.PropertyDummyImpl;

public class ItemViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        Item item = getTestItem(); // Just for testing

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setTitle(item.getName());

        NestedScrollView scrollView = findViewById(R.id.scroll_view);
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY < 8)
                fab.extend();
            else
                fab.shrink();
        });

        ImageView image = findViewById(R.id.image);
        TextView name = findViewById(R.id.subtitle);
        TextView description = findViewById(R.id.description);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ChipGroup chips = findViewById(R.id.chips);
        TextView created = findViewById(R.id.created);
        TextView lastEdited = findViewById(R.id.last_edited);

        // TODO: Set Image

        name.setText(item.getName());
        description.setText(item.getDescription());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PropertiesRecyclerAdapter(item));

        if (item.getTags().isEmpty()) {
            HorizontalScrollView chipsScroll = findViewById(R.id.chips_scroll_layout);
            View divider = findViewById(R.id.chips_divider);
            chipsScroll.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        } else {
            for (Tag tag : item.getTags()) {
                Chip chip = new Chip(this);
                chip.setText(tag.getName());
                chips.addView(chip);
            }
        }

        DateFormat format = android.text.format.DateFormat.getLongDateFormat(this);

        created.setText(format.format(item.getCreated()));
        lastEdited.setText(format.format(item.getLastEdited()));
    }

    private Item getTestItem() {
        Item item = new ItemDummyImpl();

        Property prop = new PropertyDummyImpl();
        prop.setName("Obi Wan");
        prop.setDisplayType(DisplayType.UNKNOWN);
        prop.setValue("Hello There");
        item.getPropertiesCustom().add(prop);

        Property prop2 = new PropertyDummyImpl();
        prop2.setName("My Range");
        prop2.setDisplayType(DisplayType.RANGE);
        prop2.setValue(5);
        item.getPropertiesCustom().add(prop2);

        return item;
    }

    private static class PropertiesRecyclerAdapter extends RecyclerView.Adapter<ItemViewActivity.PropertiesRecyclerAdapter.ViewHolder> {

        PropertyItem[] items;

        public PropertiesRecyclerAdapter(Item item) {
            int len = 2 + item.getPropertiesInternal().size() + item.getPropertiesCustom().size() + item.getAttachments().size();
            items = new PropertyItem[len];

            items[0] = new PropertyItem(R.string.amount, item.getAmount(), R.drawable.ic_baseline_widgets_24);
            items[1] = new PropertyItem(R.string.location, "Some Location", R.drawable.ic_baseline_place_24);

            int offset = 2;
            for (int i = 0; i < item.getPropertiesInternal().size(); i++) {
                Property property = item.getPropertiesInternal().get(i);
                Integer iconID = getIconIDFromDisplayType(property.getDisplayType());
                items[i + offset] = new PropertyItem(property.getName(), property.getValue(), iconID);
            }

            offset += item.getPropertiesInternal().size();
            for (int i = 0; i < item.getPropertiesCustom().size(); i++) {
                Property property = item.getPropertiesCustom().get(i);
                Integer iconID = getIconIDFromDisplayType(property.getDisplayType());
                items[i + offset] = new PropertyItem(property.getName(), property.getValue(), iconID);
            }

            offset += item.getPropertiesCustom().size();
            int i = 0;
            for (String name : item.getAttachments().keySet()) {
                items[i + offset] = new PropertyItem(name, R.drawable.ic_baseline_file_present_24);
                i++;
            }
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

        @NonNull
        @Override
        public ItemViewActivity.PropertiesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attribute, parent, false);
            return new ItemViewActivity.PropertiesRecyclerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewActivity.PropertiesRecyclerAdapter.ViewHolder holder, int position) {
            View root = holder.itemView;
            TextView title = root.findViewById(R.id.title);
            TextView subtitle = root.findViewById(R.id.subtitle);
            ImageView icon = root.findViewById(R.id.icon);
            View divider = root.findViewById(R.id.divider);

            PropertyItem item = items[position];

            if (item.getName() instanceof Integer)
                title.setText((Integer) item.getName());
            else
                title.setText((String) item.getName());

            if (item.getValue() == null) {
                subtitle.setVisibility(View.GONE);
            } else {
                subtitle.setVisibility(View.VISIBLE);
                subtitle.setText(item.value.toString());
            }

            if (item.getIconID() == null)
                icon.setImageDrawable(null);
            else
                icon.setImageResource(item.getIconID());

            if (position == getItemCount() - 1)
                divider.setVisibility(View.GONE);
            else
                divider.setVisibility(View.VISIBLE);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        private static class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View view) {
                super(view);
            }
        }

        private static class PropertyItem {
            private final Object name;
            private final Object value;
            private final Integer iconID;

            public PropertyItem(String name, Integer iconID) {
                this(name, null, iconID);
            }

            public PropertyItem(Object name, Object value, Integer iconID) {
                this.name = name;
                this.value = value;
                this.iconID = iconID;
            }

            public Object getName() {
                return name;
            }

            public Object getValue() {
                return value;
            }

            public Integer getIconID() {
                return iconID;
            }
        }
    }
}