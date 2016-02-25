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
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 人脉对话框(投资需求，融资需求，专家需求，专家身份)
 * @author leon
 *
 */
public class ConnsRegionDialog extends Dialog {

	private final String TAG = getClass().getSimpleName();
	
	//// 控件
	private TextView cancelTv; // 取消
	private TextView okTv; // 确定
	private WheelView abroadWv; // 国内外
	private WheelView continentWv; // 洲
	private WheelView countryWv; // 国
	private WheelView provinceWv; // 省
	private WheelView cityWv; // 市
	private WheelView countyWv; // 县
	
	// 变量
	private Activity mContext;
	private RegionAdapter mAbroadAdapter;
	private RegionAdapter mContinentAdapter;
	private RegionAdapter mCountryAdapter;
	private RegionAdapter mProvinceAdapter;
	private RegionAdapter mCityAdapter;
	private RegionAdapter mCountyAdapter;
	private List<JTRegion> mListAbroad;
	private List<JTRegion> mListContinent;
	private List<JTRegion> mListCountry;
	private List<JTRegion> mListProvince;
	private List<JTRegion> mListCity;
	private List<JTRegion> mListCounty;
	private OnFinishListener mListener;
	private RegionDBManager mDBManager;
	private PeopleAddress mPeopleAddress;
	private View mView;
	
	public ConnsRegionDialog(Activity context,View view, PeopleAddress peopleAddress, OnFinishListener listener) {
		super(context,R.style.ConnsDialogTheme);
		setContentView(R.layout.widget_conns_region_dialog);
		mContext = context;
		mView = view;
		mListener = listener;
		mPeopleAddress = peopleAddress;
		initDialogStyle();
		initVars();
		initControls();
	}
	
	// 初始化对话框样式
	@SuppressWarnings("deprecation")
	private void initDialogStyle(){
		getWindow().setWindowAnimations(R.style.ConnsDialogAnim);
		WindowManager.LayoutParams wmlp = getWindow().getAttributes();
		wmlp.width = mContext.getWindowManager().getDefaultDisplay().getWidth();
		wmlp.gravity = Gravity.BOTTOM ;
	}
	
	// 初始化变量
	private void initVars(){
		
		mDBManager = new RegionDBManager(mContext);
		// 国内外
		mListAbroad = mDBManager.query("0");
		mAbroadAdapter = new RegionAdapter(mContext, 0);
		mAbroadAdapter.update(mListAbroad);
		// 洲
		mListContinent = new ArrayList<JTRegion>();
		mListContinent.addAll(mDBManager.query(mListAbroad.get(1).getId() + ""));
		mContinentAdapter = new RegionAdapter(mContext, 1);
		mContinentAdapter.update(mListContinent);
		// 国家
		mListCountry = new ArrayList<JTRegion>();
		mCountryAdapter = new RegionAdapter(mContext, 2);
		mCountryAdapter.update(mListCountry);
		// 省
		mListProvince = new ArrayList<JTRegion>();
		mListProvince.addAll(mDBManager.query(mListAbroad.get(0).getId() + ""));
		mProvinceAdapter = new RegionAdapter(mContext, 3);
		mProvinceAdapter.update(mListProvince);
		// 市
		mListCity = new ArrayList<JTRegion>();
		mCityAdapter = new RegionAdapter(mContext, 4);
		mCityAdapter.update(mListCity);
		// 县
		mListCounty = new ArrayList<JTRegion>();
		mCountyAdapter = new RegionAdapter(mContext, 5);
		mCountyAdapter.update(mListCounty);
	}
	
