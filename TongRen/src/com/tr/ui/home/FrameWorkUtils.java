package com.tr.ui.home;

/** sunjianan */
import java.util.HashMap;
import java.util.Hashtable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;

//import com.baidu.navisdk.util.common.StringUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tr.App;
import com.tr.R;
import com.tr.model.home.MShareContent;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.obj.DynamicNews;
import com.tr.model.obj.ForwardDynamicNews;
import com.tr.model.obj.JTContact2;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavigate;
import com.tr.ui.widgets.EProgressDialog;
import com.utils.http.EAPIConsts;
import com.utils.log.ToastUtil;
import com.utils.string.StringUtils;

public class FrameWorkUtils {

	/**
	 * 根据字符串生成二维码
	 * 
	 * @param str
	 *            字符串
	 * @param imgWidth
	 *            二维码宽度
	 * @param imgHeight
	 *            二维码高度
	 * @return 二维码bitmap
	 * @throws WriterException
	 */
	public static Bitmap createQRCode(String str, int imgWidth, int imgHeight) throws WriterException {

		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 300, 300, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int i = 0; i < 300; i++) {
			for (int j = 0; j < 300; j++) {
				if (matrix.get(i, j)) {
					pixels[i * width + j] = 0xff000000;
				} else {
					pixels[i * width + j] = 0xfffffff;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				Toast.makeText(App.getApplicationConxt(), "分享失败", 0).show();
			} else if (msg.what == 2) {
				Toast.makeText(App.getApplicationConxt(), "请先安装微信", 0).show();

			}else{
				Toast.makeText(App.getApplicationConxt(), "分享成功", 0).show();
			}
		};
	};

	private static PopupWindow popupWindow;

