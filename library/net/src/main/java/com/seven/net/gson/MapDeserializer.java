package com.seven.net.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by SeVen on 2020/5/26 16:31
 */
public class MapDeserializer implements JsonDeserializer<TreeMap<String, Object>> {
    @Override
    public TreeMap<String, Object> deserialize(JsonElement json, Type typeOfT,
                                               JsonDeserializationContext context) throws JsonParseException {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        JsonObject jsonObject = json.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            Object ot = entry.getValue();
            if (ot instanceof JsonPrimitive) {
                treeMap.put(entry.getKey(), ((JsonPrimitive) ot).getAsString());
            } else {
                treeMap.put(entry.getKey(), ot);
            }
        }
        return treeMap;
    }
}
