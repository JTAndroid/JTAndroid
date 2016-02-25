package com.tr.model.knowledge;

import java.io.Serializable;

/**
 * 知识统计类
 * @author gushi
 *
 */
public class KnowledgeStatics implements Serializable {
    //知识id
	private Long knowledgeId;
	//评论数
    private Long commentcount;
    //分享数
    private Long sharecount;
    //收藏数
    private Long collectioncount;
    /**
     * 点击数
     */
    private Long clickcount;
    //标题
    private String title;
    //知识来源
    private int source;
    //类型
    private int type;

    private static final long serialVersionUID = 1L;

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Long getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(Long commentcount) {
        this.commentcount = commentcount;
    }

    public Long getSharecount() {
        return sharecount;
    }

    public void setSharecount(Long sharecount) {
        this.sharecount = sharecount;
    }

    public Long getCollectioncount() {
        return collectioncount;
    }

    public void setCollectioncount(Long collectioncount) {
        this.collectioncount = collectioncount;
    }

    public Long getClickcount() {
        return clickcount;
    }

    public void setClickcount(Long clickcount) {
        this.clickcount = clickcount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}