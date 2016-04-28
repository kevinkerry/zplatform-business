
package com.zlebank.zplatform.business.individual.service;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zlebank.zplatform.business.individual.bean.Bank;
import com.zlebank.zplatform.business.individual.bean.BankCardInfo;
import com.zlebank.zplatform.business.individual.bean.IndividualRealInfo;
import com.zlebank.zplatform.business.individual.bean.Member;
import com.zlebank.zplatform.business.individual.bean.Order;
import com.zlebank.zplatform.business.individual.exception.AbstractIndividualBusinessException;
import com.zlebank.zplatform.business.individual.exception.SmsCodeVerifyFailException;
import com.zlebank.zplatform.business.individual.exception.ValidateOrderException;
import com.zlebank.zplatform.business.individual.util.ApplicationContextAbled;
import com.zlebank.zplatform.commons.bean.PagedResult;
import com.zlebank.zplatform.commons.utils.DateUtil;
import com.zlebank.zplatform.sms.pojo.enums.ModuleTypeEnum;
import com.zlebank.zplatform.trade.bean.ResultBean;
import com.zlebank.zplatform.trade.exception.TradeException;

public class OrderServiceTest extends ApplicationContextAbled {
    
    
    @Ignore
    public void testCreateOrder() {
        OrderService orderService = (OrderService) getContext().getBean(
                "orderServiceImpl");
        String tn = null;
        OrderGenerator orderGenerator = null;
        try {
            // test recharge order
            orderGenerator = new RechargeOrderGenerator();
            Order order = orderGenerator.generate(false);
            tn = orderService.createOrder(order);
            System.out.println(tn);
            Assert.assertNotNull(tn);
            
            // test consume non anonymous order
            orderGenerator = new ConsumeOrderGenerator();
            tn = orderService.createOrder(orderGenerator.generate(false));
            System.out.println(tn);
            Assert.assertNotNull(tn);
            // test consume non anonymous order
            orderGenerator = new ConsumeOrderGenerator();
            tn = orderService.createOrder(orderGenerator.generate(true));
            System.out.println(tn);
            Assert.assertNotNull(tn);
        } catch (TradeException e) {
            Assert.fail(e.getMessage());
        } catch (AbstractIndividualBusinessException e) {
            Assert.fail(e.getMessage());
        } catch (ValidateOrderException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
    
   
    public void beforeBindCard(){
        SmsService smsService = (SmsService) getContext().getBean("smsSendService");
        smsService.sendSmsCode("100000000000564", "18500291365", ModuleTypeEnum.BINDCARD);
    }
    
    public void bindCard() throws SmsCodeVerifyFailException{
        MemberCardService memberCardService = (MemberCardService) getContext()
                .getBean("memberCardService");
        Member individualMember = new Member();
        individualMember.setInstiCode("25");
        individualMember.setMemberId("100000000000564");
        individualMember.setPhone("18500291365");
        BankCardInfo bankCardInfo = new BankCardInfo();

        Bank bank = new Bank();
        bank.setBankCode("03080000");
        bank.setBankName("招商银行");
        bankCardInfo.setBank(bank);
        IndividualRealInfo individualRealInfo = new IndividualRealInfo();
        individualRealInfo.setCardNo("360729198610280337");
        individualRealInfo.setCardType("1");
        individualRealInfo.setCustomerName("郭佳");
        individualRealInfo.setCertifType("01");
        individualRealInfo.setCertifNo("360729198610280337");
        individualRealInfo.setPhoneNo("18500291365");
        individualRealInfo.setCvn2("");
        individualRealInfo.setExpired("");
        bankCardInfo.setBankCardInfo(individualRealInfo);
        memberCardService
                .bindBankCard(individualMember, bankCardInfo, "674768");
    }
  /*  
    @Test
    public void testBeforePayOrder(){
        OrderService orderService = (OrderService) getContext().getBean("orderServiceImpl");
        Order order = orderService.queryOrder("100000000000564", "OTErdHyjWNBR50804521466478580");
        IGateWayService gateWayService = (IGateWayService)getContext().getBean("gateWayService");
        Assert.assertEquals(order.getStatus(), OrderStatus.INIT);
        SmsService smsService = (SmsService) getContext().getBean("smsSendService");
        smsService.sendSmsCode("100000000000564", "18500291365", ModuleTypeEnum.BINDCARD);
    }
    
    @Test
    @Ignore
    public void testPayOrder(){
        OrderService orderService = (OrderService) getContext().getBean("orderServiceImpl");
        Order order = orderService.queryOrder("100000000000564", "OTErdHyjWNBR50804521466478580");
        try {
            orderService.pay(JSON.toJSONString(order), "", "e10adc3949ba59abbe56e057f20f883e", PayWay.ACCOUNT);
        } catch (AbstractTradeDescribeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AbstractIndividualBusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TradeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }*/
    
    public void test_queryOrder(){
		OrderService orderService = (OrderService) getContext().getBean("orderServiceImpl");
		Order order = orderService.queryOrder("100000000000564", "dGVWwxSCU68267726142181273");
		System.out.println(JSON.toJSONString(order));
	}
    
	
	@Ignore
	public void test_queryOrderList(){
		OrderService orderService = (OrderService) getContext().getBean("orderServiceImpl");
		PagedResult<Order> orderList = null;
		try {
			orderList = orderService.queryOrderList("100000000000564", DateUtil.convertToDate("20160120112643", "yyyyMMddHHmmss"), DateUtil.convertToDate("20160121122643", "yyyyMMddHHmmss"), 1, 10);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(JSON.toJSONString(orderList));
	}
	
	@Test
	@Transactional
	public void test_anonymousRealName(){
		OrderService orderService = (OrderService) getContext().getBean("orderServiceImpl");
		Member individualMember = new Member();
        individualMember.setInstiCode("25");
        individualMember.setMemberId("100000000000564");
        individualMember.setPhone("18500291365");
        BankCardInfo bankCardInfo = new BankCardInfo();

        Bank bank = new Bank();
        bank.setBankCode("03080000");
        bank.setBankName("招商银行");
        bankCardInfo.setBank(bank);
        IndividualRealInfo individualRealInfo = new IndividualRealInfo();
        individualRealInfo.setCardNo("6228480028543668952");
        individualRealInfo.setCardType("1");
        individualRealInfo.setCustomerName("测试3");
        individualRealInfo.setCertifType("01");
        individualRealInfo.setCertifNo("360729198610280337");
        individualRealInfo.setPhoneNo("18500291365");
        individualRealInfo.setCvn2("");
        individualRealInfo.setExpired("");
        individualRealInfo.setDevId("1234567");
        bankCardInfo.setBankCardInfo(individualRealInfo);
		String memberId = "200000000000613";
		
		ResultBean anonymousRealName = orderService.anonymousRealName(individualRealInfo, memberId);
		System.out.println(JSON.toJSON(anonymousRealName));
	}
}
