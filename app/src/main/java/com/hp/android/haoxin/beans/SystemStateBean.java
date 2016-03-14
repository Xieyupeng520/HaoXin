package com.hp.android.haoxin.beans;

/**
 * 系统状态信息
 * Created by AZZ on 15/8/9.
 */
public class SystemStateBean {
    /**
     * 连接状态，1为连接，0为未连接，默认-1未连接
     */
    private int mCommActived = -1;
    /**
     * 设备门状态（0x00：关闭 0x01：开启）
     */
    private int mStatDevLid;
    /**
     * 设备A试剂状态（0x00：不足 0x01：充足）
     */
    private int mStatAReagent;
    /**
     * 设备B试剂状态（0x00：不足 0x01：充足）
     */
    private int mStatBReagent;
    /**
     * 设备C试剂状态（0x00：不足 0x01：充足）
     */
    private int mStatCReagent;
    /**
     * 设备D试剂状态（0x00：不足 0x01：充足）
     */
    private int mStatDReagent;
    /**
     * 设备E试剂状态（0x00：不足 0x01：充足）
     */
    private int mStatEReagent;
    /**
     * 设备试剂状态（0x00：不足 0x01：充足)
     * 0A 1B 2C 3D 4E
     */
    private int[] mStatReagent = new int[5];
    /**
     * 设备流路填充状态（0x00：未填充 0x01：已填充）
     */
    private int mStatDevFilled;
    /**
     * 设备电机运行状态（0x00：停止 0x01：运行）（扩展预留
     */
    private int mStatDevMotor;
    /**
     * 设备加热枪温度（0~100）（扩展预留
     */
    private int mTDevValue;
    /**
     * 设备电机转速 （扩展预留
     */
    private int mSpeedDevMotor;
    /**
     * 设备门锁状态 （扩展预留
     */
    private int mStatLockLid;

    public static final int STAT_YES = 1;
    public static final int STAT_NO = 0;

    /**
     * 试剂名称
     */
    private final String[] mStatReagentName = {"A","B","C","D","E"};




    /**
     * --- getter and setter ---
     */
    /**
     * 设备是否处于连接中
     * @return true 连接状态； false 非连接状态
     */
    public boolean isCommActived() {
        return mCommActived == 1 ? true : false;
    }

    /**
     * 设置链路连接状态(1为连接，0为未连接）
     * @param flag 正数为连接，其他为非连接
     */
    public void setCommActived(int flag) {
        mCommActived = flag;
    }
    /**
     * 得到设备链路状态
     * @return 1为连接，0为未连接，默认-1未连接
     */
    public int getCommActived() {
        return mCommActived;
    }

    public int getStatDevLid() {
        return mStatDevLid;
    }

    public SystemStateBean setStatDevLid(int statDevLid) {
        this.mStatDevLid = statDevLid;
        return this;
    }

    /**
     * @return true门是关闭的；false门是打开的
     */
    public boolean isDoorClosed() {
        return mStatDevLid == 0 ? true : false;
    }

    /**
     * @return 试剂是否充足
     */
    public boolean isReagentEnough() {
        int i = getStatAReagent() + getStatBReagent()
                + getStatCReagent() + getStatDReagent()
                + getStatEReagent();
        if (i != 5) {
            return false;
        }
        return true;
    }

    /**
     * 试剂i是否充足。i范围：0-4 == A-E
     */
    public boolean isReagentEnough(int i) {
        return (mStatReagent[i] == 1);
    }

    /**
     * 获取单个试剂名称
     * @param index 试剂所在下标
     */
    public String getReagentString(int index) {
        return mStatReagentName[index];
    }
    public int getStatAReagent() {
        return mStatAReagent;
    }

    public SystemStateBean setStatAReagent(int statAReagent) {
        this.mStatAReagent = statAReagent;
        this.mStatReagent[0] = statAReagent;
        return this;
    }

    public int getStatBReagent() {
        return mStatBReagent;
    }

    public SystemStateBean setStatBReagent(int statBReagent) {
        this.mStatBReagent = statBReagent;
        this.mStatReagent[1] = statBReagent;
        return this;
    }

    public int getStatCReagent() {
        return mStatCReagent;
    }

    public SystemStateBean setStatCReagent(int statCReagent) {
        this.mStatCReagent = statCReagent;
        this.mStatReagent[2] = statCReagent;
        return this;
    }

    public int getStatDReagent() {
        return mStatDReagent;
    }

    public SystemStateBean setStatDReagent(int statDReagent) {
        this.mStatDReagent = statDReagent;
        this.mStatReagent[3] = statDReagent;
        return this;
    }

    public int getStatEReagent() {
        return mStatEReagent;
    }

    public SystemStateBean setStatEReagent(int statEReagent) {
        this.mStatEReagent = statEReagent;
        this.mStatReagent[4] = statEReagent;
        return this;
    }

    public int[] getStatReagent() {
        return mStatReagent;
    }

    public SystemStateBean setStatReagent(int[] statReagent) {
        if (statReagent == null) {
            throw new NullPointerException("参数statReagent为null");
        }
        if (statReagent.length < mStatReagent.length) {
            throw new IllegalArgumentException("参数statReagent数组长度不够（<5）");
        }
        this.mStatReagent = statReagent;
        this.mStatAReagent = statReagent[0];
        this.mStatBReagent = statReagent[1];
        this.mStatCReagent = statReagent[2];
        this.mStatDReagent = statReagent[3];
        this.mStatEReagent = statReagent[4];
        return this;
    }

    public int getStatDevFilled() {
        return mStatDevFilled;
    }

    /**
     * @return 流路是否被填充
     */
    public boolean isStatDevFilled() {return mStatDevFilled == 0 ? false : true;}

    public SystemStateBean setStatDevFilled(int statDevFilled) {
        this.mStatDevFilled = statDevFilled;
        return this;
    }

    public int getStatDevMotor() {
        return mStatDevMotor;
    }

    public SystemStateBean setStatDevMotor(int statDevMotor) {
        this.mStatDevMotor = statDevMotor;
        return this;
    }

    public int getTDevValue() {
        return mTDevValue;
    }

    public SystemStateBean setTDevValue(int mTDevValue) {
        this.mTDevValue = mTDevValue;
        return this;
    }

    public int getSpeedDevMotor() {
        return mSpeedDevMotor;
    }

    public SystemStateBean setSpeedDevMotor(int speedDevMotor) {
        this.mSpeedDevMotor = speedDevMotor;
        return this;
    }

    public int getStatLockLid() {
        return mStatLockLid;
    }

    public SystemStateBean setStatLockLid(int statLockLid) {
        this.mStatLockLid = statLockLid;
        return this;
    }
}
