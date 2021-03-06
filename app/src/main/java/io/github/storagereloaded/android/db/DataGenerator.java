package io.github.storagereloaded.android.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.storagereloaded.android.db.entity.CustomPropertyEntity;
import io.github.storagereloaded.android.db.entity.DatabaseEntity;
import io.github.storagereloaded.android.db.entity.InternalPropertyEntity;
import io.github.storagereloaded.android.db.entity.ItemEntity;
import io.github.storagereloaded.android.db.entity.LocationEntity;
import io.github.storagereloaded.android.db.entity.TagEntity;
import io.github.storagereloaded.android.db.entity.TagRelationEntity;

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

    public static List<ItemEntity> generateItems(List<DatabaseEntity> databases, List<LocationEntity> locations) {
        List<ItemEntity> items = new ArrayList<>();
        Random rnd = new Random();

        int itemId = 0;
        for (DatabaseEntity database : databases) {
            for (String name : ITEM_NAMES) {
                for (int i = 0; i < 10; i++) {
                    ItemEntity item = new ItemEntity();
                    item.setId(itemId++);
                    item.setName(name + " " + i);
                    item.setDescription("Test Description " + itemId);
                    item.setCreated(System.currentTimeMillis());
                    item.setLastEdited(System.currentTimeMillis());
                    item.setAmount(rnd.nextInt(100));
                    item.setDatabaseId(database.getId());
                    item.setLocationId(locations.get(rnd.nextInt(locations.size())).getId());
                    items.add(item);
                }
            }
        }

        return items;
    }

    public static List<InternalPropertyEntity> generateInternalProperties(List<ItemEntity> items) {
        List<InternalPropertyEntity> props = new ArrayList<>();

        int propId = 0;
        for (ItemEntity item : items) {
            InternalPropertyEntity p1 = new InternalPropertyEntity();
            p1.setId(propId++);
            p1.setType(InternalPropertyEntity.TYPE_PRICE);
            p1.setValue(15.99);
            p1.setItemId(item.getId());
            props.add(p1);

            InternalPropertyEntity p2 = new InternalPropertyEntity();
            p2.setId(propId++);
            p2.setType(InternalPropertyEntity.TYPE_EAN13);
            p2.setValue(1568741365894L);
            p2.setItemId(item.getId());
            props.add(p2);
        }

        return props;
    }

    public static List<CustomPropertyEntity> generateCustomProperties(List<ItemEntity> items) {
        List<CustomPropertyEntity> props = new ArrayList<>();

        int propId = 0;
        for (ItemEntity item : items) {
            CustomPropertyEntity p1 = new CustomPropertyEntity();
            p1.setId(propId++);
            p1.setItemId(item.getId());
            p1.setName("Test Prop 1");
            p1.setValue(512);
            props.add(p1);

            CustomPropertyEntity p2 = new CustomPropertyEntity();
            p2.setId(propId++);
            p2.setItemId(item.getId());
            p2.setName("Obi Wan");
            p2.setValue("Hello There");
            props.add(p2);
        }

        return props;
    }

    public static List<LocationEntity> generateLocations() {
        List<LocationEntity> locations = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            LocationEntity location = new LocationEntity();
            location.setName("Location " + i);
            location.setId(i);
            locations.add(location);
        }

        return locations;
    }

    public static List<TagEntity> generateTags() {
        List<TagEntity> tags = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            TagEntity tag = new TagEntity();
            tag.setId(i);
            tag.setName("Test Tag " + i);
            tags.add(tag);
        }
        return tags;
    }

    public static List<TagRelationEntity> generateTagRelations(List<TagEntity> tags, List<ItemEntity> items) {
        List<TagRelationEntity> tagRelations = new ArrayList<>();

        Random rnd = new Random();
        int id = 0;
        for (ItemEntity item : items) {
            for (int i = 0; i < 2; i++) {
                TagRelationEntity tagRelation = new TagRelationEntity();
                tagRelation.setId(id++);
                tagRelation.setItemId(item.getId());
                tagRelation.setTagId(tags.get(rnd.nextInt(tags.size())).getId());
                tagRelations.add(tagRelation);
            }
        }

        return tagRelations;
    }
}
