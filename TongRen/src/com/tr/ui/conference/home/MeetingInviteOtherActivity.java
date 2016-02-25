package com.tr.ui.conference.home;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

import com.tr.R;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.obj.JTContactMini;
import com.tr.navigate.ENavConsts;
import com.tr.ui.conference.common.BaseActivity;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 其他邀请
 * 
 * @author d.c
 */

public class MeetingInviteOtherActivity extends BaseActivity implements IBindData {
	// 标题栏
	private LinearLayout mIvBackButton = null;
	private TextView mTvTitle = null;
	private Button mBtnCopy = null;

	private RelativeLayout ll_invite_by_sms = null;
	private RelativeLayout ll_invite_by_email = null;
	private RelativeLayout ll_invite_by_weibo = null;
	private RelativeLayout ll_invite_by_weixin = null;
	private RelativeLayout ll_invite_by_face = null;

	private MMeetingQuery mMeetingQuery = null;
	private List<JTContactMini> selectedList = null;

	public int CALL_MEETING_INVITE_FIREND = 0x10000013;

	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_meeting_invite_other);
		mIvBackButton = (LinearLayout) findViewById(R.id.hy_layoutTitle_backBtn);
		mTvTitle = (TextView) findViewById(R.id.hy_layoutTitle_title);
		mBtnCopy = (Button) findViewById(R.id.hy_bth_invite_copy_url);

		ll_invite_by_sms = (RelativeLayout) findViewById(R.id.hy_ll_invite_by_sms);
		ll_invite_by_email = (RelativeLayout) findViewById(R.id.hy_ll_invite_by_email);
		ll_invite_by_weibo = (RelativeLayout) findViewById(R.id.hy_ll_invite_by_weibo);
		ll_invite_by_weixin = (RelativeLayout) findViewById(R.id.hy_ll_invite_by_weixin);
		ll_invite_by_face = (RelativeLayout) findViewById(R.id.hy_ll_invite_by_face);
	}

	@Override
	public void initData() {
		mTvTitle.setText("邀请");

		mMeetingQuery = (MMeetingQuery) getIntent().getSerializableExtra(ENavConsts.EMeetingDetail);
		if (null == mMeetingQuery) {
			Toast.makeText(this, "无效的会议数据", 0).show();
			finish();
		}

		mIvBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mBtnCopy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(MeetingInviteOtherActivity.this, "复制成功！", 0).show();
				ClipboardManager clipboard = (ClipboardManager) getSystemService(MeetingInviteOtherActivity.CLIPBOARD_SERVICE);
				clipboard.setText("http://static.gintong.com/resources/appweb/index.html");
			}
		});
		ll_invite_by_sms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = mMeetingQuery.getCreateName();
				content += "邀请您参加会议，会议时间：";
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分");
				try {
					if (false == mMeetingQuery.getStartTime().isEmpty()) {
						Date tmp = (fmt.parse(mMeetingQuery.getStartTime()));
						content += dateFormat.format(tmp);
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
				content += "，";
				content += "http://static.gintong.com/resourc0es/appweb/index.html";
				// content+=EAPIConsts.TMS_URL+"go/1/"+mMeetingQuery.getId();
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
				intent.putExtra("sms_body", content);
				startActivity(intent);
			}
		});
		ll_invite_by_email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = mMeetingQuery.getCreateName() + "邀请您参加会议";
				String content = mMeetingQuery.getCreateName();
				content += "邀请您参加会议，会议时间：";
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分");
				try {
					if (false == mMeetingQuery.getStartTime().isEmpty()) {
						Date tmp = (fmt.parse(mMeetingQuery.getStartTime()));
						content += dateFormat.format(tmp);
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
				content += "，";
				content += "http://static.gintong.com/resources/appweb/index.html";
				// content+=EAPIConsts.TMS_URL+"go/1/"+mMeetingQuery.getId();
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.putExtra(Intent.EXTRA_SUBJECT, title);
				intent.putExtra(Intent.EXTRA_TEXT, content);
				intent.setType("message/rfc822");
				startActivity(Intent.createChooser(intent, "选择邮件程序"));
			}
		});
		ll_invite_by_weibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = mMeetingQuery.getCreateName();
				content += "邀请您参加会议，会议时间：";
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分");
				try {
					if (false == mMeetingQuery.getStartTime().isEmpty()) {
						Date tmp = (fmt.parse(mMeetingQuery.getStartTime()));
						content += dateFormat.format(tmp);
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
				content += "，";
				content += "http://static.gintong.com/resources/appweb/index.html";
				// content+=EAPIConsts.TMS_URL+"go/1/"+mMeetingQuery.getId();
				OnekeyShare oks = new OnekeyShare();
//				oks.setNotification(R.drawable.ic_launcher, "");
				oks.setPlatform(SinaWeibo.NAME);
				oks.setText(content);
				oks.setTitleUrl("http://gintong.com");
				oks.setUrl("http://sharesdk.cn");
				oks.setSiteUrl("http://gintong.com");
				oks.setSilent(false);
				oks.show(MeetingInviteOtherActivity.this);

			}
		});
		ll_invite_by_weixin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = mMeetingQuery.getCreateName() + "邀请您参加会议";
				String content = mMeetingQuery.getCreateName();
				content += "邀请您参加会议，会议时间：";
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH点mm分");
				try {
					if (false == mMeetingQuery.getStartTime().isEmpty()) {
						Date tmp = (fmt.parse(mMeetingQuery.getStartTime()));
						content += dateFormat.format(tmp);
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
				content += "，";
				content += "http://static.gintong.com/resources/appweb/index.html";
				// content+=EAPIConsts.TMS_URL+"go/1/"+mMeetingQuery.getId();
				OnekeyShare oks = new OnekeyShare();
//				oks.setNotification(R.drawable.ic_launcher, "");
				oks.setPlatform(Wechat.NAME);
				oks.setTitle(title);
				oks.setText(content);
				oks.setTitleUrl("http://gintong.com");
				oks.setUrl("http://sharesdk.cn");
				oks.setSiteUrl("http://gintong.com");
				oks.setSilent(false);
				oks.show(MeetingInviteOtherActivity.this);
			}
		});
		ll_invite_by_face.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MeetingInviteOtherActivity.this, MeetingInviteFaceActivity.class);
				intent.putExtra(ENavConsts.EMeetingDetail, mMeetingQuery);
				startActivity(intent);
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
		if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_INVITATIONBYFACETOFACE && null != object) {
			SimpleResult flag = (SimpleResult) object;
			if (null != flag) {
				if (flag.isSucceed()) {
					Toast.makeText(this, "邀请成功", 0).show();
				} else {
					Toast.makeText(this, "邀请失败", 0).show();
				}
			}

			if (null != selectedList) {
				if (selectedList.size() > 0) {

					JTContactMini aContact = selectedList.get(0);
					selectedList.remove(0);
					if (null != aContact) {
						MMeetingMember aMem = new MMeetingMember();
						aMem.setAttendMeetStatus(0);
						aMem.setAttendMeetType(0);
						aMem.setMeetingId(mMeetingQuery.getId());
						String ids = aContact.id;
						Long idl = Long.valueOf(ids);
						if (null != ids) {
							long id = idl.longValue();
							aMem.setMemberId(id);
						}
						aMem.setMemberMeetStatus(0);
						aMem.setMemberName(aContact.name);
						aMem.setMemberPhoto(aContact.image);
						aMem.setMemberType(0);

						showLoadingDialog();
						ConferenceReqUtil.doInvitationFaceToFace(this, this, aMem, null);
					}
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CALL_MEETING_INVITE_FIREND) {
			if (resultCode == RESULT_OK) {
				Bundle b = data.getExtras();
				selectedList = (List<JTContactMini>) b.get("value");
				if (null != selectedList) {
					if (selectedList.size() > 0) {

						JTContactMini aContact = selectedList.get(0);
						selectedList.remove(0);
						if (null != aContact) {
							MMeetingMember aMem = new MMeetingMember();
							aMem.setAttendMeetStatus(0);
							aMem.setAttendMeetType(0);
							aMem.setMeetingId(mMeetingQuery.getId());
							String ids = aContact.id;
							Long idl = Long.valueOf(ids);
							if (null != ids) {
								long id = idl.longValue();
								aMem.setMemberId(id);
							}
							aMem.setMemberMeetStatus(0);
							aMem.setMemberName(aContact.name);
							aMem.setMemberPhoto(aContact.image);
							aMem.setMemberType(0);

							showLoadingDialog();
							ConferenceReqUtil.doInvitationFaceToFace(this, this, aMem, null);
						}
					}
				}
			}
		}
	}
}