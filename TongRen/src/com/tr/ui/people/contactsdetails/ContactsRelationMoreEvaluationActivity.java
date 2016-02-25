package com.tr.ui.people.contactsdetails;

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
import android.util.Log;
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
import com.tr.api.PeopleReqUtil;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.model.CommentApproverLists.ApproverMiNi;
import com.tr.ui.people.model.CommentApproverLists.CommentApprover;
import com.tr.ui.people.model.CommentApproverLists.CommentApproverLists;
import com.tr.ui.people.model.params.AgreeToCancel;
import com.tr.ui.people.model.params.FindMoreEvaluateParams;
import com.tr.ui.people.model.success.AgreeToCancleBoolean;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;

/***
 *人脉详情更多评价
 * 
 * @author zhongshan
 * @param <V>
 * @since 2015-01-15
 * 
 */
public class ContactsRelationMoreEvaluationActivity extends JBaseFragmentActivity implements IBindData {

	private ListView moreEvaluationLv; 

	/** 组织评价列表 */
	private ArrayList<CommentApprover> commentApproverLists;
	private MoreEvaluationLvAdapter mAdapter;
	private ApproverAdapter gvAdapter;
	/**true: 展开;false:收起*/
	private Map<Integer, Boolean> showAllMap = new HashMap<Integer, Boolean>();
	private long homeUserId;
	private int APPROVER_SHOW_LIMIT = 6;

	private final int FEEDBACK_STATE_NORMAL = 1001;
	private final int FEEDBACK_STATE_AGGRE = 1002;
	private final int FEEDBACK_STATE_CANCEL = 1003;
	private int feedBackState = FEEDBACK_STATE_NORMAL;

	private boolean isShowAll = false;
	private Context context;
	
