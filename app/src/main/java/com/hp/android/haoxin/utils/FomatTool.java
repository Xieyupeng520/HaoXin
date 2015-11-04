package com.hp.android.haoxin.utils;

/**
 * 格式化工具
 * Created by AZZ on 15/8/3.
 */
public class FomatTool {
    /**
     * int -> byte[]
     * @param param int类型
     * @return 字节数组
     */
    public static byte[] int2bytes(int param) {
        String s = String.valueOf(param);
        return s.getBytes();
    }

    /**
     * int -> byte
     * @param param int类型
     * @return 字节
     */
    public static byte int2byte(int param) {
        return (byte)param;
    }
}
