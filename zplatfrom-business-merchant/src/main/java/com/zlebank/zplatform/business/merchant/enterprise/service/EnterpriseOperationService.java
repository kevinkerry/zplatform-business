/* 
 * EnterpriseService.java  
 * 
 * version TODO
 *
 * 2016年8月18日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.merchant.enterprise.service;

import com.zlebank.zplatform.business.merchant.exception.SmsCodeVerifyFailException;
import com.zlebank.zplatform.member.bean.EnterpriseBankAccountBean;
import com.zlebank.zplatform.member.bean.EnterpriseBean;
import com.zlebank.zplatform.member.bean.EnterpriseRealNameBean;
import com.zlebank.zplatform.member.bean.EnterpriseRealNameConfirmBean;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月18日 下午5:41:07
 * @since 
 */
public interface EnterpriseOperationService {

	/**
	 * 企业注册申请
	 * @param enterpriseBean
	 */
	public void registerApply(EnterpriseBean enterpriseBean) throws Exception;
	
	/**
	 * 企业实名认证申请
	 * @param enterpriseRealNameBean
	 * @return
	 */
	public String realNameApply(EnterpriseRealNameBean enterpriseRealNameBean) throws Exception;
	
	/**
	 * 企业实名认证确认
	 * @param enterpriseRealNameConfirmBean
	 */
	public void realnameConfirm(EnterpriseRealNameConfirmBean enterpriseRealNameConfirmBean) throws SmsCodeVerifyFailException,Exception;
	
	/**
	 * 企业会员绑定银行账户
	 * @param enterpriseBankAccountBean
	 */
	public void bindingBankAccount(EnterpriseBankAccountBean enterpriseBankAccountBean) throws Exception;
}
