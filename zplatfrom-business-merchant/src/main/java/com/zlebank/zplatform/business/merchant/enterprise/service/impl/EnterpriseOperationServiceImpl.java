/* 
 * EnterpriseServiceImpl.java  
 * 
 * version TODO
 *
 * 2016年8月18日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.merchant.enterprise.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zlebank.zplatform.business.merchant.enterprise.service.EnterpriseOperationService;
import com.zlebank.zplatform.business.merchant.exception.SmsCodeVerifyFailException;
import com.zlebank.zplatform.member.bean.EnterpriseBankAccountBean;
import com.zlebank.zplatform.member.bean.EnterpriseBean;
import com.zlebank.zplatform.member.bean.EnterpriseRealNameBean;
import com.zlebank.zplatform.member.bean.EnterpriseRealNameConfirmBean;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.rmi.member.IEnterpriseService;
import com.zlebank.zplatform.rmi.member.IMemberService;
import com.zlebank.zplatform.rmi.trade.EnterpriseTradeServiceProxy;
import com.zlebank.zplatform.sms.service.ISMSService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月18日 下午5:41:47
 * @since 
 */
@Repository
public class EnterpriseOperationServiceImpl implements EnterpriseOperationService{

	@Autowired
	private IEnterpriseService enterpriseService;
	@Autowired
	private EnterpriseTradeServiceProxy enterpriseTradeServiceProxy;
	@Autowired
	private ISMSService smsService;
	@Autowired
	private IMemberService memberService;
	
	/**
	 *
	 * @param enterpriseBean
	 */
	@Override
	public void registerApply(EnterpriseBean enterpriseBean) throws Exception{
		enterpriseService.registerApply(enterpriseBean);
	}

	/**
	 *
	 * @param enterpriseRealNameBean
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String realNameApply(EnterpriseRealNameBean enterpriseRealNameBean) throws Exception {
		return enterpriseTradeServiceProxy.createEnterpriseRealNameOrder(enterpriseRealNameBean);
	}

	/**
	 *
	 * @param enterpriseRealNameConfirmBean
	 * @throws SmsCodeVerifyFailException 
	 */
	@Override
	public void realnameConfirm(EnterpriseRealNameConfirmBean enterpriseRealNameConfirmBean) throws SmsCodeVerifyFailException,Exception {
		//校验短信验证码
		PojoMember enterpriseMember = memberService.getMbmberByMemberId(enterpriseRealNameConfirmBean.getMemberId(), MemberType.ENTERPRISE);
		int verifyCode = smsService.verifyCode(enterpriseMember.getPhone(), enterpriseRealNameConfirmBean.getTn(), enterpriseRealNameConfirmBean.getSmsCode());
		if(verifyCode==1){//验证成功
			enterpriseTradeServiceProxy.realNameConfirm(enterpriseRealNameConfirmBean);
		}else{
			throw new SmsCodeVerifyFailException();
		}
		
	}
	
	@Override
	public void bindingBankAccount(EnterpriseBankAccountBean enterpriseBankAccountBean) throws Exception{
		enterpriseService.bindingBankAccount(enterpriseBankAccountBean);
	}

}
