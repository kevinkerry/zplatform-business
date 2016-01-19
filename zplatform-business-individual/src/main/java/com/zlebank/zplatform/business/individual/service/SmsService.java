package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;

public interface SmsService {
	
	/**
	 * 短信发送验证码
	 * @param memberId 会员号
	 * @param phone 手机号
	 * @param moduleType
	 * @return
	 */
	public boolean sendSmsCode(String memberId,String phone,ModuleTypeEnum moduleType);
}