	/**
	 * 我的页面分享
	 * 
	 * @param context
	 */
	public static void showSharePopupWindow(final Context context, String qrcode) {
		/** 点击头像后，弹出popupwindow */

		View view = View.inflate(context, R.layout.my_home_page_share, null);
		ImageView logo_sms = (ImageView) view.findViewById(R.id.logo_sms);
		ImageView logo_qq = (ImageView) view.findViewById(R.id.logo_qq);
		ImageView logo_wechat = (ImageView) view.findViewById(R.id.logo_wechat);
		ImageView logo_sinaweibo = (ImageView) view.findViewById(R.id.logo_sinaweibo);
		ImageView logo_wechatmoments = (ImageView) view.findViewById(R.id.logo_wechatmoments);
		// ImageView frame_qr_code = (ImageView)
		// view.findViewById(R.id.frame_qr_code);
		// ImageView frame_app_avatar = (ImageView)
		// view.findViewById(R.id.frame_app_avatar);
		TextView cancel = (TextView) view.findViewById(R.id.cancel);
		// Bitmap bitmap = null;
		// try {
		// bitmap = FrameWorkUtils.createQRCode(qrcode, Utils.dipToPx(context,
		// 200), Utils.dipToPx(context, 200));
		// } catch (WriterException e) {
		// e.printStackTrace();
		// }
		// frame_qr_code.setImageBitmap(bitmap);

		// ImageLoader.getInstance().displayImage(App.getUser().getImage(),
		// frame_app_avatar);

		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.PupwindowAnimation);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		logo_sms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * Intent intent = new Intent(Intent.ACTION_VIEW);
				 * intent.setType("vnd.android-dir/mms-sms");
				 */
				Intent intent = new Intent(Intent.ACTION_SENDTO);
				intent.setData(Uri.parse("smsto:"));
				intent.putExtra("sms_body", "我正在使用金桐app，一款集商务社交、投融资项目对接、个人资源管理的商务应用神器！ 推荐给你，快来哦，轻点 http://app.gintong.com 即可下载。");
				context.startActivity(intent);
				popupWindow.dismiss();
			}
		});
		logo_qq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ShareUtils.share_QQFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {

					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
					}
				}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
				popupWindow.dismiss();
			}
		});
		logo_wechat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


				ShareUtils.share_WxFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						Message msg = new Message();
						handler.sendEmptyMessage(1);
			
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
					
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						
					}
				}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
				popupWindow.dismiss();
			}

		});
		logo_sinaweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
				ShareUtils.share_SinaWeibo(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						
					}
				}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
				popupWindow.dismiss();
			}
		});
		logo_wechatmoments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
				ShareUtils.share_CircleFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
					
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						
						handler.sendEmptyMessage(3);
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						
					}
				}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
				popupWindow.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	/** @param context */
	@Deprecated
	public static void showSharePopupWindow2(final Context context, final JTContact2 jtContact) {
		/** 点击头像后，弹出popupwindow */

		View view = View.inflate(context, R.layout.activity_share_to_apps, null);
		ImageView logo_qq = (ImageView) view.findViewById(R.id.logo_qq);
		ImageView logo_wechat = (ImageView) view.findViewById(R.id.logo_wechat);
		ImageView logo_sinaweibo = (ImageView) view.findViewById(R.id.logo_sinaweibo);
		ImageView logo_wechatmoments = (ImageView) view.findViewById(R.id.logo_wechatmoments);
		// ImageView frame_qr_code = (ImageView)
		// view.findViewById(R.id.frame_qr_code);
		// ImageView frame_app_avatar = (ImageView)
		// view.findViewById(R.id.frame_app_avatar);
		LinearLayout logo_friends = (LinearLayout) view.findViewById(R.id.logo_friends);
		LinearLayout logo_sociality = (LinearLayout) view.findViewById(R.id.logo_sociality);

		TextView cancel = (TextView) view.findViewById(R.id.cancel);

		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.PupwindowAnimation);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		logo_qq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);
				ShareUtils.share_QQFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();

					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, ShareConfig.share_title2, ShareConfig.share_title_url2, ShareConfig.share_text2, ShareConfig.image_url2);
				popupWindow.dismiss();
			}
		});
		logo_wechat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);

				ShareUtils.share_WxFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						Message msg = new Message();
						handler.sendEmptyMessage(1);
						dismissLoadingDialog();
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
				popupWindow.dismiss();
			}

		});
		logo_sinaweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);
				ShareUtils.share_SinaWeibo(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
				popupWindow.dismiss();
			}
		});
		logo_wechatmoments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);
				ShareUtils.share_CircleFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(3);
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
				popupWindow.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		logo_friends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** 将转发的内容转成转发动态对象 */
				ForwardDynamicNews forwardDynamicNews = new ForwardDynamicNews();
				forwardDynamicNews.content = jtContact.getUserJob();
				forwardDynamicNews.createrId = App.getUserID();
				forwardDynamicNews.imgPath = jtContact.getIconUrl();
				forwardDynamicNews.targetId = jtContact.getUser().id;
				forwardDynamicNews.title = jtContact.getName();
				forwardDynamicNews.type = DynamicNews.TYPE_USER_CARD + "";
				forwardDynamicNews.lowType = "0";
				ENavigate.startForwardToFriendActivity(context, forwardDynamicNews);
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		logo_sociality.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				JTFile jTfile = jtContact.toJTfile();
				ENavigate.startSocialShareActivity(context, jTfile);
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	/*转发第三方*/
	static String shareOtherUrlString = "";
	static String httpAddress = "";
	static final String GINTONGWEBADDRESS = "http://www.gintong.com";
	/** @param context */
	public static void showSharePopupWindow2(final Context context, final JTFile jtFile) {
		/** 点击头像后，弹出popupwindow */

		MShareContent shareContent1 = new MShareContent();
		if(EAPIConsts.ENVIRONMENT==2)
		httpAddress = "http://www.gintong.com//";//beta
		else if(EAPIConsts.ENVIRONMENT == EAPIConsts.SIMULATER){
			httpAddress = "http://test.online.gintong.com//";//simulater
		}else {
			httpAddress = EAPIConsts.getTMSUrl();
			int end=httpAddress.lastIndexOf(":");
			httpAddress=httpAddress.substring(0, end)+"//";
		}	
//		httpAddress = "http://192.168.120.144/";
//		httpAddress = "http://192.168.101.90:8012/"; QA
//		httpAddress = "http://192.168.101.41/";sim
		switch (jtFile.getmType()) {
		// 关系
		case JTFile.TYPE_ORG_OFFLINE:
		case JTFile.TYPE_ORG_ONLINE:
		
		case JTFile.TYPE_JTCONTACT_OFFLINE:
		case JTFile.TYPE_JTCONTACT_ONLINE:
			shareContent1.setImageUrl(jtFile.getmUrl());
			shareContent1.setShareText(jtFile.mSuffixName + " " + jtFile.reserved1);
			shareContent1.setTitle(jtFile.mFileName);
			shareContent1.setTitleUrl("http://static.gintong.com/resources/appweb/index.html");
			/*
			 * 用户人脉
			 * http://www.gintong.com/person.html?id=13315&personType=1&view=1
			 * id = [人脉或用户id] & personType = [1(用户) or 2(人脉)] & view = [1(view=1不走权限控制)]
			 * 
			 */
			if(!TextUtils.isEmpty(jtFile.virtual)){
				shareOtherUrlString = httpAddress + "html/person.html?id="+jtFile.mTaskId+"&view=1&personType="+jtFile.virtual;
			}else{
				if(JTFile.TYPE_JTCONTACT_ONLINE == jtFile.getmType()){
					shareOtherUrlString = httpAddress + "html/person.html?id="+jtFile.mTaskId+"&view=1&personType=1";
				}else{
					shareOtherUrlString = httpAddress + "html/person.html?id="+jtFile.mTaskId+"&view=1&personType=2";
				}
			}

//			shareOtherUrlString = GINTONGWEBADDRESS + "html/person.html?id="+jtFile.mTaskId+"&view=1&personType="+jtFile.virtual;
			break;
		
	
			
			// 新知识
		case JTFile.TYPE_KNOWLEDGE2:
			shareContent1.setImageUrl(jtFile.getmUrl());
			shareContent1.setShareText(jtFile.getmSuffixName());
			shareContent1.setTitle(jtFile.reserved2);
			// there is a unfixed undone
			if (!StringUtils.isEmpty(jtFile.reserved3)) {
				shareContent1.setTitleUrl(jtFile.reserved3);
			} else {
				shareContent1.setTitleUrl("http://static.gintong.com/resources/appweb/index.html");
			}
			//知识 http://www.gintong.com/html/knowledge.html?id=19451&type=1  id:知识ID;type:知识类型
			if(!TextUtils.isEmpty(jtFile.virtual)){
				shareOtherUrlString = httpAddress + "html/knowledge.html?id="+jtFile.mTaskId+"&type="+jtFile.virtual;	
			}else{
				shareOtherUrlString = httpAddress + "html/knowledge.html?id="+jtFile.mTaskId+"&type="+jtFile.reserved1;	
			}
//			shareOtherUrlString = GINTONGWEBADDRESS + "html/knowledge.html?id="+jtFile.mTaskId+"&type="+jtFile.virtual;
			break;
		// 会议
		case JTFile.TYPE_CONFERENCE:
			shareContent1.setImageUrl(jtFile.getmUrl());
			shareContent1.setShareText(jtFile.reserved1);
			shareContent1.setTitle(jtFile.getmSuffixName());
			shareContent1.setTitleUrl("http://static.gintong.com/resources/appweb/index.html");
			shareOtherUrlString = httpAddress + "html/meeting.html?id="+jtFile.mTaskId;
//			shareOtherUrlString = GINTONGWEBADDRESS + "html/meeting.html?id="+jtFile.mTaskId;
			break;
		case JTFile.TYPE_DEMAND:
		case JTFile.TYPE_REQUIREMENT:
			// 需求
			shareContent1.setImageUrl(jtFile.getmUrl());
			shareContent1.setShareText(jtFile.reserved1);
			shareContent1.setTitle(jtFile.mFileName);
			shareContent1.setTitleUrl("http://static.gintong.com/resources/appweb/index.html");
			//需求 http://www.gintong.com/html/demand.html?demandId=19451&fType=2 demandId:需求ID;fType:不带权限
			shareOtherUrlString = httpAddress + "html/demand.html?demandId="+jtFile.mTaskId+"&fType=2";
//			shareOtherUrlString = GINTONGWEBADDRESS + "html/demand.html?demandId="+jtFile.mTaskId+"&fType=2";
			break;
		case JTFile.TYPE_CLIENT: //客户
		case JTFile.TYPE_ORGANIZATION:// 组织

			shareContent1.setImageUrl(jtFile.getmUrl());
			shareContent1.setShareText(jtFile. reserved1);
			shareContent1.setTitle(jtFile.mSuffixName);
			shareContent1.setTitleUrl("http://static.gintong.com/resources/appweb/index.html");
			//组织客户  http://www.gintong.com/html/organ.html?orgId=57&view=2&virtual=0  virtual:0:客户 1:组织;view:值是固定的2，代表大数据推送组织;orgId:组织ID
//			shareOtherUrlString = httpAddress + "html/organ.html?orgId="+jtFile.mTaskId+"&view=2&virtual="+jtFile.virtual;
//			shareOtherUrlString = GINTONGWEBADDRESS + "html/organ.html?orgId="+jtFile.mTaskId+"&view=2&virtual="+jtFile.virtual;
			
			if(!TextUtils.isEmpty(jtFile.virtual)){
				shareOtherUrlString = httpAddress + "html/organ.html?orgId="+jtFile.mTaskId+"&view=2&virtual="+jtFile.virtual;
			}else{
				shareOtherUrlString = httpAddress + "html/organ.html?orgId="+jtFile.mTaskId+"&view=2&virtual="+jtFile.reserved2;
			}
			break;

		case JTFile.TYPE_COMMUNITY://社群
			shareContent1.setImageUrl(jtFile.getmUrl());
			shareContent1.setShareText(jtFile.getmSuffixName());
			shareContent1.setTitle(jtFile.fileName);
			// http://test.online.gintong.com/html/social.html?communityId={{communityId}}&userId={{userId}}
			shareOtherUrlString = httpAddress + "html/social.html?communityId="+jtFile.mTaskId+"&userId="+jtFile.virtual;	
			break;
		}
		final MShareContent shareContent = shareContent1;

		View view = View.inflate(context, R.layout.activity_share_to_apps, null);
		ImageView logo_qq = (ImageView) view.findViewById(R.id.logo_qq);
		ImageView logo_wechat = (ImageView) view.findViewById(R.id.logo_wechat);
		ImageView logo_sinaweibo = (ImageView) view.findViewById(R.id.logo_sinaweibo);
		ImageView logo_wechatmoments = (ImageView) view.findViewById(R.id.logo_wechatmoments);
		ImageView shareOther = (ImageView) view.findViewById(R.id.share_other);
		// ImageView frame_qr_code = (ImageView)
		// view.findViewById(R.id.frame_qr_code);
		// ImageView frame_app_avatar = (ImageView)
		// view.findViewById(R.id.frame_app_avatar);
		LinearLayout logo_friends = (LinearLayout) view.findViewById(R.id.logo_friends);
		if(jtFile.getmType()==JTFile.TYPE_COMMUNITY)//社群分享暂时不要动态分享
			logo_friends.setVisibility(View.GONE);
		LinearLayout logo_sociality = (LinearLayout) view.findViewById(R.id.logo_sociality);//分享到社交
		TextView cancel = (TextView) view.findViewById(R.id.cancel);

		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.PupwindowAnimation);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		
	
		logo_qq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*showLoadingDialog(context);*/
				ShareUtils.share_QQFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						handler.sendEmptyMessage(1);
						dismissLoadingDialog();
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();

					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, shareContent.getTitle(), shareOtherUrlString/*shareContent.getTitleUrl()*/,
				shareContent.getShareText(), shareContent.getImageUrl());
				popupWindow.dismiss();
			}
		});
		logo_wechat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*showLoadingDialog(context);*/

				ShareUtils.share_WxFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						Log.e("微信分享", arg2.toString());
						if (arg2 instanceof WechatClientNotExistException) {
							handler.sendEmptyMessage(2);
						} else {
							handler.sendEmptyMessage(1);
						}
						dismissLoadingDialog();
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, shareContent.getTitle(), shareOtherUrlString/*shareContent.getTitleUrl()*/
				, shareContent.getShareText(), shareContent.getImageUrl());
				popupWindow.dismiss();
			}

		});
		logo_sinaweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*showLoadingDialog(context);*/
				ShareUtils.share_SinaWeibo(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						dismissLoadingDialog();
						arg2.printStackTrace();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, shareContent.getTitle(),shareOtherUrlString
				/*shareContent.getTitleUrl()*/, shareContent.getShareText(), shareContent.getImageUrl());
				popupWindow.dismiss();
			}
		});
		logo_wechatmoments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*showLoadingDialog(context);*/
				ShareUtils.share_CircleFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
						Toast.makeText(App.getApplicationConxt(), "分享成功", 0).show();
						handler.sendEmptyMessage(3);
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, shareContent.getTitle(), shareOtherUrlString
				/*shareContent.getTitleUrl()*/, shareContent.getShareText(), shareContent.getImageUrl());
				popupWindow.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		/** 转给好友 */
		logo_friends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ENavigate.startCreateFlowActivity(context, jtFile, App.getUserID());

				/** 将转发的内容转成转发动态对象 */
				// ENavigate.startForwardToFriendActivity(context, );
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		/** 转到社交 */
		logo_sociality.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (jtFile.mType == JTFile.TYPE_DEMAND) { // 如果是15 新版本需求 就需要将
					jtFile.mType = 11;
				}else if(jtFile.mType == 11) {
					jtFile.mType = JTFile.TYPE_KNOWLEDGE2;
				}
				ENavigate.startSocialShareActivity(context, jtFile);
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		
		/**
		 * 其他方式
		 */
		shareOther.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Intent.ACTION_SEND); 
				intent.setType("text/plain"); 
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); 
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				CharSequence title="分享";
				context.startActivity(Intent.createChooser(intent,title)); 
			}
		});
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	/** @param context */
	public static void showSharePopupWindowKnow(final Context context, final Knowledge2 knowledge2) {
		/** 点击头像后，弹出popupwindow */

		View view = View.inflate(context, R.layout.activity_share_to_apps, null);
		ImageView logo_qq = (ImageView) view.findViewById(R.id.logo_qq);
		ImageView logo_wechat = (ImageView) view.findViewById(R.id.logo_wechat);
		ImageView logo_sinaweibo = (ImageView) view.findViewById(R.id.logo_sinaweibo);
		ImageView logo_wechatmoments = (ImageView) view.findViewById(R.id.logo_wechatmoments);
		// ImageView frame_qr_code = (ImageView)
		// view.findViewById(R.id.frame_qr_code);
		// ImageView frame_app_avatar = (ImageView)
		// view.findViewById(R.id.frame_app_avatar);
		LinearLayout logo_friends = (LinearLayout) view.findViewById(R.id.logo_friends);
		LinearLayout logo_sociality = (LinearLayout) view.findViewById(R.id.logo_sociality);
		TextView cancel = (TextView) view.findViewById(R.id.cancel);

		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.PupwindowAnimation);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		logo_qq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);
				ShareUtils.share_QQFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();

					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
				popupWindow.dismiss();
			}
		});
		logo_wechat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);

				ShareUtils.share_WxFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						Message msg = new Message();
						handler.sendEmptyMessage(1);
						dismissLoadingDialog();
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
				popupWindow.dismiss();
			}

		});
		logo_sinaweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);
				ShareUtils.share_SinaWeibo(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
						
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
				popupWindow.dismiss();
			}
		});
		logo_wechatmoments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);
				ShareUtils.share_CircleFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(3);
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, ShareConfig.share_title, ShareConfig.share_title_url, ShareConfig.share_text, ShareConfig.image_url);
				popupWindow.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		logo_friends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** 将转发的内容转成转发动态对象 */
				ForwardDynamicNews forwardDynamicNews = new ForwardDynamicNews();

				forwardDynamicNews.content = knowledge2.getDesc();
				forwardDynamicNews.createrId = App.getUserID();
				forwardDynamicNews.imgPath = knowledge2.getPic();
				forwardDynamicNews.targetId = knowledge2.getId() + "";
				forwardDynamicNews.title = knowledge2.getTitle();
				forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_KNOWLEDGE + "";
				forwardDynamicNews.lowType = knowledge2.getType() + "";
				ENavigate.startForwardToFriendActivity(context, forwardDynamicNews);
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		logo_sociality.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				knowledge2.toJTFile();
				JTFile jTfile = knowledge2.toJTFile();
				ENavigate.startSocialShareActivity(context, jTfile);
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	private static EProgressDialog mProgressDialog;

