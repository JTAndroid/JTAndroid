package com.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.tr.model.obj.Connections;
import com.utils.common.GlobalVariable;
import com.utils.string.StringUtils;
  
public class FileUtils {  
    /** 
     * sd卡的根目录 
     */  
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();  
    /** 
     * 手机的缓存根目录 
     */  
    private static String mDataRootPath = null;  

    public FileUtils(Context context){  
        mDataRootPath = context.getCacheDir().getPath();  
    }  
  
    /** 
     * 获取储存Image的目录 
     * @return 
     */  
    private String getStorageDirectory(){  
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?  
                mSdRootPath + GlobalVariable.FOLDER_NAME : mDataRootPath + GlobalVariable.FOLDER_NAME;  
    }  
      
    /** 
     * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录 
     * @param fileName  
     * @param bitmap    
     * @throws IOException 
     */  
    public void savaBitmap(String fileName, Bitmap bitmap) throws IOException{  
        if(bitmap == null){  
            return;  
        }  
        String path = getStorageDirectory();  
        File folderFile = new File(path);  
        if(!folderFile.exists()){  
            folderFile.mkdir();  
        }  
        File file = new File(path + File.separator + fileName);  
        file.createNewFile();  
        FileOutputStream fos = new FileOutputStream(file);  
        bitmap.compress(CompressFormat.JPEG, 100, fos);  
        fos.flush();  
        fos.close();  
    }  
      
    /** 
     * 从手机或者sd卡获取Bitmap 
     * @param fileName 
     * @return 
     */  
    public Bitmap getBitmap(String fileName){  
        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);  
    }  
      
    /** 
     * 判断文件是否存在 
     * @param fileName 
     * @return 
     */  
    public boolean isFileExists(String fileName){  
        return new File(getStorageDirectory() + File.separator + fileName).exists();  
    }  
      
    /** 
     * 获取文件的大小 
     * @param fileName 
     * @return 
     */  
    public long getFileSize(String fileName) {  
        return new File(getStorageDirectory() + File.separator + fileName).length();  
    }  
      
      
    /** 
     * 删除SD卡或者手机的缓存图片和目录 
     */  
    public void deleteFile() {  
        File dirFile = new File(getStorageDirectory());  
        if(! dirFile.exists()){  
            return;  
        }  
        if (dirFile.isDirectory()) {  
            String[] children = dirFile.list();  
            for (int i = 0; i < children.length; i++) {  
                new File(dirFile, children[i]).delete();  
            }  
        }  
          
        dirFile.delete();  
    } 
    
    
    /**
     * 删除arraylist里面的重复元素
     * @param tags
     * @return
     */
    public static  ArrayList<String> deleteRep(ArrayList<String> tags) {
    	for (int i = 0; i < tags.size(); i++) {
			for (int j = i+1; j < tags.size(); j++) {
				if (tags.get(i).equals(tags.get(j))) {
					tags.remove(tags.get(j));
				}
			}
		}
    	
		return tags;
		
	}
    
    
    /**
     * 删除重复元素
     * @param tags
     * @return
     */
    public static ArrayList<Connections> deleteRepList(ArrayList<Connections> tags) {
    	ArrayList<Connections> list = new ArrayList<Connections>();
    	for (Connections connections : tags) {
			if (!list.contains(connections)) {
				list.add(connections);
			}
		}
		return list;
		
	}
    
    /**
     * 删除string数组里面的重复元素
     * @param tags
     * @return
     */
    public static String[] deleteRepString(String[] tagString) {
    	ArrayList<String> tags = new ArrayList<String>();
    	for (int i = 0; i < tagString.length; i++) {
    		if (!tags.contains(tagString[i]) && !StringUtils.isEmpty(tagString[i])) {
    			tags.add(tagString[i]);
			}
		}
    	String[] ss = new String[tags.size()];
    	for (int i = 0; i < tags.size(); i++) {
			ss[i] = tags.get(i);
		}
    	
		return ss;
		
	}
}
