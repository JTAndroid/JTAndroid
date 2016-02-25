package com.tr.ui.organization.model.profile;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.tr.ui.organization.model.profile.CustomerPersonalLine;
/**
* <p>Title: LinkManInfo.java<／p> 
* <p>Description:联系人<／p> 
* @author wfl
* @date 2015-1-12 
* @version 1.0
 */
public class CustomerLinkMan implements Parcelable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;

	 public long id;
	 public String name;//姓名
	 public String mobile;//手机
	 public String email;//邮箱
	 public List<CustomerPersonalLine> propertyList; //自定义属性
	 
	  //静态的Parcelable.Creator接口
     public static final Parcelable.Creator<CustomerLinkMan> CREATOR = new Creator<CustomerLinkMan>() {
             
             //创建出类的实例，并从Parcel中获取数据进行实例化
             public CustomerLinkMan createFromParcel(Parcel source) {
            	 CustomerLinkMan LinkMan = new  CustomerLinkMan(source);
            	 LinkMan.id = source.readLong();
            	 LinkMan. name = source.readString();
            	 LinkMan. mobile = source.readString();
            	 LinkMan.email = source.readString();
            	 LinkMan.propertyList = new ArrayList<CustomerPersonalLine>();
            	source.readList( LinkMan.propertyList, getClass().getClassLoader());
            	 
                     return LinkMan;
             }

             public CustomerLinkMan[] newArray(int size) {
                     return new CustomerLinkMan[size];
             }

     };
     // 读数据进行恢复  
     public CustomerLinkMan(Parcel source) {  
    	 
     }  
     //
     @Override
     public int describeContents() {
             return 0;
     }
     
     //将数据写入外部提供的Parcel中
     @Override
     public void writeToParcel(Parcel dest, int flags) {
    	 	dest.writeLong(id);
             dest.writeString(name);
             dest.writeString(mobile);
             dest.writeString(email);
             dest.writeList(propertyList);
     }
	 
}
