package com.tr.ui.communities.adapter;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import m.framework.utils.Utils;

import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.CommunityReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.api.KnowledgeReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.db.VoiceFileDBManager;
import com.tr.image.ImageLoader;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.service.FileDownloadService;
import com.tr.ui.common.FilePreviewActivity;
import com.tr.ui.communities.im.ChatBaseActivity;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.people.cread.NewConnectionsActivity;
import com.tr.ui.widgets.ChatDialog;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.SmileyParser;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.FileDownloader;
import com.utils.common.FileUploader;
import com.utils.common.JTDateUtils;
import com.utils.common.OpenFiles;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;
import com.utils.picture.PictureManageUtil;
import com.utils.string.StringUtils;
import com.utils.time.Util;

import android.widget.EditText;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class ChatAdapter extends BaseAdapter{

	private Context mContext;
	private ChatBaseActivity activity;
	private List<IMBaseMessage> messageList;
	// 控制是都支持头像的长按功能 默认true
	protected boolean isAvatarLongClickable = true;
	private IBindData bd;
	private String fileIds;
	// 缓存的超链接
	protected String cacheUrl = "";
	private VoiceFileDBManager voiceFileManager;
	private DownloadManager downloadManager;
	private String filePath;
	/** 图片视频的缩放大小（数值按照dp的标准进行等比例放大或者缩小） */
	private static final int SCALESIZE = 200;
	// 文件上传队列
	protected List<FileUploader> mListUploader = new ArrayList<FileUploader>();
	// 下载服务
	private FileDownloadService mDownloadService;
	private SmileyParser parser;

	private Cursor cursor = null;
	protected MUCDetail mucDetail;// 群聊详情
	protected String communityId;
	
	// 播放语音相关
	private AnimationDrawable mVoiceAnim = null; // 当前语音动画
	private ImageView voiceIv = null; // 当前语音控件
	private String mVoicePath = ""; // 当前播放的语音文件路径
	private boolean mPlaying = false; // 是否在播放
	private String curVoiceUrl = ""; // 当前播放的音频文件的url

	// 录音相关
	private String mAudioName = ""; // 临时语音文件名
	private String mAudioPath = ""; // 临时语音文件全路径
	private ChatDialog chatDlg; // 聊天对话框
	// 录音相关
	private MediaRecorder mRecorder;
	private MediaPlayer mPlayer;
	private Handler mHandler;
	private EditText textEt;
	private List<ConnectionsMini> atConnectionsMinis;
	
	private static final String LEFTSPECCHAR = ((char) 0X1B) + "";
	private static final String RIGHTSPECCHAR = ((char) 0X11) + "";

	public ChatAdapter(Context mContext, ChatBaseActivity activity){
		this.mContext = mContext;
		this.activity = activity;
		voiceFileManager = new VoiceFileDBManager(mContext);
		downloadManager = (DownloadManager) mContext.getSystemService(mContext.DOWNLOAD_SERVICE);
		parser = SmileyParser.getInstance(mContext);
	}
	
	public void setFDService(FileDownloadService mDownloadService){
		this.mDownloadService = mDownloadService;
	}
	
	public void setData(MUCDetail mucDetail, IBindData bd, String communityId){
		this.mucDetail = mucDetail;
		this.bd = bd;
		this.communityId = communityId;
	}

	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}
	
	public void setList(List<IMBaseMessage> messageList) {
		this.messageList = messageList;
	}

	public void setView(EditText textEt) {
		this.textEt = textEt;
	}

	public void setConn(List<ConnectionsMini> atConnectionsMinis) {
		this.atConnectionsMinis = atConnectionsMinis;
	}
	
	/**
	 * 返回消息列表
	 * 
	 * @return
	 */
	public List<IMBaseMessage> getData() {
		return this.messageList;
	}
	
	@Override
	public int getCount() {
		return messageList != null ? messageList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return messageList != null ? messageList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.im_chat_message_item, null);
			holder = new ViewHolder();
			holder.viewBG = (View) convertView.findViewById(R.id.im_message_bg);
			holder.leftLayout = (RelativeLayout) convertView.findViewById(R.id.left_letter_layout);
			holder.rightLayout = (RelativeLayout) convertView.findViewById(R.id.right_letter_layout);

			holder.leftContent = (TextView) convertView.findViewById(R.id.left_letter_content);
			holder.rightContent = (TextView) convertView.findViewById(R.id.right_letter_content);

			holder.leftHead = (CircleImageView) convertView.findViewById(R.id.head);
			holder.rightHead = (CircleImageView) convertView.findViewById(R.id.right_head);
			holder.leftName = (TextView) convertView.findViewById(R.id.leftuserName);
			holder.rightName = (TextView) convertView.findViewById(R.id.rightuserName);

			holder.letterDate = (TextView) convertView.findViewById(R.id.letter_date);
			holder.systemMessage = (TextView) convertView.findViewById(R.id.system_message);
			holder.sendMsgProgress = (ProgressBar) convertView.findViewById(R.id.send_message_progress);
			holder.sendMsgFail = (ImageView) convertView.findViewById(R.id.send_message_fail);

			// 语音部分
			holder.leftVoiceLl = (LinearLayout) convertView.findViewById(R.id.leftVoiceLl);
			holder.leftVoiceDurationLl = (LinearLayout) convertView.findViewById(R.id.leftVoiceDurationLl);
			holder.leftVoiceIv = (ImageView) convertView.findViewById(R.id.leftVoiceIv);
			holder.leftVoiceDurationTv = (TextView) convertView.findViewById(R.id.leftVoiceDurationTv);
			holder.leftVoiceLoadingPb = (ProgressBar) convertView.findViewById(R.id.leftVoiceLoadingPb);

			holder.rightVoiceLl = (LinearLayout) convertView.findViewById(R.id.rightVoiceLl);
			holder.rightVoiceDurationLl = (LinearLayout) convertView.findViewById(R.id.rightVoiceDurationLl);
			holder.rightVoiceLoadingPb = (ProgressBar) convertView.findViewById(R.id.rightVoiceLoadingPb);
			holder.rightVoiceDurationTv = (TextView) convertView.findViewById(R.id.rightVoiceDurationTv);
			holder.rightVoiceIv = (ImageView) convertView.findViewById(R.id.rightVoiceIv);

			// 图片部分
			holder.leftImageRl = (RelativeLayout) convertView.findViewById(R.id.leftImageRl);
			holder.leftImageIv = (ImageView) convertView.findViewById(R.id.leftImageIv);

			holder.rightImageRl = (RelativeLayout) convertView.findViewById(R.id.rightImageRl);
			holder.rightImageIv = (ImageView) convertView.findViewById(R.id.rightImageIv);

			// 视频部分
			holder.leftVideoRl = (RelativeLayout) convertView.findViewById(R.id.leftVideoRl);
			holder.leftVideoIv = (ImageView) convertView.findViewById(R.id.leftVideoIv);
			holder.leftPlayIv = (ImageView) convertView.findViewById(R.id.leftPlayIv);

			holder.rightVideoRl = (RelativeLayout) convertView.findViewById(R.id.rightVideoRl);
			holder.rightVideoIv = (ImageView) convertView.findViewById(R.id.rightVideoIv);
			holder.rightPlayIv = (ImageView) convertView.findViewById(R.id.rightPlayIv);

			// 分享部分
			holder.leftShareLl = (LinearLayout) convertView.findViewById(R.id.leftShareLl);
			holder.leftShareTopLl = (LinearLayout) convertView.findViewById(R.id.leftShareTopLl);
			holder.leftShareMsgTv = (TextView) convertView.findViewById(R.id.leftShareMsgTv);
			holder.leftShareBottomLl = (LinearLayout) convertView.findViewById(R.id.leftShareBottomLl);
			holder.leftShareImageIv = (ImageView) convertView.findViewById(R.id.leftShareImageIv);
			holder.leftShareTypeIv = (ImageView) convertView.findViewById(R.id.leftShareTypeIv);
			holder.leftShareTitleTv = (TextView) convertView.findViewById(R.id.leftShareTitleTv);
			holder.leftShareContentTv = (TextView) convertView.findViewById(R.id.leftShareContentTv);
			holder.leftShareLinkIv = (TextView) convertView.findViewById(R.id.leftShareLinkIv);

			holder.rightShareLl = (LinearLayout) convertView.findViewById(R.id.rightShareLl);
			holder.rightShareTopLl = (LinearLayout) convertView.findViewById(R.id.rightShareTopLl);
			holder.rightShareMsgTv = (TextView) convertView.findViewById(R.id.rightShareMsgTv);
			holder.rightShareBottomLl = (LinearLayout) convertView.findViewById(R.id.rightShareBottomLl);
			holder.rightShareImageIv = (ImageView) convertView.findViewById(R.id.rightShareImageIv);
			holder.rightShareTypeIv = (ImageView) convertView.findViewById(R.id.rightShareTypeIv);
			holder.rightShareTitleTv = (TextView) convertView.findViewById(R.id.rightShareTitleTv);
			holder.rightShareContentTv = (TextView) convertView.findViewById(R.id.rightShareContentTv);
			holder.rightShareLinkIv = (TextView) convertView.findViewById(R.id.rightShareLinkIv);

			// 分享文件部分
			holder.leftFileLl = (LinearLayout) convertView.findViewById(R.id.leftFileLl);
			holder.leftFileTypeIv = (ImageView) convertView.findViewById(R.id.leftFileTypeIv);
			holder.leftFileNameTv = (TextView) convertView.findViewById(R.id.leftFileNameTv);
			holder.leftFileSizeTv = (TextView) convertView.findViewById(R.id.leftFileSizeTv);
			holder.leftFileStatusTv = (TextView) convertView.findViewById(R.id.leftFileStatusTv);
			holder.leftFileProgressPb = (ProgressBar) convertView.findViewById(R.id.leftFileProgressPb);

			holder.rightFileLl = (LinearLayout) convertView.findViewById(R.id.rightFileLl);
			holder.rightFileTypeIv = (ImageView) convertView.findViewById(R.id.rightFileTypeIv);
			holder.rightFileNameTv = (TextView) convertView.findViewById(R.id.rightFileNameTv);
			holder.rightFileSizeTv = (TextView) convertView.findViewById(R.id.rightFileSizeTv);
			holder.rightFileStatusTv = (TextView) convertView.findViewById(R.id.rightFileStatusTv);
			holder.rightFileProgressPb = (ProgressBar) convertView.findViewById(R.id.rightFileProgressPb);

			holder.rightCnsRl = (RelativeLayout) convertView.findViewById(R.id.rightCnsRl);
			holder.rightCnsIcon = (ImageView) convertView.findViewById(R.id.rightCnsIcon);
			holder.rightCnsTitle = (TextView) convertView.findViewById(R.id.rightCnsTitle);
			holder.rightCnsName1 = (TextView) convertView.findViewById(R.id.rightCnsName1);
			holder.rightCnsName2 = (TextView) convertView.findViewById(R.id.rightCnsName2);
			holder.rightCnsName3 = (TextView) convertView.findViewById(R.id.rightCnsName3);

			holder.leftCnsRl = (RelativeLayout) convertView.findViewById(R.id.leftCnsRl);
			holder.leftCnsIcon = (ImageView) convertView.findViewById(R.id.leftCnsIcon);
			holder.leftCnsTitle = (TextView) convertView.findViewById(R.id.leftCnsTitle);
			holder.leftCnsName1 = (TextView) convertView.findViewById(R.id.leftCnsName1);
			holder.leftCnsName2 = (TextView) convertView.findViewById(R.id.leftCnsName2);
			holder.leftCnsName3 = (TextView) convertView.findViewById(R.id.leftCnsName3);

			holder.chat_blank_ll = (LinearLayout) convertView.findViewById(R.id.chat_blank_ll);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final IMBaseMessage item = messageList.get(position);
		
		if (position == (messageList.size() - 1)) {
			holder.chat_blank_ll.setVisibility(View.VISIBLE);
		} else {
			holder.chat_blank_ll.setVisibility(View.GONE);

		}

		// 长按事件（根据类别区分处理）
		OnLongClickListener longClickListener = new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {

				// 左侧头像
				if (isAvatarLongClickable == true) {
					if (v == holder.leftHead) {
						String leftName = holder.leftName.getText()
								.toString();
						if (!textEt.getText().toString().contains(LEFTSPECCHAR + "@" + leftName + RIGHTSPECCHAR)) {
							textEt.setText(textEt.getText() + LEFTSPECCHAR + "@" + leftName + RIGHTSPECCHAR + " ");

							textEt.setSelection(textEt.getText().length());

							String senderId = (String) holder.leftHead.getTag();
							ConnectionsMini mini = new ConnectionsMini();
							mini.setId(senderId);
							mini.setName(leftName);
							atConnectionsMinis.add(mini);
						}
						return true;
					}
				}
				showItemLongClickDialog(item, position);
				return true;
			}
		};
		
		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int type = getSenderTypeByMessage(item);
				switch (v.getId()) {
				case R.id.head: // 左侧头像
					if ((type + 1) == ConnectionsMini.UT_ORG) {
						ENavigate.startOrgMyHomePageActivityByUseId(mContext, Long.parseLong(item.getSenderID()));
					} else {
						ENavigate.startRelationHomeActivity(mContext, item.getSenderID(),true,1);
					}
					break;
				case R.id.right_head: // 右侧头像
					ENavigate.startRelationHomeActivity(mContext, App.getUserID(),true,1);
					break;
				case R.id.leftShareLl: // 分享
				case R.id.rightShareLl:
				try {
					if (item.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 需求
						ENavigate.startNeedDetailsActivity(mContext, item.getJtFile().mTaskId, 2);
					} else if (item.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 知识e
						ENavigate.startShareDetailActivity(mContext, item.getJtFile().toKnowledgeMini());
					} else if (item.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 新知识
						ENavigate.startKnowledgeOfDetailActivity(activity, Long.parseLong(item.getJtFile().mTaskId),
								Integer.parseInt(item.getJtFile().getReserved1()));
					} else if (item.getType() == IMBaseMessage.TYPE_CONFERENCE) { // 会议
						ENavigate.startSquareActivity(mContext, Long.parseLong(item.getJtFile().mTaskId),0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
					break;
				case R.id.leftFileLl: { // 左侧文件
					Intent intent = new Intent(mContext, FilePreviewActivity.class);
					intent.putExtra(EConsts.Key.JT_FILE, item.getJtFile());
					mContext.startActivity(intent);
				}
					break;
				case R.id.rightFileLl: // 右侧文件
					if (item.getJtFile() != null && !TextUtils.isEmpty(item.getJtFile().mUrl)) {
						Intent intent = new Intent(mContext, FilePreviewActivity.class);
						intent.putExtra(EConsts.Key.JT_FILE, item.getJtFile());
						mContext.startActivity(intent);
					}
					break;
				case R.id.leftImageRl: // 左侧图片
				case R.id.rightImageRl: // 右侧图片
					ENavigate.startImageBrowserActivity(activity, communityId, item.getMessageID(), getMessageHasImg(messageList));
					break;
				case R.id.leftVoiceLl: { // 左侧语音
					// 语音下载中
					/*
					 * if(holder.leftVoiceLoadingPb.getVisibility() ==
					 * View.VISIBLE){ return; }
					 */
					// 当前音频的url
					curVoiceUrl = item.getJtFile().mUrl;
					try {
						File file = new File(getVoiceFileDir(), item.getJtFile().mFileName);
						if (file.exists() && file.length() == item.getJtFile().mFileSize) {
							// 判断语音时长
							if (StringUtils.isEmpty(item.getJtFile().reserved2)) {
								showToast("无法播放语音文件，文件可能已损坏");
								return;
							}
							// 播放音频
							startPlaying(
									file.getAbsolutePath(),
									holder.leftVoiceIv,
									Integer.parseInt(item.getJtFile().reserved2) * 1000);
						} else { // 是否已开始下载任务
							new AsyncTask<Void, Void, Long>() {

								@Override
								protected Long doInBackground(
										Void... params) {
									// 更新数据库状态
									long taskId = voiceFileManager.query(item.getJtFile().mUrl);
									return taskId;
								}

								@Override
								protected void onPostExecute(Long taskId) {
									super.onPostExecute(taskId);
									try {
										if (taskId < 0) { // 从未开始任务
											File dir = getVoiceFileDir();
											if (dir != null) {
												Request request = new Request(Uri.parse(item.getJtFile().mUrl));
												request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
												request.setDestinationUri(Uri.fromFile(new File(dir, item.getJtFile().mFileName))); // 设置文件下载位置
												voiceFileManager.insert(item.getJtFile().mUrl, downloadManager.enqueue(request));
												// 界面修改
												holder.leftVoiceLoadingPb.setVisibility(View.VISIBLE);
												holder.leftVoiceIv.setVisibility(View.GONE);
											} else {
												showToast("没有SD卡，无法下载语音文件");
											}
										} else { // 任务已经开始
											Query query = new Query().setFilterById(taskId);
											Cursor cursor = null;
											try {
												cursor = downloadManager.query(query);
												int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
												switch (status) {
												case DownloadManager.STATUS_PENDING:
												case DownloadManager.STATUS_RUNNING:
													holder.leftVoiceLoadingPb.setVisibility(View.VISIBLE);
													holder.leftVoiceIv.setVisibility(View.GONE);
													break;
												default:
													// 删除旧任务
													downloadManager.remove(taskId);
													voiceFileManager.delete(taskId);
													try {
													// 开始新任务
													File dir = getVoiceFileDir();
													if (dir != null) {
														Request request = new Request(Uri.parse(item.getJtFile().mUrl));
														request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
														request.setDestinationUri(Uri.fromFile(new File(dir, item.getJtFile().mFileName))); // 设置文件下载位置

														voiceFileManager.insert(item.getJtFile().mUrl, downloadManager.enqueue(request));
														// 界面修改
														holder.leftVoiceLoadingPb.setVisibility(View.VISIBLE);
														holder.leftVoiceIv.setVisibility(View.GONE);
													} else {
														showToast("没有SD卡，无法下载语音文件");
													}
													} catch (Exception ex) {
														ex.printStackTrace();
													}
													break;
												}
											} catch (Exception e) {
												if (cursor != null) {
													cursor.close();
												}
												holder.leftVoiceLoadingPb.setVisibility(View.GONE);
												holder.leftVoiceIv.setVisibility(View.VISIBLE);
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}.execute();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
					break;
				case R.id.rightVoiceLl: { // 右侧语音

					// 语音下载中
					/*
					 * if(holder.rightVoiceLoadingPb.getVisibility() ==
					 * View.VISIBLE){ return; }
					 */
					// 正在播放的音频的url
					curVoiceUrl = item.getJtFile().mUrl;
					try {
					File file = new File(getVoiceFileDir(), item.getJtFile().mFileName);
					if (file.exists() && file.length() == item.getJtFile().mFileSize) {
						if (StringUtils.isEmpty(item.getJtFile().reserved2)) {
							showToast("无法播放语音文件，文件可能已损坏");
							return;
						}
						// 开始播放音频和动画
						startPlaying(file.getAbsolutePath(), holder.rightVoiceIv, Integer.parseInt(item.getJtFile().reserved2) * 1000);
					} else if (item.getJtFile().mUrl != null && item.getJtFile().mUrl.length() > 0) { // 文件已上传
						new AsyncTask<Void, Void, Long>() {

							@Override
							protected Long doInBackground(
									Void... params) {
								long taskId = voiceFileManager.query(item.getJtFile().mUrl);
								return taskId;
							}

							@Override
							protected void onPostExecute(Long taskId) {
								super.onPostExecute(taskId);
								if (taskId < 0) { // 从未开始任务
									File dir = getVoiceFileDir();
									if (dir != null) {
										Request request = new Request(Uri.parse(item.getJtFile().mUrl));
										request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
										request.setDestinationUri(Uri.fromFile(new File(dir, item.getJtFile().mFileName))); // 设置文件下载位置
										voiceFileManager.insert(item.getJtFile().mUrl, downloadManager.enqueue(request));
										// 界面修改
										holder.rightVoiceLoadingPb.setVisibility(View.VISIBLE);
										holder.rightVoiceIv.setVisibility(View.GONE);
									} else {
										showToast("没有SD卡，无法下载语音文件");
									}
								} else {
									Query query = new Query().setFilterById(taskId);
									Cursor cursor = null;
									try {
										cursor = downloadManager.query(query);
										if (cursor != null) {
											int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
											switch (status) {
											case DownloadManager.STATUS_PENDING:
											case DownloadManager.STATUS_RUNNING:
												holder.rightVoiceLoadingPb.setVisibility(View.VISIBLE);
												holder.rightVoiceIv.setVisibility(View.GONE);
												break;
											default:
												// 删除旧任务
												downloadManager.remove(taskId);
												voiceFileManager.delete(taskId);
												// 开始新任务
												File dir = getVoiceFileDir();
												if (dir != null) {
													Request request = new Request(Uri.parse(item.getJtFile().mUrl));
													request.setNotificationVisibility(Request.VISIBILITY_HIDDEN); // 不显示下载进度
													request.setDestinationUri(Uri.fromFile(new File(dir, item.getJtFile().mFileName))); // 设置文件下载位置
													voiceFileManager.insert(item.getJtFile().mUrl, downloadManager.enqueue(request));
													// 界面修改
													holder.rightVoiceLoadingPb.setVisibility(View.VISIBLE);
													holder.rightVoiceIv.setVisibility(View.GONE);
												} else {
													showToast("没有SD卡，无法下载语音文件");
												}
												break;
											}
											if (cursor!=null) {
												cursor.close();
											}
											
										}
									} catch (Exception e) {
										if (cursor != null) {
											cursor.close();
										}
										holder.rightVoiceLoadingPb.setVisibility(View.GONE);
										holder.rightVoiceIv.setVisibility(View.VISIBLE);
									}
								}
							}
						}.execute();

					} else {
						file.delete(); // 删除不完整文件
						showToast("语音文件不存在");
					}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
					break;
				case R.id.leftPlayIv: // 左侧视频
					try {
						File file = new File(EUtil.getAppCacheFileDir(mContext), item.getJtFile().mFileName);
						if (file.exists() && file.length() == item.getJtFile().mFileSize) { // 文件已经下载到本地
							OpenFiles.open(mContext, file.getAbsolutePath());
						} else {
							Intent intent = new Intent(mContext, FilePreviewActivity.class);
							intent.putExtra(EConsts.Key.JT_FILE, item.getJtFile());
							mContext.startActivity(intent);
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
					break;
				case R.id.rightPlayIv: // 右侧视频
					break;
				case R.id.leftCnsRl: // 左侧关系
				case R.id.rightCnsRl: // 右侧关系
					int mtype = item.getJtFile().getmType()!=0?item.getJtFile().getmType():item.getType();
					if (!StringUtils.isEmpty(item.getJtFile().mTaskId)) {
						if (item.getJtFile() != null) {
							if (JTFile.TYPE_JTCONTACT_ONLINE == mtype) { // 线上用户
								ENavigate.startRelationHomeActivity(mContext, item.getJtFile().mTaskId,true,1);
							}
							// 组织
							else if (JTFile.TYPE_ORG_ONLINE == mtype) {
								ENavigate.startOrgMyHomePageActivityByUseId(mContext, Long.parseLong(item.getJtFile().mTaskId));	
							} else if (JTFile.TYPE_ORG_OFFLINE == mtype) {
								ENavigate.startOrgMyHomePageActivity(mContext, Long.parseLong(item.getJtFile().mTaskId),
												Long.parseLong(item.getJtFile().reserved2),
												true,
												ENavConsts.type_details_org);
							} else if (JTFile.TYPE_JTCONTACT_OFFLINE == mtype) {//人脉
//								ENavigate.startContactsDetailsActivity(mContext, 2, Long.parseLong(item.getJtFile().mTaskId),1456, 0);
								ENavigate.startRelationHomeActivity(mContext, item.getJtFile().mTaskId,false,2);
							} else if (JTFile.TYPE_ORGANIZATION == mtype) {
								ENavigate.startOrgMyHomePageActivityByUseId(mContext, Long.parseLong(item.getJtFile().mTaskId));	
							} else if (JTFile.TYPE_CLIENT == mtype) {
								ENavigate.startClientDedailsActivity(mContext, Long.parseLong(item.getJtFile().mTaskId), 1, 6);
							}else if(IMBaseMessage.TYPE_JTCONTACT_OFFLINE ==item.getType() ){
								ENavigate.startContactsDetailsActivity(mContext, 2, Long.parseLong(item.getJtFile().mTaskId),0,0);
							}
						}
					}
					break;
				}
			}
		};
		
		if (item.isHide()) {
			// 如果该记录隐藏,则隐藏记录
			convertView.setVisibility(View.GONE);
			holder.viewBG.setVisibility(View.GONE);
			holder.leftLayout.setVisibility(View.GONE);
			holder.rightLayout.setVisibility(View.GONE);
			holder.systemMessage.setVisibility(View.GONE);
			holder.letterDate.setVisibility(View.GONE);
			return convertView;
		} else {
			// 如果没隐藏，则显示
			holder.viewBG.setVisibility(View.VISIBLE);
			convertView.setVisibility(View.VISIBLE);
		}
		
		// 点击头像
		holder.leftHead.setOnClickListener(clickListener);
		holder.rightHead.setOnClickListener(clickListener);
		holder.leftHead.setTag(item.getSenderID());

		// 长按头像
		holder.leftHead.setOnLongClickListener(longClickListener);
		holder.rightHead.setOnLongClickListener(longClickListener);

		// 点击知识消息
		holder.leftShareLl.setOnClickListener(clickListener);
		holder.rightShareLl.setOnClickListener(clickListener);

		// 点击文件消息
		holder.leftFileLl.setOnClickListener(clickListener);
		holder.rightFileLl.setOnClickListener(clickListener);
		
		if (item != null && item.getContent() != null) {
			String body = item.getContent().trim();
			if (KeelLog.DEBUG) {
				KeelLog.e("发布内容 " + body);
			}
			if (item.getSenderType() == IMBaseMessage.MSG_SYS_SEND) {// 发送系统消息
				holder.leftLayout.setVisibility(View.GONE);
				holder.rightLayout.setVisibility(View.GONE);
				holder.systemMessage.setVisibility(View.VISIBLE);
				holder.systemMessage.setText(body);
			} else if (item.getSenderType() == IMBaseMessage.MSG_MY_SEND) { // 发送显示右边

				// 自己发送的消息
				holder.leftLayout.setVisibility(View.GONE);
				holder.systemMessage.setVisibility(View.GONE);
				holder.rightLayout.setVisibility(View.VISIBLE);

				if(TextUtils.isEmpty(getImageByMessage(item))){
					if(getSenderTypeByMessage(item)+1 == ConnectionsMini.UT_ORG){
						holder.rightHead.setImageResource(R.drawable.default_portrait116);
					}else{
						holder.rightHead.setImageResource(R.drawable.ic_default_avatar);
					}
				}else{
					if(getSenderTypeByMessage(item)+1 == ConnectionsMini.UT_ORG){
						com.utils.common.Util.initAvatarImage(mContext, holder.rightHead, item.getSenderName(), getImageByMessage(item), 1, 2);
					}else{
						com.utils.common.Util.initAvatarImage(mContext, holder.rightHead, item.getSenderName(), getImageByMessage(item), 1, 1);
					}
				}
				holder.rightName.setText(item.getSenderName());

				if (IMBaseMessage.TYPE_IMAGE == item.getType()) { // 右侧图片

					holder.rightVideoRl.setVisibility(View.GONE);
					holder.rightContent.setVisibility(View.GONE);
					holder.rightVoiceLl.setVisibility(View.GONE);
					holder.rightShareLl.setVisibility(View.GONE);
					holder.rightFileLl.setVisibility(View.GONE);
					holder.rightCnsRl.setVisibility(View.GONE);
					holder.rightImageRl.setVisibility(View.VISIBLE);

					if (item.getJtFile() != null) {
						if(!TextUtils.isEmpty(item.getJtFile().mUrl)){ // 图片已上传
							ImageLoader.load(holder.rightImageIv, 130,item.getJtFile().mUrl, R.drawable.hy_chat_right_pic);
							}
						else{ // 图片不存在
							holder.rightImageIv.setImageResource(R.drawable.hy_chat_right_pic);
						}
					}else{
						holder.rightImageIv.setImageResource(R.drawable.hy_chat_right_pic);
					}
					// 长按事件
					holder.rightImageRl.setOnLongClickListener(longClickListener);
					// 点击事件
					holder.rightImageRl
							.setOnClickListener(clickListener);
				} else if (IMBaseMessage.TYPE_AUDIO == item.getType()) { // 右侧语音
					try {
					holder.rightVideoRl.setVisibility(View.GONE);
					holder.rightContent.setVisibility(View.GONE);
					holder.rightImageRl.setVisibility(View.GONE);
					holder.rightContent.setVisibility(View.GONE);
					holder.rightShareLl.setVisibility(View.GONE);
					holder.rightFileLl.setVisibility(View.GONE);
					holder.rightCnsRl.setVisibility(View.GONE);
					holder.sendMsgProgress.setVisibility(View.GONE);
					holder.rightVoiceLl.setVisibility(View.VISIBLE);

					if (item.getJtFile() != null) {

						// 显示时长
						if (TextUtils.isEmpty(item.getJtFile().reserved2)) {
							holder.rightVoiceDurationTv.setText("未知\"");
						} else {
							holder.rightVoiceDurationTv.setText(item.getJtFile().reserved2 + "\"");
						}
						// 语音文件
						final File file = new File(getVoiceFileDir(), item.getJtFile().mFileName);
						// 设置状态
						if (file.exists() && file.length() == item.getJtFile().mFileSize) {
							holder.rightVoiceLoadingPb.setVisibility(View.GONE);
							holder.rightVoiceIv.setVisibility(View.VISIBLE);
						} else if (!StringUtils.isEmpty(item
								.getJtFile().mUrl)) {
							new AsyncTask<Void, Void, Long>() {

								@Override
								protected Long doInBackground(
										Void... params) {
									long taskId = voiceFileManager.query(item.getJtFile().mUrl);

									Query query = new Query().setFilterById(taskId);
									try {
										cursor = downloadManager.query(query);
										if (cursor != null) {
											int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
										}
									} catch (Exception e) {
										if (cursor != null) {
											cursor.close();
										}
										return null;
									}
									return taskId;

								}

								@Override
								protected void onPostExecute(Long result) {
									super.onPostExecute(result);
									try {
										if (result >= 0) {
											holder.rightVoiceLoadingPb.setVisibility(View.GONE);
											holder.rightVoiceIv.setVisibility(View.VISIBLE);
										} else {
											holder.rightVoiceLoadingPb.setVisibility(View.GONE);
											holder.rightVoiceIv.setVisibility(View.VISIBLE);
										}
									} catch (Exception ex) {
										ex.printStackTrace();
									}

								}
							}.execute();

						} else {
							holder.rightVoiceLoadingPb.setVisibility(View.GONE);
							holder.rightVoiceIv.setVisibility(View.VISIBLE);
						}
						// 点击事件
						holder.rightVoiceLl.setOnClickListener(clickListener);
						// 长按事件
						holder.rightVoiceLl.setOnLongClickListener(longClickListener);
					}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else if (IMBaseMessage.TYPE_KNOWLEDGE == item.getType()) { // 右侧知识

					holder.rightVideoRl.setVisibility(View.GONE);
					holder.rightImageRl.setVisibility(View.GONE);
					holder.rightContent.setVisibility(View.GONE);
					holder.rightVoiceLl.setVisibility(View.GONE);
					holder.rightFileLl.setVisibility(View.GONE);
					holder.rightCnsRl.setVisibility(View.GONE);
					holder.rightShareLl.setVisibility(View.VISIBLE);

					if (item.getJtFile() != null) {

						holder.rightShareTopLl.setVisibility(View.VISIBLE);
						holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
						holder.rightShareMsgTv.setText("分享了[知识]");
						holder.rightShareImageIv.setVisibility(View.GONE);// 截图
						holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);// 类型
						holder.rightShareTitleTv.setVisibility(View.GONE);// 标题
						int httpIndex = item.getJtFile().mSuffixName.indexOf("http");// 内容
						if (!StringUtils.isEmpty(item.getJtFile().mSuffixName)) {
							if (httpIndex < 0) {
								holder.rightShareContentTv.setText(!TextUtils.isEmpty(item
										.getJtFile().mFileName)?item
												.getJtFile().mFileName:item
												.getJtFile().mSuffixName);
							} else if (httpIndex == 0) {
								holder.rightShareContentTv.setText("");
							} else {
								holder.rightShareContentTv.setText(!TextUtils.isEmpty(item
										.getJtFile().mFileName)?item
												.getJtFile().mFileName:item
												.getJtFile().mSuffixName);
							}
							holder.rightShareContentTv.setVisibility(View.VISIBLE);
						} else {
							holder.rightShareContentTv.setVisibility(View.GONE);
						}
						// 链接
						if (!StringUtils.isEmpty(item.getJtFile().mUrl)) {
							if (item.getJtFile().mUrl.startsWith("http://mp.weixin.qq.com/")) { // 微信分享
								// holder.rightShareLinkIv.setImageResource(R.drawable.chat_link_weixin);
								holder.rightShareLinkIv.setText("来自微信");
							} else {
								// holder.rightShareLinkIv.setImageResource(R.drawable.chat_link_normal);
								holder.rightShareLinkIv.setText("来自网页");
							}
							holder.rightShareLinkIv.setVisibility(View.VISIBLE);
						} else {
							holder.rightShareLinkIv.setVisibility(View.GONE);
						}
					}
					// 长按
					holder.rightShareLl.setOnLongClickListener(longClickListener);
				} else if (IMBaseMessage.TYPE_KNOWLEDGE2 == item.getType()) { // 右侧新知识

					holder.rightVideoRl.setVisibility(View.GONE);
					holder.rightImageRl.setVisibility(View.GONE);
					holder.rightContent.setVisibility(View.GONE);
					holder.rightVoiceLl.setVisibility(View.GONE);
					holder.rightFileLl.setVisibility(View.GONE);
					holder.rightCnsRl.setVisibility(View.GONE);
					holder.rightShareLl.setVisibility(View.VISIBLE);

					if (item.getJtFile() != null) {
						holder.rightShareMsgTv.setText("分享了[知识]");
						holder.rightShareTopLl.setVisibility(View.VISIBLE);
						holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
						// 图片
						holder.rightShareImageIv.setVisibility(View.VISIBLE);
						if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
							ImageLoader.load(holder.rightShareImageIv, item.getJtFile().mUrl, R.drawable.hy_chat_share_img);
						} else {
							 holder.rightShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
						}
						// 类型
						holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);
						// 标题
						holder.rightShareTitleTv.setText("\u3000\u3000" + item.getJtFile().reserved2);
						holder.rightShareTitleTv.setVisibility(View.VISIBLE);
						// 内容
						if (!StringUtils.isEmpty(item.getJtFile().mSuffixName)||!StringUtils.isEmpty(item.getJtFile().mFileName)) {
							holder.rightShareContentTv.setText(!TextUtils.isEmpty(item
									.getJtFile().mFileName)?item
											.getJtFile().mFileName:item
											.getJtFile().mSuffixName);
							holder.rightShareContentTv.setVisibility(View.VISIBLE);
						} else {
							holder.rightShareContentTv.setVisibility(View.GONE);
						}
						// 链接(判断来源)
						if (TextUtils.isEmpty(item.getJtFile().reserved3)) {
							holder.rightShareLinkIv.setVisibility(View.GONE);
						} else {
							if (item.getJtFile().reserved3.startsWith("http://mp.weixin.qq.com/")) {
								// holder.rightShareLinkIv.setImageResource(R.drawable.chat_link_weixin);
								holder.rightShareLinkIv.setText("来自微信");
							} else {
								// holder.rightShareLinkIv.setImageResource(R.drawable.chat_link_normal);
								holder.rightShareLinkIv.setText("来自网页");
							}
							holder.rightShareLinkIv.setVisibility(View.VISIBLE);
						}
					}
					// 长按
					holder.rightShareLl.setOnLongClickListener(longClickListener);
				} else if (IMBaseMessage.TYPE_CONFERENCE == item.getType()) { // 右侧会议

					holder.rightVideoRl.setVisibility(View.GONE);
					holder.rightImageRl.setVisibility(View.GONE);
					holder.rightContent.setVisibility(View.GONE);
					holder.rightVoiceLl.setVisibility(View.GONE);
					holder.rightFileLl.setVisibility(View.GONE);
					holder.rightCnsRl.setVisibility(View.GONE);
					holder.rightShareLl.setVisibility(View.VISIBLE);

					if (item.getJtFile() != null) {
						holder.rightShareMsgTv.setText("分享了[会议]");
						holder.rightShareTopLl.setVisibility(View.VISIBLE);
						holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
						// 图片
						if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
							ImageLoader.load(holder.rightShareImageIv, item.getJtFile().mUrl, R.drawable.hy_chat_share_img);
						}else{
							holder.rightShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
						}
						// 类型
						holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_conference);
						// 标题
						holder.rightShareTitleTv.setText("\u3000\u3000" + item.getJtFile().mFileName);
						holder.rightShareTitleTv.setVisibility(View.VISIBLE);
						// 内容
						if (!StringUtils.isEmpty(item.getJtFile().reserved1)) {
							holder.rightShareContentTv.setText(item.getJtFile().reserved1);
							holder.rightShareContentTv.setVisibility(View.VISIBLE);
						} else {
							holder.rightShareContentTv.setVisibility(View.GONE);
						}
						// 链接
						holder.rightShareLinkIv.setVisibility(View.GONE);
					}
					// 长按
					holder.rightShareLl.setOnLongClickListener(longClickListener);
					// 点击
					holder.rightShareLl.setOnClickListener(clickListener);
				} else if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType()
						|| // 右侧关系
						IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()
						|| IMBaseMessage.TYPE_ORG_OFFLINE == item.getType()
						|| IMBaseMessage.TYPE_ORG_ONLINE == item.getType()
						|| IMBaseMessage.TYPE_ORGANIZATION == item.getType()
						|| IMBaseMessage.TYPE_CUSTOMER == item.getType()) {

					holder.rightVideoRl.setVisibility(View.GONE);
					holder.rightImageRl.setVisibility(View.GONE);
					holder.rightContent.setVisibility(View.GONE);
					holder.rightVoiceLl.setVisibility(View.GONE);
					holder.rightFileLl.setVisibility(View.GONE);
					holder.rightShareLl.setVisibility(View.GONE);
					holder.rightCnsName1.setVisibility(View.GONE);
					holder.rightCnsName2.setVisibility(View.GONE);
					holder.rightCnsName3.setVisibility(View.GONE);
					holder.rightCnsRl.setVisibility(View.VISIBLE);
					holder.rightCnsRl.setTag(item.getJtFile());

					if (item.getJtFile() != null) {
						final JTFile jtFile = item.getJtFile();
						// 用户
						if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType()
								|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
							if (!StringUtils.isEmpty(jtFile.mFileName)) {
								// holder.rightCnsTitle.setText(jtFile.mFileName);
								holder.rightCnsName1.setText(jtFile.mFileName);
								holder.rightCnsName1.setVisibility(View.VISIBLE);
							}
						}
						// 组织
						else {
							holder.rightCnsName1.setText(!TextUtils.isEmpty(item
											.getJtFile().mFileName)?item
													.getJtFile().mFileName:item
													.getJtFile().mSuffixName);
							holder.rightCnsName1.setVisibility(View.VISIBLE);
						}
						// 用户
						if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType()
								|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
							if (!StringUtils.isEmpty(jtFile.mSuffixName)) {
								holder.rightCnsName2.setText(jtFile.mSuffixName);
								holder.rightCnsName2.setVisibility(View.VISIBLE);
							}
							if (!StringUtils.isEmpty(jtFile.reserved1)) {
								holder.rightCnsName3.setText(jtFile.reserved1);
								holder.rightCnsName3.setVisibility(View.VISIBLE);
							}
						}
						// 组织
						else {
							if (!StringUtils.isEmpty(jtFile.reserved1)) {
								holder.rightCnsName2.setText(jtFile.reserved1);
								holder.rightCnsName2.setVisibility(View.VISIBLE);
							}
						}
						// if(!StringUtils.isEmpty(jtFile.mUrl)){
						if (jtFile.mUrl == null) {
							jtFile.mUrl = "";
						}

						if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType()
								|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
							if(!TextUtils.isEmpty(jtFile.mUrl)){
								ImageLoader.load(holder.rightCnsIcon, jtFile.mUrl, R.drawable.ic_default_avatar);
							}else{
								holder.rightCnsIcon.setImageResource(R.drawable.ic_default_avatar);
							}
						} else {
							if(!TextUtils.isEmpty(jtFile.mUrl)){
								ImageLoader.load(holder.rightCnsIcon, jtFile.mUrl, R.drawable.companyfriend);
							}else{
								holder.rightCnsIcon.setImageResource(R.drawable.companyfriend);
							}
						}

					}

					// 点击事件
					holder.rightCnsRl.setOnClickListener(clickListener);
					// 长按事件
					holder.rightCnsRl.setOnLongClickListener(longClickListener);
				} else if (IMBaseMessage.TYPE_REQUIREMENT == item.getType()) { // 右侧需求

					holder.rightVideoRl.setVisibility(View.GONE);
					holder.rightImageRl.setVisibility(View.GONE);
					holder.rightContent.setVisibility(View.GONE);
					holder.rightVoiceLl.setVisibility(View.GONE);
					holder.rightFileLl.setVisibility(View.GONE);
					holder.rightCnsRl.setVisibility(View.GONE);
					holder.rightShareLl.setVisibility(View.VISIBLE);

					// 初始化数据
					if (item.getJtFile() != null) {

						holder.rightShareTopLl.setVisibility(View.VISIBLE);
						holder.rightShareBottomLl.setBackgroundResource(R.drawable.chat_ziji_share_bottom);
						holder.rightShareMsgTv.setText("分享了[需求]");
						// 图片
						holder.rightShareImageIv.setVisibility(View.GONE);
						// 类型
						holder.rightShareTypeIv.setImageResource(R.drawable.hy_chat_type_requirement);
						// 标题
						holder.rightShareTitleTv.setText("\u3000\u3000" + item.getJtFile().mFileName);
						holder.rightShareTitleTv.setVisibility(View.VISIBLE);
						// 内容
						if (!StringUtils.isEmpty(item.getJtFile().reserved1)) {
							holder.rightShareContentTv.setText(item.getJtFile().reserved1);
							holder.rightShareContentTv.setVisibility(View.VISIBLE);
						} else {
							holder.rightShareContentTv.setVisibility(View.GONE);

						}
						// 链接
						holder.rightShareLinkIv.setVisibility(View.GONE);
					}
					// 长按
					holder.rightShareLl.setOnLongClickListener(longClickListener);
				} else if (IMBaseMessage.TYPE_VIDEO == item.getType()) { // 右侧视频
					try {

						holder.rightImageRl.setVisibility(View.GONE);
						holder.rightContent.setVisibility(View.GONE);
						holder.rightVoiceLl.setVisibility(View.GONE);
						holder.rightShareLl.setVisibility(View.GONE);
						holder.rightFileLl.setVisibility(View.GONE);
						holder.rightCnsRl.setVisibility(View.GONE);
						holder.rightVideoRl.setVisibility(View.VISIBLE);

						if (item.getJtFile() != null) {
							if (!TextUtils.isEmpty(item.getJtFile().mUrl)) { // 视频已上传
								File localFile = null;
								boolean fileExist = false;
								// 本地文件是否存在
								if (!StringUtils.isEmpty(item.getJtFile().reserved1)) {
									localFile = new File(item.getJtFile().reserved1);
									fileExist = localFile.exists();
								}
								if(!fileExist){
									File localDir = EUtil.getAppCacheFileDir(mContext);
									if (localDir != null && !StringUtils.isEmpty(item.getJtFile().mFileName)) {
										localFile = new File(localDir,
										item.getJtFile().mFileName); // 本地缓存文件路径
										fileExist = localFile.exists();
									}
								}
								filePath = null;
								if (localFile!=null) {
									filePath= localFile != null ? localFile.getAbsolutePath() : "";
								}
							
								if (fileExist && localFile.length() == item.getJtFile().mFileSize) {
									BmpAsyncTask bAsyncTask = new BmpAsyncTask(holder.rightVideoIv);
									bAsyncTask.execute(filePath);
									holder.rightPlayIv.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) { // 打开文件
														OpenFiles.open(mContext, filePath);
													}
												});
								} else { // 本地文件不存在（显示截图）
									holder.rightVideoIv.setImageResource(R.drawable.hy_chat_right_pic);
									holder.rightPlayIv.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													Intent intent = new Intent(mContext, FilePreviewActivity.class);
													intent.putExtra(EConsts.Key.JT_FILE, item.getJtFile());
													mContext.startActivity(intent);
												}
											});
								}
							} else { // 视频文件未上传或上传中
								if (!StringUtils.isEmpty(item.getJtFile().mLocalFilePath)) {
									BmpAsyncTask bAsyncTask = new BmpAsyncTask(holder.rightVideoIv);
									bAsyncTask.execute(item.getJtFile().mLocalFilePath);
								} else {
									holder.rightVideoIv.setImageResource(R.drawable.hy_chat_right_pic);
								}
								holder.rightPlayIv.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) { // 打开文件
												if (!StringUtils.isEmpty(item.getJtFile().mLocalFilePath)) {
													OpenFiles.open(mContext, item.getJtFile().mLocalFilePath);
												} else {
													showToast("视频源未找到");
												}
											}
										});
							}
						}
						// 长按事件
						holder.rightVideoIv.setOnLongClickListener(longClickListener);
						// 点击事件
						holder.rightVideoIv.setOnClickListener(clickListener);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else if (IMBaseMessage.TYPE_FILE == item.getType()) { // 右侧文件

					holder.rightImageRl.setVisibility(View.GONE);
					holder.rightContent.setVisibility(View.GONE);
					holder.rightVoiceLl.setVisibility(View.GONE);
					holder.rightShareLl.setVisibility(View.GONE);
					holder.rightCnsRl.setVisibility(View.GONE);
					holder.rightVideoRl.setVisibility(View.GONE);
					holder.rightFileLl.setVisibility(View.VISIBLE);

					if (item.getJtFile() != null) {

						// 根据不同的后缀名设置不同的文件图片
						setFileIconAccoredSuffixName(holder.rightFileTypeIv, item.getJtFile().getmSuffixName());

						holder.rightFileNameTv.setText(item.getJtFile().mFileName); // 文件名
						holder.rightFileSizeTv.setText(EUtil.formatFileSize(item.getJtFile().mFileSize)); // 文件大小
						if (item.getJtFile().mUrl == null
								|| StringUtils.isEmpty(item.getJtFile().mUrl)) { // 文件正在上传
							holder.rightFileStatusTv.setText("");
							for (FileUploader uploader : mListUploader) {
								if (uploader.getJTFile().mTaskId.equals(item.getJtFile().mTaskId)) {
									holder.rightFileProgressPb.setProgress(uploader.getProgress());
									break;
								}
							}
							holder.rightFileProgressPb.setVisibility(View.VISIBLE);
						} else { // 文件已上传
							boolean taskExist = false;
							if (mDownloadService != null) {
								for (FileDownloader downloader : mDownloadService.getListDownloader()) {
									if (downloader.getJTFile().mUrl.equals(item.getJtFile().mUrl)) {
										holder.rightFileStatusTv.setText("");
										holder.rightFileProgressPb.setProgress(downloader.getProgress());
										holder.rightFileProgressPb.setVisibility(View.VISIBLE);
										taskExist = true;
										break;
									}
								}
							}
							if (!taskExist) {
								File localFile = null;
								boolean fileExist = false;
								if (!StringUtils.isEmpty(item.getJtFile().reserved1)) {
									localFile = new File(item.getJtFile().reserved1);
									fileExist = localFile.exists();
								} else {
									File localDir = EUtil.getAppCacheFileDir(mContext);
									if (localDir != null && !StringUtils.isEmpty(item.getJtFile().mFileName)) {
										localFile = new File(
												localDir,
												item.getJtFile().mFileName);
										fileExist = localFile.exists();
									}
								}
								if (fileExist) {
									holder.rightFileStatusTv.setText("打开");
									holder.rightFileProgressPb.setVisibility(View.GONE);
								} else {
									holder.rightFileStatusTv.setText("未下载");
									holder.rightFileProgressPb.setVisibility(View.GONE);
								}
							}
						}
					}
					// 长按
					holder.rightFileLl.setOnLongClickListener(longClickListener);
				} else { // 右侧文本

					holder.rightVideoRl.setVisibility(View.GONE);
					holder.rightShareLl.setVisibility(View.GONE);
					holder.rightImageRl.setVisibility(View.GONE);
					holder.rightFileLl.setVisibility(View.GONE);
					holder.rightCnsRl.setVisibility(View.GONE);
					holder.rightVoiceLl.setVisibility(View.GONE);
					holder.rightContent.setVisibility(View.VISIBLE);
					CharSequence dd = parser.addSmileySpans(body);
					// CharSequence dd1 = parser2.addSmileySpans(dd);
					holder.rightContent.setText(dd);
					if (item.getContent().startsWith("http://")
							|| item.getContent().startsWith("https://")) {
						Linkify.addLinks(holder.rightContent, Linkify.WEB_URLS);
					}
					// 长按
					holder.rightContent.setOnLongClickListener(longClickListener);
				}
				// 发送失败的红色按钮处理
				if (item.getSendType() == IMBaseMessage.SEND_TYPE_FAIL) {
					// 失败
					holder.sendMsgProgress.setVisibility(View.GONE);
					holder.sendMsgFail.setVisibility(View.VISIBLE);
					holder.sendMsgFail.setOnClickListener(new ImageView.OnClickListener() {
								@Override
								public void onClick(View v) {
									showSendMessageDialog(item);
								}
							});
					// 处理文件发送失败时的情况
					/*
					 * if(item.getType() == IMBaseMessage.TYPE_FILE){
					 * holder
					 * .rightFileProgressPb.setVisibility(View.GONE); }
					 */
				} else if (item.getSendType() == IMBaseMessage.SEND_TYPE_SENDING) {
					// 发送中
					holder.sendMsgFail.setVisibility(View.GONE);
					holder.sendMsgProgress.setVisibility(View.VISIBLE);
				} else {
					// 成功
					holder.sendMsgFail.setVisibility(View.GONE);
					holder.sendMsgProgress.setVisibility(View.GONE);
				}
			}else{

				// 对方发送的内容
				holder.leftLayout.setVisibility(View.VISIBLE);
				holder.rightLayout.setVisibility(View.GONE);
				holder.systemMessage.setVisibility(View.GONE);
				if(TextUtils.isEmpty(getImageByMessage(item))){
					if(getSenderTypeByMessage(item)+1 == ConnectionsMini.UT_ORG){
						holder.leftHead.setImageResource(R.drawable.default_portrait116);
					}else{
						holder.leftHead.setImageResource(R.drawable.ic_default_avatar);
					}
				}else{
					if(getSenderTypeByMessage(item)+1 == ConnectionsMini.UT_ORG){
						com.utils.common.Util.initAvatarImage(mContext, holder.leftHead, item.getSenderName(), getImageByMessage(item), 1, 2);
					}else{
						com.utils.common.Util.initAvatarImage(mContext, holder.leftHead, item.getSenderName(), getImageByMessage(item), 1, 1);
					}
				}
				holder.leftName.setText(item.getSenderName());

				if (IMBaseMessage.TYPE_TEXT == item.getType()) { // 左侧文本

					holder.leftImageRl.setVisibility(View.GONE);
					holder.leftShareLl.setVisibility(View.GONE);
					holder.leftFileLl.setVisibility(View.GONE);
					holder.leftVideoRl.setVisibility(View.GONE);
					holder.leftCnsRl.setVisibility(View.GONE);
					holder.leftVoiceLl.setVisibility(View.GONE);
					holder.leftContent.setVisibility(View.VISIBLE);
					holder.leftContent.setOnLongClickListener(longClickListener);
					CharSequence dd = parser.addSmileySpans(body);
					// CharSequence dd1 = parser2.addSmileySpans(dd);
					holder.leftContent.setText(dd);

					if (item.getContent().startsWith("http://")|| item.getContent().startsWith("https://")) {
						Linkify.addLinks(holder.leftContent, Linkify.WEB_URLS);
					}
				} else if (IMBaseMessage.TYPE_IMAGE == item.getType()) { // 左侧图片

					holder.leftContent.setVisibility(View.GONE);
					holder.leftVoiceLl.setVisibility(View.GONE);
					holder.leftShareLl.setVisibility(View.GONE);
					holder.leftFileLl.setVisibility(View.GONE);
					holder.leftVideoRl.setVisibility(View.GONE);
					holder.leftCnsRl.setVisibility(View.GONE);
					holder.leftImageRl.setVisibility(View.VISIBLE);

					if (item.getJtFile() != null) {
						if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
							ImageLoader.load(holder.leftImageIv, 130, item.getJtFile().mUrl, R.drawable.hy_chat_right_pic);
						} else {
							holder.leftImageIv.setImageResource(R.drawable.hy_chat_right_pic);
						}
					} else {
						holder.leftImageIv.setImageResource(R.drawable.hy_chat_right_pic);
					}
					// 长按事件
					holder.leftImageRl.setOnLongClickListener(longClickListener);
					// 点击事件
					holder.leftImageRl.setOnClickListener(clickListener);
				} else if (IMBaseMessage.TYPE_KNOWLEDGE == item.getType()) { // 左侧知识

					holder.leftImageRl.setVisibility(View.GONE);
					holder.leftContent.setVisibility(View.GONE);
					holder.leftVoiceLl.setVisibility(View.GONE);
					holder.leftFileLl.setVisibility(View.GONE);
					holder.leftVideoRl.setVisibility(View.GONE);
					holder.leftCnsRl.setVisibility(View.GONE);
					holder.leftShareLl.setVisibility(View.VISIBLE);

					holder.leftShareMsgTv.setText("分享了[知识]");
					holder.leftShareTopLl.setVisibility(View.VISIBLE);
					holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
					// 图片
					holder.leftShareImageIv.setVisibility(View.GONE);
					// 类型
					holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);
					// 标题
					holder.leftShareTitleTv.setVisibility(View.GONE);
					// 内容
					int httpIndex = item.getJtFile().mSuffixName.indexOf("http");
					if (!StringUtils.isEmpty(item.getJtFile().mSuffixName)||!StringUtils.isEmpty(item.getJtFile().mFileName)) {
						holder.leftShareContentTv.setVisibility(View.VISIBLE);
						if (httpIndex <= 0) {
							holder.leftShareContentTv.setText(!TextUtils.isEmpty(item
									.getJtFile().mFileName)?item
											.getJtFile().mFileName:item
											.getJtFile().mSuffixName);
						} else {
							holder.leftShareContentTv.setText(!TextUtils.isEmpty(item
									.getJtFile().mFileName)?item
											.getJtFile().mFileName:item
											.getJtFile().mSuffixName);
						}
					} else {
						holder.leftShareContentTv.setVisibility(View.GONE);
					}
					// 链接
					if (!StringUtils.isEmpty(item.getJtFile().mUrl)) {
						if (item.getJtFile().mUrl.startsWith("http://mp.weixin.qq.com/")) { // 微信分享
							// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_weixin);
							holder.leftShareLinkIv.setText("来自微信");
						} else {
							// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_normal);
							holder.leftShareLinkIv.setText("来自网页");
						}
						holder.leftShareLinkIv.setVisibility(View.VISIBLE);
					} else {
						holder.leftShareLinkIv.setVisibility(View.GONE);
					}
					// 长按
					holder.leftShareLl.setOnLongClickListener(longClickListener);
				} else if (IMBaseMessage.TYPE_KNOWLEDGE2 == item.getType()) { // 左侧新知识

					holder.leftImageRl.setVisibility(View.GONE);
					holder.leftContent.setVisibility(View.GONE);
					holder.leftVoiceLl.setVisibility(View.GONE);
					holder.leftFileLl.setVisibility(View.GONE);
					holder.leftVideoRl.setVisibility(View.GONE);
					holder.leftCnsRl.setVisibility(View.GONE);
					holder.leftShareLl.setVisibility(View.VISIBLE);

					// 知识
					holder.leftShareMsgTv.setText("分享了[知识]");
					holder.leftShareTopLl.setVisibility(View.VISIBLE);
					holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
					// 图片
					holder.leftShareImageIv.setVisibility(View.VISIBLE);
					if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
						ImageLoader.load(holder.leftShareImageIv,item.getJtFile().mUrl, R.drawable.hy_chat_share_img);
					} else {
						 holder.leftShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
					}
					// 类型
					holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_knowledge);
					// 标题
					holder.leftShareTitleTv.setText("\u3000\u3000" + item.getJtFile().reserved2);
					holder.leftShareTitleTv.setVisibility(View.VISIBLE);
					// 内容
					if (!StringUtils.isEmpty(item.getJtFile().mSuffixName)||!StringUtils.isEmpty(item.getJtFile().mFileName)) {
						holder.leftShareContentTv.setText(!TextUtils.isEmpty(item
								.getJtFile().mFileName)?item
										.getJtFile().mFileName:item
										.getJtFile().mSuffixName);
						holder.leftShareContentTv.setVisibility(View.VISIBLE);
					} else {
						holder.leftShareContentTv.setVisibility(View.GONE);
					}
					// 链接
					holder.leftShareLinkIv.setVisibility(View.GONE);
					if (TextUtils.isEmpty(item.getJtFile().reserved3)) {
						holder.leftShareLl.setOnLongClickListener(longClickListener);
					} else {
						if (item.getJtFile().reserved3.startsWith("http://mp.weixin.qq.com/")) { // 微信分享
							// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_weixin);
							holder.leftShareLinkIv.setText("来自微信");
						} else {
							// holder.leftShareLinkIv.setImageResource(R.drawable.chat_link_normal);
							// // 其它分享
							holder.leftShareLinkIv.setText("来自网页");
						}
						holder.leftShareLinkIv.setVisibility(View.VISIBLE);
					}
					// 长按
					holder.leftShareLl.setOnLongClickListener(longClickListener);
				} else if (IMBaseMessage.TYPE_CONFERENCE == item.getType()) { // 左侧会议

					holder.leftVideoRl.setVisibility(View.GONE);
					holder.leftImageRl.setVisibility(View.GONE);
					holder.leftContent.setVisibility(View.GONE);
					holder.leftVoiceLl.setVisibility(View.GONE);
					holder.leftFileLl.setVisibility(View.GONE);
					holder.leftCnsRl.setVisibility(View.GONE);
					holder.leftShareLl.setVisibility(View.VISIBLE);

					if (item.getJtFile() != null) {
						holder.leftShareMsgTv.setText("分享了[会议]");
						holder.leftShareTopLl.setVisibility(View.VISIBLE);
						holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
						// 图片
						if (!TextUtils.isEmpty(item.getJtFile().mUrl)) {
							ImageLoader.load(holder.leftShareImageIv, item.getJtFile().mUrl, R.drawable.hy_chat_share_img);
						} else {
							 holder.leftShareImageIv.setImageResource(R.drawable.hy_chat_share_img);
						}
						// 类型
						holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_conference);
						// 标题
						holder.leftShareTitleTv.setText("\u3000\u3000" + item.getJtFile().mFileName);
						holder.leftShareTitleTv.setVisibility(View.VISIBLE);
						// 内容
						if (!StringUtils.isEmpty(item.getJtFile().reserved1)) {
							holder.leftShareContentTv.setText(item.getJtFile().reserved1);
							holder.leftShareContentTv.setVisibility(View.VISIBLE);
						} else {
							holder.leftShareContentTv.setVisibility(View.GONE);
						}
						// holder.leftShareContentTv.setText(item.getJtFile().reserved1);
						// holder.leftShareContentTv.setVisibility(View.VISIBLE);
						// 链接
						holder.leftShareLinkIv.setVisibility(View.GONE);
					}
					// 长按
					holder.leftShareLl.setOnLongClickListener(longClickListener);
					// 点击
					holder.leftShareLl.setOnClickListener(clickListener);
				} else if (IMBaseMessage.TYPE_REQUIREMENT == item.getType()) { // 左侧需求

					holder.leftImageRl.setVisibility(View.GONE);
					holder.leftContent.setVisibility(View.GONE);
					holder.leftVoiceLl.setVisibility(View.GONE);
					holder.leftFileLl.setVisibility(View.GONE);
					holder.leftVideoRl.setVisibility(View.GONE);
					holder.leftCnsRl.setVisibility(View.GONE);
					holder.leftShareLl.setVisibility(View.VISIBLE);
					holder.leftShareLl.setTag(item.getJtFile());

					holder.leftShareMsgTv.setText("分享了[需求]");
					holder.leftShareTopLl.setVisibility(View.VISIBLE);
					holder.leftShareBottomLl.setBackgroundResource(R.drawable.chat_duifang_share_bottom);
					// 图片
					holder.leftShareImageIv.setVisibility(View.GONE);
					// 类型
					holder.leftShareTypeIv.setImageResource(R.drawable.hy_chat_type_requirement);
					// 标题
					holder.leftShareTitleTv.setText("\u3000\u3000" + item.getJtFile().mFileName);
					holder.leftShareTitleTv.setVisibility(View.VISIBLE);
					// 内容
					if (!StringUtils.isEmpty(item.getJtFile().reserved1)) {
						holder.leftShareContentTv.setText(item.getJtFile().reserved1);
						holder.leftShareContentTv.setVisibility(View.VISIBLE);
					} else {
						holder.leftShareContentTv.setVisibility(View.GONE);
					}
					// holder.leftShareContentTv.setText(item.getJtFile().reserved1);
					// holder.leftShareContentTv.setVisibility(View.VISIBLE);
					// 链接
					holder.leftShareLinkIv.setVisibility(View.GONE);
					// 长按
					holder.leftShareLl.setOnLongClickListener(longClickListener);
				} else if (IMBaseMessage.TYPE_VIDEO == item.getType()) { // 左侧视频

					holder.leftImageRl.setVisibility(View.GONE);
					holder.leftContent.setVisibility(View.GONE);
					holder.leftVoiceLl.setVisibility(View.GONE);
					holder.leftShareLl.setVisibility(View.GONE);
					holder.leftFileLl.setVisibility(View.GONE);
					holder.leftCnsRl.setVisibility(View.GONE);
					holder.leftVideoRl.setVisibility(View.VISIBLE);

					if (item.getJtFile() != null) {
						File file = new File(EUtil.getAppCacheFileDir(mContext), item.getJtFile().mFileName);
						if (file.exists() && file.length() == item.getJtFile().mFileSize) { // 文件已经下载到本地
							BmpAsyncTask bAsyncTask = new BmpAsyncTask(holder.leftVideoIv);
							bAsyncTask.execute(file.getAbsolutePath());
						} else { // 文件尚未下载
							holder.leftVideoIv.setImageResource(R.drawable.hy_chat_right_pic);
						}
					}
					// 长按事件
					holder.leftVideoIv.setOnLongClickListener(longClickListener);
					// 点击事件
					holder.leftPlayIv.setOnClickListener(clickListener);
				} else if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType()
						|| // 左侧关系
						IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()
						|| IMBaseMessage.TYPE_ORG_OFFLINE == item.getType()
						|| IMBaseMessage.TYPE_ORG_ONLINE == item.getType()
						|| IMBaseMessage.TYPE_ORGANIZATION == item.getType()
						|| IMBaseMessage.TYPE_CUSTOMER == item.getType()) { // 关系

					holder.leftVideoRl.setVisibility(View.GONE);
					holder.leftImageRl.setVisibility(View.GONE);
					holder.leftContent.setVisibility(View.GONE);
					holder.leftVideoRl.setVisibility(View.GONE);
					holder.leftVoiceLl.setVisibility(View.GONE);
					holder.leftFileLl.setVisibility(View.GONE);
					holder.leftShareLl.setVisibility(View.GONE);
					holder.leftCnsRl.setVisibility(View.VISIBLE);

					holder.leftCnsName1.setVisibility(View.GONE);
					holder.leftCnsName2.setVisibility(View.GONE);
					holder.leftCnsName3.setVisibility(View.GONE);
					if (item.getJtFile() != null) {
						final JTFile jtFile = item.getJtFile();
						// 用户
						if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType()
								|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
							if (!StringUtils.isEmpty(jtFile.mFileName)) {
								// holder.leftCnsTitle.setText(jtFile.mFileName);
								holder.leftCnsName1.setText(jtFile.mFileName);
								holder.leftCnsName1.setVisibility(View.VISIBLE);
							}
						}
						// 组织
						else {
							holder.leftCnsName1.setText(!TextUtils.isEmpty(item
											.getJtFile().mFileName)?item
													.getJtFile().mFileName:item
													.getJtFile().mSuffixName);
							holder.leftCnsName1.setVisibility(View.VISIBLE);

						}
						// 用户
						if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType()
								|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
							if (!StringUtils.isEmpty(jtFile.mSuffixName)) {
								holder.leftCnsName2.setText(jtFile.mSuffixName);
								holder.leftCnsName2.setVisibility(View.VISIBLE);
							}
							if (!StringUtils.isEmpty(jtFile.reserved1)) {
								holder.leftCnsName3.setText(jtFile.reserved1);
								holder.leftCnsName3.setVisibility(View.VISIBLE);
							}
						}
						// 组织
						else {
							holder.leftCnsName2.setText(jtFile.reserved1);
							holder.leftCnsName2.setVisibility(View.VISIBLE);

						}
						// if(!StringUtils.isEmpty(jtFile.mUrl)){
						if (jtFile.mUrl == null) {
							jtFile.mUrl = "";
						}

						
						if (IMBaseMessage.TYPE_JTCONTACT_OFFLINE == item.getType()
								|| IMBaseMessage.TYPE_JTCONTACT_ONLINE == item.getType()) {
							if(!TextUtils.isEmpty(jtFile.mUrl)){
								ImageLoader.load(holder.leftCnsIcon, jtFile.mUrl, R.drawable.ic_default_avatar);
							}else{
								holder.leftCnsIcon.setImageResource(R.drawable.ic_default_avatar);
							}
						} else {
							if(!TextUtils.isEmpty(jtFile.mUrl)){
								ImageLoader.load(holder.leftCnsIcon, jtFile.mUrl, R.drawable.org_default_orgnization);
							}else{
								holder.leftCnsIcon.setImageResource(R.drawable.org_default_orgnization);
							}
						}

						// 点击事件
						holder.leftCnsRl.setOnClickListener(clickListener);
						// 长按事件
						holder.leftCnsRl.setOnLongClickListener(longClickListener);
					}
				} else if (IMBaseMessage.TYPE_FILE == item.getType()) { // 左侧文件

					holder.leftImageRl.setVisibility(View.GONE);
					holder.leftContent.setVisibility(View.GONE);
					holder.leftVoiceLl.setVisibility(View.GONE);
					holder.leftShareLl.setVisibility(View.GONE);
					holder.leftVideoRl.setVisibility(View.GONE);
					holder.leftCnsRl.setVisibility(View.GONE);
					holder.leftFileLl.setVisibility(View.VISIBLE);

					if (item.getJtFile() != null) {

						// 根据不同的后缀名设置不同的文件图片
						setFileIconAccoredSuffixName(holder.leftFileTypeIv, item.getJtFile().getmSuffixName());

						holder.leftFileNameTv.setText(item.getJtFile().mFileName); // 文件名
						holder.leftFileSizeTv.setText(EUtil.formatFileSize(item.getJtFile().mFileSize)); // 文件大小
						// 是否正在下载
						boolean taskExist = false;
						if (mDownloadService != null) {
							for (FileDownloader downloader : mDownloadService.getListDownloader()) {
								if (downloader.getJTFile().mUrl.equals(item.getJtFile().mUrl)) {
									taskExist = true;
									holder.leftFileStatusTv.setText("");
									holder.leftFileProgressPb.setProgress(downloader.getProgress());
									holder.leftFileProgressPb.setVisibility(View.VISIBLE);
									break;
								}
							}
						}
						if (!taskExist) {
							if (new File(EUtil.getAppCacheFileDir(mContext),item.getJtFile().mFileName).exists()) {
								holder.leftFileStatusTv.setText("打开");
								holder.leftFileProgressPb.setVisibility(View.GONE);
							} else {
								holder.leftFileStatusTv.setText("未下载");
								holder.leftFileProgressPb.setVisibility(View.GONE);
							}
						}
					}
					// 长按事件
					holder.leftFileLl.setOnLongClickListener(longClickListener);
				} else if (IMBaseMessage.TYPE_AUDIO == item.getType()) { // 左侧语音

					holder.leftVideoRl.setVisibility(View.GONE);
					holder.leftContent.setVisibility(View.GONE);
					holder.leftImageRl.setVisibility(View.GONE);
					holder.leftContent.setVisibility(View.GONE);
					holder.leftShareLl.setVisibility(View.GONE);
					holder.leftFileLl.setVisibility(View.GONE);
					holder.leftCnsRl.setVisibility(View.GONE);
					holder.leftVoiceLl.setVisibility(View.VISIBLE);

					if (item.getJtFile() != null) {

						// 显示时长
						if (StringUtils.isEmpty(item.getJtFile().reserved2)) {
							holder.leftVoiceDurationTv.setText("未知\"");
						} else {
							holder.leftVoiceDurationTv.setText(item.getJtFile().reserved2 + "\"");
						}
						// 控件长度
						int paramsWidth = 50 + (StringUtils.isEmpty(item.getJtFile().reserved2) ? 0
								: Integer.parseInt(item.getJtFile().reserved2) * 5);
						// 最大不超过200dp
						paramsWidth = Math.min(paramsWidth, 200);
						// 设置长度
						LinearLayout.LayoutParams layoutParams = (LayoutParams) holder.leftVoiceDurationLl.getLayoutParams();
						layoutParams.width = EUtil.convertDpToPx(paramsWidth);
						holder.leftVoiceDurationLl.setLayoutParams(layoutParams);
						// 语音文件
						File file = new File(getVoiceFileDir(), item.getJtFile().mFileName);
						// 当前状态
						if (file.exists() && file.length() == item.getJtFile().mFileSize) {
							holder.leftVoiceLoadingPb.setVisibility(View.GONE);
							holder.leftVoiceIv.setVisibility(View.VISIBLE);
						} else {
							long taskId = voiceFileManager.query(item.getJtFile().mUrl);
							if (taskId >= 0) {
								final Query query = new Query().setFilterById(taskId);
								try {
									new AsyncTask<Void, Void, Integer>(){

										@Override
										protected Integer doInBackground(
												Void... params) {
											cursor = downloadManager.query(query);
											int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
											cursor.close();
											return status;
										}
										@Override
										protected void onPostExecute(Integer status) {
											super.onPostExecute(status);
											switch (status) {
											case DownloadManager.STATUS_PENDING:
											case DownloadManager.STATUS_RUNNING:
												holder.leftVoiceLoadingPb.setVisibility(View.VISIBLE);
												holder.leftVoiceIv.setVisibility(View.GONE);
												break;
											default:
												holder.leftVoiceLoadingPb.setVisibility(View.GONE);
												holder.leftVoiceIv.setVisibility(View.VISIBLE);
												break;
											}
										}
									};
									
									
								} catch (Exception e) {
									if (cursor != null) {
										cursor.close();
									}
									holder.leftVoiceLoadingPb.setVisibility(View.GONE);
									holder.leftVoiceIv.setVisibility(View.VISIBLE);
								}
							} else {
								holder.leftVoiceLoadingPb.setVisibility(View.GONE);
								holder.leftVoiceIv.setVisibility(View.VISIBLE);
							}
						}
						// 点击事件
						holder.leftVoiceLl.setOnClickListener(clickListener);
						// 长按事件
						holder.leftVoiceLl.setOnLongClickListener(longClickListener);
					}
				}
			}

			// 发布时间
			String createDate = item.getTime();
			if (!TextUtils.isEmpty(createDate)) {
				createDate = JTDateUtils.getIMTimeDisplay(createDate, App.getApplicationConxt(), false);
			} else {
				createDate = "";
			}

			if (KeelLog.DEBUG) {
				KeelLog.i("发布时间 " + createDate);
			}

			if (TextUtils.isEmpty(createDate)) {
				holder.letterDate.setVisibility(View.GONE);
			} else {
				if (position > 0) {
					long interval = JTDateUtils.getIntervalMoreTime(
							messageList.get(position - 1).getTime(), item.getTime());
					// 聊天间隔小于5分钟， 不显示时间
					if (interval < 60) {
						holder.letterDate.setVisibility(View.GONE);
					} else {
						holder.letterDate.setVisibility(View.VISIBLE);
						holder.letterDate.setText(createDate);
					}
				} else {
					holder.letterDate.setVisibility(View.VISIBLE);
					holder.letterDate.setText(createDate);
				}
			}
		}
		
		return convertView;
	}
	
	public class ViewHolder {

		public View viewBG;
		public RelativeLayout leftLayout;
		public RelativeLayout rightLayout;
		public TextView leftContent;
		public TextView rightContent;
		public CircleImageView leftHead;
		public CircleImageView rightHead;
		public TextView leftName;
		public TextView rightName;
		public ImageView sendMsgFail;
		public TextView letterDate;
		public TextView systemMessage;
		public ProgressBar sendMsgProgress;

		// 语音部分
		public LinearLayout leftVoiceLl;
		public LinearLayout leftVoiceDurationLl;
		public ImageView leftVoiceIv;
		public TextView leftVoiceDurationTv;
		public ProgressBar leftVoiceLoadingPb;

		public LinearLayout rightVoiceLl;
		public LinearLayout rightVoiceDurationLl;
		public ImageView rightVoiceIv;
		public TextView rightVoiceDurationTv;
		public ProgressBar rightVoiceLoadingPb;

		// 图片部分
		public RelativeLayout rightImageRl;
		public ImageView rightImageIv;

		public RelativeLayout leftImageRl;
		public ImageView leftImageIv;

		// 分享部分
		public LinearLayout leftShareLl;
		public TextView leftShareMsgTv;
		public LinearLayout leftShareTopLl;
		public ImageView leftShareImageIv;
		public LinearLayout leftShareBottomLl;
		public ImageView leftShareTypeIv;
		public TextView leftShareTitleTv;
		public TextView leftShareContentTv;
		public TextView leftShareLinkIv;

		public LinearLayout rightShareLl;
		public TextView rightShareMsgTv;
		public LinearLayout rightShareTopLl;
		public LinearLayout rightShareBottomLl;
		public ImageView rightShareImageIv;
		public ImageView rightShareTypeIv;
		public TextView rightShareTitleTv;
		public TextView rightShareContentTv;
		public TextView rightShareLinkIv;

		// 视频部分
		public RelativeLayout leftVideoRl;
		public ImageView leftVideoIv;
		public ImageView leftPlayIv;

		public RelativeLayout rightVideoRl;
		public ImageView rightVideoIv;
		public ImageView rightPlayIv;

		// 文件部分
		public LinearLayout leftFileLl;
		public ImageView leftFileTypeIv;
		public TextView leftFileNameTv;
		public TextView leftFileSizeTv;
		public TextView leftFileStatusTv;
		public ProgressBar leftFileProgressPb;

		public LinearLayout rightFileLl;
		public ImageView rightFileTypeIv;
		public TextView rightFileNameTv;
		public TextView rightFileSizeTv;
		public TextView rightFileStatusTv;
		public ProgressBar rightFileProgressPb;

		// 关系部分
		public RelativeLayout rightCnsRl;
		public TextView rightCnsTitle;
		public ImageView rightCnsIcon;
		public TextView rightCnsName1;
		public TextView rightCnsName2;
		public TextView rightCnsName3;

		public RelativeLayout leftCnsRl;
		public TextView leftCnsTitle;
		public ImageView leftCnsIcon;
		public TextView leftCnsName1;
		public TextView leftCnsName2;
		public TextView leftCnsName3;

		// 最后一条消息空白
		public LinearLayout chat_blank_ll;
	}
	
	// 聊天项长按
	private void showItemLongClickDialog(final IMBaseMessage msg,
			final int position) {
		try {
			String[] listOperation = null;

			switch (msg.getType()) {
			case IMBaseMessage.TYPE_TEXT: // 文本
				listOperation = new String[] { "复制", "转发/分享", "删除" };
				// listOperation = new String[] { "复制", "转发/分享"};
				break;
			case IMBaseMessage.TYPE_IMAGE: // 图片
			case IMBaseMessage.TYPE_VIDEO: // 视频
			case IMBaseMessage.TYPE_FILE: // 文件
			case IMBaseMessage.TYPE_AUDIO: // 语音
				listOperation = new String[] { "转发", "删除", "保存" };
				// listOperation = new String[] { "转发", "保存" };
				break;
			case IMBaseMessage.TYPE_KNOWLEDGE: // 知识
			case IMBaseMessage.TYPE_KNOWLEDGE2: // 新知识
				listOperation = new String[] { "收藏", "转发/分享", "保存", "删除" };
				// listOperation = new String[] { "收藏", "转发/分享", "保存" };
				break;
			case IMBaseMessage.TYPE_REQUIREMENT: // 需求
				// listOperation = new String[] { "关注", "转发/分享", "删除" };
				listOperation = new String[] { "转发/分享", "删除" };// 需求长按没有关注
				// listOperation = new String[] { "转发/分享" };//需求长按没有关注
				break;
			case IMBaseMessage.TYPE_JTCONTACT_OFFLINE: // 关系
			case IMBaseMessage.TYPE_JTCONTACT_ONLINE:
			case IMBaseMessage.TYPE_ORG_OFFLINE:
			case IMBaseMessage.TYPE_ORG_ONLINE:
			case IMBaseMessage.TYPE_ORGANIZATION:
			case IMBaseMessage.TYPE_CUSTOMER:
				listOperation = new String[] { "转发/分享", "删除" };
				// listOperation = new String[] { "转发/分享"};
				break;
			case IMBaseMessage.TYPE_CONFERENCE: // 会议
				listOperation = new String[] { "转发", "删除" };
				// listOperation = new String[] { "转发"};
				break;
			default:
				break;
			}
			// 显示弹出框
			new AlertDialog.Builder(mContext)
					.setItems(listOperation,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// 封装操作类型，0-复制文字；1-转发；2-转发/分享；3-收藏知识；4-关注需求；5-保存知识；6-删除；
									int operationType = -1;
									if (msg.getType() == IMBaseMessage.TYPE_TEXT) { // 对文本操作
										if (which == 0) {
											operationType = 0;
										} else if (which == 1) {
											operationType = 1;
										} else if (which == 2) {
											operationType = 6;
										}
									} else if (msg.getType() == IMBaseMessage.TYPE_IMAGE
											|| msg.getType() == IMBaseMessage.TYPE_VIDEO
											|| msg.getType() == IMBaseMessage.TYPE_FILE
											|| msg.getType() == IMBaseMessage.TYPE_AUDIO) { // 对图片、视频、音频、文件操作
										if (which == 0) {
											operationType = 1;
										} else if (which == 1) {
											operationType = 6;
										} else if (which == 2) {
											operationType = 7;
										}
									} else if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE
											|| msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 对知识操作
										if (which == 0) {
											operationType = 3;
										} else if (which == 1) {
											operationType = 2;
										} else if (which == 2) {
											operationType = 5;
										} else if (which == 3) {
											operationType = 6;
										}
									} else if (msg.getType() == IMBaseMessage.TYPE_REQUIREMENT) { // 对需求操作
										if (which == 0) {
											operationType = 2;
										} else if (which == 1) {
											operationType = 6;
										}
									} else if (msg.getType() == IMBaseMessage.TYPE_JTCONTACT_ONLINE
											|| msg.getType() == IMBaseMessage.TYPE_JTCONTACT_OFFLINE
											|| msg.getType() == IMBaseMessage.TYPE_ORG_ONLINE
											|| msg.getType() == IMBaseMessage.TYPE_ORGANIZATION
											|| msg.getType() == IMBaseMessage.TYPE_ORG_OFFLINE
											|| msg.getType() == IMBaseMessage.TYPE_CUSTOMER) { // 对关系操作
										if (which == 0) {
											operationType = 2;
										} else if (which == 1) {
											operationType = 6;
										}
									} else if (msg.getType() == IMBaseMessage.TYPE_CONFERENCE) { // 对会议操作
										if (which == 0) {
											operationType = 1;
										} else if (which == 1) {
											operationType = 6;
										}
									}

									if (operationType == 0) { // 复制文本
										((ClipboardManager) mContext.getSystemService(mContext.CLIPBOARD_SERVICE))
												.setText(msg.getContent());
										showToast("已复制");
									} else if (operationType == 1) { // 转发
										if (msg.getType() == IMBaseMessage.TYPE_TEXT) {
											if (msg.getJtFile() == null) {
												msg.setJtFile(new JTFile());// 发送失败的文本，jtfile为null
											}
											msg.getJtFile().mFileName = msg
													.getContent();
											msg.getJtFile().mType = JTFile.TYPE_TEXT;
										}
										ENavigate.startSocialShareActivity(mContext, msg.getJtFile());
									} else if (operationType == 2) { // 转发分享
										FrameWorkUtils.showSharePopupWindow2(mContext, msg.getJtFile());
									} else if (operationType == 3) { // 收藏知识
										if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 收藏旧知识
											activity.showLoadingDialog();
											cacheUrl = "";
											CommonReqUtil
													.doFetchExternalKnowledgeUrl(
															mContext,
															bd,
															msg.getJtFile().mUrl,
															true, null);
										} else if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 收藏新知识
											activity.showLoadingDialog();
											KnowledgeReqUtil
													.doUpdateCollectKnowledge(
															mContext,
															bd,
															Long.parseLong(msg
																	.getJtFile().mTaskId),
															Integer.parseInt(msg
																	.getJtFile().reserved1),
															"", null);
										}
									} else if (operationType == 4) { // 关注需求
										activity.showLoadingDialog();
										UserReqUtil.doFocusRequirement(
														mContext,
														bd,
														UserReqUtil
																.getDoFocusRequirementParams(
																		msg.getJtFile().mTaskId,
																		true),
														null);
									} else if (operationType == 5) { // 保存知识
										if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE) { // 保存旧知识
											ENavigate.startCreateKnowledgeActivity(
															activity,
															true,
															msg.getJtFile().mUrl,
															false);
										} else if (msg.getType() == IMBaseMessage.TYPE_KNOWLEDGE2) { // 保存新知识
											activity.showLoadingDialog();
											KnowledgeReqUtil
													.doGetKnoDetailsBySaveKno(
															mContext,
															bd,
															Long.parseLong(msg
																	.getJtFile().mTaskId),
															Integer.parseInt(msg
																	.getJtFile().reserved1),
															null);
										}
									} else if (operationType == 6) { // 删除
										deleteItem(msg, position);
									} else if (operationType == 7) {// 对图片、视频、音频、文件操作
										if (messageList.get(position) != null
												&& messageList.get(position)
														.getJtFile() != null) {
											/* 图片、视频、音频、文件 的idT */
											try {
												fileIds = Util
														.getDownloadIdByUrl(messageList
																.get(position)
																.getJtFile()
																.getmUrl());
											} catch (Exception ex) {
												ex.printStackTrace();
											}
										}
										/* 跳转到二级目录，选择目录（保存目录ID */
										ENavigate.startFileManagementActivity(activity, fileIds);
									}
								}

							}).create().show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void showToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}
	
	// 删除该条聊天记录
	private void deleteItem(IMBaseMessage msg, int position) {
		int type = 0;
		long mucId = 0;
		long senderId = 0;
		IMBaseMessage imbaseMsg = getData().get(position);
		
		if(imbaseMsg.getImtype() == IMBaseMessage.IM_TYPE_MUC){
			type = 2;
			mucId = Long.valueOf(mucDetail.getId());
			senderId = Long.valueOf(imbaseMsg.getSenderID());
		}
		CommunityReqUtil.clientDeleteMessage(mContext, bd, Long.valueOf(App.getUserID()), imbaseMsg.getMessageID(), type, 0, mucId, senderId, 0, mHandler);
		this.getData().remove(position); // 删除该节点
	}
	
	/**
	 * 随时调用检查
	 * 
	 * @return
	 */
	private File getVoiceFileDir() {
		return EUtil.getChatVoiceCacheDir(mContext, communityId);
	}
	
	/**
	 * 获取群聊成员的头像
	 */
	public String getImageByMessage(IMBaseMessage msg) {
		try {
			if (!TextUtils.isEmpty(msg.getSenderID())) {
				ConnectionsMini connsMini = mucDetail.getConnectionsMiniByUserId(msg.getSenderID());
				return connsMini.getImage();
			}
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public int getSenderTypeByMessage(IMBaseMessage msg) {
		try {
			ConnectionsMini connsMini = mucDetail.getConnectionsMiniByUserId(msg.getSenderID());
			return connsMini.getType();
		} catch (Exception e) {
			return 0;
		}
	}
	
	class BmpAsyncTask extends AsyncTask<String, Void, Bitmap>{

		private final WeakReference<ImageView> imageViewReference;
		private String data = "";
		
		public BmpAsyncTask(ImageView imageView){
			imageViewReference=new WeakReference<ImageView>(imageView);
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			data=params[0];
			MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
			mediaMetadataRetriever.setDataSource(data);
			Bitmap bmpOriginal = mediaMetadataRetriever
					.getFrameAtTime(0);
			Bitmap netImage = PictureManageUtil
					.resizeBitmap(bmpOriginal, Utils.dipToPx(mContext, SCALESIZE), Utils.dipToPx(mContext, SCALESIZE));
			return netImage;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (imageViewReference != null && bitmap != null) {
				final ImageView imageView = imageViewReference.get();
				if (imageView != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}
	}

	public void setFileIconAccoredSuffixName(ImageView iv, String suffixName) {
		// 根据文件的类型 给文件设置不同的图标
		if (suffixName.equalsIgnoreCase("jpg")
				|| suffixName.equalsIgnoreCase("jpeg")
				|| suffixName.equalsIgnoreCase("png")
				|| suffixName.equalsIgnoreCase("bmp")) {
			iv.setImageResource(R.drawable.chat_ui_pic);
		} else if (suffixName.equalsIgnoreCase("mp4")
				|| suffixName.equalsIgnoreCase("rmvb")
				|| suffixName.equalsIgnoreCase("avi")
				|| suffixName.equalsIgnoreCase("mpeg")
				|| suffixName.equalsIgnoreCase("mkv")
				|| suffixName.equalsIgnoreCase("flv")) {
			iv.setImageResource(R.drawable.chat_ui_video);
		} else if (suffixName.equalsIgnoreCase("pdf")) {
			iv.setImageResource(R.drawable.chat_ui_pdf);
		} else if (suffixName.equalsIgnoreCase("txt")) {
			iv.setImageResource(R.drawable.chat_ui_txt);
		} else if (suffixName.equalsIgnoreCase("pptx")
				|| suffixName.equalsIgnoreCase("ppt")
				|| suffixName.equalsIgnoreCase("pptm")) {
			iv.setImageResource(R.drawable.chat_ui_ppt);
		} else if (suffixName.equalsIgnoreCase("doc")
				|| suffixName.equalsIgnoreCase("docx")
				|| suffixName.equalsIgnoreCase("docm")) {
			iv.setImageResource(R.drawable.chat_ui_word);
		} else if (suffixName.equalsIgnoreCase("doc")
				|| suffixName.equalsIgnoreCase("docx")) {
			iv.setImageResource(R.drawable.chat_ui_word);
		} else if (suffixName.equalsIgnoreCase("xlsx")
				|| suffixName.equalsIgnoreCase("xls")) {
			iv.setImageResource(R.drawable.chat_ui_excel);
		} else {
			iv.setImageResource(R.drawable.chat_file);
		}
	}
	
	/** 是否重新发送 */
	private void showSendMessageDialog(final IMBaseMessage msg) {
		new AlertDialog.Builder(mContext)
				.setTitle(R.string.str_again_send_title)
				.setMessage(R.string.str_again_send_hint)
				.setPositiveButton(R.string.str_ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 执行重发操作
								msg.setSendType(IMBaseMessage.SEND_TYPE_SENDING);
								activity.sendMessage(msg);
							}
						}).setNegativeButton(R.string.str_cancel, null)
				.create().show();
	}
	

	
	private ArrayList<IMBaseMessage> getMessageHasImg(List<IMBaseMessage> listMessage){
		ArrayList<IMBaseMessage> listImgMessage = new ArrayList<IMBaseMessage>();
		for(IMBaseMessage imbm : listMessage){
			if(imbm.getType() == 2){
				listImgMessage.add(imbm);
			}
		}
		return listImgMessage;
	}
	
	/** 播放录音 */
	private void startPlaying(String voicePath, ImageView view, long duration) {

		if (mPlaying && voicePath.equals(mVoicePath)) { // 语音正在播放
			// 停止播放
			stopPlaying();
		} else {
			// 停止播放
			stopPlaying();
			// 开始播放新的
			mPlayer = new MediaPlayer();
			try {
				mPlayer.setDataSource(voicePath);
				mPlayer.prepare();
				mPlayer.start();
				mVoicePath = voicePath;
				voiceIv = view;
				mPlaying = true;
				startPlayingAnim();
				mHandler.postDelayed(mStopPlayingRunnable, duration);
			} catch (IOException e) {
				stopPlaying();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}
	


	/** 停止播放录音 */
	private void stopPlaying() {
		// 停止播放
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
		// 取消回调
		mHandler.removeCallbacks(mStopPlayingRunnable);
		// 停止动画
		stopPlayingAnim();
		// 设置状态为
		mPlaying = false;
	}
	
	/**
	 * 开始播放动画
	 * 
	 * @param voiceIv
	 */
	private void startPlayingAnim() {
		if (voiceIv != null) {
			int tag = Integer.parseInt(voiceIv.getTag().toString());
			if (tag == 0) { // 本方
				voiceIv.setBackgroundResource(R.drawable.chat_to_anim);
			} else { // 对方
				voiceIv.setBackgroundResource(R.drawable.chat_from_anim);
			}
			mVoiceAnim = (AnimationDrawable) voiceIv.getBackground();
			mVoiceAnim.start();
		}
	}
	
	/**
	 * 停止播放动画
	 * 
	 * @param voiceIv
	 */
	private void stopPlayingAnim() {

		// 停止动画
		if (mVoiceAnim != null) {
			mVoiceAnim.stop();
		}
		// 设置背景
		if (voiceIv != null) {
			int tag = Integer.parseInt(voiceIv.getTag().toString());
			if (tag == 0) { // 本方
				voiceIv.setBackgroundResource(R.drawable.chat_right_voice_playing);
			} else { // 对方
				voiceIv.setBackgroundResource(R.drawable.chatfrom_voice_playing);
			}
		}
	}
	
	/** 停止录音Runnable */
	private Runnable mStopPlayingRunnable = new Runnable() {

		@Override
		public void run() {
			stopPlaying();
		}
	};
}
