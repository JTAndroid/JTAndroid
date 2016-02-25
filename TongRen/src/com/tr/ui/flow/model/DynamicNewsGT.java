package com.tr.ui.flow.model;

import java.io.Serializable;
import java.util.List;

public class DynamicNewsGT  implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private List<Industry> industrys;
  
  //ID
  private long id;
  //动态标题
  private String title;
  /**
   * 类型 10：创建知识；11：转发知识; 12：金桐脑推荐知识  20：创建需求；21：转发需求 22：金桐脑推荐需求
   * 30：新建人脉；31：转发人脉; 32：金桐脑推荐人脉;40：新建会议;41：转发会议
   * 50：用户名片;51：组织名片；52：人脉名片；53:客户名片 
   * 60：创建组织；61：转发组织；62：创建客户；63：转发客户 64:大数据推荐组织
   * 99：说说
   */
  private String type;
  //内容
  private String content;
  //清除格式后的内容
  private String clearContent;
  //内容的连接地址，外来转发和分享
  private String contentPath;
  //创建时间
  private Long ctime;
  //源图片地址
  private String imgPath;
  //源id，如知识id
  private long targetId;
  //子类型，如：不同知识类型，转发会议0表示邀请函，1表示会议
  private String lowType;
  //分享类型 2大乐，3中乐，4小乐，5独乐
  private String ptype;
  //动态关联的目标
  private List<DynamicRelation> targetRelations;
 
  //用户评价
  private List<DynamicNewsApprove> approves;
  //用户评论
  private List<DynamicComment> comments;
  
  public List<DynamicNewsApprove> getApproves() {
    return approves;
  }
  public void setApproves(List<DynamicNewsApprove> approves) {
    this.approves = approves;
  }
  public List<DynamicComment> getComments() {
    return comments;
  }
  public void setComments(List<DynamicComment> comments) {
    this.comments = comments;
  }
  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getClearContent() {
    return clearContent;
  }
  public void setClearContent(String clearContent) {
    this.clearContent = clearContent;
  }
  public String getContentPath() {
    return contentPath;
  }
  public void setContentPath(String contentPath) {
    this.contentPath = contentPath;
  }
  public Long getCtime() {
    return ctime;
  }
  public void setCtime(Long ctime) {
    this.ctime = ctime;
  }
  public String getImgPath() {
    return imgPath;
  }
  public void setImgPath(String imgPath) {
    this.imgPath = imgPath;
  }
  public long getTargetId() {
    return targetId;
  }
  public void setTargetId(long targetId) {
    this.targetId = targetId;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getLowType() {
    return lowType;
  }
  public void setLowType(String lowType) {
    this.lowType = lowType;
  }
  public String getPtype() {
    return ptype;
  }
  public void setPtype(String ptype) {
    this.ptype = ptype;
  }
  public List<DynamicRelation> getTargetRelations() {
    return targetRelations;
  }
  public void setTargetRelations(List<DynamicRelation> targetRelations) {
    this.targetRelations = targetRelations;
  }

  public List<Industry> getIndustrys() {
    return industrys;
  }

  public void setIndustrys(List<Industry> industrys) {
    this.industrys = industrys;
  }
  
}
