package com.tr.ui.connections.revision20150122.detail;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.AppData;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.obj.UserComment;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.connections.viewfrg.BaseViewPagerFragment;
import com.tr.ui.home.frg.HomePageFrag;
import com.tr.ui.organization.model.Customer;
import com.tr.ui.organization.model.PushKnowledge;
import com.tr.ui.organization.model.RelatedInformation;
import com.tr.ui.organization.model.notice.CustomerNotice;
import com.tr.ui.organization.model.resource.CustomerResource;
import com.tr.ui.widgets.AddEvaluationEditDialog;
import com.tr.ui.widgets.AddEvaluationEditDialog.OnDialogFinishListener;
import com.tr.ui.widgets.ConnsEditDialog;
import com.tr.ui.widgets.ConnsEditDialog.OnFinishListener;
import com.tr.ui.widgets.DoubleTextViewTagLayout;
import com.tr.ui.widgets.KnoTagGroupView;
import com.tr.ui.widgets.viewpagerheaderscroll.delegate.ScrollViewDelegate;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 个人主页 评价frg
 * 
 * @author zhongshan
 * 
 */
public class RelationEvaluationFrg extends BaseViewPagerFragment implements IBindData, OnClickListener {

	/** 评价GroupView */
	private KnoTagGroupView evaluationGv;
	/** 添加评价 */
	private TextView evaluationAddTv;
	/** 编辑标签 */
	private TextView tagEditTv;
	/** 评价内容列表 */
	private ArrayList<UserComment> userCommentlists;
	/** 更多评价 */
	private LinearLayout moreEvaluationLayout;
//
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void upDate() {
		Bundle bundleDatas = getArguments();
		homeUserId = bundleDatas.getString(EConsts.Key.ID);
	}

	public static RelationEvaluationFrg newInstance(int index) {
		RelationEvaluationFrg fragment = new RelationEvaluationFrg();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, index);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.relation_detial_evaluation_frg, container, false);
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
		mScrollView = (ScrollView) container.findViewById(R.id.scrollview);
		evaluationGv = (KnoTagGroupView) container.findViewById(R.id.add_new_evaluation_gv);
		evaluationAddTv = (TextView) container.findViewById(R.id.add_evaluation_Tv);
		tagEditTv = (TextView) container.findViewById(R.id.tag_edit_TV);
		moreEvaluationLayout = (LinearLayout) container.findViewById(R.id.moreEvaluationLayout);
		if (!homeUserId.equals(App.getUserID())) {// 如果不是自己则隐藏编辑职业标签
			tagEditTv.setVisibility(View.INVISIBLE);
		}
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
			userCommentlists = new ArrayList<UserComment>();
		}
		ConnectionsReqUtil.doFindEvaluate(getActivity(), this, Long.valueOf(homeUserId), false, null);
//		showLoadingDialog();
	}

	/** 点击事件 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_evaluation_Tv:// 添加评价

			// if (new AppData().ismFriendsAppraiseType()) {//
			// 判断是否允许非好友评价1不容许(false)
			if (isEvaluated) {
				addNewEvaluation();
			} else {
				Toast.makeText(getActivity(), "对方设置非好友不能评价赶快去加好友吧", 0).show();
			}
			// } else {
			// }
			break;
		case R.id.tag_edit_TV:// 编辑标签
			ENavigate.EditRelationEvaluationTagActivity(getActivity());
			break;
		case R.id.moreEvaluationLayout:// 更多评价
			if (userCommentlists.size() < 1) {
				Toast.makeText(getActivity(), "没有更多了", 0).show();
				return;
			}
			ENavigate.startRelationMoreEvaluationActivityForResult(getActivity(), homeUserId);
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
				if (!((DoubleTextViewTagLayout) view).getContentText().trim().isEmpty()) {// 不为为空
					if (hasEvaluationTag(userCommentlists, content)) {
						Toast.makeText(getActivity(), "评价已存在", 0).show();
					} else if (content.length() > 10) {
						Toast.makeText(getActivity(), "评价不大于20字符", 0).show();
					} else {
						// 请求添加评价
						ConnectionsReqUtil.doAddEvaluate(getActivity(), RelationEvaluationFrg.this, Long.valueOf(homeUserId), content, null);
//						showLoadingDialog();
					}
				}else{
					Toast.makeText(getActivity(), "请输入评价", 0).show();
				}
			}
		}).show();

	}

	private boolean hasEvaluationTag(ArrayList<UserComment> userCommentlists, String content) {
		for (int i = 0; i < userCommentlists.size(); i++) {
			if (userCommentlists.get(i).getUserComment().equals(content)) {
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
					Toast.makeText(getActivity(), "对方设置非好友不能评价赶快去加好友吧", 0).show();
				} else {
					if (evaluationTag.isChecked()) {// true
						// :此人已经赞同;false:此人未赞同
						Toast.makeText(getActivity(), "您已赞同该评价", 0).show();
					} else {
						// 赞同该评价接口
						selectUserCommentLayout = evaluationTag;
						ConnectionsReqUtil.doFeedbackEvaluate(getActivity(), RelationEvaluationFrg.this,Long.valueOf(homeUserId), ((UserComment) (evaluationTag.getTag())).getId(), true, null);
//						showLoadingDialog();
					}
				}
			}
		});
	}

	/** 数据请求回调接口 */
	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		// 获取全部知识标签
		case EAPIConsts.concReqType.findEvaluate:
