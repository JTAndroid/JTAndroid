package com.tr.ui.organization.firstpage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.constvalue.EnumConst.ModuleType;
import com.lidroid.xutils.BitmapUtils;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.model.knowledge.UserCategory;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.demand.RedactLabelActivity;
import com.tr.ui.organization.adapter.ListViewAdapter;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.organization.firstpage.QuickIndexBar.OnTouchIndexListener;
import com.tr.ui.organization.model.Contacts;
import com.tr.ui.organization.model.cusandorg.CusAndOrg_Page;
import com.tr.ui.organization.model.cusandorg.PageItem;
import com.tr.ui.organization.model.param.ClientDetailsParams;
import com.tr.ui.widgets.CircleImageView;
import com.utils.common.EConsts;
import com.utils.common.Util;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

/*
 * 组织/客户列表
 */
public class OrganizationAndCustomerActivity extends Activity implements
		OnClickListener, OnItemClickListener, IBindData {
	private ImageView org_iv;
	private MyXListView listView;
	private List<Contacts> lists = new ArrayList<Contacts>();
	private List<Contacts> listss = new ArrayList<Contacts>();
	private MyAdapter adapter;
	private CusAndOrg_Page cusandorg_page;
	private BitmapUtils bitmapUtils;
	private TextView categoryTv;
	private TextView tagTv;
	private QuickIndexBar quickIndexBar;
	private TextView currentIndex;
	private EditText keywordEt;
	private int width;
	private boolean flag = false;
	private List<PageItem> pageItemList;
	private List<String> mobileList = new ArrayList<String>();// 手机号码
	private List<String> planeList = new ArrayList<String>();// 座机号码
	private Map<Integer, String> maps = new HashMap<Integer, String>();// 放返回的所有电话号码
	private List<String> all_Phone = new ArrayList<String>();// 所有可以打电话的
	private List<String> message_Phone = new ArrayList<String>();// 所有可以发短信的
	private long clickPositionCusId;

	private static final int TYPE_ALL = 3; // 全部 我的知识:::修改：将全部我的知识删掉，首页显示我创建的
	private static final int TYPE_FAVORITES = 1; // 我收藏的
	private static final int TYPE_SHARE_TO_ME = 2; // 分享给我
	private static final int TYPE_CREATE = 3; // 我创建的

	private static final int TYPE_CATEGORY = 1004; // 目录返回的
	private static final int TYPE_TAG = 1003; // 标签返回的
	private static final int TYPE_SCREEN = 1003; // 筛选返回的
	private static final int TYPE_REFRESH = 1005;
	public final static int STATE_NORMAL = 0; // 正常
	public final static int STATE_GETMORE = 1; // 加载更多

	public final static int STATE_REFRESH = 2; // 刷新
	private int type = TYPE_ALL;
	private UserCategory category;
	private String knoTagName = "0";
	private TextView org_Tv;
	private boolean editMode = false; // 编辑状态
	private boolean checkAll = false; // 全选状态
	private String mKey = "";// 搜索，传的字符串
	private int index = 0;// 当前页码
	private int size;// 该页内容数
	private int total;// 项目总数
	private int lastItem;
	private int count;// 知识缩略对象的总数
	// private JTPage mPage;
	private int mState = 0;// 0-正常状态 1-获取更多中 2-刷新中
	private String TAG = "OrganizationAndCustomerActivity";
	private int label = 2;

	private long customerId;
	private long createById;
	private Map<String, Object> map;

	private final String reg1 = "^[1][358][0-9]{9}";// 手机号码验证 正则表达式
	private final String reg2 = "^[0][1-9]{2,3}-[0-9]{5,10}$";// 座机号码正则
	private String CreateOrCollecttype = -2 + "";// "type":"类型 -2:全部客户和组织好友 -1:全部客户 1:我创建的 2:我收藏的",

	private String[] create_or_collect = { "组织/客户", "我创建的", "我收藏的" };

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		listView.startRefresh();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_orgorcustomer);
		x = getWindowManager().getDefaultDisplay().getWidth();
		init();
		initListener();// 816827
		initOnouchListener();
		quickIndexBar.setBackgroundColor(Color.TRANSPARENT);
		bitmapUtils = new BitmapUtils(this);
		category = new UserCategory();

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			currentIndex.setVisibility(View.INVISIBLE);
		}
	};
	private int x;
	private PopupWindow popupWindow;
	private TextView createOrganization_Tv;

	private void showIndex(String word) {
		if (flag == true) {
			currentIndex.setVisibility(View.VISIBLE);
			currentIndex.setText(word);
		}
		handler.removeCallbacksAndMessages(null);
	}

	// 初始化控件对象
	private void init() {
		keywordEt = (EditText) findViewById(R.id.keywordEt);
		org_iv = (ImageView) findViewById(R.id.org_iv);
		listView = (MyXListView) findViewById(R.id.listview);
		categoryTv = (TextView) findViewById(R.id.categoryTv);
		tagTv = (TextView) findViewById(R.id.tagTv);
		org_Tv = (TextView) findViewById(R.id.org_Tv);
		quickIndexBar = (QuickIndexBar) findViewById(R.id.quickIndexBar);
		currentIndex = (TextView) findViewById(R.id.currentIndex);
		createOrganization_Tv = (TextView) findViewById(R.id.createOrganization_Tv);
		LinearLayout footLl = (LinearLayout) findViewById(R.id.footLl);
		listView.setBottomBar(footLl);
		listView.showFooterView(false);
		// 设置xlistview可以加载、刷新
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				type = TYPE_REFRESH;
				index =  0;
				startGetData();
			}

			@Override
			public void onLoadMore() {
				index++;
				startGetData();
			}
		});
	}

	/*
	 * protected void startGetData() { if (type == TYPE_CATEGORY) {
	 * CreateOrCollecttype = -1+""; OrganizationReqUtil.doGetCusAndOrg(this,
	 * this, category.id+"", "0",CreateOrCollecttype , "1000", "", null); } else
	 * if (type == TYPE_SCREEN) { OrganizationReqUtil.doGetCusAndOrg(this, this,
	 * "0", "0",CreateOrCollecttype , "1000", "", null); }else{
	 * CreateOrCollecttype = -2+""; OrganizationReqUtil.doGetCusAndOrg(this,
	 * this, "0", "0",CreateOrCollecttype , "1000", "", null); } }
	 */

	protected void startGetData() {
		if (type == TYPE_SCREEN) {
			OrganizationReqUtil.doGetCusAndOrg(this, this, "0", "0",
					CreateOrCollecttype, "1000", mKey, "", null);
		} else if (type == TYPE_REFRESH) {
			OrganizationReqUtil.doGetCusAndOrg(this, this, "0", "0",
					CreateOrCollecttype, "20", mKey, "", null);
		} else {
			OrganizationReqUtil.doGetCusAndOrg(this, this, "0", index + "",
					CreateOrCollecttype, "20", mKey, "", null);
		}

	}

	// 初始化点击事件
	private void initListener() {
		org_iv.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		categoryTv.setOnClickListener(this);
		tagTv.setOnClickListener(this);
		org_Tv.setOnClickListener(this);
		createOrganization_Tv.setOnClickListener(this);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			private PopupWindow popupWindow2;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				View contentView = View.inflate(
						OrganizationAndCustomerActivity.this,
						R.layout.organdcus_popupwindow, null);
				contentView.measure(0, 0);
				width = contentView.getMeasuredWidth();
				popupWindow2 = new PopupWindow(contentView,
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				popupWindow2.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				popupWindow2.setOutsideTouchable(true);
				popupWindow2.showAsDropDown(view, x / 2 - width / 2, 0);
				ImageView delete_iv = (ImageView) contentView
						.findViewById(R.id.delete_iv);
				delete_iv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (popupWindow2 != null && popupWindow2.isShowing()) {
							popupWindow2.dismiss();
							if (lists.get(position - 1).getVirtual() == 0) {// 是客户，可以删除
								long customerId = lists.get(position - 1)
										.getCustomerId();
								OrganizationReqUtil.doDeleteOrgAndCustomer(
										OrganizationAndCustomerActivity.this,
										OrganizationAndCustomerActivity.this,
										customerId, null);
								lists.remove(position - 1);
								adapter.notifyDataSetChanged();
							} else {
								ToastUtil.showToast(
										OrganizationAndCustomerActivity.this,
										"组织不可以删除，请在详情页面与组织解除好友关系");
							}
						}
					}
				});
				// }
				return true;
			}
		});

		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(keywordEt.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					break;
				}
				return false;
			}
		});

		quickIndexBar.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					flag = true;
					break;
				case MotionEvent.ACTION_UP:
					flag = false;
					handler.sendEmptyMessage(0);
					break;
				}
				return false;
			}
		});
		keywordEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				type = TYPE_SCREEN;
				if (keywordEt.getText() != null
						&& !keywordEt.getText().toString().equals("")) {
					mKey = keywordEt.getText().toString();
					startGetData();
					// mKey = "";
				} else {
					mKey = "";
					startGetData();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// keywordEt.setOnEditorActionListener(new OnEditorActionListener() {
		//
		// @Override
		// public boolean onEditorAction(TextView v, int actionId, KeyEvent
		// event) {
		// if ((actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId ==
		// EditorInfo.IME_ACTION_SEARCH)
		// && event != null) {
		// if (keywordEt.getText() != null
		// && !keywordEt.getText().toString().equals("")) {
		// mKey = keywordEt.getText().toString();
		// startGetData();
		// mKey = "";
		// } else {
		// ToastUtil.showToast(OrganizationAndCustomerActivity.this, "请输入查询内容");
		// }
		// }
		// return false;
		// }
		// });

	}

	// 将请求回来的数据填充到集合中
	private void fillList() {
		for (int i = 0; i < pageItemList.size(); i++) {
			Contacts contacts = new Contacts();
			contacts.setCity(pageItemList.get(i).city);
			contacts.setCustomerId(pageItemList.get(i).customerId);
			contacts.setNameFirst(pageItemList.get(i).nameFirst);
			contacts.setIndustrys(pageItemList.get(i).industrys);
			contacts.setName(pageItemList.get(i).name);
			contacts.setPicLogo(pageItemList.get(i).picLogo);
			contacts.setVirtual(pageItemList.get(i).virtual);
			contacts.setLinkMobile(pageItemList.get(i).linkMobile);
			contacts.setShortName(pageItemList.get(i).shotName);
			contacts.createById = pageItemList.get(i).createById;
			lists.add(contacts);
		}
	}

	private void initOnouchListener() {
		quickIndexBar.setOnTouchIndexListener(new OnTouchIndexListener() {
			@Override
			public void onTouchIndex(String word) {
				showIndex(word);
				for (int i = 0; i < lists.size(); i++) {
					String firstWord = lists.get(i).getNameFirst();
					if (firstWord.equals(word)) {
						listView.setSelection(i);
						break;
					}
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.org_iv:// 点击后退键，进入前一个页面
			if (editMode) {
				editMode = false;
			} else if (type == TYPE_CATEGORY) {
				ENavigate.startKnowledgeCategoryActivityForResult(
						OrganizationAndCustomerActivity.this, 10001, null,
						ModuleType.ORG, false, org_Tv.getText()
								.toString());
			} else {
				finish();
			}
			break;
		case R.id.createOrganization_Tv:
			ENavigate.startCreateClienteleActivity(
					OrganizationAndCustomerActivity.this, null, 1, 0L);
			break;
		case R.id.org_Tv:

			popupWindow = new PopupWindow(OrganizationAndCustomerActivity.this);
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
			ColorDrawable dw = new ColorDrawable(0000000);
			popupWindow.setBackgroundDrawable(dw);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
			View convertView = View.inflate(
					OrganizationAndCustomerActivity.this,
					R.layout.organization_customer_popupwindow, null);
			final TextView organization_customer = (TextView) convertView
					.findViewById(R.id.organization_customer);
			final TextView my_create = (TextView) convertView
					.findViewById(R.id.my_create);
			final TextView my_collect = (TextView) convertView
					.findViewById(R.id.my_collect);
			final LinearLayout organization_customer_Ll = (LinearLayout) convertView
					.findViewById(R.id.organization_customer_Ll);
			final LinearLayout my_create_Ll = (LinearLayout) convertView
					.findViewById(R.id.my_create_Ll);
			final LinearLayout my_collect_Ll = (LinearLayout) convertView
					.findViewById(R.id.my_collect_Ll);
			organization_customer_Ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CreateOrCollecttype = -2 + "";
					type = TYPE_SCREEN;
					OrganizationReqUtil.doGetCusAndOrg(
							OrganizationAndCustomerActivity.this,
							OrganizationAndCustomerActivity.this, "", "0",
							CreateOrCollecttype, "20", "", "", null);
					popupWindow.dismiss();
					org_Tv.setText(organization_customer.getText().toString());
				}
			});
			my_create_Ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CreateOrCollecttype = 1 + "";
					type = TYPE_SCREEN;
					OrganizationReqUtil.doGetCusAndOrg(
							OrganizationAndCustomerActivity.this,
							OrganizationAndCustomerActivity.this, "", "0",
							CreateOrCollecttype, "20", "", "", null);
					popupWindow.dismiss();
					org_Tv.setText(my_create.getText().toString());
				}
			});
			my_collect_Ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CreateOrCollecttype = 2 + "";
					type = TYPE_SCREEN;
					OrganizationReqUtil.doGetCusAndOrg(
							OrganizationAndCustomerActivity.this,
							OrganizationAndCustomerActivity.this, "", "0",
							CreateOrCollecttype, "20", "", "", null);
					popupWindow.dismiss();
					org_Tv.setText(my_collect.getText().toString());
				}
			});
			popupWindow.setContentView(convertView);

			popupWindow.showAsDropDown(org_Tv, -20, -2);

			break;
		// 目录
		case R.id.categoryTv:

			ENavigate.startKnowledgeCategoryActivityForResult(
					OrganizationAndCustomerActivity.this, 10001, null,
					ModuleType.ORG, false, org_Tv.getText()
							.toString());

			break;
		// 标签
		case R.id.tagTv:
			ENavigate.startRedactLabelActivity(
					OrganizationAndCustomerActivity.this, 10002,
					RedactLabelActivity.ModulesType.OrgAndCustomModules, true); // 进入标签界面
			break;
		}
	}

	class MyAdapter extends BaseAdapter {
		private Context context;
		private List<Contacts> lists;

		public MyAdapter(Context context, List<Contacts> lists) {
			this.context = context;
			this.lists = lists;
		}

		public List<Contacts> getLists() {
			return lists;
		}

		public void setLists(List<Contacts> lists) {
			this.lists = lists;
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context,
						R.layout.org_orgorcustomer_listviewitem, null);
//				holder.org_RL = (RelativeLayout) convertView
//						.findViewById(R.id.org_RL);// 首字母所在的布局
//				holder.tv_word = (TextView) convertView
//						.findViewById(R.id.tv_word);// 开头首字母
				holder.org_tv_name = (TextView) convertView
						.findViewById(R.id.org_tv_name);// 名称
				holder.iv_message = (ImageView) convertView
						.findViewById(R.id.iv_message);// 短信图标
				holder.iv_dial = (ImageView) convertView
						.findViewById(R.id.iv_dial);// 电话图标
				holder.org_iv_headprotrait = (CircleImageView) convertView
						.findViewById(R.id.org_iv_headprotrait);// Logo
				holder.org_iv_head = (ImageView) convertView
						.findViewById(R.id.org_iv_head);// 组织 客户图标
				holder.org_tv_location = (TextView) convertView
						.findViewById(R.id.org_tv_location);// 地区
				holder.org_tv_work = (TextView) convertView
						.findViewById(R.id.org_tv_work);// 行业

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}
			final Contacts contacts = lists.get(position);
			if (!TextUtils.isEmpty(contacts.getShortName())) {// 设置名称
				holder.org_tv_name.setText(contacts.getShortName());
			} else {
				holder.org_tv_name.setText(contacts.getName());
			}
			String org_name = holder.org_tv_name.getText().toString();
			Util.initAvatarImage(context, holder.org_iv_headprotrait, org_name,
					contacts.getPicLogo(), 1, 2);

			holder.org_tv_name.setCompoundDrawables(null, null, null, null);
			Drawable drawable = null;
			if (contacts.getVirtual() == 0) {// 设置是否是组织/客户
				drawable = getResources().getDrawable(
						R.drawable.contactclienttag);
			} else if (contacts.getVirtual() == 1 || contacts.getVirtual() == 2) {
				drawable = getResources().getDrawable(
						R.drawable.contactorganizationtag);

				mobileList.clear();
				planeList.clear();

			}
			String linkedMobiless = contacts.getLinkMobile();
			if (!TextUtils.isEmpty(linkedMobiless.trim())) {
				holder.iv_message.setVisibility(View.VISIBLE);
				holder.iv_dial.setVisibility(View.VISIBLE);
			} else {
				holder.iv_message.setVisibility(View.GONE);
				holder.iv_dial.setVisibility(View.GONE);
			}
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			holder.org_tv_name.setCompoundDrawables(drawable, null, null, null);
			holder.org_tv_name.setCompoundDrawablePadding(DisplayUtil.dip2px(
					OrganizationAndCustomerActivity.this, 10));

			if (!TextUtils.isEmpty(contacts.getCity())) {
				holder.org_tv_location.setText(contacts.getCity());// 设置城市
			} else {
				holder.org_tv_location.setVisibility(View.GONE);
			}

			if (!"(null)".equals(contacts.getIndustrys())) {
				holder.org_tv_work.setText(contacts.getIndustrys());// 设置行业
			}

			holder.iv_message.setOnClickListener(new OnClickListener() {// 短信
						@Override
						public void onClick(View v) {
							message_Phone.clear();
							String linkMobiles = contacts.getLinkMobile();
							String reg1 = "^[1][358][0-9]{9}";// 手机号码验证
																// 正则表达式
																// String
																// reg2 =
																// "^[0][1-9]{2,3}-[0-9]{5,10}$";//
																// 座机号码正则
							if (!TextUtils.isEmpty(linkMobiles)) {// 设置电话　　短信的图标
								String[] linkedMobiles = linkMobiles.split(",");
								for (String linkedMobile : linkedMobiles) {
									if (linkedMobile.matches(reg1)) {// 手机号码
										message_Phone.add(linkedMobile);
									}
								}

							}
							AlertDialog alertDialog_message = new AlertDialog.Builder(
									context).create();
							if (message_Phone.size() > 0) {
								alertDialog_message.show();
							} else {
								toastMsg("手机号码格式不正确，无法发送短信");
							}

							Window window_message = alertDialog_message
									.getWindow();
							View convertView = View.inflate(context,
									R.layout.people_maincontacts_message, null);
							window_message.setContentView(convertView);
							alertDialog_message.setCanceledOnTouchOutside(true);
							ListView listView = (ListView) convertView
									.findViewById(R.id.listView);
							listView.setAdapter(new ListViewAdapter(
									message_Phone, context));
							listView.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									TextView phone = (TextView) view
											.findViewById(R.id.phone);
									sendMessage(phone.getText().toString());
								}
							});
						}
					});

			holder.iv_dial.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					all_Phone.clear();
					String linkMobiles = contacts.getLinkMobile();
					String reg1 = "^[1][358][0-9]{9}";// 手机号码验证 正则表达式
					String reg2 = "^[0][1-9]{2,3}-[0-9]{5,10}$";// 座机号码正则
					if (!TextUtils.isEmpty(linkMobiles)) {// 设置电话　　短信的图标
						String[] linkedMobiles = linkMobiles.split(",");
						for (String linkedMobile : linkedMobiles) {
							if (linkedMobile.matches(reg1)) {// 手机号码
								all_Phone.add(linkedMobile);
							} else if (linkedMobile.matches(reg2)) {
								all_Phone.add(linkedMobile);
							}
						}
					}

					AlertDialog alertDialog_message = new AlertDialog.Builder(
							context).create();
					if (all_Phone.size() > 0) {
						alertDialog_message.show();
					} else {
						toastMsg("手机号码格式不正确，无法拨打电话");
					}

					Window window_message = alertDialog_message.getWindow();
					View convertView = View.inflate(context,
							R.layout.people_maincontacts_message, null);
					window_message.setContentView(convertView);
					alertDialog_message.setCanceledOnTouchOutside(true);
					ListView listView = (ListView) convertView
							.findViewById(R.id.listView);
					listView.setAdapter(new ListViewAdapter(all_Phone, context));
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							/*
							 * TextView phone = (TextView) view
							 * .findViewById(R.id.phone);
							 * callPhone(phone.getText().toString());
							 */
							callSystemPhone(view);
						}

					});
				}
			});

			// 对首字母进行判断
			String currentFirstWord = contacts.getNameFirst();
			if (position > 0) {
				String lastFirstWord = lists.get(position - 1).getNameFirst();
				if (currentFirstWord.equals(lastFirstWord)) {
//					holder.org_RL.setVisibility(View.GONE);
				} else {
//					holder.org_RL.setVisibility(View.VISIBLE);
//					holder.tv_word.setText(currentFirstWord);
				}
			} else {
//				holder.org_RL.setVisibility(View.VISIBLE);
//				holder.tv_word.setText(currentFirstWord);
			}

			return convertView;
		}
	}

	private void callSystemPhone(View view) {
		TextView phone = (TextView) view.findViewById(R.id.phone);
		Uri uri = Uri.parse("tel:" + phone.getText());
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		OrganizationAndCustomerActivity.this.startActivity(intent);
	}

	class ViewHolder {
		public RelativeLayout org_RL;
//		public TextView tv_word;// 顶部的字母
		public TextView org_tv_name;// 每个listView子条目的名称
		public ImageView iv_message;// 每个listView子条目的短信图标
		public ImageView iv_dial;// 每个listView子条目的电话图标
		public CircleImageView org_iv_headprotrait;// 每个listView子条目的Logo图标
		public ImageView org_iv_head;// 每个listView子条目的组织/客户的图标
		public TextView org_tv_location;// 每个ListView的子条目的地区
		public TextView org_tv_work;// 每个ListView的子条目的行业
	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case EAPIConsts.OrganizationReqType.ORAGANIZATION_REQ_GETCUSTOMANDORG:
			listView.stopRefresh();
			listView.stopLoadMore();
			listView.resetHeaderHeight();
			if (object == null) {
				return;
			}
			Map<String, Object> dataHm = (Map<String, Object>) object;
			cusandorg_page = (CusAndOrg_Page) dataHm.get("page");
			if (cusandorg_page != null && cusandorg_page.listResults != null) {
				pageItemList = cusandorg_page.listResults;
				if (type == TYPE_SCREEN || type == TYPE_REFRESH) {
					lists.clear();
				}
				fillList();
				// for(int i = 0;i<lists.size();i++){
				// listss.add(lists.get(i));
				// }
				Collections.sort(lists);
				if (adapter == null) {
					adapter = new MyAdapter(this, lists);
					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else {
					adapter.notifyDataSetChanged();
				}
				type = TYPE_ALL;
			}

			break;
		case OrganizationReqType.ORAGANIZATION_REQ_DELCUSANDORG:
			if (object == null) {
				return;
			}
			Map<String, Boolean> dataMap = (Map<String, Boolean>) object;
			boolean result = dataMap.get("success");
			if (result == false) {
				Toast.makeText(this, "删除失败", 0).show();
			} else {
				Toast.makeText(this, "删除成功", 0).show();
			}
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (10001 == requestCode) {
				Log.i(TAG, " REQUESTCODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY");
				if (Activity.RESULT_OK == resultCode) {
					// 获取相应的目录

					category = (UserCategory) data
							.getSerializableExtra(EConsts.Key.KNOWLEDGE_CATEGORY);
					if (category != null) {
						type = TYPE_CATEGORY;
						// org_Tv.setText(category.getCategoryname());
					}

					// clearResponseData();
					// onRefresh();
				} else {
					type = TYPE_ALL;
				}

			}
			if (10002 == requestCode) {
				Log.i(TAG, " REQUESTCODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY");
				if (Activity.RESULT_OK == resultCode) {
					knoTagName = data.getStringExtra("tag");
					if (knoTagName != null) {
						org_Tv.setText(knoTagName);
					}
					// clearResponseData();
					// onRefresh();
				} else {
					type = TYPE_ALL;
					// clearResponseData();
					// onRefresh();
				}
			}

			if (10003 == requestCode) {
				ClientDetailsParams clientdetil = (ClientDetailsParams) data
						.getSerializableExtra("clientDetil");
				if (clientdetil != null
						&& clickPositionCusId == clientdetil.customerId) {
					for (Contacts item : lists) {
						if (clientdetil.customerId == item.customerId) {
							item.picLogo = clientdetil.picLogo;
							item.name = clientdetil.name;
							adapter.setLists(lists);
							adapter.notifyDataSetChanged();
							break;
						}
					}

				}
				listView.stopRefresh();
			}
		}
		clickPositionCusId = 0L;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (editMode) {
				editMode = false;
			} else if (type == TYPE_CATEGORY) {
				// ENavigate.startGlobalKnowledgeCategoryActivityForResult(MyKnowledgeActivity.this,
				// REQUESTCODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY);
				ENavigate.startKnowledgeCategoryActivityForResult(
						OrganizationAndCustomerActivity.this, 10001, null,
						ModuleType.ORG, false, org_Tv.getText()
								.toString());
			} else {
				return super.onKeyDown(keyCode, event);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			return;
		}
/*<<<<<<< HEAD
		if (lists!=null&&lists.get(position-1)!=null&&!lists.isEmpty()) {
		
		if (lists.get(position-1).virtual == 1 || lists.get(position-1).virtual == 2) {// 进入组织

			customerId = lists.get(position-1).customerId;
			createById = lists.get(position-1).createById;
			ENavigate.startOrgMyHomePageActivity(OrganizationAndCustomerActivity.this,customerId, createById, true,ENavConsts.type_details_org);
		} else if (lists.get(position-1).virtual == 0) {//进入客户：
			
			long customerId = lists.get(position-1).customerId;
			clickPositionCusId = customerId;
			Intent intent = new Intent(this, ClientDetailsActivity.class);
			intent.putExtra("customerId", customerId);
			intent.putExtra("label", label);
			startActivity(intent);
		}
=======*/
		if (lists != null && lists.get(position - 1) != null
				&& !lists.isEmpty()) {

			if (lists.get(position - 1).virtual == 1
					|| lists.get(position - 1).virtual == 2) {// 进入组织

				
				  customerId = lists.get(position-1).customerId; createById =
				  lists.get(position-1).createById;
				  ENavigate.startOrgMyHomePageActivity(
				  OrganizationAndCustomerActivity.this, customerId, createById,
				  true, ENavConsts.type_details_org);
				 
//				customerId = lists.get(position-1).customerId;
//				Intent intent = new Intent();
//				Bundle bundle = new Bundle();
//				bundle.putString(EConsts.Key.ID, customerId + "");
//				bundle.putString(EConsts.Key.HOME_PAGE_ROLE, GlobalVariable.HomePageInformation.organization.roleStr);
//				intent.putExtras(bundle);
//				intent.setClass(OrganizationAndCustomerActivity.this,
//						OrgMyHomePageActivity.class);
//				startActivity(intent);
			} else if (lists.get(position - 1).virtual == 0) {// 进入客户：

				long customerId = lists.get(position - 1).customerId;
				clickPositionCusId = customerId;
				Intent intent = new Intent(this, ClientDetailsActivity.class);
				intent.putExtra("customerId", customerId);
				intent.putExtra("label", label);
				startActivity(intent);

//				Intent intent = new Intent();
//				Bundle bundle = new Bundle();
//				bundle.putString(EConsts.Key.ID, customerId + "");
//				bundle.putString(EConsts.Key.HOME_PAGE_ROLE, GlobalVariable.HomePageInformation.Customer.roleStr);
//				intent.putExtras(bundle);
//				intent.setClass(OrganizationAndCustomerActivity.this,
//						ClientDetailsActivity.class);
//				startActivity(intent);
			}
		}

	}

	// 激活发短信页面，并将电话号码携带过去
	private void sendMessage(String message_number_business) {
		Uri uri = Uri.parse("smsto:" + message_number_business);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		startActivity(it);
	}

	// 激活打电话页面，并将电话号码携带过去
	private void callPhone(String number_business) {
		if (number_business.matches(reg1) || number_business.matches(reg2)) {
			Intent intent = new Intent("android.intent.action.DIAL");
			intent.setClassName("com.android.contacts",
					"com.android.contacts.DialtactsActivity");
			intent.setData(Uri.parse("tel:" + number_business));
			startActivity(intent);
		} else {
			ToastUtil.showToast(this, "号码格式不正确");
		}

	}

	private void toastMsg(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

}
