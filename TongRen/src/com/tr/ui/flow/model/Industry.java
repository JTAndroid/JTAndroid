package com.tr.ui.flow.model;

import java.io.Serializable;

/**
 * 行业
 *
 */
public class Industry implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  //行业ID
  private long id;
  //行业编码
  private String code;
  //行业名称
  private String name;
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
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
}