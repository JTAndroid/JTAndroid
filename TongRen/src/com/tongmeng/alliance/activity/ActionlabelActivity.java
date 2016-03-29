package com.tongmeng.alliance.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tongmeng.alliance.view.MyCatalogDialog;
import com.tongmeng.alliance.view.MyCatalogDialog.OnDialogClickListener;
import com.tongmeng.alliance.view.MyCatalogDialog.OperType;
import com.tongmeng.alliance.view.MyTagGroupView;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.LableData;
import com.tr.ui.base.JBaseActivity;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;

public class ActionlabelActivity extends JBaseActivity {

	private MyTagGroupView selectedCv;// 已选择的标签
	private MyTagGroupView myLabelCv;// 我的标签
	private MyTagGroupView defaultLabelCv;// 金桐推荐标签

	List<ActionLabel> selectedList = new ArrayList<ActionlabelActivity.ActionLabel>(),
			myLabelList = new ArrayList<ActionlabelActivity.ActionLabel>(),
			defaultLabelList = new ArrayList<ActionlabelActivity.ActionLabel>();
	List<Boolean> sBList = new ArrayList<Boolean>(),
			mBList = new ArrayList<Boolean>(),
			dBList = new ArrayList<Boolean>();
	MyCatalogDialog dialog;
	ArrayList<LableData> defauleData = new ArrayList<LableData>();// 选中的对象
	int index;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 吐司提示结果
				Toast.makeText(ActionlabelActivity.this, msg.obj + "", 0)
						.show();
				break;
			case 1:// 添加标签成功
				Bundle bundle = msg.getData();
				ActionLabel label = new ActionLabel(bundle.getInt("id"),
						bundle.getString("name"));
				selectedList.add(label);
				sBList.add(true);
				initSelectedLayout();

				myLabelList.add(label);
				mBList.add(true);
				initMyTagLayout();
				break;
			case 2://“我的标签”获取数据成功，加载view
				initMyTagLayout();
				break;
			case 3://“金桐推荐标签”获取数据成功，加载view
				initGTTagLayout();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_label);
		selectedCv = (MyTagGroupView) findViewById(R.id.selectedCv);
		myLabelCv = (MyTagGroupView) findViewById(R.id.myLabelCv);
		defaultLabelCv = (MyTagGroupView) findViewById(R.id.defaultLabelCv);

		// 将“已选择”中的添加按钮当成一个对象，存入selectedList
		ActionLabel label = new ActionLabel(-2, "添加按钮");
		selectedList.add(label);

		initDialog();
		initSelectedLayout();
		initMyTagData();
