package io.github.storagereloaded.android.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.appbar.MaterialToolbar;

import io.github.storagereloaded.android.R;
import io.github.storagereloaded.android.viewmodel.ItemViewModel;

public class ItemEditActivity extends AppCompatActivity {

    Integer itemId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        ItemViewModel model = new ViewModelProvider(this).get(ItemViewModel.class);

        Intent intent = getIntent();
        if(intent.hasExtra(ItemViewActivity.EXTRA_ITEM_ID)){
            itemId = getIntent().getIntExtra(ItemViewActivity.EXTRA_ITEM_ID, 0);
            model.setItemId(itemId);
            model.getItem().observe(this, item -> {
                toolbar.setTitle(item.getName());
            });
        }

        Log.d("ItemEditActivity", String.valueOf(itemId));
    }
}