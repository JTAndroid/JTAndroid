package com.tr.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.tr.http.HttpHelper;
import com.tr.http.HttpHelper.HttpResult;
import com.tr.image.manager.ThreadManager;
import com.tr.image.manager.ThreadManager.ThreadPoolProxy;
import com.utils.display.DisplayUtil;
import com.utils.string.StringUtils;

public class ImageLoader {
	/** 图片下载的线程池名称 */
	public static final String THREAD_POOL_NAME = "IMAGE_THREAD_POOL";
	/** 图片缓存最大数量 */
	public static final int MAX_DRAWABLE_COUNT = 100;
	/** 图片的KEY缓存 */
	private static ConcurrentLinkedQueue<String> mKeyCache = new ConcurrentLinkedQueue<String>();
	/** 图片的缓存 */
	private static Map<String, Drawable> mDrawableCache = new ConcurrentHashMap<String, Drawable>();
	private static BitmapFactory.Options mOptions = new BitmapFactory.Options();
	/** 图片下载的线程池 */
	private static ThreadPoolProxy mThreadPool = ThreadManager
			.getSinglePool(THREAD_POOL_NAME);
	/** 用于记录图片下载的任务，以便取消 */
	private static ConcurrentHashMap<String, Runnable> mMapRuunable = new ConcurrentHashMap<String, Runnable>();
	/** 图片的总大小 */
	private static long mTotalSize;

	public static final int CHAT_BITMAP = 100;
	public static final int MUCCHAT_BITMAP = 101;
	public static final int INDEX_BITMAP = 102;
	private static int typeBitmapSize = 50;
	private static Context mContext;

	static {
		mOptions.inDither = false;// 设置为false，将不考虑图片的抖动值，这会减少图片的内存占用
		mOptions.inPurgeable = true;// 设置为ture，表示允许系统在内存不足时，删除bitmap的数组。
		mOptions.inInputShareable = true;// 和inPurgeable配合使用，如果inPurgeable是false，那么该参数将被忽略，表示是否对bitmap的数组进行共享
	}

	public static void setContext(Context context) {
		mContext = context;
	}

