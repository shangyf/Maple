package com.seven.net.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by SeVen on 2019-11-12 14:48
 */
public class DoubleDeserializer implements JsonDeserializer<Double> {
    @Override
    public Double deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
        String string = json.getAsString();
        if (string.length() > 0) {
            return Double.parseDouble(string);
        } else {
            return 0.0;
        }
    }
}
