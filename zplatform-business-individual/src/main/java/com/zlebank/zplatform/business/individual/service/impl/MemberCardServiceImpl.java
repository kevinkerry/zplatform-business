/* 
 * MemberCardService.java  
 * 
 * version TODO
 *
 * 2016年1月20日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.business.individual.bean.Bank;
import com.zlebank.zplatform.business.individual.bean.BankCardInfo;
import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.SupportedBankCardType;
import com.zlebank.zplatform.business.individual.service.MemberCardService;
import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.commons.bean.DefaultPageResult;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.dao.CardBinDao;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.QuickpayCustBean;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.exception.UnbindBankFailedException;
import com.zlebank.zplatform.member.service.MemberBankCardService;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.sms.service.ISMSService;
import com.zlebank.zplatform.trade.model.CashBankModel;
import com.zlebank.zplatform.trade.service.ICashBankService;
import com.zlebank.zplatform.trade.service.IGateWayService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年1月20日 下午1:50:40
 * @since 
 */
@Service("memberCardService")
public class MemberCardServiceImpl implements MemberCardService{

	@Autowired
	private IGateWayService gateWayService;
	@Autowired
	private MemberBankCardService memberBankCardService;
	@Autowired
	private CardBinDao cardBinDao;
	@Autowired
	private ICashBankService cashBankService;
	@Autowired
	private ISMSService smsService;
	
	/**
	 *
	 * @param memberId
	 * @return
	 * @throws IllegalAccessException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public PagedResult<BankCardInfo> queryBankCard(String memberId,String cardType,int page,int pageSize) throws IllegalAccessException {
		PagedResult<QuickpayCustBean> pagedResult = memberBankCardService.queryMemberBankCard(memberId, cardType, page, pageSize);
		System.out.println(JSON.toJSON(pagedResult.getPagedResult()));
		List<BankCardInfo> bankCardList = new ArrayList<BankCardInfo>();
		for(QuickpayCustBean custBean :pagedResult.getPagedResult()){
			BankCardInfo bankCardInfo = new BankCardInfo();
			bankCardInfo.setBindcardid(custBean.getId()+"");
			Bank bank = new Bank();
			bank.setBankCode(custBean.getBankcode());
			bank.setBankName(custBean.getBankname());
			bankCardInfo.setBank(bank);
			IndividualRealInfo individualRealInfo = new IndividualRealInfo();
			individualRealInfo.setCardNo(custBean.getCardno());
			individualRealInfo.setCardType(custBean.getCardtype());
			individualRealInfo.setCustomerName(custBean.getAccname());
			individualRealInfo.setCertifType(custBean.getIdtype());
			individualRealInfo.setCertifNo(custBean.getIdnum());
			individualRealInfo.setPhoneNo(custBean.getPhone());
			individualRealInfo.setCvn2(custBean.getCvv2());
			individualRealInfo.setExpired(custBean.getValidtime());
			bankCardInfo.setBankCardInfo(individualRealInfo);
			bankCardList.add(bankCardInfo);
		}
		return new DefaultPageResult<BankCardInfo>(bankCardList,pagedResult.getTotal());
	}

	/**
	 *
	 * @param bankCardNo
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public CardBin queryCardBin(String bankCardNo) {
		return cardBinDao.getCard(bankCardNo);
	}

	/**
	 *
	 * @param individualMember
	 * @param bankCardInfo
	 * @param smsCode
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public String bindBankCard(MemberBean individualMember,
			BankCardInfo bankCardInfo, String smsCode) {
		int retCode = smsService.verifyCode(ModuleTypeEnum.BINDCARD,
				individualMember.getPhone(), smsCode);
		if (retCode != 1) {
			throw new RuntimeException("验证码错误");
		}
		QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
		quickpayCustBean.setCustomerno(individualMember.getInstiCode());
		quickpayCustBean.setCardno(bankCardInfo.getBankCardInfo().getCardNo());
		quickpayCustBean.setCardtype(bankCardInfo.getBankCardInfo().getCardType());
		quickpayCustBean.setAccname(bankCardInfo.getBankCardInfo().getCustomerName());
		quickpayCustBean.setPhone(bankCardInfo.getBankCardInfo().getPhoneNo());
		quickpayCustBean.setIdtype(bankCardInfo.getBankCardInfo().getCertifType());
		quickpayCustBean.setIdnum(bankCardInfo.getBankCardInfo().getCertifNo());
		quickpayCustBean.setCvv2(bankCardInfo.getBankCardInfo().getCvn2());
		quickpayCustBean.setValidtime(bankCardInfo.getBankCardInfo().getExpired());
		quickpayCustBean.setRelatememberno(individualMember.getMemberId());
		quickpayCustBean.setBankcode(bankCardInfo.getBank().getBankCode());
		quickpayCustBean.setBankname(bankCardInfo.getBank().getBankName());
		memberBankCardService.saveQuickPayCust(quickpayCustBean);
		return "";
	}

	/**
	 *
	 * @param memberId
	 * @param bindcardid
	 * @param payPwd
	 * @return
	 * @throws UnbindBankFailedException 
	 * @throws DataCheckFailedException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public boolean unbindBankCard(String memberId, String bindcardid,
			String payPwd) throws DataCheckFailedException, UnbindBankFailedException {
		//校验支付密码
		QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
		quickpayCustBean.setId(Long.valueOf(bindcardid));
		quickpayCustBean.setRelatememberno(memberId);
		memberBankCardService.unbindQuickPayCust(quickpayCustBean);
		return true;
	}

	/**
	 *
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public PagedResult<SupportedBankCardType> queryBank(int page, int pageSize) {
		List<SupportedBankCardType> supportBankList = new ArrayList<SupportedBankCardType>();
		List<CashBankModel> bankList = cashBankService.findBankPage(page, pageSize);
		for(CashBankModel casebank:bankList){
			SupportedBankCardType supportedBankCardType = new SupportedBankCardType();
			supportedBankCardType.setCardType(casebank.getCardtype());
			Bank bank = new Bank();
			bank.setBankCode(casebank.getBankcode());
			bank.setBankName(casebank.getBankname());
			supportedBankCardType.setBank(bank);
			supportBankList.add(supportedBankCardType);
		}
		PagedResult<SupportedBankCardType> pagedResult = new DefaultPageResult<SupportedBankCardType>(supportBankList, cashBankService.findBankCount());
		return pagedResult;
	}
}
