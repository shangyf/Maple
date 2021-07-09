package com.seven.net.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by SeVen on 2019-11-12 14:50
 */
public class IntegerDeserializer implements JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String string = json.getAsString();
        if (string.length() > 0) {
            return Integer.parseInt(string);
        } else {
            return 0;
        }
    }
}
