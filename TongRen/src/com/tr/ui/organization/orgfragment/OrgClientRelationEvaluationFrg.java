package com.tr.ui.organization.orgfragment;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.connections.viewfrg.BaseViewPagerFragment;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.organization.model.evaluate.CustomerEvaluate;
import com.tr.ui.organization.orgdetails.orgfragment.OrgRelationEvaluationFrg;
import com.tr.ui.widgets.AddEvaluationEditDialog;
import com.tr.ui.widgets.AddEvaluationEditDialog.OnDialogFinishListener;
import com.tr.ui.widgets.DoubleTextViewTagLayout;
import com.tr.ui.widgets.KnoTagGroupView;
import com.tr.ui.widgets.viewpagerheaderscroll.delegate.ScrollViewDelegate;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

/**
 * 我的客户/客户详情  评价
 * @author huangzhiyu
 *
 */

public class OrgClientRelationEvaluationFrg extends BaseViewPagerFragment implements IBindData, OnClickListener{

	/** 评价GroupView */
	private KnoTagGroupView evaluationGv;
	/** 添加评价 */
	private TextView evaluationAddTv;
	/** 编辑标签 */
	private TextView tagEditTv;
	/** 评价内容列表 */
	private ArrayList<CustomerEvaluate> userCommentlists;
	/** 更多评价 */
	private LinearLayout moreEvaluationLayout;

	/** 适应框架 */
	private ScrollViewDelegate mScrollViewDelegate = new ScrollViewDelegate();
	private ScrollView mScrollView;
	private DoubleTextViewTagLayout selectUserCommentLayout;
	/** 主页君id */
	private String homeUserId;
	private DoubleTextViewTagLayout evaluationNewTag;
	private String evaluationContent;

	/** 主页君设置的非好友评价权限，true可以，false不可以 */
	private boolean isEvaluated;
	
	private Map<String, Object> map;

	private long client_id;
	
	private String auth;
	private ClientDetailsActivity client_DetailsActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	public void upDate() {
		Bundle bundleDatas = getArguments();
//		client_id = bundleDatas.getLong(EConsts.Key.ID);
	    auth = bundleDatas.getString("AUTH");
		Log.e("MSG", "评价customerId"+client_id);
		
	}