//		initGTTagData();
		getIntentLabel();// 金铜推荐标签
	}

	private void getIntentLabel() {
		// TODO Auto-generated method stub
		DemandReqUtil.getTagList(this, new IBindData() {
			
			@Override
			public void bindData(int tag, Object object) {
				// TODO Auto-generated method stub
				ArrayList<LableData> recommend = (ArrayList<LableData>) object;
				for (LableData lableData : recommend) {
					KeelLog.e(TAG, "lableData::"+lableData.toString());
					ActionLabel label = new ActionLabel(lableData.id, lableData.tag);
					defaultLabelList.add(label);
					dBList.add(false);
					handler.sendEmptyMessage(3);
				}
			}
		}, null);
	}

	private void initMyTagData() {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				String param = "";
				KeelLog.e(TAG, "initMyTagData param::" + param);
				String result = HttpRequestUtil.sendPost(Constant.applyPath,
						param, ActionlabelActivity.this);
				KeelLog.e(TAG, "initMyTagData result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao != null) {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())
								&& !"null".equals(dao.getResponseData())) {
							myLabelList = getListItemData(dao.getResponseData(),0);
							if(myLabelList.size()>0){
								handler.sendEmptyMessage(2);
							}else{
								sendMessage(0, "您现在尚未有标签", null);
							}
						} else {
							sendMessage(0, "获取我的标签失败", null);
						}
					} else {
						sendMessage(0, "获取我的标签失败,失败原因：" + dao.getNotifyInfo(),
								null);
					}
				} else {
					sendMessage(0, "获取我的标签失败", null);
				}
			}			
		}.start();
	}

	private void initSelectedLayout() {
		selectedCv.removeAllViews();
		for (int i = selectedList.size() - 1; i >= 0; i--) {
			if (selectedList.get(i).getId() != -2) {
				final LinearLayout layout = (LinearLayout) LayoutInflater.from(
						ActionlabelActivity.this).inflate(
						R.layout.actionlabel_item, selectedCv, false);
				TextView tv = (TextView) layout
						.findViewById(R.id.actionlabel_itemTv);
				tv.setText(selectedList.get(i).getName());
				tv.setBackgroundResource(R.drawable.relation_evaluationtag_bg_press_shape);
				layout.setTag(selectedList.get(i).getId());
				layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int id = (Integer) v.getTag();
						if (isContansId(id, myLabelList) > -1) {
							// ((TextView) myLayout.getChildAt(
							// isContansId(id, myLabelList)).findViewById(
							// R.id.addviewText))
							// .setBackgroundResource(R.drawable.relation_evaluationtag_bg_commen_shape);
							mBList.remove(isContansId(id, myLabelList));
							mBList.add(isContansId(id, myLabelList), false);
							initMyTagLayout();
						}
						if (isContansId(id, defaultLabelList) > -1) {
							// ((TextView) jtLayout.getChildAt(
							// isContansId(id, defaultLabelList)).findViewById(
							// R.id.addviewText))
							// .setBackgroundResource(R.drawable.relation_evaluationtag_bg_commen_shape);
							dBList.remove(isContansId(id, defaultLabelList));
							dBList.add(isContansId(id, defaultLabelList), false);
							initGTTagLayout();
						}
						selectedCv.removeView(layout);
						selectedList.remove(isContansId(id, selectedList));

					}

				});
				selectedCv.addView(layout);
			} else {
				LinearLayout layout = (LinearLayout) LayoutInflater.from(this)
						.inflate(R.layout.actionlabel_addlayout, selectedCv,
								false);
				layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.show(OperType.Create, null);
					}
				});
				selectedCv.addView(layout);
			}
		}
	}

	private void initMyTagLayout(){
		myLabelCv.removeAllViews();
		for (int i = myLabelList.size() - 1; i >= 0; i--) {
			final int n = i;
			final LinearLayout layout = (LinearLayout) LayoutInflater.from(
					ActionlabelActivity.this).inflate(R.layout.actionlabel_item, myLabelCv,
					false);
			TextView tv = (TextView) layout.findViewById(R.id.actionlabel_itemTv);
			tv.setText(myLabelList.get(i).getName());
			layout.setTag(myLabelList.get(i).getId());
			if (mBList.get(i) == true) {
				tv.setBackgroundResource(R.drawable.relation_evaluationtag_bg_press_shape);
			} else {
				tv.setBackgroundResource(R.drawable.relation_evaluationtag_bg_commen_shape);
			}
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int id = (Integer) v.getTag();
					Log.e(TAG, "current id::"+id);
					Log.e(TAG, "isChoosed::"+mBList.get(isContansId(id, myLabelList)));
					if (mBList.get(isContansId(id, myLabelList)) == true) {
						if(isContansId(id, selectedList) > -1){
							selectedList.remove(isContansId(id, selectedList));
						}else{
							selectedList.add(myLabelList.get(isContansId(id, myLabelList)));
						}
						initSelectedLayout();
						mBList.remove(isContansId(id, myLabelList));
						mBList.add(isContansId(id, myLabelList), false);
						initMyTagLayout();
					}else{
						mBList.remove(isContansId(id, myLabelList));
						mBList.add(isContansId(id, myLabelList), true);
						initMyTagLayout();
						selectedList.add(myLabelList.get(isContansId(id, myLabelList)));
						initSelectedLayout();
					}
				}
			});
			myLabelCv.addView(layout);
		}
	}
	
	private void initGTTagLayout(){
		defaultLabelCv.removeAllViews();
		for (int i = defaultLabelList.size() - 1; i >= 0; i--) {
			final int n = i;
			final LinearLayout layout = (LinearLayout) LayoutInflater.from(
					ActionlabelActivity.this).inflate(R.layout.actionlabel_item, defaultLabelCv,
							false);
			TextView tv = (TextView) layout.findViewById(R.id.actionlabel_itemTv);
			tv.setText(defaultLabelList.get(i).getName());
			layout.setTag(defaultLabelList.get(i).getId());
			if (dBList.get(i) == true) {
				tv.setBackgroundResource(R.drawable.relation_evaluationtag_bg_press_shape);
			} else {
				tv.setBackgroundResource(R.drawable.relation_evaluationtag_bg_commen_shape);
			}
			layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int id = (Integer) v.getTag();
					Log.e(TAG, "current id::"+id);
					Log.e(TAG, "isChoosed::"+dBList.get(isContansId(id, defaultLabelList)));
					if (dBList.get(isContansId(id, defaultLabelList)) == true) {
						if(isContansId(id, selectedList) > -1){
							selectedList.remove(isContansId(id, selectedList));
						}else{
							selectedList.add(defaultLabelList.get(isContansId(id, defaultLabelList)));
						}
						initSelectedLayout();
						dBList.remove(isContansId(id, defaultLabelList));
						dBList.add(isContansId(id, defaultLabelList), false);
						initGTTagLayout();
					}else{
						dBList.remove(isContansId(id, defaultLabelList));
						dBList.add(isContansId(id, defaultLabelList), true);
						initGTTagLayout();
						selectedList.add(defaultLabelList.get(isContansId(id, defaultLabelList)));
						initSelectedLayout();
					}
				}
			});
			defaultLabelCv.addView(layout);
		}
	}
	
	private void initDialog() {
		// TODO Auto-generated method stub
		dialog = new MyCatalogDialog(this);
		dialog.setOnDialogClickListener(new OnDialogClickListener() {

			@Override
			public void onClick(OperType operType, int which,
					String categoryName) {
				// TODO Auto-generated method stub
				if (operType == OperType.Create) {
					addActionLabel(categoryName);
				}
			}
		});
	}

	/**
	 * 1、发送标签名字到服务器
	 * 2、成功返回后，将数据封装成ActionLabel对象，分别加入selectedList和myLabelList,同时mBLis添加
	 * “true”数据 3、执行“已选择”和“我的标签”数据的添加
	 * 
	 * @param name
	 */
	public void addActionLabel(String name) {
		new Thread() {
			public void run() {

				String param = "";
				KeelLog.e(TAG, "addTagToServer param::" + param);
				String result = HttpRequestUtil.sendPost(Constant.applyPath,
						param, ActionlabelActivity.this);
				KeelLog.e(TAG, "addTagToServer result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao != null) {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())
								&& !"null".equals(dao.getResponseData())) {
							try {
								JSONObject job = new JSONObject(
										dao.getResponseData());
								int id = job.getInt("id");
								String name = job.getString("name");
								Message msg = new Message();
								msg.what = 1;
								Bundle bundle = new Bundle();
								bundle.putInt("id", id);
								bundle.putString("name", name);
								msg.setData(bundle);
								handler.sendMessage(msg);

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						} else {
							sendMessage(0, "添加标签失败", null);
						}
					} else {
						sendMessage(0, "添加标签失败,失败原因：" + dao.getNotifyInfo(),
								null);
					}
				} else {
					sendMessage(0, "添加标签失败", null);
				}
			};
		}.start();
	}

	public List<ActionLabel> getListItemData(String str,int type) {
		List<ActionLabel> list = new ArrayList<ActionlabelActivity.ActionLabel>();
		try {
			Gson gson = new Gson();
			JSONObject job = new JSONObject(str);
			JSONArray arr = job.getJSONArray("list");
			for (int i = 0; i < arr.length(); i++) {
				String tempStr = arr.get(i).toString();
				ActionLabel label = gson.fromJson(tempStr, ActionLabel.class);
				list.add(label);
				if(type == 0){//我的标签
					mBList.add(false);
				}else if(type ==1){//金桐推荐
					dBList.add(false);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询对象list中是否包含此id，包含返回对象在list中位置，不包含返回-1
	 * 
	 * @param id
	 * @param list
	 * @return
	 */
	public int isContansId(int id, List<ActionLabel> list) {
		int i = -1;
		for (int m = 0; m < list.size(); m++) {
			if (id == list.get(m).getId()) {
				i = m;
			}
		}
		return i;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_create_label_ok, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_ok:
			// selectList 所有已选择的标签
			// Intent data = new Intent();
			// data.putExtra(ENavConsts.DEMAND_LABEL_DATA, selectData);
			// setResult(Activity.RESULT_OK, data);
			// finish();
			if(selectedList.size()>10){
				Toast.makeText(this, "您最多只能选择10个标签", 0).show();
			}else if(selectedList.size() == 0){
				Toast.makeText(this, "请选择标签", 0).show();
			}else{
				String str = "";
				for(int i = 0;i<selectedList.size();i++){
					if(i == (selectedList.size()-1)){
						str = str+selectedList.get(i).getName();
					}else{
						str = str+selectedList.get(i).getName()+"、";
					}
				}
				Intent intent = new Intent();
				intent.putExtra("labelStr", str);
				setResult(RESULT_OK, intent);
				
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void initJabActionBar() {
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		TextView myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("标签");
		mCustomView.findViewById(R.id.titleIv).setVisibility(View.GONE);

	}

	public void sendMessage(int what, String str, Bundle bundle) {
		Message msg = new Message();
		msg.what = what;
		if (str != null && !"".equals(str)) {
			msg.obj = str;
		}
		if (bundle != null) {
			msg.setData(bundle);
		}
		handler.sendMessage(msg);
	}

	class ActionLabel implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4773861028172754193L;

		private int id;
		private String name;

		public ActionLabel(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "id:" + id + ",name:" + name;
		}
	}

}
