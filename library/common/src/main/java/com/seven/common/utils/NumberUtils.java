package com.seven.common.utils;

/**
 * Created by SeVen on 7/9/21 2:52 PM
 * What if I am the devil for you
 */
public class NumberUtils {
    /**
     * V4.5.0起，保证数据不溢出，使用long型保存数据包大小结果
     */
    public static String formatDataSize(long size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }
}
