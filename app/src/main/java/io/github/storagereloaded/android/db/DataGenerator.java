package io.github.storagereloaded.android.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.db.entity.ItemEntity;

public class DataGenerator {

    private static final String[] FIRST = new String[]{"Test", "My", "Electronics"};
    private static final String[] SECOND = new String[]{"Database", "List", "Stuff"};

    private static final String[] ITEM_NAMES = new String[]{"Test Item", "Tea", "Capacitor", "App"};

    public static List<DatabaseEntity> generateDatabases() {
        List<DatabaseEntity> databases = new ArrayList<>(FIRST.length * SECOND.length);
        for (int i = 0; i < FIRST.length; i++) {
            for (int j = 0; j < SECOND.length; j++) {
                DatabaseEntity database = new DatabaseEntity();
                database.setName(FIRST[i] + " " + SECOND[j]);
                database.setId(FIRST.length * i + j + 1);
                databases.add(database);
            }
        }
        return databases;
    }

    public static List<ItemEntity> generateItems(List<DatabaseEntity> databases) {
        List<ItemEntity> items = new ArrayList<>();
        Random rnd = new Random();

        int itemId = 0;
        for(DatabaseEntity database : databases) {
            for(String name : ITEM_NAMES){
                for (int i = 0; i < 10; i++){
                    ItemEntity item = new ItemEntity();
                    item.setId(itemId++);
                    item.setName(name + " " + i);
                    item.setDescription("Test Description");
                    item.setCreated(System.currentTimeMillis());
                    item.setLastEdited(System.currentTimeMillis());
                    item.setAmount(rnd.nextInt(100));
                    item.setDatabaseId(database.getId());
                    items.add(item);
                }
            }
        }

        return items;
    }
}
