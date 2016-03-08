package com.tr.ui.work;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.tr.R;
import com.tr.model.work.BUAffar;
import com.tr.model.work.BUAffarList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 事务适配器
 * @author Administrator
 *
 */
public class WorkMainAdapter extends BaseAdapter {

	private Activity mParentActivity;//全局
	public List<BUAffar> mItemsList = null;//列表条目的集合
	public BUAffarList mBuaffarList;//列表的集合
	private boolean redGone=false;
	public WorkMainAdapter(Activity activity, BUAffarList inData,
			ListView inListView) {
		this.mParentActivity = activity;
		this.mItemsList = inData.mAffarList;
		this.mBuaffarList = inData;
	}

	public void setItemList(List<BUAffar> inData) {
		this.mItemsList = inData;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mItemsList != null)
			return mItemsList.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AffairItem vi;
		View infoView = convertView;
		if (mItemsList != null) {
			BUAffar vItem = mItemsList.get(position);
			LayoutInflater inflater = (LayoutInflater) mParentActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//获得LayoutInflater实例
			vi = new AffairItem();
			Log.d("xmx",
					"position:" + position + " showtype:"
							+ vItem.getmShowType());

			if (vItem.getmShowType() == 0) {
				// 展示 事务
				infoView = (RelativeLayout) inflater.inflate(
						R.layout.work_main_affair_cell, null);
				vi.ImageViewColor = (ImageView) infoView
						.findViewById(R.id.ImageViewColor);
				vi.ButtonSound = (TextView) infoView
						.findViewById(R.id.ButtonSound);
				vi.TextViewTitle = (TextView) infoView
						.findViewById(R.id.TextViewTitle);
				vi.ImageViewRemind = (ImageView) infoView
						.findViewById(R.id.ImageViewRemind);
				vi.TextViewStatus = (TextView) infoView
						.findViewById(R.id.TextViewStatus);
				vi.TextViewDesc = (TextView) infoView
						.findViewById(R.id.TextViewDesc);
				//TODO   
				vi.iv_red  = (ImageView) infoView.findViewById(R.id.Iv_red);//红点提示控件
				
				vi.statusIv = (ImageView) infoView.findViewById(R.id.statusIv);

				vi.statusIv.setVisibility(View.GONE);
				//ffffff
				if (vItem.getColor() == 1 || vItem.getColor() == 2) {
					vi.ImageViewColor.setBackground(mParentActivity
							.getResources().getDrawable(
									R.drawable.icon_level3));
				} else if (vItem.getColor() == 3 || vItem.getColor() == 4){
					vi.ImageViewColor.setBackground(mParentActivity
							.getResources().getDrawable(
									R.drawable.icon_level2));
				}else if (vItem.getColor() == 0){
					vi.ImageViewColor.setBackground(mParentActivity
							.getResources().getDrawable(
									R.drawable.icon_level1));
				}

				Log.d("xmx",
						"titleType:" + vItem.titleType + " title:"
								+ vItem.getTitle());

				if (vItem.titleType.equals("t")) {
					vi.ButtonSound.setVisibility(View.GONE);
					if (vItem.getTitle().length() > 18) {
						vi.TextViewTitle.setText(vItem.getTitle().substring(0, 18)+"...");
					} else {
						vi.TextViewTitle.setText(vItem.getTitle());
					}
				} else {
					vi.TextViewTitle.setVisibility(View.GONE);
				}

				if (vItem.mListRemindType == 0) {
					vi.ImageViewRemind.setVisibility(View.GONE);
				} else {
					vi.ImageViewRemind.setVisibility(View.VISIBLE);
				}

				if (vItem.finished.equals("1")){
//					vi.TextViewStatus.setText("已完成");
					vi.statusIv.setVisibility(View.VISIBLE);
					vi.statusIv.setImageResource(R.drawable.work_d);
				}else if (vItem.expired.equals("1")) {
//					vi.TextViewStatus.setText("已过期");
					vi.statusIv.setVisibility(View.VISIBLE);
					vi.statusIv.setImageResource(R.drawable.work_e);
				}
				
				if (!redGone&&vItem.isNew.equals("1")) {
					vi.iv_red.setVisibility(View.VISIBLE);
				}else {
					vi.iv_red.setVisibility(View.GONE);
				}

				vi.TextViewDesc.setText(vItem.mLogDesc);
			} else {
				// 展示日期
				Log.d("xmx", "展示日期");
				infoView = (RelativeLayout) inflater.inflate(
						R.layout.work_main_date_cell, null);
				vi.TextViewDate = (TextView) infoView
						.findViewById(R.id.TextViewDate);
				vi.TextViewDate.setText(getDataStrByDate(vItem.getStartTime()
						.substring(0, 8)));
			}
			
			

		}
		return infoView;
	}
	public void setAllRedGone(boolean gone){
		this.redGone=gone;
		notifyDataSetChanged();
	}
	/**时间的格式化*/
	public String getDataStrByDate(String inDate) {
		try {
			Log.d("xmx", "getDataStrByDate:" + inDate);

			Locale.setDefault(Locale.CHINA);
			
			
			int vInYear = Integer.parseInt(inDate.substring(0, 4));
			int vInMonth = Integer.parseInt(inDate.substring(4, 6))-1;
			int vInDay = Integer.parseInt(inDate.substring(6, 8));
			
			Calendar vcalendarInDate = Calendar.getInstance();
			Log.d("xmx","olweek:"+vcalendarInDate.get(Calendar.DAY_OF_WEEK));
			vcalendarInDate.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  
			vcalendarInDate.set(vInYear, vInMonth, vInDay);
			Log.d("xmx","newweek:"+vcalendarInDate.get(Calendar.DAY_OF_WEEK));
			Log.d("xmx","vInYear:"+vInYear+" vInMonth:"+vInMonth+" vInDay:"+vInDay);

			
			Calendar vcalendarNow = Calendar.getInstance();
			String vCurrentDate = vcalendarNow.get(Calendar.YEAR)
					+ WorkDatePickerDialog.intToStr2(vcalendarNow
							.get(Calendar.MONTH) + 1)
					+ WorkDatePickerDialog.intToStr2(vcalendarNow
							.get(Calendar.DAY_OF_MONTH));
			//Log.d("xmx","vCurrentDate:"+vCurrentDate+" vcalendarInDate:"+vcalendarInDate);

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			Date vinDate = df.parse(inDate);
			// Log.d("xmx","vinDate:"+vinDate.toString());
			Date vCurDate = df.parse(vCurrentDate);
			// Log.d("xmx","vCurDate:"+vCurDate.toString());
			long diff = vinDate.getTime() - vCurDate.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			Log.d("xmx", "vinDate:" + inDate + " vCurDate:" +vCurrentDate+ " days:" + days);
			if (days < 0) {
				// 比今天小
				if (days == -1) {
					// 昨天
					return "昨天," + Integer.parseInt(inDate.substring(4, 6))
							+ "月" + Integer.parseInt(inDate.substring(6, 8))
							+ "日";
				} else {
					// 昨天以前
					if (vInYear != vcalendarNow.get(Calendar.YEAR)) {
						int vWeek = vcalendarInDate.get(Calendar.DAY_OF_WEEK);
						return getWeekStrByInt(vWeek) + ","
								+ Integer.parseInt(inDate.substring(0, 4))
								+ "年"
								+ Integer.parseInt(inDate.substring(4, 6))
								+ "月"
								+ Integer.parseInt(inDate.substring(6, 8))
								+ "日";
					} else {
						int vWeek = vcalendarInDate.get(Calendar.DAY_OF_WEEK);
						return getWeekStrByInt(vWeek) + ","
								+ Integer.parseInt(inDate.substring(4, 6))
								+ "月"
								+ Integer.parseInt(inDate.substring(6, 8))
								+ "日";
					}
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
				if (vInYear != vcalendarNow.get(Calendar.YEAR)) {
					int vWeek = vcalendarInDate.get(Calendar.DAY_OF_WEEK);
					return getWeekStrByInt(vWeek) + ","
							+ Integer.parseInt(inDate.substring(0, 4)) + "年"
							+ Integer.parseInt(inDate.substring(4, 6)) + "月"
							+ Integer.parseInt(inDate.substring(6, 8)) + "日";
				} else {
					int vWeek = vcalendarInDate.get(Calendar.DAY_OF_WEEK);
					return getWeekStrByInt(vWeek) + ","
							+ Integer.parseInt(inDate.substring(4, 6)) + "月"
							+ Integer.parseInt(inDate.substring(6, 8)) + "日";
				}
			}
		} catch (Exception e) {
			Log.d("xmx", "cash");
		}
		return "";
	}

	public String getWeekStrByInt(int inWeek) {
		if (inWeek == 2)
			return "星期一";
		if (inWeek == 3)
			return "星期二";
		if (inWeek == 4)
			return "星期三";
		if (inWeek == 5)
			return "星期四";
		if (inWeek == 6)
			return "星期五";
		if (inWeek == 7)
			return "星期六";
		if (inWeek == 1)
			return "星期日";
		return "";
	}

	public class AffairItem {
		/**事务条目颜色的选择显示"颜色 0默认 1:红，2:黄,3:绿,4:蓝 必填"*/
		public ImageView ImageViewColor;
		/**目前没有使用*/
		public TextView ButtonSound;
		/**事务条目的标题*/
		public TextView TextViewTitle;
		/**事务提醒的铃铛*/
		public ImageView ImageViewRemind;
		/**事务是否过期的显示*/
		public TextView TextViewStatus;
		/**事务的备注*/
		public TextView TextViewDesc;
		/**事务列表日期的显示*/
		public TextView TextViewDate;
		/**红点*/
		public ImageView iv_red;
		//事务完成状态
		public ImageView statusIv;

	}
}
