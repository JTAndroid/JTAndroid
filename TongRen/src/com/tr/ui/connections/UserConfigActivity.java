package com.tr.ui.connections;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.model.UserConfig;
import com.tr.ui.base.ActivityHolder;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.http.IBindData;

public class UserConfigActivity extends JBaseFragmentActivity {

	public static final String TAG = "UserConfigActivity";
	
	private Activity mContext;

	// private ArrayList<UserConfig> userFeedList;
	private UserConfig userConfig;
	private ListView userConfingLv;

	private UserConfigLvAdapter userConfigLvAdapter;

	private ArrayList<Config> configList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.tr.R.layout.activity_user_config);
		ActivityHolder.getInstance().push(this);
		mContext = this;
		userConfingLv = (ListView) findViewById(R.id.userConfingLv);

		JSONObject json;
		try {
			json = new JSONObject();
			json.put("", "");
			ConnectionsReqUtil.getVisible(this, getConfigIb, json, null);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				submit();

			}
		});

	}

	IBindData getConfigIb = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			String MSG = "bindData()";
			if (object == null) {
				Log.i(TAG, MSG + " object == null");
			}
			if (object != null) {
				//0对自己可见1 对好友可见2所有人可见
				userConfig = (UserConfig) object;

				configList = new ArrayList<UserConfigActivity.Config>();
				if (userConfig.personalInfoVisible == null) {
					userConfig.personalInfoVisible= 2;
				}
				if (userConfig.zjxqVisible == null) {
					userConfig.zjxqVisible = 2;
				}
				if (userConfig.socialaActVisible == null) {
					userConfig.socialaActVisible = 2;
				}
				if (userConfig.mettingVisible == null) {
					userConfig.mettingVisible = 2;
				}
				configList.add(new Config("基本信息", userConfig.baseVisible));
				configList.add(new Config("联系方式", userConfig.contactVisible));
				configList.add(new Config("个人情况", userConfig.personalInfoVisible));
				configList.add(new Config("投资需求", userConfig.xqVisible));
				configList.add(new Config("融资需求", userConfig.zyVisible));
				configList.add(new Config("专家需求", userConfig.zjxqVisible));
				// configList.add(new Config("专家身份", userConfig.get));
				configList.add(new Config("教育经历", userConfig.educationVisible));
				configList.add(new Config("工作经历", userConfig.jobVisible));
				configList.add(new Config("社会活动", userConfig.socialaActVisible));
				configList.add(new Config("会面情况", userConfig.mettingVisible));
				// configList.add(new Config("附件", value));

				userConfigLvAdapter = new UserConfigLvAdapter(mContext, configList);
				userConfingLv.setAdapter(userConfigLvAdapter);
				
				userConfingLv.setOnItemLongClickListener(onItemLongClickListener);

			}

		}
	};

	IBindData setConfigIb = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			String MSG = "bindData()";
			if (object == null) {
				Log.i(TAG, MSG + " object == null");
			}
			if(object!=null){
				String sur=(String)object;
				if(sur.equals("true")){
					
					showToast("提交成功");
					
//					finish();
					return;
				}
			}
			showToast("提交失败");
