package com.tr.ui.knowledge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.knowledge.UserTag;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.KnoCategoryOperateDialog;
import com.tr.ui.widgets.KnoCategoryOperateDialog.OnDelTagListener;
import com.tr.ui.widgets.KnowledgeTagSideBar;
import com.tr.ui.widgets.MaxLengthWatcher;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * 全局知识标签
 * 
 * @author gintong
 * 
 */
public class GlobalKnowledgeTagActivity extends JBaseActivity implements IBindData {
	public static final String TAG = "GlobalKnowledgeTagActivity";

	public enum OperateType {
		/** 单选返回 */
		ClickBack,
		/** 正常多选 */
		MultipleChoice
	}

	private OperateType mOperateType = OperateType.MultipleChoice;

	private Context context;
	private EditText newTagNameEt;
	private TextView addTagTv;
	private ListView tagListLv;
	private TagListAdapter tagListAdapter;
	
	private KnoCategoryOperateDialog operDialog ; // 操作对话框

	// 字母索引工具bar start....
	private KnowledgeTagSideBar indexBar;// 字母索引工具bar
	private WindowManager mWindowManager;
	private TextView mDialogText;// 中间提示的text
	// 字母索引工具bar end....

	private ArrayList<String> listTag;
	private ArrayList<Integer> listCount;
	private ArrayList<UserTag> userTagList = new ArrayList<UserTag>();
	private ArrayList<UserTag> checkedTagList = new ArrayList<UserTag>();

