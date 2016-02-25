package com.utils.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.ui.im.IMRelationSelectActivity;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.image.AnimateFirstDisplayListener;
import com.utils.string.StringUtils;

/** @author bin */
public class Util {

	public static String Log_Tag = "TangYu";

	private static BroadcastReceiver sdCardReceiver;
	private static boolean isSDCardReceiverRegister = false;
	private static boolean isSDCardAvailable = false;
	private static boolean isSDCardWriteable = false;
	private static Context saveContext;

	public static void v(String msg) {
		Log.v(Log_Tag, msg);
	}

	public static void e(String msg) {
		Log.e(Log_Tag, msg);
	}

	public static void toast(Context ctx, String msg, boolean isShort) {
		Toast.makeText(ctx, msg, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
	}

	public static void toast(Context ctx, int msgid, boolean isShort) {
		Toast.makeText(ctx, msgid, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
	}

	public static boolean isNull(Object o) {
		return o == null ? true : false;
	}

	public static boolean isNull(List<?> list) {
		return list == null || list.size() == 0 ? true : false;
	}

	public static boolean isNull(String str) {
		return TextUtils.isEmpty(str) ? true : false;
	}

	/** @param src
	 * @param key
	 * @return >= 0 is in array. negative value means not. */
	public static int isInArray(int[] src, int key) {
		if (isNull(src))
			return -1;
		for (int i = 0; i < src.length; i++) {
			if (src[i] == key) {
				return i;
			}
		}
		return -1;
	}

	/** find param2 in param1
	 * 
	 * @param src
	 * @param key
	 * @return if return positive value. that is index in array. negative value
	 *         means not. */
	public static int isInList(List<? extends Object> src, Object key) {
		if (isNull(src))
			return -1;
		if (isNull(key))
			return -1;
		for (int i = 0; i < src.size(); i++) {
			if (isNull(src.get(i)))
				continue;
			if (src.get(i).equals(key)) {
				return i;
			}
		}
		return -1;
	}

	/** parameter 2 is contain in parameter 1.
	 * 
	 * @param sourceFlag
	 * @param compareFlag
	 * @return */
	public static boolean isFlagContain(int sourceFlag, int compareFlag) {
		return (sourceFlag & compareFlag) == compareFlag;
	}

	/** Whether show StatueBar or not.
	 * 
	 * @param active
	 *            in which Activity
	 * @param visible
	 *            View.VISIBLE is show, otherwise is dismiss */
	public static void statueBarVisible(Activity active, final int visible) {
		if (visible == View.VISIBLE) {
			active.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		else {
			active.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
	}

	/** check SD card is available. then u may be call toastShort to communicate
	 * with user.
	 * 
	 * @return <b>true</b> the SD card is available. <b>false</b> not. */
	public static boolean sdcardIsOnline() {
		final String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) ? true : false;
	}

	/** update the isSDCardAvailable and isSDCardWriteable state */
	private static void sdCardUpdateState(Context context) {
		final String sdCardState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(sdCardState)) {
			isSDCardAvailable = isSDCardWriteable = true;
		}
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(sdCardState)) {
			isSDCardAvailable = true;
			isSDCardWriteable = false;
		}
		else {
			isSDCardAvailable = isSDCardWriteable = false;
		}
	}

	/** open the SD card state listener. Generally call it in "onStart" method. */
	public static synchronized void sdCardStartListener(Context context, sdcardListener lis) {
		if (saveContext != null && saveContext != context) {
			sdCardStopListener(saveContext);
		}
		mSdcardListener = lis;
		saveContext = context;
		sdCardReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				sdCardUpdateState(context);
				if (mSdcardListener != null)
					mSdcardListener.onReceiver(isSDCardAvailable, isSDCardWriteable);
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
		if (!isSDCardReceiverRegister) {
			context.registerReceiver(sdCardReceiver, filter);
			isSDCardReceiverRegister = true;
		}
		sdCardUpdateState(context);
	}

	/** close the SD card state listener. Generally call it in "onStart" method. */
	public static synchronized void sdCardStopListener(Context context) {
		if (isSDCardReceiverRegister && saveContext == context) {
			context.unregisterReceiver(sdCardReceiver);
			isSDCardReceiverRegister = false;
			mSdcardListener = null;
		}
	}

	public static sdcardListener mSdcardListener;


	public interface sdcardListener {
		void onReceiver(boolean isAvailable, boolean isCanWrite);
	}

	public static void sysSetActionBness(Activity action, float bness) {
		WindowManager.LayoutParams lp = action.getWindow().getAttributes();
		lp.screenBrightness = bness;
		action.getWindow().setAttributes(lp);
	}

	public static float sysGetActionBness(Activity action) {
		return action.getWindow().getAttributes().screenBrightness;
	}

	public static void sysIsLockScreen(Activity act, boolean isLock) {
		if (isLock) {
			switch (act.getResources().getConfiguration().orientation) {
			case Configuration.ORIENTATION_PORTRAIT:
				act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;
			case Configuration.ORIENTATION_LANDSCAPE:
				act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				break;
			}
		}
		else {
			act.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null && cm.getActiveNetworkInfo() != null) {
			return cm.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}

	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
		List<String> accessibleProviders = lm.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;
	}

	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	public static final void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (PendingIntent.CanceledException e) {
			e.printStackTrace();
		}
	}

