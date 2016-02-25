package com.tr.ui.cloudDisk.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.tr.model.obj.JTFile;

/**
 * "userCategory":{
                "id": "",
                "categoryname": "目录名称",
                "sortid": "路径Id",
                "createtime": "创建时间",
                "pathName": "路径名称",
                "parentId": "父级id",
                "listUserCategory":[],
                "level":"层级数"

        }
 * @author John
 *
 */
public class FileCategoryManager implements Serializable {
	public ArrayList<UserDocument> files;
	public ArrayList<UserCategory> categorys;
	
}
