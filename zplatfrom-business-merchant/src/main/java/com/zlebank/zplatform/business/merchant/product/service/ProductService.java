/* 
 * ProductService.java  
 * 
 * version TODO
 *
 * 2016年8月24日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.merchant.product.service;

import com.zlebank.zplatform.business.merchant.enterprise.bean.ProductBalanceBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月24日 下午4:11:20
 * @since 
 */
public interface ProductService {

	
	/**
	 * 
	 * @param productCode 产品编号
	 * @param prodcutName 产品名称
	 * @param fundManager 资管人
	 * @param financier 融资人
	 * @return
	 */
	public boolean openProduct(String productCode,String prodcutName,String fundManager,String financier);
	
	/**
	 * 查询产品账户
	 * @param productCode
	 * @return
	 * @throws Exception
	 */
	public ProductBalanceBean queryBalance(String productCode) throws Exception;
}