//			dismissLoadingDialog(); 
			if (object != null) {
				ArrayList arr = (ArrayList) object;
				String str = (String) arr.get(0);
				// 评价权限
				isEvaluated = str.equals("true") ? true : false;
				// if (!isEvaluated) {
				// evaluationAddTv.setVisibility(View.INVISIBLE);
				// }
				if (!userCommentlists.isEmpty()) {
					userCommentlists.clear();
				}
				userCommentlists = (ArrayList<UserComment>) arr.get(1);
				evaluationGv.addTagViews(userCommentlists, listener, null, true, false);
			}
			break;
		case EAPIConsts.concReqType.feedbackEvaluate:
//			dismissLoadingDialog();
			if (object != null) {
				String str = (String) object;
				if (str.equals("true") && selectUserCommentLayout != null) {
					int provateNumber = selectUserCommentLayout.getNumber() + 1;
					selectUserCommentLayout.setNumber(provateNumber + "");
					selectUserCommentLayout.setChecked(true);
					selectUserCommentLayout.changeBackground(true);
					Toast.makeText(getActivity(), "赞同成功", 0).show();
				}
			}
			break;

		case EAPIConsts.concReqType.addEvaluate:
//			dismissLoadingDialog();
			if (object != null) {
				ArrayList arr = (ArrayList) object;
				String str = (String) arr.get(0);
				if (str.equals("true") && evaluationNewTag != null && !TextUtils.isEmpty(evaluationContent)) {
					
					UserComment comment = new UserComment();
					comment.setUserComment(evaluationContent);
					comment.setCount(1);
					comment.setEvaluateStatus(true);
					userCommentlists.add(comment);
					evaluationGv.addTagView(comment, listener, null, true, true);
					
					// Toast.makeText(getActivity(), "评价成功", 0).show();
				} else {
					// Toast.makeText(getActivity(), "评价失败", 0).show();
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
					ConnectionsReqUtil.doFeedbackEvaluate(getActivity(),
							RelationEvaluationFrg.this, Long.valueOf(homeUserId),
							((UserComment) (evaluationTag.getTag())).getId(), true, null);
					// showLoadingDialog();
				}
			}
		};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION || requestCode == ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION) {
			// 每次从更多、编辑页面回来，刷新界面前将集合中的数据清除
			if (evaluationGv != null && userCommentlists != null&&userCommentlists.size()>0) {
				int childCount = evaluationGv.getChildCount();
				if (childCount>=userCommentlists.size()) {
					evaluationGv.removeViews(0, userCommentlists.size());
				}
			}
//			showLoadingDialog();
			ConnectionsReqUtil.doFindEvaluate(getActivity(), this, Long.valueOf(homeUserId), false, null);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean isViewBeingDragged(MotionEvent event) {
		return mScrollViewDelegate.isViewBeingDragged(event, mScrollView);
	}
}
