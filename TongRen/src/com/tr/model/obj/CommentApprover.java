package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * 评价赞同对象
 * @author zhongshan
 *
 */
public class CommentApprover implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**用户评价id*/
	private Long ueid;
	/**评价内容*/
	private String comment;
	/**主页人是否赞同了该评价*/
	private boolean feedback;
	/**评价人mini对象*/
	private ArrayList< ApproverMiNi> approvers;
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isFeedback() {
		return feedback;
	}
	public void setFeedback(boolean feedback) {
		this.feedback = feedback;
	}
	public ArrayList<ApproverMiNi> getApprovers() {
		return approvers;
	}
	public void setApprovers(ArrayList<ApproverMiNi> approvers) {
		this.approvers = approvers;
	}
	
	
	public Long getUeid() {
		return ueid;
	}
	public void setUeid(Long ueid) {
		this.ueid = ueid;
	}
	public void initWithJson(JSONObject jsonObject) throws JSONException, MalformedURLException, ParseException {
		String str_key = null;

		str_key = "ueid";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			setUeid(jsonObject.getLong(str_key));
		}
		
		str_key = "comment";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			setComment(jsonObject.getString(str_key));
		}

		str_key = "feedback";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			setFeedback(jsonObject.getBoolean(str_key));
		}

		str_key = "listApproverMiNi";//ApproverMiNis
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			ArrayList<ApproverMiNi> approverMiNiList=new ArrayList<ApproverMiNi>();
			JSONArray approverMiNisJsArr=jsonObject.getJSONArray("listApproverMiNi");
			ApproverMiNi approverMiNi;
			for(int i=0;i<approverMiNisJsArr.length();i++){
				approverMiNi=new ApproverMiNi();
				approverMiNi.initWithJson(approverMiNisJsArr.getJSONObject(i));
				approverMiNiList.add(approverMiNi);
			}
			setApprovers(approverMiNiList);
		}
	}
	@Override
	public String toString() {
		return "CommentApprover [ueid=" + ueid + ", comment=" + comment
				+ ", feedback=" + feedback + ", approvers=" + approvers + "]";
	}
	
	
}
