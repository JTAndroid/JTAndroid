package com.utils.file;

import java.io.File;
import java.io.FileOutputStream;

import com.tr.App;
import com.utils.log.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class Screenshot {

	/**
	 * 获取和保存当前屏幕的截图
	 */
	@SuppressWarnings({ "unused", "deprecation" })
	public static void GetandSaveCurrentImage(Activity mActivity) {
		// 1.构建Bitmap
		WindowManager windowManager = mActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();

		Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

		// 2.获取屏幕
		View decorview = mActivity.getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		Bmp = decorview.getDrawingCache();

		String SavePath = getSDCardPath() + "/gintong/image";

		// 3.保存Bitmap
		try {
			File path = new File(SavePath);
			String fileName = System.currentTimeMillis() + ".jpg";
			// 文件
			String filepath = SavePath + fileName;
			File file = new File(filepath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}

			// 其次把文件插入到系统图库
			MediaStore.Images.Media.insertImage(mActivity.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
			// 最后通知图库更新
			mActivity.sendBroadcast(new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
							+ filepath)));
			ToastUtil.showToast(mActivity, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取SDCard的目录路径功能
	 * 
	 * @return
	 */
	private static String getSDCardPath() {
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}
}
