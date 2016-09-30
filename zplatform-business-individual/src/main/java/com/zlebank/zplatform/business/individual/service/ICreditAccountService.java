package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.exception.CommonException;
import com.zlebank.zplatform.trade.bean.gateway.BailRechargeOrderBean;
import com.zlebank.zplatform.trade.bean.gateway.BailWithdrawOrderBean;
import com.zlebank.zplatform.trade.bean.gateway.OpenAccBean;
import com.zlebank.zplatform.trade.bean.gateway.QueryAccBean;
import com.zlebank.zplatform.trade.bean.gateway.QueryAccResultBean;
import com.zlebank.zplatform.trade.bean.gateway.TransferOrderBean;
/***
 * 授信账户接口
 * 20160927
 * @author liusm
 *
 */
public interface ICreditAccountService {
	
   /****
    * 授信账户开户
    * @param accoutInfo
    * @throws CommonException
    */
   public void openCreditAccount(OpenAccBean accoutInfo)throws CommonException;
	
	/***
	 * 授信账户充值
	 * @param order
	 * @return
	 * @throws CommonException
	 */
	public String creditAccountRecharge(TransferOrderBean order)throws CommonException;
	
	/***
	 * 授信账户充值
	 * @param order
	 * @return
	 * @throws CommonException
	 */
	public  String creditAccountConsume(TransferOrderBean order)throws CommonException;
	/****
	 * 授信账户退款
	 * @param order
	 * @return
	 * @throws CommonException
	 */
	public  String creditAccountRefund(TransferOrderBean order)throws CommonException;
}
