package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 会面
 */
public class PersonMeet implements Serializable {
    /**
     * 会面id
     */
    public Long meetId;

    /**
     * 颜色
     */
    public Short color;
    /**
     * 国家
     */
    public Short country;
    /**
     * 省
     */
    public String province;
    /**
     * 市
     */
    public String city;

    /**
     * 县
     */
    public String town;

    /**
     * 详细地点
     */
    public String location;
    /**
     * 地点横坐标
     */
    public String locX;
    /**
     * 地点纵坐标
     */
    public String locY;
    /**
     * 会面时间
     */
    public Date meetTime;
    /**
     * 重复类型
     */
    public Date repeatType;
    /**
     * 提醒类型
     */
    public Short remindType;
    /**
     * 提醒提前时间
     */
    public Short remindTimes;
    /**
     * 会面附件
     */
    public String taskIdA;
    /**
     * 备注图片视频附件
     */
    public String taskIdV;
    /**
     * 备注其他附件
     */
    public String taskIdO;
    /**
     * 会面附件
     */
    public String taskId;
    /**
     * 创建人
     */
    public Long cid;
    /**
     * 创建时间
     */
    public Date ctime;
    /**
     * 人的ID
     */
    public Long personId;
    /**
     * 人的类型
     */
    public Short personType;

    
}