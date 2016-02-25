/**
 * 
 */
package com.tr.ui.organization.model.hight;

import java.io.Serializable;
import java.util.ArrayList;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerPartner;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;

/**
 * 高层治理详情
 * @author liubang
 *
 */
public class CustomerHightInfo implements Parcelable {
	public static final long serialVersionUID = 1139232922352234712L;
	public String id; //id
    public Relation relation; //名称
    public String job; //职务
    public String sex;//性别
    public String birth;//任职日期
    public String eduational;//学历
    public String annualSalary;//年薪(万元)
    public String shares; //股份数
    public String type; //区分董事会 监事会 高管  高管兼职
    
    //静态的Parcelable.Creator接口
    public static final Parcelable.Creator<CustomerHightInfo> CREATOR = new Creator<CustomerHightInfo>() {
            
            //创建出类的实例，并从Parcel中获取数据进行实例化
            public CustomerHightInfo createFromParcel(Parcel source) {
            	CustomerHightInfo partner = new  CustomerHightInfo();
            	partner.id = source.readString();
            	partner.relation = (Relation) source.readSerializable();
            	partner.job = source.readString();
            	partner.sex=source.readString();
            	partner.birth=source.readString();
            	partner.eduational=source.readString() ;
            	partner.annualSalary =source.readString();
        		partner.shares=source.readString() ;
        		partner.type=source.readString() ;
           	 
                    return partner;
            }

            public CustomerHightInfo[] newArray(int size) {
                    return new CustomerHightInfo[size];
            }

    };
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeSerializable(relation);
		dest.writeString(job);
		dest.writeString(sex);
		dest.writeString(birth);
		dest.writeString(eduational);
		dest.writeString(annualSalary);
		dest.writeString(shares);
		dest.writeString(type);
	}       
}
