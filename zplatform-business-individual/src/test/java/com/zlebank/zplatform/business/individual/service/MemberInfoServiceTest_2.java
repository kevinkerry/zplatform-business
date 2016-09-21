/* 
 * MemberInfoServiceTest_2.java  
 * 
 * version TODO
 *
 * 2016年9月2日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.exception.CommonException;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月2日 上午9:31:08
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("/remoting-client.xml")
public class MemberInfoServiceTest_2 {
	@Autowired
	private SmsService smsService; 
	@Autowired
	private MemberInfoService memberInfoService;
	
	
	@Test
	@Ignore
	public void test_querymembere(){
		
		Member member = memberInfoService.queryMember("wjf018", "300000000000004");
		System.out.println(JSON.toJSONString(member));
	}
	
	
	@Test
	//@Ignore
	public void test_register(){
		try {
        	Member registerMemberInfo = new Member();
        	//loginName/pwd/phone/instiCode)
        	registerMemberInfo.setLoginName("guojia_093");
        	registerMemberInfo.setPwd("123456guojia");
        	registerMemberInfo.setPhone("18500826798");
        	registerMemberInfo.setInstiCode("300000000000006");
        	String memberId = memberInfoService.register(registerMemberInfo, "537543");
        	System.out.println(memberId);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	
	@Test
	@Ignore
	public void test_vaildateUnbindPhone(){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("phoneNo", "13654608889");
		String smsCode = smsService.generateSmsCode(JSON.toJSONString(jsonMap), ModuleTypeEnum.UNBINDPHONE);
		System.out.println("smsCode:"+smsCode);
		try {
			boolean vaildateUnbindPhone = memberInfoService.vaildateUnbindPhone("100000000000576", "13654608889", "123456", smsCode);
			System.out.println(vaildateUnbindPhone);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void test_modifyPhone(){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("phoneNo", "18600806796");
		String smsCode = smsService.generateSmsCode(JSON.toJSONString(jsonMap), ModuleTypeEnum.BINDPHONE);
		System.out.println("smsCode:"+smsCode);
		try {
			memberInfoService.modifyPhone("100000000000576", "18600806796", smsCode);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	@Ignore
	public void test_vaildateBankCardForResetPwd(){
		long bindId = 175;
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("phoneNo", "18600806796");
		String smsCode = smsService.generateSmsCode(JSON.toJSONString(jsonMap), ModuleTypeEnum.RESETPAYPWD);
		System.out.println("smsCode:"+smsCode);
		try {
			memberInfoService.vaildateBankCardForResetPwd("100000000000576", "18600806796", smsCode, bindId, "6228480018543668976");
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void test_vaildateBankCardForModifyPhone(){
		long bindId = 175;
		try {
			memberInfoService.vaildateBankCardForModifyPhone("100000000000576", bindId, "6228480018543668976", "110105198610094112", "123456");
		}catch (CommonException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getCode());
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
}
