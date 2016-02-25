package com.tr.ui.home;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.model.obj.DynamicNews;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.people.homepage.ContactsMainPageActivity;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class IndexDynamicOnClickLister implements OnClickListener, IBindData {
	private DynamicNews mDynamicNews;
	private Activity mActivity;
	private int label = 3;
	private int labels = 4;

	public IndexDynamicOnClickLister() {
		super();
	}

	public IndexDynamicOnClickLister(DynamicNews dynamicNews, Activity activity) {
		super();
		this.mActivity = activity;
		this.mDynamicNews = dynamicNews;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_flow_share_common:
		case R.id.home_flow_published_common_ll:
		case R.id.flow_published_common_title_tv:
			if (mDynamicNews.getType() == DynamicNews.TYPE_CREATE_REQUIREMENT) { // 创建需求
				ENavigate.startNeedDetailsActivity(mActivity,
						mDynamicNews.getTargetId() + "", 1);
			}
			if (mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_REQUIREMENT
					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_REQUIREMENT) { // 转发需求
				ENavigate.startNeedDetailsActivity(mActivity,
						mDynamicNews.getTargetId() + "", 2);
			}
			/* ptype 2大乐 3中乐 4小乐 5独乐 */
			// boolean boolTmp = mDynamicNews.getPtype() == 2 ? true :false;
			if (mDynamicNews.getType() == DynamicNews.TYPE_CREATE_KNOWLEDGE) {/*
																			 * 启动知识详情显示页面
																			 * type
																			 * :
																			 * 1
																			 */
				ENavigate.startKnowledgeOfDetailActivity(mActivity,
						mDynamicNews.getTargetId(), mDynamicNews.getLowType(),
						false);
			} else if (mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_KNOWLEDGE) {/* 转发的知识都是大乐 */
				if (mDynamicNews.getCreaterId() == 0) {
					ENavigate.startKnowledgeOfDetailActivity(mActivity,
							mDynamicNews.getTargetId(),
							mDynamicNews.getLowType(), true);
				} else {
					ENavigate.startKnowledgeOfDetailActivity(mActivity,
							mDynamicNews.getTargetId(),
							mDynamicNews.getLowType(), true);
				}
			}
			if (mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_KNOWLEDGE) {// 金桐网推荐的知识
				ENavigate.startKnowledgeOfDetailActivity(mActivity,
						mDynamicNews.getTargetId(), mDynamicNews.getLowType(),
						true);
			}
			if ((mDynamicNews.getType() == DynamicNews.TYPE_CREATE_MEETING || mDynamicNews
					.getType() == DynamicNews.TYPE_FORWARDING_MEETING)
					/*&& mDynamicNews.getLowType() == 1*/) {/* 跳转到会议详情 */
				ENavigate.startSquareActivity(mActivity, mDynamicNews.getTargetId(),0);
			}
			if (mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_MEETING
					&& mDynamicNews.getLowType() == 0) {/* 邀请会议 */
				/*ENavigate.startMeetingInvitationActivity(mActivity);*/
				ENavigate.startSquareActivity(mActivity, mDynamicNews.getTargetId(),0);
			}
			break;
		case R.id.home_flow_figure_ll:/* 人物布局 */
			startRelationHome();  //用户逻辑跳转
			startOrganizationActivity();// 组织逻辑跳转
			startCustomerActivity();// //客户逻辑跳转
			startContactsActivity();// 人脉逻辑跳转
			break;
		case R.id.home_flow_item_head_LL:
			if(mDynamicNews.getCreaterId() <= 0 
					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_KNOWLEDGE //金桐脑推荐知识
					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_REQUIREMENT //金桐脑推荐需求
					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS //金桐脑推荐人脉
					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER //金桐脑推荐客户
					|| mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION//金桐脑推荐组织
					){ //金桐脑推荐的任何知识点击头像不跳转
				return ;
			}
			//大数据推送的0客户 1组织　2未注册组织
			boolean isStart = false;
			if(mDynamicNews.getLowType() == 1 && mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION || mDynamicNews.getLowType() == 2){
				//ENavigate.startOrgMyHomePageActivity(mActivity, mDynamicNews.getTargetId(),mDynamicNews.getCreaterId(), true, ENavConsts.type_details_org);
				ENavigate.startRelationHomeActivity(mActivity,mDynamicNews.getCreaterId()+"",true,ENavConsts.type_details_other);
				isStart = true;
			}else if(mDynamicNews.getLowType() == 0 && mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION){
				startClientDetails();//跳转客户详情
				isStart = true;
			}else{
				HomeReqUtil.getDynamicNewLowType(mActivity, IndexDynamicOnClickLister.this, null, mDynamicNews.getCreaterId());
				isStart = true;
			}
			if (!isStart) {
				HomeReqUtil.getDynamicNewLowType(mActivity, this, null,mDynamicNews.getCreaterId());
			}
			break;
		default:
			break;
		}
	}
	private void startClientDetails() {
		ENavigate.startClientDedailsActivity(mActivity,mDynamicNews.getTargetId());
	}

	private void startRelationHome() {
		if (mDynamicNews.getType() == DynamicNews.TYPE_USER_CARD /* 用户 */
		) {
			ENavigate.startRelationHomeActivity(mActivity,
					mDynamicNews.getTargetId() + "", true,
					ENavConsts.type_details_other);
		}  else if (mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CUSTOMER) {/* 金桐脑推荐的用户 */
			ENavigate.startRelationHomeActivity(mActivity,
					mDynamicNews.getTargetId() + "", true,
					ENavConsts.type_details_other);
		}
	}

	/**
	 * 组织逻辑跳转
	 */
	private void startOrganizationActivity() {
//		if (mDynamicNews.getType() == DynamicNews.TYPE_CREATE_ORGANIZATION) {/* 创建组织 */
//			ENavigate.startOrgMyHomePageActivity(mActivity,
//					mDynamicNews.getTargetId(), mDynamicNews.getCreaterId(),
//					true, ENavConsts.type_details_org);
//		}
		/*
		 * 转发组织
		 */
		if (mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_ORGANIZATION) {
			ENavigate.startOrgMyHomePageActivityByUseId(mActivity,mDynamicNews.getTargetId());
		}
		// 大数据推送的0客户 1组织　2未注册组织
		if (mDynamicNews.getLowType() == 1
				&& mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION
				|| mDynamicNews.getLowType() == 2&& mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION) {
			ENavigate.startOrgMyHomePageActivity(mActivity,
					mDynamicNews.getTargetId(), mDynamicNews.getCreaterId(),
					true, ENavConsts.type_details_org);
		} else if (mDynamicNews.getLowType() == 0
				&& mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_ORGANIZATION) {
			ENavigate.startClientDedailsActivity(mActivity,  mDynamicNews.getTargetId(), 1, 6);//跳转客户详情
					
		} 
	}

	/**
	 * 客户逻辑跳转
	 */
	private void startCustomerActivity() {
		if (mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CUSTOM) {/* 创建客户 */
			Log.v("WC", "TargetId---->" + mDynamicNews.getTargetId());
			startClientDetails();
		}
		if (mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CUSTOM) {/* 转发客户 */
			ENavigate.startClientDedailsActivity(mActivity,
					mDynamicNews.getTargetId(), 1, 6);

		}
//		if (mDynamicNews.getType() == DynamicNews.TYPE_CUSTOMER_CARD) {/* 客户名片 */
//			ENavigate.startClientDedailsActivity(mActivity,
//					mDynamicNews.getTargetId());
//		}
	}

	/**
	 * 人脉逻辑跳转
	 */
	private void startContactsActivity() {
		if (mDynamicNews.getType() == DynamicNews.TYPE_CREATE_CONTACTS) {/* 创建人脉 */
			ENavigate.startRelationHomeActivity(
					mActivity, 	mDynamicNews.getTargetId()+"",false,ENavConsts.TYPE_CONNECTIONS_HOME_PAGE,false);
		}
//		else if (mDynamicNews.getType() == DynamicNews.TYPE_CONTACTS_CARD) {/* 人脉 */
//			ENavigate.startContactsDetailsActivity(mActivity, 2,
//					mDynamicNews.getTargetId(), 210, 1);
//		} 
		else if (mDynamicNews.getType() == DynamicNews.TYPE_FORWARDING_CONTACTS) {/* 转发人脉 */
//			ENavigate.startContactsDetailsActivity(mActivity, 2,
//					mDynamicNews.getTargetId(), 210, 1);
			ENavigate.startRelationHomeActivity(
					mActivity, 	mDynamicNews.getTargetId()+"",false,ENavConsts.TYPE_CONNECTIONS_HOME_PAGE,false);
		} else if (mDynamicNews.getType() == DynamicNews.TYPE_RECOMMEND_CONTACTS) {/* 金桐脑推荐人脉 */
//			ENavigate.startContactsDetailsActivity(mActivity, 2,
//					mDynamicNews.getTargetId(), 210, 1);
			ENavigate.startRelationHomeActivity(
					mActivity, 	mDynamicNews.getTargetId()+"",false,ENavConsts.TYPE_CONNECTIONS_HOME_PAGE,false);
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		/**
		 * 目前动态后台无法区别组织和个人 需要前端在跳转之前 从服务器拿去类型 1 个人 2组织
		 */
		if (tag == EAPIConsts.HomeReqType.HOME_REQ_GETCONNECTORORG) {// 判断是否是组织还是人
			if (object != null) {
				int type = (Integer) object;
				if (type == 2) {// 组织跳转
					ENavigate.startOrgMyHomePageActivityByUseId(mActivity,
							mDynamicNews.getCreaterId());
				} else {// 个人主页
					ENavigate.startRelationHomeActivity(mActivity,
							mDynamicNews.getCreaterId() + "", true,
							ENavConsts.type_details_other);
				}
			}
		}
	}

}
