package com.tr.ui.people.model;


import java.io.Serializable;

public class Address  implements Serializable {
	/**
	 * 父类型
	 */
    public Byte parentType ;
    /**
     * 地域类型
     */
    public Byte areaType ;
    /**
     * 状态名称
     */
    public String stateName ;
    /**
     * 城市名称
     */
    public  String cityName  ;
    /**
     * 国家名称
     */
    public String countyName  ;

}
