package com.tr.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

/**
 * @Description:文件缓存工具类基类
 * @author gushi
 *
 */

public class BaseCacheUtils {
	
	protected int MB = 1024 * 1024;
	protected static int FILESIZE = 4 * 1024;
	public static final String SAVE_FILE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	// IM文件保存路径
	public static final String SAVE_FILE_PATH_DIRECTORY_IM = "/imcache/";

	// 写入失败
	public static final int FILE_ERROR_WRITE = -2;
	// 文件错误
	public static final int FILE_ERROR = -1;
	// 此次操作执行建立
	public static final int FILE_CREATE = 0;
	// 文件已经建立不为空
	public static final int FILE_EXISTS = 1;
	// 文件已经建立为空
	public static final int FILE_EXISTS_NULL = 2;

	// 文件后缀
	public static final String FILE_SUFFIX = ".dat";

	public static int checkFileStatus(String dirPath, String fileName,
			String suffix) {
		int fileStatus;
		String filePath = checkFilePath(dirPath, fileName, suffix);

		if (filePath == null) {
			return fileStatus = FILE_ERROR;
		}

		try {
			File dirFile = new File(dirPath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File mFile = new File(filePath);
			if (!mFile.exists()) {
				fileStatus = FILE_CREATE;
				mFile.createNewFile();
			} else {
				long size = mFile.length();
				if (size > 0) {
					fileStatus = FILE_EXISTS;
				} else {

					fileStatus = FILE_EXISTS_NULL;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return FILE_ERROR;
		}
		return fileStatus;

	}

	public static int checkFileStatus(String dirPath, String fileName) {
		return checkFileStatus(dirPath, fileName, null);
	}

	public static String checkFilePath(String dirPath, String fileName,
			String suffix) {
		if (!TextUtils.isEmpty(dirPath) && !TextUtils.isEmpty(fileName)) {
			if (suffix != null) {
				return dirPath + "/" + fileName + suffix;
			} else {
				return dirPath + "/" + fileName + FILE_SUFFIX;
			}
		}
		return null;
	}

	public static String checkFilePath(String dirPath, String fileName) {
		return checkFilePath(dirPath, fileName, null);
	}

	private int freeSpaceOnSd() {
		final StatFs stat = new StatFs(Environment
				.getExternalStorageDirectory().getPath());
		final double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	public static String write2SDFromInput(String path, String fileName,
			InputStream input, String suffix) {

		File file = null;

		OutputStream output = null;
		String tempPath = null;
		try {
			if (checkFileStatus(path, fileName, suffix) == FILE_ERROR) {
				return null;
			}
			tempPath = checkFilePath(path, fileName, suffix);
			file = new File(tempPath);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[FILESIZE];
			int size = 0;
			while ((size = input.read(buffer)) != -1) {
				if (size > 0)
					output.write(buffer, 0, size);
			}
			output.flush();

		} catch (Exception e) {
			e.printStackTrace();
			if (tempPath != null)
				deleteFileOld(tempPath);
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return file.getAbsolutePath();
	}

	/**
	 * 删除文件
	 * */
	public static boolean deleteFileOld(String dirPath, String fileName) {

		boolean isOK = true;
		String filePath = dirPath + "/" + fileName + FILE_SUFFIX;
		try {
			File dirFile = new File(filePath);
			if (dirFile.exists()) {
				dirFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			isOK = false;
		}
		return isOK;
	}

	public static boolean deleteFileOld(String fullPath) {

		boolean isOK = true;
		try {
			File dirFile = new File(fullPath);
			if (dirFile.exists()) {
				dirFile.delete();
			}
		} catch (Exception e) {

			isOK = false;
		}
		return isOK;
	}

	public static void writeObject(String filePath, String fileName,
			Object mObject) {

		String path = checkFilePath(filePath, fileName);
		if (path != null) {
			int fileStatus = checkFileStatus(filePath, fileName);
			writeObject(path, mObject, fileStatus);
		}

	}

	public static void writeObject(String path, Object mObject, int fileStatus) {

		if (!TextUtils.isEmpty(path) && mObject != null
				&& fileStatus != FILE_ERROR) {
			try {

				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(path));
				out.writeObject(mObject); // !!!!!!�����������д���ϡ�����
				out.close();
			} catch (Exception e) {

			}

		}

	}

	/**
	 * �������л�
	 * */
	public static Object readObject(String filePath, String fileName) {

		String path = checkFilePath(filePath, fileName);
		if (path != null) {
			int fileStatus = checkFileStatus(filePath, fileName);
			return readObject(path, fileStatus);
		}
		return null;
	}

	public static Object readObject(String path, int fileStatus) {
		if (!TextUtils.isEmpty(path) && fileStatus == FILE_EXISTS) {
			try {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(path));
				Object mObject = in.readObject();
				in.close();
				return mObject;
			} catch (Exception e) {

			}
		}
		return null;
	}

	public static void writeObjectAppend(String filePath, String fileName,
			Object mObject) {
		String path = checkFilePath(filePath, fileName);
		if (path != null && mObject != null) {
			int fileStatus = checkFileStatus(filePath, fileName);
			writeObjectAppend(path, mObject, fileStatus);
		}
	}

	public static ArrayList readObjectAppend(String filePath, String fileName) {

		String path = checkFilePath(filePath, fileName);
		if (path != null) {
			int fileStatus = checkFileStatus(filePath, fileName);
			return readObjectAppend(path, fileStatus);
		}
		return null;

	}

	public static void writeObjectListAppend(String filePath, String fileName,
			List objectList, boolean append) {

		String path = checkFilePath(filePath, fileName);

		if (path != null && objectList != null && objectList.size() > 0) {
			int fileStatus = checkFileStatus(filePath, fileName);
			writeObjectListAppend(path, objectList, fileStatus, append);
		}

	}

	public static void writeObjectAppend(String path, Object mObject,
			int fileStatus) {
		if (!TextUtils.isEmpty(path) && mObject != null
				&& fileStatus != FILE_ERROR) {
			try {
				FileOutputStream fo = new FileOutputStream(path, true);
				ObjectOutputStream os = new ObjectOutputStream(fo);
				if (fileStatus == FILE_EXISTS) {
					long pos = 0;
					pos = fo.getChannel().position() - 4;
					fo.getChannel().truncate(pos);
				}
				os.writeObject(mObject);
				os.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public static ArrayList readObjectAppend(String path, int fileStatus) {
		if (fileStatus == FILE_EXISTS) {
			ArrayList objectList = new ArrayList();
			try {
				FileInputStream fin = new FileInputStream(path);
				ObjectInputStream in = new ObjectInputStream(fin);
				while (fin.available() > 0) {
					objectList.add(in.readObject());
				}
				return objectList;
			} catch (Exception e) {
				e.printStackTrace();

				// deleteFileOld(path);
				disposeException(path, objectList);
			}
		}
		return null;
	}

	public static void writeObjectListAppend(String path, List objectList,
			int fileStatus, boolean append) {
		if (!TextUtils.isEmpty(path) && objectList != null
				&& objectList.size() > 0 && fileStatus != FILE_ERROR) {
			try {
				FileOutputStream fo = new FileOutputStream(path, append);
				ObjectOutputStream os = new ObjectOutputStream(fo);
				if (fileStatus == FILE_EXISTS && append) {
					long pos = 0;
					pos = fo.getChannel().position() - 4;
					fo.getChannel().truncate(pos);
				}
				for (Object object : objectList) {
					os.writeObject(object);
				}
				os.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void disposeException(String path, List objectList) {

		writeObjectListAppend(path, objectList, FILE_EXISTS, false);
	}

}
