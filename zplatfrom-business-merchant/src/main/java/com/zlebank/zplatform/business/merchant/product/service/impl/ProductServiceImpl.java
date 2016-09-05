/* 
 * ProductServiceIml.java  
 * 
 * version TODO
 *
 * 2016年8月24日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.merchant.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.acc.bean.FinanceProductBean;
import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.business.merchant.enterprise.bean.ProductBalanceBean;
import com.zlebank.zplatform.business.merchant.enterprise.bean.enums.ExcepitonTypeEnum;
import com.zlebank.zplatform.business.merchant.exception.CommonException;
import com.zlebank.zplatform.business.merchant.product.service.ProductService;
import com.zlebank.zplatform.member.bean.FinanceProductAccountBean;
import com.zlebank.zplatform.member.bean.FinanceProductQueryBean;
import com.zlebank.zplatform.rmi.acc.IFinanceProductService;
import com.zlebank.zplatform.rmi.member.IFinanceProductAccountService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月24日 下午4:15:45
 * @since 
 */
@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private IFinanceProductService financeProductService;
	@Autowired
	private IFinanceProductAccountService financeProductAccountService;
	/**
	 *
	 * @param productCode
	 * @param prodcutName
	 * @param fundManager
	 * @param financier
	 * @return
	 */
	@Override
	public boolean openProduct(String productCode, String prodcutName,
			String fundManager, String financier) {
		FinanceProductBean bean = new FinanceProductBean();
		bean.setFinancier(financier);
		bean.setFundManager(fundManager);
		bean.setProductCode(productCode);
		bean.setProductName(prodcutName);
		try {
			financeProductService.openProduct(bean, 0L);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public ProductBalanceBean queryBalance(String productCode) throws CommonException{
		FinanceProductQueryBean bean = new FinanceProductQueryBean();
		bean.setProductCode(productCode);
		//资金账户余额
		FinanceProductAccountBean basicpayBalance = null;
		//待结算账户余额
		FinanceProductAccountBean waitsettleBalance = null;
		try {
			basicpayBalance = financeProductAccountService.queryBalance(bean, Usage.BASICPAY);
			waitsettleBalance = financeProductAccountService.queryBalance(bean, Usage.WAITSETTLE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CommonException(ExcepitonTypeEnum.ENTERPRISE_CARD.getCode(),e.getMessage());
		}
		
		ProductBalanceBean productBalanceBean = new ProductBalanceBean();
		productBalanceBean.setProductCode(productCode);
		productBalanceBean.setCapitalAccount(basicpayBalance.getBalance());
		productBalanceBean.setWaitSettlementAccount(waitsettleBalance.getBalance());
		return productBalanceBean;
		
		
	}
	

}
