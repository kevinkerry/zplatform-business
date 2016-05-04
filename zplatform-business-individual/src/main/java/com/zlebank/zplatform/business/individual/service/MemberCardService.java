package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.business.individual.bean.BankCardInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.bean.SupportedBankCardType;
import com.zlebank.zplatform.business.individual.exception.SmsCodeVerifyFailException;
import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.exception.UnbindBankFailedException;
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
	 * @throws IllegalAccessException 
	 */
	public PagedResult<BankCardInfo> queryBankCard(String memberId,String cardType,String devId,int page,int pageSize) throws IllegalAccessException;
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
	public String bindBankCard(Member individualMember,
            BankCardInfo bankCardInfo,
            String smsCode) throws SmsCodeVerifyFailException;
	/**
	 * 解绑银行卡 Unbind a bank card 
	 * @param memberId 会员号
	 * @param bindcardid 绑卡标示
	 * @return
	 * @throws UnbindBankFailedException 
	 * @throws DataCheckFailedException 
	 */
	public boolean unbindBankCard(String memberId, String bindcardid, String payPwd) throws DataCheckFailedException, UnbindBankFailedException;
	/**
	 * 查询支持交易的银行卡列表  Query list of bank
	 * @return
	 */
	public PagedResult<SupportedBankCardType> queryBank(int page, int pageSize);

	
}
