package com.tongmeng.alliance.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.tongmeng.alliance.view.MyCatalogDialog1;
import com.tongmeng.alliance.view.MyCatalogDialog1.OnSelectListener;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.widgets.HorizontalListView;
import com.utils.log.KeelLog;

public class ActionTreeActivity extends JBaseActivity {

	private final String TAG = getClass().getSimpleName();
	private ImageView search;
	private TextView myTitle;
	private TextView create_Tv;

	private EditText keywordEt; // 关键字EditText
	private ImageView addIv; // 添加ImageView
	private XListView categoryLv; // 目录列表ListView
	private TextView tabTv, categorySumTv;// 根目录名字、当前多少个目录
	private HorizontalListView tabLv; // 横向目录名称展示控件

	private String mKeyword = ""; // 搜索关键字
	private TabAdapter tabAdapter;// 横向目录名称适配器
	private List<ActionCategory> list = new ArrayList<ActionCategory>();// 用于目录展示
	List<ActionCategory> tempList = new ArrayList<ActionCategory>();// 用于目录数据的获取
	private List<ActionCategory> returnList = new ArrayList<ActionCategory>();// 用于记录已选择的目录

	List<ActionCategory> keyList = new ArrayList<ActionTreeActivity.ActionCategory>();// 关键字不会空时，记录list中包含当前关键字的对象

	CategoryAdapter categoryAdapter;// Listview 的adapter
	MyCatalogDialog1 itemDialog;// listview长按item，出现的三个选项
	MyCatalogDialog operateDialog;// 弹出对话框
	ActionCategory mActionCategory;// 当前item对应数据，用于listview item的点击

	List<Integer> idList = new ArrayList<Integer>();// tabLv中，item对应的id集合
	List<String> nameList = new ArrayList<String>();// tabLv中，item对应的name集合

	int tabId = 0;// tabLv item点击，记录当前item对应id
	int loadNum = 0;// 记录当前加载的次数

	int currentId = -1;// item的id，用于listview item的长按
	String currentName = "";// item的name，用于listview item的长按

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:// 有根目录或者当前目录有子目录

				loadNum = loadNum + 1;
				KeelLog.e(TAG, "handler 0  loadNum:" + loadNum);
				if (list != null && list.size() > 0) {
					list.clear();
				}
				list.addAll(tempList);
				KeelLog.e(TAG, "handler 0  list.size:" + list.size());
				categorySumTv.setText(list.size() + "个目录");

				if (categoryAdapter != null) {
					categoryAdapter.setAdapterList(list);
					categoryAdapter.notifyDataSetChanged();
				} else {
					categoryAdapter = new CategoryAdapter(list);
					categoryLv.setAdapter(categoryAdapter);
				}

