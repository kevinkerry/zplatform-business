package com.zlebank.zplatform.business.individual.service;

import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;

public interface SmsService {
	boolean sendSmsCode(String memberId,String phone,ModuleTypeEnum moduleType);
}
