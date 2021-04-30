package io.github.storagereloaded.android.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.model.InternalProperty;

@Entity(tableName = "internal_properties")
public class InternalPropertyEntity implements InternalProperty {

    public static final String TYPE_PRICE = "price";
    public static final String TYPE_EAN13 = "EAN13";

    private static final int VALUE_TYPE_INT = 0;
    private static final int VALUE_TYPE_STRING = 1;

    @PrimaryKey
    private int id;
    private int itemId;
    private String type;
    private Object value;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public int getNameId() {
        switch (type) {
            case TYPE_PRICE:
                return R.string.price;
            case TYPE_EAN13:
                return R.string.ean13;
            default:
                return -1;
        }
    }

    @Override
    public int getIconId() {
        switch (type) {
            case TYPE_PRICE:
                return R.drawable.ic_baseline_attach_money_24;
            case TYPE_EAN13:
                return R.drawable.ic_baseline_filter_center_focus_24;
            default:
                return -1;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }
}
