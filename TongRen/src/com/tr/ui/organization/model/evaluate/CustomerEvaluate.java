package com.tr.ui.organization.model.evaluate;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 * Title: CustomerEvaluate.java<／p>
 * <p>
 * Description: 组织客户评价<／p>
 * 
 * @author wfl
 * @date 2015-3-13
 * @version 1.0
 */
public class CustomerEvaluate implements Serializable {

	public static final long serialVersionUID = -4551695702157445101L;

	public long id;// 主键
	public long count; // 赞同个数
	public long userId; // 主页人id(组织和客户的id)
	public long createId; // 创建人id
	public String userComment;// 评论内容
	public Timestamp commentTime;// 评论时间

	public boolean evaluateStatus; // 赞同状态

	public String type;// 1:组织 2:客户
	
	
	
	
	public void initWithJson(JSONObject jsonObject) throws JSONException, MalformedURLException, ParseException {
		String str_key = null;
		

		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			id = jsonObject.getLong(str_key);
		}

		str_key = "count";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			count = jsonObject.getLong(str_key);
		}

		str_key = "userComment";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			userComment = jsonObject.getString(str_key);
		}

		str_key = "evaluateStatus";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			evaluateStatus = jsonObject.getBoolean(str_key);
		}

	}
	

	@Override
	public String toString() {
		return "CustomerEvaluate [id=" + id + ", count=" + count + ", userId="
				+ userId + ", createId=" + createId + ", userComment="
				+ userComment + ", commentTime=" + commentTime
				+ ", evaluateStatus=" + evaluateStatus + ", type=" + type + "]";
	}
	
	
	
	
}
