package com.tr.ui.people.model;

import java.io.Serializable;


/**
 * 文件存放记录表
 * @author liu
 * 创建时间：2012-2-2 10:38:41
 */
/**
 * @author liu
 * 
 */
public class FileIndex implements Serializable {

	/**
     * 
     */
	public static final long serialVersionUID = 6755175107258258053L;
	public String id;// 主键
	public String filePath;// 文件存放的物理路径
	public String fileTitle;// 源文件的名称
	public long fileSize; // 文件大小
	public boolean status; //文件状态
	public long author; //创建人
	public String md5; // 加密形式
	public String taskId; //taskId
	public String ctime; //创建时间
	public int moduleType; //类型
	public String authorName; //创建人姓名
	public String crc32;      //解压缩
	
}
