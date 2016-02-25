package com.tr.ui.knowledge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.KnoTagGroupView;
import com.utils.common.EConsts;
import com.utils.file.FileUtils;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.IBindData;

/**
 * 编辑指定知识标签的详细信息
 * 
 * @author andshan
 * 
 */
public class EditKnowledgeTagActivity extends JBaseActivity implements OnItemClickListener, IBindData, OnClickListener {

	private final String TAG = getClass().getSimpleName();

	private KnoTagGroupView selectTagKTV;
	private ArrayList<String> allTags;
	private ArrayList<String> selectTags;
	private GridView allTagGV;
	private EditText addTagEt;
	private KnoTagsAdapter adapter;
	private Context context;
	private InputMethodManager imm;
	private LinearLayout knotageditLl;

	private Intent intent;

	private ArrayList<KnowledgeMini2> miniKnowledgeLists;

	private int mini2Size;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "编辑标签", false, null, false, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_knowledge_edittag);
		intent = getIntent();
		context = this;
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initView();
	}

	@Override
	public void onResume() {
		showLoadingDialog();
		KnowledgeReqUtil.doGetKnowledgeTagList(context, this, App.getUserID(), null);
		super.onResume();
	}

	public void initView() {
		allTags = new ArrayList<String>();
		selectTags = new ArrayList<String>();
		// listKnowledgeId = (ArrayList<Long>)
		// intent.getSerializableExtra(EConsts.Key.KNOWLEDGE_ID_LIST);
		// listKnowledgeType =
		// intent.getIntegerArrayListExtra(EConsts.Key.KNOWLEDGE_TYPE_LIST);
		miniKnowledgeLists = (ArrayList<KnowledgeMini2>) intent.getSerializableExtra("miniknowledgelists");
		
		selectTagKTV = (KnoTagGroupView) findViewById(R.id.add_tag_gl);
		allTagGV = (GridView) findViewById(R.id.kno_tag_gv);
		addTagEt = (EditText) findViewById(R.id.et_add_tag);
		// addTagEt = addEditText();
		// selectTagKTV.addView(addTagEt);
		knotageditLl = (LinearLayout) findViewById(R.id.kno_tag_edit_ll);

		adapter = new KnoTagsAdapter();
		allTagGV.setAdapter(adapter);
		allTagGV.setOnItemClickListener(this);

		knotageditLl.setOnClickListener(this);
		// 限制输入长度
		addTagEt.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
		// 监听输入完成
		addTagEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					// do something;
					// 完成输入
					final String newtag = addTagEt.getText().toString();
					if (imm.isActive()) {
						imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
					}
					if (newtag.length() > 10) {
						showToast("标签不大于20个字符！");
					} else if (TextUtils.isEmpty(newtag)) {
						showToast("您输入的标签为空");

					} else if (allTags.contains(newtag)) {
						showToast("您输入的标签已存在");

					} else {
						final TextView newTagTv = creatTagTextView(newtag, true);// 创建TextView
						newTagTv.setTag(newtag);
						allTags.add(newtag);
						selectTags.add(newtag);
						adapter.notifyDataSetChanged();
						selectTagKTV.addView(newTagTv, selectTags.size() - 1);
						newTagTv.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								selectTags.remove(newtag);
								adapter.notifyDataSetChanged();
								selectGroupremoveView(newtag);
							}
						});
						addTagEt.setText("");
					}
					return true;
				}
				return false;
			}
		});
	
	}

	@Override
	public void onClick(View v) {
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	class KnoTagsAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return allTags.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = View.inflate(context, R.layout.gridview_item_tags, null);
			}
			TextView view = (TextView) convertView.findViewById(R.id.gv_item_tv);
			view.setPadding(10, 2, 10, 2);
			String stringtag = allTags.get(position);
			view.setText(stringtag);
			if (selectTags.contains(stringtag)) {// 如果当前标签在选中的标签集合中，就给他设置tag,并且将textView的背景颜色改变
				view.setTag(stringtag);
				view.setBackgroundResource(R.drawable.kno_myall_tag);
				view.setTextColor(getResources().getColor(R.color.common_orange));
			} else {
				view.setTag("");
				view.setBackgroundResource(R.drawable.kno_all_tag);
				view.setTextColor(getResources().getColor(R.color.black));
			}
			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TextView tagtv = (TextView) view.findViewById(R.id.gv_item_tv);
		final String tag = allTags.get(position);// 获取TextView的Tag
		TextView newTagTv = null;

		if (selectTags.contains(tag)) {
			// 响应点击事件
			selectTags.remove(tag);
			// 更新数据
			adapter.notifyDataSetChanged();
			// 将VIew从cg中移出
			selectTagKTV.removeView(selectTagKTV.findViewWithTag(tag));
		} else {// 标签未选择
			// 点击事件
			selectTags.add(tag);
			// 更新适配器
			adapter.notifyDataSetChanged();
			newTagTv = creatTagTextView(tag, true);
			newTagTv.setTag(tag);
			selectTagKTV.addView(newTagTv, selectTags.size() - 1);
			newTagTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					selectTags.remove(tag);
					adapter.notifyDataSetChanged();
					selectGroupremoveView(tag);
				}
			});
		}
	}

	private void selectGroupremoveView(String str) {
		for (int i = 0; i < selectTagKTV.getChildCount(); i++) {
			TextView childtv = (TextView) selectTagKTV.getChildAt(i);
			String cgChildStr = childtv.getText().toString();
			if (cgChildStr.equals(str)) {
				childtv.setTag(str);
			}
		}
		selectTagKTV.removeView(selectTagKTV.findViewWithTag(str));
	}

	/**
	 * 在selectTagKTV中创建新的TextView
	 * 
	 * @param tag
	 * @param flag
	 * @return
	 */
	private TextView creatTagTextView(String str, boolean flag) {
		TextView newTagTv = new TextView(context);
		newTagTv.setText(str);
		newTagTv.setSingleLine(true);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		// int dip = Util.DensityUtil.px2dip(context, 24);
		lp.setMargins(8, 8, 8, 8);
		newTagTv.setPadding(10, 2, 10, 2);
		newTagTv.setLayoutParams(lp);
		newTagTv.setGravity(Gravity.CENTER);
		changTagStyle(newTagTv, flag);
		return newTagTv;
	}

	/**
	 * 改变标签背景颜色
	 * 
	 * @param view
	 * @param flag
	 *            true:选中
	 */
	private void changTagStyle(TextView view, boolean flag) {
		if (flag) {
			view.setBackgroundResource(R.drawable.kno_my_tag);
			view.setTextColor(getResources().getColor(R.color.common_orange));
		} else {
			view.setBackgroundResource(R.drawable.kno_select_tag);
			view.setTextColor(Color.BLACK);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.knowledge_tag_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(addTagEt.getApplicationWindowToken(), 0);
		}
		switch (item.getItemId()) {
		// 完成，返回前页
		case R.id.edit_finish:
			showLoadingDialog();
			String endTag = addTagEt.getText().toString();
			if (!endTag.isEmpty()) {
				selectTags.add(endTag);
			}
			ArrayList<Long> listKnowledgeId = new ArrayList<Long>();
			ArrayList<Integer> listKnowledgeType = new ArrayList<Integer>();
			ArrayList<String> listKnowledgeTag = new ArrayList<String>();
			if (miniKnowledgeLists != null && !miniKnowledgeLists.isEmpty()) {
				mini2Size = miniKnowledgeLists.size();
				for (KnowledgeMini2 mini2 : miniKnowledgeLists) {
					listKnowledgeId.add(mini2.id);
					listKnowledgeType.add(mini2.type);
					listKnowledgeTag.addAll(selectTags);
					KnowledgeReqUtil.doEditTagByKnoId(context, this, App.getUserID(), listKnowledgeId, listKnowledgeType, listKnowledgeTag, null);
					listKnowledgeId.clear();
					listKnowledgeType.clear();
					listKnowledgeTag.clear();
					mini2Size--;
				}
			}

			showToast("编辑完成");

			setResult(RESULT_OK, null);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private String getBlankString(ArrayList<String> list) {
		StringBuffer sb = new StringBuffer();
		for (String string : list) {
			sb.append(string + " ");
		}
		return sb.toString();

	}

	private ArrayList<String> getAtoB(ArrayList<String> aa, ArrayList<String> bb) {
		StringBuffer sBuffer = new StringBuffer();
		ArrayList<String> tmp = new ArrayList<String>();
		for (String string : bb) {
			tmp.add(string + " " + getBlankString(aa));
		}
		return tmp;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		switch (tag) {
		// 获取全部知识标签
		case KnoReqType.GetKnowledgeTagList:
			if (object != null) {
				Map<String, Object> dataMap = (Map<String, Object>) object;
				List<Integer> listCount = (List<Integer>) dataMap.get("listCount");
				ArrayList<String> tmpTags = (ArrayList<String>) dataMap.get("listTag");
				allTags = FileUtils.deleteRep(tmpTags);
				if (null != miniKnowledgeLists && !miniKnowledgeLists.isEmpty()) {
					for (KnowledgeMini2 mini : miniKnowledgeLists) {
						if (null!=mini.listTag&&!mini.listTag.isEmpty()){
							for(String mTag:mini.listTag){
								// 更新适配器
//								adapter.notifyDataSetInvalidated();
								if(!TextUtils.isEmpty(mTag)){
								selectTags.add(mTag);
									TextView newTagTv = creatTagTextView(mTag, true);
									newTagTv.setTag(mTag);
									selectTagKTV.addView(newTagTv, selectTags.size() - 1);
									newTagTv.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											TextView text=(TextView) v;
											String str=(String) text.getTag();
											selectTags.remove(str);
											adapter.notifyDataSetChanged();
											selectGroupremoveView(str);
										}
									});	
								}
							}
						
						}
					}
				}
				adapter.notifyDataSetChanged();
			}
			break;
		// 编辑后返回的结果
		case KnoReqType.EditKnoTagByKnoId:
			if (object != null) {
				boolean b = (Boolean) object;
				if (b) {
					if (mini2Size == 0) {
						// showToast("编辑完成");
					}
				} else {
					// showToast("编辑失败");
				}
			}
			break;
		default:
			break;
		}
	}

}
