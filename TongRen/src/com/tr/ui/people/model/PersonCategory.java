package com.tr.ui.people.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 人脉、好友目录实体
 *
 * @author xingtianlun
 */
public class PersonCategory implements Serializable {

    /**
     * 主键
     */
    public Long id;

    /**
     * 父级id
     */
    public Long pid;

    /**
     * 目录名称
     */
    public String name;

    /**
     * 路径Id，如:01/0101
     */
    public String sortId;

    /**
     * 用户ID
     */
    public Long userId;

    /**
     * 创建时间
     */
    public Timestamp ctime;


   

}
