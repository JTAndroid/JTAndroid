package com.tr.model.knowledge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tr.model.obj.ConnectionsMini;

/**
 * 评论
 * @author gushi
 *
 */
public class KnowledgeComment extends KnowledgeCommentViewBase implements Serializable {
	//评论id
    public long id;
    //知识id
    public long knowledgeId;
    //评论内容
    public String content;
    //评论时间
    public String createtime;
    //评论状态
    public Boolean status;
    //上级评论id
    public long parentid;
    //用户id
    public long userId;
    //子评论数
    public long count;
    //精简对象，关系，保留最精简的数据
    public ConnectionsMini connectionsMini;
    // 子目录集合
    public List<KnowledgeComment> listKnowledgeComment =new ArrayList<KnowledgeComment>();
    
    public static final long serialVersionUID = 1L;

	
     
}