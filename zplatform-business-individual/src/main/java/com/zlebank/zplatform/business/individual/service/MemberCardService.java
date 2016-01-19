package com.zlebank.zplatform.business.individual.service;

import java.util.List;

import com.zlebank.zplatform.business.individual.bean.Bank;
import com.zlebank.zplatform.business.individual.bean.BankCardInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
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
	 * 查询会员绑定的银行卡 Query list of bank card which the member can use
	 * @param memberId 会员号
	 * @return
	 */
	public List<BankCardInfo> queryBankCard(String memberId);
	/**
	 * 查询银行卡bin信息 Query bank card info by bank card no.
	 * @param bankCardNo 银行卡号
	 * @return
	 */
	public CardBin queryCardBin(String bankCardNo);
	/**
	 * 绑定银行卡 bind a bank card to member
	 * @param member 会员信息
	 * @param bankCardInfo 银行卡信息
	 * @return
	 */
	public boolean bindBankCard(Member member,BankCardInfo bankCardInfo);
	/**
	 * 解绑银行卡 Unbind a bank card 
	 * @param memberId 会员号
	 * @param bindcardid 绑卡标示
	 * @return
	 */
	public boolean unBindBankCard(String memberId,String bindcardid);
	/**
	 * 查询支持交易的银行卡列表  Query list of bank
	 * @return
	 */
	public List<Bank> queryBank();
}
