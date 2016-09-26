package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
/**
 * 
 * sms service
 *
 * @author yangying
 * @version
 * @date 2016年1月19日 下午3:11:42
 * @since
 */
public interface SmsService {
	/**
	 * 短信发送验证码 send a sms represented verify code to phone 
	 * @param memberId 会员号 member id
	 * @param phone 手机号 phone number
	 * @param moduleType {@link ModuleTypeEnum}
	 * @return
	 */
	public boolean sendSmsCode(String memberId,String phone,ModuleTypeEnum moduleType);
	
	/**
	 * 短信发送验证码
	 * @param json
	 * @param moduleType
	 * @return
	 */
	public boolean sendSmsCode(String json,ModuleTypeEnum moduleType);
	
	/**
	 * 生成短信验证码（不发送短信验证码）
	 * @param json
	 * @param moduleType
	 * @return
	 */
	public String generateSmsCode(String json, ModuleTypeEnum moduleType);

}