package com.tr.ui.people.contactsdetails;

import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.tr.api.PeopleReqUtil;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.model.UserCommentList;
import com.tr.ui.people.model.params.AddEvaluateParams;
import com.tr.ui.people.model.params.ContactsDeleteEvaluateLableParams;
import com.tr.ui.people.model.params.FindEvaluateParams;
import com.tr.ui.people.model.success.AddBooleanSuccess;
import com.tr.ui.people.model.success.ContactsDeleteEvaluateLableParamsSuccess;
import com.tr.ui.widgets.EditTextAlertDialog;
import com.tr.ui.widgets.EditTextAlertDialog.OnEditDialogClickListener;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;

/***
 * 组织编辑评价标签
 * 
 * @author zhongshan
 * @since 2015-01-19
 * 
 */
public class ContactsEditRelationEvaluationTagActivity extends JBaseFragmentActivity implements OnClickListener, OnItemClickListener, IBindData {

	private TextView evaluationAddTagTv;
	private ListView evaluationTagLv;

	/** 用户评价列表 */
	private ArrayList<UserCommentList.UserComment> userCommentlists;
	private ArrayList<String> tags;
	private EvaluationTagAdapter evaluationTagAdapter;
	private EditTextAlertDialog editTextAlertDialog;

	private String addNewTag;
	private int thispotion;
	
	private Map<String, Object> map;
	
	private String[] types = {"1","2"};//"1":组织   "2":客户
	
	private long id;

	@Override
	public void initJabActionBar() {
//		getActionBar().setTitle("编辑评价");
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "编辑评价", false, null, false, true);
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
		Log.v("BJ", "获取到所有自己的评价");
		id = getIntent().getLongExtra("id", -1);
		
		// 获取到所有自己的评价
		FindEvaluateParams findEvaluateParams = new FindEvaluateParams();
		findEvaluateParams.userId = id;
		Log.v("BJ", "id---->"+id);
		findEvaluateParams.isSelf = false;
		//获取评价列表数据
		PeopleReqUtil.doRequestWebAPI(ContactsEditRelationEvaluationTagActivity.this, this, findEvaluateParams, null, PeopleRequestType.PEOPLE_REQ_FINDEVALUTE);
			userCommentlists = new ArrayList<UserCommentList.UserComment>();
			tags = new ArrayList<String>();
			
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
				if (TextUtils.isEmpty(evaluationValue)) {
					return;
				}
				if (tags!=null) {
					//去重操作
					if (tags.contains(evaluationValue)) {
						showToast("标签已存在");
					} else {
						addNewTag = evaluationValue;
						AddEvaluateParams addEvaluateParams = new AddEvaluateParams();
						addEvaluateParams.comment = evaluationValue;
						addEvaluateParams.userId = id;
						//添加评价
						PeopleReqUtil.doRequestWebAPI(ContactsEditRelationEvaluationTagActivity.this, ContactsEditRelationEvaluationTagActivity.this, addEvaluateParams, null, PeopleRequestType.PEOPLE_REQ_ADDEVALUATE);
						showLoadingDialog();
					}
				}
				
			}
		});
	}

	private ArrayList<String> getComments(ArrayList<UserCommentList.UserComment> userCommentlists) {
		ArrayList<String> comments = new ArrayList<String>();
			
		for (UserCommentList.UserComment userComment : userCommentlists) {
			if (userComment != null && !TextUtils.isEmpty(userComment.userComment)) {
				comments.add(userComment.userComment);
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
				return userCommentlists !=null ?userCommentlists.size():0;
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
				convertView = View.inflate(ContactsEditRelationEvaluationTagActivity.this, R.layout.relation_evalutiontag_item, null);
				viewHolder = new ViewHolder();
				viewHolder.evalutionTagTv = (TextView) convertView.findViewById(R.id.evaluationtagcontent);
				viewHolder.evaluationDeleteTv = (ImageView) convertView.findViewById(R.id.evaluationtagdelete);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.evalutionTagTv.setText(userCommentlists.get(position).userComment);
			viewHolder.evaluationDeleteTv.setTag(position);
			viewHolder.evaluationDeleteTv.setOnClickListener(new OnClickListener() {

				

				@Override
				public void onClick(View v) {
					thispotion = (Integer) v.getTag();
//					showToast("删除标签");
					ContactsDeleteEvaluateLableParams contactsDeleteEvaluateLableParams = new ContactsDeleteEvaluateLableParams();
					contactsDeleteEvaluateLableParams.id = userCommentlists.get(thispotion).id;
					
					PeopleReqUtil.doRequestWebAPI(ContactsEditRelationEvaluationTagActivity.this, ContactsEditRelationEvaluationTagActivity.this, contactsDeleteEvaluateLableParams, null,PeopleRequestType.PEOPLE_REQ_DELETEEVALUATE);
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
		// 获取该用户的评价
		case PeopleRequestType.PEOPLE_REQ_FINDEVALUTE:
			dismissLoadingDialog();
			if (object != null && !object.equals("")) {
				UserCommentList commentLists = (UserCommentList) object;

				Log.v("BJ", "获取编辑评价---->" + commentLists);

				if(commentLists.listUserComment!=null){
					userCommentlists = (ArrayList<UserCommentList.UserComment>) commentLists.listUserComment;
					if (!userCommentlists.isEmpty()) {
						tags = getComments(userCommentlists);
						evaluationTagAdapter.notifyDataSetChanged();
					}
						
				}
				
			}
			break;
		case PeopleRequestType.PEOPLE_REQ_ADDEVALUATE://添加评价
			dismissLoadingDialog();
			if (object != null) {
				
				AddBooleanSuccess addBooleanSuccess = (AddBooleanSuccess) object;
				boolean flag = addBooleanSuccess.success;
				if (flag) {
					Long id = addBooleanSuccess.id;
					tags.add(addNewTag);
					UserCommentList.UserComment userComment = new UserCommentList().new UserComment();
					userComment.id = id;
					userComment.userComment = addNewTag;
					if (userCommentlists!=null) {
						userCommentlists.add(userComment);
						evaluationTagAdapter.notifyDataSetChanged();
					}
				}
			}
			break;
		case PeopleRequestType.PEOPLE_REQ_DELETEEVALUATE://删除评价标签
			dismissLoadingDialog();
			
			if(object != null){
				ContactsDeleteEvaluateLableParamsSuccess deleteEvaluateLableParamsSuccess = (ContactsDeleteEvaluateLableParamsSuccess) object;
				boolean isFlag = deleteEvaluateLableParamsSuccess.success;
				
				if (isFlag) {
					 userCommentlists.remove(thispotion);
					 evaluationTagAdapter.notifyDataSetChanged();
				}
			}
			break;
		default:
			break;
		}
	}

}
