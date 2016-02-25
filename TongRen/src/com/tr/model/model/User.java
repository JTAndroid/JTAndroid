package com.tr.model.model;

import java.io.Serializable;

import com.utils.string.StringUtils;

public class User implements Serializable  {
    public static final long serialVersionUID = -3200471165235804436L;
    public String id; // id
    public long uid; // uid
    public int test =1;//是否为测试用户 0 正常用户 1测试用户
    public Integer status =1;//1：正常；0：锁定；-1：注销
    public String activationCode ="";//激活验证码
    public String nameIndex="";//简拼音
    public String nameIndexAll="";//全拼音
    public int type=1;//注册类型 ，个人："1"，机构："2"
    //基本信息
    public String userName=""; // 用户名
    public String password=""; // 密码
    public String salt=""; //用户注册时由系统自动为用户产生
    public String name="";//真实姓名
    public int sex=1;//性别 1男  2 女
    public boolean country=true; //国家
    public String province="";//省份
    public String city="";//城市
    public String county="";//县
    //联系方式
    public String email=""; // 邮箱
    public String mobile=""; // 手机
    public String companyName="";//单位名称
    public String companyJob="";//职务
    
    public String ctime;//注册日期
    public String ipRegistered="";//注册IP地址
    public String remark="";//备注
    public int isOnline=0;//是否在线  1：在线  0：未在线
    public String picPath="";//头像地址
    public long remainPoints=0; // 用户剩余积分
    public String lastLoginTime;//最后登录时间
    public String level="";//用户级别
   
    public boolean virtual=false;//虚拟用户，是否是公司   0否  1是
    public long corpId=0;//公司id  业务系统中的公司id
    public long remainMoney=0; // 用户剩余金额
  
    public UserConfig userConfig;//设置权限
    public int isOneLogin=0;
    public int isFirstSet=0; //是否已提交了完善提示 0：未完善 1：已完善
    public int isReceiveEmail = 2;// 是否接收email推送消息  1 接收  2. 不接收
    public int shieldStatus = 0;//屏蔽状态 0 未屏蔽 1 已屏蔽 2已屏蔽但尚未生效
    public String nameFirst="";// 用户首字母  比如 “张三”对应的就是“z”
    public String regFrom;//注册时客户端类型  gintongweb、wegintongweb、gintongapp等
    public String peopleId; //人脉id
	
    
    public String getAddress(){
    	    String returnData="";
    	    if(!StringUtils.isEmpty(province)){
    	    	returnData+=province;
    	    }
    	    if(!StringUtils.isEmpty(city)){
    	    	returnData+=city;
    	    }
    	    if(!StringUtils.isEmpty(county)){
    	    	returnData+=county;
    	    }
    	    return returnData;
    }
	
    
    
}