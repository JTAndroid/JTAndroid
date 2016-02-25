package com.tr.ui.organization.orgfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.model.obj.ApproverMiNi;
import com.tr.model.obj.CommentApprover;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.CircleImageView;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

/**
 * 客户更多评价
 * @author User  huangzhiyu
 *
 */
public class OrgClientMoreEvaluationActivity extends JBaseFragmentActivity implements IBindData {

	private ListView moreEvaluationLv; 

	/** 组织评价列表 */
	private ArrayList<CommentApprover> commentApproverLists;
	private MoreEvaluationLvAdapter mAdapter;
	private ApproverAdapter gvAdapter;
	/**true: 展开;false:收起*/
	private Map<Integer, Boolean> showAllMap = new HashMap<Integer, Boolean>();
	private long client_id;
	private int APPROVER_SHOW_LIMIT = 6;

	private final int FEEDBACK_STATE_NORMAL = 1001;
	private final int FEEDBACK_STATE_AGGRE = 1002;
	private final int FEEDBACK_STATE_CANCEL = 1003;
	private int feedBackState = FEEDBACK_STATE_NORMAL;

	private boolean isShowAll = false;
	private Context context;
	
	private Map<String, Object> map;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(OrgClientMoreEvaluationActivity.this, getActionBar(), " 评价人", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = OrgClientMoreEvaluationActivity.this;
		initData();
		initView();
		initControls();
	}

	private void initData() {
		
		Intent intentDatas = getIntent();
		client_id = intentDatas.getLongExtra(EConsts.Key.ID, 1);
		//获得更多评价
		OrganizationReqUtil.doGetMoreEvaluate(OrgClientMoreEvaluationActivity.this, this, client_id,"2", null);
		showLoadingDialog();
	}

	private void initView() {
		setContentView(R.layout.activity_client_moreevaluation);
		moreEvaluationLv = (ListView) findViewById(R.id.client_expandableLv);
	}

	private void initControls() {
		commentApproverLists = new ArrayList<CommentApprover>();
	}

	private CommentApprover commentApprover;

	class MoreEvaluationLvAdapter extends BaseAdapter implements OnClickListener {

