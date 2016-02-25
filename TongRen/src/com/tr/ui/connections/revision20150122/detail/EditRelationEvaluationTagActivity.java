package com.tr.ui.connections.revision20150122.detail;

import java.util.ArrayList;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.obj.UserComment;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.MainActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.EditTextAlertDialog;
import com.tr.ui.widgets.EditTextAlertDialog.OnEditDialogClickListener;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/***
 * 编辑评价标签
 * 
 * 
 * @author zhongshan
 * @since 2015-01-19
 * 
 */
public class EditRelationEvaluationTagActivity extends JBaseFragmentActivity implements OnClickListener, OnItemClickListener, IBindData {

	private TextView evaluationAddTagTv;
	private ListView evaluationTagLv;

	/** 用户评价列表 */
	private ArrayList<UserComment> userCommentlists;
	private ArrayList<String> tags;
	private EvaluationTagAdapter evaluationTagAdapter;
	private EditTextAlertDialog editTextAlertDialog;

	private String addNewTag;
	private int thispotion;
	
	private String userId;
	private int type;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "编辑评价", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initControls();
	}

	private void initView() {
		setContentView(R.layout.activity_relation_evaluationtag);
		evaluationAddTagTv = (TextView) findViewById(R.id.evaluationAddTagTv);
		evaluationTagLv = (ListView) findViewById(R.id.evaluationlv);
	}

	private void initControls() {
		Intent intentDatas = getIntent();
		userId = intentDatas.getStringExtra(EConsts.Key.ID);
		type = intentDatas.getIntExtra(EConsts.Key.TYPE, 1);
		// 获取到所有自己的评价
		if(TextUtils.isEmpty(userId)){
			userId = App.getUserID();
		}
		ConnectionsReqUtil.doFindEvaluate(EditRelationEvaluationTagActivity.this, this, Long.valueOf(userId), null, type);

		if (userCommentlists == null) {
			userCommentlists = new ArrayList<UserComment>();
		}

		evaluationAddTagTv.setOnClickListener(this);
		evaluationTagLv.setOnItemClickListener(this);

		evaluationTagAdapter = new EvaluationTagAdapter();
		evaluationTagLv.setAdapter(evaluationTagAdapter);

	}

	@Override
	public void onClick(View v) {
		editTextAlertDialog = new EditTextAlertDialog(this);
		editTextAlertDialog.show();
		editTextAlertDialog.setOnDialogClickListener(new OnEditDialogClickListener() {

			@Override
			public void onClick(String evaluationValue) {
				if (TextUtils.isEmpty(evaluationValue.trim())) {
					return;
				}
				if (tags.contains(evaluationValue.trim())) {
					showToast("标签已存在");
				} else {
					addNewTag = evaluationValue;
					ConnectionsReqUtil.doAddEvaluate(EditRelationEvaluationTagActivity.this, EditRelationEvaluationTagActivity.this, Long.valueOf(userId), evaluationValue, null, type);
					showLoadingDialog();
				}
			}
		});
	}

	private ArrayList<String> getComments(ArrayList<UserComment> userCommentlists) {
		ArrayList<String> comments = new ArrayList<String>();
		for (UserComment userComment : userCommentlists) {
			if (userComment != null && !TextUtils.isEmpty(userComment.getUserComment())) {
				comments.add(userComment.getUserComment());
			}
		}
		return comments;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	class EvaluationTagAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userCommentlists.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(EditRelationEvaluationTagActivity.this, R.layout.relation_evalutiontag_item, null);
				viewHolder = new ViewHolder();
				viewHolder.evalutionTagTv = (TextView) convertView.findViewById(R.id.evaluationtagcontent);
				viewHolder.evaluationDeleteTv = (ImageView) convertView.findViewById(R.id.evaluationtagdelete);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.evalutionTagTv.setText(userCommentlists.get(position).getUserComment());
			viewHolder.evaluationDeleteTv.setTag(position);
			viewHolder.evaluationDeleteTv.setOnClickListener(new OnClickListener() {

				

				@Override
				public void onClick(View v) {
					thispotion = (Integer) v.getTag();
//					showToast("删除标签");
					ConnectionsReqUtil.doDeleteEvaluate(EditRelationEvaluationTagActivity.this, EditRelationEvaluationTagActivity.this, userCommentlists.get(thispotion).getId(), null, type);

					// userCommentlists.remove(position);
					// notifyDataSetChanged();
				}
			});

			return convertView;
		}

		class ViewHolder {
			/** 职业标签 */
			TextView evalutionTagTv;
			/** 删除职业标签 */
			ImageView evaluationDeleteTv;
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		// 获取全部知识标签
		case EAPIConsts.concReqType.findEvaluate:
			dismissLoadingDialog();
			if (object != null && !object.equals("")) {
				ArrayList arr = (ArrayList) object;
				userCommentlists = (ArrayList<UserComment>) arr.get(1);
				tags = getComments(userCommentlists);
				evaluationTagAdapter.notifyDataSetChanged();
			}
			break;
		case EAPIConsts.concReqType.addEvaluate:
			dismissLoadingDialog();
			if (object != null) {
				ArrayList arr = (ArrayList) object;
				String b = (String) arr.get(0);
				if (b.equals("true")) {
					isChange = true;
					Long id = (Long) arr.get(1);
					tags.add(addNewTag);
					UserComment userComment = new UserComment();
					userComment.setId(id);
					userComment.setUserComment(addNewTag);
					userCommentlists.add(userComment);
					evaluationTagAdapter.notifyDataSetChanged();
				}
			}
			break;
		case EAPIConsts.concReqType.deleteEvaluate:
			dismissLoadingDialog();
			String str = (String) object;
			if (str.equals("true")) {
				 isChange = true;
				 userCommentlists.remove(thispotion);
				 evaluationTagAdapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}

	private boolean isChange;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent();
			intent.putExtra("isChange", isChange);
			setResult(RESULT_OK, intent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.putExtra("isChange", isChange);
			setResult(RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
