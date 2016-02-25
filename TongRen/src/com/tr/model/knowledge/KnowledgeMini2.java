package com.tr.model.knowledge;

import java.util.ArrayList;
import com.tr.model.conference.MMeetingData;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.ResourceBase;

/**
 * 知识缩略对象
 * @author gushi
 *
 */
public class KnowledgeMini2  extends ResourceBase{

	
	public static final long serialVersionUID = 1L;
	// 知识id
	public long id;
	// 类型：1-资讯，2-投融工具，3-行业，4-经典案例，5-图书报告，6-资产管理，7-宏观，8-观点，9-判例，10-法律法规，11-文章
	public int type;
	// 标题
	public String title;
	// 描述
	public String desc;
	// 来源
	public String source;
	// 最后修改时间
	public String modifytime;
	// 封面地址
	public String pic;
	// 标签
	public ArrayList<String> listTag;
	// 原内容
	public String content;
	// 目录
	public String columnpath;
	// 发布人
	public Connections connections;
	// 数据库索引
	public long shareMeId;
	
	public boolean isAlterMeeting;
    public MMeetingData alterMeetingData;
    public String tag;
	public KnowledgeMini2(long id,int type,String title,String desc,String modifytime,String pic, ArrayList<String> listTag,
			String columnpath,Connections connections,long shareMeId, String tagText){
		this.id=id;
		this.type=type;
		this.title=title;
		this.desc=desc;
		this.modifytime=modifytime;
		this.pic=pic;
		this.listTag=listTag;
		this.columnpath=columnpath;
		this.connections=connections;
		this.shareMeId=shareMeId;
		this.tag = tagText;
	}
	
	
	public KnowledgeMini2(String title){
		this.title = title;
	}
	public KnowledgeMini2() {
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	// 转换为JTFile对象
	public JTFile toJTFile(){
		JTFile jtFile = new JTFile();
		jtFile.mUrl = pic;
        jtFile.mSuffixName = desc;
        jtFile.mTaskId = id + "";
		jtFile.mType = JTFile.TYPE_KNOWLEDGE2;
		jtFile.reserved1 = type + "";
		jtFile.reserved2 = title;
		return jtFile;
	}
	
	public DemandASSOData toDemandASSOData() {
		DemandASSOData demandASSOData = new DemandASSOData();
		demandASSOData.id=String.valueOf(this.id);
		demandASSOData.title=this.title;
		demandASSOData.columnpath=this.columnpath;
		demandASSOData.type=this.type;
		return demandASSOData;
	}
}
