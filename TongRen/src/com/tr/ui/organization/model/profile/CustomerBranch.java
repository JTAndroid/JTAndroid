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
 * 分支机构
 * @author liubang
 *
 */
public class CustomerBranch implements Parcelable{

	/**
	 * 
	 */
	public static final long serialVersionUID = -7192486949119963101L;
	public String id;
	public String name;//'机构名称',
    public Relation relation;//'关联已有客户',
    public Relation leader;//负责人/法人姓名/法定代表人/联系人
    public Relation sponsor;//保荐代表人/经办人
    public String address;//办公地址
    public String phone;//联系电话
    public String fax;//传真
    public String email;//邮箱
    public String website;//网址
    public List<CustomerPersonalLine> propertyList; //自定义属性
    //静态的Parcelable.Creator接口
    public static final Parcelable.Creator<CustomerBranch> CREATOR = new Creator<CustomerBranch>() {
            
            //创建出类的实例，并从Parcel中获取数据进行实例化
            public CustomerBranch createFromParcel(Parcel source) {
            	CustomerBranch branch = new  CustomerBranch(source);
            	branch.id = source.readString();
            	branch. name = source.readString();
            	branch.relation = (Relation) source.readSerializable();
            	branch. leader = (Relation) source.readSerializable();
            	branch. sponsor = (Relation) source.readSerializable();
            	branch. address = source.readString();
            	branch. phone = source.readString();
            	branch. fax = source.readString();
            	branch.email = source.readString();
            	branch.website = source.readString();
            	branch.propertyList = new ArrayList<CustomerPersonalLine>();
           	source.readList( branch.propertyList, getClass().getClassLoader());
           	 
                    return branch;
            }

            public CustomerBranch[] newArray(int size) {
                    return new CustomerBranch[size];
            }

    };
    // 读数据进行恢复  
    public CustomerBranch(Parcel source) {  
   	 
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
            dest.writeString(name);
            dest.writeSerializable(relation);
            dest.writeSerializable(leader);
            dest.writeSerializable(sponsor);
            dest.writeString(address);
            dest.writeString(phone);
            dest.writeString(fax);
            dest.writeString(email);
            dest.writeString(website);
            dest.writeList(propertyList);
    }
}