				if (loadNum >= 2) {
					KeelLog.e(TAG, "handler 0 mActionCategory::"
							+ mActionCategory.toString());
					idList.add(mActionCategory.getId());
					nameList.add(mActionCategory.getName());
					tabAdapter.notifyDataSetChanged();
				}
				break;
			case 1:// toast给出提示
				dismissLoadingDialog();
				Toast.makeText(ActionTreeActivity.this, msg.obj + "", 0).show();
				break;
			case 2:// 通过addIv添加目录成功
				dismissLoadingDialog();
				Bundle addBundle = msg.getData();
				ActionCategory tempDao = (ActionCategory) addBundle
						.getSerializable("addDao");
				KeelLog.e(TAG, "tempDao::" + tempDao.toString());
				if (loadNum >= 2) {
					list.add(tempDao);
				} else {
					list.add(list.size() - 1, tempDao);
				}
				categorySumTv.setText(list.size() + "个目录");
				categoryAdapter.notifyDataSetChanged();
				break;
			case 3:// 修改目录名字成功
				dismissLoadingDialog();
				Bundle bundle = msg.getData();
				int id = bundle.getInt("id");
				String name = bundle.getString("name");
				KeelLog.e("handler 3 ", "id::" + id + ",name:" + name);
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId() == id) {
						list.get(i).setName(name);
					}
				}
				categoryAdapter.notifyDataSetChanged();
				break;
			case 4:// 删除目录成功
				dismissLoadingDialog();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId() == currentId) {
						list.remove(i);
					}
				}
				categoryAdapter.notifyDataSetChanged();
				break;
			case 5:// tabLv点击成功
				loadNum = idList.indexOf(tabId) + 1;
				KeelLog.e(TAG, "handler 5  loadNum:" + loadNum);
				setList(tabId);
				tabAdapter.notifyDataSetChanged();

				if (list != null && list.size() > 0) {
					list.clear();
				}
				list.addAll(tempList);
				categorySumTv.setText(list.size() + "个目录");

				if (categoryAdapter != null) {
					// categoryAdapter.setAdapterList(list);
					categoryAdapter.notifyDataSetChanged();
				} else {
					categoryAdapter = new CategoryAdapter(list);
					categoryLv.setAdapter(categoryAdapter);
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void initJabActionBar() {
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
		myTitle.setText("目录");
		create_Tv = (TextView) mCustomView.findViewById(R.id.create_Tv);
		create_Tv.setText("完成");
		create_Tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (returnList == null || returnList.size() == 0) {
					Toast.makeText(ActionTreeActivity.this, "您尚未选择目录", 0)
							.show();
				} else {
					Intent intent = new Intent();
					String tempStr = "";
					for (int i = 0; i < returnList.size(); i++) {
						if (i == returnList.size() - 1) {
							tempStr = tempStr + returnList.get(i).getName()
									+ "等";
						} else {
							tempStr = tempStr + returnList.get(i).getName()
									+ "、";
						}
					}
					intent.putExtra("catalogStr", tempStr);
					intent.putExtra("catalogNum", returnList.size());
					// intent.putExtra("", returnList);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		search = (ImageView) mCustomView.findViewById(R.id.titleIv);
		search.setVisibility(View.GONE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kno_act_category);
		initView();
		getData(0, keywordEt.getText().toString(), 1);
	}

	private void initView() {
		// TODO Auto-generated method stub
		idList.add(0);
		nameList.add("需求");

		keywordEt = (EditText) findViewById(R.id.keywordEt);
		keywordEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				mKeyword = s.toString();
				// if ("".equals(s) || s == null || s.length() == 0) {
				// categoryAdapter.setAdapterList(list);
				// categoryAdapter.notifyDataSetChanged();
				// } else {
				// categoryAdapter.setKeyWord(mKeyword);
				// categoryAdapter.notifyDataSetChanged();
				// }
				if (mKeyword != null && !"".equals(mKeyword)) {
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getName().contains(mKeyword)) {
							keyList.add(list.get(i));
						}
					}
					categoryAdapter.setAdapterList(keyList);
				} else {
					categoryAdapter.setAdapterList(list);
				}

				categoryAdapter.setKeyWord(mKeyword);
				categoryAdapter.notifyDataSetChanged();
			}
		});

		addIv = (ImageView) findViewById(R.id.addIv);
		addIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// alertDialog.show(OperType.Create, null);
				// operateDialog.show(OperType.Create, null);
				MyCatalogDialog addIvDialog = new MyCatalogDialog(
						ActionTreeActivity.this);
				addIvDialog
						.setOnDialogClickListener(new OnDialogClickListener() {

							@Override
							public void onClick(OperType operType, int which,
									String categoryName) {
								// TODO Auto-generated method stub
								KeelLog.e(TAG, "addIvDialog  operType::"
										+ operType + ",categoryName:"
										+ categoryName);
								if (operType == OperType.Create) {
									KeelLog.e(
											TAG,
											"idList.get(idList.size()-1)::"
													+ idList.get(idList.size() - 1));
									addCatalog(idList.get(idList.size() - 1),
											categoryName, true);
								}
							}
						});
				addIvDialog.show(OperType.Create, null);
			}
		});

		tabTv = (TextView) findViewById(R.id.tabTv);
		tabTv.setVisibility(View.GONE);
		// tabTv.setText("需求");
		// tabTv.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// keywordEt.setText("");
		// idList.clear();
		// nameList.clear();
		// if (tabAdapter != null) {
		// tabAdapter.notifyDataSetChanged();
		// }
		// idList.add(0);
		// nameList.add("需求");
		// getData(0, keywordEt.getText().toString());
		// }
		// });

		tabLv = (HorizontalListView) findViewById(R.id.tabLv);
		tabAdapter = new TabAdapter();
		tabLv.setAdapter(tabAdapter);
		tabLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				keywordEt.setText("");
				if (categoryAdapter.getKeyWord() != null
						&& !"".equals(categoryAdapter.getKeyWord())) {
					categoryAdapter.setKeyWord(keywordEt.getText().toString());
				}
				tabId = idList.get(position);
				KeelLog.e("tabLv", "tabId::" + tabId);
				KeelLog.e("tabLv", "tabName::" + nameList.get(position));
				setList(tabId);
				getData(tabId, keywordEt.getText().toString(), 3);
			}
		});

		categoryLv = (XListView) findViewById(R.id.categoryLv);
		categoryLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// index = false;
				if (mKeyword != null && !"".equals(mKeyword)) {
					mActionCategory = keyList.get(position-1);
				} else {
					mActionCategory = list.get(position - 1);
				}
				getData(mActionCategory.getId(),
						keywordEt.getText().toString(), 2);
				// nameList.add(mActionCategory.getName());
				// idList.add(mActionCategory.getId());
			}
		});
		categoryLv.setOnItemLongClickListener(new OnItemLongClickListener() {// 长按弹出对话框

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						// mActionCategory = list.get(position - 1);
						KeelLog.e(TAG, "position::" + position);
						if (mKeyword == null || "".equals(mKeyword)) {
							KeelLog.e(
									TAG,
									"list.get(position-1)::"
											+ list.get(position - 1).toString());
							currentId = list.get(position - 1).getId();
							currentName = list.get(position - 1).getName();
						} else {
							KeelLog.e(
									TAG,
									"keyList.get(position-1)::"
											+ keyList.get(position - 1).toString());
							currentId = keyList.get(position - 1).getId();
							currentName = keyList.get(position - 1).getName();
						}
						if (currentName.equals("未分组") && currentId == -1) {
							//
						} else {
							// // operDialog.setAttachViewAndCategory(view);
							// // operDialog.show();
							itemDialog.setDialogLocation(view);
							itemDialog.show();
						}
						return true;
					}
				});

		categorySumTv = (TextView) findViewById(R.id.categorySumTv);
		initDialog();
	}

	/**
	 * 
	 * @param id
	 *            目录id
	 * @param value
	 *            关键字
	 * @param type
	 *            判断是哪种搜索 1:根目录查询 2：子目录查询 3：tabLv点击查询
	 */
	public void getData(int id, String value, final int type) {
		final String param = getParams(id, value);
		KeelLog.e(TAG, "param ::" + param);
		new Thread() {
			public void run() {
				String result = HttpRequestUtil.sendPost(
						Constant.queryCatalogPath, param,
						ActionTreeActivity.this);
				KeelLog.e(TAG, "result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao == null) {
					getMessage(1, "获取数据失败");
				} else {
					if (dao.getNotifyCode().equals("0001")) {
						if (dao.getResponseData() != null
								&& !"".equals(dao.getResponseData())
								&& !"null".equals(dao.getResponseData())) {

							tempList = getCatalogList(dao.getResponseData());
							KeelLog.e(TAG, "tempList:" + tempList
									+ ",tempList.size::" + tempList.size());
							if (tempList == null || tempList.size() == 0) {
								getMessage(1, "当前目录没有子目录");
							} else {
								if (type != 3) {
									handler.sendEmptyMessage(0);
								} else {
									handler.sendEmptyMessage(5);
								}

							}
						} else {
							getMessage(1, "当前目录没有子目录");
						}
					} else {
						getMessage(1, "获取数据失败,失败原因：" + dao.getNotifyInfo());
					}
				}
			};
		}.start();
	}

	// 查询目录参数
	public String getParams(int id, String value) {
		if (value.equals(null) || "".equals(value)) {
			value = "";
		}
		return "{\"parentId\":\"" + id + "\",\"keyword\":\"" + value
				+ "\",\"page\":" + 0 + ",\"size\":" + 10 + "}";
	}

	// 解析获取到的目录信息
	public List<ActionCategory> getCatalogList(String str) {
		List<ActionCategory> list = new ArrayList<ActionCategory>();
		try {
			JSONObject job = new JSONObject(str);
			int count = job.getInt("count");
			if (count <= 0) {
				return list;
			}
			JSONArray arr = job.getJSONArray("catalogList");
			for (int i = 0; i < arr.length(); i++) {
				String json = arr.optJSONObject(i).toString();
				Gson gson = new Gson();
				ActionCategory dao = gson.fromJson(json, ActionCategory.class);
				list.add(dao);
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			KeelLog.e(TAG, "e::" + e.getMessage());
		}
		return null;
	}

	class CategoryAdapter extends BaseAdapter {
		String keyWord;
		private final int SELECT_ITEM_COLOR = 0XFFFFA500; // 选中项目颜色
		private final int NORMAL_TEXT_COLOR = 0XFF000000; // 默认文字颜色

		List<ActionCategory> list1 = new ArrayList<ActionTreeActivity.ActionCategory>();
		List<ActionCategory> list2 = new ArrayList<ActionTreeActivity.ActionCategory>();
		List<ActionCategory> adapterList = new ArrayList<ActionTreeActivity.ActionCategory>();

		public CategoryAdapter(List<ActionCategory> list) {
			this.list1 = list;
			adapterList = list1;
		}

		public void setAdapterList(List<ActionCategory> list) {
			this.list1 = list;
			adapterList = list1;
		}

		public String getKeyWord() {
			return keyWord;
		}

		public void setKeyWord(String keyWord) {
			this.keyWord = keyWord;

			if (keyWord != null && !"".equals(keyWord)) {
				if (list2 != null && list2.size() > 0) {
					list2.clear();
				}

				for (int i = 0; i < list1.size(); i++) {
					if (list1.get(i).getName().contains(keyWord)) {
						list2.add(list1.get(i));
					}
				}
				adapterList = list2;
			} else {
				adapterList = list1;
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return adapterList.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater
						.from(ActionTreeActivity.this)
						.inflate(R.layout.kno_list_item_category, parent, false);
				viewHolder.selectCb = (CheckBox) convertView
						.findViewById(R.id.selectCb);
				viewHolder.selectCb2 = (CheckBox) convertView
						.findViewById(R.id.selectCb2);
				viewHolder.foldIv = (ImageView) convertView
						.findViewById(R.id.foldIv);
				viewHolder.titleTv = (TextView) convertView
						.findViewById(R.id.titleTv);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final ActionCategory dao = adapterList.get(position);
			KeelLog.e(TAG, "dao::" + dao.toString());

			if (keyWord == null || "".equals(keyWord)) {
				viewHolder.titleTv.setText(dao.getName());
				viewHolder.titleTv.setTextColor(NORMAL_TEXT_COLOR);
			} else {
				if (dao.getName().contains(keyWord)) {
					viewHolder.titleTv.setTextColor(NORMAL_TEXT_COLOR);
					SpannableString spannableString = new SpannableString(
							dao.getName());
					spannableString.setSpan(new ForegroundColorSpan(
							SELECT_ITEM_COLOR), dao.getName().indexOf(keyWord),
							dao.getName().indexOf(keyWord) + keyWord.length(),
							Spanned.SPAN_INCLUSIVE_INCLUSIVE);
					viewHolder.titleTv.setText(spannableString);
				} else {
					viewHolder.titleTv.setTextColor(NORMAL_TEXT_COLOR);
					viewHolder.titleTv.setText(dao.getName());
				}
			}
			viewHolder.selectCb.setVisibility(View.GONE);
			viewHolder.selectCb2
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (isChecked) {
								returnList.add(dao);
								KeelLog.e(TAG, returnList.size() + "");
							} else {
								returnList.remove(dao);
								KeelLog.e(TAG, returnList.size() + "");
							}
						}
					});
			return convertView;
		}

		class ViewHolder {
			CheckBox selectCb, selectCb2;
			ImageView foldIv;
			TextView titleTv;
		}
	}

	class TabAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// return nameList.size();
			return nameList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(ActionTreeActivity.this,
					R.layout.category_tabview, null);
			TextView tabLv_Tv = (TextView) convertView
					.findViewById(R.id.tabLv_Tv);
			KeelLog.e(TAG, "nameList.size::" + nameList.size());
			KeelLog.e(TAG, "name" + nameList.get(position));
			if (position == 0) {
				tabLv_Tv.setCompoundDrawables(null, null, null, null);
				tabLv_Tv.setTextColor(Color.parseColor("#569ee2"));
				tabLv_Tv.setPadding(15, 0, 0, 0);
			}
			tabLv_Tv.setText(nameList.get(position));
			return convertView;
		}
		//
		// public void notifyChanged() {
		// this.notifyDataSetChanged();
		// tabLv.setSelection(tabLv.getLastVisiblePosition());
		// }
	}

	class ActionCategory implements Serializable {
		/**
		 * {"id":36,"name":"热门活动","subCount":6}
		 */
		int id;
		String name;
		int subCount;

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

		public int getSubCount() {
			return subCount;
		}

		public void setSubCount(int subCount) {
			this.subCount = subCount;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "id:" + id + ",name:" + name + ",subCount::" + subCount;
		}
	}

	public void initDialog() {
		itemDialog = new MyCatalogDialog1(this);
		itemDialog.setOnSelectListener(new OnSelectListener() {

			@Override
			public void onSelect(OperType operType) {
				// TODO Auto-generated method stub
				operateDialog.show(operType, currentName);
			}
		});
		operateDialog = new MyCatalogDialog(this);
		operateDialog.deleteMessage = "您确定要删除此目录吗？";
		operateDialog.setOnDialogClickListener(new OnDialogClickListener() {

			@Override
			public void onClick(OperType operType, int which,
					String categoryName) {
				// TODO Auto-generated method stub
				KeelLog.e(TAG, "id::" + currentId + ",name:" + categoryName);
				if (which == 0) { // 取消
					return;
				}
				showLoadingDialog();
				switch (operType) {
				case Create: // 创建
					if (!TextUtils.isEmpty(categoryName.trim())) {
						// if (checkCategoryName(categoryName, categoryId,
						// level,false)) {
						// createCategory(categoryType, categoryId,
						// categoryName);
						// } else {
						// dismissLoadingDialog();
						// showToast("该目录已存在");
						// }
						addCatalog(currentId, categoryName, false);
					} else {
						dismissLoadingDialog();
						showToast("目录名称不能为空");
					}
					break;
				case Modify: // 修改
					if (!TextUtils.isEmpty(categoryName.trim())) {
						// if (checkCategoryName(categoryName, categoryId,
						// level,true)) {
						// editCategory(categoryType, categoryId, categoryName);
						// } else {
						// dismissLoadingDialog();
						// showToast("该目录已存在");
						// }
						modifyCatalog(currentId, categoryName);
					} else {
						dismissLoadingDialog();
						showToast("目录名称不能为空");

					}

					break;
				case Delete: // 删除
					// deleteCategory(categoryType, categoryId);
					deleteCatalog(currentId);
					break;
				}
			}
		});
	}

	/**
	 * 增加目录
	 * 
	 * @param id
	 *            当前目录的id，一级目录为0
	 * @param categoryName
	 *            要增加的目录的名字
	 * @param type
	 *            是否在当前目录下增加（通过addIv添加的目录是在当前目录下，成功后adapter要刷新；
	 *            通过item长按添加的目录是当前item的子目录）
	 */
	private void addCatalog(final int id, final String categoryName,
			final boolean type) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				String param = "{\"parentId\":\"" + id + "\",\"name\":\""
						+ categoryName + "\"}";
				KeelLog.e(TAG, "add catalog param::" + param);
				String result = HttpRequestUtil
						.sendPost(Constant.addCatalogPath, param,
								ActionTreeActivity.this);
				KeelLog.e(TAG, "add catalog result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao != null) {
					if (dao.getNotifyCode().equals("0001")) {
						// if (dao.getResponseData() == null
						// || "".equals(dao.getResponseData())
						// || "null".equals(dao.getResponseData())) {
						// Toast.makeText(ActionTreeActivity.this, "添加子目录失败", 0)
						// .show();
						// } else {
						//
						// }
						// getMessage(1, "添加子目录成功！");
						if (dao.getResponseData() == null
								|| "".equals(dao.getResponseData())
								|| "null".equals(dao.getResponseData())) {

						} else {
							Gson gson = new Gson();
							try {
								JSONObject job = new JSONObject(
										dao.getResponseData());
								JSONObject catalog = job
										.getJSONObject("catalog");
								ActionCategory tempDao = new ActionCategory();
								tempDao.setId(catalog.getInt("key"));
								if (!"".equals(catalog.getString("value"))
										&& catalog.getString("value") != null) {
									tempDao.setName(catalog.getString("value"));
								}
								KeelLog.e(TAG, "tempDao::" + tempDao.toString());
								if (type) {
									Message msg = new Message();
									msg.what = 2;
									Bundle bundle = new Bundle();
									bundle.putSerializable("addDao", tempDao);
									msg.setData(bundle);
									handler.sendMessage(msg);
								} else {
									getMessage(1, "子目录添加成功！");
								}

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
					} else {
						getMessage(1, "添加子目录失败,失败原因：" + dao.getNotifyInfo());
					}
				} else {
					getMessage(1, "添加子目录失败！");
				}
			};
		}.start();

	}

	// 修改目录名字
	private void modifyCatalog(final int id, final String categoryName) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				String param = "{\"id\":\"" + id + "\",\"name\":\""
						+ categoryName + "\"}";
				KeelLog.e(TAG, "modifyCatalog param::" + param);
				String result = HttpRequestUtil.sendPost(
						Constant.modifyCatalogPath, param,
						ActionTreeActivity.this);
				KeelLog.e(TAG, "modifyCatalog result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao == null) {
					getMessage(1, "修改目录名字失败！");
				} else {
					if (dao.getNotifyCode().equals("0001")) {
						try {
							JSONObject job = new JSONObject(
									dao.getResponseData());
							JSONObject catalog = new JSONObject(
									job.getString("catalog"));
							Message msg = new Message();
							msg.what = 3;
							Bundle bundle = new Bundle();
							KeelLog.e("modifyCatalog",
									"id::" + catalog.getInt("key") + ",name::"
											+ catalog.getString("value"));
							bundle.putInt("id", catalog.getInt("key"));
							bundle.putString("name", catalog.getString("value"));
							msg.setData(bundle);
							handler.sendMessage(msg);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					} else {
						getMessage(1, "修改目录名字失败！");
					}
				}
			};
		}.start();
	}

	// 删除目录
	private void deleteCatalog(final int id) {
		new Thread() {
			public void run() {
				String param = "{\"id\":\"" + id + "\"}";
				KeelLog.e(TAG, "add catalog param::" + param);
				String result = HttpRequestUtil.sendPost(
						Constant.deleteCatalogPath, param,
						ActionTreeActivity.this);
				KeelLog.e(TAG, "add catalog result::" + result);
				ServerResultDao dao = Utils.getServerResult(result);
				if (dao != null) {
					if (dao.getNotifyCode().equals("0001")) {
						handler.sendEmptyMessage(4);
					} else {
						getMessage(1, "删除目录失败,失败原因：" + dao.getNotifyInfo());
					}
				} else {
					getMessage(1, "删除目录失败");
				}
			};
		}.start();
	}

	public void getMessage(int what, String obj) {
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	public void setList(int id) {
		int i = 0;
		for (int j = 0; j < idList.size(); j++) {
			if (idList.get(j) == id) {
				i = j;
			}
		}
		KeelLog.e(TAG, "setList  i::" + i);

		for (int m = idList.size() - 1; m >= 0; m--) {
			if (m > i) {
				idList.remove(m);
				nameList.remove(m);
			}
			KeelLog.e(TAG, "idList.size::" + idList.size());
		}
	}
}
