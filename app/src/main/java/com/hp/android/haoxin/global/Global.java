package com.hp.android.haoxin.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.beans.SystemStateBean;
import com.hp.android.haoxin.command.CommandBridge;

/**
 * 全局（应用公用）变量
 */
public class Global {
	/**
	 * 转盘速度
	 */
	public static final int SPEED_NORMAL = 3000;		//正常
	public static final int SPEED_DRY = 1500;			//甩干
	public static final int SPEED_CENTRIFUGAL = 600;	//离心

	public static final String DATA_ZAIBOPIAN_NAME     = "zaibopian";
	public static final String DATA_RANSE_NAME         = "ranse";
	public static final String DATA_JIEJING_NAME       = "jiejing";
	public static final String DATA_DIANJIU_NAME       = "dianjiu";
	public static final String DATA_GUDING_NAME        = "guding";
	public static final String DATA_CHENGZHOGN_NAME    = "chengzhong";
	public static final String DATA_HEAT_NAME 		   = "jiare";

	public static final String DATA_A_PUMP =  "apump";
	public static final String DATA_B_PUMP =  "bpump";
	public static final String DATA_C_PUMP =  "cpump";
	public static final String DATA_D_PUMP =  "dpump";
	public static final String DATA_E_PUMP =  "epump";
	public static final String DATA_CELL_CENTRIFUGAL_SPEED =  "cell_centrifugal_speed";

	/**
	 * 载玻片的最小和最大值
	 */
	public static final int ZAIBOPIAN_NUM_MIN = 1;
	public static final int ZAIBOPIAN_NUM_MAX = 24;

	/**
	 * 系统参数
	 */
	public static int mZaiBoPianNum;
	public static int mRanSeHouDu;
	public static int mJieJingZiLev;
	public static int mDianJiuLev;
	public static int mGuDingQDState;
	public static int mChengZhong;
	public static int mHeat;

	/**
	 * 工程师菜单
	 */
	public static int mAPump;
	public static int mBPump;
	public static int mCPump;
	public static int mDPump;
	public static int mEPump;
	public static int mCellCentrifugalSpeed;

	/**
	 * 软硬件版本
	 */
	public static byte[] HW_VERSION;
	public static byte[] SW_VERSION;
	/**
	 * 系统状态
	 */
	private static SystemStateBean mSystemStateBean = new SystemStateBean();
	/**
	 * APP程序状态（比如启动中等...）
	 */
	private static GlobalState mState = GlobalState.NONE;


	/**
	 * 初始化Global参数
	 */
	public static void init() {
		mSystemStateBean.setCommActived(-1); //连接状态
	}
	/**
	 * 初始化系统参数
	 * @param context 上下文
	 */
	public static void initDatas(Context context){
		//初始化系统参数
		SharedPreferences sp = getSharePreferences(context);
		mZaiBoPianNum  = sp.getInt(DATA_ZAIBOPIAN_NAME, 5);
		mRanSeHouDu    = sp.getInt(DATA_RANSE_NAME, 5);
		mJieJingZiLev  = sp.getInt(DATA_JIEJING_NAME, 0);
		mDianJiuLev    = sp.getInt(DATA_DIANJIU_NAME, 1);
		mGuDingQDState = sp.getInt(DATA_GUDING_NAME, 0);
		mChengZhong    = sp.getInt(DATA_CHENGZHOGN_NAME, 1);

		mAPump = sp.getInt(DATA_A_PUMP, 12);
		mBPump = sp.getInt(DATA_B_PUMP, 12);
		mCPump = sp.getInt(DATA_C_PUMP, 12);
		mDPump = sp.getInt(DATA_D_PUMP, 12);
		mEPump = sp.getInt(DATA_E_PUMP, 12);
		mCellCentrifugalSpeed = sp.getInt(DATA_CELL_CENTRIFUGAL_SPEED, 1);
	}

	/**
	 * 初始化系统状态
	 */
	public static void initSystemState(Context context) {
		mSystemStateBean.setStatDevLid(context.getResources().getInteger(R.integer.stat_dev_lid_init_data));
        mSystemStateBean.setStatAReagent(context.getResources().getInteger(R.integer.stat_a_reagent_init_data));
		mSystemStateBean.setStatBReagent(context.getResources().getInteger(R.integer.stat_b_reagent_init_data));
		mSystemStateBean.setStatCReagent(context.getResources().getInteger(R.integer.stat_c_reagent_init_data));
		mSystemStateBean.setStatDReagent(context.getResources().getInteger(R.integer.stat_d_reagent_init_data));
		mSystemStateBean.setStatEReagent(context.getResources().getInteger(R.integer.stat_e_reagent_init_data));
		mSystemStateBean.setStatDevFilled(context.getResources().getInteger(R.integer.stat_dev_filled_init_data));
	}

