package com.tr.ui.people.model;


import java.io.Serializable;

/**
 * 标签类型
 */
public class TypeTag implements Serializable {
	public static final long serialVersionUID = -4662106043586962685L;
	/**
     * 标签id
     */
    public Long id;
    /**
     * 类型，0-默认，1-自定义
     */
    public int type;
    public static int TYPE_DEFINED = 0;//默认、预定义好的
    /**
     * 标签名称
     */
    public String name;

   
}
