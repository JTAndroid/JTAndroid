package com.tr.ui.share;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.IMReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.MGetListIMRecord;
import com.tr.model.obj.ChatMessage;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.IMRecord;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCMessage;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.IMAdapter;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.widgets.BasicListView;
import com.utils.common.CustomDlgClickListener;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class ShareActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();

	// 常量部分
	private final int PAGE_LENGTH = 20;

	// 控件
	private TextView newChatTv;
	private SwipeRefreshLayout containerSrl;
	private BasicListView chatLv;

	// 变量
	private IMAdapter mAdapter;
	private int mSelection;
	// 是否需要进一步处理
	private boolean furtherHandle;
	// 分享单个数据
	private JTFile mShareInfo; 
	// 可分享一组数据
	private ArrayList<JTFile> mListShareInfo;

	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("分享到");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		initVars();
		initControls();
	}

	@SuppressWarnings("unchecked")
	private void initVars() {
		mSelection = -1;
		mAdapter = new IMAdapter(this);
		furtherHandle = false;
		mListShareInfo = null;
		mShareInfo = null;
		if (getIntent().hasExtra(ENavConsts.EShareParam)) { // 分享单个数据
			mShareInfo = (JTFile) getIntent().getSerializableExtra(ENavConsts.EShareParam);
			// 如果是知识的话调用大数据的接口
			/*
			if (mShareInfo.getmType() == JTFile.TYPE_KNOWLEDGE
					&& TextUtils.isEmpty(mShareInfo.getmSuffixName())) {
				showLoadingDialog();
				UserReqUtil.doGetTreatedHtml(this, mBindData, UserReqUtil.getTreatedHtmlParams(mShareInfo.mUrl), null);
			}
			*/
		} 
		else if (getIntent().hasExtra(ENavConsts.EShareParamList)) { // 分享一组数据
			mListShareInfo = (ArrayList<JTFile>) getIntent().getSerializableExtra(
					ENavConsts.EShareParamList);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void initControls() {
		// 创建新畅聊
		newChatTv = (TextView) findViewById(R.id.newChatTv);
		newChatTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mShareInfo != null){
					ENavigate.startIMRelationSelectActivityEx(ShareActivity.this, mShareInfo);
				}
				else if(mListShareInfo != null){
					ENavigate.startIMRelationSelectActivityEx(ShareActivity.this, mListShareInfo);
				}
			}
		});
		// 下拉刷新框架
		containerSrl = (SwipeRefreshLayout) findViewById(R.id.containerSrl);
		containerSrl.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		containerSrl.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				IMReqUtil.getListIMRecord(ShareActivity.this, mBindData, null, 0, PAGE_LENGTH);
			}
		});
		// 聊天列表
		chatLv = (BasicListView) findViewById(R.id.chatLv);
		chatLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {

				mSelection = arg2;
				final IMRecord imRecord = (IMRecord) mAdapter.getItem(arg2);

				if (mListShareInfo != null) { // 分享多个信息
					ArrayList<IMBaseMessage> listMsg = new ArrayList<IMBaseMessage>();
					for(JTFile jtFile : mListShareInfo){
						listMsg.add(convertJTFile2IMBaseMessage(imRecord.getType(), jtFile));
					}
					if(imRecord.getType() == IMBaseMessage.IM_TYPE_CHAT){ // 私聊
						 ENavigate.startIMActivity(ShareActivity.this, convertIMRecord2ChatDetail(imRecord), mListShareInfo);
					}
					else if(imRecord.getType() == IMBaseMessage.IM_TYPE_MUC){ // 群聊
						ENavigate.startIMGroupActivity(ShareActivity.this, imRecord.getId(), mListShareInfo);
					}
				}
				else if(mShareInfo != null){ // 分享单个信息
					if (mShareInfo.mType == JTFile.TYPE_JTCONTACT_OFFLINE
							|| mShareInfo.mType == JTFile.TYPE_JTCONTACT_ONLINE
							|| mShareInfo.mType == JTFile.TYPE_ORG_ONLINE
							|| mShareInfo.mType == JTFile.TYPE_ORG_OFFLINE) { // 转发人脉、用户、组织

						if (mShareInfo.mType == JTFile.TYPE_JTCONTACT_OFFLINE) {
							ENavigate.startIMCreateGroupCategoryActivity1(
									ShareActivity.this, convertIMRecord2ChatDetail(imRecord),
									convertJTFile2IMBaseMessage(imRecord.getType(), mShareInfo));
						} 
						else {
							if (imRecord.getType() == IMBaseMessage.IM_TYPE_CHAT) {
								/*
								ENavigate.startIMActivity(
										ShareActivity.this, convertIMRecord2ChatDetail(imRecord),
										convertJTFile2IMBaseMessage(imRecord.getType(), mShareInfo));
								*/
							} 
							else if(imRecord.getType() == IMBaseMessage.IM_TYPE_MUC){
								ENavigate.startIMGroupActivity(
										ShareActivity.this, imRecord.getId(),
										convertJTFile2IMBaseMessage(imRecord.getType(), mShareInfo));
							}
						}
					} 
					else if (mShareInfo.mType == JTFile.TYPE_TEXT
							|| mShareInfo.mType == JTFile.TYPE_IMAGE
							|| mShareInfo.mType == JTFile.TYPE_AUDIO
							|| mShareInfo.mType == JTFile.TYPE_VIDEO
							|| mShareInfo.mType == JTFile.TYPE_FILE) { // 转发文本、图片、语音、视频、文件

						if (imRecord.getType() == IMBaseMessage.IM_TYPE_CHAT) { // 分享到私聊
//							ENavigate.startIMActivity(ShareActivity.this, convertIMRecord2ChatDetail(imRecord),
//									convertJTFile2IMBaseMessage(imRecord.getType(), mShareInfo));
						} 
						else if (imRecord.getType() == IMBaseMessage.IM_TYPE_MUC) { // 分享到群聊
							ENavigate.startIMGroupActivity(ShareActivity.this, imRecord.getId(),
									convertJTFile2IMBaseMessage(imRecord.getType(), mShareInfo));
						}
					} 
					else if(mShareInfo.mType == JTFile.TYPE_CONFERENCE){ // 转发会议
						
						if (imRecord.getType() == IMBaseMessage.IM_TYPE_CHAT) { // 分享到私聊
//							ENavigate.startIMActivity(
//									ShareActivity.this, convertIMRecord2ChatDetail(imRecord),
//									convertJTFile2IMBaseMessage(imRecord.getType(), mShareInfo));
						} 
						else if (imRecord.getType() == IMBaseMessage.IM_TYPE_MUC) { // 分享到群聊
							ENavigate.startIMGroupActivity(
									ShareActivity.this, imRecord.getId(),
									convertJTFile2IMBaseMessage(imRecord.getType(), mShareInfo));
						}
					}
					else if (mShareInfo.mType == JTFile.TYPE_KNOWLEDGE
							|| mShareInfo.mType == JTFile.TYPE_KNOWLEDGE2
							|| mShareInfo.mType == JTFile.TYPE_REQUIREMENT) { // 转发知识、新知识、需求

						String title = "";
						if (mShareInfo.mType == JTFile.TYPE_KNOWLEDGE) { // 知识
							title = mShareInfo.mSuffixName;
						} 
						else if (mShareInfo.mType == JTFile.TYPE_REQUIREMENT) { // 需求
							title = mShareInfo.mFileName;
						} 
						else if (mShareInfo.mType == JTFile.TYPE_KNOWLEDGE2) { // 新知识
							title = mShareInfo.reserved2;
						}
						EUtil.showShareDialog(ShareActivity.this, title,
								new CustomDlgClickListener() {
									@Override
									public void onPositiveClick(String message) {
										if (mShareInfo.mType == JTFile.TYPE_KNOWLEDGE
												|| mShareInfo.mType == JTFile.TYPE_KNOWLEDGE2) { // 知识、新知识
											mShareInfo.mFileName = message;
											if (imRecord.getType() == IMRecord.TYPE_CHAT) { // 分享到私聊
												IMBaseMessage shareMsg = new ChatMessage(message);
												shareMsg.setJtFile(mShareInfo);
												shareMsg.setType(mShareInfo);
												ENavigate.startIMActivity(ShareActivity.this,
														convertIMRecord2ChatDetail(imRecord), mShareInfo);
											} 
											else if(imRecord.getType() == IMRecord.TYPE_MUC){ // 分享到群聊
												IMBaseMessage shareMsg = new MUCMessage(message);
												shareMsg.setJtFile(mShareInfo);
												shareMsg.setType(mShareInfo);
												ENavigate.startIMGroupActivity(ShareActivity.this,
														imRecord.getId(), mShareInfo);
											}
										} 
										else if (mShareInfo.mType == JTFile.TYPE_REQUIREMENT) { // 需求
											mShareInfo.mSuffixName = message;
											if (imRecord.getType() == IMBaseMessage.IM_TYPE_CHAT) { // 分享到私聊
												IMBaseMessage shareMsg = new ChatMessage(message);
												shareMsg.setJtFile(mShareInfo);
												shareMsg.setType(mShareInfo);
//												ENavigate.startIMActivity(ShareActivity.this,
//																convertIMRecord2ChatDetail(imRecord),shareMsg);
											} 
											else if (imRecord.getType() == IMBaseMessage.IM_TYPE_MUC) { // 分享到群聊
												IMBaseMessage shareMsg = new MUCMessage(message);
												shareMsg.setJtFile(mShareInfo);
												shareMsg.setType(mShareInfo);
												ENavigate.startIMGroupActivity(ShareActivity.this, imRecord.getId(), shareMsg);
											}
										}
									}
								});

					}
					return;
				}
			}
		});
		chatLv.setAdapter(mAdapter);
	}

	private ChatDetail convertIMRecord2ChatDetail(IMRecord imRecord) {

		ChatDetail chatDetail = new ChatDetail();
		// 对方id
		chatDetail.setThatID(imRecord.getId());
		String imgUrl = "";
		if (imRecord.getListImageUrl().size() > 0) {
			imgUrl = imRecord.getListImageUrl().get(0);
		}
		// 对方头像
		chatDetail.setThatImage(imgUrl);
		// 对象姓名
		chatDetail.setThatName(imRecord.getTitle());
		return chatDetail;
	}

	private IMBaseMessage convertJTFile2IMBaseMessage(int type, JTFile jtFile) {

		IMBaseMessage shareMsg = null;
		if (type == IMBaseMessage.IM_TYPE_CHAT) { // 私聊
			shareMsg = new ChatMessage("");
		}
		else if(type == IMBaseMessage.IM_TYPE_MUC){ // 群聊
			shareMsg = new MUCMessage("");
		}
		if (jtFile.mType == JTFile.TYPE_JTCONTACT_OFFLINE
				|| jtFile.mType == JTFile.TYPE_JTCONTACT_ONLINE
				|| jtFile.mType == JTFile.TYPE_ORG_ONLINE
				|| jtFile.mType == JTFile.TYPE_ORG_OFFLINE) { // 转发人脉、组织、用户

		} 
		else if (jtFile.mType == JTFile.TYPE_TEXT) { // 转发文本
			shareMsg.setContent(jtFile.mFileName);
		} 
		else if (jtFile.mType == JTFile.TYPE_IMAGE) { // 转发图片
			shareMsg.setContent("[图片]");
		} 
		else if (jtFile.mType == JTFile.TYPE_AUDIO) { // 转发语音
			shareMsg.setContent("[语音]");
		} 
		else if (jtFile.mType == JTFile.TYPE_FILE) { // 转发文件
			shareMsg.setContent("[文件]");
		} 
		else if (jtFile.mType == JTFile.TYPE_VIDEO) { // 转发视频
			shareMsg.setContent("[视频]");
		} 
		else if (jtFile.mType == JTFile.TYPE_KNOWLEDGE2
				|| jtFile.mType == JTFile.TYPE_KNOWLEDGE) { // 转发新旧知识
			if(TextUtils.isEmpty(jtFile.mFileName)){
				shareMsg.setContent("[知识]");
			}
			else{
				shareMsg.setContent(jtFile.mFileName);
			}
		} 
		else if(jtFile.mType == JTFile.TYPE_REQUIREMENT){ // 转发需求
			if(TextUtils.isEmpty(jtFile.mSuffixName)){
				shareMsg.setContent("[需求]");
			}
			else{
				shareMsg.setContent(jtFile.mSuffixName);
			}
		}
		else if (jtFile.mType == JTFile.TYPE_CONFERENCE) { // 转发会议
			shareMsg.setContent("[会议]");
		}
		shareMsg.setJtFile(jtFile);
		return shareMsg;
	}

	@Override
	public void onResume() {
		super.onResume();
		containerSrl.setRefreshing(true);
		IMReqUtil.getListIMRecord(this, mBindData, null, 0, PAGE_LENGTH);
	}

	private IBindData mBindData = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {

			if (tag == EAPIConsts.IMReqType.IM_REQ_GET_LISTIM) { // 获取畅聊列表
				containerSrl.setRefreshing(false);
				if (object != null) {
					MGetListIMRecord getListIMRecord = (MGetListIMRecord) object;
					JTPage page = getListIMRecord.getJtPage();
					if (page != null) {
						if ((page.getLists() != null)
								&& (page.getLists().size() > 0)) {
							List<IMRecord> listIMRecord = new ArrayList<IMRecord>();
							for (int i = 0; i < page.getLists().size(); i++) {
								listIMRecord.add((IMRecord) page.getLists()
										.get(i));
							}
							mAdapter.update(listIMRecord);
						}
					}
				}
			}
			else if (tag == EAPIConsts.ReqType.GET_TREATED_HTML) { // 处理网页
				if (object != null) {
					DataBox dataBox = (DataBox) object;
					if (dataBox.mTreatedHtml != null) {
						mShareInfo.mSuffixName = dataBox.mTreatedHtml.getTitle();
					}
					if (furtherHandle) {
						IMRecord imRecord = (IMRecord) mAdapter.getItem(mSelection);
						imRecord.setNewCount(0);
						if (imRecord.getType() == IMRecord.TYPE_CHAT) { // 分享到私聊
							IMBaseMessage shareMsg = new ChatMessage(mShareInfo.mFileName);
							shareMsg.setJtFile(mShareInfo);
							shareMsg.setType(mShareInfo);
//							ENavigate.startIMActivity(ShareActivity.this,
//									convertIMRecord2ChatDetail(imRecord), shareMsg);
						} 
						else if(imRecord.getType() == IMRecord.TYPE_MUC){ // 分享到群聊
							IMBaseMessage shareMsg = new MUCMessage(mShareInfo.mFileName);
							shareMsg.setJtFile(mShareInfo);
							shareMsg.setType(mShareInfo);
							ENavigate.startIMGroupActivity(ShareActivity.this,
									imRecord.getId(), shareMsg);
						}
					} 
					else {
						dismissLoadingDialog();
					}
				} 
				else {
					dismissLoadingDialog();
				}
			}
		}
	};
}
