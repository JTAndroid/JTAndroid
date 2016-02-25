package com.tr.ui.people.cread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;

import com.tr.R;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.organization.model.Customer;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerProfile;
import com.tr.ui.organization.model.profile.CustomerRemark;
import com.tr.ui.people.cread.utils.Utils;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;
import com.tr.ui.widgets.EProgressDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * activity的基类
 * 
 * @author Wxh07151732
 * 
 */
public class BaseActivity extends FragmentActivity {
	private PopupWindow popupWindow;
	public static Context context;
	public Hashtable<String, MyEditTextView> mDictionary;
	public Hashtable<String, MyEditTextView> mType;
	public Hashtable<String, MyEditTextView> mArea;
	public Hashtable<String, MyEditTextView> mInvestment;
	public Hashtable<String, MyLineraLayout> mLineraDictionary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDictionary = new Hashtable<String, MyEditTextView>();
		mType = new Hashtable<String, MyEditTextView>();
		mArea = new Hashtable<String, MyEditTextView>();
		mInvestment = new Hashtable<String, MyEditTextView>();
		mLineraDictionary = new Hashtable<String, MyLineraLayout>();
		context = this.getApplicationContext();
		customer = new Customer();
		customerProfile = new CustomerProfile();
		customer.profile = customerProfile;
		mType = new Hashtable<String, MyEditTextView>();
	
		// 获得手机分辨率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		s = dm.widthPixels + "*" + dm.heightPixels;
		Utils.init(context.getResources());
		displayMetrics = (int) Utils.convertDpToPixel(48);
	}

	public Context getContext() {
		return context;
	}

	public PopupWindow getPopupWindow() {
		return popupWindow;
	}
