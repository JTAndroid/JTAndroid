package com.tr.ui.widgets;

import com.tr.R;
import com.tr.model.model.PeopleForeignLanguage;
import com.tr.ui.widgets.time.AbstractWheelTextAdapter;
import com.tr.ui.widgets.time.OnWheelChangedListener;
import com.tr.ui.widgets.time.WheelView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 人脉对话框(外语语种和等级)
 * @author leon
 *
 */
public class ConnsLanguageDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private WheelView languageWv; 
	private WheelView levelWv; 
	private TextView cancelTv; // 取消
	private TextView okTv; // 确定
	private ImageView sendIv; // 确定
	
	// 变量
	private Activity mContext;
	private CommonAdapter mLanguageAdapter;
	private CommonAdapter mLevelAdapter;
	private OnFinishListener mListener; 
	private PeopleForeignLanguage mPeopleLanguage;
	private String [] mLanguageSet = null;
	private String [] mLevelSet = null;

	public ConnsLanguageDialog(Activity context, PeopleForeignLanguage peopleLanguage, OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_language_dialog);
		mContext = context;
		mListener = listener;
		mPeopleLanguage = peopleLanguage;
		initVars();
		initDialogStyle();
		initControls();
	}
	
	private void initVars(){
		mLanguageSet = mContext.getResources().getStringArray(R.array.conns_languages);
		mLanguageAdapter = new CommonAdapter(mContext, mLanguageSet);
		mLevelSet = mContext.getResources().getStringArray(R.array.english_level);
		mLevelAdapter = new CommonAdapter(mContext,mLevelSet);
	}
	
	@SuppressWarnings("deprecation")
	private void initDialogStyle(){
		getWindow().setWindowAnimations(R.style.ConnsDialogAnim);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = mContext.getWindowManager().getDefaultDisplay().getWidth();
		wmlp.gravity = Gravity.BOTTOM ;
	}
	
	// 初始化控件
	private void initControls(){
		// 取消
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(mClickListener);
		// 确定
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(mClickListener);
		//
		sendIv = (ImageView) findViewById(R.id.sendIv);
		sendIv.setOnClickListener(mClickListener);
		// 滚轮
		languageWv = (WheelView) findViewById(R.id.languageWv);
		languageWv.setVisibleItems(3);
		languageWv.setViewAdapter(mLanguageAdapter);
		//
		levelWv = (WheelView) findViewById(R.id.levelWv);
		levelWv.setVisibleItems(3);
		levelWv.setViewAdapter(mLevelAdapter);
		
		if(mPeopleLanguage != null){ // 编辑
			if(mPeopleLanguage.type != null
					|| !TextUtils.isEmpty(mPeopleLanguage.type)){
				for (int i = 0; i < mLanguageSet.length; i++) {
					if (mPeopleLanguage.type.equals(mLanguageSet[i])) {
						languageWv.setCurrentItem(i);
						break;
					}
				}
				switch(languageWv.getCurrentItem()){
				case 0:
					mLevelSet = mContext.getResources().getStringArray(R.array.english_level);
					break;
				case 1:
					mLevelSet = mContext.getResources().getStringArray(R.array.japanese_level);
					break;
				case 2:
					mLevelSet = mContext.getResources().getStringArray(R.array.frence_level);
					break;
				case 3:
					mLevelSet = mContext.getResources().getStringArray(R.array.german_level);
					break;
				case 4:
					mLevelSet = mContext.getResources().getStringArray(R.array.korean_level);
					break;
				case 5:
					mLevelSet = mContext.getResources().getStringArray(R.array.russian_level);
					break;
				case 6:
					mLevelSet = mContext.getResources().getStringArray(R.array.spanish_level);
					break;
				case 7:
					mLevelSet = mContext.getResources().getStringArray(R.array.portuguese_level);
					break;
				case 8:
					mLevelSet = mContext.getResources().getStringArray(R.array.arabic_level);
					break;
				case 9:
					mLevelSet = mContext.getResources().getStringArray(R.array.italian_level);
					break;
				}
				mLevelAdapter.update(mLevelSet);
				if(mPeopleLanguage.levelType != null
						&& !TextUtils.isEmpty(mPeopleLanguage.levelType)){
					for (int i = 0; i < mLevelSet.length; i++) {
						if (mPeopleLanguage.levelType.equals(mLevelSet[i])) {
							levelWv.setCurrentItem(i);
							break;
						}
					}
				}
			}
			okTv.setVisibility(View.VISIBLE);
			sendIv.setVisibility(View.GONE);
		}
		languageWv.addChangingListener(new OnWheelChangedListener(){

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
				switch(newValue){
				case 0:
					mLevelSet = mContext.getResources().getStringArray(R.array.english_level);
					break;
				case 1:
					mLevelSet = mContext.getResources().getStringArray(R.array.japanese_level);
					break;
				case 2:
					mLevelSet = mContext.getResources().getStringArray(R.array.frence_level);
					break;
				case 3:
					mLevelSet = mContext.getResources().getStringArray(R.array.german_level);
					break;
				case 4:
					mLevelSet = mContext.getResources().getStringArray(R.array.korean_level);
					break;
				case 5:
					mLevelSet = mContext.getResources().getStringArray(R.array.russian_level);
					break;
				case 6:
					mLevelSet = mContext.getResources().getStringArray(R.array.spanish_level);
					break;
				case 7:
					mLevelSet = mContext.getResources().getStringArray(R.array.portuguese_level);
					break;
				case 8:
					mLevelSet = mContext.getResources().getStringArray(R.array.arabic_level);
					break;
				case 9:
					mLevelSet = mContext.getResources().getStringArray(R.array.italian_level);
					break;
				}
				mLevelAdapter.update(mLevelSet);
				levelWv.setCurrentItem(0);
			}
		});
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				ConnsLanguageDialog.this.dismiss();
				break;
			case R.id.okTv: // 确定
			case R.id.sendIv:
				
				if(mPeopleLanguage == null){
					mPeopleLanguage = new PeopleForeignLanguage();
				}
				mPeopleLanguage.type = (mLanguageAdapter.getItemText(languageWv.getCurrentItem())+"");
				mPeopleLanguage.levelType = (mLevelAdapter.getItemText(levelWv.getCurrentItem())+"");
				if(mListener != null){
					mListener.onFinish(mPeopleLanguage);
				}
				ConnsLanguageDialog.this.dismiss();
				break;
			}
		}
	};
	
	private class CommonAdapter extends AbstractWheelTextAdapter {

		private String [] data = null;
		
		protected CommonAdapter(Context context, String[] data) {
			super(context, R.layout.widget_wheel_item, R.id.itemTv);
			this.data = data;
		}
		
		public void update(String [] data){
			this.data = data;
			this.notifyDataChangedEvent();
			this.notifyDataInvalidatedEvent();
		}

		@Override
		public int getItemsCount() {
			return data.length;
		}

		@Override
		protected CharSequence getItemText(int position) {
			return data[position];
		}
	}
	
	public interface OnFinishListener{
		public void onFinish(PeopleForeignLanguage peopleLanguage);
	}
}
