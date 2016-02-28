package com.hp.android.haoxin.workview;

import android.content.Context;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.command.CommandBridge;

/**
 * 离心界面
 * Created by AZZ on 15/9/8 21:22.
 */
public class WorkCentrifugalView extends WorkCleanView{
    public WorkCentrifugalView(Context context) {
        super(context);
    }
    @Override
    public int getTitleId() {
        return R.drawable.index_title;
    }

    @Override
    protected int getCancelMsgId() {
        return R.string.dialog_msg_cancel_centrifugal;
    }
    @Override
    protected void start() {
        CommandBridge.getInstance().linkCentrifugalStart(getContext());
    }

    @Override
    protected void cancel() {
        CommandBridge.getInstance().linkCentrifugalCancel();
    }

    @Override
    protected void finished() {
        dismissCancelDialog();
        controller.curr2Success(R.string.succ_centrifugal, ViewController.VIEW_SYSTEM);
        controller.getMenuFragment().setSelected(3);
    }

    @Override
    protected void cancelListener() {
        controller.changeView(ViewController.VIEW_SYSTEM);
        controller.getMenuFragment().setSelected(3); //取消返回系统设置界面
    }
}
