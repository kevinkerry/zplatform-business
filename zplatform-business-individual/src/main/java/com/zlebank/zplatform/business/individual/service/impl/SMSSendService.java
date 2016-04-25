/* 
 * SMSSendService.java  
 * 
 * version TODO
 *
 * 2016年1月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlebank.zplatform.business.individual.service.SmsService;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.sms.service.ISMSService;
import com.zlebank.zplatform.trade.exception.TradeException;
import com.zlebank.zplatform.trade.service.IGateWayService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年1月21日 上午10:57:00
 * @since 
 */
@Service("smsSendService")
public class SMSSendService implements SmsService{

	@Autowired
	private ISMSService smsSendService;
	@Autowired 
	private IGateWayService gateWayService;
	/**
	 *
	 * @param memberId
	 * @param phone
	 * @param moduleType
	 * @return
	 */
	@Override
	public boolean sendSmsCode(String memberId, String phone,
			ModuleTypeEnum moduleType) {
		int retcode = smsSendService.sendSMS(moduleType, phone, "", "");
		if(retcode==100||retcode==105){
			return true;
		}
		return false;
	}
	/**
	 *
	 * @param json
	 * @param moduleType
	 * @return
	 */
	@Override
	public boolean sendSmsCode(String json, ModuleTypeEnum moduleType) {
		JSONObject jsonObject =  JSON.parseObject(json);
		int retcode = 999;
		String phoneNo = null;
		switch (moduleType) {
			case BINDCARD:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType, phoneNo, "", "");
				break;
			case CHANGELOGINPWD:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType, phoneNo, "", "");
				break;
			case CHANGEPAYPWD:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType, phoneNo, "", "");
				break;
			case PAY:
				//需要bindId tn
				try {
					gateWayService.sendSMSMessage(json);
				} catch (TradeException e) {
					e.printStackTrace();
					return false;
				}
				return true;
			case REGISTER:
				phoneNo = jsonObject.get("phoneNo").toString();
				retcode = smsSendService.sendSMS(moduleType, phoneNo, "", "");
				break;
			case ACCOUNTPAY:
				phoneNo = jsonObject.get("phoneNo").toString();
				String tn = jsonObject.get("tn").toString();
				retcode = smsSendService.sendSMS(moduleType, phoneNo, tn, "");
				break;
			default:
				
				break;
		}
		if(retcode==100||retcode==105){
			return true;
		}
		return false;
	}

}
