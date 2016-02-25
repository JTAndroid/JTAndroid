package com.tr.ui.communities.model;

import java.io.Serializable;

import com.tr.model.obj.JTFile;
import com.utils.common.EUtil;

/**
 * @ClassName: ImMucinfo
 * @Description: 首页 -社群列表实体实体 与创建社群的社区实体区别是时间类型
 * @author cui
 * @date 2015-11-26 下午3:48:49
 * 
 */
public class ImMucinfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9107726801551443977L;
	/**
	 * 群聊or会议id
	 */
	private long id;
	/**
	 * "createUserId": 创建者id，不能为空
	 */
	private long createUserId;
	/**
	 * 如果是社群的话，这里存储社群的名称.
	 */
	private String title;
	/**
	 * 会议主题（只有会议才有 主题，普通群聊无主题）;社群的介绍.
	 */
	private String subject;
	/**
	 * 0-内部机构;1-普通群聊;2-会议; 4社群.
	 */
	private int type;
	/**
	 * 机构ID.
	 */
	private Long organizationId;
	/**
	 * 会议内容;社群的公告.
	 */
	private String content;
	/**
	 * 人数上限.
	 */
	private int max;
	/**
	 * 是否置顶 0否，1是.
	 */
	private int stickType;
	/**
	 * 是否保存聊天记录，0否，1是，目前是自动保存.
	 */
	private int autoSaveType;
	/**
	 * 会议开始时间，预约时间可调整， 及时已召开，相当于再次预约。预约时间没到的会议自动置顶.
	 */
	private Long time;
	/**
	 * 1-正常召开；2-预约中，等待开始；3-已解散，解散后用户不可见.
	 */
	private int status;
	/**
	 * 社群号
	 */
	private String communityNo;
	/**
	 * 社群头像
	 */
	private String picPath;
	/**
	 * 群主id
	 */
	private long ownerId;
	/**
	 * 是否群主1：群主2：不是群主
	 */
	private int isqz;
	/**
	 * 是否隐身1：不隐身2：隐身
	 */
	private int isys;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(long createUserId) {
		this.createUserId = createUserId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getStickType() {
		return stickType;
	}

	public void setStickType(int stickType) {
		this.stickType = stickType;
	}

	public int getAutoSaveType() {
		return autoSaveType;
	}

	public void setAutoSaveType(int autoSaveType) {
		this.autoSaveType = autoSaveType;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCommunityNo() {
		return communityNo;
	}

	public void setCommunityNo(String communityNo) {
		this.communityNo = communityNo;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public int getIsqz() {
		return isqz;
	}

	public void setIsqz(int isqz) {
		this.isqz = isqz;
	}

	public int getIsys() {
		return isys;
	}

	public void setIsys(int isys) {
		this.isys = isys;
	}
	
	// 转换为JTFile对象
		public JTFile toJTFile(){
			JTFile jtFile = new JTFile();
			jtFile.mUrl = this.picPath;
	        jtFile.mSuffixName = this.subject;
	        jtFile.mTaskId = id + "";
			jtFile.mType = JTFile.TYPE_COMMUNITY;
			jtFile.fileName = this.title;
			jtFile.messageID=EUtil.genMessageID();
			return jtFile;
		}
}
