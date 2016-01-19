package com.zlebank.zplatform.business.individual.service;

import java.util.List;

import com.zlebank.zplatform.business.individual.bean.Bank;
import com.zlebank.zplatform.business.individual.bean.BankCardInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.commons.bean.CardBin;

public interface MemberCardService {
	List<BankCardInfo> queryBankCard(String memberId);
	CardBin queryCardBin(String bankCardNo);
	boolean bindBankCard(Member member,BankCardInfo bankCardInfo);
	boolean unBindBankCard(String memberId,String bindcardid);
	List<Bank> queryBank();
}
