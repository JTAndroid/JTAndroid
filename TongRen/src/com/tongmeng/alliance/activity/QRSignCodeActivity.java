package com.tongmeng.alliance.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class QRSignCodeActivity extends JBaseActivity implements
		OnClickListener {

	// 界面
	ImageView qrImg;
	Button saveBtn;

	String activityId;
	int wigth;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.qrsigncode_save:
			Bitmap bitmap = ((BitmapDrawable) qrImg.getDrawable()).getBitmap();
			saveImageToGallery(this, bitmap);
			break;
		default:
			break;
		}
	}

	@Override
	public void initJabActionBar() {
		// TODO Auto-generated method stub
		setContentView(R.layout.qrsigncode);
		initTitle();
		qrImg = (ImageView) findViewById(R.id.qrsigncode_image);
		saveBtn = (Button) findViewById(R.id.qrsigncode_save);
		saveBtn.setOnClickListener(this);

		wigth = qrImg.getWidth();
		String url = "signInCode://activityId=" + activityId;
		createQRImage(url);
	}

	private void initTitle() {
		// TODO Auto-generated method stub
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "活动二维码",
				false, null, false, true);
	}

	public void createQRImage(String url) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, wigth, wigth, hints);
			int[] pixels = new int[wigth * wigth];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < wigth; y++) {
				for (int x = 0; x < wigth; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * wigth + x] = 0xff000000;
					} else {
						pixels[y * wigth + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(wigth, wigth,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, wigth, 0, 0, wigth, wigth);
			// 显示到一个ImageView上面
			qrImg.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(),
				"Boohee");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.parse("file://" + file.getAbsolutePath())));
	}
}