		@Override
		public int getCount() {
			return commentApproverLists.size();
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
			final CommentApprover commentApprovers = commentApproverLists.get(position);
			ArrayList<ApproverMiNi> approverList = commentApprovers.getApprovers();
			final ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(OrgClientMoreEvaluationActivity.this, R.layout.client_relation_evaluation_lv_item, null);
				viewHolder = new ViewHolder();
				viewHolder.countTv = (TextView) convertView.findViewById(R.id.client_countTv);
				viewHolder.evaluationTv = (TextView) convertView.findViewById(R.id.clien_evaluationTv);
				viewHolder.favoriteIv = (ImageView) convertView.findViewById(R.id.client_favoriteIv);
				viewHolder.imageGv = (GridView) convertView.findViewById(R.id.client_imageGv);
				viewHolder.showAllTv = (TextView) convertView.findViewById(R.id.client_showAllTv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (commentApprovers.isFeedback()) {
				viewHolder.favoriteIv.setBackgroundResource(R.drawable.heart_press);
			} else {
				viewHolder.favoriteIv.setBackgroundResource(R.drawable.heart_commen);
			}
			viewHolder.favoriteIv.setOnClickListener(MoreEvaluationLvAdapter.this);
			viewHolder.favoriteIv.setTag(position);

			if (showAllMap.get(position) == false) {// 收起状态:只显示APPROVER_SHOW_LIMIT个
				if (approverList.size() > APPROVER_SHOW_LIMIT) {// 大于规定数量-->限制
					ArrayList<ApproverMiNi> approverLimitList = new ArrayList<ApproverMiNi>();
					for (int i = 0; i < APPROVER_SHOW_LIMIT; i++) {
						approverLimitList.add(approverList.get(i));
					}
					gvAdapter = new ApproverAdapter(approverLimitList, position);
					viewHolder.imageGv.setAdapter(gvAdapter);
					viewHolder.showAllTv.setVisibility(View.VISIBLE);
				}
//				else {// 小于规定数量-->不限制.且隐藏底部按钮
//					gvAdapter = new ApproverAdapter(approverList, position);
//					viewHolder.imageGv.setAdapter(gvAdapter);
//					viewHolder.showAllTv.setVisibility(View.GONE);
//				}
			} else {// 展开状态:不限制
				gvAdapter = new ApproverAdapter(approverList, position);
				viewHolder.imageGv.setAdapter(gvAdapter);
				viewHolder.showAllTv.setVisibility(View.GONE);
			}
			if (approverList.size() > APPROVER_SHOW_LIMIT) {
				viewHolder.showAllTv.setVisibility(View.VISIBLE);
			}else {
				viewHolder.showAllTv.setVisibility(View.GONE);
			}
			viewHolder.showAllTv.setOnClickListener(MoreEvaluationLvAdapter.this);
			viewHolder.showAllTv.setTag(position);
			viewHolder.countTv.setText(approverList.size() + "  位好友已赞同");
			Spannable span = new SpannableString(viewHolder.countTv.getText());  
			span.setSpan(new ForegroundColorSpan(Color.BLACK), String.valueOf(approverList.size()).length(),String.valueOf(approverList.size()).length()+8 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			viewHolder.countTv.setText(span);
			viewHolder.evaluationTv.setText("  \"" + commentApprovers.getComment() + "\"");

			return convertView;
		}

		class ViewHolder {
			/** 评价内容 */
			TextView evaluationTv;
			/** 评价数量 */
			TextView countTv;
			/** 赞同按钮 */
			ImageView favoriteIv;
			/** 头像GridView */
			GridView imageGv;
			/** 显示更多按钮 */
			TextView showAllTv;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.client_favoriteIv:
				dismissLoadingDialog();
				int position = (Integer) v.getTag();
				commentApprover = commentApproverLists.get(position);
				if (commentApprover.isFeedback()) {// 赞同状态-->取消赞同
					// TODO:取消赞同
					OrganizationReqUtil.doFeedbackEvaluate(OrgClientMoreEvaluationActivity.this, OrgClientMoreEvaluationActivity.this, client_id,commentApprover.getUeid(), false,"2", null);
					showLoadingDialog();
//					Toast.makeText(getApplicationContext(), "取消第" + position, 0).show();
					feedBackState = FEEDBACK_STATE_CANCEL;
				} else {
					// :TODO:赞同
					OrganizationReqUtil.doFeedbackEvaluate(OrgClientMoreEvaluationActivity.this, OrgClientMoreEvaluationActivity.this, client_id,commentApprover.getUeid(), true,"2", null);
					showLoadingDialog();
//					Toast.makeText(getApplicationContext(), "赞同第" + position, 0).show();
					feedBackState = FEEDBACK_STATE_AGGRE;
				}
				break;
			case R.id.client_showAllTv:
				dismissLoadingDialog();
				int thisPosition = (Integer) v.getTag();
				if (showAllMap.get(thisPosition)) {// 展开状态-->收起
					((TextView) v).setText("展开全部  ");
					Drawable nav_down =getResources().getDrawable(R.drawable.relation_evaluation_showmorele);  
					nav_down.setBounds(0, 0, nav_down.getMinimumWidth(), nav_down.getMinimumHeight());  
					((TextView) v).setCompoundDrawables(null, null, nav_down, null);
					ArrayList<ApproverMiNi> approverLimitList = new ArrayList<ApproverMiNi>();
					for (int i = 0; i < APPROVER_SHOW_LIMIT; i++) {
						approverLimitList.add(commentApproverLists.get(thisPosition).getApprovers().get(i));
					}
					gvAdapter = new ApproverAdapter(approverLimitList, thisPosition);
					showAllMap.put(thisPosition, false);
					gvAdapter.notifyDataSetChanged();
					mAdapter.notifyDataSetChanged();
				} else {// 收起状态-->展开
					((TextView) v).setText("收起 ");
					Drawable nav_up=getResources().getDrawable(R.drawable.relation_evaluation_showlittle);  
					nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());  
					((TextView) v).setCompoundDrawables(null, null, nav_up, null);
					gvAdapter = new ApproverAdapter(commentApproverLists.get(thisPosition).getApprovers(), thisPosition);
					showAllMap.put(thisPosition, true);
					gvAdapter.notifyDataSetChanged();
					mAdapter.notifyDataSetChanged();
				}

				break;

			default:
				break;
			}
		}

	}

