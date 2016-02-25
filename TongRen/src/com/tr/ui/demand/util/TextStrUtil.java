package com.tr.ui.demand.util;

import java.util.List;

import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.tr.model.demand.DemandData;
import com.tr.model.demand.LableData;
import com.tr.model.knowledge.UserCategory;
import com.tr.ui.communities.model.Label;

/**
 * @ClassName: TextStrUtil.java
 * @author fxtx
 * @Date 2015年3月18日 下午12:06:05
 * @Description: 将文本处理输出
 */
public class TextStrUtil {
	public static void setHtmlText(TextView text, String text1, String text2) {
		String st = "<font color='#3c6fa9'>%s: </font><font color='#bfbfbf'>%s</font>";
		text.setText(Html.fromHtml(String.format(st, text1, text2)));
	}
	
	/**
	 * 处理 XX（）类型的文本
	 * 
	 * @param text
	 * @param num
	 * @return
	 */
	public static String getCommentNum(String text, int num) {
		if (num == 0) {
			return text;
		} else {
			return String.format("%s(%d)", text,num);
		}
	}

	/**
	 * 将文本进行个数统计并限制
	 * 
	 * @param maxLength
	 * @param strings
	 * @return
	 */
	public static String getStringSize(int maxLength, List<String> strings) {
		StringBuffer sb = new StringBuffer();
		boolean isSelect = false;
		for (String data : strings) {
			if (isSelect) {
				sb.append(",");
			}
			isSelect = true;
			sb.append(data);
		}
		// 判断 当前是否超过最高限
		if (sb.length() >= maxLength) {
			String value = sb.substring(0, maxLength - 1);
			sb = new StringBuffer();
			sb.append(value);
			sb.append("...");
			sb.append(" 等");
			sb.append(strings.size());
			sb.append("项");
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * @param maxLength
	 * @param demands
	 * @return
	 */
	public static String getDemandDataSize(int maxLength,List<DemandData> demands){
		if(demands==null)
			return "";
		StringBuffer sb = new StringBuffer();
		boolean isSelect = false;
		for (DemandData data:demands) {
			if (isSelect) {
				sb.append(",");
			}
			isSelect = true;
			sb.append(data.name);
		}
		// 判断 当前是否超过最高限
		if (sb.length() >= maxLength) {
			String value = sb.substring(0, maxLength - 1);
			sb = new StringBuffer();
			sb.append(value);
			sb.append("...");
			sb.append(" 等");
			sb.append(demands.size());
			sb.append("项");
		}
		return sb.toString();
	}
	
	/**
	 * 最后的名称
	 * @param maxLength
	 * @param demands
	 * @return
	 */
	public static String getDemandDataAreaSize(int maxLength,List<DemandData> demands){
		if(demands==null)
			return "";
		StringBuffer sb = new StringBuffer();
		boolean isSelect = false;
		for (DemandData data:demands) {
			if (isSelect) {
				sb.append(",");
			}
			isSelect = true;
			sb.append(data.lastName());//最后的名称
		}
		// 判断 当前是否超过最高限
		if (sb.length() >= maxLength) {
			String value = sb.substring(0, maxLength - 1);
			sb = new StringBuffer();
			sb.append(value);
			sb.append("...");
			sb.append(" 等");
			sb.append(demands.size());
			sb.append("项");
		}
		return sb.toString();
	}
	/**
	 * 显示标签
	 * @return
	 */
	public static String getLableDataSize(int maxLength,List<LableData> lables){
		if(lables==null)
			return "";
		StringBuffer sb = new StringBuffer();
		boolean isSelect = false;
		for (LableData data:lables) {
			if (isSelect) {
				sb.append(",");
			}
			isSelect = true;
			sb.append(data.tag);
		}
		// 判断 当前是否超过最高限
		if (sb.length() >= maxLength) {
			String value = sb.substring(0, maxLength - 1);
			sb = new StringBuffer();
			sb.append(value);
			sb.append("...");
			sb.append(" 等");
			sb.append(lables.size());
			sb.append("项");
		}
		return sb.toString();
	}
	/**
	 * 显示标签
	 * @return
	 */
	public static String getComunityLableDataSize(int maxLength,List<Label> lables){
		if(lables==null)
			return "";
		StringBuffer sb = new StringBuffer();
		boolean isSelect = false;
		for (Label data:lables) {
			if (isSelect) {
				sb.append(",");
			}
			isSelect = true;
			sb.append(data.getName());
		}
		// 判断 当前是否超过最高限
		if (sb.length() >= maxLength) {
			String value = sb.substring(0, maxLength - 1);
			sb = new StringBuffer();
			sb.append(value);
			sb.append("...");
			sb.append(" 等");
			sb.append(lables.size());
			sb.append("项");
		}
		return sb.toString();
	}
	/**
	 * 显示目录
	 * @return
	 */
	public static String getCategoryDataSize(int maxLength,List<UserCategory> categorys){
		if(categorys==null)
			return "";
		StringBuffer sb = new StringBuffer();
		boolean isSelect = false;
		for (UserCategory data:categorys) {
			if (isSelect) {
				sb.append(",");
			}
			isSelect = true;
			sb.append(data.getCategoryname());
		}
		// 判断 当前是否超过最高限
		if (sb.length() >= maxLength) {
			String value = sb.substring(0, maxLength - 1);
			sb = new StringBuffer();
			sb.append(value);
			sb.append("...");
			sb.append(" 等");
			sb.append(categorys.size());
			sb.append("项");
		}
		return sb.toString();
	}
	/**
	 * 拼接id字段
	 * @param strings
	 * @param fh
	 * @return
	 */
	public static String getStringAppend(List<String> strings, String fh) {
		StringBuffer sb = new StringBuffer();
		boolean isSelect = false;
		for (String data : strings) {
			if (isSelect) {
				sb.append(fh);
			}
			isSelect = true;
			sb.append(data);
		}
		return sb.toString();
	}
	/**
	 * 目录信息的id 拼接
	 * @param category
	 * @param fh
	 * @return
	 */
	public static String getCategoryAppend(List<UserCategory>category,String fh){
		StringBuffer sb = new StringBuffer();
		boolean isSelect = false;
		for (UserCategory data : category) {
			if (isSelect) {
				sb.append(fh);
			}
			isSelect = true;
			sb.append(data.getId());
		}
		return sb.toString();
	}
	
	//递归获取用户目录名称
	public static  String checkCategoryname(UserCategory userCategory){
		if(userCategory.getListUserCategory()==null || userCategory.getListUserCategory().size()<=0){
			return userCategory.getCategoryname() ;
		}else{
			return checkCategoryname(userCategory.getListUserCategory().get(0)) ;
		}
		
	}
	//递归获取用户目录主键
	public static String checkCategoryId(UserCategory userCategory){
		if(userCategory.getListUserCategory()==null || userCategory.getListUserCategory().size()<=0){
			return String.valueOf(userCategory.getId());
		}else{
			return checkCategoryId(userCategory.getListUserCategory().get(0)) ;
		}
		
	}
	//判读是否是视频路径
	public static boolean isVideo(String fileUrl){
		boolean isVideo = false ;
		if(!TextUtils.isEmpty(fileUrl)){
			isVideo = fileUrl
					.matches("^.*?\\.(MP4|mp4|avi|rm|rmvb|mov|3gp)$") ;
		}
		return isVideo ;
	}
	//最大长度
	public static boolean maxLength(String checkStr,Integer maxLength){
		boolean flag = false ;
		if(null!=maxLength && null!=checkStr){
			Integer nowLength = checkStr.length() ;
			nowLength+=checkStr.replaceAll("[x00-xff]", "").length();
			return maxLength.intValue()>=nowLength.intValue() ;
		}
		return flag ;
	}
	
}
