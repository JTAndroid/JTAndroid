package com.tr.model.obj;

import java.io.Serializable;

import com.tr.model.page.IPageBaseItem;

public class ResourceBase extends ViewBase implements IPageBaseItem{

	private static final long serialVersionUID = 1L;
	
	protected ResourceAttribute attribute = ResourceAttribute.Platform; // 资源来源；0-金桐脑,1-"我"的
	protected ResourceType resourceType = ResourceType.Unknown;
	
	public ResourceAttribute getAttribute() {
		return attribute;
	}
	public void setAttribute(ResourceAttribute attribute) {
		this.attribute = attribute;
	}
	
	public ResourceType getResourceType() {
		return resourceType;
	}
	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}
	
	// 资源来源，金桐脑和"我"的
	public enum ResourceAttribute{
		Platform,
		My
	}
	
	// 资源类型
	public enum ResourceType{
		Requirement, Affair, People, Organization, Knowledge, Unknown;
	}
}
