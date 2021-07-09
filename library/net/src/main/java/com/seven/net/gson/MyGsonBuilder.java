package com.seven.net.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.TreeMap;

/**
 * Created by SeVen on 2019-11-12 14:42
 */
public class MyGsonBuilder {
    public static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.addSerializationExclusionStrategy(new SerializationExclusionStrategy());
        gsonBuilder.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory());
        gsonBuilder.registerTypeHierarchyAdapter(Integer.class,
                new IntegerDeserializer());
        gsonBuilder.registerTypeAdapter(new TypeToken<TreeMap<String, Object>>() {
                }.getType()
                , new MapDeserializer());
        gsonBuilder.registerTypeHierarchyAdapter(Double.class,
                new DoubleDeserializer());
        gsonBuilder.registerTypeHierarchyAdapter(Float.class,
                new FloatDeserializer());
        return gsonBuilder.create();
    }
}
