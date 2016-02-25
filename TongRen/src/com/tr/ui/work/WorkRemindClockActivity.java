package com.tr.ui.work;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tr.R;
import com.tr.api.WorkReqUtil;
import com.tr.model.work.BUResponseData;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.WorkReqType;
/**
 * 事务状态
 * 
 * 启动方法：	WorkReqType.AFFAIR_MODIFY_STATUS
 * 
 */
public class WorkRemindClockActivity  extends Activity implements
 IBindData{

	private LinearLayout LinearLayoutShow1;
	private LinearLayout LinearLayoutShow2;
	private TextView TextViewTitle;
	private ImageView ImageViewRemain;
	private Button ButtonBack;
	
	private String mTitle;
	private long mAffarId;
	private long mUserId;
	
	private long mRemindTime;
	private long mRemindEndTime;
	private long mRemindSpace;
	
	private Ringtone mRingtone;
	private Vibrator mVibrator;
	private int mSound=0;
	
	WakeLock mWakelock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("xmx","onCreate");
		
		final Window win = getWindow();
		 win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
		 | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		 win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		 | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		 
		 
		setContentView(R.layout.work_remind_clock_activity);
		
		mTitle=getIntent().getStringExtra("Title");
		mAffarId=getIntent().getLongExtra("AffarId", 0);
		mUserId=getIntent().getLongExtra("UserId", 0);
		
		mRemindTime=System.currentTimeMillis();
		mRemindEndTime=getIntent().getLongExtra("RemindEndTime", 0);
		mRemindSpace=getIntent().getLongExtra("RemindSpace", 0);
		
		Log.d("xmx","activity-tile:"+mTitle+",affarid:"+mAffarId+",vUserId:"+mUserId+",vReminEnd:"+mRemindEndTime+",space:"+mRemindSpace);
		
		
		PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
		mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
		mWakelock.acquire();
		        
		
		if (mAffarId==0)
			finish();
		initView();
		initData();
		setClock();
		Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);  
		mRingtone = RingtoneManager.getRingtone(WorkRemindClockActivity.this,notification);  
		mVibrator = (Vibrator)getSystemService(WorkRemindClockActivity.VIBRATOR_SERVICE);  

		myHandler.sendEmptyMessageDelayed(100, 500);
		
	}
	
	
	Handler myHandler = new Handler() {  
        public void handleMessage(Message msg) {   
        	Log.d("xmx","onHander");
        	if (mSound==0)
    		{
        		Log.d("xmx","handle");
    			mSound=1;
    			mRingtone.play();
    			
    	        long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启   
    	        mVibrator.vibrate(pattern,2);           //重复两次上面的pattern 如果只想震动一次，index设为-1   
    	        
    		}
             super.handleMessage(msg);   
        }   
   };
   

	public void initView() {
		 LinearLayoutShow1=(LinearLayout) findViewById(R.id.LinearLayoutShow1);
			LinearLayoutShow2=(LinearLayout) findViewById(R.id.LinearLayoutShow2);
			TextViewTitle=(TextView) findViewById(R.id.TextViewTitle);
			ImageViewRemain=(ImageView) findViewById(R.id.ImageViewRemain);
			ButtonBack=(Button) findViewById(R.id.ButtonBack);
			
	}

	@Override
	public void onStop(){  
		Log.d("xmx","onStop");
		try
		{
			mVibrator.cancel(); 
			mRingtone.stop();
			mWakelock.release();
		}
		catch (RuntimeException e) {
			Log.d("xmx", e.getMessage());
		}
		super.onStop();  
    } 
	
	public void initData() {
		TextViewTitle.setText(mTitle);
	}
	
	public void onButtonBackClick(View v)
	{
		LinearLayoutShow1.setVisibility(View.VISIBLE);
		LinearLayoutShow2.setVisibility(View.GONE);
		ButtonBack.setVisibility(View.GONE);
		ImageViewRemain.setVisibility(View.VISIBLE);
		
	}

	public void OnDelayClick(View v)
	{
		LinearLayoutShow2.setVisibility(View.VISIBLE);
		LinearLayoutShow1.setVisibility(View.GONE);
		ButtonBack.setVisibility(View.VISIBLE);
		ImageViewRemain.setVisibility(View.GONE);
	}
	
	public void OnConfirmClick(View v)
	{
		Log.d("xmx","OnConfirmClick:"+mAffarId);
		WorkReqUtil.modifyAffarStatus(WorkRemindClockActivity.this, this, mAffarId
				+ "", mUserId + "", "f", null);
	}
	
	@Override
	public void bindData(int tag, Object object) {
		Log.d("xmx", "bindData:" + tag);
		if (object != null) {
			Log.d("xmx", "bindData:" + object.toString());
		}
		else
		{
//			Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
			return;
		}
		switch (tag) {
		case WorkReqType.AFFAIR_MODIFY_STATUS: // 改状态
			BUResponseData vResponseDataModify = (BUResponseData) object;
			if (vResponseDataModify.succeed) {
				// 成功
				String vMsg = "事务已完成";

				Toast.makeText(this, vMsg, Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(this, "修改状态失败", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	
	public void OnCloseClick(View v)
	{
		Log.d("xmx","OnCloseClick"+mAffarId);
		finish();
		
	}
	public void OnDelay5Click(View v)
	{
		Log.d("xmx","OnDelay5Click");
		setClockDealy(5);
		finish();
	}
	public void OnDelay30Click(View v)
	{
		Log.d("xmx","OnDelay30Click");
		setClockDealy(30);
		finish();
		
	}
	public void OnDelay60Click(View v)
	{
		Log.d("xmx","OnDelay60Click");
		setClockDealy(60);
		finish();

	}
	
	
	public void setClock() {
		if (mRemindSpace>0)
		{
			if (mRemindTime+mRemindSpace<mRemindEndTime)
			{
				Intent intent = new Intent(WorkRemindClockActivity.this,
						WorkRemindClockReceiver.class);
				intent.setAction("JT_WORK");
				intent.setType("REMIND");
				intent.setData(Uri.EMPTY);
				intent.addCategory("CATEGORY_ACTIVITY");
				
				intent.putExtra("Title", mTitle);
				intent.putExtra("AffarId", mAffarId);
				intent.putExtra("UserId", mUserId);
				intent.putExtra("RemindEndTime", mRemindEndTime);
				intent.putExtra("RemindSpace", mRemindSpace);
				
				int vAffid=(int) mAffarId;
				
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						WorkRemindClockActivity.this, vAffid, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				// alarmCount是你需要记录的闹钟数量，必须保证你所发的alarmCount不能相同，最后一个参数填0就可以。
				AlarmManager am = (AlarmManager) getSystemService(WorkRemindClockActivity.this.ALARM_SERVICE);
				am.set(AlarmManager.RTC_WAKEUP, mRemindTime+mRemindSpace, pendingIntent);
				Log.d("xmx","cur remainTime:"+mRemindTime+",add:"+mRemindSpace);
				Log.d("xmx","next remainTime:"+(mRemindTime+mRemindSpace)+"");
				
			}
		}
	}

	public void setClockDealy(int inDelayTime) {
		int vDelayTime=inDelayTime*60*1000;
		
		//if (mRemindSpace>0)
		{
			//if (mRemindTime+mRemindSpace<mRemindEndTime)
			{
				Intent intent = new Intent(WorkRemindClockActivity.this,
						WorkRemindClockReceiver.class);
				intent.setAction("JT_WORK");
				intent.setType("REMIND");
				intent.setData(Uri.EMPTY);
				intent.addCategory("CATEGORY_ACTIVITY");
				
				intent.putExtra("Title", mTitle);
				intent.putExtra("AffarId", mAffarId);
				intent.putExtra("UserId", mUserId);
				intent.putExtra("RemindEndTime", mRemindEndTime);
				intent.putExtra("RemindSpace", 0);
				
				int vAffid=(int) mAffarId;
				
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						WorkRemindClockActivity.this, vAffid, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				// alarmCount是你需要记录的闹钟数量，必须保证你所发的alarmCount不能相同，最后一个参数填0就可以。
				AlarmManager am = (AlarmManager) getSystemService(WorkRemindClockActivity.this.ALARM_SERVICE);
				am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+vDelayTime, pendingIntent);
				Log.d("xmx","cur remainTime:"+mRemindTime+",add:"+vDelayTime);
				Log.d("xmx","next remainTime:"+(System.currentTimeMillis()+vDelayTime)+"");
				
			}
		}
		
	}
	
}
