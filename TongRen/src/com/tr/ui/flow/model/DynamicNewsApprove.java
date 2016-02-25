package com.tr.ui.flow.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 动态点赞
 *
 */
public class DynamicNewsApprove implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public static final int UNDELATE_FLAG = 0;
  public static final int DELATE_FLAG = -1;
  //ID
  private long id;

  //动态ID
  private long dynamicId;

  //用户ID
  private long userId;
  
  //用户名称
  private String userName;

  //创建时间
  private Long ctime;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getDynamicId() {
    return dynamicId;
  }

  public void setDynamicId(long dynamicId) {
    this.dynamicId = dynamicId;
  }

  public long getUserId() {
    return userId;
  }

  public Long getCtime() {
    return ctime;
  }

  public void setCtime(Long ctime) {
    this.ctime = ctime;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }
  
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

}
