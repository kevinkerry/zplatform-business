/* 
 * SMSServiceTest.java  
 * 
 * version TODO
 *
 * 2016年9月1日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.rmi.commons.SMSServiceProxy;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月1日 上午10:50:14
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("/remoting-client.xml")
public class SMSServiceTest {

	@Autowired
	private SmsService smsService; 
	@Autowired
	private SMSServiceProxy smsServiceProxy;
	
	@Test
	public void test_send(){
		int verifyCodeByModuleType = smsServiceProxy.verifyCodeByModuleType("1005", "18201289505", "388375");
		System.out.println(verifyCodeByModuleType);
	}
}
