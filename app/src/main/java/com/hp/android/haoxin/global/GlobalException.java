package com.hp.android.haoxin.global;

/**
 * 全局异常处理类
 * Created by AZZ on 15/8/18 21:30.
 */
public class GlobalException {
    /**
     * 异常状态
     * 0 设备电机转速异常
     * 1 设备上盖打开(染色)
     * 2 设备上盖打开(清洗)
     * 3 设备上盖打开(流路填充)
     * 4 设备上盖打开(细胞离心)
     * 5 设备加热失效
     * 6 设备玻盘在位检测失败
     * 7 设备称重异常
     */
    private byte devException = 0;

    /**
     * 构造函数
     */
    public GlobalException(){};

    /**
     * 带参构造函数
     * @param argDevException 异常状态 见{@link #devException}
     */
    public GlobalException(byte argDevException) {
        devException = argDevException;
    }

    /**
     * 检测异常
     * @return true 通过；false 有异常
     */
    public boolean checkForException() {
        return (devException == 0) ? true : false;
    }

    /**
     * 按位取出byte中对应的值
     * @param b
     * @param position
     * @return
     */
    private byte getBitOfByteByPos(byte b, byte position) {
        return (byte)(1 & (b >> position));
    }
    
    /**
     * 获取电机转速异常
     * @return true异常，false无异常
     */
    public boolean isDevMotorExeception() {
    	return getBitOfByteByPos(devException, (byte) 0) == 0 ? false : true;
    }

    /**
     * 设备上盖打开(染色)
     * @return true异常，false无异常
     */
    public boolean isCoverOpenByDye() {
    	return getBitOfByteByPos(devException, (byte) 1) == 0 ? false : true;
    }

    /**
     * 设备上盖打开(清洗)
     * @return true异常，false无异常
     */
    public boolean isCoverOpenByClean() {
    	return getBitOfByteByPos(devException, (byte) 2) == 0 ? false : true;
    }

    /**
     * 设备上盖打开(流路填充)
     * @return true异常，false无异常
     */
    public boolean isCoverOpenByFill() {
    	return getBitOfByteByPos(devException, (byte) 3) == 0 ? false : true;
    }

    /**
     * 设备上盖打开(细胞离心)
     * @return true异常，false无异常
     */
    public boolean isCoverOpenByCytocentrifugation() {
    	return getBitOfByteByPos(devException, (byte) 4) == 0 ? false : true;
    }

    /**
     * 设备加热异常
     * @return true异常，false无异常
     */
    public boolean isHeatingException() {
    	return getBitOfByteByPos(devException, (byte) 5) == 0 ? false : true;
    }

    /**
     * 设备波盘在位检测异常
     * @return true异常，false无异常
     */
    public boolean isGlassCardException() {
    	return getBitOfByteByPos(devException, (byte) 6) == 0 ? false : true;
    }

    /**
     * 设备称重异常
     * @return true异常，false无异常
     */
    public boolean isWeighException() {
    	return getBitOfByteByPos(devException, (byte) 7) == 0 ? false : true;
    }
 
    public GlobalException setDevException(byte devException) {
        this.devException = devException;
        return this;
    }
}