	private Map<String, Object> map;
	//获取更多评价的数据类
	private CommentApproverLists moreEvaluateDataList;
	//获取赞同取消评价的响应类
	private AgreeToCancleBoolean agreeToCancleBoolean;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), " 评价人", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = ContactsRelationMoreEvaluationActivity.this;
		initData();
		initView();
		initControls();
	}

	private void initData() {
		
		Intent intentDatas = getIntent();
		
		homeUserId = intentDatas.getLongExtra(EConsts.Key.ID,-1);
		Log.v("ADD", "用户id"+homeUserId);
		FindMoreEvaluateParams findMoreEvaluateParams = new FindMoreEvaluateParams();
		findMoreEvaluateParams.userId = homeUserId;
		PeopleReqUtil.doRequestWebAPI(context, this, findMoreEvaluateParams, null, PeopleRequestType.PEOPLE_REQ_MOREEVALUATE);
		
		
//		OrganizationReqUtil.doGetMoreEvaluate(ContactsRelationMoreEvaluationActivity.this, this, Long.valueOf(homeUserId),"1", null);
		showLoadingDialog();
	}

	private void initView() {
		setContentView(R.layout.activity_evaluation);
		moreEvaluationLv = (ListView) findViewById(R.id.expandableLv);
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
			ArrayList<ApproverMiNi> approverList = commentApprovers.listApproverMiNi;
			final ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(ContactsRelationMoreEvaluationActivity.this, R.layout.relation_evaluation_listview_item, null);
				viewHolder = new ViewHolder();
				viewHolder.countTv = (TextView) convertView.findViewById(R.id.countTv);
				viewHolder.evaluationTv = (TextView) convertView.findViewById(R.id.evaluationTv);
				viewHolder.favoriteIv = (ImageView) convertView.findViewById(R.id.favoriteIv);
				viewHolder.imageGv = (GridView) convertView.findViewById(R.id.imageGv);
				viewHolder.showAllTv = (TextView) convertView.findViewById(R.id.showAllTv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (commentApprovers.feedback) {
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
			viewHolder.evaluationTv.setText("  \"" + commentApprovers.comment + "\"");

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
			case R.id.favoriteIv:
				dismissLoadingDialog();
				int position = (Integer) v.getTag();
				commentApprover = commentApproverLists.get(position);
				AgreeToCancel agreeToCancel = new AgreeToCancel();
				if (commentApprover.feedback) {// 赞同状态-->取消赞同
					// TODO:取消赞同
					agreeToCancel.id = commentApprover.ueid;
					agreeToCancel.feedback = false;
					PeopleReqUtil.doRequestWebAPI(ContactsRelationMoreEvaluationActivity.this, ContactsRelationMoreEvaluationActivity.this, agreeToCancel, null, PeopleRequestType.PEOPLE_REQ_FEEDBACKEVALUATE);
//					showLoadingDialog();
					showLoadingDialogWithoutOnCancelListener();
//					Toast.makeText(getApplicationContext(), "取消第" + position, 0).show();
					feedBackState = FEEDBACK_STATE_CANCEL;
				} else {
					// :TODO:赞同
					agreeToCancel.id = commentApprover.ueid;
					agreeToCancel.feedback = true;
					PeopleReqUtil.doRequestWebAPI(ContactsRelationMoreEvaluationActivity.this, ContactsRelationMoreEvaluationActivity.this, agreeToCancel, null, PeopleRequestType.PEOPLE_REQ_FEEDBACKEVALUATE);
//					showLoadingDialog();
					showLoadingDialogWithoutOnCancelListener();
//					Toast.makeText(getApplicationContext(), "赞同第" + position, 0).show();
					feedBackState = FEEDBACK_STATE_AGGRE;
				}
				break;
			case R.id.showAllTv:
				dismissLoadingDialog();
				int thisPosition = (Integer) v.getTag();
				if (showAllMap.get(thisPosition)) {// 展开状态-->收起
					((TextView) v).setText("展开全部  ");
					Drawable nav_down =getResources().getDrawable(R.drawable.relation_evaluation_showmorele);  
					nav_down.setBounds(0, 0, nav_down.getMinimumWidth(), nav_down.getMinimumHeight());  
					((TextView) v).setCompoundDrawables(null, null, nav_down, null);
					ArrayList<ApproverMiNi> approverLimitList = new ArrayList<ApproverMiNi>();
					for (int i = 0; i < APPROVER_SHOW_LIMIT; i++) {
						approverLimitList.add(commentApproverLists.get(thisPosition).listApproverMiNi.get(i));
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
					gvAdapter = new ApproverAdapter(commentApproverLists.get(thisPosition).listApproverMiNi, thisPosition);
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
		//点击人头像进入
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(ContactsRelationMoreEvaluationActivity.this, R.layout.relation_evaluation_approver_item, null);
			ImageView imageCv = (ImageView) convertView.findViewById(R.id.imageCv);
			// imageCv.setImageResource(approverList.get(position));
			ImageLoader.getInstance().displayImage(approverList.get(position).imageUrl, imageCv);
			imageCv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					Toast.makeText(RelationMoreEvaluationActivity.this, "您点击了第" + parentposition + "item的第" + position + "个", 0).show();
					ENavigate.startRelationHomeActivity(context, String.valueOf(approverList.get(position).userId), true, ENavConsts.type_details_other);
					//跳转到我的好友的人脉详情界面
//					Log.v("MSG", "approverList的id---->"+approverList.get(position).userId);
//					ENavigate.startContactsDetailsActivity(context, 1,approverList.get(position).userId,ENavConsts.type_details_member);
				}
			});
			return convertView;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		if (tag == PeopleRequestType.PEOPLE_REQ_MOREEVALUATE) {
			dismissLoadingDialog();
			if (object != null && !object.equals("")) {
				moreEvaluateDataList =  (CommentApproverLists) object;
				ArrayList<CommentApprover>  commentApproverlists = moreEvaluateDataList.listCommentApprover;
				Log.v("ADD", "获取更多评价接口返回的数据---->"+commentApproverlists.toString());
				commentApproverLists = commentApproverlists;
				for (int i = 0; i < commentApproverLists.size(); i++) {
					if (commentApproverLists.get(i)!=null&&commentApproverLists.get(i).listApproverMiNi!=null) {
						if (commentApproverLists.get(i).listApproverMiNi.size()>APPROVER_SHOW_LIMIT) {
							showAllMap.put(i, false);
						}else {
							showAllMap.put(i, true);
						}
					}
					
				}
				mAdapter = new MoreEvaluationLvAdapter();
				moreEvaluationLv.setAdapter(mAdapter);
			}
		} else if (tag == PeopleRequestType.PEOPLE_REQ_FEEDBACKEVALUATE) {
			dismissLoadingDialog();
			if (object != null) {
				
				agreeToCancleBoolean =  (AgreeToCancleBoolean) object;
				
				boolean flag = agreeToCancleBoolean.success;
				if (flag) {

					if (feedBackState == FEEDBACK_STATE_CANCEL) {
						commentApprover.feedback = false;
						ArrayList<ApproverMiNi> approvers = commentApprover.listApproverMiNi;
						if (approvers.size()<=1) {//此条评价删除
							commentApproverLists.remove(commentApprover);
							mAdapter.notifyDataSetChanged();
						}
						else {
							int removeIndex = -1;
							for (int i = 0; i < approvers.size(); i++) {
								if (App.getUserID().equals(approvers.get(i).userId + "")) {
									removeIndex = i;
								}
							}
							if (removeIndex != -1) {
								approvers.remove(removeIndex);
								mAdapter.notifyDataSetChanged();
//							showToast("已取消");
							}
						}
					} else if (feedBackState == FEEDBACK_STATE_AGGRE) {
						commentApprover.feedback = true;
						ApproverMiNi meApproverMiNi = new ApproverMiNi();
						meApproverMiNi.imageUrl = App.getUser().getImage();
						meApproverMiNi.isOnline = true;
						meApproverMiNi.userId = Long.valueOf(App.getUserID());
						commentApprover.listApproverMiNi.add(meApproverMiNi);
						mAdapter.notifyDataSetChanged();
						showToast("已赞同");
					}

				}else{
					
				}
			}

		}
	}

}
