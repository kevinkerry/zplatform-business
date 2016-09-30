package com.zlebank.zplatform.business.individual.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.business.individual.bean.enums.AccTradeExcepitonEnum;
import com.zlebank.zplatform.business.individual.bean.enums.ExcepitonTypeEnum;
import com.zlebank.zplatform.business.individual.exception.CommonException;
import com.zlebank.zplatform.business.individual.service.IAccountTradeService;
import com.zlebank.zplatform.rmi.trade.AccountTradeServiceProxy;
import com.zlebank.zplatform.trade.bean.gateway.BailRechargeOrderBean;
import com.zlebank.zplatform.trade.bean.gateway.BailWithdrawOrderBean;
import com.zlebank.zplatform.trade.bean.gateway.QueryAccBean;
import com.zlebank.zplatform.trade.bean.gateway.QueryAccResultBean;
import com.zlebank.zplatform.trade.bean.gateway.TransferOrderBean;
@Service("accountTradeServiceBusi")
public class AccountTradeServiceImpl implements IAccountTradeService {
	
	private Log log=LogFactory.getLog(AccountTradeServiceImpl.class);
	
	@Autowired
	private AccountTradeServiceProxy accountTradeServiceProxy;
	@Override
	public QueryAccResultBean queryMemberBalance(QueryAccBean query)
			throws CommonException {
		QueryAccResultBean result=null;
		try { 
			result = this.accountTradeServiceProxy.queryMemberBalance(query);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            String code= e.getMessage();
            throw new CommonException(code,AccTradeExcepitonEnum.getMsg(code));
        }
		return result;
	}

	@Override
	public String transfer(TransferOrderBean order) throws CommonException {
		String tn="";
		try {
			tn=this.accountTradeServiceProxy.transfer(order);
		} catch (Exception e) {
			 e.printStackTrace();
	         log.error(e.getMessage());
	         String code= e.getMessage();
	          throw new CommonException(code,AccTradeExcepitonEnum.getMsg(code));
		} 
		return tn;
	}

	@Override
	public String bailAccountRecharge(BailRechargeOrderBean order)
			throws CommonException {
		String tn="";
		try {
			tn=this.accountTradeServiceProxy.bailAccountRecharge(order);
		} catch (Exception e) {
			 e.printStackTrace();
	         log.error(e.getMessage());
	         String code= e.getMessage();
	         throw new CommonException(code,AccTradeExcepitonEnum.getMsg(code));
		} 
		return tn;
	}

	@Override
	public String bailAccountWithdraw(BailWithdrawOrderBean order)
			throws CommonException {
		String tn="";
		try {
			tn=this.accountTradeServiceProxy.bailAccountWithdraw(order);
		} catch (Exception e) {
			 e.printStackTrace();
	         log.error(e.getMessage());
	         String code= e.getMessage();
	            throw new CommonException(code,AccTradeExcepitonEnum.getMsg(code));
		} 
		return tn;
	}

}
