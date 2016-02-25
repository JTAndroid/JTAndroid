package com.tr.ui.conference.myhy.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tr.R;

public class Utils {
	public static final int DIALOG_LEFT = R.id.hy_mymeeting_dialog_btn_cancel;
	public static final int DIALOG_RIGHT = R.id.hy_mymeeting_dialog_btn_sure;
	public final static String CREATE_MEETING = "create_meeting";
	public final static String MEETING = "meeting";
	/**
	 * 人员处理会议的状态 0：草稿，1：发起,2会议进行中，3会议结束
	 */
	public final static int MEETING_STATUS_DRAFT = 0;
	public final static int MEETING_STATUS_APPEND = 1;
	public final static int MEETING_STATUS_RUNNING = 2;
	public final static int MEETING_STATUS_FINISH = 3;

	/**
	 * 从字符串中解析日期
	 * 
	 * @author xgp
	 * @param dateStr
	 * @return
	 */
	public static String parseDate(String dateStr) {
		if(null==dateStr){
			return "";
		}
		if(dateStr.isEmpty()){
			return "";
		}
		try {
			String frist = dateStr.substring(0, dateStr.indexOf(" "));
			String[] arr = frist.split("-");
			return arr[1] + "月" + arr[2] + "日";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 从字符串中解析日期:xxxx-xx-xx
	 * 
	 * @author xgp
	 * @param dateStr
	 * @return
	 */
	public static String splitDate(String dateStr) {
		if(null==dateStr){
			return "";
		}
		if(dateStr.isEmpty()){
			return "";
		}
		try {
			String frist = dateStr.substring(0, dateStr.indexOf(" "));
			return frist;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 从字符串中解析日期和星期
	 * 
	 * @author xgp
	 * @param dateStr
	 * @return
	 */
	public static String parseTime(String dateStr) {
		if(null==dateStr){
			return "";
		}
		if(dateStr.isEmpty()){
			return "";
		}
		try {
			String frist = dateStr.substring(0, dateStr.indexOf(" "));
			String[] arr = frist.split("-");
			String second=dateStr.substring(dateStr.indexOf(" "), dateStr.lastIndexOf(":"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(format.parse(frist));
			String myWeek = null;
			switch(c.get(Calendar.DAY_OF_WEEK))
			{
			case 1:
			myWeek="日";
			break;
			case 2:
			myWeek="一";
			break;
			case 3:
			myWeek="二";
			break;
			case 4:
			myWeek="三";
			break;
			case 5:
			myWeek="四";
			break;
			case 6:
			myWeek="五";
			break;
			case 7:
			myWeek="六";
			break;
			default:
			break;
			}
			return arr[0]+"年"+arr[1] + "月" + arr[2] + "日"+" "+"星期"+myWeek+" "+second;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 从字符串中解析日期（无week）
	 * 
	 * @author xgp
	 * @param dateStr
	 * @return
	 */
	public static String parseTimeWithOutWeek(String dateStr) {
		if(null==dateStr){
			return "";
		}
		if(dateStr.isEmpty()){
			return "";
		}
		try {
			String frist = dateStr.substring(0, dateStr.indexOf(" "));
			String[] arr = frist.split("-");
			String second=dateStr.substring(dateStr.indexOf(" "), dateStr.lastIndexOf(":"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.setTime(format.parse(frist));
			return arr[0]+"年"+arr[1] + "月" + arr[2] + "日"+" "+second;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 显示ProgressDialog
	 * 
	 * @param context
	 *            Context
	 * @param msg
	 *            文本
	 * @param params
	 *            如果需要设置样式,放置在这里
	 * @return
	 */
	public static AlertDialog showProgressDialog(Context context, String title,
			String msg, OnClickListener cancelLinstener,
			OnClickListener sureLinstener) {
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.hy_mymetting_dialog_confrim);
		if (!TextUtils.isEmpty(title)) {
			TextView tvTitle = (TextView) window
					.findViewById(R.id.hy_mymeeting_dialog_tv_title);
			tvTitle.setText(title);
		}
		if (!TextUtils.isEmpty(msg)) {
			TextView tvMsg = (TextView) window
					.findViewById(R.id.hy_mymeeting_dialog_tv_message);
			tvMsg.setText(msg);
		}
		if (cancelLinstener != null) {
			Button btnCancel = (Button) window
					.findViewById(R.id.hy_mymeeting_dialog_btn_cancel);
			btnCancel.setOnClickListener(cancelLinstener);
		}
		if (sureLinstener != null) {
			Button btnCancel = (Button) window
					.findViewById(R.id.hy_mymeeting_dialog_btn_sure);
			btnCancel.setOnClickListener(sureLinstener);
		}
		return dlg;
	}

}
