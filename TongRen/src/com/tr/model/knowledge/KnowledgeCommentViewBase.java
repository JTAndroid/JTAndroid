package com.tr.model.knowledge;

public class KnowledgeCommentViewBase extends KnowledgeViewBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean folded; // 子项是否收起

	public boolean isFolded() {
		return folded;
	}

	public void setFolded(boolean folded) {
		this.folded = folded;
	}
}
