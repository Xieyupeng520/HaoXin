package com.hp.android.haoxin.global;

/**
 * Created by AZZ on 15/8/28 23:05.
 */
public class Instruction {
    /**
     * 染色
     */
    public static class Dye {
        public static final byte START_DYE      = 0x00; //开始染色
        public static final byte CANCEL_DYE     = 0x01; //取消染色
        public static final byte START_FILL     = 0x02; //进行填充
        public static final byte CANCEL_FILL    = 0x03; //取消填充
    }
}
