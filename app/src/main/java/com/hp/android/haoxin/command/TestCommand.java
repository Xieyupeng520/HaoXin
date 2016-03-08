package com.hp.android.haoxin.command;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hp.android.haoxin.HaoXinApplication;
import com.hp.android.haoxin.widgets.CustomDialog;


import java.io.IOException;
import java.util.Random;

public abstract class TestCommand extends Activity implements CommandInterface{

	protected static CommandBridge mCommand = CommandBridge.getInstance();
	/**
	 * 系统上下文，无法创建Dialog
	 */
	protected static Context mContext = HaoXinApplication.getAppContext();
	
	
	private UsbManager manager;
	private UsbSerialDriver driver;

	// connect usb init
	public void usbOTGinit() {
		try {
			/*
			 * // Get UsbManager from Android. UsbManager manager = (UsbManager)
			 * getSystemService(Context.USB_SERVICE);
			 */

			/*
			 * // Find the first available driver. UsbSerialDriver driver =
			 * UsbSerialProber.acquire(manager);
			 */

			driver = UsbSerialProber.acquire(manager);

			if (driver != null) {
				try {
					driver.open();
					// textbox_send.setText("���ڴ򿪳ɹ���");
					driver.setBaudRate(115200);
				} catch (IOException e1) {
					// textbox_send.setText("���ڴ�ʧ�ܣ�"+e1.getMessage());
				}
			} else {
				// textbox_send.setText("No serial device!");
			}
		} catch (Exception ex) {
			// textbox_send.setText("���ڴ�ʧ�ܣ�"+ex.getMessage());
		}
	}

	// close connect USB
	public void usbClose() {

		try {
			/*
			 * // Get UsbManager from Android. UsbManager manager = (UsbManager)
			 * getSystemService(Context.USB_SERVICE);
			 */

			// Find the first available driver.
			/* UsbSerialDriver driver = UsbSerialProber.acquire(manager); */

			if (driver != null) {
				try {
					driver.close();
					// textbox_send.setText("���ڹرճɹ���");
				} catch (IOException e) {
					// textbox_send.setText("���ڹر�ʧ�ܣ�" + e.getMessage());
				}
			}
		} catch (Exception ex) {
			// textbox_send.setText("���ڴ�ʧ�ܣ�" + ex.getMessage());
		}
	}

	// send Data
	public void sendData() {
		try {
			/*
			 * // Get UsbManager from Android. UsbManager manager = (UsbManager)
			 * getSystemService(Context.USB_SERVICE);
			 * 
			 * // Find the first available driver. UsbSerialDriver driver =
			 * UsbSerialProber.acquire(manager);
			 */
			byte buffer[] = new byte[1000];
			buffer = "buffer".getBytes();
			// buffer = textbox_send.getText().toString().getBytes("gb2312");
			driver.write(buffer, 1000);
			// Log.d(TAG, "Read " + numBytesRead + " bytes.");
		} catch (Exception ex) {
			// textbox_send.setText("������Ϣʧ�ܣ�" + ex.getMessage());
		}
	}

	// receive Data
	public void receiveData() {
		try {
			/*
			 * // Get UsbManager from Android. UsbManager manager = (UsbManager)
			 * getSystemService(Context.USB_SERVICE);
			 * 
			 * // Find the first available driver. UsbSerialDriver driver =
			 * UsbSerialProber.acquire(manager);
			 */
			byte buffer[] = new byte[1000];
			driver.read(buffer, 1000);
			// textbox_receive.setText(buffer.toString());
			// Log.d(TAG, "Read " + numBytesRead + " bytes.");
		} catch (Exception ex) {
			// textbox_receive.setText("��������ʧ�ܣ�"+ex.getMessage());
		}
	}	
	
	

