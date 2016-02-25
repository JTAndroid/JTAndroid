package com.tr.model.obj; 

import org.json.JSONObject;

import com.tr.model.page.IPageBaseItem;

/** 
 * @author xuxinjian
 * @E-mail: xuxinjian@gintong.com
 * @version 1.0
 * @创建时间：2014-4-30 上午10:00:20 
 * @类说明 
 */
public class SearchResult implements IPageBaseItem{
	private static final long serialVersionUID = 19811444134264422L;
	private String id;
	private String title;
	private String content;
	private String source;
	private String time;
	private String image;
	private String company;//公司
	private String industrys;//行业
	private String position;//职位
	private String area;//地域
	private int  type;//1用户,２人脉 
	private int  gender;//性别
	
	
	public static SearchResult createFactory(JSONObject jsonObject) {
		try {
			SearchResult self = new SearchResult();
			self.id = jsonObject.optString("id");
			self.title = jsonObject.optString("title");
			self.content = jsonObject.optString("content");
			self.source = jsonObject.optString("source");
			self.time = jsonObject.optString("time");
			self.image = jsonObject.optString("image");
			self.company = jsonObject.optString("company");
			self.industrys = jsonObject.optString("industrys");
			self.position = jsonObject.optString("position");
			self.area = jsonObject.optString("area");
			self.type = jsonObject.optInt("type");
			self.gender = jsonObject.optInt("gender");
			
			return self;
		} catch (Exception e) {
			return null;
		}
	}

	
	public int getGender() {
		return gender;
	}

	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public String getIndustrys() {
		return industrys;
	}


	public void setIndustrys(String industrys) {
		this.industrys = industrys;
	}


	public String getPosition() {
		return position;
	}


	public void setPosition(String position) {
		this.position = position;
	}


	public String getArea() {
		return area;
	}


	public void setArea(String area) {
		this.area = area;
	}


	public void setGender(int gender) {
		this.gender = gender;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
}