package com.hp.android.haoxin.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import com.hp.android.haoxin.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    /**
     * 当前时间转换成 "2016/04/01 11:11 上午   >"
     * @return
     */
    public static String formatDateTime(Date date) {
        Calendar calender = Calendar.getInstance();
        if (date == null) {
            calender.setTimeInMillis(System.currentTimeMillis());
        } else {
            calender.setTime(date);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String time = df.format(calender.getTime());

        int hour = calender.get(Calendar.HOUR_OF_DAY);
        String amOrPm = (hour > 12) ? "下午" : "上午";

        return String.format("%s %s   >", time, amOrPm);

    }
}
