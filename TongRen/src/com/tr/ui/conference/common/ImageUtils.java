package com.tr.ui.conference.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

/**
 * 图像实用类
 * 
 * @author rocksen
 */
public class ImageUtils {

	/**
	 * 图片字节
	 * 
	 * @param bmp
	 * @param needRecycle
	 * @return
	 */
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 图片是否为空
	 * 
	 * @param bitmap
	 * @return
	 */
	public static boolean isEmpty(Bitmap bitmap) {
		if (null == bitmap || bitmap.isRecycled()) {
			return true;
		}
		return false;
	}

	/**
	 * 图片旋转向左旋转90度后，按屏幕大小缩放
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap matrix(Bitmap bitmap, int width, int height) {
		int bWidth = bitmap.getWidth();
		int bHeight = bitmap.getHeight();

		try {
			// 旋转90度
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			Bitmap temp = Bitmap.createBitmap(bitmap, 0, 0, bWidth, bHeight, matrix, true);
			bitmap.recycle();

			// 缩放
			bitmap = Bitmap.createScaledBitmap(temp, height, width, true);
			if (null != bitmap && bitmap != temp) {
				temp.recycle();
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 计算样本大小
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * 计算初始样本大小
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		// 手机密度
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 计算样本大小
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee a final image
			// with both dimensions larger than or equal to the requested height
			// and width.
			// inSampleSize = heightRatio < widthRatio ? heightRatio :
			// widthRatio;
			double d1 = (double) reqWidth / (double) width;
			double d2 = (double) reqHeight / (double) height;
			double d3 = (double) ((d1 + d2) / 2);
			inSampleSize = (int) Math.rint(d3);
			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			// final float totalPixels = width * height;
			//
			// // Anything more than 2x the requested pixels we'll sample down
			// // further
			// final float totalReqPixelsCap = reqWidth * reqHeight * 2;
			//
			// while (totalPixels / (inSampleSize * inSampleSize) >
			// totalReqPixelsCap) {
			// inSampleSize++;
			// }
		}
		return inSampleSize;
	}

	public static Bitmap getRoundBitmap(Bitmap bitmapSrc, int nWidth) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);

		Bitmap bitmapScaled = null;
		Bitmap bitmapDes = null;

		try {
			bitmapScaled = Bitmap.createScaledBitmap(bitmapSrc, nWidth, nWidth, true);

			bitmapDes = Bitmap.createBitmap(nWidth, nWidth, Bitmap.Config.ARGB_4444);
			Canvas canvas = new Canvas(bitmapDes);

			canvas.save();
			canvas.drawColor(Color.TRANSPARENT);

			// 绘图区域
			Path path = new Path();
			path.addCircle(nWidth / 2, nWidth / 2, nWidth / 2, Direction.CW);
			canvas.clipPath(path);

			canvas.drawBitmap(bitmapScaled, 0, 0, paint);

			// 白边
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(3.0f);
			paint.setColor(Color.WHITE);
			canvas.drawCircle(nWidth / 2, nWidth / 2, (float) (nWidth / 2 - 1.55), paint);
			canvas.restore();

			if (bitmapDes != null) {
				return bitmapDes.isRecycled() ? null : bitmapDes;
			} else {
				return null;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bitmapScaled != null && !bitmapScaled.isRecycled()) {
				if (bitmapDes != null && bitmapScaled == bitmapDes) {
				} else {
					bitmapScaled.recycle();
					bitmapScaled = null;
				}
				// System.gc();
				// System.gc();
			}
		}

		return null;
	}

	public static Bitmap getRoundBitmapEx(Bitmap bitmapSrc, int nWidth) {
		Bitmap bitmapScaled = null;
		Bitmap bitmapDes = null;

		if (bitmapSrc == null || bitmapSrc.isRecycled() || bitmapSrc.getWidth() <= 0) {
			return null;
		}

		try {
			bitmapScaled = Bitmap.createScaledBitmap(bitmapSrc, nWidth, nWidth, true);

			if (bitmapScaled == null || bitmapScaled.isRecycled() || bitmapScaled.getWidth() <= 0) {
				return null;
			}
			bitmapDes = Bitmap.createBitmap(nWidth, nWidth, Bitmap.Config.ARGB_4444);
			if (bitmapDes == null || bitmapDes.isRecycled() || bitmapDes.getWidth() <= 0) {
				return null;
			}
			Canvas canvas = new Canvas(bitmapDes);

			if (canvas != null) {
				canvas.save();
				canvas.drawColor(Color.TRANSPARENT);

				final int color = 0xff424242;
				final Paint paint = new Paint();
				final Rect rect = new Rect(0, 0, bitmapDes.getWidth(), bitmapDes.getHeight());
				final RectF rectF = new RectF(rect);
				final float roundPx = bitmapDes.getWidth() / 2;

				paint.setAntiAlias(true);
				canvas.drawARGB(0, 0, 0, 0);
				paint.setColor(color);
				canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

				paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
				canvas.drawBitmap(bitmapScaled, rect, rect, paint);

				// 白边
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(3.0f);
				paint.setColor(Color.WHITE);
				canvas.drawCircle(nWidth / 2, nWidth / 2, (float) (nWidth / 2 - 1.55), paint);
			}

			if (bitmapDes != null && bitmapDes.getWidth() > 0) {
				return bitmapDes.isRecycled() ? null : bitmapDes;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bitmapScaled != null && !bitmapScaled.isRecycled() && bitmapScaled != bitmapDes) {
				bitmapScaled.recycle();
				bitmapScaled = null;

				// System.gc();
				// System.gc();
			}
			if (bitmapSrc != null && !bitmapSrc.isRecycled() && bitmapSrc != bitmapDes) {
				bitmapSrc.recycle();
				bitmapSrc = null;

				// System.gc();
				// System.gc();
			}
		}

		return null;
	}

	public static Bitmap getRoundBitmapEx(Resources res, int resId, int nWidth) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);

		Bitmap bitmapSrc = null;
		Bitmap bitmapScaled = null;
		Bitmap bitmapDes = null;

		try {
			// 获取源图片的大小
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// opts.inPurgeable=true;
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			BitmapFactory.decodeResource(res, resId, opts);
			int srcWidth = opts.outWidth, srcHeight = opts.outHeight;
			int destWidth = 0, destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			double proportionate;
			destWidth = nWidth;
			ratio = (double) srcWidth / destWidth;
			proportionate = (double) (srcHeight / ratio);
			destHeight = (int) Math.rint(proportionate);
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			// newOpts.inSampleSize = (int) ratio;// + 1;
			newOpts.inSampleSize = calculateInSampleSize(opts, destWidth, destHeight);
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inPreferredConfig = Config.RGB_565;
			newOpts.inJustDecodeBounds = false;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;

			InputStream is = res.openRawResource(resId);
			bitmapSrc = BitmapFactory.decodeStream(is, null, newOpts);
			// bitmapSrc = BitmapFactory.decodeResource(res, resId, newOpts);
			bitmapScaled = Bitmap.createScaledBitmap(bitmapSrc, nWidth, nWidth, true);
			if (bitmapSrc != null && !bitmapSrc.isRecycled()) {
				bitmapSrc.recycle();
				bitmapSrc = null;
			}

			bitmapDes = Bitmap.createBitmap(nWidth, nWidth, Bitmap.Config.ARGB_4444);
			Canvas canvas = new Canvas(bitmapDes);

			canvas.save();
			canvas.drawColor(Color.TRANSPARENT);

			final int color = 0xff424242;
			final Rect rect = new Rect(0, 0, bitmapDes.getWidth(), bitmapDes.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = bitmapDes.getWidth() / 2;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmapScaled, rect, rect, paint);

			// 白边
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(3.0f);
			paint.setColor(Color.WHITE);
			canvas.drawCircle(nWidth / 2, nWidth / 2, (float) (nWidth / 2 - 1.55), paint);
			canvas.restore();

			if (bitmapDes != null) {
				return bitmapDes.isRecycled() ? null : bitmapDes;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bitmapSrc != null && !bitmapSrc.isRecycled() && bitmapSrc != bitmapDes) {
				bitmapSrc.recycle();
				bitmapSrc = null;
			}

			if (bitmapScaled != null && !bitmapScaled.isRecycled() && bitmapScaled != bitmapDes) {
				bitmapScaled.recycle();
				bitmapScaled = null;
			}

			// System.gc();
			// System.gc();
		}

		return null;
	}

	public static Bitmap getRoundBitmap(Resources res, int resId, int nWidth) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);

		Bitmap bitmapSrc = null;
		Bitmap bitmapScaled = null;
		Bitmap bitmapDes = null;

		try {
			// 获取源图片的大小
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// opts.inPurgeable=true;
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			BitmapFactory.decodeResource(res, resId, opts);
			int srcWidth = opts.outWidth, srcHeight = opts.outHeight;
			int destWidth = 0, destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			double proportionate;
			destWidth = nWidth;
			ratio = (double) srcWidth / destWidth;
			proportionate = (double) (srcHeight / ratio);
			destHeight = (int) Math.rint(proportionate);
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			// newOpts.inSampleSize = (int) ratio;// + 1;
			newOpts.inSampleSize = calculateInSampleSize(opts, destWidth, destHeight);
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inPreferredConfig = Config.RGB_565;
			newOpts.inJustDecodeBounds = false;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;

			InputStream is = res.openRawResource(resId);
			bitmapSrc = BitmapFactory.decodeStream(is, null, newOpts);
			// bitmapSrc = BitmapFactory.decodeResource(res, resId, newOpts);
			bitmapScaled = Bitmap.createScaledBitmap(bitmapSrc, nWidth, nWidth, true);
			if (bitmapSrc != null && !bitmapSrc.isRecycled()) {
				bitmapSrc.recycle();
				bitmapSrc = null;
			}

			bitmapDes = Bitmap.createBitmap(nWidth, nWidth, Bitmap.Config.ARGB_4444);
			Canvas canvas = new Canvas(bitmapDes);

			canvas.save();
			canvas.drawColor(Color.TRANSPARENT);

			// 绘图区域
			Path path = new Path();
			path.addCircle(nWidth / 2, nWidth / 2, nWidth / 2, Direction.CW);
			canvas.clipPath(path);

			canvas.drawBitmap(bitmapScaled, 0, 0, paint);

			// 白边
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(3.0f);
			paint.setColor(Color.WHITE);
			canvas.drawCircle(nWidth / 2, nWidth / 2, (float) (nWidth / 2 - 1.55), paint);
			canvas.restore();

			if (bitmapDes != null) {
				return bitmapDes.isRecycled() ? null : bitmapDes;
			} else {
				return null;
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bitmapSrc != null && !bitmapSrc.isRecycled() && bitmapSrc != bitmapDes) {
				bitmapSrc.recycle();
				bitmapSrc = null;
			}

			if (bitmapScaled != null && !bitmapScaled.isRecycled() && bitmapScaled != bitmapDes) {
				bitmapScaled.recycle();
				bitmapScaled = null;
			}

			// System.gc();
			// System.gc();
		}
		return null;
	}

	/*
	 * 获取头像
	 */
	public static Bitmap getRoundBitmap(Bitmap bitmapDes, Bitmap bitmapSrc, int nWidth, int nHeight) {
		Bitmap bitmapDesNew = null, bitmapSrcNew = null;
		Bitmap bitmap = null;
		int nPixelsDes[] = null, nPixelsSrc[] = null, nA = 0, nR = 0, nG = 0, nB = 0;

		try {
			nPixelsDes = new int[nWidth * nHeight];
			nPixelsSrc = new int[nWidth * nHeight];

			bitmapDesNew = Bitmap.createScaledBitmap(bitmapDes, nWidth, nHeight, true);

			bitmapSrcNew = Bitmap.createScaledBitmap(bitmapSrc, nWidth, nHeight, true);

			bitmapDesNew.getPixels(nPixelsDes, 0, nWidth, 0, 0, nWidth, nHeight);

			bitmapSrcNew.getPixels(nPixelsSrc, 0, nWidth, 0, 0, nWidth, nHeight);

			for (int i = 0; i < nPixelsSrc.length; ++i) {
				nA = Color.alpha(nPixelsDes[i]);
				nR = Color.red(nPixelsDes[i]);
				nG = Color.green(nPixelsDes[i]);
				nB = Color.blue(nPixelsDes[i]);

				if ((nA == 255) && (nR == 255) && (nG < 200) && (nB < 200)) {
					nPixelsDes[i] = nPixelsSrc[i];
				}
			}

			bitmap = Bitmap.createBitmap(nPixelsDes, nWidth, nHeight, Config.ARGB_4444);

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			bitmap = null;
		} finally {
			if (bitmapDesNew != null && (bitmapDesNew != bitmapDes) && (!bitmapDesNew.isRecycled())) {
				bitmapDesNew.recycle();
			}

			if (bitmapSrcNew != null && (bitmapSrcNew != bitmapSrc) && (!bitmapSrcNew.isRecycled())) {
				bitmapSrcNew.recycle();
			}

			if (bitmapDes != null && !bitmapDes.isRecycled()) {
				bitmapDes.recycle();
			}

			if (bitmapSrc != null && !bitmapSrc.isRecycled()) {
				bitmapSrc.recycle();
			}

			// System.gc();
			// System.gc();
		}

		if (bitmap != null) {
			return bitmap.isRecycled() ? null : bitmap;
		} else {
			return null;
		}
	}

