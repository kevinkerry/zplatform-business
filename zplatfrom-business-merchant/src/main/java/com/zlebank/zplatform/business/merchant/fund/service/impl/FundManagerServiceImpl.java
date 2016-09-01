/* 
 * FundManagerServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年8月25日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.merchant.fund.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.business.merchant.fund.service.FundManagerService;
import com.zlebank.zplatform.rmi.trade.EnterpriseTradeServiceProxy;
import com.zlebank.zplatform.trade.bean.FinancierReimbursementBean;
import com.zlebank.zplatform.trade.bean.MerchantReimbursementBean;
import com.zlebank.zplatform.trade.bean.OffLineChargeBean;
import com.zlebank.zplatform.trade.bean.RaiseMoneyTransferBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月25日 上午11:02:25
 * @since 
 */
@Service
public class FundManagerServiceImpl implements FundManagerService{

	
	@Autowired
	private EnterpriseTradeServiceProxy enterpriseTradeServiceProxy;
	/**
	 *
	 * @param financierReimbursementBean
	 * @return
	 */
	@Override
	public String financierReimbursement(
			FinancierReimbursementBean financierReimbursementBean) {
		return enterpriseTradeServiceProxy.createFinancierOrder(financierReimbursementBean);
	}

	/**
	 *
	 * @param raiseMoneyTransferBean
	 * @return
	 */
	@Override
	public String raiseMoneyTransfer(
			RaiseMoneyTransferBean raiseMoneyTransferBean) {
		// TODO Auto-generated method stub
		return enterpriseTradeServiceProxy.createRaiseMoneyTransferOrder(raiseMoneyTransferBean);
	}

	/**
	 *
	 * @param merchanReimbursementBean
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String merchanReimbursement(
			MerchantReimbursementBean merchanReimbursementBean) throws Exception {
		// TODO Auto-generated method stub
		return enterpriseTradeServiceProxy.createMerchantReimbusementOrder(merchanReimbursementBean);
	}

	/**
	 *
	 * @param offLineChargeBean
	 * @return
	 */
	@Override
	public String offLineCharge(OffLineChargeBean offLineChargeBean) {
		// TODO Auto-generated method stub
		return enterpriseTradeServiceProxy.offLineCharge(offLineChargeBean);
	}

}
