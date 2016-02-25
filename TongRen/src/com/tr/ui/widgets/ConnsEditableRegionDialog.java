package com.tr.ui.widgets;

import java.util.ArrayList;
import java.util.List;
import com.tr.R;
import com.tr.db.RegionDBManager;
import com.tr.model.model.PeopleAddress;
import com.tr.model.obj.JTRegion;
import com.tr.ui.widgets.time.AbstractWheelTextAdapter;
import com.tr.ui.widgets.time.OnWheelChangedListener;
import com.tr.ui.widgets.time.WheelView;
import com.tr.ui.widgets.time.WheelViewAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 人脉对话框(籍贯)
 * @author leon
 *
 */
public class ConnsEditableRegionDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private TextView cancelTv; // 取消
	private TextView okTv; // 确定
	private WheelView provinceWv; // 省
	private WheelView cityWv; // 市
	private WheelView countyWv; // 县
	private EditText detailEt;// 详细地址
	
	// 变量
	private Activity mContext;
	private RegionAdapter mProvinceAdapter;
	private RegionAdapter mCityAdapter;
	private RegionAdapter mCountyAdapter;
	private List<JTRegion> mListProvince;
	private List<JTRegion> mListCity;
	private List<JTRegion> mListCounty;
	private OnFinishListener mListener;
	private RegionDBManager mDBManager;
	private PeopleAddress mPeopleAddress;
	
	public ConnsEditableRegionDialog(Activity context,PeopleAddress peopleAddress, OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_editable_region_dialog);
		mContext = context;
		mListener = listener;
		mPeopleAddress = peopleAddress;
		initDialogStyle();
		initVars();
		initControls();
	}
	
	// 初始化对话框样式
	private void initDialogStyle(){
		getWindow().setWindowAnimations(R.style.ConnsDialogAnim);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = mContext.getWindowManager().getDefaultDisplay().getWidth();
		wmlp.gravity = Gravity.BOTTOM ;
	}
	
	// 初始化变量
	private void initVars(){
		
		mDBManager = new RegionDBManager(mContext);
		// 省
		mListProvince = new ArrayList<JTRegion>();
		mListProvince.add(new JTRegion("省"));
		mListProvince.addAll(mDBManager.query(mDBManager.query("0").get(0).getId() + ""));
		mProvinceAdapter = new RegionAdapter(mContext,mListProvince);
		// mProvinceAdapter.update(mListProvince);
		// 市
		mListCity = new ArrayList<JTRegion>();
		mListCity.add(new JTRegion("市"));
		mCityAdapter = new RegionAdapter(mContext,mListCity);
		// 县
		mListCounty = new ArrayList<JTRegion>();
		mListCounty.add(new JTRegion("县"));
		mCountyAdapter = new RegionAdapter(mContext,mListCounty);
	}
	
	// 初始化控件
	private void initControls(){
		// 取消
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(mClickListener);
		// 确定
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(mClickListener);
		// 详细地址
		detailEt = (EditText) findViewById(R.id.detailEt);
		// 省
		provinceWv = (WheelView) findViewById(R.id.provinceWv);
		provinceWv.setVisibleItems(3);
		provinceWv.setViewAdapter(mProvinceAdapter);
		// 市
		cityWv = (WheelView) findViewById(R.id.cityWv);
		cityWv.setVisibleItems(3);
		cityWv.setViewAdapter(mCityAdapter);
		// 县
		countyWv = (WheelView) findViewById(R.id.countyWv);
		countyWv.setVisibleItems(3);
		countyWv.setViewAdapter(mCountyAdapter);
		// 省
		provinceWv.addChangingListener(new OnWheelChangedListener(){

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(newValue == 0){
					// 重置市
					mListCity.clear();
					mListCity.add(new JTRegion("市"));
					mCityAdapter.update(mListCity);
					cityWv.setCurrentItem(0);
					// 重置县
					mListCounty.clear();
					mListCounty.add(new JTRegion("县"));
					mCountyAdapter.update(mListCounty);
					countyWv.setCurrentItem(0);
				}
				else{
					mListCity = mDBManager.query(mListProvince.get(newValue).getId() + "");
					mListCity.add(0, new JTRegion("市"));
					mCityAdapter.update(mListCity);
					cityWv.setCurrentItem(0);
				}
			}
		});
		cityWv.addChangingListener(new OnWheelChangedListener(){

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(newValue == 0){
					// 重置县
					mListCounty.clear();
					mListCounty.add(new JTRegion("县"));
					mCountyAdapter.update(mListCounty);
					countyWv.setCurrentItem(0);
				}
				else{
					mListCounty = mDBManager.query(mListCity.get(newValue).getId() + "");
					mListCounty.add(0, new JTRegion("县"));
					mCountyAdapter.update(mListCounty);
					countyWv.setCurrentItem(0);
				}
			}
		});
		
		if (mPeopleAddress != null) {
			
			if (mPeopleAddress.stateName != null
					&& mPeopleAddress.stateName.length() > 0) {

				for (int i = 0; i < mListProvince.size(); i++) {
					if (mPeopleAddress.stateName.equals(mListProvince.get(i).getCname())) {
						provinceWv.setCurrentItem(i);
						break;
					}
				}
				
				if(mPeopleAddress.cityName != null
						&& mPeopleAddress.cityName.length() > 0
						&& provinceWv.getCurrentItem() > 0){
					
					mListCity.addAll(mDBManager.query(mListProvince.get(provinceWv.getCurrentItem()).getId() + ""));
					
					for (int i = 0; i < mListCity.size(); i++) {
						if (mPeopleAddress.cityName.equals(mListCity.get(i).getCname())) {
							cityWv.setCurrentItem(i);
							break;
						}
					}
					
					if(mPeopleAddress.countyName != null
							&& mPeopleAddress.countyName.length() > 0
							&& cityWv.getCurrentItem() > 0){
						
						mListCounty.addAll(mDBManager.query(mListCity.get(cityWv.getCurrentItem()).getId() + ""));
						
						for (int i = 0; i < mListCounty.size(); i++) {
							if (mPeopleAddress.countyName.equals(mListCounty.get(i).getCname())) {
								countyWv.setCurrentItem(i);
								break;
							}
						}
					}
				}
			}
			// 详细地址
			if(mPeopleAddress.address != null && mPeopleAddress.address.length() > 0){
				detailEt.setText(mPeopleAddress.address);
			}
		}
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				ConnsEditableRegionDialog.this.dismiss();
				break;
			case R.id.okTv: // 确定
				if (mPeopleAddress == null) {
					mPeopleAddress = new PeopleAddress();
				}
				// 省
				if (provinceWv.getCurrentItem() > 0) {
					mPeopleAddress.stateName =(mListProvince.get(provinceWv.getCurrentItem()).getCname()); 
				}
				// 市
				if (cityWv.getCurrentItem() > 0) {
					mPeopleAddress.stateName= (mListCity.get(cityWv.getCurrentItem()).getCname()); 
				}
				// 县
				if (countyWv.getCurrentItem() > 0) {
					mPeopleAddress.countyName =(mListCounty.get(countyWv.getCurrentItem()).getCname()); 
				}
				// 详细地址
				mPeopleAddress.address =(detailEt.getText().toString());
				ConnsEditableRegionDialog.this.dismiss();
				if(mListener != null){
					mListener.onFinish(mPeopleAddress);
				}
				break;
			}
		}
	};
	
	private class RegionAdapter extends AbstractWheelTextAdapter {
        
		private List<JTRegion> mListRegion = new ArrayList<JTRegion>();
		/*
		private int mType; // 0-省,1-市,2-县
		private JTRegion mDefault = new JTRegion();
		*/
		
		/*
        protected RegionAdapter(Context context,int type) {
            super(context, R.layout.widget_wheel_item, R.id.itemTv);
            mType = type;
            switch(mType){
            case 0:
            	mDefault.setCname("省");
            	break;
            case 1:
            	mDefault.setCname("市");
            	break;
            case 2:
            	mDefault.setCname("县");
            	break;
            }
        }
        */
		
		public RegionAdapter(Context context,List<JTRegion> listRegion){
			super(context, R.layout.widget_wheel_item, R.id.itemTv);
			mListRegion = listRegion;
		}
        
		/*
        public void reset(){
        	mListRegion.clear();
        	if(mType > 0){
        		mListRegion.add(mDefault);
        	}
        	notifyDataChangedEvent();     
        	notifyDataInvalidatedEvent();
        }
        */
        
        public void update(List<JTRegion> listRegion){
        	
        	if(listRegion != null){
        		mListRegion = listRegion;
        	}
        	notifyDataChangedEvent();     
        	notifyDataInvalidatedEvent();
        }
        
        @Override
        public int getItemsCount() {
            return mListRegion.size();
        }
        
        @Override
        protected CharSequence getItemText(int position) {
            return mListRegion.get(position).getCname();
        }
    }
	
	
	public interface OnFinishListener{
		public void onFinish(PeopleAddress peopleAddress);
	}

}
