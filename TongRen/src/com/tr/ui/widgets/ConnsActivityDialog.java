package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.List;
import com.tr.R;
import com.tr.model.model.PeopleSelectTag;
import com.utils.common.EUtil;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.tr.ui.widgets.time.AbstractWheelTextAdapter;
import com.tr.ui.widgets.time.WheelView;

public class ConnsActivityDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private TextView cancelTv; // 取消
	private TextView okTv; // 确定
	private ImageView sendIv; // 确定添加
	private EditText customEt; // 自定义标签
	private TextView customTv;
	private WheelView activityWv;
	
	// 变量
	private Activity mContext;
	private OnFinishListener mListener;
	private List<PeopleSelectTag> mListActivity;
	private ActivityAdapter mAdapter;
	private int mCurSelection = 0;
	
	public ConnsActivityDialog(Activity context, PeopleSelectTag activity, OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_activity_dialog);
		mContext = context;
		mListener = listener;
		mListActivity = getDefaultListActivity();
		if(activity != null){
			boolean exist = false;
			for(int i = 0; i < mListActivity.size(); i++){
				if(activity.name.equalsIgnoreCase(mListActivity.get(i).name)){
					exist = true;
					mCurSelection = i;
					break;
				}
			}
			if(!exist){
				mListActivity.add(activity);
				mCurSelection = mListActivity.size() - 1;
			}
		}
		initDialogStyle();
		initControls();
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
		// 自定义标签
		customEt = (EditText) findViewById(R.id.customEt);
		customTv = (TextView) findViewById(R.id.customTv);
		customTv.setOnClickListener(mClickListener);
		// 确定添加
		sendIv = (ImageView) findViewById(R.id.sendIv);
		sendIv.setOnClickListener(mClickListener);
		// 滚轮
		activityWv = (WheelView) findViewById(R.id.activityWv);
		activityWv.setVisibleItems(3);
		mAdapter = new ActivityAdapter(mContext, mListActivity);
		activityWv.setViewAdapter(mAdapter);
		activityWv.setCurrentItem(mCurSelection);
	}
	
	private List<PeopleSelectTag> getDefaultListActivity(){
		List<PeopleSelectTag> listActivity = new ArrayList<PeopleSelectTag>();
		String [] activities = mContext.getResources().getStringArray(R.array.conns_activities);
		for(int i = 0;i < activities.length; i++){
			listActivity.add(new PeopleSelectTag(PeopleSelectTag.type_default,(i + 1) + "", activities[i]));
		}
		return listActivity;
	}

	private View.OnClickListener mClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				ConnsActivityDialog.this.dismiss();
				break;
			case R.id.okTv:  // 完成
			case R.id.sendIv:
				if(mListener != null){
					mListener.onFinish(mListActivity.get(activityWv.getCurrentItem()));
				}
				ConnsActivityDialog.this.dismiss();
				break;
			case R.id.customTv: // 自定义标签
				String activity = customEt.getText().toString();
				if(TextUtils.isEmpty(activity)){
					EUtil.showToast(mContext, "活动名称不能为空");
				}
				else if(activity.length() > 5){
					EUtil.showToast(mContext, "活动名称不能超过5个字符");
				}
				else{
					PeopleSelectTag peopleSelectTag=new PeopleSelectTag(PeopleSelectTag.type_custom,"", activity);
					mListActivity.add(peopleSelectTag);
					mAdapter.update(mListActivity);
					activityWv.setCurrentItem(mAdapter.getItemsCount() - 1);
					customEt.setText("");
				}
				break;
			}
		}
	};
	
	private class ActivityAdapter extends AbstractWheelTextAdapter{

		private List<PeopleSelectTag> listActivity = new ArrayList<PeopleSelectTag>();
		
		protected ActivityAdapter(Context context,List<PeopleSelectTag> listActivity) {
			super(context, R.layout.widget_wheel_item, R.id.itemTv);
			this.listActivity = listActivity;
		}
		
		public void update(List<PeopleSelectTag> listActivity){
			this.listActivity = listActivity;
			this.notifyDataChangedEvent();
			this.notifyDataInvalidatedEvent();
		}

		@Override
		public int getItemsCount() {
			return listActivity.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return listActivity.get(index).name;
		}
	}
	
	
	public interface OnFinishListener{
		public void onFinish(PeopleSelectTag activity);
	}

}
