package com.tr.model.demand;

import org.json.JSONObject;

import com.tr.model.page.IPageBaseItem;
import com.tr.model.page.IPageBaseItemFactory;
import com.tr.model.page.JTPage;

/**
 * @ClassName: TemplateListItem.java
 * @author fxtx
 * @Date 2015年3月6日 下午12:45:36
 * @Description: 需求载解析对象
 */
public class NeedItemListItem implements IPageBaseItemFactory {
	public JTPage jtPage;

	public JTPage getJtPage() {
		return jtPage;
	}

	public void setJtPage(JTPage jtPage) {
		this.jtPage = jtPage;
	}

	public static NeedItemListItem createFactory(JSONObject jsonObject,String jsonkey) {
		NeedItemListItem self = null;
		if (jsonObject != null) {
			self = new NeedItemListItem();
			self.jtPage = JTPage.createDemandList(jsonObject,
					jsonkey, self);
		}
		return self;
	}

	@Override
	public IPageBaseItem paserByObject(JSONObject jsonObj) {
		try {
			NeedItemData item =NeedItemData.createFactory(jsonObj);
			return item;
		} catch (Exception e) {
		}
		return null;
	}

}
