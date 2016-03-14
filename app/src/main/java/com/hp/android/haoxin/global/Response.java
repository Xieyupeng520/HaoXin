package com.hp.android.haoxin.global;

/**
 * 回应报文对应消息
 * Created by AZZ on 15/8/29.
 */
public class Response {
    /**
     * 当前操作反馈-完成状态
     */
    public static class Complete {
        public static final byte DYE_COMPLETE   = 0x00;             //染色完成(系统主界面“开始”按键触发)
        public static final byte FILL_COMPLETE_FROM_DYE  = 0x01;    //流路填充完成(系统主界面“开始”按键触发)
        public static final byte CLEAN_COMPLETE  = 0x02;            //流路清洗完成(菜单项“流路清洗”触发)
        public static final byte FILL_COMPLETE_FROM_CLEAN = 0x03;   //流路填充完成(菜单项“流路清洗”触发)

        public static final byte CHECK_A_COMPLETE = 0x04;           //流量检测 A 完成
        public static final byte CHECK_B_COMPLETE = 0x05;           //流量检测 B 完成
        public static final byte CHECK_C_COMPLETE = 0x06;           //流量检测 C 完成
        public static final byte CHECK_D_COMPLETE = 0x07;           //流量检测 D 完成
        public static final byte CHECK_E_COMPLETE = 0x08;           //流量检测 E 完成

        public static final byte B_ONE_STEP_COMPLETE = 0x09;        //B-流路检测“第一步”完成
        public static final byte B_TWO_STEP_COMPLETE = 0x0A;        //B-流路检测“二步”完成
        public static final byte B_THREE_STEP_COMPLETE = 0x0B;      //B-流路检测“第三步”完成

        public static final byte WEIGHT_ONE_STEP_COMPLETE = 0x0C;   //称重校验“第一步”操作完成
        public static final byte WEIGHT_TWO_STEP_COMPLETE = 0x0D;   //称重校验“第二步”操作完成
        public static final byte WEIGHT_THREE_STEP_COMPLETE = 0x0E; //称重校验“第三步”操作完成
        public static final byte WEIGHT_FOUR_STEP_COMPLETE = 0x0F;  //称重校验“第四步”操作完成

        public static final byte FILL_COMPLETE = 0x10;              //流路填充完成(系统维护界面下“流路填充”按键触发)

        public static final byte CENTRIFUGAL_COMPLETE = 0x11;       //细胞离心完成**
        public static final byte CLEAN_A_COMPLETE = 0x12;           //大力清洗泵 A 完成**
        public static final byte CLEAN_B_COMPLETE = 0x13;           //大力清洗泵 B 完成**
        public static final byte CLEAN_C_COMPLETE = 0x14;           //大力清洗泵 C 完成**
        public static final byte CLEAN_D_COMPLETE = 0x15;           //大力清洗泵 D 完成**
        public static final byte CLEAN_E_COMPLETE = 0x16;           //大力清洗泵 E 完成**
    }

    /**
     * 当前进度反馈-当前设备所处的操作标示
     */
    public static class Option {
        public static final byte OPTION_DYE            = 0x00;      //正在执行“染色”(系统主界面“开始”按键触发)
        public static final byte OPTION_FILL_FROM_DYE  = 0x01;      //正在执行“流路填充”(系统主界面“开始”按键触发)
        public static final byte OPTION_CLEAN  = 0x02;              //正在清洗
        public static final byte OPTION_FILL_FROM_CLEAN  = 0x03;    //正在执行“流路填充”(菜单项“流路清洗”触发)

        public static final byte OPTION_B_ONE_STEP = 0x04;          //正在执行“B-流路检测第一步”
        public static final byte OPTION_B_TWO_STEP = 0x05;          //正在执行“B-流路检测第二步”
        public static final byte OPTION_B_THREE_STEP = 0x06;        //正在执行“B-流路检测第三步”