//			finish();

		}
	};

	@Override
	public void initJabActionBar() {

	}

	private void submit() {
		String configString;
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.serializeNulls().create();

		HashMap<String, UserConfig> hash = new HashMap<String, UserConfig>();
		hash.put("userConfig", userConfig);
		configString = gson.toJson(hash);
		
		ConnectionsReqUtil.setVisible(UserConfigActivity.this, setConfigIb, configString, null);
	}

	class UserConfigLvAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<Config> mConfigList;

		public UserConfigLvAdapter(Context mContext, ArrayList<Config> mConfigList) {
			super();
			this.mContext = mContext;
			this.mConfigList = mConfigList;
		}

		@Override
		public int getCount() {
			return mConfigList.size();
		}

		@Override
		public Config getItem(int position) {
			return mConfigList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_keyvalue3, null);
				holder.key = (TextView) convertView.findViewById(R.id.keyTv);
				holder.value = (TextView) convertView.findViewById(R.id.valueTv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Config config = mConfigList.get(position);
			holder.key.setText(config.getKey());

			if (config.getValue() != null) {
				holder.value.setText(config.getValue2Str());
			}

			return convertView;
		}

		class ViewHolder {
			TextView key;
			TextView value;
		}

	}

	class Config {

		String key;
		Integer value;

		public Config(String key, Integer value) {
			super();
			this.key = key;
			this.value = value;
		}
		
		// 0对自己可见1 对好友可见2所有人可见
		public String getValue2Str() {
			String str = null;

			switch (value) {
			case 0:
				str = "对自己可见";
				break;
			case 1:
				str = "对好友可见";
				break;
			case 2:
				str = "所有人可见";
				break;

			default:
				break;
			}

			return str;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

	}

	OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, final int position1, long id) {
			final UserConfigLvAdapter adapter = (UserConfigLvAdapter) parent.getAdapter();
			final Config config = adapter.getItem(position1);
			final PopupWindow popupWindow = new PopupWindow();
			TextView tv = (TextView) view.findViewById(R.id.valueTv);
			ImageView deleteIv = (ImageView) view.findViewById(R.id.deleteIv);
			
			OnItemClickListener onItemClickListener = new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
					String MSG = "onItemClick()--";
					Log.i(TAG, MSG + position2);
					
					config.setValue(position2);
					
					if("基本信息".equals(config.getKey())){
						
						userConfig.baseVisible = position2;
					}
					else if ("联系方式".equals(config.getKey())){
						
						userConfig.contactVisible = position2;
					}
					else if ("个人情况".equals(config.getKey())){
						
						userConfig.personalInfoVisible = position2;
					}
					else if ("投资需求".equals(config.getKey())){
						userConfig.xqVisible = position2;
						
					}
					else if ("融资需求".equals(config.getKey())){
						userConfig.zyVisible = position2;
						
					}
					else if ("专家需求".equals(config.getKey())){
						
						userConfig.zjxqVisible = position2;
					}
					else if ("教育经历".equals(config.getKey())){
						userConfig.educationVisible = position2;
					}
					else if ("工作经历".equals(config.getKey())){
						
						userConfig.jobVisible = position2;
					}
					else if ("社会活动".equals(config.getKey())){
						
						userConfig.socialaActVisible = position2;
					}
					else if ("会面情况".equals(config.getKey())){
						
						userConfig.mettingVisible = position2;
					}
					
//					int base = userConfig.baseVisible;
//					Log.i(TAG, MSG + " base = " + base);
					
					adapter.notifyDataSetChanged();
					popupWindow.dismiss();
					
					submit();
					
				}
			}; 
			
			
			popupWindow.setWidth( LayoutParams.WRAP_CONTENT);
			popupWindow.setHeight( LayoutParams.WRAP_CONTENT);
			
			View popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_list, null);
			
			ListView popupLv =  (ListView) popupView.findViewById(R.id.popupLv);
			String[] memulist = {"对自己可见","对好友可见" , "所有人可见"};
			
			popupLv.setAdapter(new PopupAdaper(mContext, memulist));
			popupLv.setOnItemClickListener(onItemClickListener);
			
			
			popupWindow.setContentView(popupView);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			//设置点击窗口外边窗口消失
			popupWindow.setOutsideTouchable(true);
			popupWindow.setFocusable(true);
//			popupWindow.showAsDropDown(tv, 0, 0, Gravity.RIGHT);
			popupWindow.showAsDropDown(tv);
//			popupWindow.showAtLocation(view, Gravity.RIGHT, 0, 0);
			
			adapter.notifyDataSetChanged();
			

			return false;
		}
		
		
	};
	
	
	
	class PopupAdaper extends BaseAdapter {
		
		private Context mContext;
		private String[] mList ;
		
		public PopupAdaper(Context mContext, String[] mList) {
			super();
			this.mContext = mContext;
			this.mList = mList;
		}

		@Override
		public int getCount() {
			return mList.length;
		}

		@Override
		public String getItem(int position) {
			return mList[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.popup_list_item_value, null);
			TextView valueTv = (TextView) convertView.findViewById(R.id.valueTv);
			valueTv.setText(mList[position]);
			
			return convertView;
		}
		
	}
	
	

}