	// 初始化控件
	private void initControls(){
		// 取消
		cancelTv = (TextView) findViewById(R.id.cancelTv);
		cancelTv.setOnClickListener(mClickListener);
		// 确定
		okTv = (TextView) findViewById(R.id.okTv);
		okTv.setOnClickListener(mClickListener);
		// 国内外
		abroadWv = (WheelView) findViewById(R.id.abroadWv);
		abroadWv.setVisibleItems(3);
		abroadWv.setViewAdapter(mAbroadAdapter);
		// 洲
		continentWv = (WheelView) findViewById(R.id.continentWv);
		continentWv.setVisibleItems(3);
		continentWv.setViewAdapter(mContinentAdapter);
		// 国家
		countryWv = (WheelView) findViewById(R.id.countryWv);
		countryWv.setVisibleItems(3);
		countryWv.setViewAdapter(mCountryAdapter);
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
		
		// 添加
		abroadWv.addChangingListener(new OnWheelChangedListener(){

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(newValue == 0){ // 国内
					continentWv.setVisibility(View.GONE);
					countryWv.setVisibility(View.GONE);
					provinceWv.setVisibility(View.VISIBLE);
					cityWv.setVisibility(View.VISIBLE);
					countyWv.setVisibility(View.VISIBLE);
					// 设置省
					provinceWv.setCurrentItem(0);
				}
				else{ // 国外
					
					continentWv.setVisibility(View.VISIBLE);
					countryWv.setVisibility(View.VISIBLE);
					provinceWv.setVisibility(View.GONE); 
					cityWv.setVisibility(View.GONE); 
					countyWv.setVisibility(View.GONE); 
					// 设置洲
					continentWv.setCurrentItem(0);
				}
			}
		});
		continentWv.addChangingListener(new OnWheelChangedListener(){

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(newValue == 0){
					mCountryAdapter.reset();
					countryWv.setCurrentItem(0);
				}
				else{
					mListCountry = mDBManager.query(mListContinent.get(newValue).getId() + "");
					mCountryAdapter.update(mListCountry);
					countryWv.setCurrentItem(0);
				}
			}
		});
		/*
		countryWv.addChangingListener(new OnWheelChangedListener(){

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(newValue == 0){
					mProvinceAdapter.reset();
					provinceWv.setCurrentItem(0);
				}
				else{
					mListProvince = mDBManager.query(mListCountry.get(newValue - 1).getId() + "");
					mProvinceAdapter.update(mListProvince);
					provinceWv.setCurrentItem(0);
				}
			}
		});
		*/
		provinceWv.addChangingListener(new OnWheelChangedListener(){

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(newValue == 0){
					mCityAdapter.reset();
					cityWv.setCurrentItem(0);
				}
				else{
					mListCity = mDBManager.query(mListProvince.get(newValue).getId() + "");
					mCityAdapter.update(mListCity);
					cityWv.setCurrentItem(0);
				}
			}
		});
		cityWv.addChangingListener(new OnWheelChangedListener(){

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(newValue == 0){
					mCountyAdapter.reset();
					countyWv.setCurrentItem(0);
				}
				else{
					mListCounty = mDBManager.query(mListCity.get(newValue).getId() + "");
					mCountyAdapter.update(mListCounty);
					countyWv.setCurrentItem(0);
				}
			}
		});
		
		// 设置默认值
		if(mPeopleAddress != null){
			if(mPeopleAddress.areaType  == 0){ // 国内
				if(mPeopleAddress.stateName != null
						&& !TextUtils.isEmpty(mPeopleAddress.stateName)){
					// 有匹配省
					for(int i = 0; i < mListProvince.size(); i++){
						if(mListProvince.get(i).getCname().equals(mPeopleAddress.stateName)){
							provinceWv.setCurrentItem(i);
							break;
						}
					}
					if(provinceWv.getCurrentItem() > 0
							&& mPeopleAddress.cityName != null
							&& !TextUtils.isEmpty(mPeopleAddress.cityName)){
						// 有匹配市
						for (int j = 0; j < mListCity.size(); j++) {
							if(mListCity.get(j).getCname().equals(mPeopleAddress.cityName)){
								cityWv.setCurrentItem(j);
								break;
							}
						}
						if(cityWv.getCurrentItem() > 0
								&& mPeopleAddress.countyName != null
								&& !TextUtils.isEmpty(mPeopleAddress.countyName)){
							// 有匹配县
							for (int k = 0; k < mListCounty.size(); k++) {
								if (mListCounty.get(k).getCname().equals(mPeopleAddress.countyName)) {
									countyWv.setCurrentItem(k);
									break;
								}
							}
						}
					}
				}
			}
			else{ // 国外
				if(mPeopleAddress.stateName != null
						&& !TextUtils.isEmpty(mPeopleAddress.stateName)){
					// 有匹配洲
					for(int i = 0; i < mListContinent.size(); i++){
						if(mListContinent.get(i).getCname().equals(mPeopleAddress.stateName)){
							continentWv.setCurrentItem(i);
							break;
						}
					}
					if(continentWv.getCurrentItem() > 0
							&& mPeopleAddress.cityName != null
							&& !TextUtils.isEmpty(mPeopleAddress.cityName)){
						// 有匹配国家
						for (int j = 0; j < mListCountry.size(); j++) {
							if(mListCountry.get(j).getCname().equals(mPeopleAddress.cityName)){
								countryWv.setCurrentItem(j);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cancelTv: // 取消
				ConnsRegionDialog.this.dismiss();
				break;
			case R.id.okTv: // 确定
				if(mPeopleAddress == null){
					mPeopleAddress = new PeopleAddress();
				}
				mPeopleAddress.areaType = (abroadWv.getCurrentItem());
				if(mPeopleAddress.areaType == 0){ // 国内
					// 省
					if(provinceWv.getCurrentItem() > 0){
						mPeopleAddress.stateName = (mListProvince.get(provinceWv.getCurrentItem()).getCname()); 
					}
					else{
						mPeopleAddress.stateName = ("");
					}
					// 市
					if(cityWv.getCurrentItem() > 0){
						mPeopleAddress.cityName = (mListCity.get(cityWv.getCurrentItem()).getCname());
					}
					else{
						mPeopleAddress.cityName = ("");
					}
					// 县
					if(countyWv.getCurrentItem() > 0){
						mPeopleAddress.countyName = (mListCounty.get(countyWv.getCurrentItem()).getCname()); 
					}
					else{
						mPeopleAddress.countyName = ("");
						 
					}
				}
				else{ // 国外
					
					mPeopleAddress.countyName =("");
					
					// 洲
					if(continentWv.getCurrentItem() > 0){
						mPeopleAddress.stateName = (mListContinent.get(continentWv.getCurrentItem()).getCname()); 
					}
					else{
						mPeopleAddress.stateName = ("");
					}
					// 国
					if(countryWv.getCurrentItem() > 0){
						mPeopleAddress.cityName= (mListCountry.get(countryWv.getCurrentItem()).getCname());
					}
					else{
						mPeopleAddress.cityName = (""); 
					}
					
					
				}
				if(mListener != null){
					mListener.onFinish(mView, mPeopleAddress);
				}
				ConnsRegionDialog.this.dismiss();
				break;
			}
		}
	};
	
	private class RegionAdapter extends AbstractWheelTextAdapter {
        
		private List<JTRegion> mListRegion = new ArrayList<JTRegion>();
		private int mType; // 1-洲,2-国家,3-省,4-市,5-县
		private JTRegion mDefault = new JTRegion();
		
        protected RegionAdapter(Context context,int type) {
            super(context, R.layout.widget_wheel_item, R.id.itemTv);
            mType = type;
            switch(mType){
            case 1:
            	mDefault.setCname("洲");
            	break;
            case 2:
            	mDefault.setCname("国家");
            	break;
            case 3:
            	mDefault.setCname("省");
            	break;
            case 4:
            	mDefault.setCname("市");
            	break;
            case 5:
            	mDefault.setCname("县");
            	break;
            }
        }
        
        public void reset(){
        	mListRegion.clear();
        	if(mType > 0){
        		mListRegion.add(mDefault);
        	}
        	notifyDataChangedEvent();     
        	notifyDataInvalidatedEvent();
        }
        
        public void update(List<JTRegion> listRegion){
    
        	if(listRegion != null){
        		mListRegion = listRegion;
        		if(mType > 0){
        			mListRegion.add(0, mDefault);
        		}
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
		public void onFinish(View view, PeopleAddress peopleAddress);
	}

}
