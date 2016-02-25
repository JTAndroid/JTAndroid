package com.tr.ui.conference.square;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract.Events;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.baidumapsdk.BaiduLoc;
import com.tr.baidumapsdk.BaiduNavi;
import com.tr.model.conference.JTFile2ForHY;
import com.tr.model.conference.MMeetingPic;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.demand.VoicePlayer;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.conference.GridViewMeetingProfileAdapter;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.conference.common.LocalPlayer;
import com.tr.ui.conference.home.MeetingAttendInfomationActivity;
import com.tr.ui.conference.home.MeetingBranchSettingActivity;
import com.tr.ui.conference.home.RTMeetingNoteActivity;
import com.tr.ui.conference.im.MChatBaseActivity;
import com.tr.ui.demand.util.DemandUtil;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.KnowledgeDetailsScrollView;
import com.tr.ui.widgets.MeetingChatMenuPopupWindow;
import com.tr.ui.widgets.MeetingChatMenuPopupWindow.OnMeetingChatMenuItemClickListener;
import com.utils.common.EConsts;
import com.utils.common.FileDownloader;
import com.utils.common.FileDownloader.OnFileDownloadListener;
import com.utils.display.DisplayUtil;
import com.utils.string.StringUtils;
//import com.baidu.navisdk.util.common.StringUtils;

public class MeetingBranchFragment extends JBaseFragment {

	// 判断当前fragment是否是折叠的
	private boolean isFold = true;

	// 标题栏
	private ImageView mIvBackButton = null;
	private TextView mTvTitle = null;
	private ImageView mIvEdit = null;
	private ImageView mIvAttend = null;

	private final int VIDEO_SUCCESS = 1;
	private final int VIDEO_FAILURE = 2;

	private final int AUDIO_SUCCESS = 3;
	private final int AUDIO_FAILURE = 4;
	private static final int SHOW_ANIMATION = 5;

	private MMeetingTopicQuery topicQuery = null;

	@ViewInject(R.id.hy_titlebar_branch)
	private FrameLayout branchMeetingTitle;

	private Context mContext;
	private RelativeLayout viewGroup;
	// 主讲人头像
	@ViewInject(R.id.hy_branch_iv_avatar)
	private CircleImageView avatar;
	// 主讲人名称
	@ViewInject(R.id.hy_branch_tv_name)
	private TextView name;
	// 路演图片
	@ViewInject(R.id.hy_branch_meeting_gv_image)
	private GridView introductionGv;

	@ViewInject(R.id.videoRl)
	private RelativeLayout video_rl;

	// 会议介绍
	@ViewInject(R.id.hy_branch_tv_desc)
	private TextView desc;

	@ViewInject(R.id.hy_branch_meeting_toggle)
	private ImageView branchMeetingToggle;

	@ViewInject(R.id.hy_branch_meeting_scroll)
	private KnowledgeDetailsScrollView branchMeetingScroll;
	String mVoicePath = "";

	private static JTFile videoFile;
	private static JTFile audioFile;

	int startX = 0;
	int startY = 0;
	long startTime = 0;
	FrameLayout.LayoutParams moveParams;
	FrameLayout.LayoutParams moveScrollParams;
	int viewgroupHeight = 0;
	int toggleHeight = 0;
	int iniScrollHeight = 0;
	int currScrollHeight = 0;
	int titleHeight = 0;
	int baseHeight;
	private int branchMeetingMaxHeight;
	
	private long timer;
	/** 进度条 */
	private SeekBar seekBar;
	private TimerTask mTimerTask;
	/** 正在播放的对象按钮 */
	private ImageView playIv;
	/** 播放时间 */
	private TextView time;
	/** 按钮状态 */
	private TextView play_btn_tv;
	/** 记录进度条的 */
	private Timer mTimer;
	private Date date = new Date();
	private LinearLayout documentCatalogLl;
	/***/
	private ArrayList<VoicePlayer> voiceList = new ArrayList<VoicePlayer>();
	private SimpleDateFormat simpleData = new SimpleDateFormat("mm:ss");
	/** 正在播放的对象 */
	private VoicePlayer indexPlay = null;
	/** 播放器 */
	private LocalPlayer mPlayer = new LocalPlayer();
	
