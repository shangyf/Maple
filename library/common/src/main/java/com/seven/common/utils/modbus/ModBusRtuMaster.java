package com.seven.common.utils.modbus;

import java.util.Formatter;

/**
 * Created by SeVen on 2/18/21 9:42 AM
 */
public class ModBusRtuMaster {
    /**
     * 组装Modbus RTU消息帧
     *
     * @param slave            从站地址号
     * @param function_code    功能码
     * @param starting_address 读取寄存器起始地址 / 写入寄存器地址 / 写入寄存器起始地址
     * @param quantity_of_x    读取寄存器个数 / 写入寄存器个数
     * @param output_value     需要写入单个寄存器的数值
     * @param output_values    需要写入多个寄存器的数组
     * @return 将整个消息帧转成byte[]
     * @throws ModBusError Modbus错误
     */
    synchronized private byte[] execute(int slave, int function_code, int starting_address, int quantity_of_x,
                                        int output_value, int[] output_values) throws  ModBusError {
        //检查参数是否符合协议规定
        if (slave < 0 || slave > 0xff) {
            throw new  ModBusError(ModBusErrorType.ModBusInvalidArgumentError, "Invalid slave " + slave);
        }
        if (starting_address < 0 || starting_address > 0xffff) {
            throw new  ModBusError(ModBusErrorType.ModBusInvalidArgumentError, "Invalid starting_address " + starting_address);
        }
        if (quantity_of_x < 1 || quantity_of_x > 0xff) {
            throw new  ModBusError(ModBusErrorType.ModBusInvalidArgumentError, "Invalid quantity_of_x " + quantity_of_x);
        }

        // 构造request
         ByteArrayWriter request = new  ByteArrayWriter();
        //写入从站地址号
        request.writeInt8(slave);
        //根据功能码组装数据区
        //如果为读取寄存器指令
        if (function_code ==  ModBusFunction.READ_COILS || function_code ==  ModBusFunction.READ_DISCRETE_INPUTS
                || function_code ==  ModBusFunction.READ_INPUT_REGISTERS || function_code ==  ModBusFunction.READ_HOLDING_REGISTERS) {
            request.writeInt8(function_code);
            request.writeInt16(starting_address);
            request.writeInt16(quantity_of_x);

        } else if (function_code ==  ModBusFunction.WRITE_SINGLE_COIL || function_code ==  ModBusFunction.WRITE_SINGLE_REGISTER) {//写单个寄存器指令
            if (function_code ==  ModBusFunction.WRITE_SINGLE_COIL)
                if (output_value != 0) output_value = 0xff00;//如果为线圈寄存器（写1时为 FF 00,写0时为00 00）
            request.writeInt8(function_code);
            request.writeInt16(starting_address);
            request.writeInt16(output_value);

        } else if (function_code ==  ModBusFunction.WRITE_COILS) {//写多个线圈寄存器
            request.writeInt8(function_code);
            request.writeInt16(starting_address);
            request.writeInt16(quantity_of_x);

            //计算写入字节数
            int writeByteCount = (quantity_of_x / 8) + 1;/// 满足关系-> (w /8) + 1
            //写入数量 == 8 ,则写入字节数为1
            if (quantity_of_x % 8 == 0) {
                writeByteCount -= 1;
            }
            request.writeInt8(writeByteCount);

            int index = 0;
            //如果写入数据数量 > 8 ，则需要拆分开来
            int start = 0;//数组开始位置
            int end = 7;//数组结束位置
            int[] splitData = new int[8];
            //循环写入拆分数组，直到剩下最后一组 元素个数 <= 8 的数据
            while (writeByteCount > 1) {
                writeByteCount--;
                int sIndex = 0;
                for (index = start; index <= end; index++) {
                    splitData[sIndex++] = output_values[index];
                }
                //数据反转 对于是否要反转要看你传过来的数据，如果高低位顺序正确则不用反转
                splitData = reverseArr(splitData);
                //写入拆分数组
                request.writeInt8(toDecimal(splitData));
                start = index;
                end += 8;
            }
            //写入最后剩下的数据
            int last = quantity_of_x - index;
            int[] tData = new int[last];
            System.arraycopy(output_values, index, tData, 0, last);
            //数据反转 对于是否要反转要看你传过来的数据，如果高低位顺序正确则不用反转
            tData = reverseArr(tData);
            request.writeInt8(toDecimal(tData));
        } else if (function_code ==  ModBusFunction.WRITE_HOLDING_REGISTERS) {//写多个保持寄存器
            request.writeInt8(function_code);
            request.writeInt16(starting_address);
            request.writeInt16(quantity_of_x);
            request.writeInt8(2 * quantity_of_x);
            //写入数据
            for (int v : output_values) {
                request.writeInt16(v);
            }
        } else {
            throw new  ModBusError(ModBusErrorType.ModBusFunctionNotSupportedError, "Not support function " + function_code);
        }
        byte[] bytes = request.toByteArray();
        //计算CRC校验码
        int crc =  CRC16.compute(bytes);
        request.writeInt16Reversal(crc);
        bytes = request.toByteArray();
        return bytes;
    }

    /**
     * 读多个线圈寄存器
     *
     * @param slave          从站地址
     * @param startAddress   起始地址
     * @param numberOfPoints 读取线圈寄存器个数
     * @throws  ModBusError Modbus错误
     */
    public byte[] readCoils(int slave, int startAddress, int numberOfPoints) throws  ModBusError {
        byte[] sendBytes = execute(slave,  ModBusFunction.READ_COILS, startAddress, numberOfPoints, 0, null);
        return sendBytes;
    }

