package com.tr.ui.people.model;


import java.io.Serializable;

/**
 * 人脉名称
 */
public class PersonName implements Serializable {
	public static final long serialVersionUID = -3153505096946533595L;
	/**
     * 姓
     */
    public String lastname;
    /**
     * 名
     */
    public String firstname;
    
    /**
     * 1-中文名，2-英文名，N-自定义
     */
    public Byte parentType;
    public static Byte PARENT_TYPE_ZH = 1;//中文名
    /**
     * 标签类型
     */
    public TypeTag typeTag;

}
