package com.zlebank.zplatform.business.individual.service;

import java.util.List;

import com.zlebank.zplatform.business.individual.bean.Bank;
import com.zlebank.zplatform.business.individual.bean.BankCardInfo;
import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.member.bean.MemberBean;
/**
 * Member bank card service
 *  
 * @author yangying
 * @version
 * @date 2016年1月19日 下午3:15:20
 * @since
 */
public interface MemberCardService {
    /**
     * Query list of bank card which the member can use
     * @param memberId member id
     * @return
     */
	List<BankCardInfo> queryBankCard(String memberId);
	/**
	 * Query bank card info by bank card no.
	 * @param bankCardNo
	 * @return {@link CardBin}
	 */
	CardBin queryCardBin(String bankCardNo);
	/**
	 * bind a bank card to member
	 * @param individualMember
	 * @param bankCardInfo
	 * @return true if success,else false
	 */
	boolean bindBankCard(MemberBean individualMember,BankCardInfo bankCardInfo);
	/**
	 * Unbind a bank card 
	 * @param memberId
	 * @param bindcardid
	 * @return
	 */
	boolean UnbindBankCard(String memberId,String bindcardid);
	/**
	 * Query list of bank
	 * @return
	 */
	List<Bank> queryBank();
}