    //读单个线圈寄存器
    public byte[] readCoil(int slave, int address) throws  ModBusError {
        byte[] sendBytes = readCoils(slave, address, 1);
        return sendBytes;
    }

    /**
     * 读多个保持寄存器
     *
     * @param slave          从站地址   蓝牙校验仪的从站地址是11
     * @param startAddress   起始地址
     * @param numberOfPoints 读取保持寄存器个数
     * @throws  ModBusError Modbus错误
     */
    public byte[] readHoldingRegisters(int slave, int startAddress, int numberOfPoints) throws  ModBusError {
        byte[] sendBytes = execute(slave,  ModBusFunction.READ_HOLDING_REGISTERS, startAddress, numberOfPoints, 0, null);
        return sendBytes;
    }

    /**
     * 读单个保持寄存器
     *
     * @param slave
     * @param address
     * @return
     * @throws  ModBusError
     */
    public byte[] readHoldingRegister(int slave, int address) throws  ModBusError {
        byte[] sendBytes = readHoldingRegisters(slave, address, 1);
        return sendBytes;
    }


    /**
     * 读多个输入寄存器
     *
     * @param slave          从站地址
     * @param startAddress   起始地址
     * @param numberOfPoints 读取输入寄存器个数
     * @throws  ModBusError Modbus错误
     */
    public byte[] readInputRegisters(int slave, int startAddress, int numberOfPoints) throws  ModBusError {
        byte[] sendBytes = execute(slave,  ModBusFunction.READ_INPUT_REGISTERS, startAddress, numberOfPoints, 0, null);
        return sendBytes;
    }

    //读单个输入寄存器
    public byte[] readInputRegister(int slave, int address) throws  ModBusError {
        byte[] sendBytes = readInputRegisters(slave, address, 1);
        return sendBytes;
    }

    /**
     * 读多个离散输入寄存器
     *
     * @param slave          从站地址
     * @param startAddress   起始地址
     * @param numberOfPoints 读取离散输入寄存器个数
     * @throws  ModBusError Modbus错误
     */
    public byte[] readDiscreteInputs(int slave, int startAddress, int numberOfPoints) throws  ModBusError {
        byte[] sendBytes = execute(slave,  ModBusFunction.READ_DISCRETE_INPUTS, startAddress, numberOfPoints, 0, null);
        return sendBytes;
    }

    //读单个离散输入寄存器
    public byte[] readDiscreteInput(int slave, int address) throws  ModBusError {
        byte[] sendBytes = readDiscreteInputs(slave, address, 1);
        return sendBytes;
    }

    /**
     * 写单个线圈寄存器
     *
     * @param slave   从站地址
     * @param address 写入寄存器地址
     * @param value   写入值（true/false)
     * @throws  ModBusError Modbus错误
     */
    public byte[] writeSingleCoil(int slave, int address, boolean value) throws  ModBusError {
        byte[] sendBytes = execute(slave,  ModBusFunction.WRITE_SINGLE_COIL, address, 1, value ? 1 : 0, null);
        return sendBytes;
    }

    /**
     * 写单个保持寄存器
     *
     * @param slave   从站地址
     * @param address 写入寄存器地址
     * @param value   写入值
     * @throws  ModBusError Modbus错误
     */
    public byte[] writeSingleRegister(int slave, int address, int value) throws  ModBusError {
        byte[] sendBytes = execute(slave,  ModBusFunction.WRITE_SINGLE_REGISTER, address, 1, value, null);
        return sendBytes;
    }

    /**
     * 写入多个保持寄存器
     *
     * @param slave   从站地址
     * @param address 写入寄存器地址
     * @param sCount  写入寄存器个数
     * @param data    写入数据
     * @throws  ModBusError
     */
    public byte[] writeHoldingRegisters(int slave, int address, int sCount, int[] data) throws  ModBusError {
        byte[] sendBytes = execute(slave,  ModBusFunction.WRITE_HOLDING_REGISTERS, address, sCount, 0, data);
        return sendBytes;
    }

    /**
     * 写入多个位
     *
     * @param slave   从站地址
     * @param address 写入寄存器地址
     * @param bCount  写入寄存器个数
     * @param data    写入数据{1,0}
     * @throws  ModBusError
     */
    public byte[] writeCoils(int slave, int address, int bCount, int[] data) throws  ModBusError {
        byte[] sendBytes = execute(slave,  ModBusFunction.WRITE_COILS, address, bCount, 0, data);
        return sendBytes;
    }


    //将数组反转
    private int[] reverseArr(int[] arr) {
        int[] tem = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            tem[i] = arr[arr.length - 1 - i];
        }
        return tem;
    }

    /**
     * 字节转换为 16 进制字符串
     *
     * @param b 字节
     * @return Hex 字符串
     */
    static String byte2Hex(byte b) {
        StringBuilder hex = new StringBuilder(Integer.toHexString(b));
        if (hex.length() > 2) {
            hex = new StringBuilder(hex.substring(hex.length() - 2));
        }
        while (hex.length() < 2) {
            hex.insert(0, "0");
        }
        return hex.toString();
    }


    /**
     * 字节数组转换为 16 进制字符串
     *
     * @param bytes 字节数组
     * @return Hex 字符串
     */
    static String bytes2Hex(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x ", b);
        }
        String hash = formatter.toString();
        formatter.close();
        return hash;
    }

    //将int[1,0,0,1,1,0]数组转换为十进制数据
    private int toDecimal(int[] data) {
        int result = 0;
        if (data != null) {
            StringBuilder sData = new StringBuilder();
            for (int d : data) {
                sData.append(d);
            }
            try {
                result = Integer.parseInt(sData.toString(), 2);
            } catch (NumberFormatException e) {
                result = -1;
            }

        }
        return result;
    }
}