	/**
	 * 保存系统参数，同时发送请求到下位机
	 * @param context 上下文
	 */
	public static void saveDatas(Context context) {
		//1.保存数据到文件
		SharedPreferences sp = getSharePreferences(context);
		Editor edit = sp.edit();
		edit.putInt(DATA_ZAIBOPIAN_NAME, mZaiBoPianNum);
		edit.putInt(DATA_RANSE_NAME, mRanSeHouDu);
		edit.putInt(DATA_JIEJING_NAME, mJieJingZiLev);
		edit.putInt(DATA_DIANJIU_NAME, mDianJiuLev);
		edit.putInt(DATA_GUDING_NAME, mGuDingQDState);
		edit.putInt(DATA_CHENGZHOGN_NAME, mChengZhong);
		edit.commit();

		//2.发送数据请求到设备
		sendDataToDevice();
	}

	public static void saveEngineerDatas(int aPump, int bPump, int cPump, int dPump, int ePump, int cellCentrifugalSpeed, Context context) {
		mAPump = aPump;
		mBPump = bPump;
		mCPump = cPump;
		mDPump = dPump;
		mEPump = ePump;
		mCellCentrifugalSpeed = cellCentrifugalSpeed;

		SharedPreferences sp = getSharePreferences(context);
		Editor edit = sp.edit();


		edit.putInt(DATA_A_PUMP, mAPump);
		edit.putInt(DATA_B_PUMP, mBPump);
		edit.putInt(DATA_C_PUMP, mCPump);
		edit.putInt(DATA_D_PUMP, mDPump);
		edit.putInt(DATA_E_PUMP, mEPump);
		edit.putInt(DATA_CELL_CENTRIFUGAL_SPEED, mCellCentrifugalSpeed);
		edit.commit();

		sendEngineerDataToDevice();
	}
	/**
	 * 发送参数数据到下位机
	 */
	public static void sendDataToDevice() {
		CommandBridge.getInstance().linkSiteDatas(mZaiBoPianNum, mRanSeHouDu, mGuDingQDState, mJieJingZiLev, mDianJiuLev, mChengZhong);
	}
	public static void sendEngineerDataToDevice() {
		CommandBridge.getInstance().linkEngineerDatas(mAPump + 1, mBPump + 1, mCPump + 1, mDPump + 1, mEPump + 1, mCellCentrifugalSpeed);
	}

	private static SharedPreferences getSharePreferences(Context context){
		return context.getSharedPreferences("custom_data", Context.MODE_PRIVATE);
	}

	public static int getDataByName(Context context,String key,int defValue){
		return getSharePreferences(context).getInt(key, defValue);
	}

	public static void setDataByName(Context context,String key,int value){
		getSharePreferences(context).edit().putInt(key, value).commit();
	}

	/**
	 * 设备是否处于连接中
	 * @return true 连接状态； false 非连接状态
	 */
	public static boolean isCommActived() {
		return mSystemStateBean.isCommActived();
	}

	/**
	 * 设置链路连接状态(1为连接，0为未连接）
	 * @param flag 正数为连接，其他为非连接
	 */
	public static void setCommActived(int flag) {
		mSystemStateBean.setCommActived(flag);
	}
    /**
     * 得到设备链路状态
     * @return 1为连接，0为未连接，默认-1未连接
     */
    public static int getCommActived() {
        return mSystemStateBean.getCommActived();
    }

	/**
	 * @return 得到系统状态
	 */
	public static SystemStateBean getSystemStateBean() {
		return mSystemStateBean;
	}

	/**
	 * @return 得到当前程序状态
	 */
	public static GlobalState getState() {
		return mState;
	}

	/**
	 * @return 重置系统状态为主界面
	 */
	public static void resetState() {
		mState = GlobalState.HOME;
	}
	/**
	 * 设置当前程序状态
	 * @param state 枚举类型
	 */
	public static void setState(GlobalState state) {
		Global.mState = state;
	}

}
