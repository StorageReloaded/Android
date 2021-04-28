package io.github.storagereloaded.android.db;

import java.util.ArrayList;
import java.util.List;

import io.github.storagereloaded.android.db.entity.DatabaseEntity;

public class DataGenerator {

    private static final String[] FIRST = new String[]{"Test", "My ", "Electronics"};
    private static final String[] SECOND = new String[]{"Database", "List", "Stuff"};

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
}
