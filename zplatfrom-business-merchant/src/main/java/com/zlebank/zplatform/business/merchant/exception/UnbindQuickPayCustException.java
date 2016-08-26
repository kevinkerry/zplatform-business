/* 
 * UnbindQuickPayCustException.java  
 * 
 * version TODO
 *
 * 2016年4月28日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.merchant.exception;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年4月28日 上午9:45:09
 * @since 
 */
public class UnbindQuickPayCustException extends AbstractIndividualBusinessException{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2647346149786426410L;

	/**
	 *
	 * @return
	 */
	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return "EBUBI0006";
	}

}
