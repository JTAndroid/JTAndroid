package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.security.Timestamp;
import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户评价对象
 * 
 * @author zhongshan
 * @since 2015-01-14
 */
public class UserComment implements Serializable {
	/** 评论id */
	private long id;
	/** 赞同数量 */
	private long count;
	/** 评价内容 */
	private String userComment;
	/** 当前用户是否赞同 */
	public boolean evaluateStatus;

	// /**主页人id 对移动端无效，无需考虑*/
	// private long userId;
	// /**创建人id对移动端无效，无需考虑*/
	// private long createId;
	// /**评论时间对移动端无效，无需考虑*/
	// private Timestamp commentTime;

	public UserComment() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}

	public boolean isEvaluateStatus() {
		return evaluateStatus;
	}

	/**
	 * 
	 * @param evaluateStatus
	 *            true 自己已经赞同不可以再评价 false 自己未赞同可以赞同评价
	 */
	public void setEvaluateStatus(boolean evaluateStatus) {
		this.evaluateStatus = evaluateStatus;
	}

	public void initWithJson(JSONObject jsonObject) throws JSONException, MalformedURLException, ParseException {
		String str_key = null;

		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			setId(jsonObject.getLong(str_key));
		}

		str_key = "count";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			setCount(jsonObject.getLong(str_key));
		}

		str_key = "userComment";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			setUserComment(jsonObject.getString(str_key));
		}

		str_key = "evaluateStatus";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			setEvaluateStatus(jsonObject.getBoolean(str_key));
		}

	}

}
