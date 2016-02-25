package com.tr.ui.flow.model;

import java.io.Serializable;

import com.tr.model.demand.ASSORPOK;

public class FlowResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public FlowASSO result;

	public FlowASSO getResult() {
		return result;
	}

	public void setResult(FlowASSO result) {
		this.result = result;
	}

}
