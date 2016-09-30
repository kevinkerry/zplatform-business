package com.zlebank.zplatform.business.individual.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.business.individual.bean.enums.AccTradeExcepitonEnum;
import com.zlebank.zplatform.business.individual.exception.CommonException;
import com.zlebank.zplatform.business.individual.service.ICreditAccountService;
import com.zlebank.zplatform.rmi.trade.AccountTradeServiceProxy;
import com.zlebank.zplatform.rmi.trade.CreditAccountServiceProxy;
import com.zlebank.zplatform.trade.bean.gateway.OpenAccBean;
import com.zlebank.zplatform.trade.bean.gateway.TransferOrderBean;
@Service
public class CreditAccountServiceImpl implements ICreditAccountService {

	private Log log=LogFactory.getLog(CreditAccountServiceImpl.class);
	
	@Autowired
	private CreditAccountServiceProxy creditAccountServiceProxy;
	
	@Override
	public void openCreditAccount(OpenAccBean accoutInfo)
			throws CommonException {
		try {
			this.creditAccountServiceProxy.openCreditAccount(accoutInfo.getMemberId(), accoutInfo.getUsage());
		} catch (Exception e) {
			e.printStackTrace();
            log.error(e.getMessage());
            String code= e.getMessage();
            throw new CommonException(code,AccTradeExcepitonEnum.getMsg(code));
		}

	}

	@Override
	public String creditAccountRecharge(TransferOrderBean order)
			throws CommonException {
		String tn = null;
		try {
			tn = this.creditAccountServiceProxy.creditAccountRecharge(order);
		} catch (Exception e) {
			e.printStackTrace();
            log.error(e.getMessage());
            String code= e.getMessage();
            throw new CommonException(code,AccTradeExcepitonEnum.getMsg(code));
		}
		return tn;
	}

	@Override
	public String creditAccountConsume(TransferOrderBean order)
			throws CommonException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String creditAccountRefund(TransferOrderBean order)
			throws CommonException {
		// TODO Auto-generated method stub
		return null;
	}

}
