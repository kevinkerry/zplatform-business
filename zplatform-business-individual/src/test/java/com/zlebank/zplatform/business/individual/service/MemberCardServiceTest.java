/* 
 * MemberCardServiceTest.java  
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
import com.zlebank.zplatform.business.individual.bean.Bank;
import com.zlebank.zplatform.business.individual.bean.BankCardInfo;
import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.bean.SupportedBankCardType;
import com.zlebank.zplatform.business.individual.exception.CommonException;
import com.zlebank.zplatform.business.individual.util.ApplicationContextAbled;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年1月21日 下午3:46:27
 * @since
 */
public class MemberCardServiceTest extends ApplicationContextAbled {
	@Test
	public void test_queryBankCard() {
		MemberCardService memberCardService = (MemberCardService) getContext()
				.getBean("memberCardService");
		try {
			PagedResult<BankCardInfo> result = memberCardService.queryBankCard(
					"100000000000572", "1","1234", 0, 10);
			System.out.println("result: "
					+ JSON.toJSONString(result.getPagedResult()));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 
	public void test_queryCardBin() {
		MemberCardService memberCardService = (MemberCardService) getContext()
				.getBean("memberCardService");
		//CardBin cardbin = memberCardService.queryCardBin("123456789012");
		//System.out.println(JSON.toJSONString(cardbin));
		
	}

	public void testSMS() {
		SmsService smssendService = (SmsService) getContext().getBean(
				"smsSendService");
		smssendService.sendSmsCode("", "18600806796", ModuleTypeEnum.BINDCARD);
	}

	// @Test
	public void test_bindBankCard() throws CommonException  {
		MemberCardService memberCardService = (MemberCardService) getContext()
				.getBean("memberCardService");
		Member individualMember = new Member();
		individualMember.setInstiCode("25");
		individualMember.setMemberId("100000000000572");
		individualMember.setPhone("18600806796");
		BankCardInfo bankCardInfo = new BankCardInfo();

		Bank bank = new Bank();
		bank.setBankCode("0103");
		bank.setBankName("农业银行");
		bankCardInfo.setBank(bank);
		IndividualRealInfo individualRealInfo = new IndividualRealInfo();
		individualRealInfo.setCardNo("6228480018543668976");
		individualRealInfo.setCardType("1");
		individualRealInfo.setCustomerName("郭佳");
		individualRealInfo.setCertifType("01");
		individualRealInfo.setCertifNo("110105198610094112");
		individualRealInfo.setPhoneNo("18600806796");
		individualRealInfo.setCvn2("");
		individualRealInfo.setExpired("");
		bankCardInfo.setBankCardInfo(individualRealInfo);
		memberCardService
				.bindBankCard(individualMember, bankCardInfo, "235469");
	}

	
	public void test_unbindBankCard() throws Exception {
		MemberCardService memberCardService = (MemberCardService) getContext().getBean("memberCardService");
		
			boolean flag = memberCardService.unbindBankCard("100000000000572", "105", "654321");
			System.out.println(flag);
		
	}
//	@Test
	public void test_queryBank(){
		MemberCardService memberCardService = (MemberCardService) getContext().getBean("memberCardService");
		PagedResult<SupportedBankCardType> result=  memberCardService.queryBank(0, 10);
		System.out.println(JSON.toJSONString(result));
	}
	
	

}
