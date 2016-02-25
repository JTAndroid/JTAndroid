package com.tr.model.model;



import java.io.Serializable;
import java.util.List;


/**
 * 好友动态 mongo 
 * @author 窦友
 *
 */
public class UserFeed implements Serializable {
    public static final long serialVersionUID = -26037617805798145L;
    
    public String id;//主键
    
    public String mongoId;
    
    public List<ReceiversInfo> receivers;//信息接收人
    public ResendInfo resend;//转发
    
    public long createdById;//信息发布人
    
    public String createdBy;//信息发布人
    
    public long oCreatedById;//信息首次发布人
    
    public String oCreatedBy;//信息首次发布人
    
    public String companyName;//信息首次发布人公司
    
    public String companyJob;//信息首次发布人职位
    
    public int scope;//扩散范围   默认0 所有人可见，1根据receiver检测 2所有好友查询所有好友id然后放入receivers然后scope改成1
    
    public int type;//1：发观点2：回复观点3：发布需求4：分享需求5：评论需求6：引荐好友7：添加了新好友/一度朋友添加好友成功   8：关注机构9：分享机构 10:回复我关注的需求 11 我关注的需求
    public long targetId;//model对象在mysql中的id
    
    public String title;//信息标题
    public String content;//信息内容
    
    public List<String> industryArrs;//行业id集合方便查询
    public List<String> categoryArrs;//类型集合方便查询
    
    
    public String ctime;//发布时间
    public String parentId = "";//扩展字段，心情日记的回复功能，暂时不用(开启，表示被屏蔽)
    
    public List<Long> deletedById;//删除人从自己的列表中删除
    
    public int diaryType;//观点长短，0：短，1：长
    public String imgPath;//长观点的图片路径
    public int delstatus;//状态（0正常使用 -1 删除 ）
    
    public String clearContent;//清楚格式之后的content
    
    public String navTitle;//用来显示该人的行为 例如 发布需求、关注的需求有回复等
    
    public String reqType; //需求类型。 1投资需求  2融资需求     专门用来区分是投资需求还是融资需求  
    public int resendNum;//转发/???分享 次数
    public int favrateNum;//收藏 次数

    public int oneReplyCount;//回复,评论 次数

    public List<EtUserInfo> etInfo;
    
    public String groupName;
    
   
//    是否可见
	public String isVisable;
	
    
    
}
