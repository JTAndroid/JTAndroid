package com.tr.ui.home.utils;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.ui.tongren.TongRenFragment.CurrentTongRenFrgTitle;
//import com.tr.ui.home.MainActivity.CurrentFlowFrgTitle;
import com.utils.display.DisplayUtil;

public class HomeCommonUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2097342698150303864L;

	public static Set<String> fileAllList = new HashSet<String>();
	public static Set<String> fileAllListZonghe = new HashSet<String>();

	private static View mainBottomLine;

	public static TextView mainpageTv;

	public static TextView gintongpageTv;

	public static TextView flowpageTv;
	private static int basicWidth;
	private static int currentX;

	/**
	 * 初始化我的ActionBar
	 */
	public static void initActionBar(Context context, ActionBar mActionBar, String title) {

		TextView myTitle;
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = View.inflate(context, R.layout.home_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText(title);
	}

	/** 初始化带'下角标'ActionBar ν */
	public static void initLeftCustomActionBar(Context context, ActionBar mActionBar, String title, boolean showImage, OnClickListener listener, boolean isCenter, boolean isShowHome) {
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(isShowHome);
		mActionBar.setDisplayShowTitleEnabled(false);
		View mCustomView = View.inflate(context, R.layout.home_flow_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		// mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK |
		// Gravity.BOTTOM;

		// mActionBar.setTitle(title);
		ImageView image = (ImageView) mCustomView.findViewById(R.id.image);
		TextView titleTv = (TextView) mCustomView.findViewById(R.id.actionBartitleTv);
		titleTv.setText(title);
		titleTv.setOnClickListener(listener);
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;

		image.setBackgroundResource(R.drawable.demand_me_need_triangle_white);
		mActionBar.setCustomView(mCustomView, mP);
		if (showImage) {
			image.setVisibility(View.VISIBLE);
		} else {
			image.setVisibility(View.GONE);
		}
	}
	/** 初始化带'下角标'ActionBar ν 带数量*/
	public static void initLeftCustomActionBar(Context context, ActionBar mActionBar, String title, String sum, boolean showImage, OnClickListener listener, boolean isCenter) {
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		View mCustomView = View.inflate(context, R.layout.home_flow_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		// mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK |
		// Gravity.BOTTOM;

		// mActionBar.setTitle(title);
		ImageView image = (ImageView) mCustomView.findViewById(R.id.image);
		TextView titleTv = (TextView) mCustomView.findViewById(R.id.actionBartitleTv);
		TextView actionBarSumTv = (TextView) mCustomView.findViewById(R.id.actionBarSumTv);
		actionBarSumTv.setText(sum);
		titleTv.setText(title);
		titleTv.setOnClickListener(listener);
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;

		image.setBackgroundResource(R.drawable.demand_me_need_triangle_white);
		mActionBar.setCustomView(mCustomView, mP);
		if (showImage) {
			image.setVisibility(View.VISIBLE);
		} else {
			image.setVisibility(View.GONE);
		}
	}
	/** 初始化带'下角标'ActionBar ν */
	public static void initLeftMainActionBar(Context context, ActionBar mActionBar, String title, boolean showImage, OnClickListener listener, int colorId) {
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		View mCustomView = View.inflate(context, R.layout.home_flow_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		ImageView image = (ImageView) mCustomView.findViewById(R.id.image);
		TextView titleTv = (TextView) mCustomView.findViewById(R.id.actionBartitleTv);
		if(titleTv != null){
			titleTv.setText("   " + title + " ");
			titleTv.setOnClickListener(listener);
			titleTv.setTextColor(App.getApp().getResources().getColor(colorId));
		}
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		if(image != null){
			image.setBackgroundResource(R.drawable.demand_me_need_triangle_white);
		}
		mActionBar.setCustomView(mCustomView, mP);
		if (showImage && image != null) {
			image.setVisibility(View.VISIBLE);
		} else {
			image.setVisibility(View.GONE);
		}
	}

	public static void initHorizontalCustomActionBar(Context context, ActionBar actionbar, OnClickListener listener, CurrentTongRenFrgTitle currentFlowFrgTitle) {
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setDisplayShowTitleEnabled(false);
		View mCustomView = View.inflate(context, R.layout.home_flow_horizontal_actionbar_title, null);
		actionbar.setCustomView(mCustomView, new ActionBar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		// mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK |
		// Gravity.CENTER_HORIZONTAL;

		mainpageTv = (TextView) mCustomView.findViewById(R.id.mainpage);
		gintongpageTv = (TextView) mCustomView.findViewById(R.id.gintongpage);
		ViewTreeObserver vto = gintongpageTv.getViewTreeObserver();     
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {   
            @Override     
            public void onGlobalLayout() {   
            	gintongpageTv.getViewTreeObserver().removeGlobalOnLayoutListener(this);   
            	gintongpageTv.getHeight();  
            	basicWidth = gintongpageTv.getWidth();  
            }     
        });     
		flowpageTv = (TextView) mCustomView.findViewById(R.id.flowpage);
		mainBottomLine = (View) mCustomView.findViewById(R.id.mainBottomLine);
		if (listener!=null) {
			mainpageTv.setOnClickListener(listener);
			gintongpageTv.setOnClickListener(listener);
			flowpageTv.setOnClickListener(listener);
		}
		resetTextSize();
		int selectColor = App.getApp().getResources().getColor(R.color.actionbar_title_textcolor);
		switch (currentFlowFrgTitle) {
		case first:
			HomeCommonUtils.mainpageTv.setTextSize(DisplayUtil.sp2px(16, 1));
			HomeCommonUtils.mainpageTv.setTextColor(selectColor);
			startTranslateAnimation(currentFlowFrgTitle);
			break;
		case second:
			HomeCommonUtils.flowpageTv.setTextSize(DisplayUtil.sp2px(16, 1));
			HomeCommonUtils.flowpageTv.setTextColor(selectColor);
			startTranslateAnimation(currentFlowFrgTitle);
			break;
		case third:
			HomeCommonUtils.gintongpageTv.setTextSize(DisplayUtil.sp2px(16, 1));
			HomeCommonUtils.gintongpageTv.setTextColor(selectColor);
			startTranslateAnimation(currentFlowFrgTitle);
			break;
		}
	}

	public static void resetTextSize() {
		int defaultColor = App.getApp().getResources().getColor(R.color.homepage_title_bg_color);
		HomeCommonUtils.mainpageTv.setTextSize(DisplayUtil.sp2px(15, 1));
		HomeCommonUtils.gintongpageTv.setTextSize(DisplayUtil.sp2px(15, 1));
		HomeCommonUtils.flowpageTv.setTextSize(DisplayUtil.sp2px(15, 1));
		HomeCommonUtils.mainpageTv.setTextColor(defaultColor);
		HomeCommonUtils.gintongpageTv.setTextColor(defaultColor);
		HomeCommonUtils.flowpageTv.setTextColor(defaultColor);
	}

	/**
	 * 文件管理
	 * 
	 * @param context
	 */
	public static void readFileZonghe(final Context context) {
		fileAllListZonghe.clear();
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					try {
						readerAllFilesZonghe(Environment.getExternalStorageDirectory(), context);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					SharedPreferences upLoadParmar = context.getSharedPreferences("upLoad", Activity.MODE_PRIVATE);
					Editor editor = upLoadParmar.edit();
					editor.putBoolean("isCompZonghe", true);
					editor.commit();
					super.onPostExecute(result);
				}

			}.execute();
		} else {
			Toast.makeText(context, "读取文件失败", 0).show();
			// uploadFileDir = this.getCacheDir();
		}

	}

	/**
	 * 文件管理目录
	 * 
	 * @param dir
	 * @param context
	 */
	private static void readerAllFilesZonghe(File dir, Context context) {
		try {
			String zongHefuJian = "doc|docx|ppt|pptx|pdf|txt|xls|xlsx|rar|zip|7z|jpg|png|bmp|jpeg|gif|avi|mpeg|mpg|qt|ram|viv|avi|mp4|wmv|rmvb|mkv|vob|3gp|doc|wps|word|docx|aif|svx|snd|mid|voc|wav|mp3";
			File[] fs = dir.listFiles();
			for (int i = 0; i < fs.length; i++) {
				String path = fs[i].getAbsolutePath();
				if (!path.startsWith(".") && path.matches("^.*?\\.(" + zongHefuJian + ")$") && fs[i].length() > 0) {// doc|docx|ppt|pptx|pdf|txt|xls|xlsx
					fileAllListZonghe.add(path);
					// System.out.println(path);
				}
				if (!fs[i].getAbsolutePath().startsWith(".") && !fs[i].isHidden() && fs[i].isDirectory()) {
					try {
						readerAllFilesZonghe(fs[i], context);
						// System.out.println();
					} catch (Exception e) {
						Toast.makeText(context, "读取文件失败", 0).show();
						e.printStackTrace();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 需求
	 * 
	 * @param context
	 */
	public static void readFile(final Context context) {
		fileAllList.clear();
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					readerAllFiles(Environment.getExternalStorageDirectory(), context);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					SharedPreferences upLoadParmar = context.getSharedPreferences("upLoad", Activity.MODE_PRIVATE);
					Editor editor = upLoadParmar.edit();
					editor.putBoolean("isComp", true);
					editor.commit();
					super.onPostExecute(result);
				}

			}.execute();
		} else {
			Toast.makeText(context, "读取文件失败", 0).show();
			// uploadFileDir = this.getCacheDir();
		}

	}

	/**
	 * 需求目录
	 * 
	 * @param dir
	 * @param context
	 */
	private static void readerAllFiles(File dir, Context context) {
		String fuJian = "doc|docx|ppt|pptx|pdf|txt|xls|xlsx|rar|zip|7z";
		File[] fs = dir.listFiles();
		if (fs != null) {
			for (int i = 0; i < fs.length; i++) {
				String path = fs[i].getAbsolutePath();
				if (!path.startsWith(".") && path.matches("^.*?\\.(" + fuJian + ")$") && fs[i].length() > 0) {// doc|docx|ppt|pptx|pdf|txt|xls|xlsx
					fileAllList.add(path);
					// System.out.println(path);
				}
				if (!fs[i].getAbsolutePath().startsWith(".") && !fs[i].isHidden() && fs[i].isDirectory()) {
					try {
						readerAllFiles(fs[i], context);
						// System.out.println();
					} catch (Exception e) {
						Toast.makeText(context, "读取文件失败", 0).show();
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * actionbar底部滑块平移动画
	 * @param currentFlowFrgTitle 当前为那个页面？first首页，second推荐，third动态
	 * @param fromXDelta 起始
	 * @param toXDelta 结束
	 */
	public static void startTranslateAnimation(CurrentTongRenFrgTitle currentFlowFrgTitle) {
		TranslateAnimation ta  = null;
		switch (currentFlowFrgTitle) {
		case first:
			ta= new TranslateAnimation(currentX, 0, 0, 0);
			currentX = 0;
			break;
		case second:
			ta= new TranslateAnimation(currentX, HomeCommonUtils.basicWidth, 0, 0);
			currentX = HomeCommonUtils.basicWidth;
			break;
		case third:
			ta= new TranslateAnimation(currentX, HomeCommonUtils.basicWidth*2, 0, 0);
			currentX = HomeCommonUtils.basicWidth*2;
			break;
		}
		if (ta!=null) {
			ta.setDuration(200);
			ta.setFillAfter(true);
			mainBottomLine.startAnimation(ta);
		}
	}
	
	public static void setFileIconAccoredSuffixName(ImageView iv, String suffixName) {
		// 根据文件的类型 给文件设置不同的图标
		if (suffixName.equalsIgnoreCase("jpg")
				|| suffixName.equalsIgnoreCase("jpeg")
				|| suffixName.equalsIgnoreCase("png")
				|| suffixName.equalsIgnoreCase("bmp")) {
			iv.setImageResource(R.drawable.chat_ui_pic);
		} else if (suffixName.equalsIgnoreCase("mp4")
				|| suffixName.equalsIgnoreCase("rmvb")
				|| suffixName.equalsIgnoreCase("avi")
				|| suffixName.equalsIgnoreCase("mpeg")
				|| suffixName.equalsIgnoreCase("mkv")
				|| suffixName.equalsIgnoreCase("flv")) {
			iv.setImageResource(R.drawable.chat_ui_video);
		} else if (suffixName.equalsIgnoreCase("pdf")) {
			iv.setImageResource(R.drawable.chat_ui_pdf);
		} else if (suffixName.equalsIgnoreCase("txt")) {
			iv.setImageResource(R.drawable.chat_ui_txt);
		} else if (suffixName.equalsIgnoreCase("pptx")
				|| suffixName.equalsIgnoreCase("ppt")
				|| suffixName.equalsIgnoreCase("pptm")) {
			iv.setImageResource(R.drawable.chat_ui_ppt);
		} else if (suffixName.equalsIgnoreCase("doc")
				|| suffixName.equalsIgnoreCase("docx")
				|| suffixName.equalsIgnoreCase("docm")) {
			iv.setImageResource(R.drawable.chat_ui_word);
		} else if (suffixName.equalsIgnoreCase("doc")
				|| suffixName.equalsIgnoreCase("docx")) {
			iv.setImageResource(R.drawable.chat_ui_word);
		} else if (suffixName.equalsIgnoreCase("xlsx")
				|| suffixName.equalsIgnoreCase("xls")) {
			iv.setImageResource(R.drawable.chat_ui_excel);
		} else {
			iv.setImageResource(R.drawable.chat_file);
		}
	}
}
