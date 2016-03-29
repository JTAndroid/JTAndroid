package com.tongmeng.alliance.activity;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tongmeng.alliance.dao.PeopleApplyInfoDao;
import com.tongmeng.alliance.dao.PeopleApplyInfoPropertiesDao;
import com.tongmeng.alliance.dao.PeopleApplyInfoTagDao;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tongmeng.alliance.view.InfoDialog;
import com.tongmeng.alliance.view.MyGridView;
import com.tongmeng.alliance.view.PeopleManagerPopupWindow;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.utils.log.KeelLog;

public class PeopleMemberActivity extends JBaseActivity implements OnClickListener{

	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;

	// 界面
	TextView nameText, phoneText, emailText, positionText, companyText,
			signCodeText, isSignText;
	TextView supplyAddBtn, supplyEditText, needAddBtn, needEditBtn;
	MyGridView needGridView, supplyGridView;
	LinearLayout layout;

	String activityId, type, userId;
	PeopleApplyInfoDao infoDao;
	MyTagAdapter needAdapter, supplyAdapter;
	String addIndex = "", // 添加按钮（需求或供应标签中的添加）
			deleteIndex = "",// 编辑按钮（需求或供应标签中的编辑）
			needEditIndex = "gone",// 需求标签中的编辑状态，负责adapter中图片的显示和隐藏
			supplyEditIndex = "gone";// 供应标签中的编辑状态，负责adapter中图片的显示和隐藏

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				initValue();
				break;
			case 1:
				Toast.makeText(PeopleMemberActivity.this, msg.obj + "", 0)
						.show();
				break;
			case 2:// 删除需求标签成功
				int position = Integer.parseInt(msg.obj + "");
				KeelLog.e(TAG, "当前标签是：" + deleteIndex);
				if (deleteIndex.equals("need")) {
					KeelLog.e(
							TAG,
							"当前删除标签是："
									+ infoDao.getDemandTagList().get(position)
											.getName());
					infoDao.getDemandTagList().remove(position);
					needAdapter.notifyDataSetChanged();
				} else if (deleteIndex.equals("supply")) {
					KeelLog.e(
							TAG,
							"当前删除标签是："
									+ infoDao.getSupplyTagList().get(position)
											.getName());
					infoDao.getSupplyTagList().remove(position);
					supplyAdapter.notifyDataSetChanged();
				}

