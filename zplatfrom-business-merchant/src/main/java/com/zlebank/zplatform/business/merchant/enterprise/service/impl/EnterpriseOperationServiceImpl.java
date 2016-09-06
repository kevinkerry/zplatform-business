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

import com.zlebank.zplatform.business.merchant.enterprise.bean.enums.ExcepitonTypeEnum;
import com.zlebank.zplatform.business.merchant.enterprise.service.EnterpriseOperationService;
import com.zlebank.zplatform.business.merchant.exception.CommonException;
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
	public String registerApply(EnterpriseBean enterpriseBean) throws CommonException{
		try {
			String memberId = enterpriseService.registerApply(enterpriseBean);
			return memberId;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CommonException(ExcepitonTypeEnum.ENTERPRISE_INFO.getCode(),e.getMessage());
		}
	}

	/**
	 *
	 * @param enterpriseRealNameBean
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String realNameApply(EnterpriseRealNameBean enterpriseRealNameBean) throws CommonException {
		try {
			return enterpriseTradeServiceProxy.createEnterpriseRealNameOrder(enterpriseRealNameBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CommonException(ExcepitonTypeEnum.ENTERPRISE_INFO.getCode(),e.getMessage());
		}
	}

	/**
	 *
	 * @param enterpriseRealNameConfirmBean
	 * @throws SmsCodeVerifyFailException 
	 */
	@Override
	public void realnameConfirm(EnterpriseRealNameConfirmBean enterpriseRealNameConfirmBean) throws CommonException {
		//校验短信验证码
		PojoMember enterpriseMember = memberService.getMbmberByMemberId(enterpriseRealNameConfirmBean.getMemberId(), MemberType.ENTERPRISE);
		int verifyCode = smsService.verifyCode(enterpriseMember.getPhone(), enterpriseRealNameConfirmBean.getTn(), enterpriseRealNameConfirmBean.getSmsCode());
		if(verifyCode==1){//验证成功
			try {
				enterpriseTradeServiceProxy.realNameConfirm(enterpriseRealNameConfirmBean);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new CommonException(ExcepitonTypeEnum.ENTERPRISE_INFO.getCode(),e.getMessage());
			}
		}else{
			throw new CommonException(ExcepitonTypeEnum.PASSWORD.getCode(),"短信验证码错误");
		}
		
	}
	
	@Override
	public void bindingBankAccount(EnterpriseBankAccountBean enterpriseBankAccountBean) throws CommonException{
		try {
			enterpriseService.bindingBankAccount(enterpriseBankAccountBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CommonException(ExcepitonTypeEnum.ENTERPRISE_CARD.getCode(),e.getMessage());
		}
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

	/**
	 *
	 * @param memberId
	 * @return
	 */
	@Override
	public EnterpriseBean queryEnterpriseByMemberId(String memberId) {
		EnterpriseBean enterpris = enterpriseService.getEnterpriseByMemberId(memberId);
		return enterpris;
	}

}
