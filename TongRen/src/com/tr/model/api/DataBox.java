package com.tr.model.api;

import java.util.ArrayList;
import java.util.List;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Area;
import com.tr.model.obj.Comment;
import com.tr.model.obj.Connections;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.InvestType;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.Knowledge;
import com.tr.model.obj.KnowledgeMini;
import com.tr.model.obj.MoneyType;
import com.tr.model.obj.Requirement;
import com.tr.model.obj.RequirementMini;
import com.tr.model.obj.Trade;
import com.tr.model.obj.TreatedHtml;
import com.tr.model.user.JTMember;
import com.tr.model.user.OrganizationMini;
import com.tr.ui.organization.model.OrgInfoVo;

/**
 * @ClassName:     DataBox.java
 * @Description:   封装接口返回对象（用户可自定义）
 * @Author         leon
 * @Version        v 1.0  
 * @Date           2014-04-10
 * @LastEdit       2014-04-14
 */
public class DataBox {

	public int mUpdateMode = 0; // 更新模式
	public String mUpdateInfo = ""; // 更新状态提示信息
	public String mUrl = ""; // 更新下载地址,当updateMode为1,2时有效
	public String mSessionID = ""; // 会话ID
	public JTMember mJTMember; // 用户对象
	public OrgInfoVo orgInfoVo; // 组织用户信息对象
	
	public List<MoneyType> mListMoneyType; // 货币类型
	public List<String> mListMoneyRange; // 投资金额范围
	public List<InvestType> mListInInvestType; // 融资类型
	public List<InvestType> mListOutInvestType; // 投资类型
	public List<Area> mListArea; // 投资地区
	public List<Trade> mListTrade; // 投资行业
	
	public String mInviteJoinGinTongInfo = ""; // 邀请用户加入金桐网的邀请语
	
	// 投融资关键字
	
	
	public boolean mIsSuccess = false; // 获取验证码是否成功（默认失败）
	public boolean mUpdateSuccess = false; 
	public boolean mBindingStatus = false; //第三方登陆是否绑定已有账号
	public String mMessage = ""; // 附加信息
	public String mVerifyCode = ""; // 验证码
	
	public String userQRUrl = "";//用户二维码内容

	public Requirement mRequirement; // 需求对象

	public List<RequirementMini> mListRequirementMini; // 相关需求
	public List<Connections> mListConnections; // 相关关系
	public List<KnowledgeMini> mListKnowledgeMini; // 相关知识
	public List<AffairsMini> mListAffairMini; // 事务列表
	
	public Knowledge mKnowledge; // 知识详情
	public boolean isChangeSucceed;
	
	// 评论相关
	public List<Comment> mListComment;
	/*首页动态评论*/
	public List<DynamicComment> mDynamicCommentList; 
	public Comment mComment;
	public DynamicComment mDynamicComment;
	public int mTotal;
	public int mIndex;
	public int mSize;
	
	
	public TreatedHtml mTreatedHtml;
	
	public int mResourceType;
	public List<Connections> mListPlatformPeo; // 相关关系
	public List<Connections> mListUserPeo;
	public List<Connections> mListPlatformOrg;
	public List<Connections> mListUserOrg;
	public List<KnowledgeMini2> mListPlatformKno;
	public List<KnowledgeMini2> mListUserKno;
	public List<AffairsMini> mListPlatformAff;
	public List<AffairsMini> mListUserAff;
	
	public ArrayList<AffairNode> mListAffairNode; // 关联事件
	public ArrayList<ConnectionNode> mListPeopleNode; // 关联人脉
	public ArrayList<ConnectionNode> mListOrganizationNode; // 关联组织
	public ArrayList<KnowledgeNode> mListKnowledgeNode; // 关联知识
	
	public DataBox(){
		
	}

	@Override
	public String toString() {
		return "DataBox [mUpdateMode=" + mUpdateMode + ", mUpdateInfo="
				+ mUpdateInfo + ", mUrl=" + mUrl + ", mSessionID=" + mSessionID
				+ ", mJTMember=" + mJTMember + ", mListMoneyType="
				+ mListMoneyType + ", mListMoneyRange=" + mListMoneyRange
				+ ", mListInInvestType=" + mListInInvestType
				+ ", mListOutInvestType=" + mListOutInvestType + ", mListArea="
				+ mListArea + ", mListTrade=" + mListTrade
				+ ", mInviteJoinGinTongInfo=" + mInviteJoinGinTongInfo
				+ ", mIsSuccess=" + mIsSuccess + ", mUpdateSuccess="
				+ mUpdateSuccess + ", mBindingStatus=" + mBindingStatus +", mMessage=" + mMessage + ", mVerifyCode="
				+ mVerifyCode + ", userQRUrl=" + userQRUrl + ", mRequirement="
				+ ", mListConnections=" + mListConnections
				+ ", mListKnowledgeMini=" + mListKnowledgeMini
				+ ", mListAffairMini=" + mListAffairMini + ", mKnowledge="
				+ mKnowledge + ", isChangeSucceed=" + isChangeSucceed
				+ ", mListComment=" + mListComment + ", mDynamicCommentList="
				+ mDynamicCommentList + ", mComment=" + mComment
				+ ", mDynamicComment=" + mDynamicComment + ", mTotal=" + mTotal
				+ ", mIndex=" + mIndex + ", mSize=" + mSize + ", mTreatedHtml="
				+ mTreatedHtml + ", mResourceType=" + mResourceType
				+ ", mListPlatformPeo=" + mListPlatformPeo + ", mListUserPeo="
				+ mListUserPeo + ", mListPlatformOrg=" + mListPlatformOrg
				+ ", mListUserOrg=" + mListUserOrg + ", mListPlatformKno="
				+ mListPlatformKno + ", mListUserKno=" + mListUserKno
				+ ", mListPlatformAff=" + mListPlatformAff + ", mListUserAff="
				+ mListUserAff + ", mListAffairNode=" + mListAffairNode
				+ ", mListPeopleNode=" + mListPeopleNode
				+ ", mListOrganizationNode=" + mListOrganizationNode
				+ ", mListKnowledgeNode=" + mListKnowledgeNode
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
}
