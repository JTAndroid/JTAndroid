package com.tr.ui.people.contactsdetails.fragment;

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
import com.tr.api.PeopleReqUtil;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.connections.viewfrg.BaseViewPagerFragment;
import com.tr.ui.people.contactsdetails.View.ContactsDoubleTextViewCombinLayout;
import com.tr.ui.people.model.UserCommentList;
import com.tr.ui.people.model.params.AddEvaluateParams;
import com.tr.ui.people.model.params.AgreeToCancel;
import com.tr.ui.people.model.params.FindEvaluateParams;
import com.tr.ui.people.model.success.AddBooleanSuccess;
import com.tr.ui.people.model.success.AgreeToCancleBoolean;
import com.tr.ui.widgets.AddEvaluationEditDialog;
import com.tr.ui.widgets.AddEvaluationEditDialog.OnDialogFinishListener;
import com.tr.ui.widgets.KnoTagGroupView;
import com.tr.ui.widgets.viewpagerheaderscroll.delegate.ScrollViewDelegate;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

/**
 * 评价frg
 * 
 * @author zhongshan
 * 
 */
public class ContactsRelationEvaluationFrg extends BaseViewPagerFragment
		implements IBindData, OnClickListener {

	/** 评价GroupView */
	private KnoTagGroupView evaluationGv;
	/** 添加评价 */
	private TextView evaluationAddTv;
	/** 编辑标签 */
	private TextView tagEditTv;

	/** 更多评价 */
	private LinearLayout moreEvaluationLayout;

	/** 适应框架 */
	private ScrollViewDelegate mScrollViewDelegate = new ScrollViewDelegate();
	private ScrollView mScrollView;
	private ContactsDoubleTextViewCombinLayout selectUserCommentLayout;
	/** 主页君id */
	private long homeUserId;
	/** 区分我的人脉，他人人脉的标识 */
	private int type;
	private ContactsDoubleTextViewCombinLayout evaluationNewTag;
	private String evaluationContent;

	/** 主页君设置的非好友评价权限，true可以，false不可以 */
	private boolean isEvaluated;

	private Map<String, Object> map;
	/** 评价列表 */
	private UserCommentList commentLists;
	/** 评价内容列表 */
	private ArrayList<UserCommentList.UserComment> userCommentlists;
	// 获取评价列表数据的参数类
	private FindEvaluateParams findEvaluateParams;
	// 请求添加平评价参数类
	private AddEvaluateParams addEvaluateParams;
	// 请求添加评价返回的数据类
	private AddBooleanSuccess addBooleanSuccess;
	// 响应赞同与取消赞同的数据类
	private AgreeToCancleBoolean agreeToCancleBoolean;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void upDate() {
		Bundle bundleDatas = getArguments();
		homeUserId = bundleDatas.getLong(EConsts.Key.ID);
		type = bundleDatas.getInt(EConsts.Key.PERSON_TYPE);
		Log.v("DATA", "homeUserId---->" + homeUserId);
		Log.v("DATA", "人脉type---->" + type);
		// type=2||3他人人脉 type=6我的人脉
		if (type == 2 || type == 3) {// 他人人脉
			tagEditTv.setVisibility(View.GONE);
		} else if (type == 6) {
			tagEditTv.setVisibility(View.VISIBLE);
		}
		initData();
	}

	public static ContactsRelationEvaluationFrg newInstance(int index) {
		ContactsRelationEvaluationFrg fragment = new ContactsRelationEvaluationFrg();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, index);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.relation_detial_evaluation_frg,
				container, false);
		
		return view;
	}
	
	@Override
	public void onViewCreated(View container, Bundle savedInstanceState) {
		initView(container);
		setListener();
	}

	/** 初始化视图 */
	private void initView(View container) {
		mScrollView = (ScrollView) container.findViewById(R.id.scrollview);
		evaluationGv = (KnoTagGroupView) container
				.findViewById(R.id.add_new_evaluation_gv);
		evaluationAddTv = (TextView) container
				.findViewById(R.id.add_evaluation_Tv);
		tagEditTv = (TextView) container.findViewById(R.id.tag_edit_TV);
		
		moreEvaluationLayout = (LinearLayout) container
				.findViewById(R.id.moreEvaluationLayout);

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
			userCommentlists = new ArrayList<UserCommentList.UserComment>();
			userCommentlists.clear();
		}  
		
		findEvaluateParams = new FindEvaluateParams();
		findEvaluateParams.userId = homeUserId;
		Log.v("BJ", "homeUserId=====>"+homeUserId);
		findEvaluateParams.isSelf = false;	
		
		// 获取评价列表数据
		PeopleReqUtil.doRequestWebAPI(getActivity(), this, findEvaluateParams,
				null, PeopleRequestType.PEOPLE_REQ_FINDEVALUTE);
		showLoadingDialog();
	}

	/** 点击事件 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_evaluation_Tv:// 添加评价
			addNewEvaluation();
			break;
		case R.id.tag_edit_TV:// 编辑标签
			ENavigate.ContactsEditRelationEvaluationTagActivity(getActivity(),homeUserId);
			break;
		case R.id.moreEvaluationLayout:// 更多评价
			if (userCommentlists.size() < 1) {
				ToastUtil.showToast(getActivity(), "没有更多了");
				return;
			}
			// 启动人脉详情更多评价界面
			ENavigate.startContactsRelationMoreEvaluationActivityForResult(
					getActivity(), homeUserId);

			break;

		default:
			break;
		}
	}

	/** 添加新评价 */
	private void addNewEvaluation() {
		evaluationNewTag = new ContactsDoubleTextViewCombinLayout(
				getActivity(), new UserCommentList().new UserComment());
		// 弹出ConnsEditDialog加入自定义
		showEvaluationDialog(evaluationNewTag);
		evaluationTagSetListener(evaluationNewTag);
	}

	/** 弹出添加评价Dialog */
	private void showEvaluationDialog(
			final ContactsDoubleTextViewCombinLayout evaluationTag) {
		new AddEvaluationEditDialog(getActivity(), evaluationTag, "",
				new OnDialogFinishListener() {

					@Override
					public void onFinish(View view, String content) {
						if (content != null
								&& view instanceof ContactsDoubleTextViewCombinLayout) {
							Log.v("TAG", "content==" + content);

							evaluationContent = content;
							((ContactsDoubleTextViewCombinLayout) view)
									.setContent(content);
						}
						if (!((ContactsDoubleTextViewCombinLayout) view)
								.getContentText().trim().isEmpty()) {// 不为为空
							if (hasEvaluationTag(userCommentlists, content)) {
/*<<<<<<< HEAD
								Toast.makeText(getActivity(), "评价已存在", 0)
										.show();
							} else if (content.length() >= 20) {
								Toast.makeText(getActivity(), "温馨提示：评价不得大于20字符", 0)
										.show();
=======*/
								ToastUtil.showToast(getActivity(), "评价已存在");
							} else if (content.length() > 10) {
								ToastUtil.showToast(getActivity(), "评价不大于20字符");
							} else {
								// 请求添加评价
								addEvaluateParams = new AddEvaluateParams();
								addEvaluateParams.comment = content;
								addEvaluateParams.userId = homeUserId;

								PeopleReqUtil
										.doRequestWebAPI(
												getActivity(),
												ContactsRelationEvaluationFrg.this,
												addEvaluateParams,
												null,
												PeopleRequestType.PEOPLE_REQ_ADDEVALUATE);

								showLoadingDialog();
							}
						}else{
							ToastUtil.showToast(getActivity(), "请输入评价");
						}
					}
				}).show();

	}

	private boolean hasEvaluationTag(
			ArrayList<UserCommentList.UserComment> userCommentlists,
			String content) {
		for (int i = 0; i < userCommentlists.size(); i++) {
			if (userCommentlists.get(i).userComment.equals(content)) {
				return true;
			}
		}
		return false;
	}

	/** 为评价标签设置点击事件 */
	private void evaluationTagSetListener(
			final ContactsDoubleTextViewCombinLayout evaluationTag) {
		evaluationTag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/** 首先判断用户是否有评价权限 */
				if (!isEvaluated) {
					Toast.makeText(getActivity(), "对方设置非好友不能评价赶快去加好友吧", 0)
							.show();
				} else {
					if (evaluationTag.isEvaluateStatus()) {// true
						// :此人已经赞同;false:此人未赞同
						Toast.makeText(getActivity(), "您已赞同该评价", 0).show();
					} else {
						// 赞同该评价接口
						selectUserCommentLayout = evaluationTag;
						AgreeToCancel agreeToCancel = new AgreeToCancel();
						agreeToCancel.id = homeUserId;
						agreeToCancel.feedback = false;
						PeopleReqUtil.doRequestWebAPI(getActivity(),
								ContactsRelationEvaluationFrg.this,
								agreeToCancel, null,
								PeopleRequestType.PEOPLE_REQ_FEEDBACKEVALUATE);
						showLoadingDialog();
					}
				}
			}
		});
	}

	/** 数据请求回调接口 */
	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		// 获取对该用户的评价
		case PeopleRequestType.PEOPLE_REQ_FINDEVALUTE:

			dismissLoadingDialog();
			if (object != null) {
				
				if(userCommentlists != null && userCommentlists.size() > 0){
					return;
				}
				commentLists = (UserCommentList) object;
				Log.v("DATA", "获取人脉详情评价返回的数据---->" + commentLists);

				// 评价权限
				isEvaluated = commentLists.isEvaluated;
				ArrayList<UserCommentList.UserComment> userCommentlists = (ArrayList<UserCommentList.UserComment>) commentLists.listUserComment;
				if (userCommentlists != null) {
					for (UserCommentList.UserComment userComment : userCommentlists) {
						if (userComment != null) {
							if (!TextUtils.isEmpty(userComment.userComment)
									&& userComment.count >= 1) {
								// 初始化评价
								initOneEvaluation(userComment);
							}
						}
					}
				}
			}

			break;

		case PeopleRequestType.PEOPLE_REQ_FEEDBACKEVALUATE:// 赞同/取消赞同评价
			dismissLoadingDialog();
			if (object != null) {
				agreeToCancleBoolean = (AgreeToCancleBoolean) object;
				boolean flag = agreeToCancleBoolean.success;
				if (flag && selectUserCommentLayout != null) {
					long provateNumber = selectUserCommentLayout.getNumber() + 1;
					Log.v("WC", "provateNumber---->" + provateNumber);
					selectUserCommentLayout.setNumber(provateNumber);
					selectUserCommentLayout.setEvaluateStatus(true);
					selectUserCommentLayout
							.changeBackground(selectUserCommentLayout
									.getUserComment().evaluateStatus);
					Toast.makeText(getActivity(), "赞同成功", 0).show();
				}
			}
			break;
		case PeopleRequestType.PEOPLE_REQ_ADDEVALUATE:// 添加评价
			dismissLoadingDialog();
			if (object != null) {
				Log.v("ADD", "添加评价成功1");
				addBooleanSuccess = (AddBooleanSuccess) object;
				boolean flag = addBooleanSuccess.success;
				long id = addBooleanSuccess.id;

				Log.v("TAG", "evaluationNewTag==" + evaluationNewTag);
				Log.v("TAG", "evaluationContent==" + evaluationContent);

				if (flag == true && evaluationNewTag != null
						&& !TextUtils.isEmpty(evaluationContent)) {

					Log.v("ADD", "添加评价成功2");

					evaluationNewTag.setContent(evaluationContent);
					evaluationNewTag.setNumber(1);
					UserCommentList.UserComment userComment = evaluationNewTag
							.getUserComment();
					userComment.evaluateStatus = false;
					evaluationNewTag.setEvaluateStatus(true);
					userComment.userComment = evaluationContent;
					userComment.count = 1L;
					userCommentlists.add(userComment);
					evaluationGv.addView(evaluationNewTag,
							userCommentlists.size() - 1);
					Toast.makeText(getActivity(), "评价成功", 0).show();
				} else {
					Toast.makeText(getActivity(), "评价失败", 0).show();
				}
			}
			break;

		default:
			break;
		}
	}

	/** 初始化一条评价 */
	private void initOneEvaluation(UserCommentList.UserComment userComment) {
		// 布局
		ContactsDoubleTextViewCombinLayout evaluationTag = new ContactsDoubleTextViewCombinLayout(
				getActivity(), userComment);
		evaluationTag.setNumber(userComment.count);
		// 设置点击事件
		evaluationTagSetListener(evaluationTag);
		// 加入评价集合
		userCommentlists.add(userComment);
		// 加入布局
		evaluationGv.addView(evaluationTag, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION
				|| requestCode == ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION) {
			// 每次从更多、编辑页面回来，刷新界面前将集合中的数据清除
			if (evaluationGv != null && userCommentlists != null) {
				evaluationGv.removeViews(0, userCommentlists.size());
				if (!userCommentlists.isEmpty()) {
					userCommentlists.clear();
				}
			}
			showLoadingDialog();
			// 获取评价列表的数据
			PeopleReqUtil.doRequestWebAPI(getActivity(), this,
					findEvaluateParams, null,
					PeopleRequestType.PEOPLE_REQ_FINDEVALUTE);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean isViewBeingDragged(MotionEvent event) {
		return mScrollViewDelegate.isViewBeingDragged(event, mScrollView);
	}
}
