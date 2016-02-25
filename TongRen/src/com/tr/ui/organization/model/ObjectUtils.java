package com.tr.ui.organization.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
* <p>Title: ObjectUtils.java<／p> 
* <p>Description: 对象工具类<／p> 
* @author wfl
* @date 2015-1-23 
* @version 1.0
 */
public class ObjectUtils {

	  public static String  arraysToString(Object[] arrays){
		  String str="";
		  if(arrays!=null&&arrays.length>0){
			  int len=arrays.length;
			   for(int i=0;i<len;i++){
				    if(i!=len-1){
				    	str=str+arrays[i]+",";
				    }else{
				    	str=str+arrays[i];
				    }
			   }
		  }
		  return str;
	  }
	  
	  public static String  listToString(List<String> arrays){
		  String str="";
		  if(arrays!=null&&arrays.size()>0){
			  int len=arrays.size();
			   for(int i=0;i<len;i++){
				    if(i!=len-1){
				    	str=str+arrays.get(i)+",";
				    }else{
				    	str=str+arrays.get(i);
				    }
			   }
		  }
		  return str;
	  }
	  
	  public static List<String>  stirngToList(String arrays){
		   List<String> strs=new ArrayList<String>();
		   if(arrays!=null&&arrays.trim().length()>0){
			    String[] ss=arrays.split(",");
			    if(ss!=null&&ss.length>0){
			    	 strs=Arrays.asList(ss);
			    }
		   }
		  return strs;
	  }
	  
}
