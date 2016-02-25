/** ShareConfig.java Car273
 * 
 * @todo 分享模块的配置文件 Created by 方鹏程 on 2014年4月15日 Copyright (c) 1998-2014 273.cn.
 *       All rights reserved. */

package com.tr.ui.home;

import com.tr.App;

/** @author sunjianan */
public class ShareConfig {
	
	public static final String share_title = "金桐下载链接";
	public static final String share_url = "http://static.gintong.com/resources/appweb/index.html";
	public static final String share_title_url = "http://static.gintong.com/resources/appweb/index.html";
	public static final String share_text = App.getNick() + "邀请你加入金桐" + ",点击下载";//App：http://static.gintong.com/resources/appweb/index.html";
	public static final String image_url = "https://open.weixin.qq.com/cgi-bin/openproxy?url=http%3A%2F%2Fmmbiz.qpic.cn%2Fmmbiz%2Fds22MvLknnibISiavcwLJ9oEMM2s7JbGUrR81moHn1FT38AuouUJkW9lxeP7b8ZtP3MUqkTQSgIpdCnGftaj2V6A%2F0";

	public static final String share_title2 = "金桐网";
	public static final String share_title_url2 = App.getNick() + "\n" + App.getUser().getmMobile();
	public static final String share_text2 = App.getNick() + "\n" + App.getUser().getmMobile();
	public static final String image_url2 =  App.getUser().getImage();

	/** 此字段是在ShareSDK注册的应用所对应的appkey */
	public static final String APPKEY = "568af22841aa";

	/** QQ好友 */
	public static final String APPID_QQFRIEND = "1104162404";
	public static final String APPKEY_QQFRIEND = "5FTo2V15iyf623Il";
	public static final String BYPASSAPPROVAL_QQFRIEND = "false";
	public static final String ENABLE_QQFRIEND = "true";

	/** 短信 */

	/** 新浪微博 */
	public static final String APPKEY_SINA_WEIBO = "1657433603";
	public static final String APPSECRET_SINA_WEIBO = "a9ada417e19cbdd45b5efbeb96b8bd7e";
//	public static final String REDIRECTURL_SINA_WEIBO = "https://itunes.apple.com/cn/app/jin-tong-wang/id923820059";
	public static final String REDIRECTURL_SINA_WEIBO = "http://sns.whalecloud.com/sina2/callback";//和网上填写保持一致
	public static final String BYPASSAPPROVAL_SINA_WEIBO = "false";
	public static final String ENABLE_SINA_WEIBO = "true";
	public static final String SHAREBYAPPCLIENT_SINA_WEIBO = "true";

	/** 朋友圈 */
	public static final String APPID_CIRCLE_FRIEND = "wx465662e14a9d8e59";
	public static final String APPSECRET_CIRCLE_FRIEND = "a7ed8ae6a5bd05d7a8eeb5e2aeaafdc2";
	public static final String BYPASSAPPROVAL_CIRCLE_FRIEND = "false";
	public static final String ENABLE_CIRCLE_FRIEND = "true";

	/** 微信好友 */
	public static final String APPID_WXFRIEND = "wx465662e14a9d8e59";
	public static final String APPSECRET_WXFRIEND = "a7ed8ae6a5bd05d7a8eeb5e2aeaafdc2";
	public static final String BYPASSAPPROVAL_WXFRIEND = "false";
	public static final String ENABLE_WXFRIEND = "true";

	/** QQ空间 */
	public static final String APPID_QZONE = "101062407";
	public static final String APPKEY_QZONE = "eb03fb2e38002740c564b64bc656c357";
	public static final String BYPASSAPPROVAL_QZONE = "false";
	public static final String SHAREBYAPPCLIENT_QZONE = "true";
	public static final String ENABLE_QZONE = "true";

}