	/** 音频 文件 集合 */
	private ArrayList<JTFile> listVoiceJtfile = new ArrayList<JTFile>();
	/** 视频 文件 集合 */
	private ArrayList<JTFile> listVideoJtfile = new ArrayList<JTFile>();
	/** 附件 文件 集合 */
	private ArrayList<JTFile> listAppendixJtfile = new ArrayList<JTFile>();

	private List<String> imgs;
	private MMeetingQuery meetingQuery;
	private MeetingChatMenuPopupWindow popWindow;
	public MeetingBranchFragment(Context context, MMeetingQuery meetingQuery, MMeetingTopicQuery topicQuery) {
		this.mContext = context;
		this.meetingQuery = meetingQuery;
		this.topicQuery = topicQuery;
	}
	/**改变了整体最大高度(默认false)*/
	private boolean changeTheMaxViewGroupHight ;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FrameLayout.LayoutParams params = null;
		viewGroup.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//无主讲
		if (meetingQuery.getMeetingType() == 0) {
			mBaiduNavi = new BaiduNavi(getActivity());
			mIvEdit.setVisibility(View.VISIBLE);
			branchMeetingScroll.setVisibility(View.GONE);
			branchMeetingToggle.setVisibility(View.GONE);
			params = new FrameLayout.LayoutParams(windowWidth, branchMeetingTitle.getMeasuredHeight());
		} 
		//有主讲
		else {
			mIvEdit.setVisibility(View.GONE);
			/**默认下拉框是闭合的：布局的高度为：下拉框律条的高度+标题栏高度*/
			params = new FrameLayout.LayoutParams(windowWidth, branchMeetingTogglelayoutParamsHeight+branchMeetingTitle.getMeasuredHeight());
//			params = new FrameLayout.LayoutParams(windowWidth, branchMeetingToggleHeight);
		}
		viewGroup.setLayoutParams(params);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		viewGroup = (RelativeLayout) inflater.inflate(R.layout.hy_activity_meeting_branch, null);
		ViewUtils.inject(this, viewGroup);
		windowHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
		windowWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
		/**获取到  下拉框branchMeetingToggle底底部   的布局参数*/
		android.view.ViewGroup.LayoutParams branchMeetingTogglelayoutParams = branchMeetingToggle.getLayoutParams();
		if (branchMeetingTogglelayoutParams==null) {
			branchMeetingTogglelayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		/**获取到 下拉框  底部绿条的高度*/
		branchMeetingTogglelayoutParamsHeight = ViewGroup.getChildMeasureSpec(0, 0, branchMeetingTogglelayoutParams.height);
		
		branchMeetingToggleHeight = branchMeetingToggle.getMeasuredHeight();
		
		/**设置下拉框最大的高度为窗体de2/3*/
		branchMeetingMaxHeight = windowHeight * 2 / 3;
		
		mIvBackButton = (ImageView) branchMeetingTitle.findViewById(R.id.hy_layoutTitle_backBtn);
		mTvTitle = (TextView) branchMeetingTitle.findViewById(R.id.hy_layoutTitle_title);
		mIvEdit = (ImageView) branchMeetingTitle.findViewById(R.id.hy_layoutTitle_rightIconBtn1);
		mIvAttend = (ImageView) branchMeetingTitle.findViewById(R.id.hy_layoutTitle_rightIconBtn2);
		companyTv = (TextView) viewGroup.findViewById(R.id.hy_branch_tv_company);
		voiceRecordLinearlayout = (LinearLayout) viewGroup.findViewById(R.id.voiceRecordLinearlayout);
		/** 附件布局 */
		documentCatalogLl = (LinearLayout) viewGroup.findViewById(R.id.documentCatalogLl);
		mIvEdit.setBackgroundResource(R.drawable.frame_setting);
		mIvAttend.setBackgroundResource(R.drawable.hy_ic_action_relation_pressed);
		
		/**对下拉框进行触摸滑动监听*/
		branchMeetingToggle.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int moveX = 0;
				int moveY = 0;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getX();
					startY = (int) event.getY();
					
					titleHeight = branchMeetingTitle.getHeight();
					iniScrollHeight = branchMeetingScroll.getHeight();
					toggleHeight = branchMeetingToggle.getHeight();
					//最小的高度
					baseHeight = titleHeight + toggleHeight;
					/**整体的高度*/
					viewgroupHeight = viewGroup.getHeight();
					currScrollHeight = titleHeight;
					System.out.println("startY: --> " + startY);
					break;
				case MotionEvent.ACTION_MOVE:
					moveX = (int) event.getX();
					moveY = (int) event.getY();
					/**整体的高度+Y轴偏移量*/
					viewgroupHeight = viewGroup.getHeight() + moveY;
					currScrollHeight = titleHeight + moveY;
					System.out.println("viewgroupHeight:  --> " + viewgroupHeight);
					branchMeetingScroll.setVisibility(View.VISIBLE);
					//判断：此时，整体的高度是否超出窗体2/3，并且，整体的高度是否大于最低高度。（小于最大，大于最小）——>1，设置整体布局参数，2，让ScrollView滑动
					//需求改变：判断
					if (viewgroupHeight < branchMeetingMaxHeight && viewgroupHeight > baseHeight) {
						/**将scrollview展开*/
						branchMeetingScroll.scrollBy(0, -moveY);
						//如果当前未改变 整体高度为窗体2/3这个参数，那么就有可能低于这个高度
						if (!changeTheMaxViewGroupHight) {
							/**判断此时，scrollView是否完全展示（滚动到最下方）*/
							int scrollY = branchMeetingScroll.getScrollY();
							int height = branchMeetingScroll.getHeight();
							int computeVerticalScrollRange = branchMeetingScroll.computeVerticalScrollRange();
							if (scrollY+height>=computeVerticalScrollRange) {
								//滚动完全，此时整体的最大高度，修改为此时的高度
								branchMeetingMaxHeight = viewgroupHeight;
								//改变了最大高度，以后就不要判断了
								changeTheMaxViewGroupHight = true;
							}
						}
						moveParams = new FrameLayout.LayoutParams(windowWidth, viewgroupHeight);
						viewGroup.setLayoutParams(moveParams);
					}
					break;
				case MotionEvent.ACTION_UP:
					moveX = (int) event.getX();
					moveY = (int) event.getY();

					float hiddenToY = (1 - (float) (baseHeight + 0.0) / (viewgroupHeight));
					if (hiddenToY < 0) {
						hiddenToY = 0;
					}

					float showToY = (float) (viewgroupHeight + 0.0) / (branchMeetingMaxHeight);
					if (showToY > 1) {
						showToY = 1;
					}
					mAppShowAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
							Animation.RELATIVE_TO_PARENT, -(1 - showToY), Animation.RELATIVE_TO_PARENT, 0.0f);
					mAppShowAction.setDuration(500);
					Animation mAppHiddenAphaAction = new AlphaAnimation(1, 0);
					mAppHiddenAphaAction.setDuration(500);
					Animation mAppShowAphaAction = new AlphaAnimation(0, 1);
					mAppHiddenAphaAction.setDuration(500);

