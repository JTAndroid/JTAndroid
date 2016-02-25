package com.tr.model.demand;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ChoiceObj.java
 * @author fxtx
 * @Date 2015年3月30日 下午3:22:23
 * @Description: TODO(用一句话描述该文件做什么)
 */
public class ChoiceObj implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6501180300067239158L;
	public String id; // id
	public String name;// 名称
	public List<ChoiceObj> childs;// 子集

	/**
	 *将ChoiceObj  转成 Metadata
	 * 
	 * @return
	 */
	public static Metadata toMetadata(ChoiceObj obj) {
		Metadata metadata = new Metadata();
		metadata.id = obj.id;
		metadata.name = obj.name;
		if (obj.childs != null && obj.childs.size() > 0) {
			metadata.selectNum = 1;// 部分选择
			metadata.childs = toMetadataList(obj.childs);
		} else {
			metadata.selectNum = 2;// 全部选择
		}
		return metadata;
	}

	/**
	 *  *将Metadata  转成 ChoiceObj
	 * @param choiceObj
	 * @return
	 */
	public static List<Metadata> toMetadataList(List<ChoiceObj> choiceObj) {
		List<Metadata> metadatas = new ArrayList<Metadata>();
		if (choiceObj != null) {
			for (ChoiceObj obj : choiceObj) {
				metadatas.add(toMetadata(obj));
			}
		}
		return metadatas;
	}

	/**
	 * 将Metadata  转成 ChoiceObj
	 * @return
	 */
	public static ChoiceObj toChoiceObj(Metadata metadata) {
		ChoiceObj choiceObj = new ChoiceObj();
		choiceObj.id = metadata.id;
		choiceObj.name = metadata.name;
		if (metadata.childs != null && metadata.childs.size() > 0) {
			choiceObj.childs = toChoiceObjList(metadata.childs);
		}
		return choiceObj;
	}

	/**
	 *将Metadata  转成 ChoiceObj
	 * @param metdatas
	 * @return
	 */
	public static List<ChoiceObj> toChoiceObjList(List<Metadata> metdatas) {
		List<ChoiceObj> choiceObj = new ArrayList<ChoiceObj>();
		if (metdatas != null) {
			for (Metadata obj : metdatas) {
				choiceObj.add(toChoiceObj(obj));
			}
		}
		return choiceObj;
	}
}