	/** 赞同人头像adapter */
	class ApproverAdapter extends BaseAdapter {

		private ArrayList<ApproverMiNi> approverList;
		private int parentposition;

		public ApproverAdapter(ArrayList<ApproverMiNi> approverList, int parentposition) {
			this.approverList = approverList;
			this.parentposition = parentposition;
		}

		@Override
		public int getCount() {
			return approverList.size();
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
			convertView = View.inflate(OrgClientMoreEvaluationActivity.this, R.layout.client_evaluation_approver_item, null);
			CircleImageView imageCv = (CircleImageView) convertView.findViewById(R.id.client_appimageCv);
			// imageCv.setImageResource(approverList.get(position));
			ImageLoader.getInstance().displayImage(approverList.get(position).getImageUrl(), imageCv);
			imageCv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					Toast.makeText(RelationMoreEvaluationActivity.this, "您点击了第" + parentposition + "item的第" + position + "个", 0).show();
					if (approverList.get(position).isOnline()) {
						ENavigate.startOrgMyHomePageActivityByUseId(context, approverList.get(position).getUserId());
					}else{
						ENavigate.startRelationHomeActivity(context, approverList.get(position).getUserId()+"");
					}
					
				
//					ENavigate.startOrgMyHomePageActivity(context, String.valueOf(approverList.get(position).getUserId()), true, ENavConsts.type_details_other, "我的主页");
				}
			});
			return convertView;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.OrganizationReqType.FIND_MORE_EVALUATE) {
			dismissLoadingDialog();
			if (object != null && !object.equals("")) {
				map = (Map<String, Object>) object;
				ArrayList<CommentApprover> commentApproverlists = (ArrayList<CommentApprover>) map.get("customerApproverList");
				
				commentApproverLists = commentApproverlists;
				for (int i = 0; i < commentApproverLists.size(); i++) {
					if (commentApproverLists.get(i).getApprovers().size()>APPROVER_SHOW_LIMIT) {
						showAllMap.put(i, false);
					}else {
						showAllMap.put(i, true);
					}
				}
				mAdapter = new MoreEvaluationLvAdapter();
				moreEvaluationLv.setAdapter(mAdapter);
			}
		} else if (tag == EAPIConsts.OrganizationReqType.FEEDBACK_EVALUATE) {
			dismissLoadingDialog();
			if (object != null) {
				
				map = (Map<String, Object>) object;
				
				boolean flag = (Boolean) map.get("FLAG");
				if (flag) {

					if (feedBackState == FEEDBACK_STATE_CANCEL) {
						commentApprover.setFeedback(false);
						ArrayList<ApproverMiNi> approvers = commentApprover.getApprovers();
						int removeIndex = -1;
						for (int i = 0; i < approvers.size(); i++) {
							if (App.getUserID().equals(approvers.get(i).getUserId() + "")) {
								removeIndex = i;
							}
						}
						if (removeIndex != -1) {
							approvers.remove(removeIndex);
							mAdapter.notifyDataSetChanged();
							ToastUtil.showToast(context, "已取消");
						}
					} else if (feedBackState == FEEDBACK_STATE_AGGRE) {
						commentApprover.setFeedback(true);
						ApproverMiNi meApproverMiNi = new ApproverMiNi();
						meApproverMiNi.setImageUrl(App.getUser().getImage());
						meApproverMiNi.setOnline(true);
						meApproverMiNi.setUserId(Long.valueOf(App.getUserID()));
						commentApprover.getApprovers().add(meApproverMiNi);
						mAdapter.notifyDataSetChanged();
						ToastUtil.showToast(context, "已赞同");
					}

				}else{
					
				}
			}

		}
	}

}
