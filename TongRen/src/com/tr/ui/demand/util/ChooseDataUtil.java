package com.tr.ui.demand.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tr.model.demand.Metadata;
import com.tr.ui.organization.model.Area;

/**
 * @ClassName: ChooseDataUtil.java
 * @author fxtx
 * @Date 2015年3月12日 上午10:32:43
 * @Description: 三级多选数据处理工具 单例工具
 */
public class ChooseDataUtil {
	/**
	 * 默认情况
	 */
	public static final int CHOOSE_TYPE_DEFAULT = 0;
	/**
	 * 融资类型 2
	 */
	public static final int CHOOSE_type_InInvestType = 2;
	/**
	 * 投资类型 1
	 */
	public static final int CHOOSE_type_OutInvestType = 1;

	/**
	 * 地区
	 */
	public static final int CHOOSE_type_Area = 3;
	/**
	 * 行业
	 */
	public static final int CHOOSE_type_Trade = 4;

	/**
	 * MEtadata 的选择状态 全选
	 */
	public final static int METADATA_SELECT_ALL = 2;
	/**
	 * MEtadata 的选择状态 多选
	 */
	public final static int METADATA_SELECT_MULTI = 1;
	/**
	 * MEtadata 的选择状态 没有选择 默认情况
	 */
	public final static int METADATA_SELECT_NO = 0;


	private static ChooseDataUtil chooseDatautil;



	private ChooseDataUtil() {
		// TODO Auto-generated constructor stub
	}

	public static ChooseDataUtil getInstance() {
		if (chooseDatautil == null) {
			chooseDatautil = new ChooseDataUtil();
		}
		return chooseDatautil;
	}

	/**
	 * 
	 * @param list
	 *            :数据源 maxLength: 可以显示的文本长度
	 * @return 信息文本
	 */
	public static String dataToMetadata(Metadata list, int maxLength) {
		if (list == null) {
			return "";
		}
		int selectnum = list.selectNum;
		if (selectnum == METADATA_SELECT_ALL) {
			return list.name;// 当前自己的名称 如果是全选的话
		} else if (selectnum == METADATA_SELECT_MULTI) {
			StringBuffer sb = new StringBuffer();
			boolean isSelect = false;
			for (Metadata data : list.childs) {
				if (isSelect) {
					sb.append(",");
				}
				isSelect = true;
				sb.append(data.name);
			}
			// 判断 当前是否超过最高限
			if (sb.length() >= maxLength) {
				String value = sb.substring(0, maxLength - 1);
				sb = new StringBuffer();
				sb.append(value);
				sb.append("...");
				sb.append(" 等");
				sb.append(list.childs.size());
				sb.append("项");
			}
			return sb.toString();
		}
		return "";
	}

	/**
	 * 获取所有选择的对象 默认最大显示9个字符
	 * 
	 * @param list
	 * @return
	 */
	public static String dataToMetadata(Metadata list) {
		return dataToMetadata(list, 9);
	}

	/**
	 * 将Map 对象转换成ListView 对象
	 * 
	 * @param map
	 * @return
	 */
	public static ArrayList<Metadata> getMapToList(Map<String, Metadata> map) {
		ArrayList<Metadata> list = new ArrayList<Metadata>();
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			list.add(map.get(key));
		}
		return list;
	}

	/**
	 * 将列表对象转成Map 对象
	 * 
	 * @param list
	 * @return
	 */
	public static Map<String, Metadata> getListToMap(List<Metadata> list) {
		Map<String, Metadata> map = new HashMap<String, Metadata>();
		if (list != null) {
			for (Metadata dic : list) {
				if (dic == null) {
					continue;
				}
				map.put(dic.id, dic);
			}
		}
		return map;
	}

	/**
	 * 获取被选中的信息
	 * 
	 * @param listData
	 * @return
	 */
	public static List<Metadata> getSelectList(List<Metadata> listData) {
		List<Metadata> list = new ArrayList<Metadata>();
		if (listData != null) {
			for (Metadata data : listData) {
				if (data != null) {
					if (data.childs.size() > 0) {
						// 有二级
						for (Metadata data2 : data.childs) {
							if (data2.childs.size() > 0) {
								// 有三级
								for (Metadata data3 : data2.childs) {
									list.add(data3);
								}
							} else {
								list.add(data2);
							}
						}
					} else {
						list.add(data);
					}
				}
			}
		}
		return list;
	}

	/**
	 * 列表必须是三级形式
	 * 
	 * @param list
	 * @return
	 */
	public static String getMetadataName(List<Metadata> list, int maxLenght) {
		StringBuffer sb = new StringBuffer();
		int size = 0;
		boolean isOne = false;
		for (Metadata data : list) {

			if (data.childs.size() > 0) {
				// 有二级
				for (Metadata data2 : data.childs) {
					if (data2.childs.size() > 0) {
						// 有三级
						for (Metadata data3 : data2.childs) {
							if (isOne) {
								sb.append(",");
							}
							size++;
							isOne = true;
							sb.append(data3.name);
						}
					} else {
						if (isOne) {
							sb.append(",");
						}
						size++;
						isOne = true;
						sb.append(data2.name);
					}
				}
			} else {
				if (isOne) {
					sb.append(",");
				}
				size++;
				isOne = true;
				sb.append(data.name);
			}
		}
		if (sb.length() >= maxLenght) {
			String value = sb.substring(0, maxLenght - 1);
			sb = new StringBuffer();
			sb.append(value);
			sb.append("...");
			sb.append(" 等");
			sb.append(size);
			sb.append("项");
		}
		return sb.toString();
	}
	/**
	 * 列表必须是三级形式    
	 * 
	 * @param list
	 * @return
	 */
	public static Area getMetadataName(List<Metadata> list) {
		Area area=null;
		try {
			area = new Area();
		if(list == null){
			return area;
		}
		for (Metadata data : list) {
				// 有二级
			for (Metadata data2 : data.childs) {
					// 有三级
				for (Metadata data3 : data2.childs) {
					area.county = data3.name;
				}
					area.city = data2.name;
			}
			area.province = data.name;
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return area;
	}
	
}
