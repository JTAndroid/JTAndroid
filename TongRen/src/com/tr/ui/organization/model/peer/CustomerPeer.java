/**
 * 
 */
package com.tr.ui.organization.model.peer;

import java.io.Serializable;
import java.util.List;


import com.tr.ui.organization.model.peer.CustomerPeerInfo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;

/**
 * 同业竞争
 * 
 * @author liubang
 * 
 */
public class CustomerPeer implements Serializable {
	/**
	 * 
	 */
	public static final long serialVersionUID = 2784654355513431563L;
	public long id;// 主键
	public List<CustomerPeerInfo> peerList; // 主要竞争对手
	// public String taskId; //附件id

	public List<CustomerPersonalLine> personalLineList;// 自定义属性


}
