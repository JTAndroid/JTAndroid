package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.utils.string.StringUtils;

import android.content.Intent;
import android.net.Uri;

/**
 * @ClassName:     JTFile.java
 * @Description:   附件对象
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-10
 * @LastEdit       2014-04-10
 */
public class JTFile2 implements Serializable{
	
	private String fileName;//文件名
	
	private long fileSize;//文件大小
	
	private String url;//文件地址
	
	private String suffixName;//jpg,png,amr,pdf等
	
	private String type;//0-video,1-audio,2-file,3-image,4-other
	
	private String moduleType;//0:需求、1：业务需求、2：公司客户、3：公司项目、4：会员、5：名片 、6 公司名片 、7资讯、8客户、9人脉分享 、10机构

	private String taskId;//附件索引
	
	public JTFile toJTfile(){
		JTFile jt=new JTFile();
		jt.mFileName=fileName;
		jt.mFileSize=fileSize;
		jt.mUrl=url;
		jt.mSuffixName=suffixName;
		if(!StringUtils.isEmpty(type)){
			jt.setmType(Integer.parseInt(type));
		}
		if(!StringUtils.isEmpty(moduleType)){
			jt.mModuleType=Integer.parseInt(moduleType);
		}
		
		jt.mTaskId=taskId;
		return jt;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
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
	
	
	
}