	public static Bitmap getBitmapFromFile(String path, int width) {
		try {
			// 获取源图片的大小
			Bitmap bm;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// opts.inPurgeable=true;
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			bm = BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			double proportionate;
			if (-1 == width) {
				destWidth = srcWidth;
			} else {
				destWidth = width;
			}
			ratio = (double) srcWidth / destWidth;
			proportionate = (double) (srcHeight / ratio);
			destHeight = (int) Math.rint(proportionate);
			// 高度小于宽度,采用等高缩放
			// if (destHeight < width) {
			// destHeight = width;
			// ratio = (double) srcHeight / destHeight;
			// proportionate = (double) (srcWidth / ratio);
			// destWidth = (int) Math.rint(proportionate);
			// }
			// if (width > srcWidth) {
			// destWidth = srcWidth;
			// destHeight = srcHeight;
			// }
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			// newOpts.inSampleSize = (int) ratio;// + 1;
			newOpts.inSampleSize = calculateInSampleSize(opts, destWidth, destHeight);
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inPreferredConfig = Config.RGB_565;
			newOpts.inJustDecodeBounds = false;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;

			newOpts.inDither = false;
			newOpts.inTempStorage = new byte[16 * 1024];
			// 获取缩放后图片
			Bitmap srcBm = null;
			Bitmap destBm = null;

			File file = new File(path);
			FileInputStream fs = null;
			try {
				fs = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				fs = null;
				// e.printStackTrace();
			} catch (Exception e) {
				fs = null;
			}

			try {
				if (fs != null) {
					srcBm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, newOpts);
				}
				// srcBm = BitmapFactory.decodeFile(path, newOpts);
				// destBm = Bitmap.createScaledBitmap(srcBm, destWidth,
				// destHeight, true);
				if (srcBm != null && srcBm.getWidth() > 0) {
					return srcBm.isRecycled() ? null : srcBm;
				} else {
					return null;
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != srcBm && destBm != srcBm) {
					// srcBm.recycle();
					// System.gc();
					// System.gc();
				}
				if (fs != null) {
					try {
						fs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getBitmapFromFileEx(String path, int width) {
		try {
			// 获取源图片的大小
			Bitmap bm;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// opts.inPurgeable=true;
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			bm = BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			double proportionate;
			destWidth = width;
			ratio = (double) srcWidth / destWidth;
			proportionate = (double) (srcHeight / ratio);
			destHeight = (int) Math.rint(proportionate);
			// 高度小于宽度,采用等高缩放
			// if (destHeight < width) {
			// destHeight = width;
			// ratio = (double) srcHeight / destHeight;
			// proportionate = (double) (srcWidth / ratio);
			// destWidth = (int) Math.rint(proportionate);
			// }
			// if (width > srcWidth) {
			// destWidth = srcWidth;
			// destHeight = srcHeight;
			// }
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			// newOpts.inSampleSize = (int) ratio;// + 1;
			newOpts.inSampleSize = calculateInSampleSize(opts, destWidth, destHeight);
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inPreferredConfig = Config.RGB_565;
			newOpts.inJustDecodeBounds = false;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;

			newOpts.inDither = false;
			newOpts.inTempStorage = new byte[16 * 1024];
			// 获取缩放后图片
			Bitmap srcBm = null;
			Bitmap destBm = null;

			File file = new File(path);
			FileInputStream fs = null;
			try {
				fs = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				fs = null;
				// e.printStackTrace();
			} catch (Exception e) {
				fs = null;
			}

			try {
				if (fs != null) {
					srcBm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, newOpts);
				}
				if (srcBm != null && srcBm.getWidth() > 0) {
					// srcBm = BitmapFactory.decodeFile(path, newOpts);
					destBm = Bitmap.createScaledBitmap(srcBm, destWidth, destHeight, true);
				}
				if (destBm != null && destBm.getWidth() > 0) {
					return destBm.isRecycled() ? null : destBm;
				} else {
					return null;
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != srcBm && !srcBm.isRecycled() && destBm != srcBm) {
					srcBm.recycle();
					srcBm = null;
					// System.gc();
					// System.gc();
				}
				if (fs != null) {
					try {
						fs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getBitmapFromResources(Resources res, int id, int width) {
		if (null == res) {
			return null;
		}
		try {
			// 获取源图片的大小
			Bitmap bm;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// opts.inPurgeable=true;
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			bm = BitmapFactory.decodeResource(res, id, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			double proportionate;
			destWidth = width;
			ratio = (double) srcWidth / destWidth;
			proportionate = (double) (srcHeight / ratio);
			destHeight = (int) Math.rint(proportionate);
			// 高度小于宽度,采用等高缩放
			// if (destHeight < width) {
			// destHeight = width;
			// ratio = (double) srcHeight / destHeight;
			// proportionate = (double) (srcWidth / ratio);
			// destWidth = (int) Math.rint(proportionate);
			// }
			// if (width > srcWidth) {
			// destWidth = srcWidth;
			// destHeight = srcHeight;
			// }
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			// newOpts.inSampleSize = (int) ratio;// + 1;
			newOpts.inSampleSize = calculateInSampleSize(opts, destWidth, destHeight);
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inPreferredConfig = Config.RGB_565;
			newOpts.inJustDecodeBounds = false;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片

			Bitmap srcBm = null;
			Bitmap destBm = null;

			try {
				InputStream is = res.openRawResource(id);
				srcBm = BitmapFactory.decodeStream(is, null, newOpts);
				// srcBm = BitmapFactory.decodeResource(res, id, newOpts);
				// destBm = Bitmap.createScaledBitmap(srcBm, destWidth,
				// destHeight, true);

				if (srcBm != null) {
					return srcBm.isRecycled() ? null : srcBm;
				} else {
					return null;
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// if (null != srcBm && destBm != srcBm) {
				// srcBm.recycle();
				// System.gc();
				// System.gc();
				// }
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getBitmapFromResources(Resources res, int id, int width, Config config) {
		if (null == res) {
			return null;
		}
		try {
			// 获取源图片的大小
			Bitmap bm;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// opts.inPurgeable=true;
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			bm = BitmapFactory.decodeResource(res, id, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			double proportionate;
			destWidth = width;
			ratio = (double) srcWidth / destWidth;
			proportionate = (double) (srcHeight / ratio);
			destHeight = (int) Math.rint(proportionate);
			// 高度小于宽度,采用等高缩放
			// if (destHeight < width) {
			// destHeight = width;
			// ratio = (double) srcHeight / destHeight;
			// proportionate = (double) (srcWidth / ratio);
			// destWidth = (int) Math.rint(proportionate);
			// }
			// if (width > srcWidth) {
			// destWidth = srcWidth;
			// destHeight = srcHeight;
			// }
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = calculateInSampleSize(opts, destWidth, destHeight);
			// // inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inPreferredConfig = config;
			newOpts.inJustDecodeBounds = false;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			// // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片

			Bitmap srcBm = null;
			Bitmap destBm = null;

			try {
				InputStream is = res.openRawResource(id);
				srcBm = BitmapFactory.decodeStream(is, null, newOpts);
				// srcBm = BitmapFactory.decodeResource(res, id, newOpts);
				// destBm = Bitmap.createScaledBitmap(srcBm, destWidth,
				// destHeight, true);

				if (srcBm != null) {
					return srcBm.isRecycled() ? null : srcBm;
				} else {
					return null;
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// if (null != srcBm && destBm != srcBm) {
				// srcBm.recycle();
				// System.gc();
				// System.gc();
				// }
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getBitmapFromResourcesEx(Resources res, int id, int width) {
		if (null == res) {
			return null;
		}
		try {
			// 获取源图片的大小
			Bitmap bm;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// opts.inPurgeable=true;
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			bm = BitmapFactory.decodeResource(res, id, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			double proportionate;
			destWidth = width;
			ratio = (double) srcWidth / destWidth;
			proportionate = (double) (srcHeight / ratio);
			destHeight = (int) Math.rint(proportionate);
			// 高度小于宽度,采用等高缩放
			// if (destHeight < width) {
			// destHeight = width;
			// ratio = (double) srcHeight / destHeight;
			// proportionate = (double) (srcWidth / ratio);
			// destWidth = (int) Math.rint(proportionate);
			// }
			// if (width > srcWidth) {
			// destWidth = srcWidth;
			// destHeight = srcHeight;
			// }
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			// newOpts.inSampleSize = (int) ratio;// + 1;
			newOpts.inSampleSize = calculateInSampleSize(opts, destWidth, destHeight);
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inPreferredConfig = Config.ARGB_4444;
			newOpts.inJustDecodeBounds = false;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片

			Bitmap srcBm = null;
			Bitmap destBm = null;

			try {
				InputStream is = res.openRawResource(id);
				srcBm = BitmapFactory.decodeStream(is, null, newOpts);
				if (srcBm != null) {
					// srcBm = BitmapFactory.decodeResource(res, id, newOpts);
					destBm = Bitmap.createScaledBitmap(srcBm, destWidth, destHeight, true);
				}

				if (destBm != null) {
					return destBm.isRecycled() ? null : destBm;
				} else {
					return null;
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != srcBm && !srcBm.isRecycled() && destBm != srcBm) {
					srcBm.recycle();
					srcBm = null;
					// System.gc();
					// System.gc();
				}
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getFullFromResources(Resources res, int id, int width, int height) {
		if (null == res) {
			return null;
		}
		try {
			// 获取源图片的大小
			Bitmap bm;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// opts.inPurgeable=true;
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			bm = BitmapFactory.decodeResource(res, id, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			// 缩放的比例
			double ratio = 0.0;
			double ratio1 = (double) srcWidth / width;
			double ratio2 = (double) srcHeight / height;
			if (ratio1 > ratio2) {
				ratio = ratio1;
			} else {
				ratio = ratio2;
			}
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			// newOpts.inSampleSize = (int) ratio;// + 1;
			newOpts.inSampleSize = calculateInSampleSize(opts, width, height);
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inPreferredConfig = Config.RGB_565;
			newOpts.inJustDecodeBounds = false;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = height;
			newOpts.outWidth = width;
			// 获取缩放后图片

			Bitmap srcBm = null;
			Bitmap destBm = null;

			try {
				InputStream is = res.openRawResource(id);
				srcBm = BitmapFactory.decodeStream(is, null, newOpts);
				// srcBm = BitmapFactory.decodeResource(res, id, newOpts);
				// destBm = Bitmap.createScaledBitmap(srcBm, width, height,
				// true);

				if (srcBm != null) {
					return srcBm.isRecycled() ? null : srcBm;
				} else {
					return null;
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// if (null != srcBm && destBm != srcBm) {
				// srcBm.recycle();
				// System.gc();
				// System.gc();
				// }
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getFullScreenBitmap(Resources res, int id, int width, int height) {
		if (null == res) {
			return null;
		}
		try {
			// 获取源图片的大小
			Bitmap bm;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// opts.inPurgeable=true;
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			bm = BitmapFactory.decodeResource(res, id, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;

			// 比较图片与屏幕的宽高比例
			double ratioWidth = (srcWidth * 1.0) / width;
			double ratioHeight = (srcHeight * 1.0) / height;
			double baseValue = 0.001;
			if (ratioWidth - ratioHeight > 0.001) {
				destWidth = (int) (srcWidth / ratioHeight);
				destHeight = height;
			} else if (ratioWidth - ratioHeight < baseValue && ratioWidth - ratioHeight > -1 * baseValue) {
				destWidth = width;
				destHeight = height;
			} else {
				destWidth = width;
				destHeight = (int) (srcHeight / ratioWidth);
			}

			// 缩放的比例
			double ratio = 0.0;
			double proportionate;
			// destWidth = width;
			ratio = (double) srcWidth / destWidth;
			proportionate = (double) (srcHeight / ratio);
			// destHeight = (int) Math.rint(proportionate);
			// if (width > srcWidth) {
			// destWidth = srcWidth;
			// destHeight = srcHeight;
			// }
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			// newOpts.inSampleSize = (int) ratio;// + 1;
			newOpts.inSampleSize = calculateInSampleSize(opts, destWidth, destWidth);
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inPreferredConfig = Config.RGB_565;
			newOpts.inJustDecodeBounds = false;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片

			Bitmap srcBm = null;
			Bitmap destBm = null;

			try {
				InputStream is = res.openRawResource(id);
				srcBm = BitmapFactory.decodeStream(is, null, newOpts);
				// srcBm = BitmapFactory.decodeResource(res, id, newOpts);
				// destBm = Bitmap.createScaledBitmap(srcBm, destWidth,
				// destHeight, true);

				if (srcBm != null) {
					return srcBm.isRecycled() ? null : srcBm;
				} else {
					return null;
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// if (null != srcBm && destBm != srcBm) {
				// srcBm.recycle();
				// System.gc();
				// System.gc();
				// }
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap roundCorners(final Bitmap source, final float radius) {
		int width = source.getWidth();
		int height = source.getHeight();

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		Bitmap clipped = null;
		Bitmap rounded = null;

		try {
			clipped = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(clipped);
			canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

			rounded = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			canvas = new Canvas(rounded);
			canvas.drawBitmap(source, 0, 0, null);
			canvas.drawBitmap(clipped, 0, 0, paint);

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != source && !source.isRecycled()) {
				source.recycle();
			}
			if (null != clipped && !clipped.isRecycled()) {
				clipped.recycle();
			}

			// System.gc();
			// System.gc();
		}

		return rounded;
	}

	/**
	 * 保存Bitmap到文件
	 * 
	 * @param bitmap
	 * @param filePath
	 * @param percent
	 *            (0~100)
	 * @return
	 */
	public static boolean saveBitmapToFile(Bitmap bitmap, String filePath, int percent) {
		boolean save = false;
		if (null == bitmap) {
			return save;
		}
		try {
			File f = new File(filePath);
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, percent, out);
			out.flush();
			out.close();
			save = true;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return save;
	}

	public static Bitmap fixSnapshotOrder(Bitmap bitmap, String imagePath) {
		int orientation = readPictureDegree(imagePath);
		Bitmap tmpBitmap = null;
		if (orientation != 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(orientation);
			try {
				tmpBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return tmpBitmap;
		}
		return bitmap;
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 获取图片宽高
	 * 
	 * @param filePath
	 * @return
	 */
	public static Point getBitmapWidthAndHeight(String filePath) {
		Point pt = new Point();
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeFile(filePath, options); // 此时返回的bitmap为null
			pt.x = options.outWidth;
			pt.y = options.outHeight;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pt;
	}

	/**
	 * 获取圆形图片
	 * 
	 * @param bitmap
	 * @param width
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap bitmap, int width) {
		Bitmap destBitmap = null;

		try {
			Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap, width, width, true);
			destBitmap = Bitmap.createBitmap(smallBitmap.getWidth(), smallBitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(destBitmap);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, smallBitmap.getWidth(), smallBitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = ((float) smallBitmap.getWidth()) / 2;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(smallBitmap, rect, rect, paint);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			destBitmap = null;
		} catch (Exception e) {
			e.printStackTrace();
			destBitmap = null;
		}
		return destBitmap;
	}

	public static Bitmap getResBitmap(Resources res, int id, int width) {
		Bitmap bm;

		// 获取缩放后图片

		Bitmap srcBm = null;

		Bitmap destBitmap = null;

		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// opts.inPurgeable=true;
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			bm = BitmapFactory.decodeResource(res, id, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;

			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			// newOpts.inSampleSize = (int) ratio;// + 1;
			newOpts.inSampleSize = 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inPreferredConfig = Config.RGB_565;
			newOpts.inJustDecodeBounds = false;
			newOpts.inPurgeable = true;
			newOpts.inInputShareable = true;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = srcHeight;
			newOpts.outWidth = srcWidth;

			InputStream is = res.openRawResource(id);
			srcBm = BitmapFactory.decodeStream(is, null, newOpts);

			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			double proportionate;
			destWidth = width;
			ratio = (double) srcWidth / destWidth;
			proportionate = (double) (srcHeight / ratio);
			destHeight = (int) Math.rint(proportionate);

			Bitmap smallBitmap = Bitmap.createScaledBitmap(srcBm, destWidth, destHeight, true);
			destBitmap = Bitmap.createBitmap(smallBitmap.getWidth(), smallBitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(destBitmap);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, smallBitmap.getWidth(), smallBitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = ((float) smallBitmap.getWidth()) / 2;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(smallBitmap, rect, rect, paint);
			recycleBitmap(srcBm);
			recycleBitmap(smallBitmap);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			destBitmap = null;
		} catch (Exception e) {
			e.printStackTrace();
			destBitmap = null;
		}
		return destBitmap;
	}

	public static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}
}
