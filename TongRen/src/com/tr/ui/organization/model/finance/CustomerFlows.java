/**
 * 
 */
package com.tr.ui.organization.model.finance;

import java.io.Serializable;

/**
 * 现金流量表摘要
 * @author liubang
 *
 */
public class CustomerFlows implements Serializable {
	public static final long serialVersionUID = 8728617625915470786L;
	public String date;//报告期
	public String ncfo;//经营现金流
	public String invest;//投资现金流
	public String fund;//筹资现金流
	
}
