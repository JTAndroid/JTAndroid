package com.tr.ui.flow.model;

import java.io.Serializable;
/**
 * 话题
 * @author gintong
 *
 */
public class Topic implements Serializable {
  //主键id
  private long id;
  //话题名称
  private String name;
  //话题创建者id
  private long createrId;
  //话题类型 如音乐
  private String type;
  
  public long getCreaterId() {
    return createrId;
  }
  public void setCreaterId(long createrId) {
    this.createrId = createrId;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

}
