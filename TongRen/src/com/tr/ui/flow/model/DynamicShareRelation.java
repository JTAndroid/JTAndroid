package com.tr.ui.flow.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 动态接受者
 *
 */
public class DynamicShareRelation implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  //主键
  private long id;
  //动态id
  private long dynamicId;
  //发送者id
  private long senderId;
  //接收者id
  private long receiverId;
  //创建时间格式如2015-01-14 11:10:28
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
  public long getSenderId() {
    return senderId;
  }
  public void setSenderId(long senderId) {
    this.senderId = senderId;
  }
  public Long getCtime() {
    return ctime;
  }
  public void setCtime(Long ctime) {
    this.ctime = ctime;
  }
  public long getReceiverId() {
    return receiverId;
  }
  public void setReceiverId(long receiverId) {
    this.receiverId = receiverId;
  }
}
