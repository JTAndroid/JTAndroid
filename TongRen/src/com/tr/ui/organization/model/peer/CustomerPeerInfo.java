/**
 * 
 */
package com.tr.ui.organization.model.peer;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.model.CustomerTag;
import com.tr.ui.organization.model.Relation;

/**
 * 同业竞争详情
 * @author liubang
 *
 */
public class CustomerPeerInfo implements Serializable{

	public static final long serialVersionUID = 6195357811595957477L;
//	public long id;//序号
//	public Relation inc;//公司
//    public List<CustomerTag> tags;//标签   

	public String name;
	public List<CustomerPeerList> list;
	
	
	@Override
	public String toString() {
		return "CustomerPeerInfo [name=" + name + ", list=" + list + "]";
	}
}
