package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.bean.Order;

public interface MemberAccountService {
	Object queryMemberAccount();
	String recharge(Order order);
	String withdraw(Order order);
	
	Object queryAccountCDList(String memberId,String accountNo);
	Object queryAccountCDDetail(String memberId,String accountNo);
}
