package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限人脉关联表
 */
public class PersonPermission implements Serializable {

    /**
     * 权限人脉关联id
     */
    public Long id;
    /**
     * 人脉id
     */
    public Long personid;

    /**
     * 类型 1人脉 2用户
     */
    public Short persontype;


    /**
     * 发送人id
     */
    public Long sendid;

    /**
     * 接收人id
     */
    public Long receiveid;

    /**
     * 创建时间
     */
    public Date createtime;

    /**
     * 权限类型
     */
    public Short ptype;

    /**
     * 是否大数据推送
     */
    public Short ispush;

    /**
     * 权限模块
     */
    public String modeltype;

  
}