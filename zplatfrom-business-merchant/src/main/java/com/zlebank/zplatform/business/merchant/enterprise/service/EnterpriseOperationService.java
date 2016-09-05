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

import com.zlebank.zplatform.business.merchant.exception.CommonException;
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
	public String registerApply(EnterpriseBean enterpriseBean) throws CommonException;
	
	/**
	 * 企业实名认证申请
	 * @param enterpriseRealNameBean
	 * @return
	 */
	public String realNameApply(EnterpriseRealNameBean enterpriseRealNameBean) throws CommonException;
	
	/**
	 * 企业实名认证确认
	 * @param enterpriseRealNameConfirmBean
	 */
	public void realnameConfirm(EnterpriseRealNameConfirmBean enterpriseRealNameConfirmBean) throws CommonException;
	
	/**
	 * 企业会员绑定银行账户
	 * @param enterpriseBankAccountBean
	 */
	public void bindingBankAccount(EnterpriseBankAccountBean enterpriseBankAccountBean) throws CommonException;
	
	/**
	 * 更加企业会员号查询企业状态
	 * @param memberId
	 * @return
	 */
	public String queryEnterpriseStatus(String memberId);
}
