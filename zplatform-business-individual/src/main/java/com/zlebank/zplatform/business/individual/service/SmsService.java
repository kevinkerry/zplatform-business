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
     * send a sms represented verify code to phone 
     * @param memberId member id
     * @param phone phone number
     * @param moduleType {@link ModuleTypeEnum}
     * @return
     */
	boolean sendVerifyCode(String memberId,String phone,ModuleTypeEnum moduleType);
}
