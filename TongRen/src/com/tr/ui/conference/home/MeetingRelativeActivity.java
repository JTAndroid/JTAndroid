package com.tr.ui.conference.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.conference.common.BaseActivity;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 会议相关
 * 
 * @author d.c 2014-11-15
 */

public class MeetingRelativeActivity extends BaseActivity implements IBindData {
	// 标题栏
	private LinearLayout mIvBackButton = null;
	private TextView mTvTitle = null;

	private LinearLayout ll_more_contact = null;
	private LinearLayout ll_more_knowledge = null;
	private LinearLayout ll_more_requirement = null;
	private LinearLayout ll_more_meeting = null;

	private LinearLayout ll_more_contact_session = null;
	private LinearLayout ll_more_requirement_session = null;
	private LinearLayout ll_more_knowledge_session = null;
	private LinearLayout ll_more_meeting_session = null;

	private MMeetingQuery mMeetingQuery = null;
	private ImageLoader mImageLoader = null;

	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_meeting_relative);
		mIvBackButton = (LinearLayout) findViewById(R.id.hy_layoutTitle_backBtn);
		mTvTitle = (TextView) findViewById(R.id.hy_layoutTitle_title);

		ll_more_contact = (LinearLayout) findViewById(R.id.hy_meeting_relative_ll_more_contact);
		ll_more_knowledge = (LinearLayout) findViewById(R.id.hy_meeting_relative_ll_more_knowledge);
		ll_more_requirement = (LinearLayout) findViewById(R.id.hy_meeting_relative_ll_more_requirement);
		ll_more_meeting = (LinearLayout) findViewById(R.id.hy_meeting_relative_ll_more_meeting);

		ll_more_contact_session = (LinearLayout) findViewById(R.id.hy_meeting_relative_ll_more_contact_session);
		ll_more_requirement_session = (LinearLayout) findViewById(R.id.hy_meeting_relative_ll_more_requirement_session);
		ll_more_knowledge_session = (LinearLayout) findViewById(R.id.hy_meeting_relative_ll_more_knowledge_session);
		ll_more_meeting_session = (LinearLayout) findViewById(R.id.hy_meeting_relative_ll_more_meeting_session);

		mMeetingQuery = (MMeetingQuery) getIntent().getSerializableExtra(ENavConsts.EMeetingDetail);
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public void initData() {
		mTvTitle.setText("会议相关");

		showLoadingDialog();
		CommonReqUtil.doGetJointResource(this, this, String.valueOf(mMeetingQuery.getId()), 7, 0, 20,0 ,null);

		mIvBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (EAPIConsts.CommonReqType.GetJointResource == tag && null != object) {
			HashMap<String, Object> dataBox = (HashMap<String, Object>) object;
			if (null != dataBox) {
				if (dataBox.size() > 0) {
					fillData(dataBox);
				}
			}
		}
	}

	private void fillData(HashMap<String, Object> dataBox) {

		if (null != ll_more_contact) {
			ll_more_contact.removeAllViews();
			List<ConnectionNode> listNode = (List<ConnectionNode>) dataBox.get("listJointPeopleNode");
			if (null != listNode) {
				if (listNode.size() > 0) {
					int count = listNode.size();
					for (int i = 0; i < count; i++) {
						ConnectionNode aNode = listNode.get(i);
						if (null != aNode) {
							ArrayList<Connections> aList = aNode.getListConnections();
							if (null != aList) {
								int count2 = aList.size();
								for (int j = 0; j < count2; j++) {
									Connections conn = aList.get(j);
									if (null != conn) {
										String name = conn.getName();
										String image = conn.getImage();
										View aView = LayoutInflater.from(this).inflate(R.layout.hy_list_item_title_contact, null);
										TextView tvName = (TextView) aView.findViewById(R.id.hy_contact_tv_name);
										ImageView ivLogo = (ImageView) aView.findViewById(R.id.hy_contact_iv_logo);
										tvName.setText(name);
										if (null == image) {
											ivLogo.setImageResource(R.drawable.hy_ic_default_friend_avatar);
										} else {
											if (image.isEmpty()) {
												ivLogo.setImageResource(R.drawable.hy_ic_default_friend_avatar);
											} else {
												if (null != mImageLoader) {
													mImageLoader.displayImage(image, ivLogo);
												}
											}
										}
										ll_more_contact.addView(aView);
										aView.setTag(conn);
										aView.setOnClickListener(peopleClickListener);
										if (ll_more_contact_session.getVisibility() != View.VISIBLE) {
											ll_more_contact_session.setVisibility(View.VISIBLE);
										}
									}
								}
							}
						}
					}
				}
			}

			listNode = (List<ConnectionNode>) dataBox.get("listJointOrganizationNode");
			if (null != listNode) {
				if (listNode.size() > 0) {
					int count = listNode.size();
					for (int i = 0; i < count; i++) {
						ConnectionNode aNode = listNode.get(i);
						if (null != aNode) {
							ArrayList<Connections> aList = aNode.getListConnections();
							if (null != aList) {
								int count2 = aList.size();
								for (int j = 0; j < count2; j++) {
									Connections conn = aList.get(j);
									if (null != conn) {
										String name = conn.getName();
										String image = conn.getImage();
										View aView = LayoutInflater.from(this).inflate(R.layout.hy_list_item_title_contact, null);
										TextView tvName = (TextView) aView.findViewById(R.id.hy_contact_tv_name);
										ImageView ivLogo = (ImageView) aView.findViewById(R.id.hy_contact_iv_logo);
										tvName.setText(name);
										if (null == image) {
											ivLogo.setImageResource(R.drawable.hy_ic_default_friend_avatar);
										} else {
											if (image.isEmpty()) {
												ivLogo.setImageResource(R.drawable.hy_ic_default_friend_avatar);
											} else {
												if (null != mImageLoader) {
													mImageLoader.displayImage(image, ivLogo);
												}
											}
										}
										ll_more_contact.addView(aView);
										aView.setTag(conn);
										aView.setOnClickListener(peopleClickListener);
										if (ll_more_contact_session.getVisibility() != View.VISIBLE) {
											ll_more_contact_session.setVisibility(View.VISIBLE);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (null != ll_more_knowledge) {
			ll_more_knowledge.removeAllViews();
			List<KnowledgeNode> listNode = (List<KnowledgeNode>) dataBox.get("listJointKnowledgeNode");
			if (null != listNode) {
				if (listNode.size() > 0) {
					int count = listNode.size();
					for (int i = 0; i < count; i++) {
						KnowledgeNode aNode = listNode.get(i);
						if (null != aNode) {
							ArrayList<KnowledgeMini2> aList = aNode.getListKnowledgeMini2();
							if (null != aList) {
								int count2 = aList.size();
								for (int j = 0; j < count2; j++) {
									KnowledgeMini2 conn = aList.get(j);
									if (null != conn) {
										String title = conn.title;
										String time = conn.modifytime;
										View aView = LayoutInflater.from(this).inflate(R.layout.hy_list_item_meeting_knowledge, null);
										TextView tvContent = (TextView) aView.findViewById(R.id.hy_knowledge_tv_content);
										TextView tvTime = (TextView) aView.findViewById(R.id.hy_knowledge_tv_time);
										tvContent.setText(title);
										tvTime.setText(time);
										ll_more_knowledge.addView(aView);
										aView.setTag(conn);
										aView.setOnClickListener(knowledgeClickListener);
										if (ll_more_knowledge_session.getVisibility() != View.VISIBLE) {
											ll_more_knowledge_session.setVisibility(View.VISIBLE);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (null != ll_more_requirement) {
			ll_more_requirement.removeAllViews();
			List<AffairNode> listNode = (List<AffairNode>) dataBox.get("listJointAffairNode");
			if (null != listNode) {
				if (listNode.size() > 0) {
					int count = listNode.size();
					for (int i = 0; i < count; i++) {
						AffairNode aNode = listNode.get(i);
						if (null != aNode) {
							ArrayList<AffairsMini> aList = aNode.getListAffairMini();
							if (null != aList) {
								int count2 = aList.size();
								for (int j = 0; j < count2; j++) {
									AffairsMini conn = aList.get(j);
									if (null != conn) {
										String title = conn.title;
										String time = conn.time;
										View aView = LayoutInflater.from(this).inflate(R.layout.hy_list_item_meeting_knowledge, null);
										TextView tvContent = (TextView) aView.findViewById(R.id.hy_knowledge_tv_content);
										TextView tvTime = (TextView) aView.findViewById(R.id.hy_knowledge_tv_time);
										tvContent.setText(title);
										tvTime.setText(time);
										ll_more_requirement.addView(aView);
										aView.setTag(conn);
										aView.setOnClickListener(affairClickListener);
										if (ll_more_requirement_session.getVisibility() != View.VISIBLE) {
											ll_more_requirement_session.setVisibility(View.VISIBLE);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (null != ll_more_meeting) {

		}
		if ((ll_more_requirement_session.getVisibility() != View.VISIBLE) && (ll_more_knowledge_session.getVisibility() != View.VISIBLE) && (ll_more_contact_session.getVisibility() != View.VISIBLE)) {
			Toast.makeText(this, "没有会议相关信息", 0).show();
		}
	}

	private OnClickListener knowledgeClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			KnowledgeMini2 knoMini2 = (KnowledgeMini2) v.getTag();
			ENavigate.startKnowledgeOfDetailActivity(MeetingRelativeActivity.this, knoMini2.id, knoMini2.type);
		}
	};

	private OnClickListener peopleClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Connections conns = (Connections) v.getTag();
		}
	};

	private OnClickListener affairClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			AffairsMini affairsMini = (AffairsMini) v.getTag();
		}
	};
}