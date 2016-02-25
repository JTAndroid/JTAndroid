package com.tr.ui.people.model;

import java.io.Serializable;

/**
 * 自定义 、社会关系等
 */
public class Basic implements Serializable {
	public static final long serialVersionUID = -6518814221619289006L;
	public static final Byte PARENT_TYPE_MOBILE = 1;
	public static final Byte PARENT_TYPE_FIXED_PHONE = 2;
	public static final Byte PARENT_TYPE_FAX = 3;
	public static final Byte PARENT_TYPE_EMAIL = 4;
	/**
	 * 自定义名称
	 */
	public String name ;
	/**
	 * 内容
	 */
	public String content;
	/**
	 */
	public String type;
	
	/**
	 * 标签类型
	 */
	public String subtype;
	
	
	public Basic (){
		
	}
	public Basic (String name,String content,String type,String subtype){
		this.name=name;
		this.content=content;
		this.type=type;
		this.subtype=subtype;
	}
	
	/**
	 *   "name":"自定义项名称",
        "content":"自定义项内容",
        "type":父类型：1-手机类型，2-固话类型，3-传真类型，4-邮箱类型，5-主页类型，6-即时通讯类型，7-地址, N-自定义，都不是则为自定义,
        "subtype":子类型
                手机类型：1-手机，2-电话，3-商务电话，4-主要电话，N-自定义
                固话类型：1-固话，2-家庭电话，3-办公电话，4-主要电话，N-自定义
                传真类型：1-住宅传真，2-商务传真，N-自定义
                邮箱类型：1-主要邮箱，2-商务邮箱，N-自定义
                主页类型：1-个人主页，2-商务主页，N-自定义
                通讯类型：1-QQ，2-微信，3-微博，4-Skype，5-facebook，6-twitter，N-自定义
                地址类型：1-住宅，2-商务，N-自定义
                自定义类型：N-自定义单行文本；M-自定义多行文本；2-数值；3-日期；都不是则为单行文本"
	 */

}
