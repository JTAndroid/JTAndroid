package com.tr.ui.cloudDisk.model;

import java.io.Serializable;
/**
 *   "jtFile":{
 			"id":"文件ID"
            "fileName":"文件名",
            "fileSize":"文件大小",
            "url":"文件地址",
            "suffixName":"jpg,png,amr,pdf等",
            "type":"0-视频(video),1-音频(audio),2-文件(file),3-图片(image),4-其它(other),5-网页链接,6-人脉(JTContact),7-knowledge知识,8-机构客户,9-机构用户(线上金桐网用户),10-个人用户(线上个人用户),11-需求,12-普通文本,13-knowledge2新知识,14-会议(conference)",
            "moduleType":"0:需求、1：业务需求、2：公司客户、3：公司项目、4：会员、5：名片 、6 公司名片 、7资讯、8客户、9人脉分享 、10机构",
            "taskId":"附件索引",
            "reserved1":"备用1",
            "reserved2":"备用2",
            "reserved3":"备用3"
    }
 */
public class UserDocument implements Serializable{
	public String id;
	public String fileName;
	public String fileSize;
	public String url;
	public String suffixName;
	public String type;
	public String moduleType;
	public String taskId;
	public String reserved1 = ""; // 备用字段 人脉和机构第二字段
	public String reserved2 = ""; // 备用字段，视频缩略图地址，语音文件时长
	public String reserved3 = ""; // 备用字段
}
