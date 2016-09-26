/* 
 * ProductBalanceBean.java  
 * 
 * version TODO
 *
 * 2016年8月24日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.merchant.enterprise.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月24日 下午4:51:45
 * @since 
 */
public class ProductBalanceBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -220700217178576694L;
	private String productCode;
	private BigDecimal capitalAccount;
	private BigDecimal waitSettlementAccount;
	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}
	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	/**
	 * @return the capitalAccount
	 */
	public BigDecimal getCapitalAccount() {
		return capitalAccount;
	}
	/**
	 * @param capitalAccount the capitalAccount to set
	 */
	public void setCapitalAccount(BigDecimal capitalAccount) {
		this.capitalAccount = capitalAccount;
	}
	/**
	 * @return the waitSettlementAccount
	 */
	public BigDecimal getWaitSettlementAccount() {
		return waitSettlementAccount;
	}
	/**
	 * @param waitSettlementAccount the waitSettlementAccount to set
	 */
	public void setWaitSettlementAccount(BigDecimal waitSettlementAccount) {
		this.waitSettlementAccount = waitSettlementAccount;
	}
	
	
}