/**
 * 动态测量ListView条目的Height
 * @param listView
 * @param itemHeight
 */
	public static void setListViewHeightBasedOnChildren(ListView listView,
			int itemHeight) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = listAdapter.getCount() * itemHeight;

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	/**
	 * 适配问题解决
	 */
	public static void AdaptiveListView(ListView listView, int itemHeight) {

		// listview 因分辨率而异
		if (s.equals("1920*1080")) {
			setListViewHeightBasedOnChildren(listView);
		} else {
			setListViewHeightBasedOnChildren(listView, itemHeight);
		}
	}

	/**
	 * 动态的为listview测量条目高度
	 * 
	 * @param listView
	 */
	// 文字过长导致报错
	public static void setListViewHeightBasedOnChildren(ListView listView) {

		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
	
	/**
	 * 动态增加一整个模块(最后一个是自定义)
	 * @param list2  数据的key
	 * @param partent   布局父控件
	 * @param list   控件集合
	 * @param layouts  布局集合
	 * @return
	 */
	public ArrayList<MyEditTextView> ContinueAdd2(ArrayList<String> list2,
			final LinearLayout partent, ArrayList<MyEditTextView> list,
			final ArrayList<MyLineraLayout> layouts) {
		final MyLineraLayout layout = new MyLineraLayout(context);
		for (int i = 0; i < list2.size(); i++) {
			MyEditTextView editTextView = new MyEditTextView(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			editTextView.setTextLabel(list2.get(i));
			list.add(editTextView);
			layout.addView(editTextView);
			if (i == list2.size() - 1) {
				editTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								CustomActivity.class);
						startActivityForResult(intent, 99);
					}
				});
				editTextView.setChoose(true);
				editTextView.setAddMore_hint(true);
			}
		}
		MyDeleteView deleteView = new MyDeleteView(context);
		deleteView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				partent.removeView(layout);
				layouts.remove(layout);
			}
		});
		layouts.add(layout);
		layout.addView(deleteView);
		partent.addView(layout, 1);
		list2.removeAll(list2);
		return list;
	}
	/**
	 * 动态增加一整个模块(条目都是输入填写)
	 * @param list2  数据的key
	 * @param partent   布局父控件
	 * @param list   控件集合
	 * @param layouts  布局集合
	 * @return
	 */
	public ArrayList<MyEditTextView> ContinueAdd1(ArrayList<String> list2,
			final LinearLayout partent, ArrayList<MyEditTextView> list,
			final ArrayList<MyLineraLayout> layouts) {
		final MyLineraLayout layout = new MyLineraLayout(context);
		for (int i = 0; i < list2.size(); i++) {
			MyEditTextView editTextView = new MyEditTextView(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			editTextView.setTextLabel(list2.get(i));
			list.add(editTextView);
			layout.addView(editTextView);
		}
		MyDeleteView deleteView = new MyDeleteView(context);
		deleteView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				partent.removeView(layout);
				layouts.remove(layout);
			}
		});
		layouts.add(layout);
		layout.addView(deleteView);
		partent.addView(layout, 1);
		list2.removeAll(list2);
		return list;
	}

	/**
	 * 动态增加一整个模块
	 * 
	 * @param list2
	 *            标题的集合
	 * @param partent
	 *            控件的父类
	 * @param aaaaaaaaaa
	 *            控件的集合
	 * @param layouts
	 *            线性布局的集合
	 * @return
	 */
	public static int displayMetrics;
	private static String s;
	public Customer customer;
	public CustomerProfile customerProfile;

	

	/**
	 * 解析自定义数据
	 * 
	 * @param data
	 * @param mycustom
	 * @param Ll
	 * @param isNull
	 * @param aaaaaaaaaa
	 * @param custom
	 * @return
	 */
	protected ArrayList<CustomerPersonalLine> custom2(Intent data,
			MyEditTextView mycustom, LinearLayout Ll, boolean isNull,ArrayList<MyEditTextView> mCustom) {
		if (!isNull) {
//			for (int i = 0; i < mCustom.size(); i++) {
//				MyEditTextView editTextView = mCustom.get(i);
//				Ll.removeView(editTextView);
//			}
			Ll.removeAllViews();
			ArrayList<CustomerPersonalLine> custom = (ArrayList<CustomerPersonalLine>) data
					.getSerializableExtra("Customer_Bean");
			if (custom != null) {
				MyEditTextView editTextView = null;
				for (int i = 0; i < custom.size(); i++) {
					CustomerPersonalLine line = custom.get(i);
					if ("1".equals(line.type)) {
						if (!line.name.equals("")) {
							MyEditTextView myEditTextView_Text = new MyEditTextView(context);
							myEditTextView_Text.setText(line.content);
							myEditTextView_Text.setTextLabel(line.name);
							Ll.addView(myEditTextView_Text,
									Ll.indexOfChild(mycustom) );
							mCustom.add(myEditTextView_Text);
						}
					} 
					else if ("2".equals(line.type)) {
						if (!line.name.equals("")) {
							editTextView = new MyEditTextView(this);
							editTextView.setCustomtextLabel(line.content);
							editTextView.setTextLabel(line.name);
							editTextView.setReadOnly(true);
							editTextView.setJustLabel(true);
							Ll.addView(editTextView,
									Ll.indexOfChild(mycustom));
						}

					}
				}

			}
			return custom;
		} 
		else {
			mycustom.setTextLabel("自定义");
			mycustom.setAddMore_hint(true);
			mycustom.setChoose(true);
			mycustom.setText("");
			mycustom.setReadOnly(true);
			return null;
		}
	}
	protected EProgressDialog progressDialog;
	private String TAG = "BaseActivity";
	@Override
	protected void onDestroy() {
		dismissLoadingDialogHy();
		super.onDestroy();
	}
	/**
	 * 显示加载框
	 */
	protected void showLoadingDialogHy() {
		if (null == progressDialog) {
			progressDialog = new EProgressDialog(this);
		}
		progressDialog.setMessage("");
		progressDialog.show();
	}

	/**
	 * 隐藏加载框
	 */
	protected void dismissLoadingDialogHy() {
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		} catch (Exception e) {
			Log.d(TAG , e.getMessage());
		}
	}
}