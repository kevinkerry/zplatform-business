/* 
 * Member.java  
 * 
 * version TODO
 *
 * 2016年1月21日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.bean.enums.RealNameTypeEnum;
import com.zlebank.zplatform.business.individual.util.ApplicationContextAbled;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年1月21日 上午11:18:26
 * @since 
 */
public class MemberInfoServiceTest extends ApplicationContextAbled{

	//@Test
    public void test(){
        MemberInfoService memberInfoService = (MemberInfoService)getContext().getBean("memberInfoService"); 
        try {
        	Member registerMemberInfo = new Member();
        	//loginName/pwd/phone/instiCode)
        	registerMemberInfo.setLoginName("guojia_101");
        	registerMemberInfo.setPwd("123456guojia");
        	registerMemberInfo.setPhone("18600806795");
        	registerMemberInfo.setInstiCode("300000000000006");
        	String memberId = memberInfoService.register(registerMemberInfo, "537543");
        	System.out.println(memberId);
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
  // @Test
    public void testSMS(){
    	SmsService smssendService = (SmsService) getContext().getBean("smsSendService"); 
    	smssendService.sendSmsCode("", "18600806796", ModuleTypeEnum.BINDCARD);
    }
    //@Test
	public void test_queryMemebr(){
		MemberInfoService memberInfoService = (MemberInfoService)getContext().getBean("memberInfoService"); 
		Member member = memberInfoService.queryMember("guojia", "300000000000027");
		System.out.println(JSON.toJSONString(member));
	}
	//@Test
	public void test_login(){
		MemberInfoService memberInfoService = (MemberInfoService)getContext().getBean("memberInfoService"); 
		try {
			String memberid = memberInfoService.login("guojia", "123456guojia", "300000000000027");
			System.out.println(memberid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void test_resetPayPwd(){
		MemberInfoService memberInfoService = (MemberInfoService)getContext().getBean("memberInfoService"); 
		try {
			boolean flag = memberInfoService.resetPayPwd("100000000000572", "123456", "628954");
			System.out.println(flag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//@Test
	public void test_vaildatePayPwd(){
		MemberInfoService memberInfoService = (MemberInfoService)getContext().getBean("memberInfoService"); 
		try {
			boolean flag = memberInfoService.vaildatePayPwd("100000000000572", "654321");
			System.out.println(flag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void test_modifyPwd(){
		MemberInfoService memberInfoService = (MemberInfoService)getContext().getBean("memberInfoService"); 
		try {
			boolean flag = memberInfoService.modifyPwd("100000000000572", "123456guojia", "54321guojia");
			System.out.println(flag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//@Test
	public void test_modifyPayPwd(){
		MemberInfoService memberInfoService = (MemberInfoService)getContext().getBean("memberInfoService");
		try {
			boolean flag = memberInfoService.modifyPayPwd("100000000000572", "123456", "654321");
			System.out.println(flag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Test
	public void test_resetPwd(){
		try {
			MemberInfoService memberInfoService = (MemberInfoService)getContext().getBean("memberInfoService");
			boolean flag = memberInfoService.resetPwd("100000000000572", "123456guojia", "121495");
			System.out.println(flag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	@Test
	public void test_realName(){
		MemberInfoService memberInfoService = (MemberInfoService)getContext().getBean("memberInfoService");
		IndividualRealInfo individualRealInfo = new IndividualRealInfo("6228480018543668976", "1", "郭佳", "01", "110105198610094112", "18600806796", "", "");
		try {
			boolean flag = memberInfoService.realName(individualRealInfo, "679519", "654321", "100000000000572",RealNameTypeEnum.CARDREALNAME);
			System.out.println(flag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	//@Test
	public void test_vaildatePwd(){
		MemberInfoService memberInfoService = (MemberInfoService)getContext().getBean("memberInfoService");
		try {
			boolean flag = memberInfoService.vaildatePwd("100000000000572", "123456guojia");
			System.out.println(flag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
