package com.zlebank.zplatform.business.individual.service;

import java.util.Map;

import com.zlebank.zplatform.business.individual.bean.BankCardInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.bean.SupportedBankCardType;
import com.zlebank.zplatform.business.individual.exception.CommonException;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.trade.bean.CardBinBean;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.common.page.PageVo;
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
	public CardBinBean queryCardBin(String bankCardNo);
	/**
	 * 绑定银行卡 bind a bank card to member
	 * @param member 会员信息
	 * @param bankCardInfo 银行卡信息
	 * @return
	 */
	public String bindBankCard(Member individualMember,
            BankCardInfo bankCardInfo,
            String smsCode) throws CommonException;
	/**
	 * 解绑银行卡 Unbind a bank card 
	 * @param memberId 会员号
	 * @param bindcardid 绑卡标示
	 * @return
	 * @throws UnbindBankFailedException 
	 * @throws DataCheckFailedException 
	 */
	public boolean unbindBankCard(String memberId, String bindcardid, String payPwd) throws CommonException;
	/**
	 * 查询支持交易的银行卡列表  Query list of bank
	 * @return
	 */
	public PagedResult<SupportedBankCardType> queryBank(int page, int pageSize);
	/***
	 * 查询所支持的银行卡列表 Query list of bank
	 * @param map
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageVo<SupportedBankCardType> queryCardList(Map<String, Object> map,
			Integer pageNo, Integer pageSize);
	
	/***
	 * 匿名用户绑卡  
	 * @param json
	 * @return
	 */
	public ResultBean anonymousBindCard(String json);
	
	
	
	
	

	
}
