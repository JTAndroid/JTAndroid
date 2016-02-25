package com.tr.ui.organization.model;

import java.io.Serializable;


public class ConnectionInfo implements Serializable {
    public Long id;
    /**
     * 组织客户id
     */
    public Long customerId;

    public String tag;

    public Integer conntype;

    public Long connid;

    public String connname;

    public Long ownerid;

    public String owner;

    public String requirementtype;

    public String career;

    public String company;

    public String address;

    public String hy;

    public String columnpath;

    public Integer columntype;

    public String url;

    public String picpath;
    
    public Integer allasso;
    
    public static final long serialVersionUID = 1L;

}