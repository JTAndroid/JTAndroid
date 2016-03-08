package com.utils.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tr.App;
import com.tr.R;
import com.tr.db.ConnectionsDBManager;
import com.tr.db.DBHelper;
import com.tr.db.DownloadFileDBManager;
import com.tr.model.obj.JTFile;
import com.tr.ui.common.RecordingActivity;
import com.tr.ui.relationship.MyKnowledgeSelecteActivity;
import com.tr.ui.relationship.MyRequirementSelectActivity;
import com.tr.ui.widgets.CommonSmileyParser;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyParser2;
import com.utils.log.KeelLog;
import com.utils.log.ToastUtil;
import com.utils.picture.PictureManageUtil;
import com.utils.string.StringUtils;

/** @author archko */
public class EUtil {

	private final static String TAG = EUtil.class.getSimpleName();
	private static PopupWindow popupWindow;
	/* 图片文件后缀 */
	public static final String PIC_FILE_STR = ".JPG/.PNG/.BMP/.JPEG/.GIF";
	/* 视频文件后缀 */
	public static final String VIDEO_FILE_STR = ".AVI/.MPEG/.MPG/.QT/.RAM/.VIV/.AVI/.MP4/.WMV/.RMVB/.MKV/.VOB/.3GP";
	/* Word文档文件后缀 */
	public static final String DOC_FILE_STR = ".DOC/.WPS/.WORD/.DOCX";
	/* 音频文件后缀 */
	public static final String AUDIO_FILE_STR = ".AIF/.SVX/.SND/.MID/.VOC/.WAV/.MP3";

	// 显示消息
	public static void showToast(String text) {
		ToastUtil.showToast(App.getApp(), text) ;
	}

	public static void showToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void showLongToast(String text) {
		Toast.makeText(App.getApp(), text, Toast.LENGTH_LONG).show();
	}

	public static void showLongToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static String getLocalMacAddress(Context context, boolean isWifi) {
		String macAddr = null;
		if (!isWifi) {
			macAddr = getWifiMacAddr(context, macAddr);
		} else {
			// 获取有线网卡的mac地址
			macAddr = getLocalEthernetMacAddress();
		}

		if (null == macAddr) {
			macAddr = getWifiMacAddr(context, macAddr);
		}

		// macAddr="00:92:d1:0b:93:14";
		return macAddr;
	}