					AccelerateDecelerateInterpolator aclerateInterpolator = new AccelerateDecelerateInterpolator();
					AnimationSet hiddenSet = new AnimationSet(true);
					AnimationSet showSet = new AnimationSet(true);

					mAppHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
							Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -hiddenToY);
					mAppHiddenAction.setDuration(500);
					hiddenSet.addAnimation(mAppHiddenAphaAction);
					hiddenSet.addAnimation(mAppHiddenAction);
					hiddenSet.setInterpolator(aclerateInterpolator);
					showSet.setInterpolator(aclerateInterpolator);
					showSet.addAnimation(mAppShowAphaAction);
					if (isFold) {
						MChatBaseActivity mChatBaseActivity = (MChatBaseActivity) getActivity();
						mChatBaseActivity.hideSoftInput();
						FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(windowWidth, branchMeetingMaxHeight);
						viewGroup.setLayoutParams(params);
						isFold = false;
					} else {
						FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(windowWidth, baseHeight);
						viewGroup.setLayoutParams(params);
						isFold = true;
					}
					break;
				}

				return true;
			}
		});
		if (topicQuery != null) {
			initView();
			initData();
			mPlayer = new LocalPlayer();
		}
		return viewGroup;
	}
	
	public void setNotFold() {
		if (!isFold&&windowWidth!=0&&baseHeight!=0&&viewGroup!=null){
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(windowWidth, baseHeight);
			viewGroup.setLayoutParams(params);
			isFold = true;
		}
	}

	public void initView() {
		companyTv.setText(topicQuery.getTopicDesc());
		if (introductionGv!=null&&windowWidth!=0) {
			LayoutParams layoutParams = new LayoutParams(windowWidth*4/5, LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(DisplayUtil.dip2px(mContext, 10), 0, 0, 0);
			introductionGv.setLayoutParams(layoutParams);
		}
		/**主讲人介绍——图片的展示*/
		if (topicQuery.getListMeetingPic() != null && topicQuery.getListMeetingPic().size() != 0) {
			GridViewMeetingProfileAdapter gridViewMeetingProfileAdapter = new GridViewMeetingProfileAdapter(getActivity(), topicQuery.getListMeetingPic());
			introductionGv.setAdapter(gridViewMeetingProfileAdapter);

			imgs = new ArrayList<String>();
			if (topicQuery.getListMeetingPic() != null && topicQuery.getListMeetingPic().size() > 0) {
				for (MMeetingPic pic : topicQuery.getListMeetingPic()) {
					imgs.add(pic.getPicPath());
				}
			}
			introductionGv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					PhotoBrowseFragment browseFragment = new PhotoBrowseFragment(imgs, position);
					getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slow_fade_in, R.anim.slow_fade_out, R.anim.slow_fade_in, R.anim.slow_fade_out)
							.replace(R.id.fragment_container_rl, browseFragment).addToBackStack(null).commit();
				}
			});
		} else {
			introductionGv.setVisibility(View.GONE);
		}
		
		/** 会议 音频、视频、附件 都在这里 */
		List<JTFile2ForHY> listMeetingFile = topicQuery.getListMeetingFile();
		if (listMeetingFile!=null) {
			for (JTFile2ForHY jtFile2ForHY : listMeetingFile) {
				JTFile jtfile = jtFile2ForHY.toJtfile();
				/** 视频 */
				if (jtfile.mType == 2) {
					listVideoJtfile.add(jtfile);
				}
				/** 附件 */
				if (jtfile.mType == 3) {
					listAppendixJtfile.add(jtfile);
				}
				/** 音频 */
				if (jtfile.mType == 4) {
					listVoiceJtfile.add(jtfile);
				}
			}
		}
		
		/** 显示视频布局 */
		if (listVideoJtfile != null && listVideoJtfile.size() > 0) {
			video_rl.setVisibility(View.VISIBLE);
			JTFile videoJtFile = listVideoJtfile.get(0);// 只能有一个视频
			String videoPath = videoJtFile.getmUrl();
			showVideo(videoPath, video_rl);
		} else {
			video_rl.setVisibility(View.GONE);
		}
		
		/** 显示音频布局 */
		if (listVoiceJtfile != null && listVoiceJtfile.size() > 0) {
			voiceRecordLinearlayout.setVisibility(View.VISIBLE);
			for (JTFile voiceJtfile : listVoiceJtfile) {
				// 下载文件
				FileDownloader fileDownloader = new FileDownloader(getActivity(), voiceJtfile, new OnFileDownloadListener() {
					@Override
					public void onUpdate(String url, int progress) {
					}
					@Override
					public void onSuccess(String url, JTFile jtFile) {
						Message msg = handler.obtainMessage();
						msg.what = 2;
						msg.obj = jtFile;
						handler.sendMessage(msg);
					}
					@Override
					public void onStarted(String url) {
					}
					@Override
					public void onPrepared(String url) {
					}
					@Override
					public void onError(String url, int code, String errMsg) {
					}
					@Override
					public void onCanceled(String url) {
					}
				});
				fileDownloader.start();
			}
		} else {
			voiceRecordLinearlayout.setVisibility(View.GONE);
		}
		
		/** 显示附件布局 */
		if (listAppendixJtfile != null && listAppendixJtfile.size() > 0) {
			documentCatalogLl.setVisibility(View.VISIBLE);
			for (final JTFile file : listAppendixJtfile) {
				View v = View.inflate(getActivity(), R.layout.demand_need_details_document_item, null);
				TextView documentTv = (TextView) v.findViewById(R.id.documentTv);
				documentTv.setText(file.mFileName);
				if (StringUtils.isEmpty(file.getmSuffixName())) {
					file.mSuffixName = file.mFileName.substring(file.mFileName.lastIndexOf(".") + 1);
				}
				documentTv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						new DownLoadAndOpen(file.getmUrl(), file.mSuffixName).execute();
					}
				});
				documentCatalogLl.addView(v);
			}
		} else {
			documentCatalogLl.setVisibility(View.GONE);
		}
		
		if (!StringUtils.isEmpty(topicQuery.getMemberImage())) {
			Bitmap imageSync = ImageLoader.getInstance().loadImageSync(topicQuery.getMemberImage());
			if (imageSync == null) {
				avatar.setImageResource(R.drawable.hy_ic_default_friend_avatar);
			} else {
				avatar.setImageBitmap(imageSync);
			}
		} else {
			avatar.setImageResource(R.drawable.ic_share_people);
		}
		if (!StringUtils.isEmpty(topicQuery.getMemberName())) {
			name.setText(topicQuery.getMemberName());
		}

		if (!StringUtils.isEmpty(topicQuery.getTopicDesc())) {
			desc.setVisibility(View.VISIBLE);
			desc.setText(topicQuery.getTopicDesc());
		}else {
			desc.setVisibility(View.GONE);
		}
		if (!(App.getUserID().equals("" + topicQuery.getMemberId()) || App.getUserID().equals("" + meetingQuery.getCreateId()))) {
			mIvEdit.setVisibility(View.GONE);
		}
		if (meetingQuery.getMeetingStatus() == 2||meetingQuery.getMeetingStatus() == 3) {
			mIvEdit.setVisibility(View.GONE);
		}
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		if (!StringUtils.isEmpty(topicQuery.getMemberImage())) {
			ImageLoader.getInstance().displayImage(topicQuery.getMemberImage(), avatar);
		} else {
			avatar.setImageResource(R.drawable.ic_share_people);
		}
	}
	
	/**
	 * 显示视频
	 * @param url
	 * @param v
	 */
	private void showVideo(final String url, View v) {
		ImageView videoPlayIv = (ImageView) v.findViewById(R.id.videoPlayIv);
		videoPlayIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.e("resultURL", url);
				// 判断当前的网络
				if (!DemandUtil.isVideo(getActivity())) {
					showToast("已设置为当前的网络不能播放视频");
					return;
				}
				// 调用系统播放器
				Intent video = new Intent(Intent.ACTION_VIEW);
				if (!(new File(url).isFile())) {
					video.setDataAndType(Uri.parse(url), "video/*");
				} else {
					video.setDataAndType(Uri.parse("file://" + url), "video/*");
				}
				startActivity(video);
			}
		});
	}
	
	public void initData() {
		
		if (meetingQuery.getMeetingType()==0) {
			mTvTitle.setText(meetingQuery.getMeetingName());
		}else {
			mTvTitle.setText(topicQuery.getTopicContent());
		}

		// 回退键
		mIvBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (meetingTopicDataChanged) {
					getActivity().setResult(0xffffffff);
				}
				getActivity().finish();
			}
		});
		// 编辑键
		mIvEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popWindow == null) {
					popWindow = new MeetingChatMenuPopupWindow(mContext);
					popWindow.setOnItemClickListener(new OnMeetingChatMenuItemClickListener() {
						@Override
						public void noticeClick() {
							SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							try {
								if (false == meetingQuery.getStartTime().isEmpty()) {
									Date tmp = (fmt.parse(meetingQuery.getStartTime()));
									Calendar cal = Calendar.getInstance();
									Intent intent = new Intent(Intent.ACTION_EDIT);
									intent.setType("vnd.android.cursor.item/event");
									intent.putExtra("beginTime", tmp.getTime());
									intent.putExtra("allDay", false);
									intent.putExtra("rrule", "FREQ=DAILY");
									intent.putExtra("endTime", tmp.getTime() + 60 * 60 * 1000);
									intent.putExtra("title", meetingQuery.getMeetingName());
									intent.putExtra("description", meetingQuery.getMeetingDesc());
									intent.putExtra(Events.EVENT_LOCATION, meetingQuery.getMeetingAddress());
									startActivity(intent);
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						
						@Override
						public void notesClick() {
							//跳转到会议笔记页面
							Intent intent = new Intent(getActivity(), RTMeetingNoteActivity.class);
							intent.putExtra("meeting_id",(int)meetingQuery.getId());
//							intent.putExtra("is_edit", true);
							startActivity(intent);
						}
						
						@Override
						public void navigationClick() {
							if (null != mBaiduNavi) {
								BaiduLoc aLoc = App.getApp().getBaiduLoc();
								if (aLoc.isLocationValid(aLoc.getLongitude(), aLoc.getLatitude())) {
									double endLatitude = 0;
									double endLongitude = 0;
									if (null != meetingQuery.getMeetingAddressPosY()) {
										endLatitude = Double.valueOf(meetingQuery.getMeetingAddressPosY());
									}
									if (null != meetingQuery.getMeetingAddressPosX()) {
										endLongitude = Double.valueOf(meetingQuery.getMeetingAddressPosX());
									}
									if (aLoc.isLocationValid(endLongitude, endLatitude)) {
										mBaiduNavi.naviGuide(aLoc.getLatitude(), aLoc.getLongitude(), aLoc.getAddress(), endLatitude, endLongitude, meetingQuery.getMeetingAddress());
									} else {
										Toast.makeText(mContext, "会议地址未知", 0).show();
									}
								} else {
									Toast.makeText(mContext, "您的地址未知", 0).show();
								}
							}
						}
					});
					popWindow.showAsDropDown(branchMeetingTitle);
				}else {
					if (popWindow.isShowing()) {
						popWindow.dismiss();
					}else {
						popWindow.showAsDropDown(branchMeetingTitle);
					}
				}
				
//				// TODO 如果是会议创建人 或者是 分会场主讲人 才能修改
//				if (App.getUserID().equals("" + topicQuery.getMemberId()) || App.getUserID().equals("" + meetingQuery.getCreateId())) {
//					Intent intent = new Intent(getActivity(), MeetingTopicEditActivity.class);
//					intent.putExtra(ENavConsts.EMeetingDetail, meetingQuery);
//					intent.putExtra(ENavConsts.EMeetingTopicDetail, topicQuery);
//					getActivity().startActivityForResult(intent, EConsts.CALL_MEETING_TOPIC_EDIT);
//				} else {
//					// Toast.makeText(mContext, "非会议创建人或者主讲人不能编辑会议", 0).show();
//				}
			}
		});
		avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ENavigate.startRelationHomeActivity(mContext, topicQuery.getMemberId()+"");
			}
		});
		// 参会人
		mIvAttend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//单议题（无主讲人） 进 入   与会信息 界面
				//多议题（有主讲人）进入    分会场设置界面
				if (null != meetingQuery) {
					//无主讲人
					if (meetingQuery.getMeetingType() == 0) {
						Intent intent = new Intent(getActivity(), MeetingAttendInfomationActivity.class);
						intent.putExtra(ENavConsts.EMeetingDetail, meetingQuery);
						startActivityForResult(intent, EConsts.CALL_MEETING_ATTEND_INFOMATION);
					}
					else {
//						Intent intent = new Intent(getActivity(), AttendeesActivity.class);
//						intent.putExtra(ENavConsts.EMeetingDetail, meetingQuery);
//						// 参会人的情况 0代表 主会场中的参会人 1代表其他的参会人
//						intent.putExtra("AttendeeType", 0);
//						getActivity().startActivity(intent);
						//分会场设置
						Intent intent = new Intent(getActivity(), MeetingBranchSettingActivity.class);
						intent.putExtra(ENavConsts.EMeetingDetail, meetingQuery);
						intent.putExtra(ENavConsts.EMeetingTopicDetail, topicQuery);
						getActivity().startActivityForResult(intent, EConsts.CALL_MEETING_TOPIC_EDIT);
					}
				}
			}
		});
	}
	public interface OnToggleClickListener {
		void onToggleClick();
	}

	private OnToggleClickListener onToggleClickListener;

	private Animation mAppShowAction;

	private Animation mAppHiddenAction;

	private int windowHeight;

	private int windowWidth;

	/**主讲人描述*/
	private TextView companyTv;

	private LinearLayout voiceRecordLinearlayout;

	public void setOnToggleClick(OnToggleClickListener onToggleClickListener) {
		this.onToggleClickListener = onToggleClickListener;
	}
	private Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			if (msg.what == 1) {
				date.setTime(timer);
				time.setText(simpleData.format(date));
			} else if (msg.what == 2) {
				// 下载成功
				JTFile jtFile = (JTFile) msg.obj;
				// 点击确定按钮
				VoicePlayer voPlayer = new VoicePlayer();
				File voiceFile = new File(jtFile.mLocalFilePath);
				voPlayer.file = voiceFile;
				MediaPlayer mediaPlayer = new MediaPlayer();
				try {
					mediaPlayer.setDataSource(jtFile.getmUrl());
					mediaPlayer.prepare();
					int duration = mediaPlayer.getDuration();
					voPlayer.time = duration;
					mediaPlayer.release();
					mediaPlayer = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
				addPlayView(voPlayer, jtFile);
			}
		};
	};
	/**
	 * 创建一个音频播放器对象到界面中
	 * 
	 * @param voice
	 */
	private void addPlayView(VoicePlayer play, JTFile jtFile) {
		if (voiceList.add(play)) {
			View convertView = View.inflate(getActivity(), R.layout.demand_play_item, null);
			ImageView playIv = (ImageView) convertView.findViewById(R.id.play_start_iv); // 播放按钮
			TextView timeTv = (TextView) convertView.findViewById(R.id.play_time_tv);// 播放时间进度
			SeekBar seekBar = (SeekBar) convertView.findViewById(R.id.play_seekBar);// 播放时间长度
			ImageView deleteIv = (ImageView) convertView.findViewById(R.id.play_delete_iv);// 删除按钮
			TextView btnTv = (TextView) convertView.findViewById(R.id.play_btn_tv);//
			convertView.findViewById(R.id.view).setVisibility(View.GONE);
			deleteIv.setVisibility(View.GONE);
			playIv.setTag(play);
			timeTv.setTag(play);
			seekBar.setTag(play);
			btnTv.setTag(play);
			deleteIv.setTag(play);

			deleteIv.setOnClickListener(null); // 删除方法
			playIv.setOnClickListener(playBtnListener);
			seekBar.setOnSeekBarChangeListener(null);
			// btnTv.setOnClickListener(threadBtn);
			play.id = System.currentTimeMillis();
			seekBar.setProgress(0);
			date.setTime(play.time);
			timeTv.setText(simpleData.format(date));// 分秒
			voiceRecordLinearlayout.addView(convertView);
		}
	}
	/**
	 * 播放器控制按钮
	 */
	private OnClickListener playBtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			VoicePlayer player = (VoicePlayer) v.getTag();
			if (indexPlay != null && player.id == indexPlay.id) {
				// 是自己在播放
				if (mPlayer.isPlay()) { // 暂停
					v.setBackgroundResource(R.drawable.recordplay);
					mPlayer.pause();
					mTimerTask.cancel();
				} else {// 播放
					v.setBackgroundResource(R.drawable.recordpause);
					mPlayer.start();
					startTime();
				}
			} else {
				if (mPlayer.isPlay())
					reductionView();
				indexPlay = player;
				// 自己开始进行播放 修改自己的状态
				v.setBackgroundResource(R.drawable.recordpause);
				mPlayer.startPlay(player.file.getPath(), onCompletion);
				getView(v);
				startTime();// 开始计时器
			}
		}
	};
	
	/**
	 * 状态还原
	 */
	private void reductionView() {
		if (mPlayer != null) {
			mPlayer.stopPlay();// 停止播放
		}
		if (indexPlay != null) {
			timer = 0;
			seekBar.setProgress(0);
			mTimerTask.cancel();
			date.setTime(indexPlay.time);
			time.setText(simpleData.format(date));
			playIv.setBackgroundResource(R.drawable.recordplay);
			indexPlay = null;
		}
	}
	
	/**
	 * 播放器控制事件
	 */
	private OnCompletionListener onCompletion = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			reductionView();
		}
	};

	/**
	 * 播放器控制计时器
	 */
	private void startTime() {
		mTimer = new Timer();
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				if (null != seekBar && null != mPlayer) {
					seekBar.setProgress(mPlayer.getProgress());
					timer += 10;
					handler.sendEmptyMessage(1);
				}
			}
		};
		if (null != mPlayer && null != mTimer && null != mTimerTask) {
			mTimer.schedule(mTimerTask, 0, 10);
		}
	}
	
	/**
	 * 获取当前播放的对象 的控件信息
	 */
	private void getView(View childs) {
		View view = (View) childs.getParent();
		playIv = (ImageView) view.findViewById(R.id.play_start_iv);
		time = (TextView) view.findViewById(R.id.play_time_tv);
		seekBar = (SeekBar) view.findViewById(R.id.play_seekBar);
		play_btn_tv = (TextView) view.findViewById(R.id.play_btn_tv);
	}
	private DownLoadAndOpen downLoadAndOpen;

	private int branchMeetingToggleHeight;

	private int branchMeetingTogglelayoutParamsHeight;

	private boolean meetingTopicDataChanged;

	private BaiduNavi mBaiduNavi;
	
	class DownLoadAndOpen extends AsyncTask<String, Void, File> {
		private String fileUrl;
		private String fileType;

		public DownLoadAndOpen(String fileUrl, String fileType) {
			this.fileUrl = fileUrl;
			this.fileType = fileType;
		}

		@Override
		protected File doInBackground(String... paramsArr) {
			if (null != downLoadAndOpen) {
				downLoadAndOpen.cancel(false);
			}
			downLoadAndOpen = this;

			File fileCache = null;
			File uploadFileDir = new File(Environment.getExternalStorageDirectory(), "/jt/fileCache");
			if (!uploadFileDir.exists()) {
				uploadFileDir.mkdirs();
			}
			String fileName = UUID.randomUUID().toString();
			try {
				fileCache = new File(uploadFileDir, fileName);
				if (!fileCache.exists()) {
					fileCache.createNewFile();
				}

				URL url = new URL(fileUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Accept-Encoding", "identity");
				conn.connect();

				if (conn.getResponseCode() == 200) {
					// 创建输入流
					InputStream inputStream = conn.getInputStream();

					OutputStream outputStream = new FileOutputStream(fileCache);
					byte[] data = new byte[2048];
					int length = 0;
					while ((length = inputStream.read(data)) != -1) {
						outputStream.write(data, 0, length);
					}
					inputStream.close();
					outputStream.close();
				} else {
					fileCache = null;
				}

			} catch (Exception e) {
				fileCache = null;
			}
			return fileCache;
		}

		@Override
		protected void onPostExecute(File openFile) {
			if (!isCancelled()) {
				dismissLoadingDialog();
				if (null == openFile) {
					Toast.makeText(getActivity(), "附件下载失败", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = null;
				String temp = fileType.replaceAll("^([\\s\\S]*)([tT][xX][tT])$", "$2");
				if (fileType.replaceAll("^([\\s\\S]*)([tT][xX][tT]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri2 = Uri.fromFile(openFile);
					intent.setDataAndType(uri2, "text/plain");
				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][dD][fF]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

				} else if (fileType.replaceAll("^([\\s\\S]*)([dD][oO][cC]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/msword");

				} else if (fileType.replaceAll("^([\\s\\S]*)([dD][oO][cD][xX]) *$", "$2").length() == 4) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/msword");

				} else if (fileType.replaceAll("^([\\s\\S]*)([xX][lL][sS]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-excel");

				} else if (fileType.replaceAll("^([\\s\\S]*)([xX][lL][sS][xX]) *$", "$2").length() == 4) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-excel");

				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][pP][tT]) *$", "$2").length() == 3) {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
				} else if (fileType.replaceAll("^([\\s\\S]*)([pP][nN][gG]) *$", "$2").length() == 3 || fileType.replaceAll("^([\\s\\S]*)([jJ][pP][gG]) *$", "$2").length() == 3
						|| fileType.replaceAll("^([\\s\\S]*)([jJ][pP][eE][gG]) *$", "$2").length() == 4) {

					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "image/*");
				} else {
					intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri = Uri.fromFile(openFile);
					intent.setDataAndType(uri, "*/*");
				}
				startActivity(intent);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		if (downLoadAndOpen!=null) {
			downLoadAndOpen.cancel(true);
		}
		if (popWindow!=null&&popWindow.isShowing()) {
			popWindow.dismiss();
		}
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	public void setMeetingTopicDataChanged(boolean meetingTopicDataChanged) {
		this.meetingTopicDataChanged = meetingTopicDataChanged;
	}
}
