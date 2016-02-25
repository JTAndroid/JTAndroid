package com.tr.model.knowledge;

import java.io.Serializable;
import java.util.Date;
/**
 * 用户权限
 * @author gushi
 *
 */
public class UserPermission implements Serializable {
	//接收者Id
    private Long receiveUserId;
    //知识id
    private Long knowledgeId;
    //发起者id
    private Long sendUserId;
    //类型
    private int type;
    //分享留言
    private String mento;
    //创建时间
    private String createtime;
    //知识所属类目ID
    private int columnId;

    private static final long serialVersionUID = 1L;

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMento() {
        return mento;
    }

    public void setMento(String mento) {
        this.mento = mento;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }
}