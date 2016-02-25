/**
 * 
 */
package com.tr.ui.organization.model.finance;

import java.io.Serializable;

/**
 * 资产负债表摘要
 * @author liubang
 *
 */
public class CustomerBalance implements Serializable {
	public static final long serialVersionUID = 5321789520647926957L;
	public String date;//报告期
	public String totalAssets;//总资产
	public String currentAssets;//流动资产
	public String fixedAssets;//固定资产
	public String intangibleAssets;//无形资产
	public String currentLiab;//流动负债
	public String equity;//股东权益
}
