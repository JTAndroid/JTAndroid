package com.tr.ui.people.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 社会活动
 */
public class SocialActivity implements Serializable {
    /**
     * 介绍人
     */
    public String introducer;
    /**
     * 老乡
     */
    public String townsmen;
    /**
     * 活动类型
     */
    public ArrayList<Basic> activityType;
    public List<Basic> custom;


   
}
