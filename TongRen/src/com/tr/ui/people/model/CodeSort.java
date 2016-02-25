package com.tr.ui.people.model;

import java.io.Serializable;


/**
 * 代码表
 *
 * @author yangjie
 */
public class CodeSort implements Serializable {

    /**
     * 序列号
     */
    public static final long serialVersionUID = -6800065622225396060L;

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
     * 1-职业列表；2-分类列表
     */
    public String codeType;

    /**
     * 排序
     */
    public String orderNo;

  

}
