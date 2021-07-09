package com.seven.net.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Created by SeVen on 2019-11-12 14:52
 */
public class NullStringToEmptyAdapterFactory implements TypeAdapterFactory {
    public TypeAdapter create(Gson gson, TypeToken type) {
        Class rawType = type.getRawType();
        if (rawType != String.class) {
            return null;
        }
        return new StringNullAdapter();
    }
}