	public static OrgClientRelationEvaluationFrg newInstance(int index) {
		OrgClientRelationEvaluationFrg fragment = new OrgClientRelationEvaluationFrg();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, index);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.client_relation_details_evaluation, container, false);
		
		if (ClientDetailsActivity.class.getSimpleName().equals(getActivity().getClass().getSimpleName())) {
			ClientDetailsActivity client_DetailsActivity = (ClientDetailsActivity) getActivity();
			client_id = client_DetailsActivity.client_id;
		}


		return view;
	
	}

	@Override
	public void onViewCreated(View container, Bundle savedInstanceState) {
		initView(container);
		initData();
		setListener();
	}

	/** 初始化视图 */
	private void initView(View container) {
		mScrollView = (ScrollView) container.findViewById(R.id.eva_scrollview);
		evaluationGv = (KnoTagGroupView) container.findViewById(R.id.eva_add_new_evaluation_gv);
		evaluationAddTv = (TextView) container.findViewById(R.id.client_eva_add_evaluation_Tv);
		tagEditTv = (TextView) container.findViewById(R.id.client_eva_tag_edit_TV);
		if(auth!=null){

			if(!auth.equals("3")){
				tagEditTv.setVisibility(View.GONE);
			}
		}
		
		moreEvaluationLayout = (LinearLayout) container.findViewById(R.id.eva_moreEvaluationLayout);
//		if (!homeUserId.equals(App.getUserID())) {// 如果不是自己则隐藏编辑职业标签
//			tagEditTv.setVisibility(View.INVISIBLE);
//		}
	}

	/** 设置监听 */
	private void setListener() {

		evaluationAddTv.setOnClickListener(this);
		tagEditTv.setOnClickListener(this);
		moreEvaluationLayout.setOnClickListener(this);

	}

	/** 初始化数据 */
	private void initData() {
		if (userCommentlists == null) {
			userCommentlists = new ArrayList<CustomerEvaluate>();
		}
		//获取对该用户的评价 
		OrganizationReqUtil.doFindEvaluate(getActivity(), this,client_id, false,"2", null);
		showLoadingDialog();
	}

	/** 点击事件 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.client_eva_add_evaluation_Tv:// 添加评价
			
		    addNewEvaluation();
			
			break;
			
		case R.id.client_eva_tag_edit_TV:// 编辑标签
			ENavigate.EditClientRelationEvaluationTagActivity(getActivity(),client_id);
			break;
		case R.id.eva_moreEvaluationLayout:// 更多评价   查看评价人
			if (userCommentlists.size() < 1) {
				ToastUtil.showToast(getActivity(), "没有更多了");
				return;
			}
			ENavigate.startClientMoreEvaluationActivityForResult(getActivity(), client_id);
			break;

		default:
			break;
		}
	}

	/** 添加新评价 */
	private void addNewEvaluation() {
		evaluationNewTag = new DoubleTextViewTagLayout(getActivity(), "", "1", true);
		// 弹出ConnsEditDialog加入自定义
		showEvaluationDialog(evaluationNewTag);
		evaluationTagSetListener(evaluationNewTag);
	}

	/** 弹出添加评价Dialog */
	private void showEvaluationDialog(final DoubleTextViewTagLayout evaluationTag) {
		new AddEvaluationEditDialog(getActivity(), evaluationTag, "", new OnDialogFinishListener() {

			@Override
			public void onFinish(View view, String content) {
				if (content != null && view instanceof DoubleTextViewTagLayout) {
					evaluationContent = content;
					((DoubleTextViewTagLayout) view).setContent(content);
				}
				if (!((DoubleTextViewTagLayout) view).getContentText().trim().isEmpty()) {// 不为空
					if (hasEvaluationTag(userCommentlists, content)) {
						ToastUtil.showToast(getActivity(), "评价已存在");
					} else if (content.length() > 10) {
						ToastUtil.showToast(getActivity(), "评价不大于20字符");
					} else {
						// 请求添加评价
						OrganizationReqUtil.doAddEvaluate(getActivity(), OrgClientRelationEvaluationFrg.this, client_id, content,"2", null);
						showLoadingDialog();
					}
				}
			}
		}).show();

	}
	

	private boolean hasEvaluationTag(ArrayList<CustomerEvaluate> userCommentlists, String content) {
		for (int i = 0; i < userCommentlists.size(); i++) {
			if (userCommentlists.get(i).userComment.equals(content)) {
				return true;
			}
		}
		return false;
	}

	/** 为评价标签设置点击事件 */
	private void evaluationTagSetListener(final DoubleTextViewTagLayout evaluationTag) {
		evaluationTag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/** 首先判断用户是否有评价权限 */
				if (!isEvaluated) {
					ToastUtil.showToast(getActivity(), "对方设置非好友不能评价赶快去加好友吧");
				} else {
					if (evaluationTag.isChecked()) {// true
						// :此人已经赞同;false:此人未赞同
						ToastUtil.showToast(getActivity(), "您已赞同该评价");
					} else {
						// 赞同该评价接口
						selectUserCommentLayout = evaluationTag;
						OrganizationReqUtil.doFeedbackEvaluate(getActivity(), OrgClientRelationEvaluationFrg.this,client_id, ((CustomerEvaluate)evaluationTag.getTag()).id, true,"2", null);
						showLoadingDialog();
					}
				}
			}
		});
	}

	/** 数据请求回调接口 */
	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		// //获取对该用户的评价	
		case EAPIConsts.OrganizationReqType.GET_ORG_HOME_Evaluate:
			dismissLoadingDialog(); 
			if (object != null) {
				ArrayList arr = (ArrayList) object;
				boolean flag =  (Boolean) arr.get(0);
				// 评价权限
				isEvaluated = flag;
				userCommentlists = (ArrayList<CustomerEvaluate>) arr.get(1);
				evaluationGv.addTagViews(userCommentlists, listener, null, true, false);
			}
			break;
		case EAPIConsts.OrganizationReqType.FEEDBACK_EVALUATE: //赞同 /取消赞同评价
			dismissLoadingDialog();
			if (object != null) {
				map = (Map<String, Object>) object;
				Boolean success = (Boolean) map.get("FLAG");
				if (success && selectUserCommentLayout != null) {
					int provateNumber = selectUserCommentLayout.getNumber() + 1;
					selectUserCommentLayout.setNumber(provateNumber + "");
					selectUserCommentLayout.setChecked(true);
					selectUserCommentLayout.changeBackground(true);
				}
			}
			break;

		case EAPIConsts.OrganizationReqType.ADD_ORG_HOME_Evaluate://添加评价
			dismissLoadingDialog();
			if (object != null) {
				Log.v("TAG", "添加评价成功1");
				map = (Map<String, Object>) object;
				boolean flag = (Boolean) map.get("success");
				
				Log.v("TAG", "evaluationNewTag=="+evaluationNewTag);
				Log.v("TAG", "evaluationContent=="+evaluationContent); 
				
				if (flag == true && evaluationNewTag != null && !TextUtils.isEmpty(evaluationContent)) {
					
					Log.v("TAG", "添加评价成功2");
					
//					evaluationNewTag.setContent(evaluationContent);
//					evaluationNewTag.setNumber("1");
//					CustomerEvaluate customerEvaluate  = evaluationNewTag.getUserComment();
//					customerEvaluate.evaluateStatus = false;
//					evaluationNewTag.setEvaluateStatus(true);
//					customerEvaluate.userComment = evaluationContent;
//					customerEvaluate.count = 1;
//					evaluationGv.addView(evaluationNewTag, userCommentlists.size() - 1);
					
					CustomerEvaluate customerEvaluate = new CustomerEvaluate();
					customerEvaluate.userComment = evaluationContent;
					customerEvaluate.count = 1;
					customerEvaluate.evaluateStatus = true;
					evaluationGv.addTagView(customerEvaluate, listener, null, true, true);
					userCommentlists.add(customerEvaluate);
					ToastUtil.showToast(getActivity(), "评价成功");
				} else {
					ToastUtil.showToast(getActivity(), "评价失败");
				}
			}
			break;						
		default:
			break;
		}
			
		}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			DoubleTextViewTagLayout evaluationTag = (DoubleTextViewTagLayout) v;
			if (evaluationTag.isChecked()) {// true
				// :此人已经赞同;false:此人未赞同
				 Toast.makeText(getActivity(), "您已赞同该评价", 0).show();
			} else {
				// 赞同该评价接口
				selectUserCommentLayout = evaluationTag;
				OrganizationReqUtil.doFeedbackEvaluate(getActivity(),
						OrgClientRelationEvaluationFrg.this,
						Long.valueOf(homeUserId),
						((CustomerEvaluate) (evaluationTag.getTag())).id, true, "1",
						null);
				// showLoadingDialog();
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION || requestCode == ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION) {
			// 每次从更多、编辑页面回来，刷新界面前将集合中的数据清除
			if (evaluationGv != null && userCommentlists != null) {
				evaluationGv.removeViews(0, userCommentlists.size());
				if (!userCommentlists.isEmpty()) {
					userCommentlists.clear();
				}
			}
			showLoadingDialog();
			//获取所有评价
			OrganizationReqUtil.doFindEvaluate(getActivity(), this, client_id, false,"2", null);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean isViewBeingDragged(MotionEvent event) {
		return mScrollViewDelegate.isViewBeingDragged(event, mScrollView);
	}

}