	/**
	 * 加载图片>>无默认加载中图片
	 * 
	 * @param view
	 *            展示图片的view
	 * @param url
	 *            图片的URL
	 */
	public static void load(ImageView view, String url) {
		try {
			if (view == null || StringUtils.isEmpty(url)) {
				return;
			}
			typeBitmapSize = view.getWidth();
			view.setTag(url);// 把控件和图片的url进行绑定，因为加载是一个耗时的，等加载完毕了需要判定该控件是否和该url匹配
			Drawable drawable = null;
			if (url.contains("http")) {// 网络图片
				drawable = loadFromMemory(url);// 从内存中加载
				if (drawable != null) {
					setImageSafe(view, url, drawable);// 如果内存中加载到了，直接设置图片
				} else {
					// view.setImageResource(R.drawable.ic_default_avatar);//如果没加载到，设置默认图片，并异步加载
					asyncLoad(view, url);
				}
			} else {// 本地图片
				Bitmap bm = BitmapFactory.decodeFile(url);
				if (bm != null) {
					drawable = new BitmapDrawable(UIUtils.getResources(), bm);
					if (drawable != null) {
						setImageSafe(view, url, drawable);// 如果内存中加载到了，直接设置图片
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * 加载图片>>无默认加载中图片
	 * 
	 * @param view
	 *            展示图片的view
	 * @param url
	 *            图片的URL type :私聊：1
	 */
	public static void load(ImageView view, int type, String url, int resImage) {
		try {
			typeBitmapSize = type;
			if(view != null && StringUtils.isEmpty(url)){
				view.setImageResource(resImage);
			}
			if (view == null || StringUtils.isEmpty(url)) {
				return;
			}
			view.setTag(url);// 把控件和图片的url进行绑定，因为加载是一个耗时的，等加载完毕了需要判定该控件是否和该url匹配
			Drawable drawable = loadFromMemory(url);// 从内存中加载
			if (drawable != null) {
				setImageSafe(view, url, drawable);// 如果内存中加载到了，直接设置图片
			} else {
				view.setImageResource(resImage);// 如果没加载到，设置默认图片，并异步加载
				asyncLoad(view, url);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	/**
	 * 加载图片>>无默认加载中图片
	 * 
	 * @param view
	 *            展示图片的view
	 * @param url
	 *            图片的URL type :私聊：1
	 */
	public static void load(ImageView view, int type, String url) {
		try {
			typeBitmapSize = type;
			if (view == null || StringUtils.isEmpty(url)) {
				return;
			}
			view.setTag(url);// 把控件和图片的url进行绑定，因为加载是一个耗时的，等加载完毕了需要判定该控件是否和该url匹配
			Drawable drawable = loadFromMemory(url);// 从内存中加载
			if (drawable != null) {
				setImageSafe(view, url, drawable);// 如果内存中加载到了，直接设置图片
			} else {
				asyncLoad(view, url);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	/**
	 * 加载图片>>自定义默认加载中图片
	 * 
	 * @param view
	 *            展示图片的view
	 * @param url
	 *            图片的URL
	 * @param resImage
	 *            默认图片的id
	 */
	public static void load(ImageView view, String url, int resImage) {
		try {
			if (view == null || StringUtils.isEmpty(url)) {
				return;
			}
			view.setTag(url);// 把控件和图片的url进行绑定，因为加载是一个耗时的，等加载完毕了需要判定该控件是否和该url匹配
			Drawable drawable = loadFromMemory(url);// 从内存中加载
			if (drawable != null) {
				setImageSafe(view, url, drawable);// 如果内存中加载到了，直接设置图片
			} else {
				view.setImageResource(resImage);// 如果没加载到，设置默认图片，并异步加载
				asyncLoad(view, url);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/** 异步加载 */
	private static void asyncLoad(final ImageView view, final String url) {
		// 先创建一个runnable封装执行过程
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				mMapRuunable.remove(url);
				Drawable drawable = loadFromLocal(url, view);
				if (drawable == null) {
					drawable = loadFromNet(url, view);
				}
				if (drawable != null) {
					setImageSafe(view, url, drawable);
				}
			}
		};
		cancel(url);// 先取消这个url的下载
		mMapRuunable.put(url, runnable);// 记住这个runnable，以便后面取消
		mThreadPool.execute(runnable);// 执行任务
	}

	/** 取消下载 */
	public static void cancel(String url) {
		Runnable runnable = mMapRuunable.remove(url);// 根据url来获取指定的runnable
		if (runnable != null) {
			mThreadPool.cancel(runnable);// 从线程池中删除该任务，如果任务已经开始下载了，就无法删除
		}
	}

	/** 从内存中加载 */
	private static Drawable loadFromMemory(String url) throws Exception {
		Drawable drawable = mDrawableCache.get(url);
		if (drawable != null) {
			// 从内存中获取到了，需要重新放到内存队列的最后，以便满足LRC
			// 一般缓存算法有两种，第一是LFU，按使用次数来判定删除优先级，使用次数最少的最先删除
			// 还有一个就是LRC，就是按最后使用时间来判定删除优先级，最后使用时间越早的最先删除
			addDrawableToMemory(url, drawable);
		}
		return drawable;
	}

	/** 从本地设备中加载 */
	public static Drawable loadFromLocal(String url, ImageView view) {
		Bitmap bitmap = null;
		Drawable drawable = null;
		String path = FileUtils.getIconDir()
				+ FileUtils.convertUrlToFileName(url);
		FileInputStream fis = null;
		try {
			File parent = new File(FileUtils.getIconDir());
			if(!parent.exists()){
				parent.mkdirs();
			}
			// 获取流
			File file = new File(path);
			if(!file.exists()){
				file.createNewFile();
			}
			fis = new FileInputStream(file);
			if (fis != null) {
				if(view.getMeasuredWidth()!=0){
					bitmap = decodeSampledBitmapFromFd(path, view.getMeasuredWidth(), view.getMeasuredHeight());
				}else{
					bitmap = decodeSampledBitmapFromFd(path, 1000, 1000);
				}
//				if (typeBitmapSize == CHAT_BITMAP) {
//					if (mContext != null) {
//						bitmap = decodeSampledBitmapFromFd(path,
//								DisplayUtil.dip2px(mContext, CHAT_BITMAP),
//								DisplayUtil.dip2px(mContext, CHAT_BITMAP));
//					} else {
//						bitmap = decodeSampledBitmapFromFd(path, 70, 70);
//					}
//				} else if(typeBitmapSize == INDEX_BITMAP){
//						bitmap = decodeSampledBitmapFromFd(path, view.getMeasuredWidth(), view.getMeasuredHeight());
//				}else {
//					if (mContext != null) {
//						bitmap = decodeSampledBitmapFromFd(path,
//								DisplayUtil.dip2px(mContext, typeBitmapSize),
//								DisplayUtil.dip2px(mContext, typeBitmapSize));
//					} else {
//						bitmap = decodeSampledBitmapFromFd(path, view.getMeasuredWidth(), view.getMeasuredHeight());
//					}
//				}

			}
			if (null != bitmap) {// 把bitmap转换成drawable
				drawable = new BitmapDrawable(UIUtils.getResources(), bitmap);
				addDrawableToMemory(url, drawable);// 放到内存缓存队列中
			}
		} catch (OutOfMemoryError e) {
			mKeyCache.clear();
			mDrawableCache.clear();
			LogUtils.e(e);
		} catch (Exception e) {
			LogUtils.e(e);
		} finally {
			IOUtils.close(fis);
		}
		return drawable;
	}

	/** 从网络加载图片 */
	private static Drawable loadFromNet(String url, ImageView view) {
		/*
		 * HttpResult httpResult = HttpHelper.download(HttpHelper.URL +
		 * "image?name=" + url);
		 */
		try {
			HttpResult httpResult = HttpHelper.download(url);
			InputStream stream = null;
			if (httpResult == null
					|| (stream = httpResult.getInputStream()) == null) {// 请求网络
				return null;
			}
			String path = FileUtils.getIconDir()
					+ FileUtils.convertUrlToFileName(url);
			FileUtils.writeFile(stream, path, true);// 把网络下载保存在本地
			if (httpResult != null) {// 关闭网络连接
				httpResult.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadFromLocal(url, view);// 从本地加载
	}

	/** 添加到内存 */
	private static void addDrawableToMemory(String url, Drawable drawable)
			throws Exception {
		if (mKeyCache.remove(url)) {
			mTotalSize -= DrawableUtils.getDrawableSize(mDrawableCache
					.remove(url));
		}
		// 如果大于等于100张，或者图片的总大小大于应用总内存的四分之一先删除前面的
		while (mKeyCache.size() >= MAX_DRAWABLE_COUNT
				|| mTotalSize >= SystemUtils.getOneAppMaxMemory() / 4) {
			String firstUrl = mKeyCache.remove();
			Drawable remove = mDrawableCache.remove(firstUrl);
			mTotalSize -= DrawableUtils.getDrawableSize(remove);
		}
		mKeyCache.add(url);// 添加
		mDrawableCache.put(url, drawable);
		mTotalSize += DrawableUtils.getDrawableSize(drawable);
	}

	/** 设置给控件图片 */
	private static void setImageSafe(final ImageView view, final String url,
			final Drawable drawable) {
		if (drawable == null && view.getTag() == null) {
			return;
		}
		UIUtils.runInMainThread(new Runnable() {// 需要在主线程中设置
			@Override
			public void run() {
				Object tag;// 在主线程中判断，可以做到同步
				if ((tag = view.getTag()) != null) {
					String str = (String) tag;
					if (StringUtils.isEquals(str, url)) {// 检测如果url和控件匹配
						view.setImageDrawable(drawable);// 就进行图片设置
					}
				}
			}
		});
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	// 从sd卡上加载图片
	public static Bitmap decodeSampledBitmapFromFd(String pathName,
			int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeFile(pathName, options);
//		return createScaleBitmap(src, reqWidth, reqHeight);
		return src;
	}

	// 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
	private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
			int dstHeight) {
		try {
			Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight,
					false);
			if (src != dst) { // 如果没有缩放，那么不回收
				src.recycle(); // 释放Bitmap的native像素数组
			}
			return dst;
		} catch (Exception e) {
			return null;
		}
	}
}
