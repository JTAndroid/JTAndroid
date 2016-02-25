package com.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Environment;
import android.text.TextUtils;

import com.utils.log.KeelLog;
import com.utils.string.StringUtils;

/**
 * @Filename FileHelper.java
 * @Author xuxinjian
 * @Date 2014-3-13
 * @description 文件帮助类，用于保存日志等操作。
 */

public class FileHelper {

    /**
     * write string to file with endcode
     *
     * @param sToSave
     * @param sFileName
     * @return
     */
    private static FileHelper fileHelper;

    private FileHelper() {
        super();
    }

    public static FileHelper getInstance() {
        if (fileHelper==null)
            fileHelper=new FileHelper();
        return fileHelper;
    }

    /**
     * delete share image cache
     */
    public static void deleteFile(String sFileName) {
        if (!TextUtils.isEmpty(sFileName)) {
            final File file=new File(sFileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * read string from file with decoce
     *
     * @param sFileName
     * @return
     */
    public static String ReadStringFromFile(String sFileName) {
        if (TextUtils.isEmpty(sFileName))
            return null;
        final StringBuffer sDest=new StringBuffer();
        final File f=new File(sFileName);
        if (!f.exists()) {
            return null;
        }
        try {
            FileInputStream is=new FileInputStream(f);
            BufferedReader br=new BufferedReader(new InputStreamReader(is));

            try {
                String data=null;
                while ((data=br.readLine())!=null) {
                    sDest.append(data);
                }
            } catch (IOException ioex) {
                if (KeelLog.DEBUG) {
                    ioex.printStackTrace();
                }
            } finally {
                is.close();
                is=null;
                br.close();
                br=null;
            }
        } catch (Exception ex) {
            if (KeelLog.DEBUG) {
                ex.printStackTrace();
            }
        }
        return sDest.toString().trim();
    }

    /**
     * 保存文件
     *
     * @param sToSave
     * @param sFileName
     * @param isAppend
     * @return
     */
    public static boolean WriteStringToFile(String content, String fileName, boolean isAppend) {
        return WriteStringToFile(content, "", fileName, isAppend);
    }

    public static boolean WriteStringToFile(String content, String directoryPath, String fileName, boolean isAppend) {
        // 去掉本地json缓存中notification消息提醒
        final String flag="\"notification\":{";
        if (!TextUtils.isEmpty(content)) {
            final int notificationStartIndex=content.indexOf(flag);
            if (notificationStartIndex>-1) {
                final int notificationEndIndex=content.indexOf("}", notificationStartIndex);
                final String str=content.substring(notificationStartIndex+flag.length(), notificationEndIndex);
                content=content.replace(str, "");
                if (KeelLog.DEBUG) {
                    KeelLog.e("去掉notification后的json:"+content);
                }
            }

            if (!TextUtils.isEmpty(directoryPath)) {// 是否需要创建新的目录
                final File threadListFile=new File(directoryPath);
                if (!threadListFile.exists()) {
                    threadListFile.mkdirs();
                }
            }
            boolean bFlag=false;
            final int iLen=content.length();
            final File file=new File(fileName);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                final FileOutputStream fos=new FileOutputStream(file, isAppend);
                byte[] buffer=new byte[iLen];
                try {
                    buffer=content.getBytes();
                    fos.write(buffer);
                    if (isAppend) {
                        fos.write("||".getBytes());
                    }
                    fos.flush();
                    bFlag=true;
                } catch (IOException ioex) {
                    if (KeelLog.DEBUG) {
                        ioex.printStackTrace();
                    }
                } finally {
                    fos.close();
                    buffer=null;
                }
            } catch (Exception ex) {
                if (KeelLog.DEBUG) {
                    ex.printStackTrace();
                }
            }
            return bFlag;
        }
        return false;
    }

    public static boolean writeJSONToFile(String content, String directoryPath, String fileName, boolean isAppend) {

        if (!TextUtils.isEmpty(directoryPath)) {// 是否需要创建新的目录
            final File threadListFile=new File(directoryPath);
            if (!threadListFile.exists()) {
                threadListFile.mkdirs();
            }
        }

        boolean bFlag=false;
        final int iLen=content.length();
        final File file=new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            final FileOutputStream fos=new FileOutputStream(file, isAppend);
            byte[] buffer=new byte[iLen];
            try {
                buffer=content.getBytes();
                fos.write(buffer);
                if (isAppend) {
//					fos.write("||".getBytes());
                }
                fos.flush();
                bFlag=true;
            } catch (IOException ioex) {
                if (KeelLog.DEBUG) {
                    ioex.printStackTrace();
                }
            } finally {
                fos.close();
                buffer=null;
            }
        } catch (Exception ex) {
            if (KeelLog.DEBUG) {
                ex.printStackTrace();
            }
        }
        return bFlag;
    }

    /**
     * 删除文件夹下所有文件
     *
     * @return
     */
    public static void deleteDirectoryAllFile(String directoryPath) {
        final File file=new File(directoryPath);
        delAll(file);
    }

    private static void delAll(File file) {
        if (!file.exists()) {
            return;
        }

        boolean rslt=true;// 保存中间结果
        if (!(rslt=file.delete())) {// 先尝试直接删除
            // 若文件夹非空。枚举、递归删除里面内容
            final File subs[]=file.listFiles();
            final int size=subs.length-1;
            for (int i=0; i<=size; i++) {
                if (subs[i].isDirectory())
                    delAll(subs[i]);// 递归删除子文件夹内容
                rslt=subs[i].delete();// 删除子文件夹本身
            }
//			rslt = file.delete();// 删除此文件夹本身
        }

        if (!rslt) {
            if (KeelLog.DEBUG) {
                KeelLog.e("无法删除:"+file.getName());
            }
            return;
        }
    }

    /**
     * 根据后缀名删除文件
     *
     * @param delpath    path of file
     * @param delEndName end name of file
     * @return boolean the result
     */
    public static boolean deleteEndFile(String delPath, String delEndName) {
        // param is null
        if (delPath==null||delEndName==null) {
            return false;
        }
        try {
            // create file
            final File file=new File(delPath);
            if (file!=null) {
                if (file.isDirectory()) {
                    // file list
                    String[] fileList=file.list();
                    File delFile=null;

                    // digui
                    final int size=fileList.length;
                    for (int i=0; i<size; i++) {
                        // create new file
                        delFile=new File(delPath+"/"+fileList[i]);
                        if (delFile!=null&&delFile.isFile()) {// 删除该文件夹下所有文件以delEndName为后缀的文件（不包含子文件夹里的文件）
//                        if (delFile != null) {// 删除该文件夹下所有文件以delEndName为后缀的文件（包含子文件夹里的文件）
                            deleteEndFile(delFile.toString(), delEndName);
                        } else {
                            // nothing
                        }
                    }
                } else if (file.isFile()) {

                    // check the end name
                    if (file.toString().contains(".")
                        &&file.toString().substring(
                        (file.toString().lastIndexOf(".")+1))
                        .equals(delEndName)) {
                        // file delete
                        file.delete();
                    }
                } else {

                }
            } else {

            }
        } catch (Exception ex) {
            if (KeelLog.DEBUG) {
                ex.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * 删除文件夹内所有文件
     *
     * @param delpath delpath path of file
     * @return boolean the result
     */
    public static boolean deleteAllFile(String delpath) {
        try {
            // create file
            final File file=new File(delpath);

            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {

                final String[] filelist=file.list();
                final int size=filelist.length;
                for (int i=0; i<size; i++) {

                    //create new file
                    final File delfile=new File(delpath+"/"+filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    } else if (delfile.isDirectory()) {
                        //digui
                        deleteFile(delpath+"/"+filelist[i]);
                    }
                }
                file.delete();
            }
        } catch (Exception ex) {
            if (KeelLog.DEBUG) {
                ex.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
    	
        if (StringUtils.isEmpty(sPath)) {
            return false;
        }
        
    	boolean flag;
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        if(files != null && files.length > 0){
        	for (int i = 0; i < files.length; i++) {
        		//删除子文件
        		if (files[i].isFile()) {
        			flag = deleteSingleFile(files[i].getAbsolutePath());
        			if (!flag) break;
        		} //删除子目录
        		else {
        			flag = deleteDirectory(files[i].getAbsolutePath());
        			if (!flag) break;
        		}
        	}
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteSingleFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
   
    /**
     * 检查SD卡是否存在
     * 
     * @return
     */
    public static boolean checkSDCard() {
        final String status = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(status)) {
            return true;
        }
        return false;
    }
}
