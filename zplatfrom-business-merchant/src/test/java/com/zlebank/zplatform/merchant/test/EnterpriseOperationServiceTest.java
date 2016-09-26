/* 
 * EnterpriseOperationServiceTest.java  
 * 
 * version TODO
 *
 * 2016年8月29日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.merchant.test;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
















import com.zlebank.zplatform.business.merchant.enterprise.service.EnterpriseOperationService;
import com.zlebank.zplatform.member.bean.EnterpriseBankAccountBean;
import com.zlebank.zplatform.member.bean.EnterpriseBean;
import com.zlebank.zplatform.member.bean.EnterpriseRealNameBean;
import com.zlebank.zplatform.member.bean.EnterpriseRealNameConfirmBean;
import com.zlebank.zplatform.member.bean.enums.IndustryType;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年8月29日 上午10:53:21
 * @since 
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("/remoting-client.xml")  
public class EnterpriseOperationServiceTest {

	@Autowired
	private EnterpriseOperationService enterpriseOperationService; 
	
	@Test
	@Ignore
	public void test_query(){
		EnterpriseBean enterpriseBean = enterpriseOperationService.queryEnterpriseByMemberId("200000000000810");
		System.out.println(enterpriseBean.getCellPhoneNo());
	}
	
	@Test
	public void test_regist(){
		for(int i=0 ;i<1;i++){
			EnterpriseBean enterpriseBean = new EnterpriseBean();
			enterpriseBean.setCoopInstiCode("300000000000036");
			enterpriseBean.setEnterpriseName("测试企业"+System.currentTimeMillis());
			enterpriseBean.setEmail("test@"+System.currentTimeMillis()+".com");
			enterpriseBean.setCellPhoneNo("13"+getVerifyCode());
			enterpriseBean.setProvince(110000L);
			enterpriseBean.setCity(110100L);
			enterpriseBean.setStreet(110101L);
			enterpriseBean.setTaxNo("123");
			enterpriseBean.setLicenceNo("456");
			enterpriseBean.setOrgCode("789");
			enterpriseBean.setAddress("测试地址给");
			enterpriseBean.setPostCode("100024");
			enterpriseBean.setPrimaryBusiness(IndustryType.FINANCE);
			enterpriseBean.setCorpCertType("01");
			enterpriseBean.setCorporation("联系人");
			enterpriseBean.setCorpNo("110105198610094789");
			enterpriseBean.setContact("联系人");
			enterpriseBean.setContPhone("22222222222");
			enterpriseBean.setContAddress("联系人地址");
			enterpriseBean.setContPost("100089");
			enterpriseBean.setContEmail(System.currentTimeMillis()+"@test.com");
			try {
				String registerApply = enterpriseOperationService.registerApply(enterpriseBean);
				System.out.println("【企业申请结果】"+registerApply);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private String getVerifyCode(){
	       String verifyCode = "";
	       for(int i=0;i<9;i++){
	           int x=1+(int)(Math.random()*9);
	           verifyCode+=x;
	       }
	       return verifyCode;
	   }
	
	@Test
	@Ignore
	public void test_enterpriseStatus(){
		String status = enterpriseOperationService.queryEnterpriseStatus("200000000000889");
		System.out.println("【企业查询状态】"+status);
		
	}
	@Test
	@Ignore
	public void test_realname(){
		EnterpriseRealNameBean enterpriseRealNameBean = new EnterpriseRealNameBean();
		enterpriseRealNameBean.setTxnType("04");// 交易类型
		enterpriseRealNameBean.setTxnSubType("00");// 交易子类
		enterpriseRealNameBean.setBizType("000207");// 产品类型
		enterpriseRealNameBean.setChannelType("00");// 渠道类型
		enterpriseRealNameBean.setMemberId("200000000000935");;// 企业会员ID
		enterpriseRealNameBean.setEnterpriseName("测试企业");;// 企业名称
		enterpriseRealNameBean.setLicenceNo("123456789");;// 工商营业执照号
		enterpriseRealNameBean.setCorporation("法人");;// 法人姓名
		enterpriseRealNameBean.setContactsTelNo("18600508561");;// 联系人电话
		enterpriseRealNameBean.setFrontURL("http://www.baidu.com");;// 前台通知地址
		enterpriseRealNameBean.setCoopInsti("300000000000004");;//合作机构
		enterpriseRealNameBean.setOrgCode("123456789");;// 组织机构代码
		enterpriseRealNameBean.setTaxNo("123456789");;// 企业税务登记号
		enterpriseRealNameBean.setCorpNo("110105198610079632");;// 法人代表身份证号
		enterpriseRealNameBean.setContact("联系人姓");;// 企业联系人姓名
		enterpriseRealNameBean.setAccNum("111111111111111");;// 银行账号
		enterpriseRealNameBean.setAccName("测试对公账号");;// 银行账户名
		enterpriseRealNameBean.setBankNode("102221000382");;// 银行账户开户网点(开户行号)
		
		try {
			String tn = enterpriseOperationService.realNameApply(enterpriseRealNameBean);
			System.out.println("【实名认证订单号】"+tn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void test_realname_confirm(){
		EnterpriseRealNameConfirmBean bean = new EnterpriseRealNameConfirmBean();
		bean.setMemberId("200000000000856");
		bean.setPayPWD("123456");
		bean.setSmsCode("123456");
		bean.setTn("160829000400056300");
		bean.setTxnamt("1");
		bean.setCoopInsti("300000000000027");
		
		try {
			enterpriseOperationService.realnameConfirm(bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void test_bindingcard(){
		EnterpriseBankAccountBean enterpriseBankAccountBean = new EnterpriseBankAccountBean();
		enterpriseBankAccountBean.setMemberId("200000000000856");;// 企业会员ID
		enterpriseBankAccountBean.setCoopinsti("300000000000004");;//合作机构
		enterpriseBankAccountBean.setAccNum("134111111111112");;// 银行账号
		enterpriseBankAccountBean.setAccName("测试对公账号1");;// 银行账户名
		enterpriseBankAccountBean.setBankNode("102221000382");;// 银行账户开户网点(开户行号)
		try {
			enterpriseOperationService.bindingBankAccount(enterpriseBankAccountBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void test_offline_charge(){
		
	}
	
}