	public static boolean is3rd(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	public static class DensityUtil {

		/** dp to px */
		public static int dip2px(Context context, float dpValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dpValue * scale + 0.5f);
		}

		/** px to dp */
		public static int px2dip(Context context, float pxValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		}
		
		public static int px2sp(Context context, float pxValue) { 
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
            return (int) (pxValue / fontScale + 0.5f); 
        } 
       
        /**
         * 将sp值转换为px值，保证文字大小不变
         * 
         * @param spValue
         * @param fontScale
         *            （DisplayMetrics类中属性scaledDensity）
         * @return
         */ 
        public static int sp2px(Context context, float spValue) { 
            final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
            return (int) (spValue * fontScale + 0.5f); 
        }
	}

	public static Object CloneObject(Object obj) {
		Object cloneobj = null;
		ByteArrayInputStream bin = null;
		ByteArrayOutputStream bout = null;
		try {
			// 把对象对到内存中去
			bout = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bout);
			oos.writeObject(obj);
			oos.close();
			// 把对象从内存中读出来
			ByteArrayInputStream bais = new ByteArrayInputStream(bout.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			cloneobj = ois.readObject();
			ois.close();
			return cloneobj;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** 判断对象是否为null或空
	 * 
	 * @param obj
	 *            return IOException */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (int i = 0; i < object.length; i++) {
				if (!isNullOrEmpty(object[i])) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}
	
	/**
	 * 
	 * @param userID 用户的ID
	 * @param ginTongImgPath 金桐网账号
	 * @return
	 */
	public static String getHeadImgPath(long userID ,String ginTongImgPath) {
		String imgPath;
		if(0 == userID){/*金桐的ID为0*/
			imgPath = ginTongImgPath;
		}else{
			imgPath = EAPIConsts.getTMSUrl()+"img/user/image/?module=user&uId="+userID+"&userId="+userID;
		}
		return imgPath;
	}
	
	/**
	 * 把des放到第一个位置
	 * @param list 要置换位置的list
	 * @param pos 要置换的下标
	 * @return 置换后的list
	 */
	public static <T> List<T> getList(List<T> list, int des,int pos) {
		if(list!=null){
			if(des<list.size()){
				T des_obj = list.remove(des);
				list.add(pos, des_obj);
			}
		}//不满足条件返回原list
		return list;
	}
	
	/**
	 * @param sourceId 图片id
	 * @param colorId 文字颜色id
	 * @param textSizeId 字体大小id
	 * @param text 要显示的文字
	 */
	public static Bitmap createBGBItmap(Context context, int sourceId, int colorId, int textSizeId, String text){
		Bitmap src = BitmapFactory.decodeResource(context.getResources(), sourceId).copy(Bitmap.Config.ARGB_8888, true);
		Bitmap bitmap = Bitmap.createBitmap(src);
		Canvas canvas =new Canvas(bitmap);//初始化画布绘制的图像到icon上
		Paint paint =new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);//创建画笔
		paint.setTextSize(context.getResources().getDimension(textSizeId));//设置文字的大小
		paint.setTypeface(Typeface.DEFAULT);
		paint.setColor(context.getResources().getColor(colorId));//文字的颜色
		paint.setTextAlign(Paint.Align.CENTER);
		
		int viewWidth = bitmap.getWidth();
		int viewHeight = bitmap.getHeight();
		FontMetrics fm = paint.getFontMetrics();
		//文本的宽度
		float textWidth = paint.measureText(text);
		float textCenterVerticalBaselineY = viewHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
		float textCenterX = (float)viewWidth / 2;
		float textBaselineY = textCenterVerticalBaselineY;
		canvas.drawText(text,textCenterX, textBaselineY, paint);//将文字写入。这里面的（120，130）代表着文字在图层上的初始位置
		canvas.save(canvas.ALL_SAVE_FLAG);//保存所有图层
		canvas.restore();
		return bitmap;
	}
	
	/**
	 * 初始化头像
	 * @param mContext
	 * @param avatarIv 
	 * @param name 名字
	 * @param pic 头像url
	 * @param gender 性别（不分性别；随意）
	 * @param type 1，人；2组织
	 */
	public static void initAvatarImage(final Context mContext, ImageView avatarIv, String name, String pic ,int gender,int type) {
		if(GlobalVariable.ISNETWORK_IMG_URL){
				Drawable drawable = null;
				if (TextUtils.isEmpty(pic)) {
					if (type ==1 ) {
						drawable = mContext.getResources().getDrawable(R.drawable.default_people_avatar);
					}else if(type ==2){
						drawable = mContext.getResources().getDrawable(R.drawable.default_portrait116);
					}else{
						drawable = mContext.getResources().getDrawable(R.drawable.default_people_avatar);
					}
					avatarIv.setImageDrawable(drawable);
				}else{
//					ImageLoader.getInstance().displayImage(pic, avatarIv,new AnimateFirstDisplayListener());
					if (type ==1 || type == 0) {
						com.tr.image.ImageLoader.load(avatarIv, pic, R.drawable.default_people_avatar);
					}else if(type ==2){
						com.tr.image.ImageLoader.load(avatarIv, pic, R.drawable.default_portrait116);
					}else{
						drawable = mContext.getResources().getDrawable(R.drawable.default_people_avatar);
						avatarIv.setImageDrawable(drawable);
					}
				}
				
		}else{
			if (!StringUtils.isEmpty(pic)/*&&!pic.endsWith(GlobalVariable.PERSON_DEFAULT_AVATAR)*/) {
				ImageLoader.getInstance().displayImage(pic, avatarIv,new AnimateFirstDisplayListener());
			}else {
				String lastchar = "";
				if(!TextUtils.isEmpty(name)){
					lastchar = name.substring(name.length()-1);
				}
				Bitmap bm = null;
				int resid =0;
				if (type == 1) {
					resid = R.drawable.no_avatar_but_gender;//粉色
//					if (gender==1||gender ==2) {
//					}else {
//						resid = R.drawable.ic_person_default_avatar_gray;
//					}
				}else if(type == 2){
					resid = R.drawable.no_avatar_client_organization;//浅蓝色
				}
				bm = createBGBItmap(mContext, resid, R.color.avatar_text_color, R.dimen.avatar_text_size, lastchar);
				avatarIv.setImageBitmap(bm);
			}
		}
		
	}
	
	/** 
	 * 读取图片属性：旋转的角度 
	 * @param path 图片绝对路径 
	 * @return degree旋转的角度 
	 */ 
	public static int getExifOrientation(String filepath) {  
	    int degree = 0;  
	    ExifInterface exif = null;  
	    try {  
	        exif = new ExifInterface(filepath);  
	    } catch (IOException ex) {  
	        Log.d("getExifOrientation", "cannot read exif" + ex);  
	    }  
	    if (exif != null) {  
	    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);  
	        if (orientation != -1) {  
	            switch(orientation) {  
	                case ExifInterface.ORIENTATION_ROTATE_90:  
	                    degree = 90;  
	                    break;  
	                case ExifInterface.ORIENTATION_ROTATE_180:  
	                    degree = 180;  
	                    break;  
	                case ExifInterface.ORIENTATION_ROTATE_270:  
	                    degree = 270;  
	                    break;  
	            }  
	        }  
	    }  
	    return degree;  
	}
	
	/** 
	 * 旋转图片，使图片保持正确的方向。 
	 * @param bitmap 原始图片 
	 * @param degrees 原始图片的角度 
	 * @return Bitmap 旋转后的图片 
	 */  
	public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {  
	    if (degrees == 0 || null == bitmap) {  
	        return bitmap;  
	    }  
	    Matrix matrix = new Matrix();  
	    matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);  
	    Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
	    if (null != bitmap) {  
	        bitmap.recycle();  
	    }  
	    return bmp;  
	}
}