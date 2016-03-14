package com.hp.android.haoxin.global;

/**
 * 程序状态（启动中...）
 * Created by AZZ on 15/8/21.
 */
public enum GlobalState {
    /**空*/
    NONE,
    /**启动状态*/
    LOADING,
    /**主界面*/
    HOME,
    /**染色*/
    DYE,
    /**清洗*/
    CLEAN,
    /**填充*/
    FILL,
    /**填充（从主页面start触发）*/
    FILL_FROM_DYE,
    /**填充（由清洗界面触发）*/
    FILL_FROM_CLEAN,
    /**流路检测*/
    CHECK_B_PASS,
    /**称重*/
    WEIGH,
    /**模式/流量检测*/
    CHECK_ABCDE,
    /**离心*/
    CENTRIFUGAL;

    /**
     * @return 是否在启动中
     */
    public boolean isLoading() {
        if (this == GlobalState.LOADING) {
            return true;
        }
        return false;
    }
}