//	public static void showLoadingDialog(Context context) {
//		showLoadingDialog(context, "");
//	}
//
//	public static void showLoadingDialog(Context context, String message) {
//		mProgressDialog = new EProgressDialog(context);
//		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialogInterface) {
//				// onLoadingDialogCancel();//如果取消对话框， 结束当前activity
//			}
//		});
//		mProgressDialog.setMessage(message);
//		mProgressDialog.show();
//	}

	public static void dismissLoadingDialog() {
		try {
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		} catch (Exception e) {

		}
	}
	private static String mName=App.getNick();
	private static String mShare_text=ShareConfig.share_text;
	private static String mUserAvatar=App.getUserAvatar();
	private static void doShare(final Context context, String share_qrcode,final String name,String userAvatar,String communityId ){

		View view = View.inflate(context, R.layout.activity_share_to_apps, null);
		LinearLayout social_ll = (LinearLayout) view.findViewById(R.id.social_ll);
		social_ll.setVisibility(View.GONE);
		ImageView logo_qq = (ImageView) view.findViewById(R.id.logo_qq);
		ImageView logo_wechat = (ImageView) view.findViewById(R.id.logo_wechat);
		ImageView logo_sinaweibo = (ImageView) view.findViewById(R.id.logo_sinaweibo);
		ImageView logo_wechatmoments = (ImageView) view.findViewById(R.id.logo_wechatmoments);
		ImageView shareOther = (ImageView) view.findViewById(R.id.share_other);
		// ImageView frame_qr_code = (ImageView)
		// view.findViewById(R.id.frame_qr_code);
		// ImageView frame_app_avatar = (ImageView)
		// view.findViewById(R.id.frame_app_avatar);
		LinearLayout logo_friends = (LinearLayout) view.findViewById(R.id.logo_friends);
		LinearLayout logo_sociality = (LinearLayout) view.findViewById(R.id.logo_sociality);
		TextView cancel = (TextView) view.findViewById(R.id.cancel);

		if (EAPIConsts.ENVIRONMENT == 2)
			httpAddress = "http://www.gintong.com//";// beta
		else if (EAPIConsts.ENVIRONMENT == 1)
			httpAddress = "http://test.online.gintong.com/";// SIM
		else {
			httpAddress = EAPIConsts.getTMSUrl();
			int end = httpAddress.lastIndexOf(":");
			httpAddress = httpAddress.substring(0, end) + "//";
		}
		String userId=App.getUserID();
		if(!TextUtils.isEmpty(share_qrcode)){
			if (share_qrcode.contains("/invitation/")){
				String substr = share_qrcode.substring(0, share_qrcode.length() - 1);
				userId = substr.substring(substr.lastIndexOf("/") + 1, substr.length());
			}
		}
		if (!TextUtils.isEmpty(communityId))
			// http://test.online.gintong.com/html/social.html?communityId={{communityId}}&userId={{userId}}
			shareOtherUrlString = httpAddress + "html/social.html?communityId=" + communityId + "&userId=" + userId;
		else
			shareOtherUrlString = httpAddress + "html/person.html?id=" + userId + "&view=1&personType=1";
		if(!TextUtils.isEmpty(name)){
			mName=name;
			mShare_text= mName + "邀请你加入金桐" + ",点击下载";
			
		}
		if(!TextUtils.isEmpty(userAvatar)){
			mUserAvatar=userAvatar;
		}
		logo_qq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);
				ShareUtils.share_QQFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();

					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				}, mName, shareOtherUrlString, mShare_text, mUserAvatar);
				popupWindow.dismiss();
			}
		});
		logo_wechat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);

				ShareUtils.share_WxFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						Message msg = new Message();
						handler.sendEmptyMessage(1);
						dismissLoadingDialog();
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				},  mName, shareOtherUrlString, mShare_text, mUserAvatar);
				popupWindow.dismiss();
			}

		});
		logo_sinaweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);
				ShareUtils.share_SinaWeibo(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
						
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				},  mName, shareOtherUrlString, mShare_text, mUserAvatar);
				popupWindow.dismiss();
			}
		});
		logo_wechatmoments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				showLoadingDialog(context);
				ShareUtils.share_CircleFriend(context, new PlatformActionListener() {

					@Override
					public void onError(Platform arg0, int arg1, Throwable arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(1);
					}

					@Override
					public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
						dismissLoadingDialog();
						handler.sendEmptyMessage(3);
					}

					@Override
					public void onCancel(Platform arg0, int arg1) {
						dismissLoadingDialog();
					}
				},  mName, shareOtherUrlString, mShare_text, mUserAvatar);
				popupWindow.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.PupwindowAnimation);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	
	}
	public static void showSharePopupWindowKnow(final Context context, String share_qrcode) {
		doShare(context,share_qrcode,null,null,null);
	}
	public static void showSharePopupWindowKnow(final Context context, String share_qrcode,final String name,String userAvatar ,String communityId) {
		doShare(context,share_qrcode,name,userAvatar,communityId);
	}

}
