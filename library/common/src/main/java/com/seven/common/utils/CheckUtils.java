package com.seven.common.utils;

import android.text.TextUtils;

import java.util.List;

public class CheckUtils {
    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public static String checkNotEmpty(String object, String message) {
        if (TextUtils.isEmpty(object)) {
            throw new RuntimeException(message);
        }
        return object;
    }

    public static List checkNotEmpty(List object, String message) {
        if (object == null) {
            throw new RuntimeException(message);
        }

        if (object.size() <= 0) {
            throw new RuntimeException(message);
        }
        return object;
    }

    public static <T> void checkNotEqual(T object, T value, String message) {
        if (object == value) {
            throw new RuntimeException(message);
        }
    }
}