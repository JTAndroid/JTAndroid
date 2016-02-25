package com.tr.ui.flow.model;

import java.io.Serializable;
import java.util.Date;

public class DynamicRelation implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  //目标id，例如type为10或11时，此id就是知识的id
  private long targetId;
  //动态id
  private long dynamicId;
  //目标标题或名称 如果是知识就是标题，如果是人或组织就是姓名
  private String targetTitle;
  //目标图片
  private String picPath;
  //创建时间 格式如2015-01-14 11:10:28
  private Long ctime;
  //目标类型 例如 10：创建知识；11：转发知识
  private String type;
  //lowtype子类型，如1投资，2融资。
  private String lowtype;
  public String getLowtype() {
    return lowtype;
  }
  public void setLowtype(String lowtype) {
    this.lowtype = lowtype;
  }
  public void setType(String type) {
    this.type = type;
  }
  public long getTargetId() {
    return targetId;
  }
  public void setTargetId(long targetId) {
    this.targetId = targetId;
  }
  public String getTargetTitle() {
    return targetTitle;
  }
  public void setTargetTitle(String targetTitle) {
    this.targetTitle = targetTitle;
  }
  public String getPicPath() {
    return picPath;
  }
  public void setPicPath(String picPath) {
    this.picPath = picPath;
  }
  public Long getCtime() {
    return ctime;
  }
  public void setCtime(Long ctime) {
    this.ctime = ctime;
  }
  public String getType() {
    return type;
  }
}
