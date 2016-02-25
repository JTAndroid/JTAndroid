package com.tr.ui.organization.model.profile;

import java.io.Serializable;

/**
 * <p>
 * Title: CustomerPersonalLine.java<／p>
 * <p>
 * Description:客户通用自定义标签 <／p>
 * 查询研究报告
 * 
 * @author wfl
 * @date 2014-12-29
 * @version 1.0
 */

public class CustomerPersonalLine implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5016912496497912642L;
	/** 自定义名称 */
	public String name;
	/** 内容 */
	public String content;
	/** 类型 1简答文本 2大文本框 */
	public String type;
	
	
	  public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

//	//静态的Parcelable.Creator接口
//    public static final Parcelable.Creator<CustomerPersonalLine> CREATOR = new Creator<CustomerPersonalLine>() {
//            
//            //创建出类的实例，并从Parcel中获取数据进行实例化
//            public CustomerPersonalLine createFromParcel(Parcel source) {
//            	CustomerPersonalLine partner = new  CustomerPersonalLine();
//            	partner.name = source.readString();
//            	partner. content = source.readString();
//            	partner. type = source.readString();
//           	 
//                    return partner;
//            }
//
//            public CustomerPersonalLine[] newArray(int size) {
//                    return new CustomerPersonalLine[size];
//            }
//
//    };
//    //
//    @Override
//    public int describeContents() {
//            return 0;
//    }
//    
//    //将数据写入外部提供的Parcel中
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//   	 		dest.writeString(name);
//            dest.writeString(content);
//            dest.writeString(type);
//    }

	@Override
	public String toString() {
		return "CustomerPersonalLine [name=" + name + ", content=" + content
				+ ", type=" + type + "]";
	}
	

}
