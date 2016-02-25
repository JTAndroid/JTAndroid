package com.tr.model.demand;

import org.json.JSONObject;

import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.IPageBaseItemFactory;
import com.tr.model.page.JTPage;

/**
 * @ClassName: TemplateListItem.java
 * @author fxtx
 * @Date 2015年3月6日 下午12:45:36
 * @Description: 模版下载解析对象
 */
public class DemandCommentListItem implements IPageBaseItemFactory {
	public JTPage jtPage;

	public JTPage getJtPage() {
		return jtPage;
	}

	public void setJtPage(JTPage jtPage) {
		this.jtPage = jtPage;
	}

	public static DemandCommentListItem createFactory(JSONObject jsonObject) {
		DemandCommentListItem self = null;
		if (jsonObject != null) {
			self = new DemandCommentListItem();
			self.jtPage = JTPage.createDemandList(jsonObject,
					"list", self);
		}
		return self;
	}

	@Override
	public IPageBaseItem paserByObject(JSONObject jsonObj) {
		try {
			DemandComment item = DemandComment.createFactory(jsonObj);
			return item;
		} catch (Exception e) {
		}
		return null;
	}

}