				break;
			case 3:// 添加/删除标签失败
				Toast.makeText(PeopleMemberActivity.this, msg.obj + "", 0)
						.show();
				break;
			case 4:// 添加标签成功
				KeelLog.e(TAG, "当前添加按钮是：" + addIndex);
				if (addIndex.equals("need")) {
					if (needAdapter == null) {
						needAdapter = new MyTagAdapter(
								infoDao.getDemandTagList());
						needGridView.setAdapter(needAdapter);
					} else {
						needAdapter.setShow(false);
						needAdapter.notifyDataSetChanged();
					}
				} else if (addIndex.equals("supply")) {
					if (supplyAdapter == null) {
						supplyAdapter = new MyTagAdapter(
								infoDao.getSupplyTagList());
						supplyGridView.setAdapter(supplyAdapter);
					} else {
						supplyAdapter.setShow(false);
						supplyAdapter.notifyDataSetChanged();
					}
				}
				break;
			case 5:

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.peoplemember);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		activityId = getIntent().getStringExtra("activityId");
		KeelLog.e(TAG, "activityID::" + activityId);
		type = getIntent().getStringExtra("type");
		userId = getIntent().getStringExtra("userId");
		KeelLog.e(TAG, "type:" + type);
		KeelLog.e(TAG, "userId:" + userId);

		if (type.equals("1")) {
			type = "注册用户";
		} else if (type.equals("2")) {
			type = "未注册用户";
		}

		findViews();

		new Thread() {
			public void run() {
				String param = "{\"id\":\"" + userId + "\"}";
				KeelLog.e(TAG, "param::" + param);
				String result = HttpRequestUtil.sendPost(
						Constant.peopleMumberPath, param,
						PeopleMemberActivity.this);
				KeelLog.e(TAG, "onCreate  result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao != null) {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())
								&& !"null".equals(dao.getResponseData())) {
							infoDao = getInfo(dao.getResponseData());
							if (infoDao != null) {
								handler.sendEmptyMessage(0);
							} else {
								Message msg = new Message();
								msg.what = 1;
								msg.obj = "用户信息为空";
								handler.sendMessage(msg);
							}
						} else {
							Message msg = new Message();
							msg.what = 1;
							msg.obj = "用户信息为空！";
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = "获取成员详细信息失败,失败原因：" + dao.getNotifyInfo()
								+ ",请重试！";
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = "用户信息为空";
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	private void findViews() {
		// TODO Auto-generated method stub
		// 界面
		nameText = (TextView) findViewById(R.id.peoplemumber_name);
		phoneText = (TextView) findViewById(R.id.peoplemumber_phone);
		emailText = (TextView) findViewById(R.id.peoplemumber_email);
		positionText = (TextView) findViewById(R.id.peoplemumber_position);
		companyText = (TextView) findViewById(R.id.peoplemumber_company);
		signCodeText = (TextView) findViewById(R.id.peoplemumber_signcode);
		isSignText = (TextView) findViewById(R.id.peoplemumber_issign);

		supplyAddBtn = (TextView) findViewById(R.id.peoplemumber_addsupplyTag);
		supplyEditText = (TextView) findViewById(R.id.peoplemumber_editsupplyTag);
		needAddBtn = (TextView) findViewById(R.id.peoplemumber_addneedTag);
		needEditBtn = (TextView) findViewById(R.id.peoplemumber_editneedTag);
		supplyAddBtn.setOnClickListener(this);
		supplyEditText.setOnClickListener(this);
		needAddBtn.setOnClickListener(this);
		needEditBtn.setOnClickListener(this);

		needGridView = (MyGridView) findViewById(R.id.peoplemumber_needGridView);
		supplyGridView = (MyGridView) findViewById(R.id.peoplemumber_supplyGridView);

		layout = (LinearLayout) findViewById(R.id.peoplemumber_propertiesLayout);
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		KeelLog.e("ContactsMainPageActivity", "initJabActionBar");
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.org_firstpage_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
//		myTitle.setText("上传名单");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setVisibility(View.GONE);
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setBackgroundResource(R.drawable.login_user_acount);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(PeopleMemberActivity.this, "此功能正在完善，敬请期待", 0).show();
			}
		});
	}

	class MyTagAdapter extends BaseAdapter {
		List<PeopleApplyInfoTagDao> list;
		boolean isShow = false;

		public boolean isShow() {
			return isShow;
		}

		public void setShow(boolean isShow) {
			this.isShow = isShow;
		}

		public MyTagAdapter(List<PeopleApplyInfoTagDao> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(PeopleMemberActivity.this)
						.inflate(R.layout.peoplemumber_tag_item, null);
				viewHolder.tv = (TextView) convertView
						.findViewById(R.id.peoplemumber_tag_item_text);
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.peoplemumber_tag_item_img);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final PeopleApplyInfoTagDao dao = list.get(position);
			viewHolder.tv.setText(dao.getName());
			if (isShow) {
				if (dao.getIsEditable().equals("1")) {
					if (viewHolder.img.getVisibility() == View.GONE) {
						viewHolder.img.setVisibility(View.VISIBLE);
					} else {
						viewHolder.img.setVisibility(View.GONE);
					}
				}
			} else {
				viewHolder.img.setVisibility(View.GONE);
			}
			viewHolder.img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new Thread() {
						public void run() {
							String param = "{\"idList\":[" + dao.getId() + "]}";
							KeelLog.e(TAG, " 需求标签删除param::" + param);
							String result = HttpRequestUtil.sendPost(
									Constant.peopledeleteTagPath, param,
									PeopleMemberActivity.this);
							KeelLog.e(TAG, " 需求标签删除result::" + result);
							ServerResultDao resultDao = Utils
									.getServerResult(result);
							if (resultDao != null) {
								if (resultDao.getNotifyCode().equals("0001")) {
									if (resultDao.getResponseData() == null
											|| "".equals(resultDao
													.getResponseData())
											|| "null".equals(resultDao
													.getResponseData())) {
										Message msg = new Message();
										msg.what = 3;
										msg.obj = "删除标签失败";
										handler.sendMessage(msg);
									} else {
										boolean isDelete = getDeleteTagResult(resultDao
												.getResponseData());
										if (isDelete) {
											Message msg = new Message();
											msg.what = 2;
											msg.obj = position;
											handler.sendMessage(msg);
										} else {
											Message msg = new Message();
											msg.what = 3;
											msg.obj = "删除标签失败";
											handler.sendMessage(msg);
										}
									}
								} else {
									Message msg = new Message();
									msg.what = 3;
									msg.obj = "删除标签失败，失败原因："
											+ resultDao.getNotifyInfo();
									handler.sendMessage(msg);
								}
							} else {
								Message msg = new Message();
								msg.what = 3;
								msg.obj = "删除标签失败";
								handler.sendMessage(msg);
							}
						};
					}.start();
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView tv;
			ImageView img;
		}
	}

	public PeopleApplyInfoDao getInfo(String responseData) {
		KeelLog.e(TAG, "getInfo responseData::" + responseData);
		/**
		 * {"memberDetail":
		 * 
		 * 
		 * {"id":34, "name":"美王-007", "mobile":"18611286623",
		 * "email":"wangmeizhou@gintong.com1", "company":"桐盟桐盟一统天下",
		 * "position":"高级工程师", "properties":null, "demandTagList":null,
		 * "supplyTagList":null, "signInCode":"123456", "isSignIn":0
		 * 
		 * }}
		 */
		PeopleApplyInfoDao dao = new PeopleApplyInfoDao();
		try {
			JSONObject resJob = new JSONObject(responseData);
			String memberDetail = resJob.getString("memberDetail");
			if (memberDetail == null || "".equals(memberDetail)
					|| "null".equals(memberDetail)) {
				return null;
			} else {
				JSONObject job = resJob.getJSONObject("memberDetail");
				dao.setId(job.getInt("id"));
				dao.setName(job.getString("name"));
				dao.setMobile(job.getString("mobile"));
				dao.setEmail(job.getString("email"));
				dao.setCompany(job.getString("company"));
				dao.setPosition(job.getString("position"));

				// 获取properties数据
				String properties = job.getString("properties");
				if (properties != null && !"".equals(properties)
						&& !"[]".equals(properties)
						&& !"null".equals(properties)) {
					List<PeopleApplyInfoPropertiesDao> propertiesList = new ArrayList<PeopleApplyInfoPropertiesDao>();
					JSONArray arr = job.getJSONArray("properties");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject tempJson = arr.getJSONObject(i);
						PeopleApplyInfoPropertiesDao propertiesDao = new PeopleApplyInfoPropertiesDao();
						String key = tempJson.getString("key");
						String value = tempJson.getString("value");
						if (key != null && !"".equals(key)) {
							propertiesDao.setKey(key);
						}
						if (key != null && !"".equals(value)) {
							propertiesDao.setValue(value);
						}
						if (propertiesDao != null) {
							propertiesList.add(propertiesDao);
						}
					}
					if (propertiesList != null && propertiesList.size() > 0) {
						dao.setPropertiesList(propertiesList);
					}
				}

				// 获取demandTagList数据
				String demandTagList = job.getString("demandTagList");
				List<PeopleApplyInfoTagDao> needTagList = new ArrayList<PeopleApplyInfoTagDao>();
				if (demandTagList != null && !"".equals(demandTagList)
						&& !"null".equals(demandTagList)
						&& !"[]".equals(demandTagList)) {
					JSONArray arr = job.getJSONArray("demandTagList");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject tempJson = arr.getJSONObject(i);
						PeopleApplyInfoTagDao tagDao = new PeopleApplyInfoTagDao();
						int id = tempJson.getInt("id");
						String name = tempJson.getString("name");
						String isEditable = tempJson.getString("isEditable");
						if (id != 0) {
							tagDao.setId(id);
						}
						if (name != null && !"".equals(name)) {
							tagDao.setName(name);
						}
						tagDao.setIsEditable(isEditable);
						needTagList.add(tagDao);
					}
				}
				dao.setDemandTagList(needTagList);

				// 获取supplyTagList数据
				String supplyTagList = job.getString("supplyTagList");
				List<PeopleApplyInfoTagDao> tagList = new ArrayList<PeopleApplyInfoTagDao>();
				if (supplyTagList != null && !"".equals(supplyTagList)
						&& !"null".equals(supplyTagList)
						&& !"[]".equals(supplyTagList)) {
					JSONArray arr = job.getJSONArray("supplyTagList");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject tempJson = arr.getJSONObject(i);
						PeopleApplyInfoTagDao tagDao = new PeopleApplyInfoTagDao();
						int id = tempJson.getInt("id");
						String name = tempJson.getString("name");
						String isEditable = tempJson.getString("isEditable");
						if (id != 0) {
							tagDao.setId(id);
						}
						if (name != null && !"".equals(name)) {
							tagDao.setName(name);
						}
						tagDao.setIsEditable(isEditable);
						tagList.add(tagDao);
					}
				}
				dao.setSupplyTagList(tagList);

				dao.setSignInCode(job.getString("signInCode"));
				dao.setIsSignIn(job.getString("isSignIn"));
				return dao;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	// 删除标签
	public boolean getDeleteTagResult(String responseData) {
		try {
			JSONObject job = new JSONObject(responseData);
			return job.getBoolean("succeed");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isContainsStr(List<PeopleApplyInfoTagDao> list, String str) {
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals(str)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param type
	 *            类型：1为需求，2为供应
	 * @param tag
	 *            标签内容
	 */
	public void sendMesToServer(final String type, final String tag) {
		new Thread() {
			@SuppressWarnings("null")
			public void run() {
				String param = "{\"memberId\":\"" + userId + "\",\"tag\":\""
						+ tag + "\",\"type\":\"" + type + "\"}";
				KeelLog.e(TAG, "sendMesToServer param ::" + param);
				String result = HttpRequestUtil.sendPost(
						Constant.peopleAddTagPath, param,
						PeopleMemberActivity.this);
				KeelLog.e(TAG, "sendMesToServer result ::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				KeelLog.e(TAG, "sendMesToServer  dao :" + dao);
				if (dao != null) {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())) {
							PeopleApplyInfoTagDao applyInfoDao = addTagResult(dao
									.getResponseData());
							KeelLog.e(TAG, "sendMesToServer  applyInfoDao::"
									+ applyInfoDao);
							if (applyInfoDao == null) {
								Message msg = new Message();
								msg.what = 3;
								msg.obj = "添加标签失败";
								handler.sendMessage(msg);
							} else {
								KeelLog.e(TAG, "sendMesToServer  type :" + type);
								KeelLog.e(TAG, "infoDao::" + infoDao);
								if (type.equals("1")) {
									infoDao.getDemandTagList()
											.add(applyInfoDao);
								} else if (type.equals("2")) {
									KeelLog.e(
											TAG,
											"infoDao.getSupplyTagList()::"
													+ infoDao
															.getSupplyTagList());
									infoDao.getSupplyTagList()
											.add(applyInfoDao);
								}

								handler.sendEmptyMessage(4);
							}
						}
					} else {
						Message msg = new Message();
						msg.what = 3;
						msg.obj = "添加标签失败,失败原因：" + dao.getNotifyInfo();
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = 3;
					msg.obj = "添加标签失败";
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	public void initValue() {
		if (infoDao != null) {
			myTitle.setText(infoDao.getName()+"(" + type + ")");
			nameText.setText(infoDao.getName());
			phoneText.setText(infoDao.getMobile());
			emailText.setText(infoDao.getEmail());
			positionText.setText(infoDao.getPosition());
			companyText.setText(infoDao.getCompany());
			if (infoDao.getPropertiesList() != null
					&& infoDao.getPropertiesList().size() > 0) {
				for (int i = 0; i < infoDao.getPropertiesList().size(); i++) {
					View view = LayoutInflater.from(PeopleMemberActivity.this)
							.inflate(R.layout.peoplemumber_properties_item,
									null);
					TextView keyText = (TextView) view
							.findViewById(R.id.peoplemumber_properties_item_keyText);
					TextView valueText = (TextView) view
							.findViewById(R.id.peoplemumber_properties_item_valueText);
					PeopleApplyInfoPropertiesDao propertiesDao = infoDao
							.getPropertiesList().get(i);
					keyText.setText(propertiesDao.getKey());
					valueText.setText(propertiesDao.getValue());
					layout.addView(view);
				}

			}

			if (infoDao.getDemandTagList() != null
					&& infoDao.getDemandTagList().size() > 0) {
				needAdapter = new MyTagAdapter(infoDao.getDemandTagList());
				needGridView.setAdapter(needAdapter);
			}

			if (infoDao.getSupplyTagList() != null
					&& infoDao.getSupplyTagList().size() > 0) {
				supplyAdapter = new MyTagAdapter(infoDao.getSupplyTagList());
				supplyGridView.setAdapter(supplyAdapter);
			}

			signCodeText.setText(infoDao.getSignInCode());

			if (infoDao.getIsSignIn().equals("0")) {
				isSignText.setText("否");
			} else if (infoDao.getIsSignIn().equals("0")) {
				isSignText.setText("是");
			}
		}
	}

	// 添加标签
	public PeopleApplyInfoTagDao addTagResult(String responseData) {
		try {
			KeelLog.e(TAG, "addTagResult  responseData::" + responseData);
			JSONObject job = new JSONObject(responseData);
			Gson gson = new Gson();
			String tag = job.getString("tag");
			if (tag == null || "".equals(tag) || "null".equals(tag)) {
				return null;
			} else {
				JSONObject tagObj = job.getJSONObject("tag");
				return gson.fromJson(tagObj.toString(),
						PeopleApplyInfoTagDao.class);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.peoplemumber_addneedTag:
			addIndex = "need";
			Intent intetn = new Intent(PeopleMemberActivity.this,
					InfoDialog.class);
			startActivityForResult(intetn, 1);
			break;
		case R.id.peoplemumber_editneedTag:
			deleteIndex = "need";
			if (needEditIndex.equals("gone")) {
				needAdapter.setShow(true);
				needAdapter.notifyDataSetChanged();
				needEditIndex = "show";
			} else if (needEditIndex.equals("show")) {
				needAdapter.setShow(false);
				needAdapter.notifyDataSetChanged();
				needEditIndex = "gone";
			}
			break;
		case R.id.peoplemumber_addsupplyTag:
			addIndex = "supply";
			Intent intent = new Intent(PeopleMemberActivity.this,
					InfoDialog.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.peoplemumber_editsupplyTag:
			deleteIndex = "supply";
			if (supplyEditIndex.equals("gone")) {
				supplyAdapter.setShow(true);
				supplyAdapter.notifyDataSetChanged();
				supplyEditIndex = "show";
			} else if (supplyEditIndex.equals("show")) {
				supplyAdapter.setShow(false);
				supplyAdapter.notifyDataSetChanged();
				supplyEditIndex = "gone";
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			String s = data.getStringExtra("custom");
			if (addIndex.equals("need")) {
				if (isContainsStr(infoDao.getDemandTagList(), s)) {
					Toast.makeText(this, "已存在您定义的字段，请添加其他字段", 0).show();
				} else {
					sendMesToServer("1", s);
				}
			} else if (addIndex.equals("supply")) {
				if (isContainsStr(infoDao.getSupplyTagList(), s)) {
					Toast.makeText(this, "已存在您定义的字段，请添加其他字段", 0).show();
				} else {
					sendMesToServer("2", s);
				}
			}
		}
	}
}
