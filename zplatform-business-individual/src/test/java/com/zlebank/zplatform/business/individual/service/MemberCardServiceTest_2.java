/* 
 * MemberCardServiceTest_2.java  
 * 
 * version TODO
 *
 * 2016年9月6日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.business.individual.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.business.individual.exception.CommonException;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.member.bean.CoopInsti;
import com.zlebank.zplatform.member.bean.MemberAccountBean;
import com.zlebank.zplatform.member.bean.QuickpayCustBean;
import com.zlebank.zplatform.rmi.member.ICoopInstiService;
import com.zlebank.zplatform.rmi.member.IMemberBankCardService;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年9月6日 下午3:43:52
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("/remoting-client.xml")
public class MemberCardServiceTest_2 {

	@Autowired
	private MemberCardService memberCardService;
	@Autowired
	private IMemberBankCardService memberBankCardService;
	@Autowired
	private MemberAccountService MemberAccountService;
	@Autowired
	private ICoopInstiService coopInstiService;
	
	@Test
	public void test_queryBankCard() {
		
			/*PagedResult<QuickpayCustBean> pagedResult = memberBankCardService.queryMemberBankCard("100000000000576", "0",null, 0, 2147483647);
			System.out.println("result: "
					+ JSON.toJSONString(pagedResult.getPagedResult()));*/
			/*MemberAccountBean queryMemberFuns = null;
			try {
				queryMemberFuns = MemberAccountService.queryMemberFuns("100000000000576");
			} catch (CommonException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(JSON.toJSONString(queryMemberFuns));*/

		CoopInsti instiByInstiCode = coopInstiService.getInstiByInstiCode("300000000000027");
		System.out.println(JSON.toJSONString(instiByInstiCode));
	}

}