        public static final byte OPTION_WEIGHT_ONE_STEP = 0x07;     //:正在执行“称重校验第一步”
        public static final byte OPTION_WEIGHT_TWO_STEP = 0x08;     //:正在执行“称重校验第二步”
        public static final byte OPTION_WEIGHT_THREE_STEP = 0x09;   //:正在执行“称重校验第三步”
        public static final byte OPTION_WEIGHT_FOUR_STEP = 0x0A;    //:正在执行“称重校验第四步”

        public static final byte OPTION_FILL  = 0x0B;               //正在执行“流路填充”(系统维护界面“流路填充”按 键触发)

        public static final byte OPTION_CENTRIFUGAL = 0x0C;         //细胞离心过程中**
    }

    /**
     * 进度状态指示,在进度条上方显示。注意:动画中的喷嘴喷射要同进度状态同步。
     */
    public static class Progress {
        public static final byte PROGRESS_NONE      = 0x00;         //无
        public static final byte PROGRESS_SAFFRON   = 0x01;         //正在喷射:番红(动画:A 喷射)
        public static final byte PROGRESS_ALCOHOL   = 0x02;         //酒精固定(动画:E 喷射)
        public static final byte PROGRESS_IODINE    = 0x03;         //正在喷射:碘酒(动画:B 喷射)
        public static final byte PROGRESS_CRYSTAL_VIOLET   = 0x04;  //正在喷射:结晶紫(动画:C 喷射)

        public static final byte PROGRESS_CLEAN = 0x05;             //正在清洗(动画:Df,Dr 同时喷)
        public static final byte PROGRESS_FILL = 0x06;              //正在填充(动画:Da,Db,Dc 同时喷)
        public static final byte PROGRESS_DRY = 0x07;               //正在甩干(动画:不喷射)
        public static final byte PROGRESS_WAIT = 0x08;              //等待(动画:不喷射)
        public static final byte PROGRESS_HEAT = 0x09;              //正在加热(动画:不喷射)
        public static final byte PROGRESS_STONE_CARBONATE = 0x0A;   //石碳酸复红(动画:A 喷射)
        public static final byte PROGRESS_ACID_ALCOHOL = 0x0B;      //正在喷射:酸性酒精(动画:B 喷射)
        public static final byte PROGRESS_METHYLENE_BLUE = 0x0C;    //正在喷射:亚甲基蓝(动画:C 喷射)
        public static final byte PROGRESS_RHODAMINE_B = 0x0D;       //正在喷射:罗丹明 B(动画:A 喷射)
        public static final byte PROGRESS_AURAMINE_O= 0x0E;         //正在喷射:金胺 O(动画:A 喷射)

        public static final byte PROGRESS_CENTRIFUGAL = 0x0F;       //正在细胞离心(动画:不喷射)**

        public static final byte PROGRESS_CLEAN_PLUS = 0x10;        //正在清洗(动画:A、B、C、D、E、Df 和 Dr 同时喷)
    }

    /**
     * 异常_报文
     */
    public static class Exception {
        public static final byte NO_ANY_EXCEPTION = 0x00;                                       //无任何异常

        public static final byte ELECTRIC_SPEED_EXCEPTION = 0x01;                               //电机转速异常(实际转速与参数设置的转速不符)
        public static final byte T0P_COVER_OPEND_WHILE_DYEING_EXCEPTION = 0x02;                 //设备执行染色过程中,上盖打开
        public static final byte T0P_COVER_OPEND_WHILE_CLEANING_EXCEPTION  = 0x04;              //设备执行在清洗过程中,上盖打开
        public static final byte T0P_COVER_OPEND_WHILE_FILLING_EXCEPTION = 0x08;                //设备填充过程中,上盖打开
        public static final byte T0P_COVER_OPEND_WHILE_CENTRIFUGAL_EXCEPTION = 0x10;            //设备细胞离心过程中,上盖打开
        public static final byte HEATING_FAILURE_EXCEPTION = 0x20;                              //设备加热失效(启动加热,但是温度无变化)
        public static final byte DETECT_GLASS_POSITION_FAILURE_EXCEPTION = 0x40;                //设备玻盘零点检测异常
        public static final byte WEIGHING_EXCEPTION = -0x80;                                    //设备称重异常
    }
}
