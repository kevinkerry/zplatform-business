package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.exception.CommonException;
import com.zlebank.zplatform.trade.bean.gateway.BailRechargeOrderBean;
import com.zlebank.zplatform.trade.bean.gateway.BailWithdrawOrderBean;
import com.zlebank.zplatform.trade.bean.gateway.QueryAccBean;
import com.zlebank.zplatform.trade.bean.gateway.QueryAccResultBean;
import com.zlebank.zplatform.trade.bean.gateway.TransferOrderBean;
/***
 * 账户转账
 * @author SunXiaoshi
 *
 */
public interface IAccountTradeService {
	/****
	 * 分账户查询
	 * @param query
	 * @return
	 * @throws TradeException
	 */
   public QueryAccResultBean queryMemberBalance(QueryAccBean query)throws CommonException;
	
	/***
	 * 基本账户转账
	 * @param order
	 * @return
	 */
	public String transfer(TransferOrderBean order)throws CommonException;
	
	/***
	 * 保证金充值
	 * @param order
	 * @return
	 */
	public  String bailAccountRecharge(BailRechargeOrderBean order)throws CommonException;
	/****
	 *保证金体现
	 * @param order
	 * @return
	 */
	public  String bailAccountWithdraw(BailWithdrawOrderBean order)throws CommonException;
}
