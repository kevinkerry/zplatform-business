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
import org.springframework.stereotype.Service;

import com.zlebank.zplatform.business.merchant.enterprise.service.EnterpriseOperationService;
import com.zlebank.zplatform.member.bean.EnterpriseBankAccountBean;
import com.zlebank.zplatform.member.bean.EnterpriseBean;
import com.zlebank.zplatform.member.bean.EnterpriseRealNameBean;
import com.zlebank.zplatform.member.bean.EnterpriseRealNameConfirmBean;
import com.zlebank.zplatform.member.bean.enums.MemberStatusType;
import com.zlebank.zplatform.member.bean.enums.MemberType;
import com.zlebank.zplatform.member.pojo.PojoMember;
import com.zlebank.zplatform.member.pojo.PojoMemberApply;
import com.zlebank.zplatform.rmi.commons.SMSServiceProxy;
import com.zlebank.zplatform.rmi.member.IEnterpriseService;
import com.zlebank.zplatform.rmi.member.IMemberService;
import com.zlebank.zplatform.rmi.trade.EnterpriseTradeServiceProxy;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月18日 下午5:41:47
 * @since 
 */
@Service
public class EnterpriseOperationServiceImpl implements EnterpriseOperationService{

	@Autowired
	private IEnterpriseService enterpriseService;
	@Autowired
	private EnterpriseTradeServiceProxy enterpriseTradeServiceProxy;
	@Autowired
	private SMSServiceProxy smsService;
	@Autowired
	private IMemberService memberService;
	
	/**
	 *
	 * @param enterpriseBean
	 */
	@Override
	public boolean registerApply(EnterpriseBean enterpriseBean) throws Exception{
		try {
			enterpriseService.registerApply(enterpriseBean);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
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
	public void realnameConfirm(EnterpriseRealNameConfirmBean enterpriseRealNameConfirmBean) throws Exception {
		//校验短信验证码
		PojoMember enterpriseMember = memberService.getMbmberByMemberId(enterpriseRealNameConfirmBean.getMemberId(), MemberType.ENTERPRISE);
		int verifyCode = smsService.verifyCode(enterpriseMember.getPhone(), enterpriseRealNameConfirmBean.getTn(), enterpriseRealNameConfirmBean.getSmsCode());
		if(verifyCode!=1){//验证成功
			enterpriseTradeServiceProxy.realNameConfirm(enterpriseRealNameConfirmBean);
		}else{
			throw new Exception("短信验证码错误");
		}
		
	}
	
	@Override
	public void bindingBankAccount(EnterpriseBankAccountBean enterpriseBankAccountBean) throws Exception{
		enterpriseService.bindingBankAccount(enterpriseBankAccountBean);
	}

	/**
	 *
	 * @param memberId
	 * @return
	 */
	@Override
	public String queryEnterpriseStatus(String memberId) {
		PojoMemberApply member = memberService.getMemberApply(memberId);
		if(member!=null){
			return member.getStatus();
		}else{
			return MemberStatusType.NOEXIST.getCode();
		}
		
	}

}
