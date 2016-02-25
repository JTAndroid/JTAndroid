package com.tr.model.conference;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.tr.model.obj.JTFile;

/**
 * 
 * @author sunjianan
 * 
 */
public class JTFile2ForHY implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6530079891604509783L;

	public Long fileIndexId;
	public String fileName;
	public String url;
	public String suffixName;
	public String moduleType;
	public String taskId;
	public int fileSize;
	public String type;

	public JSONObject toJson() throws JSONException {

		JSONObject jObject = new JSONObject();
		jObject.put("fileIndexId", fileIndexId);
		jObject.put("url", url);
		jObject.put("suffixName", suffixName);
		jObject.put("type", type);
		jObject.put("fileName", fileName);
		jObject.put("fileSize", fileSize);
		jObject.put("moduleType", moduleType);
		jObject.put("taskId", taskId);
		return jObject;
	}
	/**文件包含*/
	public JTFile toJtfile() {
		String suffixName = getSuffixName();
		JTFile jtFile = new JTFile();
		jtFile.mTaskId = getTaskId();
		jtFile.mModuleType = -1;
		jtFile.mUrl =getUrl();
		jtFile.mLocalFilePath = getUrl();
		jtFile.reserved2 =fileIndexId+"";
		jtFile.mFileName = getFileName();
		
		String[] str = {"doc","docx","ppt", "pptx","pdf","txt","xls","xlsx","rar","zip","7z"};
		
		if ("amr".equals(suffixName)) {//音频
			jtFile.mType = 4;
		}else if ("avi".equals(suffixName)||"mp4".equals(suffixName) || "wmv".equals(suffixName)||"3gp".equals(suffixName)) {
			jtFile.mType = 2;//视频
		}else {
			for (int i = 0; i < str.length; i++) {
				if (str[i].equals(suffixName)) {
					jtFile.mType = 3;//附件
					break;
				}
			}
		}
		
		
		return jtFile;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
