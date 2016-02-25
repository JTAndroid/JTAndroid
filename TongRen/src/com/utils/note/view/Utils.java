package com.utils.note.view;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

public class Utils {

	/*
	 * 复制单个文件
	 *
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 *
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 *
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				// 文件存在时
				InputStream inStream = new FileInputStream(oldPath);
				// 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					// 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}

	public static boolean string2File(String res, String filePath) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024]; // 字符缓冲区
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	public static String file2String(File file, String encoding) {
		BufferedReader reader = null;
		StringWriter writer = new StringWriter();
		String string;
		try {
			reader = new BufferedReader(new FileReader(file));
			// 将输入流写入输出流
			String line = reader.readLine();
			while (line != null) {
				writer.write(line);
				writer.write("\n");
				writer.flush();
				line = reader.readLine();
			}
			string = writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return string.trim();
	}

	public static String file2Base64String(File file) {
		FileInputStream reader = null;
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		String string = null;
		try {
			reader = new FileInputStream(file);
			byte[] bys = new byte[1024];
			int len;
			// 将输入流写入输出流
			while ((len = reader.read(bys)) != -1) {
				writer.write(bys, 0, len);
			}
			byte[] appicon = writer.toByteArray();
			string = Base64.encodeToString(appicon, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return string;
	}

	// dp转为px
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	// 手机px转为dp
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static Bitmap readBitmapAutoSize(Context context, Uri uri,
			int outWidth, int outHeight) {
		// outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		InputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = context.getContentResolver().openInputStream(uri);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(context, uri,
					outWidth, outHeight);
			Bitmap bitmap = BitmapFactory.decodeStream(bs, null, options);
			int minwidth = bitmap.getWidth() < outWidth ? outWidth : bitmap
					.getWidth();
			int minheight = bitmap.getHeight() < outHeight ? outHeight : bitmap
					.getHeight();
			return Bitmap.createScaledBitmap(bitmap, minwidth, minheight, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bs.close();
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static Bitmap readBitmapAutoWidth(Activity context, Uri uri,
			int outWidth,int outHeight) {
		InputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = context.getContentResolver().openInputStream(uri);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(context, uri,
					outWidth,outHeight);
			Bitmap bitmap = BitmapFactory.decodeStream(bs, null, options);
			int degree = getExifOrientation(getImageAbsolutePath(context, uri));
	        if(degree == 90 || degree == 180 || degree == 270){
	        	int w = bitmap.getWidth();  
	        	int h = bitmap.getHeight();  
	            Matrix matrix = new Matrix();
	            matrix.postRotate(degree);
	            return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	        }
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("sssssssss", e.toString());
		} finally {
			try {
				bs.close();
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static BitmapFactory.Options setBitmapOption(Context context,
			Uri uri, int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		// 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
		try {
			BitmapFactory.decodeStream(context.getContentResolver()
					.openInputStream(uri), null, opt);
			int outWidth = opt.outWidth; // 获得图片的实际高和宽
			int outHeight = opt.outHeight;
			opt.inDither = false;
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
			opt.inSampleSize = 1;
			// 设置缩放比,1表示原比例，2表示原来的四分之一....
			// 计算缩放比
			if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
				int sampleSize = (outWidth / width + outHeight / height) / 2;
				opt.inSampleSize = sampleSize;
			}

			opt.inJustDecodeBounds = false;// 最后把标志复原
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("ccccccccc", e.toString());
		}
		return opt;
	}

	public static Bitmap readBitmapAutoSize(Context context, String path,
			int outWidth, int outHeight) {
		// outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		BitmapFactory.Options options = setBitmapOption(context, path,
				outWidth, outHeight);
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		int degree = getExifOrientation(path);
        if(degree == 90 || degree == 180 || degree == 270){
            //Roate preview icon according to exif orientation
        	int w = bitmap.getWidth();
        	int h = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            if(outWidth != 0 && outHeight != 0){
            	bitmap = Bitmap.createBitmap(
            			bitmap,
            			0, 0, w, h, matrix, true);
            }else{
            	int[] bitmapSize = getBitmapSize(path);
            	bitmap = Bitmap.createBitmap(bitmap,0,0,bitmapSize[0],bitmapSize[1],matrix,true);
            	return bitmap;
            }
        }
		return Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);
	}
	
	public static int[] getBitmapSize(String path){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opt);
		int outWidth = opt.outWidth; // 获得图片的实际高和宽
		int outHeight = opt.outHeight;
		return new int[]{outWidth,outHeight};
	}

	private static BitmapFactory.Options setBitmapOption(Context context,
			String path, int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		// 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
		try {
			BitmapFactory.decodeFile(path, opt);
			int outWidth = opt.outWidth; // 获得图片的实际高和宽
			int outHeight = opt.outHeight;
			opt.inDither = false;
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
			opt.inSampleSize = 1;
			// 设置缩放比,1表示原比例，2表示原来的四分之一....
			// 计算缩放比
			if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
				int sampleSize = (outWidth / width + outHeight / height) / 2;
				opt.inSampleSize = sampleSize;
			}

			opt.inJustDecodeBounds = false;// 最后把标志复原
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return opt;
	}

	public static int getExifOrientation(String filepath) {
	       int degree = 0;
	       ExifInterface exif = null;

	       try {
	           exif = new ExifInterface(filepath);
	       } catch (IOException ex) {
	          // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
	       }

	       if (exif != null) {
	           int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
	           if (orientation != -1) {
	               // We only recognize a subset of orientation tag values.
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
	               default:
	                   break;
	               }
	           }
	       }
	       return degree;
	   }
	                   

	public static Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {

		Matrix m = new Matrix();
		m.setRotate(orientationDegree, (float) bm.getWidth() / 2,
				(float) bm.getHeight() / 2);
		float targetX, targetY;
		if (orientationDegree == 90) {
			targetX = bm.getHeight();
			targetY = 0;
		} else {
			targetX = bm.getHeight();
			targetY = bm.getWidth();
		}

		final float[] values = new float[9];
		m.getValues(values);
		float x1 = values[Matrix.MTRANS_X];
		float y1 = values[Matrix.MTRANS_Y];

		m.postTranslate(targetX - x1, targetY - y1);

		Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(),
				Bitmap.Config.ARGB_8888);
		Paint paint = new Paint();
		Canvas canvas = new Canvas(bm1);
		canvas.drawBitmap(bm, m, paint);

		return bm1;
	}

	/**
	 * 图片转成string
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String convertIconToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		byte[] appicon = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(appicon, Base64.DEFAULT);

	}

	/**
	 * string转成bitmap
	 * 
	 * @param st
	 */
	public static Bitmap convertStringToIcon(String st) {
		// OutputStream out;
		Bitmap bitmap = null;
		try {
			// out = new FileOutputStream("/sdcard/aa.jpg");
			byte[] bitmapArray;
			bitmapArray = Base64.decode(st, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
			// bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 删除某个文件夹下的所有文件夹和文件
	 * 
	 * @param delpath
	 *            String
	 * @return boolean
	 */
	public static boolean deletefile(String delpath) throws Exception {
		try {

			File file = new File(delpath);
			// 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
			if (!file.isDirectory()) {
				file.delete();
			} else if (file.isDirectory()) {
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File delfile = new File(delpath + "\\" + filelist[i]);
					if (!delfile.isDirectory()) {
						delfile.delete();
						System.out
								.println(delfile.getAbsolutePath() + "删除文件成功");
					} else if (delfile.isDirectory()) {
						deletefile(delpath + "\\" + filelist[i]);
					}
				}
				System.out.println(file.getAbsolutePath() + "删除成功");
				file.delete();
			}

		} catch (Exception e) {
			Log.e("homeworkapp", "deletefile() Exception:" + e.getMessage());
		}
		return true;
	}

	public static boolean delFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return true;
		}

		return file.delete();
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * delete all file
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	public static Bitmap getBitmap(String url) throws Exception {
		Bitmap bitmap = null;
		URL bitmapurl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) bitmapurl.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == 200) {
			InputStream inputStream = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(inputStream);
		}
		return bitmap;
	}

	public static String getFileExtention(String fileName) {
		int length = fileName.length();
		for (int i = length - 1; i >= 0; i--) {
			if (fileName.charAt(i) == '.') {
				return fileName.substring(i);
			}
		}
		return fileName;
	}

	private static long lastClickTime;

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
