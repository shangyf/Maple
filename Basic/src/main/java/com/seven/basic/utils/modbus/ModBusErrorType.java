package com.seven.basic.utils.modbus;

/**
 * 常见的Modbus通讯错误
 * Created by SeVen on 2/18/21 9:37 AM
 */
public enum  ModBusErrorType {
    ModBusError,
    ModBusFunctionNotSupportedError,
    ModBusDuplicatedKeyError,
    ModBusMissingKeyError,
    ModBusInvalidBlockError,
    ModBusInvalidArgumentError,
    ModBusOverlapBlockError,
    ModBusOutOfBlockError,
    ModBusInvalidResponseError,
    ModBusInvalidRequestError,
    ModBusTimeoutError
}