package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tr.model.obj.MobilePhone;

public class PersonSimple implements Serializable {
	public static final long serialVersionUID = 8450933692823537008L;

	/**
     * 主键
     */
    public Long id;

    /**
     * 人的ID
     */
    public Long personid;

    /**
     * 人的类型(1-用户;2-人脉)
     */
    public Short persontype;
    public static final Short PERSON_TYPE_USER = 1;
    public static final Short PERSON_TYPE_PEOPLE = 2;

    /**
     * 姓
     */
    public String name1;

    /**
     * 名
     */
    public String name2;

    /**
     * 名称全拼拼音
     */
    public String pinyin;

    /**
     * 创建时间
     */
    public long createtime;

    /**
     * 公司
     */
    public String company;

    /**
     * 职位
     */
    public String position;

    /**
     * 头像路径
     */
    public String picpath;

    /**
     * 区域代码
     */
    public Long regionId;

    /**
     * 分类代码ID
     */
    public Long typeId;

    /**
     * 职业代码ID
     */
    public Long careerId;

    /**
     * 电话
     */
    public String phone;
    //性别
    public int gender;

//    public List<Map<String , String>> listMobilePhone ;
//
//    public List<Map<String , String>> listFixedPhone ;
    
     public ArrayList<MobilePhone> listMobilePhone;
    
     public ArrayList<MobilePhone> listFixedPhone;
    
     public String ctime;
    
     public String opType;

   
}