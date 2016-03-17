package com.hp.android.haoxin.beans;

import android.util.Log;

import com.hp.android.haoxin.global.Global;

/**
 * Created by AZZ on 15/11/7 21:21.
 */
public class CheckReagentStateBean {
    /**
     * 已经检查过试剂是否充足（当弹出试剂是否充足的选项框时,按继续就不需要再检查了）
     */
    private boolean[] hasCheckedReagent = new boolean[]{false, false, false, false, false};
    /**
     * 虚拟指针，用来指向当前检查的试剂
     */
    private int index = 0;

    /**
     * 虚拟错误指针，指向当前检查出来不足的试剂
     */
    private int errorIndex = -1;

    public CheckReagentStateBean() {
    }

    /**
     * 重置所有被检查过的试剂标签
     */
    public void resetAllCheckedReagent() {
        index = 0;
        hasCheckedReagent = new boolean[]{false, false, false, false, false};
        errorIndex = -1;
    }

    /**
     * 检查当前试剂状态
     * 如果出现试剂不足时，会把出现问题的试剂传到一个缓存（还没写）
     */
    public boolean checkCurrentReagent() {
        Log.i("CheckReagentStateBean", "checkCurrentReagent " + index);
        //index指针已经超过了所指范围，说明已经全部检查过了，这里允许通过
        if (index >= hasCheckedReagent.length) {
            Log.d("CheckReagentStateBean", "index 超出范围：index is " + index + ",lenght is " + hasCheckedReagent.length);
            return true;
        }
        //逐个检查ABCDE试剂是否充足
        for (int i = index; i < hasCheckedReagent.length; i++) {

//            if (hasCheckedReagent[i]) { //如果当前试剂已经检查过，就跳到下一试剂
//                Log.d("CheckReagentStateBean", "当前试剂（"+getReagentString(i)+"）已经检查过，检查下一试剂");
//                continue;
//            }

            //                hasCheckedReagent[i] = true; //表示检查过了

            if (Global.getSystemStateBean().isReagentEnough(i)) {
                Log.i("CheckReagentStateBean", getReagentString(i) + "试剂充足");
                index++; //指针自增1
                continue;
            } else {
                setErrorReagent(i);
                return false;
            }
        }

        return true;
    }

    private void setErrorReagent(int index) {
        errorIndex = index;
        Log.e("CheckReagentStateBean", getReagentString(index) + "试剂不足");
    }

    /**
     * @return 返回出错试剂的名称
     */
    public String getErrorReagentName() {
        return getReagentString(errorIndex);
    }

    /**
     * 得到下标为i的试剂的名称
     */
    private String getReagentString(int i) {
        return Global.getSystemStateBean().getReagentString(i);
    }
}
