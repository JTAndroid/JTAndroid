package com.tr.ui.people.model;

/**
 * Created by Administrator on 2015/4/3.
 */
public class PersonConnect {
    public static enum ConnectType {
        // 关联格式（p:人脉,r:事件,o:组织,k:知识）
        event(1, "r"), people(2, "p"), organization(5, "o"), knowledge(6, "k");
        public int v;

        public String c;
   
        ConnectType(int v, String c) {
            this.v = v;
            this.c = c;  
        }

        public int v() {
            return v;
        }

        public String c() {
            return c;
        }
    }
    public Long id;

    public Long personid;

    public Integer persontype ;

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