	private static String getWifiMacAddr(Context context, String macAddr) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		if (null != info) {
			String addr = info.getMacAddress();
			if (null != addr) {
				KeelLog.d("getWifiMacAddr:" + addr);
				macAddr = addr;
			}
		}
		return macAddr;
	}

	/**
	 * 获取有线mac.同州.不一定适合.
	 * 
	 * @return
	 */
	public static String getLocalEthernetMacAddress() {
		String mac = null;
		// try {
		// Enumeration localEnumeration=NetworkInterface.getNetworkInterfaces();
		//
		// while (localEnumeration.hasMoreElements()) {
		// NetworkInterface localNetworkInterface=(NetworkInterface)
		// localEnumeration.nextElement();
		// String interfaceName=localNetworkInterface.getDisplayName();
		//
		// if (interfaceName==null) {
		// continue;
		// }
		//
		// if (interfaceName.equals("eth0")) {
		// // MACAddr = convertMac(localNetworkInterface
		// // .getHardwareAddress());
		// mac=convertToMac(localNetworkInterface.getHardwareAddress());
		// if (mac!=null&&mac.startsWith("0:")) {
		// mac="0"+mac;
		// }
		// break;
		// }
		//
		// // byte[] address =
		// // localNetworkInterface.getHardwareAddress();
		// // Log.i(TAG, "mac=" + address.toString());
		// // for (int i = 0; (address != null && i < address.length);
		// // i++)
		// // {
		// // Log.i("Debug", String.format("  : %x", address[i]));
		// // }
		// }
		// } catch (SocketException e) {
		// e.printStackTrace();
		// }
		return mac;
	}

	private static String convertToMac(byte[] mac) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			byte b = mac[i];
			int value = 0;
			if (b >= 0 && b <= 16) {
				value = b;
				sb.append("0" + Integer.toHexString(value));
			} else if (b > 16) {
				value = b;
				sb.append(Integer.toHexString(value));
			} else {
				value = 256 + b;
				sb.append(Integer.toHexString(value));
			}
			if (i != mac.length - 1) {
				sb.append(":");
			}
		}
		return sb.toString();
	}

	/**
	 * 获取有线的mac地址,测试无效.
	 * 
	 * @return
	 * @throws SocketException
	 */
	public static String getEthMacAddress() {
		String ipaddress = null;
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				if (intf.getName().toLowerCase().equals("eth0")
						|| intf.getName().toLowerCase().equals("wlan0")) {
					for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							ipaddress = inetAddress.getHostAddress().toString();
							if (!ipaddress.contains("::")) {// ipV6的地址
								KeelLog.d("getEthMacAddress,ipV6:" + ipaddress); // 结果是ip
								return ipaddress;
							}
						}
					}
				} else {
					continue;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		KeelLog.d("getEthMacAddress:" + ipaddress);
		return ipaddress;
	}

	public String getEth0MacAddress() {
		String strMacAddr = null;
		/*
		 * try { InetAddress ip=getLocalInetAddress(); byte[]
		 * b=NetworkInterface.getByInetAddress(ip).getHardwareAddress();
		 * StringBuffer buffer=new StringBuffer(); for (int i=0; i<b.length;
		 * i++) { if (i!=0) { buffer.append(':'); } String
		 * str=Integer.toHexString(b&0xFF); buffer.append(str.length()==1 ?
		 * 0+str : str); } strMacAddr=buffer.toString().toUpperCase(); } catch
		 * (Exception e) { e.printStackTrace(); }
		 */

		return strMacAddr;
	}

	/**
	 * 啟動第三方應用
	 * 
	 * @param packName
	 */
	public static boolean startAppByPackageName(Context c, String packName) {

		List<ResolveInfo> mAllApps;
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager mPackageManager = c.getPackageManager();

		mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
		for (ResolveInfo res : mAllApps) {
			// 该应用的包名和主Activity
			String pkg = res.activityInfo.packageName;
			String cls = res.activityInfo.name;
			KeelLog.d("startAppByPackageName", "pkg:" + pkg + "===cls==" + cls);
			if (pkg.compareTo(packName) == 0) {
				KeelLog.d("startAppByPackageName", "start pkg:" + pkg
						+ "===cls==" + cls);
				ComponentName componet = new ComponentName(pkg, cls);

				Intent i = new Intent();
				i.setComponent(componet);
				c.startActivity(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * 将像素转为dp
	 * 
	 * @param px
	 * @return
	 */
	public static int convertPxToDp(int px) {
		WindowManager wm = (WindowManager) App.getApp().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		float logicalDensity = metrics.density;
		int dp = Math.round(px / logicalDensity);
		return dp;
	}

	/**
	 * 将dp转为像素
	 * 
	 * @param dp
	 * @return
	 */
	public static int convertDpToPx(int dp) {
		return Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dp, App.getApp().getResources()
						.getDisplayMetrics()));
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int getRandom() {
		Random ran = new Random(System.currentTimeMillis());
		int ret = ran.nextInt();
		ret = Math.abs(ret);
		return ret;
	}

	// 判断字符串是否纯数字
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	public static boolean isPwd(String str){
		Pattern pattern = Pattern.compile("[A-Za-z0-9_]+");
		Matcher isPwd = pattern.matcher(str);
		if (!isPwd.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 中国电信发布中国3G号码段:中国联通185,186;中国移动188,187;中国电信189,180共6个号段。
	 * 3G业务专属的180-189号段已基本分配给各运营商使用,
	 * 其中180、189分配给中国电信,187、188归中国移动使用,185、186属于新联通。
	 * 中国移动拥有号码段：139、138、137、136、135
	 * 、134、159、158、157（3G）、152、151、150、188（3G）、187（3G）;14个号段
	 * 中国联通拥有号码段：130、131、132、155、156（3G）、186（3G）、185（3G）;6个号段
	 * 中国电信拥有号码段：133、153、189（3G）、180（3G）;4个号码段 移动:
	 * 2G号段(GSM网络)有139,138,137,136,135,134(0-8),159,158,152,151,150
	 * 3G号段(TD-SCDMA网络)有157,188,187 147是移动TD上网卡专用号段. 联通:
	 * 2G号段(GSM网络)有130,131,132,155,156 3G号段(WCDMA网络)有186,185 电信:
	 * 2G号段(CDMA网络)有133,153 3G号段(CDMA网络)有189,180
	 */
	// 是否是正确的手机号码号段
	public static boolean isMobileNO(String regionCode, String mobile) {

		if (StringUtils.isEmpty(regionCode) || regionCode.startsWith("+86")) {
			// Pattern p =
			// Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
			Pattern p = Pattern.compile("^\\d{11}$");
			Matcher m = p.matcher(mobile);
			return m.matches();
			// return true;
		} else {
			if (regionCode.startsWith("+") && !mobile.contains("@")) {
				Pattern p = Pattern.compile("^\\d+$");
				Matcher m = p.matcher(mobile);
				return m.matches();
			}
			return false;
		}

		/*
		 * if (mobile != null && EUtil.isNumeric(mobile) && mobile.length() ==
		 * 11) { return true; } else { return false; }
		 */
	}

	// 改正则表达式仅能验证大陆手机号
	public static boolean isMobileNO(String mobile) {

		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobile);
		return m.matches();

		/*
		 * if (mobile != null && EUtil.isNumeric(mobile) && mobile.length() ==
		 * 11) { return true; } else { return false; }
		 */
	}

	/**
	 * 是否是手机号格式（11位全数字）
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobileNOFormat(String mobile) {

		if (mobile != null && mobile.length() > 0 && isNumeric(mobile)) {
			return true;
		} else {
			return false;
		}
	}

	// 判断email格式是否正确
	public static boolean isEmail(String email) {

		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();

		/*
		 * if (email != null && email.contains("@")) { return true; } else {
		 * return false; }
		 */
	}

	// 获取来源照片提示
	public static AlertDialog getPhotoSelectionDialog(Context context,
			DialogInterface.OnClickListener listener) {

		return new AlertDialog.Builder(context).setItems(
				context.getResources().getStringArray(R.array.select_photo),
				listener).create();
	}

	// 将Uri转换为文件路径
	public static String uri2Path(ContentResolver resolver, Uri uri) {
		// 两种方法获取文件路径
		String img_path = "";
		try {
			int actual_image_column_index;
			String[] proj = { MediaStore.Video.Media.DATA };
			Cursor cursor = resolver.query(uri, proj, null, null, null);
			actual_image_column_index = cursor.getColumnIndexOrThrow(proj[0]);
			cursor.moveToFirst();
			img_path = cursor.getString(actual_image_column_index);
			Log.d("", img_path);
		} catch (Exception e) {
			img_path = uri.getPath();
		} finally {
			if (img_path == null) {
				img_path = "";
			}
		}
		return img_path;
	}

	// 用户名是否符合规范
	public static boolean isUsernameFormatCorrect(Context context,
			String username) {
		if (username == null || username.length() == 0) {
			Toast.makeText(context, "账号不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	// 密码是否符合规范
	public static boolean isPasswordFormatCorrect(Context context,
			String password) {
		if (password == null || password.length() == 0) {
			Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (password.length() < 6 || password.length() > 20) {
			Toast.makeText(context, "请输入6-20位密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	// 手机号是否符合规范
	public static boolean isMobileFormatCorrect(Context context,
			String regionCode, String mobile) {
		if (mobile == null || mobile.length() == 0) {
			Toast.makeText(context, "手机号码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (mobile.length() != 11) {
			Toast.makeText(context, "手机号格式有误,请输入11位大陆手机号", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (!EUtil.isNumeric(mobile) || EUtil.isMobileNO(regionCode, mobile)) {
			Toast.makeText(context, "手机号格式有误,请输入11位大陆手机号", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	// 邮箱是否符合规范
	public static boolean isEmailFormatCorrect(Context context, String email) {
		if (email == null || email.length() == 0) {
			Toast.makeText(context, "邮箱地址不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (EUtil.isEmail(email)) {
			Toast.makeText(context, "邮箱格式有误,请重新输入", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	// 设备是否有SD卡
	public static boolean isSDCardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	// 图片来源获取提示
	public static void showPhotoSelectDialog(Context context,
			DialogInterface.OnClickListener listener) {

		new AlertDialog.Builder(context)
				.setItems(
						context.getResources().getStringArray(
								R.array.select_photo), listener).create()
				.show();
	}

	// 视频来源获取提示
	public static void showVideoSelectDialog(Context context,
			DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(context)
				.setItems(
						context.getResources().getStringArray(
								R.array.select_video), listener).create()
				.show();
	}

	// 发送验证码提示
	public static void showSendVerifyCodeDialog(Context context, String mobile,
			DialogInterface.OnClickListener listener) {
		new AlertDialog.Builder(context).setTitle("确认手机号码")
				.setMessage("我们将发送验证码到这个号码：" + mobile)
				.setPositiveButton("确定", listener)
				.setNegativeButton("取消", null).create().show();
	}

	// 发送调用相机的Intent
	public static void dispatchTakePictureIntent(Activity activity,
			Uri fileUri, int requestCode) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		activity.startActivityForResult(takePictureIntent, requestCode);
	}

	/**
	 * 发送调用相机的Intent
	 * 
	 * @param activity
	 * @param fileUri
	 */
	public static void dispatchTakePictureIntent(Activity activity, Uri fileUri) {

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		activity.startActivityForResult(takePictureIntent,
				EConsts.REQ_CODE_TAKE_PICTURE);
	}

	// 发送裁切图片的Intent（不限制宽高比和输出尺寸）
	public static void dispatchCropPictureIntent(Activity activity,
			Uri fileUri, int requestCode) {

		Intent cropPictureIntent = new Intent("com.android.camera.action.CROP");
		cropPictureIntent.setDataAndType(fileUri, "image/*");
		cropPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		cropPictureIntent.putExtra("crop", true);
		cropPictureIntent.putExtra("noFaceDetection", true);
		cropPictureIntent.putExtra("return-data", true); // 是否返回数据
		activity.startActivityForResult(cropPictureIntent, requestCode);
	}

	// 发送裁切图片的Intent（1:1）
	public static void dispatchCropPictureIntent(Activity activity,
			Uri fileUri, int requestCode, int picSize) {

		Intent cropPictureIntent = new Intent("com.android.camera.action.CROP")
				.setDataAndType(fileUri, "image/*")
				.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
				.putExtra("crop", true)
				.putExtra("aspectX", 1)
				// 图片比例1:1
				.putExtra("aspectY", 1)
				// .putExtra("outputX", 200)
				// .putExtra("outputY", 200)
				// .putExtra("scale", false) //设置是否允许拉伸
				.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
				// 输出jpg格式文件
				.putExtra("noFaceDetection", true)
				.putExtra("return-data", false); // 是否返回数据（小米手机不支持）
		activity.startActivityForResult(cropPictureIntent, requestCode);
	}

	// 发送裁切图片的Intent（1:1）
	public static void dispatchCropPictureIntent(Activity activity,
			Uri sourceUri, Uri outputUri, int requestCode) {

		Intent cropPictureIntent = new Intent("com.android.camera.action.CROP")
				.setDataAndType(sourceUri, "image/*")
				.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
				.putExtra("crop", true)
				.putExtra("aspectX", 1)
				.putExtra("aspectY", 1)
				.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
				.putExtra("noFaceDetection", true)
				.putExtra("return-data", false); // 是否返回数据（小米手机不支持）
		activity.startActivityForResult(cropPictureIntent, requestCode);
	}

	// 发送裁切图片的Intent（1:1）
	public static void dispatchCropSquarePictureIntent(Activity activity,
			Uri fileUri, int requestCode, int picSize) {

		Intent cropPictureIntent = new Intent("com.android.camera.action.CROP")
				.setDataAndType(fileUri, "image/*")
				.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
				.putExtra("crop", true)
				.putExtra("aspectX", 1)
				// 图片比例1:1
				.putExtra("aspectY", 1)
				// .putExtra("outputX", picSize)
				// .putExtra("outputY", picSize)
				// .putExtra("outputFormat",
				// Bitmap.CompressFormat.JPEG.toString())
				// .putExtra("scaleUpIfNeeded", false)
				// .putExtra("scale", false)
				.putExtra("noFaceDetection", true)
				.putExtra("return-data", true); // 是否返回数据
		activity.startActivityForResult(cropPictureIntent, requestCode);
	}

	/*
	 * // 发送裁切图片的Intent（长宽比1:1） public static void
	 * dispatchCropPictureIntent(Activity activity, Uri fileUri, int
	 * requestCode, int picWidth,int picHeight) { Intent cropPictureIntent = new
	 * Intent("com.android.camera.action.CROP");
	 * cropPictureIntent.setDataAndType(fileUri, "image/*");
	 * cropPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	 * cropPictureIntent.putExtra("crop", true);
	 * cropPictureIntent.putExtra("aspectX", ratio); // 图片比例1:1
	 * cropPictureIntent.putExtra("aspectY", 1);
	 * cropPictureIntent.putExtra("outputX", picWidth);
	 * cropPictureIntent.putExtra("outputY", picHeight);
	 * cropPictureIntent.putExtra("noFaceDetection", true);
	 * cropPictureIntent.putExtra("return-data", true); // 是否返回数据
	 * activity.startActivityForResult(cropPictureIntent, requestCode); }
	 */

	// 发送选择图片的Intent
	public static void dispatchPickPictureIntent(Activity activity,
			int requestCode) {

		Intent pickPictureIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		Intent intentWrapper = Intent.createChooser(pickPictureIntent, null);
		activity.startActivityForResult(intentWrapper, requestCode);
	}

	/** 发送选择图片的Intent */
	public static void dispatchPickPictureIntent(Activity activity) {
		Intent pickPictureIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		Intent intentWrapper = Intent.createChooser(pickPictureIntent, null);
		activity.startActivityForResult(intentWrapper,
				EConsts.REQ_CODE_PICK_PICTURE);
	}

	// 发送选择知识的Intent
	public static void dispatchShareKnowledgeIntent(Activity activity) {
		Intent intent = new Intent(activity, MyKnowledgeSelecteActivity.class);
		activity.startActivityForResult(intent,
				EConsts.ReqCode.SelectFromMyKnowledge);
	}

	// 发送选择需求的Intent TODO
	public static void dispatchShareRequirementIntent(Activity activity) {
		Intent intent = new Intent(activity, MyRequirementSelectActivity.class);
		intent.putExtra(EConsts.Key.ID, App.getUser().mID);
		intent.putExtra(EConsts.Key.RETURN_DATA, true);
		activity.startActivityForResult(intent,
				EConsts.ReqCode.SelectFromMyRequirement);
	}

	// 缩放Bitmap图片
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	// 保存图片
	public static void saveBitmapToFile(Bitmap bitmap, File file) {

		try {
			BufferedOutputStream bStream = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(CompressFormat.JPEG, 100, bStream);
			bStream.flush();
			bStream.close();
			bitmap.recycle();
		} catch (IOException e) {
			// Log.d(TAG, e.toString());
		}
	}

	// 拷贝文件（主要是相册中的文件）
	public static void copyFile(File desFile, ContentResolver resolver,
			Uri srcUri) {

		// 使用ContentProvider通过URI获取原始图片
		try {
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, srcUri);
			EUtil.saveBitmapToFile(bitmap, desFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.d("EUtil", e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("EUtil", e.getMessage());
		}
	}

	/**
	 * 发送拍摄视频的Intent
	 * 
	 * @param activity
	 * @param requestCode
	 */
	public static void dispatchPickVideoIntent(Activity activity,
			int requestCode) {
		// 视频
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		innerIntent.setType("video/*"); // 选择视频（mp4 3gp 是android支持的视频格式）
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		activity.startActivityForResult(wrapperIntent, requestCode);
	}

	/**
	 * 发送拍摄视频的Intent
	 * 
	 * @param activity
	 * @param requestCode
	 */
	public static void dispatchPickVideoIntent(Activity activity) {
		// 视频
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		innerIntent.setType("video/*"); // 选择视频（mp4 3gp 是android支持的视频格式）
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		activity.startActivityForResult(wrapperIntent,
				EConsts.REQ_CODE_PICK_VIDEO);
	}

	/**
	 * 拍摄视频（默认参数）
	 * 
	 * @param activity
	 * @param fileUri
	 * @param requestCode
	 */
	public static void dispatchTakeVideoIntent(Activity activity, Uri fileUri,
			int requestCode) {
		// 拍摄视频
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 设置视频录制的质量，0为低质量，1为高质量。
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 拍摄视频（默认参数）
	 * 
	 * @param activity
	 * @param fileUri
	 */
	public static void dispatchTakeVideoIntent(Activity activity, Uri fileUri) {
		// 拍摄视频
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 设置视频录制的质量，0为低质量，1为高质量。
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		activity.startActivityForResult(intent, EConsts.REQ_CODE_TAKE_VIDEO);
	}

	/**
	 * 拍摄视频
	 * 
	 * @param activity
	 * @param quality
	 * @param sizeLimit
	 * @param durationLimit
	 * @param fileUri
	 * @param requestCode
	 */
	public static void dispatchTakeVideoIntent(Activity activity, int quality,
			long sizeLimit, long durationLimit, Uri fileUri, int requestCode) {

		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality); // 设置视频录制的质量，0为低质量，1为高质量。
		intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sizeLimit); // 指定视频最大允许的尺寸，单位为byte。
		intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit); // 设置视频最大允许录制的时长，单位为毫秒。
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 选择文件
	 * 
	 * @param activity
	 * @param requestCode
	 */
	public static void dispatchPickFileIntent(Activity activity, int requestCode) {

		// <<<<<<< HEAD
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("file/*");
		Intent intentWrapper = Intent.createChooser(intent, null);
		activity.startActivityForResult(intentWrapper, requestCode);
		/*
		 * ======= try{ Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		 * intent.setType("file/*"); Intent intentWrapper =
		 * Intent.createChooser(intent, null);
		 * activity.startActivityForResult(intentWrapper, requestCode); }
		 * catch(ActivityNotFoundException e){ e.printStackTrace(); } >>>>>>>
		 * master20140515
		 */
		/*
		 * Intent intent = new Intent(Intent.ACTION_PICK); File root = new
		 * File(Environment.getExternalStorageDirectory().getAbsolutePath());
		 * Uri uri = Uri.fromFile(root); intent.setData(uri); Intent
		 * intentWrapper = Intent.createChooser(intent, null);
		 * activity.startActivityForResult(intentWrapper, requestCode);
		 */

		/*
		 * if (Build.VERSION.SDK_INT < 19){ Intent intent = new
		 * Intent(Intent.ACTION_GET_CONTENT);
		 * activity.startActivityForResult(intent, requestCode); } <<<<<<< HEAD
		 * else { ======= else { >>>>>>> master20140515 Intent intent = new
		 * Intent(Intent.ACTION_OPEN_DOCUMENT);
		 * intent.addCategory(Intent.CATEGORY_OPENABLE);
		 * activity.startActivityForResult(intent, requestCode); }
		 */
	}

	/**
	 * 选择文件
	 * 
	 * @param activity
	 */
	public static void dispatchPickFileIntent(Activity activity) {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("file/*");
		Intent intentWrapper = Intent.createChooser(intent, null);
		activity.startActivityForResult(intentWrapper,
				EConsts.REQ_CODE_PICK_FILE);
	}

	/**
	 * 获取录音文件（跳转到自定义的页面）
	 * 
	 * @param activity
	 * @param requestCode
	 */
	public static void dispatchGetRecordIntent(Activity activity,
			int requestCode) {
		Intent intent = new Intent(activity, RecordingActivity.class);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 设置ScrollView中的ListView
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 获取手机的IMEI
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceID(Context context) {
		String imei = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		// 处理DeviceId为空的情况
		if (imei == null || imei.length() <= 0) {
			imei = TaskIDMaker.getTaskId();
		}
		return imei;
	}

	/**
	 * 获取程序版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param srcFile
	 * @param desFile
	 * @return
	 */
	public static boolean copyFile(String srcFile, String desFile) {

		try {
			InputStream inputStream = new FileInputStream(srcFile);
			OutputStream outputStream = new FileOutputStream(desFile);
			byte bytes[] = new byte[1024];
			int count;
			while ((count = inputStream.read(bytes)) > 0) {
				outputStream.write(bytes, 0, count);
			}
			inputStream.close();
			outputStream.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 拷贝文件
	 * 
	 * @param srcFile
	 * @param desFile
	 * @return
	 */
	public static boolean copyFile(File srcFile, File desFile) {

		try {
			InputStream inputStream = new FileInputStream(srcFile);
			OutputStream outputStream = new FileOutputStream(desFile);
			byte bytes[] = new byte[1024];
			int count;
			while ((count = inputStream.read(bytes)) > 0) {
				outputStream.write(bytes, 0, count);
			}
			inputStream.close();
			outputStream.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取指定文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtensionName(String filename) {

		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *            图像的路径
	 * @param width
	 *            指定输出图像的宽度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {

		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		// bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
		// ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 保存字符型变量
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setStringToAppSetting(Context context, String key,
			String value) {
		SharedPreferences sp = context.getSharedPreferences(
				EConsts.Key.APP_SETTING, 0);
		sp.edit().putString(key, value).commit();
	}

	/**
	 * 获取字符型变量
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getStringFromAppSetting(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				EConsts.Key.APP_SETTING, 0);
		return sp.getString(key, "");
	}

	/**
	 * 保存整形变量
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setIntToAppSetting(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(
				EConsts.Key.APP_SETTING, 0);
		sp.edit().putInt(key, value).commit();
	}

	/**
	 * 获取整形变量
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static int getIntFromAppSetting(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				EConsts.Key.APP_SETTING, 0);
		return sp.getInt(key, 0);
	}

	/**
	 * 获取整形变量 可设置默认值模式
	 * 
	 * @param context
	 * @param key
	 * @param defaultInt
	 * @return
	 */
	public static int getIntFromAppSetting(Context context, String key,
			int defaultInt) {
		SharedPreferences sp = context.getSharedPreferences(
				EConsts.Key.APP_SETTING, 0);
		return sp.getInt(key, defaultInt);
	}

	/**
	 * 设置布尔型变量
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static void setBooleanToAppSetting(Context context, String key,
			boolean value) {
		SharedPreferences sp = context.getSharedPreferences(
				EConsts.Key.APP_SETTING, 0);
		sp.edit().putBoolean(key, value).commit();
	}

	/**
	 * 获取布尔型变量
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBooleanFromAppSetting(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				EConsts.Key.APP_SETTING, 0);
		return sp.getBoolean(key, false);
	}

	/**
	 * 弹出删除文件提示
	 * 
	 * @param context
	 * @param listener
	 */
	public static void showFileDeleteDialog(Context context,
			DialogInterface.OnClickListener listener) {

		new AlertDialog.Builder(context).setTitle("提示").setMessage("确定要删除该文件？")
				.setPositiveButton("确定", listener)
				.setNegativeButton("取消", null).create().show();
	}

	/**
	 * 弹出终止文件上传提示
	 * 
	 * @param context
	 * @param listener
	 */
	public static void showFileUploadCancelDialog(Context context,
			DialogInterface.OnClickListener listener) {

		new AlertDialog.Builder(context).setTitle("提示")
				.setMessage("您确定要终止上传该文件吗？").setPositiveButton("确定", listener)
				.setNegativeButton("取消", null).create().show();
	}

	// 获取固定格式的时间字符串，目前为 yyyy-MM-dd hh:mm:ss
	public static String getFormatFromDate(Date now) {
		now = new Date(System.currentTimeMillis());
		SimpleDateFormat formatDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// HH为24小时制,
										// hh为12小时制
		// formatDate.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));

		String str = formatDate.format(now);
		return str;
	}

	/**
	 * 生成一个messageID随机数，发送聊天信息时用
	 * 
	 * @return
	 */
	public static String genMessageID() {
		String msgID = null;
		try {
			Date now = new Date();
			String userid = App.getApp().getAppData().getUser().mID + "";
			Random ran = new Random(System.currentTimeMillis());
			msgID = userid + now + ran.nextLong();
			msgID = StringUtils.getMD5Str(msgID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgID;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param fileSize
	 * @return
	 */
	public static String getFileSize(long fileSize) {
		String result = "";
		if (fileSize < 1024) { // B
			result = fileSize + "B";
		} else if (fileSize < 1024 * 1024) { // KB
			result = String.format("%.1f", fileSize * 1.0 / 1024) + "KB";
		} else if (fileSize < 1024 * 1024 * 1024) { // MB
			result = String.format("%.1f", fileSize * 1.0 / 1024 * 1024) + "MB";
		} else {
			result = "未知";
		}
		return result;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = String.format("%.1fB", (double) fileS);
		} else if (fileS < 1048576) {
			// fileSizeString = df.format((double) fileS / 1024) + "K";
			fileSizeString = String.format("%.1fK", (double) fileS / 1024);
		} else if (fileS < 1073741824) {
			// fileSizeString = df.format((double) fileS / 1048576) + "M";
			fileSizeString = String.format("%.1fM", (double) fileS / 1048576);
		} else {
			// fileSizeString = df.format((double) fileS / 1073741824) + "G";
			fileSizeString = String
					.format("%.1fG", (double) fileS / 1073741824);
		}
		return fileSizeString;
	}

	/**
	 * 获取目录文件大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}

	/** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
	public static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}
	
	/**
	 * 递归删除文件夹
	 **/
	public static void deleteDirectory(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteDirectory(childFiles[i]);
			}
			file.delete();
		}
	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context context) {

		String version = "未知";
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo;
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 获取App缓存文件存放路径（根据用户区分）
	 * 
	 * @param context
	 * @return
	 */
	public static File getAppCacheFileDir(Context context) {

		File rootDir = context.getExternalCacheDir(); // 对外可见
		if (rootDir != null) {
			File cacheDir = new File(rootDir, App.getUserID() + "/cache");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			return cacheDir;
		} else {
			return rootDir;
		}
	}

	/**
	 * 获取App文件存放路径（根据用户区分）
	 * 
	 * @param context
	 * @return
	 */
	public static File getAppFileDir(Context context) {

		File rootDir = context.getExternalCacheDir();
		if (rootDir != null) {
			File fileDir = new File(rootDir, App.getUserID() + "/jtfile");

			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			return fileDir;
		} else {
			return rootDir;
		}
	}

	/**
	 * 获取语音聊天缓存文件
	 * 
	 * @param context
	 * @param chatId
	 * @return
	 */

	public static File getChatVoiceCacheDir(Context context, String chatId) {
		File rootDir = getAppCacheFileDir(context);
		if (rootDir != null) {
			File cacheDir = new File(rootDir, "chat/" + chatId);
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			return cacheDir;
		} else {
			return rootDir;
		}
	}

	/**
	 * 获取畅聊图片缓存文件存放路径
	 * 
	 * @param context
	 * @param chatId
	 * @return 文件路径
	 */
	public static File getChatImageCacheDir(Context context, String chatId) {
		if (TextUtils.isEmpty(chatId)) {
			return null;
		}
		File rootDir = context.getExternalFilesDir(null);
		if (rootDir != null) {
			File cacheDir = new File(rootDir, App.getUserID() + "/chat/"
					+ chatId + "/images");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			return cacheDir;
		} else {
			return null;
		}
	}

	/**
	 * 获取畅聊图片缩略图文件存放路径
	 * 
	 * @param context
	 * @param chatId
	 * @param originalImagePath
	 * @return
	 */
	public static String getChatImageThumbnail(Context context, String chatId,
			String originalImagePath) {
		String thumbnailPath = null;
		int rotate = PictureManageUtil.getPhotoOrientation(originalImagePath);
		if (TextUtils.isEmpty(originalImagePath)) {
			return null;
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(originalImagePath, options);
		int height = options.outHeight;
		int width = options.outWidth;
		int reqHeight = 0;
		// 设置为最大宽度为800
		int reqWidth = 800;
		reqHeight = (reqWidth * height) / width;
		// 在内存中创建bitmap对象，这个对象按照缩放大小创建的
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;

		Bitmap bitmap = BitmapFactory.decodeFile(originalImagePath, options);
		if (bitmap == null) {
			return null;
		}
		Bitmap bbb = compressImage(Bitmap.createScaledBitmap(bitmap, reqWidth,
				reqHeight, false));

		if (bbb == null) {
			return null;
		}
		if (rotate == 90 || rotate == 180 || rotate == 270) {
			rotatedBitmap = PictureManageUtil.rotateBitmap(bbb, rotate);
		}
		// 保存文件
		thumbnailPath = saveImageEx(context, rotatedBitmap, genMessageID(),
				getChatImageCacheDir(context, chatId));
		return thumbnailPath;
	}

	/**
	 * 获取畅聊视频截图文件存放路径
	 * 
	 * @param context
	 * @param chatId
	 * @param originalVideoPath
	 * @return
	 */
	public static String getChatVideoScreenshot(Context context, String chatId,
			String originalVideoPath) {
		String screenshotPath = null;
		String extension = EUtil.getExtensionName(originalVideoPath);
		if (isVideoFormatSupport(extension)) {
			MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
			mediaMetadataRetriever.setDataSource(originalVideoPath);
			Bitmap bmpOriginal = mediaMetadataRetriever.getFrameAtTime(0);
			screenshotPath = saveImageEx(context, bmpOriginal, genMessageID(),
					getChatImageCacheDir(context, chatId));
		}
		return screenshotPath;
	}

	/**
	 * 获取畅聊视频缓存文件存放路径
	 * 
	 * @param context
	 * @param chatId
	 * @return 文件路径
	 */
	public static File getChatVideoCacheDir(Context context, String chatId) {
		if (TextUtils.isEmpty(chatId)) {
			return null;
		}
		File rootDir = context.getExternalFilesDir(null);
		if (rootDir != null) {
			File cacheDir = new File(rootDir, App.getUserID() + "/chat/"
					+ chatId + "/videos");
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			return cacheDir;
		} else {
			return null;
		}
	}

	/**
	 * 获取会议畅聊文件缓存地址
	 * 
	 * @param context
	 * @param type
	 *            JTFile
	 * @param meetingId
	 * @param topicId
	 * @return
	 */
	public static File getMeetingChatFileDir(Context context, int type,
			long meetingId, long topicId) {
		// 缓存根目录
		File rootDir = context.getExternalCacheDir();
		if (rootDir == null) {
			return null;
		}
		// 缓存目录
		File dir = null;
		switch (type) {
		case JTFile.TYPE_IMAGE: // 图片
			dir = new File(rootDir, App.getUserID() + "/hy/" + meetingId + "/"
					+ topicId + "/images");
			break;
		case JTFile.TYPE_AUDIO: // 语音
			dir = new File(rootDir, App.getUserID() + "/hy/" + meetingId + "/"
					+ topicId + "/voices");
			break;
		case JTFile.TYPE_VIDEO: // 视频
			dir = new File(rootDir, App.getUserID() + "/hy/" + meetingId + "/"
					+ topicId + "/videos");
			break;
		case JTFile.TYPE_FILE: // 文件
			dir = new File(rootDir, App.getUserID() + "/hy/" + meetingId + "/"
					+ topicId + "/files");
			break;
		default:
			break;
		}
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 获取App缓存文件存放路径
	 * 
	 * @param context
	 * @return
	 */
	/*
	 * public static File getAppCacheDir(Context context) { File rootDir =
	 * context.getExternalCacheDir(); if (rootDir != null) { File cacheDir = new
	 * File(rootDir, "user/" + App.getUserID()); if (!cacheDir.exists()) {
	 * cacheDir.mkdirs(); } return cacheDir; } return rootDir; }
	 */

	/**
	 * 获取App文件存放路径（根据用户区分）
	 * 
	 * @param context
	 * @return
	 */
	public static File getAppFileDirEx(Context context) {

		File rootDir = Environment.getExternalStorageDirectory();
		if (rootDir != null) {
			File fileDir = new File(rootDir, context.getPackageName()
					+ "/user/" + App.getUserID());
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			return fileDir;
		}

		return rootDir;
	}

	/**
	 * 聊天缓存文件记录
	 * 
	 * @param context
	 * @param chatId
	 * @return
	 */
	/*
	 * public static File getAppChatCacheDir(Context context,String chatId, int
	 * type){ File rootDir = EUtil.getAppCacheDir(context); if(rootDir != null){
	 * File chatDir = null; switch(type){ case JTFile.TYPE_IMAGE: chatDir = new
	 * File(rootDir, "chat/" + chatId + "/image"); break; case
	 * JTFile.TYPE_AUDIO: chatDir = new File(rootDir, "chat/" + chatId +
	 * "/audio"); break; case JTFile.TYPE_VIDEO: chatDir = new File(rootDir,
	 * "chat/" + chatId + "/video"); break; default : chatDir = new
	 * File(rootDir, "chat/" + chatId + "/file"); break; }
	 * if(!chatDir.exists()){ chatDir.mkdirs(); } } return rootDir; }
	 */

	/**
	 * 获取指定目录下的所有一级文件
	 * 
	 * @param fileDir
	 * @return
	 */
	public static List<JTFile> getListJTFile(File fileDir) {
		List<JTFile> listJTFile = new ArrayList<JTFile>();
		if (fileDir != null && fileDir.isDirectory()) {
			File[] listFile = fileDir.listFiles();
			if (listFile != null) {
				for (int i = 0; i < listFile.length; i++) {
					JTFile jtfile = EUtil.file2JTFile(listFile[i]);
					if (jtfile != null) {
						listJTFile.add(jtfile);
					}
				}
			}
		}
		return listJTFile;
	}

	/**
	 * File 转 JTFile
	 * 
	 * @param file
	 * @return
	 */
	public static JTFile file2JTFile(File file) {
		JTFile jtfile = null;
		if (file != null && file.isFile()) {
			jtfile = new JTFile();
			jtfile.mLocalFilePath = file.getAbsolutePath();
			jtfile.mFileName = file.getName();
			if (file.getName().lastIndexOf(".") < 0) {
				jtfile.mSuffixName = "无";
			} else {
				jtfile.mSuffixName = file.getName().substring(
						file.getName().lastIndexOf(".") + 1);
			}
			jtfile.mFileSize = file.length();
			jtfile.mCreateTime = file.lastModified();
		}
		return jtfile;
	}

	/** 加密相关 */
	private final static int JELLY_BEAN_4_2 = 17;

	/**
	 * 加密
	 * 
	 * @param key
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String src) {
		String encryptKey = "";
		try {
			byte[] rawKey = getRawKey(EConsts.PASSWORD_KEY.getBytes());
			byte[] result = encrypt(rawKey, src.getBytes());
			encryptKey = toHex(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptKey;
	}

	/**
	 * 解密
	 * 
	 * @param key
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String encrypted) {
		String decryptKey = "";
		try {
			byte[] rawKey = getRawKey(EConsts.PASSWORD_KEY.getBytes());
			byte[] enc = toByte(encrypted);
			byte[] result = decrypt(rawKey, enc);
			decryptKey = new String(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decryptKey;
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = null;
		if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN_4_2) {
			sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
		} else {
			sr = SecureRandom.getInstance("SHA1PRNG");
		}
		sr.setSeed(seed);
		kgen.init(256, sr); // 256 bits or 128 bits,192bits
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(src);
		return encrypted;
	}

	private static byte[] decrypt(byte[] key, byte[] encrypted)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";
	private static Bitmap rotatedBitmap;

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	/**
	 * @param filePath
	 * @return
	 */
	public static JTFile createJTFile(String filePath) {
		JTFile jtfile = null;
		if (filePath == null || filePath.length() <= 0) {
			return null;
		}
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		jtfile = new JTFile();
		jtfile.mLocalFilePath = filePath;
		jtfile.mFileName = filePath.substring(filePath
				.lastIndexOf(File.separator) + 1);
		jtfile.mFileSize = file.length();
		jtfile.mTaskId = TaskIDMaker.getTaskId(App.getUser().mUserName);
		return jtfile;
	}

	/**
	 * 从本地文件创建JTFile
	 * 
	 * @param filePath
	 * @return
	 */
	public static JTFile createJTFileFromLocalFile(String filePath) {
		JTFile jtFile = null;
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		jtFile = new JTFile();
		jtFile.setId(TaskIDMaker.getTaskId(App.getUser().mUserName)); // 文件id
		jtFile.mLocalFilePath = filePath; // 文件路径
		jtFile.fileName = filePath.substring(filePath
				.lastIndexOf(File.separator) + 1);
		if (jtFile.fileName.indexOf(".") > 0) {
			jtFile.mSuffixName = jtFile.fileName.substring(jtFile.fileName
					.lastIndexOf(".") + 1);
		}
		if (isImageFile(jtFile.mSuffixName)) {
			jtFile.mType = JTFile.TYPE_IMAGE;
		} else if (isVideoFile(jtFile.mSuffixName)) {
			jtFile.mType = JTFile.TYPE_VIDEO;
		} else if (isAudioFile(jtFile.mSuffixName)) {
			jtFile.mType = JTFile.TYPE_AUDIO;
		} else {
			jtFile.mType = JTFile.TYPE_FILE;
		}
		jtFile.mFileSize = file.length();
		jtFile.mTaskId = TaskIDMaker.getTaskId(App.getUser().mUserName);
		return jtFile;
	}

	/**
	 * 是否是图片文件
	 * 
	 * @param suffix
	 * @return
	 */
	public static boolean isImageFile(String suffix) {
		if (TextUtils.isEmpty(suffix)) {
			return false;
		}
		if (suffix.startsWith(".")) {
			suffix = suffix.substring(1);
		}
		String imageFormat[] = new String[] { "JPEG", "JPG", "BMP", "PNG" };
		for (int i = 0; i < imageFormat.length; i++) {
			if (suffix.equalsIgnoreCase(imageFormat[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否是视频文件
	 * 
	 * @param suffix
	 * @return
	 */
	public static boolean isVideoFile(String suffix) {
		if (TextUtils.isEmpty(suffix)) {
			return false;
		}
		if (suffix.startsWith(".")) {
			suffix = suffix.substring(1);
		}
		String videoFormat[] = new String[] { "MP4", "3GP" };
		for (int i = 0; i < videoFormat.length; i++) {
			if (suffix.equalsIgnoreCase(videoFormat[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否是音频文件
	 * 
	 * @param suffix
	 * @return
	 */
	public static boolean isAudioFile(String suffix) {
		if (TextUtils.isEmpty(suffix)) {
			return false;
		}
		if (suffix.startsWith(".")) {
			suffix = suffix.substring(1);
		}
		String audioFormat[] = new String[] { "AMR" };
		for (int i = 0; i < audioFormat.length; i++) {
			if (suffix.equalsIgnoreCase(audioFormat[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param filePath
	 * @return
	 */
	/*
	 * public static JTFile createJTFile(Context context,Uri uri) { JTFile
	 * jtfile = null; String filePath =
	 * EUtil.uri2Path(context.getContentResolver(), uri); if (filePath == null
	 * || filePath.length() <= 0) { return null; } File file = new
	 * File(filePath); if (!file.exists() || !file.isFile()) { return null; }
	 * jtfile = new JTFile(); // jtfile.mUri = uri; jtfile.mLocalFilePath =
	 * filePath; jtfile.mFileName = filePath.substring(filePath
	 * .lastIndexOf(File.separator) + 1); jtfile.mFileSize = file.length();
	 * jtfile.mTaskId = TaskIDMaker.getTaskId(App.getUser().mUserName); return
	 * jtfile; }
	 */

	public static void showShareDialog(Context context, String content,
			final CustomDlgClickListener listener) {
		LinearLayout parentLl = (LinearLayout) LayoutInflater.from(
				App.getApplicationConxt()).inflate(
				R.layout.widget_share_content, null);
		final InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		TextView contentTv = (TextView) parentLl.findViewById(R.id.contentTv);
		contentTv.setText(content);
		final EditText messageEt = (EditText) parentLl
				.findViewById(R.id.messageEt);
		new AlertDialog.Builder(context)
				.setTitle("分享")
				.setView(parentLl)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						listener.onPositiveClick(messageEt.getText().toString());
						imm.hideSoftInputFromWindow(messageEt.getWindowToken(),
								0);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						imm.hideSoftInputFromWindow(messageEt.getWindowToken(),
								0);
					}
				}).create().show();
	}

	// @SuppressLint("NewApi")
	public static String getThumbUploadPath(Context context, String oldPath,
			int bitmapMaxWidth) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(oldPath, options);
		int height = options.outHeight;
		int width = options.outWidth;
		int reqHeight = 0;
		// 设置为最大宽度为800
		bitmapMaxWidth = 800;
		int reqWidth = bitmapMaxWidth;
		reqHeight = (reqWidth * height) / width;
		// 在内存中创建bitmap对象，这个对象按照缩放大小创建的
		options.inSampleSize = calculateInSampleSize(options, bitmapMaxWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(oldPath, options);
		if (bitmap == null) {
			return "";
		}
		// Log.d("压缩前", bitmap.getByteCount() + "");
		// Log.e("asdasdas", "reqWidth->"+reqWidth+"---reqHeight->"+reqHeight);
		Bitmap bbb = compressImage(Bitmap.createScaledBitmap(bitmap,
				bitmapMaxWidth, reqHeight, false));
		if (bbb == null) {
			return "";
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		return saveImg(context, bbb, timeStamp);
	}

	/**
	 * 获取图片缩略图Bitmap对象
	 * 
	 * @param context
	 * @param imgPath
	 * @param bitmapMaxWidth
	 * @return
	 */
	public static Bitmap getImageThumb(Context context, String imgPath,
			int bitmapMaxWidth) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPath, options);
		int height = options.outHeight;
		int width = options.outWidth;
		int reqHeight = 0;
		// 设置为最大宽度
		int reqWidth = bitmapMaxWidth;
		reqHeight = (reqWidth * height) / width;
		// 在内存中创建bitmap对象，这个对象按照缩放大小创建的
		options.inSampleSize = calculateInSampleSize(options, bitmapMaxWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
		if (bitmap == null) {
			return bitmap;
		}
		Bitmap bbb = compressImage(Bitmap.createScaledBitmap(bitmap,
				bitmapMaxWidth, reqHeight, false));
		return bbb;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	/**
	 * 获取压缩后的Bitmap
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		Bitmap bitmap = null;
		try {
			if (image == null) {
				return image;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 100;
			while (baos.toByteArray().length > 300 * 1024) { // 循环判断如果压缩后图片是否大于300kb,大于继续压缩
				options -= 10; // 每次都减少10
				baos.reset(); // 重置baos即清空baos
				if (options <= 0) {
					break;
				}
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
			return bitmap;
		} catch (Exception e) {
			
		}
		return bitmap;
	}

	// 保存图片
	public static String saveImg(Context context, Bitmap b, String name) {
		try {
			File path = EUtil.getAppCacheFileDir(context);
			File mediaFile = new File(path, name + ".jpg");
			if (mediaFile.exists()) {
				mediaFile.delete();
			}
			if (!path.exists()) {
				path.mkdirs();
			}
			mediaFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(mediaFile);
			b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			b.recycle();
			b = null;
			System.gc();
			return mediaFile.getPath();
		} catch (IOException e) {
			return "";
		}
	}

	/**
	 * 保存图片到指定位置
	 * 
	 * @param context
	 * @param bitmap
	 * @param name
	 * @param dir
	 * @return 图片的绝对路径
	 */
	public static String saveImageEx(Context context, Bitmap bitmap,
			String fileName, File dir) {
		String imagePath = null;
		if (dir == null || bitmap == null || TextUtils.isEmpty(fileName)) {
			return null;
		}
		try {
			File mediaFile = new File(dir, fileName
					+ EConsts.DEFAULT_PIC_SUFFIX);
			if (mediaFile.exists()) {
				mediaFile.delete();
			}
			if (!dir.exists()) {
				dir.mkdirs();
			}
			mediaFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(mediaFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			bitmap.recycle();
			bitmap = null;
			System.gc();
			imagePath = mediaFile.getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imagePath;
	}

	/**
	 * 保存图片
	 * 
	 * @param context
	 * @param b
	 * @param name
	 *            图片名
	 * @param desDir
	 *            目标路径
	 * @return
	 */
	public static String saveImage(Context context, Bitmap bitmap, String name,
			File desDir) {
		String imgPath = null;
		try {
			File imgFile = new File(desDir, name);
			if (imgFile.exists()) {
				imgFile.delete();
			}
			if (!desDir.exists()) {
				desDir.mkdirs();
			}
			imgFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imgFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			fos.flush();
			fos.close();
			bitmap.recycle();
			bitmap = null;
			imgPath = imgFile.getPath();
		} catch (IOException e) {
			Log.d(TAG, e.getMessage());
		}
		return imgPath;
	}

	/**
	 * 文件是否已经下载完成
	 * 
	 * @param jtFile
	 * @return
	 */
	public static boolean isFileExist(Context context, JTFile jtFile) {

		if (jtFile == null) {
			return false;
		}
		File file = null;
		if (jtFile.mType == JTFile.TYPE_FILE) {
			file = new File(EUtil.getAppCacheFileDir(context), jtFile.mFileName);
		} else {
			file = new File(EUtil.getAppFileDir(context), jtFile.mFileName);
		}
		if (file.exists() /* by d.c && file.length() == jtFile.mFileSize */) {
			return true;
		} else {
			return false;
		}
	}
	
	public static long getFileSize(Context context, JTFile jtFile) {
		if (jtFile == null) {
			return 0;
		}
		File file = null;
		if (jtFile.mType == JTFile.TYPE_FILE) {
			file = new File(EUtil.getAppCacheFileDir(context), jtFile.mFileName);
		} else {
			file = new File(EUtil.getAppFileDir(context), jtFile.mFileName);
		}
		if (file.exists() /* by d.c && file.length() == jtFile.mFileSize */) {
			return file.length();
		} else {
			return 0;
		}
	}

	/**
	 * 文件是否已存在，但未下载完成（返回已下载的文件信息）
	 * 
	 * @return JTFile
	 */
	public static JTFile getFileDownloadInfo(Context context, String url) {

		if (url == null || url.length() <= 0) {
			return null;
		}
		JTFile jtFile = null;
		DownloadFileDBManager manager = new DownloadFileDBManager(context);
		jtFile = manager.query(App.getUserID(), url);
		if (jtFile == null) {
			return null;
		}
		File file = null;
		if (jtFile.mType == JTFile.TYPE_FILE) {
			file = new File(EUtil.getAppCacheFileDir(context), jtFile.mFileName);
		} else {
			file = new File(EUtil.getAppFileDir(context), jtFile.mFileName);
		}
		if (!file.exists() || file.length() != jtFile.mDownloadSize) { // 信息不符
			manager.delete(App.getUserID(), url); // 删除数据库
			file.delete(); // 删除本地缓存文件
			return null;
		} else { // 返回本地文件信息
			return jtFile;
		}
	}

	/**
	 * 获取用户下载的文件（指定目录下）
	 * 
	 * @return
	 */
	public static List<JTFile> getUserListJTFile(Context context) {

		List<JTFile> listJTFile = new ArrayList<JTFile>();
		if (!EUtil.isSDCardExist()) {
			EUtil.showToast("没有SD卡");
		} else {
			File fileDir = EUtil.getAppFileDir(context);
			if (fileDir != null && fileDir.isDirectory()) {
				File[] files = fileDir.listFiles();
				if (files != null && files.length > 0) {

					/*
					 * DownloadFileDBManager dbManager =new
					 * DownloadFileDBManager(context); List<JTFile>
					 * tempListJTFile = dbManager.query(App.getUserID()); for
					 * (int i = 0; i < files.length; i++) { JTFile jtFile =
					 * EUtil.file2JTFile(files[i]); // 是否下载完成 if(jtFile !=
					 * null){ if(tempListJTFile != null && tempListJTFile.size()
					 * > 0){ boolean isFinish = true; for (JTFile tFile :
					 * tempListJTFile) {
					 * if(tFile.mFileName.equals(jtFile.mFileName)){ isFinish =
					 * false; break; } } if(isFinish){ listJTFile.add(jtFile); }
					 * } else{ listJTFile.add(jtFile); } } }
					 */

					for (int i = 0; i < files.length; i++) {
						JTFile jtFile = EUtil.file2JTFile(files[i]);
						if (jtFile != null) {
							listJTFile.add(jtFile);
						}
					}
				}
			}
		}
		// 按最后修改时间排序
		Collections.sort(listJTFile, new Comparator<JTFile>() {

			@Override
			public int compare(JTFile arg0, JTFile arg1) {
				// TODO Auto-generated method stub
				if (arg0.mCreateTime > arg1.mCreateTime) {
					return -1;
				} else if (arg0.mCreateTime == arg1.mCreateTime) {
					return 0;
				} else {
					return 1;
				}
			}
		});
		return listJTFile;
	}

	/**
	 * 是否支持此视频格式
	 * 
	 * @param format
	 * @return
	 */
	public static boolean isVideoFormatSupport(String format) {
		boolean result = false;
		String[] formatSet = new String[] { "mp4", "3gp" }; // "avi", "wmv",
		if (format != null) {
			for (int i = 0; i < formatSet.length; i++) {
				if (formatSet[i].equalsIgnoreCase(format)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 是否支持此图片格式
	 * 
	 * @param format
	 * @return
	 */
	public static boolean isPictureFormatSupport(String format) {
		boolean result = false;
		String[] formatSet = new String[] { "bmp", "jpg", "jpeg", "png" };
		if (format != null) {
			for (int i = 0; i < formatSet.length; i++) {
				if (formatSet[i].equalsIgnoreCase(format)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 获取视频的截图
	 * 
	 * @param videoPath
	 * @return
	 */
	public static String getVideoThumbnail(Context context, String videoPath) {
		String thumbnailPath = "";
		String extension = EUtil.getExtensionName(videoPath);
		if (isVideoFormatSupport(extension)) {
			MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
			mediaMetadataRetriever.setDataSource(videoPath);
			Bitmap bmpOriginal = mediaMetadataRetriever.getFrameAtTime(0);
			if (bmpOriginal != null) {
				File dir = EUtil.getAppCacheFileDir(context);
				if (dir == null) {
					return thumbnailPath;
				}
				File file = new File(dir, DateFormat.format("yyyyMMddkkmmss",
						System.currentTimeMillis()) + ".jpg");
				if (file.exists()) {
					file.delete();
				}
				try {
					BufferedOutputStream bos = new BufferedOutputStream(
							new FileOutputStream(file));
					bmpOriginal.compress(CompressFormat.JPEG, 50, bos);
					bos.flush();
					bos.close();
					thumbnailPath = file.getAbsolutePath();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return thumbnailPath;
	}

	/**
	 * 获取视频缩略图Bitmap
	 * 
	 * @param context
	 * @param videoPath
	 * @param maxImageWidth
	 * @return
	 */
	public static Bitmap getVideoScreenshotBitmap(Context context,
			String videoPath) {
		if (TextUtils.isEmpty(videoPath) || !new File(videoPath).exists()) {
			return null;
		}
		Bitmap bitmap = null;
		MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
		mediaMetadataRetriever.setDataSource(videoPath);
		bitmap = mediaMetadataRetriever.getFrameAtTime(0);
		return bitmap;
	}

	/**
	 * 获取压缩后的视频缩略图Bitmap
	 * 
	 * @param context
	 * @param videoPath
	 * @return
	 */
	public static Bitmap getVideoCompressedScreenshotBitmap(Context context,
			String videoPath) {
		Bitmap originalBitmap = getVideoScreenshotBitmap(context, videoPath);
		Bitmap compressedBitmap = compressImage(originalBitmap);
		return compressedBitmap;
	}

	/**
	 * 计算每个月的天数
	 * 
	 * @param year
	 *            年份
	 * @param month
	 *            月份
	 * @return days 每个月的天数
	 */
	public static int getDaysOfMonth(int year, int month) {

		int days = 0;

		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			days = 31;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			days = 30;
		} else { // 2月份，闰年29天、平年28天
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
				days = 29;
			} else {
				days = 28;
			}
		}
		return days;
	}

	/**
	 * 获取ImageLoader默认显示配置
	 * 
	 * @return
	 */
	public static DisplayImageOptions getDefaultImageLoaderDisplayOptions() {

		return new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
				.cacheOnDisc(true).build();
	}

	/**
	 * 将文件保存到用户目录
	 * 
	 * @param file
	 * @return
	 */
	public static boolean saveFileToUserDir(Context context, File srcFile) {

		if (srcFile == null || !srcFile.exists() || !srcFile.isFile()) {
			return false;
		}
		File dir = EUtil.getAppFileDir(context);
		if (dir == null) {
			return false;
		}
		File desFile = new File(dir, srcFile.getName());
		if (desFile.exists()) {
			desFile.delete();
		}
		return EUtil.copyFile(srcFile, desFile);
	}

	/**
	 * 将图片保存到用户目录
	 * 
	 * @param file
	 * @return
	 */
	public static boolean saveImageToUserDir(Context context, File srcFile) {

		if (srcFile == null || !srcFile.exists() || !srcFile.isFile()) {
			return false;
		}
		File dir = EUtil.getAppFileDir(context);
		if (dir == null) {
			return false;
		}
		File desFile = null;
		if (!srcFile.getName().contains(".")) { // 没有后缀名
			desFile = new File(dir, srcFile.getName() + ".jpg");
		} else {
			desFile = new File(dir, srcFile.getName());
		}
		if (desFile.exists()) {
			desFile.delete();
		}
		return EUtil.copyFile(srcFile, desFile);
	}

	/**
	 * 获取通知栏高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 返回我的关系数据库对象
	 * 
	 * @return
	 */
	public static ConnectionsDBManager getConnectionsDBManager(Context context) {
		ConnectionsDBManager connsDBManager = null;
		SharedPreferences sp = context.getSharedPreferences(
				EConsts.share_firstLoginGetConnections, Context.MODE_PRIVATE);
		String tableName = sp.getString(EConsts.share_itemUserTableName, "");
		if (StringUtils.isEmpty(tableName)) {
			connsDBManager = new ConnectionsDBManager(context,
					DBHelper.TABLE_APP_CONNECTIONS);
		} else {
			if (tableName.equals(DBHelper.TABLE_APP_CONNECTIONS)) {
				connsDBManager = new ConnectionsDBManager(context,
						DBHelper.TABLE_APP_CONNECTIONS);
			}

			else if (tableName.equals(DBHelper.TABLE_APP_CONNECTIONS_BACK)) {
				connsDBManager = new ConnectionsDBManager(context,
						DBHelper.TABLE_APP_CONNECTIONS_BACK);
			} else {
				connsDBManager = new ConnectionsDBManager(context,
						DBHelper.TABLE_APP_CONNECTIONS);
			}
		}
		return connsDBManager;
	}

	// 获取ApiKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	/**
	 * 去掉字符串里的html标签
	 * 
	 * @param html
	 * @return
	 */
	public static String filterHtml(String html) {
		Pattern pattern = Pattern.compile("<style[^>]*?>[\\D\\d]*?<\\/style>");
		Matcher matcher = pattern.matcher(html);
		String htmlStr = matcher.replaceAll("");
		pattern = Pattern.compile("<[^>]+>");
		String filterStr = pattern.matcher(htmlStr).replaceAll("");
		filterStr=filterStr.replace("&nbsp;", "");
		filterStr=filterStr.replace("&#13;", "");
		return filterStr.trim();
	}

	/**
	 * Returns true if the string is null or 0-length.
	 * 
	 * @param str
	 *            the string to be examined
	 * @return true if str is null or zero length
	 */
	public static boolean isEmpty(CharSequence str) {
		if (str == null || str.length() == 0
				|| "null".equals(str.toString().trim()))
			return true;
		else
			return false;
	}

	/**
	 * 表情转换
	 * 
	 * @param str
	 * @return
	 */
	public static CharSequence smileyParser(String str, Context mContext) {
		SmileyParser smileyParser;// 表情匹配
		SmileyParser2 smileyParser2;// 表情匹配
		smileyParser = SmileyParser.getInstance(mContext);
		smileyParser2 = SmileyParser2.getInstance(mContext);
		CharSequence charSequence1 = smileyParser.addSmileySpans(str);
		CharSequence charSequence2 = smileyParser2
				.addSmileySpans(charSequence1);
		return charSequence2;
	}

	/**
	 * 表情转换
	 * 
	 * @param str
	 * @return
	 */
	public static CharSequence commonSmileyParser(String str, Context mContext) {
		CommonSmileyParser commonSmileyParser;// 表情匹配
		commonSmileyParser = CommonSmileyParser.getInstance(mContext);
		CharSequence charSequence = commonSmileyParser.addSmileySpans(str);
		return charSequence;
	}

	public static String getJson(Context context, String fileName) {

		StringBuilder stringBuilder = new StringBuilder();
		try {
			AssetManager assetManager = context.getAssets();
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					assetManager.open(fileName)));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 获取ImageView视图的同时加载显示url
	 * 
	 * @param text
	 * @return
	 */
	public static ImageView getImageView(Context context, String url) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		ImageLoader.getInstance().displayImage(url, imageView);
		return imageView;
	}
	
	public static ImageView getImageView(Context context, int resID) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		imageView.setBackgroundResource(resID);
		return imageView;
	}
	
	/*
	 * 获取系统时间    只显示     月 日
	 */
	public static String getData(){
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
		return dateFormat.format(date);
	}

	/*
	 * 获取星期
	 */
	public static String getWeek(Context mContext,Calendar nowCalendar){
		int week = nowCalendar.get(Calendar.DAY_OF_WEEK);
		switch (week) {
		case 2:
			return mContext.getResources().getString(R.string.Monday);//2
		case 3:
			return mContext.getResources().getString(R.string.Tuesday);//3
		case 4:
			return mContext.getResources().getString(R.string.Wednesday);//4
		case 5:
			return mContext.getResources().getString(R.string.Thursday);//5
		case 6:
			return mContext.getResources().getString(R.string.Friday);//6
		case 7:
			return mContext.getResources().getString(R.string.Saturday);//7
		case 1:
			return mContext.getResources().getString(R.string.Sunday);//1
		default:
			return null;
		}
	}

	/*
	 * 获取农历
	 */
	private static int monCyl, dayCyl, yearCyl;
    private static int year, month, day;
    private static boolean isLeap;
    private static int[] lunarInfo = {0x04bd8, 0x04ae0, 0x0a570, 0x054d5,
        0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2, 0x04ae0,
        0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2,
        0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40,
        0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566, 0x0d4a0,
        0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7,
        0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0,
        0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550, 0x15355,
        0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
        0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263,
        0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0,
        0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6, 0x095b0,
        0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46,
        0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50,
        0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960, 0x0d954,
        0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0,
        0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0,
        0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0, 0x0ad50,
        0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
        0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6,
        0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2, 0x049b0,
        0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0};
    private static String[] Gan = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛",
        "壬", "癸"};
    private static String[] Zhi = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未",
        "申", "酉", "戌", "亥"};
    private static String[] Animals = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊",
        "猴", "鸡", "狗", "猪"};
    private static String[] nStr1 = {"日", "一", "二", "三", "四", "五", "六", "七",
        "八", "九", "十"};
    private static String[] nStr2 = {"初", "十", "廿", "卅", "　"};
    private static String[] monthNong = {"正", "正", "二", "三", "四", "五", "六",
        "七", "八", "九", "十", "十一", "十二"};
    private static String[] yearName = {"0", "1", "2", "3", "4", "5", "6",
        "7", "8", "9"};
    //====================================== 传回农历 y年的总天数  
    private static int lYearDays(int y) {
        int i;
        int sum = 348; //29*12
        for (i = 0x8000; i > 0x8; i >>= 1) {
            sum += (lunarInfo[y - 1900] & i) == 0 ? 0 : 1; //大月+1天  
        }
        return (sum + leapDays(y)); //+闰月的天数  
    }
    //====================================== 传回农历 y年闰月的天数  
    private static int leapDays(int y) {
        if (leapMonth(y) != 0) {
            return ((lunarInfo[y - 1900] & 0x10000) == 0 ? 29 : 30);
        } else {
            return (0);
        }
    }
    //====================================== 传回农历 y年闰哪个月 1-12 , 没闰传回 0  
    private static int leapMonth(int y) {
        return (lunarInfo[y - 1900] & 0xf);
    }
    //====================================== 传回农历 y年m月的总天数  
    private static int monthDays(int y, int m) {
        return ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0 ? 29 : 30);
    }
    //====================================== 算出农历, 传入日期物件, 传回农历日期物件  
    //                                      该物件属性有 .year .month .day .isLeap .yearCyl .dayCyl .monCyl  
    private static void Lunar1(Date objDate) {
        int i, leap = 0, temp = 0;
        Calendar cl = Calendar.getInstance();
        cl.set(1900, 0, 31); //1900-01-31是农历1900年正月初一  
        Date baseDate = cl.getTime();
        //1900-01-31是农历1900年正月初一  
        int offset = (int) ((objDate.getTime() - baseDate.getTime()) / 86400000); //天数(86400000=24*60*60*1000)  
        dayCyl = offset + 40; //1899-12-21是农历1899年腊月甲子日  
        monCyl = 14; //1898-10-01是农历甲子月  

        //得到年数  

        for (i = 1900; i < 2050 && offset > 0; i++) {
            temp = lYearDays(i); //农历每年天数  
            offset -= temp;
            monCyl += 12;
        }
        if (offset < 0) {
            offset += temp;
            i--;
            monCyl -= 12;
        }
        year = i; //农历年份  
        yearCyl = i - 1864; //1864年是甲子年  
        leap = leapMonth(i); //闰哪个月  
        isLeap = false;
        for (i = 1; i < 13 && offset > 0; i++) {
            //闰月  
            if (leap > 0 && i == (leap + 1) && isLeap == false) {
                --i;
                isLeap = true;
                temp = leapDays(year);
            } else {
                temp = monthDays(year, i);
            }
            //解除闰月  
            if (isLeap == true && i == (leap + 1)) {
                isLeap = false;
            }
            offset -= temp;
            if (isLeap == false) {
                monCyl++;
            }
        }
        if (offset == 0 && leap > 0 && i == leap + 1) {
            if (isLeap) {
                isLeap = false;
            } else {
                isLeap = true;
                --i;
                --monCyl;
            }
        }
        if (offset < 0) {
            offset += temp;
            --i;
            --monCyl;
        }
        month = i; //农历月份  
        day = offset + 1; //农历天份  
    }
    private static int getYear() {
        return (year);
    }
    private static int getMonth() {
        return (month);
    }
    private static int getDay() {
        return (day);
    }
    private static int getMonCyl() {
        return (monCyl);
    }
    private static int getYearCyl() {
        return (yearCyl);
    }
    private static int getDayCyl() {
        return (dayCyl);
    }
    private static boolean getIsLeap() {
        return (isLeap);
    }
    //============================== 传入 offset 传回干支, 0=甲子  
    private static String cyclical(int num) {
        return (Gan[num % 10] + Zhi[num % 12]);
    }
    //====================== 中文日期  
    private static String cDay(int d) {
        String s;
        switch (d) {
            case 10:
                s = "初十";
                break;
            case 20:
                s = "二十";
                break;
            case 30:
                s = "三十";
                break;
            default:
                s = nStr2[(int) (d / 10)];//取商  
                s += nStr1[d % 10];//取余  
        }
        return (s);
    }
    private static String cYear(int y) {
        String s = " ";
        int d;
        while (y > 0) {
            d = y % 10;
            y = (y - d) / 10;
            s = yearName[d] + s;
        }
        return (s);
    }
	public static String getNoLi(int tag, Calendar nowCalendar){
		Date sDObj = new Date();
        int SY = nowCalendar.get(Calendar.YEAR);
        int sy;
        sy = (SY - 4) % 12;
        //日期  
        Lunar1(sDObj); //农历
        switch (tag) {
		case 1:
			return "农历 "+(getIsLeap() ? "闰" : "") + monthNong[getMonth()] + "月"+ (monthDays(getYear(), getMonth()) == 29 ? "小" : "大") + 
					cDay(getDay()) + " ";
		case 2:
			return cyclical(getYearCyl()) + Animals[sy] + "年"+ "\t" +"\t" + cyclical(getMonCyl()) + "月"+ "\t" + "\t" + cyclical(getDayCyl()) + "日";
		default:
			return null;
		}
		
	}
}
