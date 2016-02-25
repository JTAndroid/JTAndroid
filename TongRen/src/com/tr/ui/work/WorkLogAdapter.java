package com.tr.ui.work;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.work.BUAffar;
import com.tr.model.work.BUAffarLog;
import com.tr.ui.work.WorkMainAdapter.AffairItem;
/**
 * 事务日志的适配器
 * @author Administrator
 *
 */
public class WorkLogAdapter extends BaseAdapter {
	private Activity mParentActivity =null;//传过来的全局
	public List<BUAffarLog> mItemsList = null;//存放日志的集合
	private ListView mListView;//显示log的listview
	

	public WorkLogAdapter(Activity activity, List<BUAffarLog> inData,
			ListView inListView) {
		this.mParentActivity = activity;
		this.mItemsList = inData;
		this.mListView = inListView;
	}

	public void setItemList(List<BUAffarLog> inData) {
		this.mItemsList = inData;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mItemsList != null)
			return mItemsList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AffairLogItem vi;
		View infoView = convertView;
		if (mItemsList != null) {
			BUAffarLog vItem = mItemsList.get(position);
			
			if (infoView==null)
			{
				LayoutInflater inflater = (LayoutInflater) mParentActivity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				vi = new AffairLogItem();
				infoView = (LinearLayout) inflater.inflate(
						R.layout.work_log_cell, null);//显示的布局
				vi.TextViewTime = (TextView) infoView
						.findViewById(R.id.TextViewTime);
				vi.TextViewInfo = (TextView) infoView
						.findViewById(R.id.TextViewInfo);

				infoView.setTag(vi);
			}
			else
			{
				vi=(AffairLogItem) infoView.getTag();
			}
			
			vi.TextViewTime.setText(WorkNewTimeActivity.getDateStrByDayNew(vItem.ctime));
			vi.TextViewInfo.setText(vItem.info);
			if (vItem.type!=null && vItem.type.equals("x"))// "类型    n：新建 e:编辑，f：完成，r：重新开启，q：退出 x：过期",
			{
				vi.TextViewInfo.setTextColor(Color.rgb(255, 0, 0));
			}
			else
			{
				vi.TextViewInfo.setTextColor(Color.rgb(0, 0, 0));
			}
			
		}
		return infoView;
	}
	//对日志的时间进行格式修改
	public String getDataStrByDate(String inDate) {
		try {
			Log.d("xmx","getDataStrByDate:"+inDate);
			
			Calendar vcalendarInDate = Calendar.getInstance();
			int vInYear = Integer.parseInt(inDate.substring(0, 4));
			int vInMonth = Integer.parseInt(inDate.substring(4, 6));
			int vInDay = Integer.parseInt(inDate.substring(6, 8));
			vcalendarInDate.set(vInYear, vInMonth, vInDay);

			Calendar vcalendarNow = Calendar.getInstance();
			String vCurrentDate = vcalendarNow.get(Calendar.YEAR)
					+ WorkDatePickerDialog.intToStr2(vcalendarNow
							.get(Calendar.MONTH) + 1)
					+ WorkDatePickerDialog.intToStr2(vcalendarNow
							.get(Calendar.DAY_OF_MONTH));
			//Log.d("xmx","vCurrentDate:"+vCurrentDate+" vcalendarNow:"+vcalendarNow);
			
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date vinDate = df.parse(inDate);
			//Log.d("xmx","vinDate:"+vinDate.toString());
			Date vCurDate = df.parse(vCurrentDate);
			//Log.d("xmx","vCurDate:"+vCurDate.toString());
			long diff = vinDate.getTime() - vCurDate.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			Log.d("xmx", "vinDate:" + vinDate + " vCurDate:" + " days:" + days);
			if (days < 0) {
				// 比今天小
				if (days == -1) {
					// 昨天
					return "昨天," + Integer.parseInt(inDate.substring(4, 6))
							+ "月" + Integer.parseInt(inDate.substring(6, 8))
							+ "日";
				} else {
					// 昨天以前
					int vWeek = vcalendarInDate.get(Calendar.DAY_OF_WEEK);
					return getWeekStrByInt(vWeek) + ","
							+ Integer.parseInt(inDate.substring(4, 6)) + "月"
							+ Integer.parseInt(inDate.substring(6, 8)) + "日";
				}
			} else if (days == 0) {
				// 今天
				return "今天," + Integer.parseInt(inDate.substring(4, 6)) + "月"
						+ Integer.parseInt(inDate.substring(6, 8)) + "日";
			} else if (days == 1) {
				// 明天
				return "明天," + Integer.parseInt(inDate.substring(4, 6)) + "月"
						+ Integer.parseInt(inDate.substring(6, 8)) + "日";
			} else {
				int vWeek = vcalendarInDate.get(Calendar.DAY_OF_WEEK);
				return getWeekStrByInt(vWeek) + ","
						+ Integer.parseInt(inDate.substring(4, 6)) + "月"
						+ Integer.parseInt(inDate.substring(6, 8)) + "日";
			}
		} catch (Exception e) {
			Log.d("xmx","cash");
		}
		return "";
	}

	public String getWeekStrByInt(int inWeek) {
		if (inWeek == 1)
			return "星期一";
		if (inWeek == 2)
			return "星期二";
		if (inWeek == 3)
			return "星期三";
		if (inWeek == 4)
			return "星期四";
		if (inWeek == 5)
			return "星期五";
		if (inWeek == 6)
			return "星期六";
		if (inWeek == 7)
			return "星期日";
		return "";
	}

	public class AffairLogItem {
		public TextView TextViewTime;
		public TextView TextViewInfo;

	}

}
