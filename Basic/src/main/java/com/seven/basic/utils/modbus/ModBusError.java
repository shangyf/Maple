package com.seven.basic.utils.modbus;

import android.text.TextUtils;

/**
 * Created by SeVen on 2/18/21 9:38 AM
 */
public class ModBusError extends Exception {
    private int code;

    public ModBusError(int code, String message) {
        super(!TextUtils.isEmpty(message) ? message : "Modbus Error: Exception code = " + code);
        this.code = code;
    }

    public ModBusError(int code) {
        this(code, null);
    }

    public ModBusError(ModBusErrorType type, String message) {
        super(type.name() + ": " + message);
    }

    public ModBusError(String message) {
        super(message);
    }

    public int getCode() {
        return this.code;
    }
}