package com.tr.ui.organization.model;

import org.apache.commons.lang3.StringUtils;


public class Constants {
	public static enum TypeEnum{
		FINANCE("金融机构","1"),
		GENERAL("一般企业","2"),
		GOV("政府组织","3"),
		AGENCY("中介机构","4"),
		MEDIA("专业媒体","5"),
		NEWS("期刊报纸","6"),
		RESEARCH("研究机构","7"),
		TV("电视广播","8"),
		INTER("互联网媒体","9"),
		COMMON("通用组织","10");
		private String name;
		private String type;
		private TypeEnum(String name,String type){
			this.name=name;
			this.type=type;
		}
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public static TypeEnum getTypeEnum(String type) throws IllegalArgumentException{
			for(TypeEnum obj:TypeEnum.values()){
				if(StringUtils.equalsIgnoreCase(type, obj.getName())){
					return obj;
				}
			}
			throw new IllegalArgumentException("No enum const class");
		}
	}
}
