package com.tr.ui.home;

import java.util.HashMap;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/** @author sunjianan */
public class ShareUtils {

	/** 分享到朋友圈 */
	public static void share_CircleFriend(Context context, PlatformActionListener listener, String share_title, String share_title_url, String share_text, String image_url) {
		Platform circle = ShareSDK.getPlatform(context, WechatMoments.NAME);
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		map.put("AppId", ShareConfig.APPID_CIRCLE_FRIEND);
		map.put("AppSecret", ShareConfig.APPSECRET_CIRCLE_FRIEND);
		map.put("Enable", ShareConfig.ENABLE_CIRCLE_FRIEND);
		map.put("BypassApproval", ShareConfig.BYPASSAPPROVAL_CIRCLE_FRIEND);

		ShareSDK.initSDK(context, ShareConfig.APPKEY);
		ShareSDK.setPlatformDevInfo(WechatMoments.NAME, map);

		cn.sharesdk.wechat.moments.WechatMoments.ShareParams sp = new cn.sharesdk.wechat.moments.WechatMoments.ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性

		sp.setTitle(share_title);
		sp.setUrl(share_title_url);
		sp.setText(share_text);
		sp.setImageUrl(image_url);

		circle.setPlatformActionListener(listener); // 设置分享事件回调
		// 执行图文分享
		circle.share(sp);
	}

	/** 分享到微信好友 */
	public static void share_WxFriend(Context context, PlatformActionListener listener, String share_title, String share_title_url, String share_text, String image_url) {
		
		/*HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("AppId", ShareConfig.APPID_WXFRIEND);
		map.put("AppSecret", ShareConfig.APPSECRET_WXFRIEND);
		map.put("Enable", ShareConfig.ENABLE_WXFRIEND);
		map.put("BypassApproval", ShareConfig.BYPASSAPPROVAL_WXFRIEND);

		ShareSDK.setPlatformDevInfo(Wechat.NAME, map);
		ShareSDK.initSDK(context, ShareConfig.APPKEY);*/

		ShareParams sp = new ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性

		sp.setTitle(share_title);
		sp.setUrl(share_title_url);
		sp.setText(share_text);
		sp.setImageUrl(image_url);
		Platform circle = ShareSDK.getPlatform(context, Wechat.NAME);
		circle.setPlatformActionListener(listener); // 设置分享事件回调
		// 执行图文分享
		circle.share(sp);
	}

	/** 分享到QQ好友 */
	public static void share_QQFriend(Context context, PlatformActionListener listener, String share_title, String share_title_url, String share_text, String image_url) {
		try {
			Platform circle = ShareSDK.getPlatform(context, QQ.NAME);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("AppId", ShareConfig.APPID_QQFRIEND);
			map.put("AppKey", ShareConfig.APPKEY_QQFRIEND);
			map.put("Enable", ShareConfig.ENABLE_QQFRIEND);

			ShareSDK.initSDK(context, ShareConfig.APPKEY);//initSDK需要在任何操作之前被调用
			ShareSDK.setPlatformDevInfo(QQ.NAME, map);

			cn.sharesdk.tencent.qq.QQ.ShareParams sp = new cn.sharesdk.tencent.qq.QQ.ShareParams();
			
			sp.setTitle(share_title);
			sp.setTitleUrl(share_title_url);
			sp.setText(share_text);
			sp.setImageUrl(image_url);

			circle.setPlatformActionListener(listener); // 设置分享事件回调
			// 执行图文分享
			circle.share(sp);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, "错误"+e.toString(), 0).show();
		}
		
	}

	/** 分享到新浪微博 */
	public static void share_SinaWeibo(Context context, PlatformActionListener listener, String share_title, String share_title_url, String share_text, String image_url) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("AppKey", ShareConfig.APPKEY_SINA_WEIBO);
		map.put("AppSecret", ShareConfig.APPSECRET_SINA_WEIBO);
		map.put("RedirectUrl", ShareConfig.REDIRECTURL_SINA_WEIBO);
		map.put("ShareByAppClient", ShareConfig.SHAREBYAPPCLIENT_SINA_WEIBO);
		map.put("Enable", ShareConfig.ENABLE_SINA_WEIBO);
		ShareSDK.initSDK(context, ShareConfig.APPKEY);
		ShareSDK.setPlatformDevInfo(SinaWeibo.NAME, map);

		cn.sharesdk.sina.weibo.SinaWeibo.ShareParams sp = new cn.sharesdk.sina.weibo.SinaWeibo.ShareParams();
		
		sp.setTitle(share_title);
//		sp.setTitleUrl(share_title_url);
		sp.setText(share_title + " \n " + share_text + share_title_url);//新浪分享连接写在text里
		sp.setImageUrl(image_url);
		if (!TextUtils.isEmpty(image_url)) {//imageUrl不为空 这个时候 要想分享出去 需要干一件事情 就是把imagePath 置为空 
			sp.setImagePath("");
		}
		Platform weibo = ShareSDK.getPlatform(context, SinaWeibo.NAME);
		weibo.setPlatformActionListener(listener); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp);
	}

	/** 分享到QQ空间 */
	public static void share_Qzone(Context context, PlatformActionListener listener, String share_title, String share_title_url, String share_text, String image_url) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("AppId", ShareConfig.APPID_QZONE);
		map.put("AppKey", ShareConfig.APPKEY_QZONE);
		map.put("ShareByAppClient", ShareConfig.SHAREBYAPPCLIENT_QZONE);
		map.put("Enable", ShareConfig.ENABLE_QZONE);
		map.put("BypassApproval", ShareConfig.BYPASSAPPROVAL_QZONE);

		ShareSDK.initSDK(context, ShareConfig.APPKEY);
		ShareSDK.setPlatformDevInfo(QZone.NAME, map);

		cn.sharesdk.tencent.qzone.QZone.ShareParams sp = new cn.sharesdk.tencent.qzone.QZone.ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
		
		sp.setTitle(share_title);
		sp.setTitleUrl(share_title_url);
		sp.setText(share_text);
		sp.setImageUrl(image_url);

		Platform qzone = ShareSDK.getPlatform(context, QZone.NAME);
		qzone.setPlatformActionListener(listener); // 设置分享事件回调
		// 执行图文分享
		qzone.share(sp);
	}
}
