package com.tr.ui.organization.orgdetails;

import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.organization.model.evaluate.CustomerEvaluate;
import com.tr.ui.widgets.EditTextAlertDialog;
import com.tr.ui.widgets.EditTextAlertDialog.OnEditDialogClickListener;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

public class ClientEditRelationEvaluationTagActivity extends JBaseFragmentActivity implements OnClickListener, OnItemClickListener, IBindData {

	private TextView evaluationAddTagTv;
	private ListView evaluationTagLv;

	/** 用户评价列表 */
	private ArrayList<CustomerEvaluate> userCommentlists;
	private ArrayList<String> tags;
	private EvaluationTagAdapter evaluationTagAdapter;
	private EditTextAlertDialog editTextAlertDialog;

	private String addNewTag;
	private int thispotion;
	
	private Map<String, Object> map;
	
	private String[] types = {"1","2"};//"1":组织   "2":客户

	private long clientId;
	
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), " 编辑评价", false, null, false, true);
//		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initControls();
	}

	private void initView() {
		setContentView(R.layout.activity_client_editevaluationtag);
		evaluationAddTagTv = (TextView) findViewById(R.id.client_evaluationAddTagTv);
		evaluationTagLv = (ListView) findViewById(R.id.client_evaluationlv);
	}

	private void initControls() {
		
		clientId = getIntent().getLongExtra(EConsts.Key.CLIENTID, -1); 
		// 获取到所有自己的评价
		OrganizationReqUtil.doFindEvaluate(ClientEditRelationEvaluationTagActivity.this, this, clientId, false,types[1], null);
		if (userCommentlists == null) {
			userCommentlists = new ArrayList<CustomerEvaluate>();
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
				if (TextUtils.isEmpty(evaluationValue)) {
					return;
				}
				if (tags.contains(evaluationValue)) {
					ToastUtil.showToast(ClientEditRelationEvaluationTagActivity.this, "标签已存在");
				} else {
					addNewTag = evaluationValue;
					OrganizationReqUtil.doAddEvaluate(ClientEditRelationEvaluationTagActivity.this, ClientEditRelationEvaluationTagActivity.this, clientId, evaluationValue,types[1], null);
					showLoadingDialog();
				}
			}
		});
	}

	private ArrayList<String> getComments(ArrayList<CustomerEvaluate> userCommentlists) {
		ArrayList<String> comments = new ArrayList<String>();
		for (CustomerEvaluate customerEvaluate : userCommentlists) {
			if (customerEvaluate != null && !TextUtils.isEmpty(customerEvaluate.userComment)) {
				comments.add(customerEvaluate.userComment);
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
				convertView = View.inflate(ClientEditRelationEvaluationTagActivity.this, R.layout.client_evalutiontag_item, null);
				viewHolder = new ViewHolder();
				viewHolder.evalutionTagTv = (TextView) convertView.findViewById(R.id.client_evaluationtagcontent);
				viewHolder.evaluationDeleteTv = (ImageView) convertView.findViewById(R.id.client_evaluationtagdelete);
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
					OrganizationReqUtil.doDeleteEvaluate(ClientEditRelationEvaluationTagActivity.this, ClientEditRelationEvaluationTagActivity.this, userCommentlists.get(thispotion).id,types[1],null );
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
		case EAPIConsts.OrganizationReqType.GET_ORG_HOME_Evaluate:
			dismissLoadingDialog();
			if (object != null && !object.equals("")) {
				ArrayList arr = (ArrayList) object;
				userCommentlists = (ArrayList<CustomerEvaluate>) arr.get(1);
				tags = getComments(userCommentlists);
				evaluationTagAdapter.notifyDataSetChanged();
			}
			break;
		case EAPIConsts.OrganizationReqType.ADD_ORG_HOME_Evaluate://添加评价
			dismissLoadingDialog();
			if (object != null) {
				
				map = (Map<String, Object>) object;
				boolean flag = (Boolean) map.get("success");
				if (flag) {
					Long id = (Long) map.get("ID");
					tags.add(addNewTag);
					CustomerEvaluate customerEvaluate = new CustomerEvaluate();
					customerEvaluate.id = id;
					customerEvaluate.userComment = addNewTag;
					userCommentlists.add(customerEvaluate);
					evaluationTagAdapter.notifyDataSetChanged();
				}
			}
			break;
		case EAPIConsts.OrganizationReqType.DELETE_EVALUATE://删除评价标签
			dismissLoadingDialog();
			
			if(object != null){
				map = (Map<String, Object>) object;
				boolean isFlag = (Boolean) map.get("success");
				
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
