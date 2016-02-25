/**
 * 
 */
package com.tr.ui.organization.model.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.tr.ui.organization.model.profile.CustomerPersonalLine;

/**
 * 股东研究
 * @author liubang
 *
 */
public class CustomerStock implements Parcelable {
	public static final long serialVersionUID = -61160835169300473L;
	public long id;//主键
	public String cShareholder;//控股股东
    public String cStockPercent;//'持股比例',
    public String rShareholder;//实际控股人
    public String rStockPercent;//'持股比例'
    public String fShareholder;//最终控股人
    public String fStockPercent;//'持股比例',
    public String taskId;//附件id
    
    public ArrayList<CustomerPersonalLine> personalLineList;

	@Override
	public String toString() {
		return "CustomerStock [id=" + id + ", cShareholder=" + cShareholder
				+ ", cStockPercent=" + cStockPercent + ", rShareholder="
				+ rShareholder + ", rStockPercent=" + rStockPercent
				+ ", fShareholder=" + fShareholder + ", fStockPercent="
				+ fStockPercent + ", taskId=" + taskId + ", personalLineList="
				+ personalLineList + "]";
	}

	//静态的Parcelable.Creator接口
    public static final Parcelable.Creator<CustomerStock> CREATOR = new Creator<CustomerStock>() {
            
            //创建出类的实例，并从Parcel中获取数据进行实例化
            public CustomerStock createFromParcel(Parcel source) {
            	CustomerStock stock = new  CustomerStock();
            	stock.id  = source.readLong();
            	stock.cShareholder = source.readString();
            	stock. cStockPercent = source.readString();
            	stock. rShareholder = source.readString();
            	stock.rStockPercent = source.readString();
            	stock. fShareholder = source.readString();
            	stock. fStockPercent = source.readString();
            	stock. taskId = source.readString();
            	stock.personalLineList = new ArrayList<CustomerPersonalLine>();
            	 source.readList(stock.personalLineList,getClass().getClassLoader());
                    return stock;
            }

            public CustomerStock[] newArray(int size) {
                    return new CustomerStock[size];
            }

    };
    //
    @Override
    public int describeContents() {
            return 0;
    }
    
    //将数据写入外部提供的Parcel中
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    		dest.writeLong(id);
   	 		dest.writeString(cShareholder);
            dest.writeString(cStockPercent);
        	dest.writeString(rShareholder);
            dest.writeString(rStockPercent);
        	dest.writeString(fShareholder);
            dest.writeString(fStockPercent);
            dest.writeString(taskId);
            dest.writeList(personalLineList);
    }

	
    
    
}
