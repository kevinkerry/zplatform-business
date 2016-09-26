/* 
 * FundManagerService.java  
 * 
 * version TODO
 *
 * 2016年8月25日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.merchant.fund.service;

import com.zlebank.zplatform.trade.bean.FinancierReimbursementBean;
import com.zlebank.zplatform.trade.bean.MerchantReimbursementBean;
import com.zlebank.zplatform.trade.bean.OffLineChargeBean;
import com.zlebank.zplatform.trade.bean.RaiseMoneyTransferBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月25日 上午10:41:07
 * @since 
 */
public interface FundManagerService {

	/**
	 * 融资人还款
	 * @param financierReimbursementBean
	 * @return
	 */
	public String financierReimbursement(FinancierReimbursementBean financierReimbursementBean);
	
	/**
	 * 募集款划转
	 * @param raiseMoneyTransferBean
	 * @return
	 */
	public String raiseMoneyTransfer(RaiseMoneyTransferBean raiseMoneyTransferBean);
	
	/**
	 * 商户还款
	 * @param merchanReimbursementBean
	 * @return
	 */
	public String merchanReimbursement(MerchantReimbursementBean merchanReimbursementBean) throws Exception;
	
	/**
	 * 线下充值订单
	 * @param offLineChargeBean
	 * @return
	 */
	public String offLineCharge(OffLineChargeBean offLineChargeBean);
}
