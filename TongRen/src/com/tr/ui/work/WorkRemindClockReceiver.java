package com.tr.ui.work;


import com.tr.R;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;


public class WorkRemindClockReceiver extends BroadcastReceiver{
 
	//通知栏
		private NotificationManager updateNotificationManager = null;
		private Notification updateNotification = null;
		//通知栏跳转Intent
		private Intent updateIntent = null;
		private PendingIntent updatePendingIntent = null;
		
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Context context=arg0;
		Intent  intent=arg1;

		//例如这个id就是你传过来的
		String vTitle=intent.getStringExtra("Title");
		long vAffarId=intent.getLongExtra("AffarId", 0);
		long vUserId=intent.getLongExtra("UserId", 0);
		long vRemindEndTime=intent.getLongExtra("RemindEndTime", 0);
		long vRemindSpace=intent.getLongExtra("RemindSpace", 0);
		
		
		Log.d("xmx","tile remind:"+vTitle+",affarid:"+vAffarId+",vUserId:"+vUserId+",vReminEnd:"+vRemindEndTime+",space:"+vRemindSpace);
		
		/*
	    this.updateNotificationManager = (NotificationManager)context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
	    this.updateNotification = new Notification();

	    //设置下载过程中，点击通知栏，回到主界面
	    updateIntent = new Intent(context, null);
	    updatePendingIntent = PendingIntent.getActivity(context,0,updateIntent,0);

	    //设置通知栏显示内容
	    updateNotification.defaults |= Notification.DEFAULT_SOUND; 
	    updateNotification.defaults |= Notification.DEFAULT_VIBRATE; 
	    long[] vibrate = {0,100,200,300}; 
	    updateNotification.vibrate = vibrate ;
	    updateNotification.icon = R.drawable.ic_launcher;
	    updateNotification.tickerText = "事物提醒";
	    //updateNotification.setLatestEventInfo(this,getResources().getString(titleId),"0%",updatePendingIntent);

	    //发出通知
	    updateNotificationManager.notify(0,updateNotification);
	    */
		
	    /*
	    long mRemindTime=System.currentTimeMillis();
	    
	    if (vRemindSpace>0)
		{
			if (mRemindTime+vRemindSpace<vRemindEndTime)
			{
				Intent intentClock = new Intent(context,
						WorkRemindClockReceiver.class);
				intentClock.setAction("JT_WORK");
				intentClock.setType("REMIND");
				intentClock.setData(Uri.EMPTY);
				intentClock.addCategory("CATEGORY_ACTIVITY");
				
				intentClock.putExtra("Title", vTitle);
				intentClock.putExtra("AffarId", vAffarId);
				intentClock.putExtra("UserId", vUserId);
				intentClock.putExtra("RemindEndTime", vRemindEndTime);
				intentClock.putExtra("RemindSpace", vRemindSpace);
				
				int vAffid=(int) vAffarId;
				
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						context, vAffid, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				// alarmCount是你需要记录的闹钟数量，必须保证你所发的alarmCount不能相同，最后一个参数填0就可以。
				AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
				am.set(AlarmManager.RTC_WAKEUP, mRemindTime+vRemindSpace, pendingIntent);
				Log.d("xmx","cur ser remainTime:"+mRemindTime+",add:"+vRemindSpace);
				Log.d("xmx","next ser remainTime:"+(mRemindTime+vRemindSpace)+"");
				
			}
		}
	    
	    */
		
        Intent vIntent=new Intent(context,WorkRemindClockActivity.class);
        vIntent.putExtra("Title", vTitle);
        vIntent.putExtra("AffarId", vAffarId);
        vIntent.putExtra("UserId", vUserId);
        vIntent.putExtra("RemindEndTime", vRemindEndTime);
        vIntent.putExtra("RemindSpace", vRemindSpace);
        vIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        context.startActivity(vIntent);

	}
	
}
