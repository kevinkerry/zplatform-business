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
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.bean.SupportedBankCardType;
import com.zlebank.zplatform.business.individual.service.MemberCardService;
import com.zlebank.zplatform.commons.bean.CardBin;
import com.zlebank.zplatform.commons.bean.DefaultPageResult;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.dao.CardBinDao;
import com.zlebank.zplatform.member.bean.MemberBean;
import com.zlebank.zplatform.member.bean.QuickpayCustBean;
import com.zlebank.zplatform.member.bean.RealNameBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.dao.CoopInstiDAO;
import com.zlebank.zplatform.member.exception.DataCheckFailedException;
import com.zlebank.zplatform.member.exception.UnbindBankFailedException;
import com.zlebank.zplatform.member.pojo.PojoCoopInsti;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.service.MemberBankCardService;
import com.zlebank.zplatform.member.service.MemberOperationService;
import com.zlebank.zplatform.member.service.MemberService;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.sms.service.ISMSService;
import com.zlebank.zplatform.trade.model.CashBankModel;
import com.zlebank.zplatform.trade.service.ICashBankService;
import com.zlebank.zplatform.trade.service.IGateWayService;
import com.zlebank.zplatform.trade.service.IQuickpayCustService;

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
    @Autowired
    CoopInstiDAO coopInstiDAO;
    @Autowired
    private MemberService memberServiceImpl;
    @Autowired
    private MemberOperationService memberOperationServiceImpl;
    @Autowired
    private IQuickpayCustService quickpayCustService;
	
	/**
	 *
	 * @param memberId
	 * @return
	 * @throws IllegalAccessException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public PagedResult<BankCardInfo> queryBankCard(String memberId,String cardType,String devId,int page,int pageSize) throws IllegalAccessException {
		PagedResult<QuickpayCustBean> pagedResult = memberBankCardService.queryMemberBankCard(memberId, cardType,devId, page, pageSize);
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
			individualRealInfo.setDevId(custBean.getDevId());
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
		CardBin cardBin = cardBinDao.getCard(bankCardNo);
		if (cardBin == null)
		    return null;
		cardBin.setBankCode(cardBin.getBankCode()+"0000");
		return cardBin;
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
	public String bindBankCard(Member individualMember,
			BankCardInfo bankCardInfo, String smsCode) {
		int retCode = smsService.verifyCode(ModuleTypeEnum.BINDCARD,
				individualMember.getPhone(), smsCode);
		if (retCode != 1) {
			throw new RuntimeException("验证码错误");
		}
		// 查询实名认证信息
		RealNameBean bean = new RealNameBean();
		bean.setMemberId(individualMember.getMemberId());
        RealNameBean realNameInfo = memberBankCardService.queryRealNameInfo(bean );
        String realName = ""; // 真实姓名
        if ( realNameInfo != null ) 
            realName = realNameInfo.getRealname();
        String cardName = bankCardInfo.getBankCardInfo().getCustomerName(); // 绑卡真实姓名
        if (!realName.equals(cardName)) 
            throw new RuntimeException("绑卡姓名和实名信息不一致");
		QuickpayCustBean quickpayCustBean = new QuickpayCustBean();
		PojoCoopInsti pojoCoopInsti = coopInstiDAO.getByInstiCode(individualMember.getInstiCode());
		quickpayCustBean.setCustomerno(pojoCoopInsti.getInstiCode());
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
		long bindId=memberBankCardService.saveQuickPayCust(quickpayCustBean);
		return bindId+"";
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
	    PojoMember member = memberServiceImpl.getMbmberByMemberId(memberId, MemberType.INDIVIDUAL);
        if (member == null) {// 资金账户不存在
            throw new UnbindBankFailedException("会员不存在");
        }
	    MemberBean memberBean = new MemberBean();
        memberBean.setLoginName(member.getLoginName());
        memberBean.setInstiId(member.getInstiId());
        memberBean.setPhone(member.getPhone());
        memberBean.setPaypwd(payPwd);
        // 校验支付密码
        if (!memberOperationServiceImpl.verifyPayPwd(MemberType.INDIVIDUAL,  memberBean)) {
            throw new UnbindBankFailedException("支付密码不对");
        }
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

