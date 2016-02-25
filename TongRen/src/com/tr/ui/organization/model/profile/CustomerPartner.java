package com.tr.ui.organization.model.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
/**
 * 合伙人/专业团队
 * @author liubang
 *
 */
public class CustomerPartner implements Parcelable{
	/**
	 * 
	 */
	public static final long serialVersionUID = 5580128403639317691L;
	public String id;//序号
	public Relation name;//合伙人姓名
	public String companyJob;//公司职务
	public String expertise;//专业领域
	public String address;//办公地点
	public String percent;//出资比例
	public String email;//邮箱
	public List<CustomerPersonalLine> propertyList; //自定义属性
	  //静态的Parcelable.Creator接口
    public static final Parcelable.Creator<CustomerPartner> CREATOR = new Creator<CustomerPartner>() {
            
            //创建出类的实例，并从Parcel中获取数据进行实例化
            public CustomerPartner createFromParcel(Parcel source) {
            	CustomerPartner partner = new  CustomerPartner(source);
            	partner.id = source.readString();
            	partner. name = (Relation) source.readSerializable();
            	partner. companyJob = source.readString();
            	partner. expertise = source.readString();
            	partner. address = source.readString();
            	partner. percent = source.readString();
            	partner.email = source.readString();
            	partner.propertyList = new ArrayList<CustomerPersonalLine>();
           	source.readList( partner.propertyList, getClass().getClassLoader());
           	 
                    return partner;
            }

            public CustomerPartner[] newArray(int size) {
                    return new CustomerPartner[size];
            }

    };
    // 读数据进行恢复  
    public CustomerPartner(Parcel source) {  
   	 
    }  
    //
    @Override
    public int describeContents() {
            return 0;
    }
    
    //将数据写入外部提供的Parcel中
    @Override
    public void writeToParcel(Parcel dest, int flags) {
   	 		dest.writeString(id);
            dest.writeSerializable(name);
            dest.writeString(companyJob);
            dest.writeString(expertise);
            dest.writeString(address);
            dest.writeString(percent);
            dest.writeString(email);
            dest.writeList(propertyList);
    }
	 
	
}