	private LinearLayout activityRootLl;
	private boolean isdelete = false;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "标签", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	public Context getContext() {
		return context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String MSG = "onCreate()";
		setContentView(R.layout.activity_global_knowledge_tag);
		context = this;
		Intent intent = getIntent();
		mOperateType = (OperateType) intent.getSerializableExtra(EConsts.Key.OPERATE_TYPE);
		initComponent();
		// 根据不同模块跳转来请求不同的标签列表
		KnowledgeReqUtil.doGetKnowledgeTagList(context, this, App.getUserID(), null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.finish, menu);
		if (mOperateType == OperateType.ClickBack) {
			menu.getItem(0).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		String MSG = "onOptionsItemSelected()";

		switch (item.getItemId()) {
		case R.id.create_ok:

			checkedTagList.clear();
			for (UserTag userTag : userTagList) {
				if (userTag.isChecked())
					checkedTagList.add(userTag);
			}

			Log.i(TAG, MSG + " checkedTagList.size() = " + checkedTagList.size());

			Intent data = new Intent();
			data.putExtra("userTagList", checkedTagList);
			setResult(Activity.RESULT_OK, data);

			finish();

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void initComponent() {
		listTag = new ArrayList<String>();
		listCount = new ArrayList<Integer>();
		checkedTagList = new ArrayList<UserTag>();
		Intent intent = getIntent();
		passTagList = (ArrayList<String>) intent.getSerializableExtra("listTag");
		if (passTagList != null) {
			checkedTagList.clear();
			for (String name : passTagList) {
				UserTag userTag = new UserTag(name, 0 + "");
				checkedTagList.add(userTag);
			}

		}

		mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

		newTagNameEt = (EditText) findViewById(R.id.newTagNameEt);
		newTagNameEt.addTextChangedListener(new MaxLengthWatcher(context, 20, "字符数长度不能超过20", newTagNameEt));

		addTagTv = (TextView) findViewById(R.id.addTagTv);
		addTagTv.setOnClickListener(mOnClickListener);

		tagListLv = (ListView) findViewById(R.id.tagListLv);
		tagListAdapter = new TagListAdapter(context, userTagList);
		tagListLv.setAdapter(tagListAdapter);
		tagListLv.setOnItemClickListener(mOnItemClickListener);
		tagListLv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				operDialog = new KnoCategoryOperateDialog(GlobalKnowledgeTagActivity.this,userTagList.get(position));
				operDialog.setAttachViewAndCategory(view);
				operDialog.setOnDelTagListener(new OnDelTagListener() {
					
					@Override
					public void onDelTag(UserTag userTag) {
						listTag.remove(userTag.getName());
						KnowledgeReqUtil.doEditUserKnowledgeTag(context, GlobalKnowledgeTagActivity.this, App.getUserID(), listTag, null);
						showLoadingDialog();
						isdelete = true;
						tagListAdapter.notifyDataSetChanged();
					}
				});
				operDialog.show();
				return true;
			}
		});

		indexBar = (KnowledgeTagSideBar) findViewById(R.id.knowledgeTagSideBar);

		mDialogText = (TextView) LayoutInflater.from(this).inflate(R.layout.im_relationcontactlist_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);

	}

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String MSG = "onItemClick()";
			if (mOperateType == OperateType.ClickBack) {
				// 进根据标签查知识页面
				UserTag tag = userTagList.get(position);
//				Intent data = new Intent();
//				data.putExtra("tag", tag.getName());
//				Log.i(TAG, MSG + " tag = " + tag.getName());
//				setResult(Activity.RESULT_OK, data);
//				finish();
				Long TagId = (long) tag.getId();
				ENavigate.startKnowledgeCategoryLabelActivity(GlobalKnowledgeTagActivity.this, null,null, TagId , tag.getName());

			} else if (mOperateType == OperateType.MultipleChoice) {

				UserTag currentTag = userTagList.get(position);
				currentTag.setChecked(!currentTag.isChecked());
				tagListAdapter.notifyDataSetChanged();

				checkedTagList.clear();
				for (UserTag userTag : userTagList) {
					if (userTag.isChecked())
						checkedTagList.add(userTag);
				}
			}
		}
	};

	private boolean isContainForUserTag(ArrayList<UserTag> listUserTag, UserTag userTag) {
		boolean b = false;
		for (UserTag userTag2 : listUserTag) {
			if (userTag2.getName().trim().equals(userTag.getName().trim())) {
				b = true;
			}
		}
		return b;
	}

	private void initSimulateData() {
		listTag = new ArrayList<String>();
		listCount = new ArrayList<Integer>();

		String arrStr = "开发环境传成别人看不到yjkl3l3zuoww看不到yjkl3l3zuoww看不到yjkl3l3zuoww看不到yjkl3l3zuoww看不到yjkl3l3zuoww看不到yjkl3l3zuoww看不到yjkl3l3zuoww看不到yjkl3l3zuoww看不到yjkl3l3zuoww看不到yjkl3l3zuoww看不到yjkl3l3zuowwdnm3zd友盟升级在升wdnm3zd友盟升级在升wdnm3zd友盟升级在升wdnm3zd友盟升级在升wdnm3zd友盟升级在升级介绍里,加指定字符串,表示是否强制升级,例如#UpgradeCompulsively=1#新旧对象添加互转方法";
		char[] arrchar = arrStr.toCharArray();
		for (int i = 0; i < 40; i++) {
			listTag.add(arrchar[i] + "标签名" + i);
			listCount.add(i);
		}

		userTagList = new ArrayList<UserTag>();

		for (int i = 0; i < listCount.size(); i++) {
			UserTag userTag = new UserTag(listTag.get(i), listCount.get(i) + "");
			userTagList.add(userTag);
		}

		tagListAdapter.setListUserTag(userTagList);
		tagListAdapter.notifyDataSetChanged();
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// 保存之前选中状态
			checkedTagList.clear();
			for (UserTag userTag : userTagList) {
				if (userTag.isChecked())
					checkedTagList.add(userTag);
			}

			// 增加标签
			if (addTagTv == v) {
				String newTagNameStr = newTagNameEt.getText().toString().trim();
				if (!StringUtils.isEmpty(newTagNameStr)) {
					for (String tag : listTag) {
						if (tag.equals(newTagNameStr)) {
							Toast.makeText(context, "此标签名已存在!", 0).show();
							return;
						}
					}

					listTag.add(newTagNameStr);
					KnowledgeReqUtil.doEditUserKnowledgeTag(context, GlobalKnowledgeTagActivity.this, App.getUserID(), listTag, null);
					showLoadingDialog();
					tagListAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(context, "标签名不能为空", 0).show();
				}

			}

		}
	};

	private ArrayList<String> passTagList;

	public class TagListAdapter extends BaseAdapter {
		private Context context;

		private ArrayList<UserTag> listUserTag;

		public TagListAdapter(Context context, ArrayList<UserTag> listUserTag) {
			super();
			this.context = context;
			if (listUserTag != null) {
				this.listUserTag = listUserTag;
			} else {
				this.listUserTag = new ArrayList<UserTag>();
			}
		}

		public ArrayList<UserTag> getListUserTag() {
			return listUserTag;
		}

		public void setListUserTag(ArrayList<UserTag> listUserTag) {
			this.listUserTag = listUserTag;
		}

		@Override
		public int getCount() {
			return listUserTag.size();
		}

		@Override
		public UserTag getItem(int position) {
			return listUserTag.get(position);
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

				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_item_taglist, null);

				holder.tagNameTv = (TextView) convertView.findViewById(R.id.tagNameTv);
				holder.tagCountTv = (TextView) convertView.findViewById(R.id.tagCountTv);
				holder.tagCb = (CheckBox) convertView.findViewById(R.id.tagCb);
				holder.tagCb.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						UserTag userTag = (UserTag) v.getTag();
						userTag.setChecked(userTag.isChecked() ? false : true);
						notifyDataSetChanged();
					}
				});
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			UserTag userTag = listUserTag.get(position);
			holder.tagNameTv.setText(userTag.getName());
			holder.tagCountTv.setText(userTag.getCount());

			if (mOperateType == OperateType.MultipleChoice) {
				holder.tagCb.setVisibility(View.VISIBLE);
				holder.tagCb.setChecked(userTag.isChecked());
				holder.tagCb.setTag(userTag);
			} else if (mOperateType == OperateType.ClickBack) {
				holder.tagCb.setVisibility(View.GONE);
			}

			return convertView;
		}

		public final class ViewHolder {
			public TextView tagNameTv;
			public TextView tagCountTv;
			public CheckBox tagCb;
		}

	}

	@Override
	public void bindData(int tag, Object object) {

		switch (tag) {
		// 获取知识标签
		case KnoReqType.GetKnowledgeTagList:
			String MSG = "bindData()--case KnoReqType.GetKnowledgeTagList:() ";

			if (object != null) {
				Map<String, Object> dataMap = (Map<String, Object>) object;
				listCount = (ArrayList<Integer>) dataMap.get("listCount");
				listTag = (ArrayList<String>) dataMap.get("listTag");
				userTagList = new ArrayList<UserTag>();
				for (int i = 0; i < listCount.size(); i++) {
					String tagName = listTag.get(i);
					UserTag userTag = new UserTag(tagName, listCount.get(i) + "");
					userTagList.add(userTag);
				}
				for (int i = 0; i < userTagList.size(); i++) {
					UserTag userTag = userTagList.get(i);
					String name = userTag.getName();
					for (int j = 0; j < checkedTagList.size(); j++) {
						if (name.equals(checkedTagList.get(j).getName())) {
							userTag.setChecked(true);
						}
					}
				}
				TagComparator tagComparator = new TagComparator();
				Collections.sort(userTagList, tagComparator);
				tagListAdapter.setListUserTag(userTagList);
				tagListAdapter.notifyDataSetChanged();
				indexBar.setListView(tagListLv, userTagList);
			}
			break;

		case KnoReqType.EditUserKnowledgeTag:
			MSG = "bindData()--case KnoReqType.EditUserKnowledgeTag:() ";

			if (object != null) {
				Map<String, Object> dataMap = (Map<String, Object>) object;

				boolean b = (Boolean) dataMap.get("success");
				Log.i(TAG, MSG + " b = " + b);
				if (b) {
					listCount = (ArrayList<Integer>) dataMap.get("listCount");
					listTag = (ArrayList<String>) dataMap.get("listTag");

					userTagList = new ArrayList<UserTag>();
					for (int i = 0; i < listCount.size(); i++) {
						UserTag userTag = new UserTag(listTag.get(i), listCount.get(i) + "");
						userTagList.add(userTag);
					}

					for (int i = 0; i < userTagList.size(); i++) {
						UserTag userTag = userTagList.get(i);
						String name = userTag.getName();
						for (int j = 0; j < checkedTagList.size(); j++) {
							if (name.equals(checkedTagList.get(j).getName())) {
								userTag.setChecked(true);
							}
						}
					}

					TagComparator tagComparator = new TagComparator();
					Collections.sort(userTagList, tagComparator);

					tagListAdapter.setListUserTag(userTagList);
					tagListAdapter.notifyDataSetChanged();
					indexBar.setListView(tagListLv, userTagList);
					dismissLoadingDialog();
					if (isdelete) {
						Toast.makeText(context, "删除成功", 0).show();
						isdelete = false;
					}else {
						Toast.makeText(context, "添加成功!", 0).show();
					}
				} else {
					Toast.makeText(context, "添加失败!", 0).show();
					// if (userTagList.size() == 0) {
					// initSimulateData();
					// }
				}
			}
			break;

		default:
			break;
		}

	}

	@SuppressWarnings("rawtypes")
	public class TagComparator implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			UserTag tag1 = (UserTag) o1;
			UserTag tag2 = (UserTag) o2;
			String spell1 = tag1.getSpell();
			String spell2 = tag2.getSpell();
			return spell1.compareTo(spell2);
		}
	}

}
