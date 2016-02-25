package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.conference.home.MeetingPiazzaActivity;
import com.tr.ui.conference.home.MeetingPiazzaActivity.TypeTitle;

public class MeetingPiazzaPopupWindow extends PopupWindow implements OnItemClickListener {

	private Context mContext;
	private OnListViewItemSelectClickListener mItemClickListener;

	private TypeTitle typeTitle;
	private int selectPosition;

	private Map<String, String> timeMap = new LinkedHashMap<String, String>();
	private Map<String, String> locationMap = new LinkedHashMap<String, String>();
	private String[] industrys = { "全部", "能源", "工商业", "矿产资源", "医药", "消费品", "金融", "房地产", "公共事业", "农林牧渔", "通信、媒体与科技" };

	public MeetingPiazzaPopupWindow(Context context, MeetingPiazzaActivity.TypeTitle typeTitle, int selectPosition) {
		mContext = context;
		this.typeTitle = typeTitle;
		this.selectPosition = selectPosition;

		timeMap.put("全部", "all");
		timeMap.put("今天", "today");
		timeMap.put("明天", "tomorrow");
		timeMap.put("本周", "week");
		timeMap.put("本月", "month");

		locationMap.put("全部", "");
		locationMap.put("附近", "near");
		locationMap.put("本市", "city");
		locationMap.put("其他城市", "other");

		View root = LayoutInflater.from(mContext).inflate(R.layout.meeting_piazza_popup_window_layout, null);
		
		if (industrys.equals(typeTitle)) {
			layout = (LinearLayout) root.findViewById(R.id.LinearLayout);
			int height = (int) ((((Activity) context).getWindowManager().getDefaultDisplay().getHeight())*0.6);
			LayoutParams layoutParams = layout.getLayoutParams();
			layoutParams.height = height;
			layout.setLayoutParams(layoutParams);
		}
		
		listView = (ListView) root.findViewById(R.id.listview);
		adapter = new DataListAdapter(typeTitle);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		// 设置SelectPicPopupWindow的View
		setContentView(root);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		setAnimationStyle(R.style.PupwindowAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x44000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);
	}

	private class DataListAdapter extends BaseAdapter {

		private ArrayList<String> strings = new ArrayList<String>();

		public ArrayList<String> getStrings() {
			return strings;
		}

		public DataListAdapter(TypeTitle typeTitle) {
			super();
			strings.clear();
			switch (typeTitle) {
			case time:
				Iterator<Entry<String, String>> iterator = timeMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, String> entry = iterator.next();
					strings.add(entry.getKey());
				}
				break;
			case address:
				Iterator<Entry<String, String>> locationMapIterator = locationMap.entrySet().iterator();
				while (locationMapIterator.hasNext()) {
					Entry<String, String> location = locationMapIterator.next();
					strings.add(location.getKey());
				}
				break;
			case industry:
				for (int i = 0; i < industrys.length; i++) {
					strings.add(industrys[i]);
				}
				break;
			}
		}

		@Override
		public int getCount() {
			return strings.size();
		}

		@Override
		public String getItem(int position) {
			return strings.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(mContext, R.layout.popup_listview_item_layout, null);
			TextView textView = (TextView) convertView.findViewById(R.id.textView);
			if (selectPosition == position) {
				Drawable drawable = mContext.getResources().getDrawable(R.drawable.select);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
				textView.setCompoundDrawables(null, null, drawable, null);
				textView.setTextColor(Color.rgb(249, 133, 18));
				textView.setBackgroundResource(R.color.find_project_view_bg);
			} else {
				textView.setCompoundDrawables(null, null, null, null);
				textView.setTextColor(Color.BLACK);
				textView.setBackgroundResource(R.color.text_white);
			}
			textView.setText(strings.get(position));
			return convertView;
		}

	}

	private ListView listView;
	private DataListAdapter adapter;
	private LinearLayout layout;

	/**
	 * 弹出
	 */
	public void showAsDropDown(View anchor) {

		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		super.showAsDropDown(anchor);
	}

	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(OnListViewItemSelectClickListener listener) {
		mItemClickListener = listener;
	}

	public interface OnListViewItemSelectClickListener {
		public void selectResult(TypeTitle typeTitle,String key, String value, int position);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String key = adapter.getItem(position);
		String value = "";
		switch (typeTitle) {
		case time:
			value = timeMap.get(key);
			break;
		case address:
			value = locationMap.get(key);
			break;
		case industry:
			value = key;
			break;
		}
		mItemClickListener.selectResult(typeTitle,key, value, position);
		dismiss();
	}
}
