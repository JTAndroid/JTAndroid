package com.tr.model.knowledge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 栏目 对象
 * @author gushi
 *
 */
public class Column implements Serializable {
	// 栏目id
    private long id;
    // 栏目名称
    private String columnname;
    // 用户Id
    private long userId;
    // 父级id
    private long parentId;
    // 创建时间
    private String createtime;
    // 路径名称
    private String pathName;
    // 分类层级路径
    private String columnLevelPath;
    // 删除状态
    private int delStatus;
    // 更新时间
    private String updateTime;
    // 订阅数
    private Long subscribeCount;
    // 栏目类型（默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规，11：文章）
    private int type;
    // 子栏目
    private List<Column> listColumn;
    // 当前目录层级,0为根目录
    private int level;


	private static final long serialVersionUID = 1L;
	
	public Column(){
		
	}
	
	public Column(String columnname){
		this.columnname = columnname;
	}
	
	/**
	 * 构造函数（生成假数据时使用）
	 * @param columnname
	 * @param level
	 * @param id
	 * @param parentId
	 */
	public Column(String columnname, int level,long id, long parentId) {
		this.columnname = columnname;
		this.level = level;
		this.id = id;
		this.parentId = parentId;
	}

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColumnname() {
        return columnname;
    }

    public void setColumnname(String columnname) {
        this.columnname = columnname;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getColumnLevelPath() {
        return columnLevelPath;
    }

    public void setColumnLevelPath(String columnLevelPath) {
        this.columnLevelPath = columnLevelPath;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Byte delStatus) {
        this.delStatus = delStatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public long getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(Long subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public List<Column> getListColumn() {
		return listColumn;
	}

	public void setListColumn(List<Column> listColumn) {
		this.listColumn = listColumn;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * 解析Json数据
	 * @param jsonStr
	 * @return
	 * @throws JSONException 
	 */
	public static Column parseJson(String jsonStr) throws JSONException{
		Column column = new Column();
		JSONObject jsonObj = new JSONObject(jsonStr);
		String strKey = "id";
		if(jsonObj.has(strKey)){
			column.setId(jsonObj.optLong(strKey));
		}
		strKey = "columnname";
		if(jsonObj.has(strKey)){
			column.setColumnname(jsonObj.optString(strKey));
		}
		strKey = "listColumn";
		if(jsonObj.has(strKey)){
			JSONArray jsonArr = jsonObj.getJSONArray(strKey);
			if(jsonArr != null){
				List<Column> listColumn = new ArrayList<Column>();
				for(int i = 0; i < jsonArr.length(); i++){
					Column subColumn = Column.parseJson(jsonArr.getString(i).toString());
					if(subColumn != null){
						listColumn.add(subColumn);
					}
				}
				column.setListColumn(listColumn);
			}
		}
		return column;
	}
}