	public void showToast(final String text){
		//LogoActivity.handler.post(new Runnable() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
			}
		});

	}


	public void showDialogWithType(final int resId, final Context context, final CustomDialog.CustomDialogType type) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (context == null) {
					Log.e("TestCommand", "showHintDialog --- context is null!");
					return;
				}
				final CustomDialog dialog = CustomDialog.createDialogByType(context, resId, type);
				dialog.setCancelBtnVisibilty(View.GONE);
				mCommand.call.workFinish(false);

				//按返回键时
				dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {
						dialog.dismiss();
					}
				});
				dialog.setPositiveListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						dialog.dismiss();
					}
				});
				dialog.setNegativeListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						dialog.dismiss();
					}
				});
			}
		});
	}

	@Override
	public void loadingStart(Context context) {
		showToast("开始加载");
		final Handler updateUpdateHandler = new Handler();
		new Handler().post(new Runnable() {
			int t = -1;
			Random random = new Random();
			public void run() {
				t++;
				if(t < 5){
					mCommand.call.loadingSetStep(t);//加载开始一步，调用一次该方法，t为步数
					updateUpdateHandler.postDelayed(this, 1800 + random.nextInt(5) * 100);
				}
			}
		});
	}

	@Override
	public void dyeSetZaiBoPian(int number) {
		showToast("接收到：染色载玻片数目->"+number);
	}

	@Override
	/**
	 * 系统维护模块，模式、流量检测值发生改变时触发
	 * @param type  哪种检测模式： 0表示模式检测，1表示流量检测
	 * @param index 新的模式检测的值0~4（对应A-E）
	 * @param keyMode 0按下，1弹起
	 */
	public void systemJianCeChange(byte type, byte key, byte keyMode, Context context) {
		String ty = type == 0 ? "模式检测":"流量检测";
		showToast(ty);
	}

	@Override
	public void siteDates(int ranse, int guding, int jiejing, int dianjiu,
			int chengzhong) {
		showToast("虚拟硬件接收到：染色("+ranse+") 固定("+guding+") 结晶("+jiejing+") 碘酒("+dianjiu+") 称重("+chengzhong+")");
	}

	@Override
	public void siteChanged(int glassCount, int dyeingThickness, int alcoholFix, int crystalViolet, int iodine, int weigh) {
		showToast("虚拟硬件接收到：玻片数("+glassCount+") 染色("+dyeingThickness+") 固定("+alcoholFix+") 结晶("+crystalViolet+") 碘酒("+iodine+") 称重("+weigh+")");
	}

	private boolean isDyeCancel;
	private boolean isCleanCancel;
	private boolean isFillCancel;
	
	@Override
	public void dyeStart(Context context) {
		showToast("虚拟硬件接受到：开始染色:空转");
		isDyeCancel = false;
		final int progress[] = {20,45,50,88,100};
		final String des[] = {"番红","固定","结晶紫","碘酒",""};
		final int colors[] = {0xffffffff,0xffcc00ff,0xffff9933,0xff0000ff,0xffffffff};

		mCommand.call.workStartRotate(false);
		
		final Handler updateDyeHandler = new Handler();
		new Handler().post(new Runnable() {
			int t = -3;
			Random random;
			public void run() {
				if(isDyeCancel) return;
				if(random == null)random = new Random();
				t++;
				if(t == -1){
					showToast("离心");
					mCommand.call.workStartRotate(true);
				}
				
				if(t >=0 && t < 5){
					if(t == 0){
						showToast("染色");
					}
					mCommand.call.workSetProgress(progress[t]);
					mCommand.call.workSetProgressText("正在喷射: ", des[t]);
					mCommand.call.workStartSpout(t,false);
					mCommand.call.workStartDiskColorAnimation(colors[t], 2000, 0, 180);
					if(t == 4){
						mCommand.call.workFinish(true);
					}
				}
				if(t < 5){
					updateDyeHandler.postDelayed(this, 1800+random.nextInt(5)*100);
				}
			}
		});
	}

	@Override
	public void cleanStart(Context context) {
		isCleanCancel = false;
		showToast("虚拟硬件接受到：开始清洗");
		mCommand.call.workSetProgressText("正在清洗，请稍后...", "");
		
		mCommand.call.workStartSpout(0 ,true);
		mCommand.call.workStartSpout(1 ,true);
		mCommand.call.workStartSpout(2 ,true);
		mCommand.call.workStartSpout(3 ,true);
		mCommand.call.workStartSpout(4,true);
		mCommand.call.workStartSpout(5,true);
		mCommand.call.workStartDiskColorAnimation(0xffffffff, 5000, 0, 180);
		final Handler updateCleanHandler = new Handler();
		updateCleanHandler.post(new Runnable() {
			int progress = -1;
			Random random;
			public void run() {
				if(isCleanCancel)return;
				if(random == null)random = new Random();
				progress += random.nextInt(5)+3;
				if(progress > 100)progress = 100;
				mCommand.call.workSetProgress(progress);
				if(progress == 100){
					mCommand.call.workFinish(true);
				}
				if(progress < 100){
					updateCleanHandler.postDelayed(this, 500+random.nextInt(5)*100);
				}
			}
		});
	}

	@Override
	public void fillStart(Context context) {
		isFillCancel = false;
		showToast("虚拟硬件接受到：开始填充");
		mCommand.call.workSetProgressText("正在填充，请稍后...", "");
		
		final int colors[] = {0xffcc00ff,0xffff9933,0xff0000ff};
		final Handler updateFillHandler = new Handler();
		new Handler().post(new Runnable() {
			int progress = -1;
			int t = -1;
			Random random;
			public void run() {
				if(isFillCancel)return;
				t++;
				if(random == null)random = new Random();
				progress += random.nextInt(5)+5;
				if(progress > 100)progress = 100;
				mCommand.call.workSetProgress(progress);
				if(t < 9 && t%4 == 0){
					mCommand.call.workStartSpout(t / 4 + 1, false);
					mCommand.call.workStartDiskColorAnimation(colors[t / 4], 1500, 0, 200);
					if(t > 3){
						mCommand.call.workStopSpout(t/4);
					}
				}
				if(progress == 100){
					//mCommand.call.workStopRotate();
					mCommand.call.workFinish(true);
				}
				if(progress < 100){
					updateFillHandler.postDelayed(this, 500);
				}else{
					updateFillHandler.removeCallbacks(this);
				}
			}
		});
	}

	@Override
	public void dyeCancel() {
		isDyeCancel = true;
		showToast("虚拟硬件接收到：染色取消了");
	}

	@Override
	public void cleanCancel() {
		isCleanCancel = true;
		showToast("清洗取消了");
	}

	@Override
	public void fillCancel() {
		isFillCancel = true;
		showToast("虚拟硬件接收到：填充取消了");
	}

	/**
	 * 离心开始
	 *
	 * @param context
	 */
	@Override
	public void centrifugalStart(Context context) {
		showToast("离心开始");
	}

	/**
	 * 取消离心
	 */
	@Override
	public void centrifugalCancel() {
		showToast("离心取消");
	}

	/**
	 * 流路检测模块，当前的步数
	 *
	 * @param step 当前步数（从1开始算步数，0为取消)
	 */
	@Override
	public void liuluNext(int step) {
		showToast("虚拟硬件收到当前步数：" + step);
	}
	@Override
	public void liuluCancel(int step) {
		showToast("虚拟硬件接收到：流路检测取消了步数："+step);
	}

	@Override
	public void weighNext(int step) {
		showToast("虚拟硬件收到当前步数：" + step);
	}
	@Override
	public void chengzhongCancel(int step) {
		showToast("虚拟硬件接收到：称重校验取消了步数："+step);
	}
}
