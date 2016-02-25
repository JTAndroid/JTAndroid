package com.tr.ui.conference.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import android.os.Environment;
import android.text.TextUtils;

/**
 * 文件操作工具
 */
public class FileUtils {
	public static final String SDCARD = Environment.getExternalStorageDirectory().getPath();
	public static final String APP = "Gentong";
	public static final String IMAGE = SDCARD + "/" + APP + "/image/";
	public static final String VOICE = SDCARD + "/" + APP + "/voice/";
	public static final String BUFFER = SDCARD + "/" + APP + "/buffer/";

	public static final String MP3 = ".mp3";
	public static final String XML = ".xml";
	public static final String LRC = ".lrc";
	public static final String NRC = ".nrc";
	public static final String JPG = ".jpg";
	public static final String TXT = ".txt";
	public static final String PNG = ".png";
	public static final String APK = ".apk";
	public static final String DWL = ".dwl";
	public static final String AMR = ".amr";

	public static String getFilePath(String dir, String name, String tag) {
		File file = new File(dir + name + tag);
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		return file.getPath();
	}

	public static String getFilePath(String dir, Object name, String tag) {
		return getFilePath(dir, String.valueOf(name), tag);
	}

	/**
	 * 新建文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static File createDirectory(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 新建文件
	 * 
	 * @param path
	 * @return
	 */
	public static File createFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			return file;
		}
		File parent = file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		try {
			file.createNewFile();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 新建临时文件
	 * 
	 * @param prefix
	 *            文件名
	 * @param suffix
	 *            文件类型 ".txt"
	 * @param directory
	 *            根目录
	 * @return
	 */
	public static File createTempFile(String prefix, String suffix, File directory) {
		if (!directory.exists()) {
			directory.mkdirs();
		}
		try {
			return File.createTempFile(prefix, suffix, directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delFile(String path) {
		if (null == path || path.length() == 0)
			return true;
		File file = new File(path);
		if (file.exists() && file.isFile()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delFile(File file) {
		if (null != file && file.exists() && file.isFile()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delDirectory(String path) {
		File directory = new File(path);
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (File file : files) {
					if (!delDirectory(file))
						delFile(file);
				}
			}
			return directory.delete();
		}
		return false;
	}

	/**
	 * 
	 * 删除文件夹中的文件
	 * 
	 * @param path
	 * @param exceptFileList
	 * @return
	 */
	public static boolean delFileFromDirectory(String path, List<String> exceptFileList) {
		boolean find = false;
		String filename = "";
		File directory = new File(path);
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (File file : files) {
					find = false;
					if (exceptFileList != null && exceptFileList.size() > 0) {
						for (int i = 0; i < exceptFileList.size(); i++) {
							filename = exceptFileList.get(i);
							if (!TextUtils.isEmpty(filename) && filename.equalsIgnoreCase(file.getName())) {
								find = true;
							}
						}
					}
					if (find) {
						continue;
					} else {
						delFile(file);
					}
				}
			}
		}
		return false;
	}

	/**
	 * 删除目录中的文件(文件名部分匹配)
	 * 
	 * @param path
	 * @param exceptFileList
	 * @return
	 */
	public static boolean delFileFromDirectoryEx(String path, List<String> exceptFileList) {
		boolean find = false;
		String filename = "";
		File directory = new File(path);
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (File file : files) {
					find = false;
					if (exceptFileList != null && exceptFileList.size() > 0) {
						for (int i = 0; i < exceptFileList.size(); i++) {
							filename = exceptFileList.get(i);
							if (!TextUtils.isEmpty(filename) && file.getName().contains(filename)) {
								find = true;
							}
						}
					}
					if (find) {
						continue;
					} else {
						delFile(file);
					}
				}
			}
		}
		return false;
	}

	/*
	 * 清空文件夹
	 */
	public static boolean clearDirectory(String path) {
		File directory = new File(path);

		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();

			if (null != files) {
				for (File file : files) {
					if (!delDirectory(file))
						delFile(file);
				}
			}
		}

		return true;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delDirectory(File directory) {
		if (null != directory && directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (File file : files) {
					if (!delDirectory(file))
						delFile(file);
				}
			}
			return directory.delete();
		}
		return false;
	}

	/*
	 * 复制文件
	 */
	public static boolean copyFile(InputStream ins, OutputStream ops) {
		try {
			byte[] byBuffer = new byte[1024];

			int readLen = 0;
			while ((readLen = ins.read(byBuffer)) > 0) {
				ops.write(byBuffer, 0, readLen);
			}
			ops.flush();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ops != null) {
					ops.close();
				}

				if (ins != null) {
					ins.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/*
	 * 复制文件
	 */
	public static boolean copyFile(String strDest, String strSrc) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		File fileDest = null, fileSrc = null;

		try {
			fileSrc = new File(strSrc);
			fileDest = new File(strDest);

			byte[] byBuffer = new byte[1024];

			inputStream = new FileInputStream(fileSrc);
			outputStream = new FileOutputStream(fileDest);

			int readLen = 0;
			while ((readLen = inputStream.read(byBuffer)) > 0) {
				outputStream.write(byBuffer, 0, readLen);
			}
			outputStream.flush();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			FileUtils.delFile(fileDest);
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}

				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
			}
		}
		return false;
	}

	/*
	 * 文件更名
	 */
	public static void renameFile(String strDest, String strSrc) {
		File fileDest, fileSrc;

		fileSrc = new File(strSrc);
		fileDest = new File(strDest);

		if (fileDest.exists() && fileDest.isFile()) {
			fileDest.delete();
		}

		if (fileSrc.exists() && fileSrc.isFile() && (!fileDest.exists())) {
			fileSrc.renameTo(fileDest);
		}
	}

	/*
	 * 是否有效文件
	 */
	public static boolean exists(String strPath) {
		File file;
		boolean bRet = false;

		try {
			file = new File(strPath);
			if (file.isFile() && file.exists()) {
				bRet = true;
			}
		} catch (Exception e) {
			bRet = false;
		}

		return bRet;
	}

	/*
	 * 保存文本文件
	 */
	public static boolean saveTextFile(String strPath, String strText) {
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		boolean bRet = true;
		File file = null;

		try {
			delFile(strPath);

			file = new File(strPath);

			outputStream = new FileOutputStream(file);
			outputStreamWriter = new OutputStreamWriter(outputStream);

			outputStreamWriter.write(strText);
			outputStreamWriter.close();
		} catch (Exception e) {
			bRet = false;
		}

		return bRet;
	}

	/*
	 * 保存文本文件
	 */
	public static boolean appendTextFile(String strPath, String strText) {
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		boolean bRet = true;
		File file = null;

		try {
			file = new File(strPath);

			outputStream = new FileOutputStream(file, true);
			outputStreamWriter = new OutputStreamWriter(outputStream);

			outputStreamWriter.write("\n");
			outputStreamWriter.write(strText);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (Exception e) {
			bRet = false;
		}

		return bRet;
	}

	/*
	 * 读取文本文件
	 */
	public static String readTextFile(String strPath) {
		InputStream inputStream = null;
		BufferedInputStream bufferedInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;

		String strText = "";
		StringBuffer stringBuffer = null;
		File file = null;

		try {
			file = new File(strPath);

			if (file.exists() && (file.isFile())) {
				stringBuffer = new StringBuffer("");

				inputStream = new FileInputStream(file);
				bufferedInputStream = new BufferedInputStream(inputStream);

				inputStreamReader = new InputStreamReader(bufferedInputStream, "UTF-8");

				bufferedReader = new BufferedReader(inputStreamReader);

				strText = bufferedReader.readLine();

				while (strText != null) {
					stringBuffer.append(strText);

					strText = bufferedReader.readLine();
				}

				bufferedReader.close();

				strText = stringBuffer.toString();
			} else {
				strText = "";
			}
		} catch (Exception e) {
			strText = "";
		}

		return strText;
	}

	/*
	 * 获取SD卡路径
	 */
	public static String getSDPath() {
		String strPath = "";

		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				strPath = Environment.getExternalStorageDirectory().getPath();
			}
		} catch (Exception e) {
			strPath = "";
		}

		return strPath;
	}

	/*
	 * 创建空文件
	 */
	public static File createDummyFile(String strPath, int nLength) {
		FileOutputStream fileOutputStream = null;
		File fileDummy = null;
		int nCounter = 0, nWrite = 0;
		byte byDummy[] = null;
		boolean bCreate = true;

		try {
			fileDummy = new File(strPath);
			byDummy = new byte[1024];

			if (fileDummy.exists() && fileDummy.isFile()) {
				if (fileDummy.length() == nLength) {
					bCreate = false;
				} else {
					fileDummy.delete();
				}
			}

			if (bCreate) {
				fileOutputStream = new FileOutputStream(fileDummy);

				for (int i = 0; i < byDummy.length; ++i) {
					byDummy[i] = 0;
				}

				while (nCounter < nLength) {
					nWrite = (nLength - nCounter) > 1024 ? 1024 : nLength - nCounter;

					fileOutputStream.write(byDummy, 0, nWrite);

					nCounter += nWrite;
				}

				fileOutputStream.close();
			}
		} catch (Exception e) {
			fileDummy = null;
		}

		return fileDummy;
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return long 单位为M
	 * @throws Exception
	 */
	private static long getFolderSize(File file) {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	public static int getFolderSize(String strDirPath) {
		long size = 0;
		try {
			File file = new File(strDirPath);
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) (size / 1048576);
	}

	public static long getFileSize(String strFilePath) {
		long size = 0;
		try {
			File file = new File(strFilePath);
			size = file.length();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}
}
