package com.tr.ui.tongren.home.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.home.OrganizationRecordActivity;
import com.tr.ui.tongren.model.record.RecordDetail;
import com.tr.ui.tongren.model.record.RecordRule;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.TimeUtil;

public class OrganizationRecordFragment extends JBaseFragment implements
		IBindData, OnClickListener {

	private Context mContext;
	private TextView headerVi, weekTv, dateTv;
	private TextView currentEndTimeTv,  currentEndTimeSecondTv, endRecordTv, endRecordTimeTv, addressEndTitleTv, addressEndDetailTv;
	private TextView currentBeginTimeTv, currentBeginTimeSecondTv, beginRecordTv, beginRecordTimeTv, addressBeginTitleTv, addressBeginDetailTv;
	private TextView beginTimeTv, endTimeTv;
	private Button addBegin, addEnd;
	private LinearLayout addressBegin_ll, addressEnd_ll, currentBT_ll, currentET_ll, beginRecord_ll, endRecord_ll;
	private String oid;
	private TitlePopup titlePopup;
	
	// 定位  
    LocationClient mLocationClient = null;  
    MyBDLocationListner mListner = null;  
    GeoCoder mSearch = null;
    
    private String ADDBEGIN = "签到";
    private String ADDEND = "签退";
    private String click_btn;
    private PoiInfo poiInfo;
    
    private RecordDetail recordDetail;
    private Menu mMenu;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		oid = getArguments().getString("oid");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tongren_org_record_activity, null);
		initView(view);
		initData();
		initMap();
		return view;
	}

	private void initView(View view) {
		headerVi = (TextView) view.findViewById(R.id.text_transparent_line);
		weekTv = (TextView) view.findViewById(R.id.week);
		dateTv = (TextView) view.findViewById(R.id.date);
		currentBeginTimeTv = (TextView) view.findViewById(R.id.currentBeginTime);
		currentEndTimeTv = (TextView) view.findViewById(R.id.currentEndTime);
		currentBeginTimeSecondTv = (TextView) view.findViewById(R.id.currentBeginTime_second);
		currentEndTimeSecondTv = (TextView) view.findViewById(R.id.currentEndTime_second);
		beginRecordTv = (TextView) view.findViewById(R.id.beginRecordTv);
		beginRecordTimeTv = (TextView) view.findViewById(R.id.beginRecordTimeTv);
		endRecordTv = (TextView) view.findViewById(R.id.endRecordTv);
		endRecordTimeTv = (TextView) view.findViewById(R.id.endRecordTimeTv);
		addressBeginTitleTv = (TextView) view.findViewById(R.id.addressBeginTitleTv);
		addressBeginDetailTv = (TextView) view.findViewById(R.id.addressBeginDetailTv);
		addressEndTitleTv = (TextView) view.findViewById(R.id.addressEndTitleTv);
		addressEndDetailTv = (TextView) view.findViewById(R.id.addressEndDetailTv);
		
		beginTimeTv = (TextView) view.findViewById(R.id.beginTimeTv);
		endTimeTv = (TextView) view.findViewById(R.id.endTimeTv);
		
		addBegin = (Button) view.findViewById(R.id.addBegin);
		addEnd = (Button) view.findViewById(R.id.addEnd);
		
		addressBegin_ll = (LinearLayout) view.findViewById(R.id.addressBegin_ll);
		addressEnd_ll = (LinearLayout) view.findViewById(R.id.addressEnd_ll);
		
		currentBT_ll = (LinearLayout) view.findViewById(R.id.currentBT_ll);
		currentET_ll = (LinearLayout) view.findViewById(R.id.currentET_ll);
		
		beginRecord_ll = (LinearLayout) view.findViewById(R.id.beginRecord_ll);
		endRecord_ll = (LinearLayout) view.findViewById(R.id.endRecord_ll);
		
		addBegin.setOnClickListener(this);
		addEnd.setOnClickListener(this);
		
		addBegin.setBackgroundResource(R.drawable.addbegin);
		addEnd.setBackgroundResource(R.drawable.addend);
		addBegin.setClickable(false);
		addEnd.setClickable(false);
	}

	private void initData() {
		weekTv.setText(TimeUtil.getWeek());
		dateTv.setText(TimeUtil.getDate(new Date(), TimeUtil.SDF_DATE_CHI));
		
		showLoadingDialog();
		getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETRULE);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		HomeCommonUtils.initLeftCustomActionBar(getActivity(), getActivity().getActionBar(), "我的考勤", false, null, true, true);
		if(mMenu!=null){
			mMenu.findItem(R.id.more).setIcon(R.drawable.affairs_calendar_top);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		this.mMenu = menu;
		mMenu.findItem(R.id.more).setIcon(R.drawable.affairs_calendar_top);
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case EAPIConsts.handler.show_err:
				break;
			}
		}
	};
	
	private void initMap(){
		// 定位  
        mLocationClient = new LocationClient(mContext);  
        mListner = new MyBDLocationListner();  
        mLocationClient.registerLocationListener(mListner);  
        LocationClientOption option = new LocationClientOption();  
        option.setIsNeedAddress(true);
        option.setOpenGps(true);// 打开gps  
        option.setCoorType("bd09ll"); // 设置坐标类型  
        option.setScanSpan(1000);  
        mLocationClient.setLocOption(option); 
        
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(listener);
	}
	
	// 定位监听器  
    private class MyBDLocationListner implements BDLocationListener {  
  
        @Override  
        public void onReceiveLocation(BDLocation location) {  
            if (location == null)  
                return; 
            LatLng mLoactionLatLng = new LatLng(location.getLatitude(), location.getLongitude());  
            // 是否第一次定位  
        	mSearch.reverseGeoCode((new ReverseGeoCodeOption())  
                    .location(mLoactionLatLng));  
			mLocationClient.stop();
        }  
    } 
	
	OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {  
	    public void onGetGeoCodeResult(GeoCodeResult result) {  
	        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	            //没有检索到结果  
	        }  
	        //获取地理编码结果  
	    }  
	 
	    @Override  
	    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {  
	        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
	            //没有找到检索结果  
	        }else{
	        	if (result.getPoiList() != null) {
	        		poiInfo = result.getPoiList().get(0);
	            	addressBegin_ll.setVisibility(View.VISIBLE);
	            	addressBeginTitleTv.setText(poiInfo.name);
	        		addressBeginDetailTv.setText(poiInfo.address);
	            	
    				if(click_btn.equals(ADDBEGIN)){
    					getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDBEGIN);
    				}else if(click_btn.equals(ADDEND)){
    					getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDEND);
    				}
	        	}
	        }
	        //获取反向地理编码结果  
	    }  
	};
	
	Runnable timeBeginRb = new Runnable() {
		@Override
		public void run() {
			handler.postDelayed(this, 1000);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			SimpleDateFormat sdf2 = new SimpleDateFormat("ss");
			Date date = new Date();
			String str = sdf.format(date);
			String str2 = sdf2.format(date);
			currentBT_ll.setVisibility(View.VISIBLE);
			currentBeginTimeTv.setText(str);
			currentBeginTimeSecondTv.setText(str2);
		}
	};
	Runnable timeEndRb = new Runnable() {
		@Override
		public void run() {
			handler.postDelayed(this, 1000);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			SimpleDateFormat sdf2 = new SimpleDateFormat("ss");
			Date date = new Date();
			String str = sdf.format(date);
			String str2 = sdf2.format(date);
			currentET_ll.setVisibility(View.VISIBLE);
			currentEndTimeTv.setText(str);
			currentEndTimeSecondTv.setText(str2);
		}
	};

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.addBegin:
			if(isOPenGps(mContext)){
				click_btn = ADDBEGIN;
				mLocationClient.start();
				showLoadingDialog();
			}else{
				MessageDialog msgdialog = new MessageDialog(getActivity());
				msgdialog.setTitle("");
				msgdialog.setContent("请在设置里打开，定位服务");
				msgdialog.goneCancleButton();
				msgdialog.setOnDialogFinishListener(new OnDialogFinishListener() {
					
					@Override
					public void onFinish(String content) {
						
					}
					
					@Override
					public void onCancel(String content) {
						
					}
				});
				msgdialog.show();
			}
			break;
		case R.id.addEnd:
			if(isOPenGps(mContext)){
				click_btn = ADDEND;
				mLocationClient.start();
				showLoadingDialog();
			}else{
				MessageDialog msgdialog = new MessageDialog(getActivity());
				msgdialog.setTitle("");
				msgdialog.setContent("请在设置里打开，定位服务");
				msgdialog.goneCancleButton();
				msgdialog.setOnDialogFinishListener(new OnDialogFinishListener() {
					
					@Override
					public void onFinish(String content) {
						
					}
					
					@Override
					public void onCancel(String content) {
						
					}
				});
				msgdialog.show();
			}
			break;
		}
	}

	@Override
	public void bindData(int tag, final Object object) {
		dismissLoadingDialog();
		switch(tag){
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDBEGIN:
			if(object == null){
				return;
			}
			recordDetail = (RecordDetail) object;
			handler.removeCallbacks(timeBeginRb);
			addBegin.setVisibility(View.GONE);
			beginRecord_ll.setVisibility(View.VISIBLE);
			beginRecordTv.setText("已签到 ");
			beginRecordTimeTv.setText(TimeUtil.getDate(recordDetail.getStartWorkTime(), "HH:mm"));
			currentBT_ll.setVisibility(View.GONE);
			addressBegin_ll.setVisibility(View.VISIBLE);
			
			//签到成功后，签退按钮可以点击
			handler.post(timeEndRb);
			addEnd.setVisibility(View.VISIBLE);
			addEnd.setClickable(true);
			addEnd.setBackgroundResource(R.drawable.addend);
			addressEnd_ll.setVisibility(View.GONE);
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDEND:
			if(object == null){
				return;
			}
			recordDetail = (RecordDetail) object;
			handler.removeCallbacks(timeEndRb);
			addEnd.setVisibility(View.GONE);
			endRecord_ll.setVisibility(View.VISIBLE);
			endRecordTv.setText("已签退 ");
			endRecordTimeTv.setText(TimeUtil.getDate(recordDetail.getWorkTimeOut(), "HH:mm"));
			currentET_ll.setVisibility(View.GONE);
			addressEnd_ll.setVisibility(View.VISIBLE);
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETATTENDANCERECORDSOFDATE:
			if(object == null){//尚未打过卡
				handler.post(timeBeginRb);
				addBegin.setVisibility(View.VISIBLE);
				addBegin.setClickable(true);
				addBegin.setBackgroundResource(R.drawable.addbegin);
				addressBegin_ll.setVisibility(View.GONE);
				return;
			}
			recordDetail = (RecordDetail) object;
			if(!TextUtils.isEmpty(recordDetail.getStartWorkTime())){
				addBegin.setVisibility(View.GONE);
				beginRecord_ll.setVisibility(View.VISIBLE);
				beginRecordTv.setText("已签到 ");
				beginRecordTimeTv.setText(TimeUtil.getDate(recordDetail.getStartWorkTime(), "HH:mm"));
				addressBegin_ll.setVisibility(View.VISIBLE);
				if(recordDetail.getLonlatStart().split(",").length>1){
	            	addressBeginTitleTv.setText(recordDetail.getLonlatStart().split(",")[0]);
	        		addressBeginDetailTv.setText(recordDetail.getLonlatStart().split(",")[1]);
				}
			}else{
				handler.post(timeBeginRb);
				addBegin.setVisibility(View.VISIBLE);
				addBegin.setClickable(true);
				addBegin.setBackgroundResource(R.drawable.addbegin);
				addressBegin_ll.setVisibility(View.GONE);
			}
			if(!TextUtils.isEmpty(recordDetail.getWorkTimeOut())){
				addEnd.setVisibility(View.GONE);
				endRecord_ll.setVisibility(View.VISIBLE);
				endRecordTv.setText("已签退 ");
				endRecordTimeTv.setText(TimeUtil.getDate(recordDetail.getWorkTimeOut(), "HH:mm"));
				addressEnd_ll.setVisibility(View.VISIBLE);
				if(recordDetail.getLonlatEnd().split(",").length>1){
	            	addressEndTitleTv.setText(recordDetail.getLonlatEnd().split(",")[0]);
	        		addressEndDetailTv.setText(recordDetail.getLonlatEnd().split(",")[1]);
				}
			}else{
				if(!TextUtils.isEmpty(recordDetail.getStartWorkTime())){//签到后才能签退
					handler.post(timeEndRb);
					addEnd.setVisibility(View.VISIBLE);
					addEnd.setClickable(true);
					addEnd.setBackgroundResource(R.drawable.addend);
					addressEnd_ll.setVisibility(View.GONE);
				}
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETRULE:
			if(object == null){
				showToast("尚未设置考勤规则，不能签到");
				return;
			}
			RecordRule rule = (RecordRule) object;
			beginTimeTv.setText(rule.getStartWorkTime());
			endTimeTv.setText(rule.getWorkTimeOut());
			//获取打卡信息
			getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETATTENDANCERECORDSOFDATE);
			break;
		}
		dismissLoadingDialog();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mSearch.destroy();
	}
	
	private void getDataFromServer(int requestType) {
		showLoadingDialog();
		JSONObject jobj = new JSONObject();
		try {
			switch (requestType) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETRULE://考勤规则
				jobj.put("organizationId", oid);
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETATTENDANCERECORDSOFDATE://打卡信息
				jobj.put("organizationId", oid);
				jobj.put("date", TimeUtil.getDate(new Date(), TimeUtil.SDF_DATE_TIME));
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDBEGIN://签到
				jobj.put("organizationId", oid);
				jobj.put("lonlatStart", poiInfo.name+","+poiInfo.address);
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_ADDEND://签退
				jobj.put("organizationId", oid);
				jobj.put("lonlatEnd", poiInfo.name+","+poiInfo.address);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		TongRenReqUtils.doRequestOrg(mContext, this, jobj, handler, requestType);
	}
	
	public boolean isOPenGps(Context context) {  
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);  
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）  
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);  
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）  
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);  
        
      //获取系统服务
        ConnectivityManager manager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取状态
        State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (gps || network || wifi == State.CONNECTED||wifi==State.CONNECTING) {  
            return true;  
        }  
        return false;  
    }
}
