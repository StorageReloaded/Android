package io.github.storagereloaded.android.db.converter;

import androidx.room.TypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

public class ObjectConverter {
    @TypeConverter
    public static Object toObject(String value) {
        try {
            JSONObject jsonObject = new JSONObject(value);
            return jsonObject.get("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public static String toString(Object value) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", value);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
