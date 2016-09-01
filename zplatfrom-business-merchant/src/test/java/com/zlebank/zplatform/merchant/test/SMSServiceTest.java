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
package com.zlebank.zplatform.merchant.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zlebank.zplatform.rmi.commons.SMSServiceProxy;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月1日 上午10:45:22
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("/remoting-client.xml")
public class SMSServiceTest {

	@Autowired
	private SMSServiceProxy smsServiceProxy;
	
	@Test
	public void test_sendsms(){
		String phoneNo = "18600806796";
		//smsServiceProxy.sendSMS(ModuleTypeEnum.ENTERPRISEREALNAME.getCode(), phoneNo, "", "");
		//smsServiceProxy.sendSMS(moduleType, phoneNo, orderNo, args)
	}
}
