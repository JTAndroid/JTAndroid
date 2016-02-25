package com.tr.model.model;

import java.io.Serializable;

public class ResendInfo  implements Serializable{
    public static final long serialVersionUID = 4341320097772762608L;
    public long userId ;//被转发的用户id
    public String userName ;//被转发用户姓名
    public String content ;//被转发内容
    public String title ;//被转发内容
    public String mongoId ;//被转发mongoid
    public long mysqlId ;//被转发mysqlId
    public String time ;//被转发的发布时间
    public String reply ;//回复内容
    

     
}
