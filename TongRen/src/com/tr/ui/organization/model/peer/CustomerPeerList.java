package com.tr.ui.organization.model.peer;

import java.io.Serializable;
import java.util.List;

/**
 * 同业竞争
 * @author User
 *
 */

public class CustomerPeerList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String  title;
	public String  pic;
	public String  author;
	public int  authorId;
	public String  cpathid;
	public String  ctime;
	public String  tags;
	public String  desc;
	public String  type ;

@Override
public String toString() {
	return "PeerList [id=" + id + ", title=" + title + ", pic=" + pic
			+ ", author=" + author + ", authorId=" + authorId + ", cpathid="
			+ cpathid + ", ctime=" + ctime + ", tags=" + tags + ", desc="
			+ desc + ", type=" + type + "]";
 }  
}